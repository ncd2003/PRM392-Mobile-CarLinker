package com.example.prm392_mobile_carlinker.data.repository;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleListResponse;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleRequest;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleResponse;
import com.example.prm392_mobile_carlinker.data.remote.ApiService;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;
import com.example.prm392_mobile_carlinker.util.FileUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleRepository {
    private final ApiService apiService;

    public VehicleRepository() {
        apiService = RetrofitClient.getApiService();
    }

    private RequestBody toForm(String s) {
        // Sử dụng text/plain là chuẩn cho các phần dữ liệu văn bản (non-file parts)
        return RequestBody.create(s == null ? "" : s, MediaType.parse("text/plain"));
    }

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri, Context context) {
        if (fileUri == null) return null;
        try {
            String path = FileUtils.getPath(context, fileUri);

            // 1. Kiểm tra nếu path là null (Lỗi phổ biến nhất)
            if (path == null) {
                System.err.println("File URI conversion failed: path is null.");
                // Thay thế bằng logic tạo file tạm thời nếu lỗi xảy ra.
                return null;
            }

            File file = new File(path);

            // 2. Kiểm tra File có tồn tại và kích thước hợp lệ không
            if (!file.exists() || file.length() == 0) {
                System.err.println("File does not exist or is empty at: " + path);
                return null;
            }

            // Lấy MIME type động hơn
            String mimeType = context.getContentResolver().getType(fileUri);
            if (mimeType == null) mimeType = "image/*";

            RequestBody requestFile = RequestBody.create(file, MediaType.parse(mimeType));

            // Tên part phải khớp với tên trong ApiService: "imageFile"
            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);

        } catch (Exception e) {
            e.printStackTrace();
            // In ra lỗi để debug
            System.err.println("Error preparing file part: " + e.getMessage());
            return null;
        }
    }

    public LiveData<Result<VehicleListResponse>> getAllVehicles() {
        MutableLiveData<Result<VehicleListResponse>> result = new MutableLiveData<>();
        result.setValue(Result.loading(null));
        apiService.getAllVehicles().enqueue(new Callback<VehicleListResponse>() {
            @Override
            public void onResponse(Call<VehicleListResponse> call, Response<VehicleListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Result.success(response.body()));
                } else {
                    result.setValue(Result.error("Error: " + response.message(), null));
                }
            }
            @Override
            public void onFailure(Call<VehicleListResponse> call, Throwable t) {
                result.setValue(Result.error("Network error: " + t.getMessage(), null));
            }
        });
        return result;
    }


    public LiveData<Result<VehicleResponse>> getVehicleById(int id) {
        MutableLiveData<Result<VehicleResponse>> result = new MutableLiveData<>();
        result.setValue(Result.loading(null));
        apiService.getVehicleById(id).enqueue(new Callback<VehicleResponse>() {
            @Override
            public void onResponse(Call<VehicleResponse> call, Response<VehicleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Result.success(response.body()));
                } else {
                    result.setValue(Result.error("Error: " + response.message(), null));
                }
            }
            @Override
            public void onFailure(Call<VehicleResponse> call, Throwable t) {
                result.setValue(Result.error("Network error: " + t.getMessage(), null));
            }
        });
        return result;
    }
    public LiveData<Result<VehicleResponse>> addVehicle(Context context, VehicleRequest req) {
        MutableLiveData<Result<VehicleResponse>> live = new MutableLiveData<>();
        live.setValue(Result.loading(null));

        // Đã sử dụng hàm toForm() đã sửa lỗi
        RequestBody rbLicense = toForm(req.getLicensePlate());
        RequestBody rbFuel = toForm(req.getFuelType());
        RequestBody rbTrans = toForm(req.getTransmissionType());
        RequestBody rbBrand = toForm(req.getBrand());
        RequestBody rbModel = toForm(req.getModel());
        RequestBody rbYear = toForm(String.valueOf(req.getYear()));

        MultipartBody.Part imagePart = prepareFilePart("imageFile", req.getImageUri(), context);

        // Xử lý trường hợp không có ảnh: Server có thể yêu cầu ảnh là bắt buộc.
        if (imagePart == null && req.getImageUri() != null) {
            live.postValue(Result.error("Lỗi: Không thể đọc file ảnh đã chọn.", null));
            return live;
        }

        Call<VehicleResponse> call = apiService.addVehicle(
                rbLicense, rbFuel, rbTrans, rbBrand, rbModel, rbYear, imagePart
        );

        call.enqueue(new Callback<VehicleResponse>() {
            @Override
            public void onResponse(Call<VehicleResponse> call, Response<VehicleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    live.postValue(Result.success(response.body()));
                } else {
                    // Cung cấp thông tin code lỗi để debug
                    String errorMsg = "Thêm thất bại. Mã lỗi: " + response.code();
                    // Nếu lỗi 400, thường do thiếu file/data sai
                    if (response.code() == 400) {
                        errorMsg += " (Bad Request - Kiểm tra yêu cầu data/file)";
                    }
                    live.postValue(Result.error(errorMsg, null));
                }
            }

            @Override
            public void onFailure(Call<VehicleResponse> call, Throwable t) {
                live.postValue(Result.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });

        return live;
    }

    public LiveData<Result<VehicleResponse>> updateVehicle(Context context, int id, VehicleRequest req) {
        MutableLiveData<Result<VehicleResponse>> live = new MutableLiveData<>();
        live.setValue(Result.loading(null));

        RequestBody rbLicense = toForm(req.getLicensePlate());
        RequestBody rbFuel = toForm(req.getFuelType());
        RequestBody rbTrans = toForm(req.getTransmissionType());
        RequestBody rbBrand = toForm(req.getBrand());
        RequestBody rbModel = toForm(req.getModel());
        RequestBody rbYear = toForm(String.valueOf(req.getYear()));

        MultipartBody.Part imagePart = prepareFilePart("imageFile", req.getImageUri(), context);

        apiService.updateVehicle(id, rbLicense, rbFuel, rbTrans, rbBrand, rbModel, rbYear, imagePart)
                .enqueue(new Callback<VehicleResponse>() {
                    @Override
                    public void onResponse(Call<VehicleResponse> call, Response<VehicleResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            live.postValue(Result.success(response.body()));
                        } else {
                            live.postValue(Result.error("Error: " + response.code(), null));
                        }
                    }

                    @Override
                    public void onFailure(Call<VehicleResponse> call, Throwable t) {
                        live.postValue(Result.error("Network error: " + t.getMessage(), null));
                    }
                });

        return live;
    }

    public LiveData<Result<VehicleResponse>> deleteVehicle(int id) {
        MutableLiveData<Result<VehicleResponse>> result = new MutableLiveData<>();
        result.setValue(Result.loading(null));
        apiService.deleteVehicle(id).enqueue(new Callback<VehicleResponse>() {
            @Override
            public void onResponse(Call<VehicleResponse> call, Response<VehicleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Result.success(response.body()));
                } else {
                    result.setValue(Result.error("Error: " + response.message(), null));
                }
            }
            @Override
            public void onFailure(Call<VehicleResponse> call, Throwable t) {
                result.setValue(Result.error("Network error: " + t.getMessage(), null));
            }
        });
        return result;
    }
}
