package com.example.prm392_mobile_carlinker.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prm392_mobile_carlinker.data.model.garagestaff.GarageStaffCreateRequest;
import com.example.prm392_mobile_carlinker.data.model.garagestaff.GarageStaffDetailResponse;
import com.example.prm392_mobile_carlinker.data.model.garagestaff.GarageStaffDto;
import com.example.prm392_mobile_carlinker.data.model.garagestaff.GarageStaffListResponse;
import com.example.prm392_mobile_carlinker.data.model.garagestaff.GarageStaffUpdateRequest;
import com.example.prm392_mobile_carlinker.data.model.cart.BaseResponse;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GarageStaffRepository {
    private static final String TAG = "GarageStaffRepository";

    /**
     * Lấy danh sách nhân viên garage
     */
    public LiveData<Resource<List<GarageStaffDto>>> getAllGarageStaff(int page, int size, String sortBy, boolean isAsc) {
        MutableLiveData<Resource<List<GarageStaffDto>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        RetrofitClient.getApiService().getAllGarageStaff(page, size, sortBy, isAsc)
                .enqueue(new Callback<GarageStaffListResponse>() {
                    @Override
                    public void onResponse(Call<GarageStaffListResponse> call, Response<GarageStaffListResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            GarageStaffListResponse.PaginatedData data = response.body().getData();
                            if (data != null && data.getItems() != null) {
                                result.setValue(Resource.success(data.getItems()));
                            } else {
                                result.setValue(Resource.error("Không có dữ liệu", null));
                            }
                        } else {
                            result.setValue(Resource.error("Lỗi: " + response.code(), null));
                        }
                    }

                    @Override
                    public void onFailure(Call<GarageStaffListResponse> call, Throwable t) {
                        Log.e(TAG, "Error getting garage staff list", t);
                        result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
                    }
                });

        return result;
    }

    /**
     * Lấy chi tiết nhân viên theo ID
     */
    public LiveData<Resource<GarageStaffDto>> getGarageStaffById(int id) {
        MutableLiveData<Resource<GarageStaffDto>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        RetrofitClient.getApiService().getGarageStaffById(id)
                .enqueue(new Callback<GarageStaffDetailResponse>() {
                    @Override
                    public void onResponse(Call<GarageStaffDetailResponse> call, Response<GarageStaffDetailResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            result.setValue(Resource.success(response.body().getData()));
                        } else {
                            result.setValue(Resource.error("Lỗi: " + response.code(), null));
                        }
                    }

                    @Override
                    public void onFailure(Call<GarageStaffDetailResponse> call, Throwable t) {
                        Log.e(TAG, "Error getting garage staff detail", t);
                        result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
                    }
                });

        return result;
    }

    /**
     * Tạo nhân viên mới
     */
    public LiveData<Resource<GarageStaffDto>> createGarageStaff(GarageStaffCreateRequest request) {
        MutableLiveData<Resource<GarageStaffDto>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        RetrofitClient.getApiService().createGarageStaff(request)
                .enqueue(new Callback<GarageStaffDetailResponse>() {
                    @Override
                    public void onResponse(Call<GarageStaffDetailResponse> call, Response<GarageStaffDetailResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            result.setValue(Resource.success(response.body().getData()));
                        } else {
                            String errorMsg = "Lỗi: " + response.code();
                            try {
                                if (response.errorBody() != null) {
                                    errorMsg = response.errorBody().string();
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing error body", e);
                            }
                            result.setValue(Resource.error(errorMsg, null));
                        }
                    }

                    @Override
                    public void onFailure(Call<GarageStaffDetailResponse> call, Throwable t) {
                        Log.e(TAG, "Error creating garage staff", t);
                        result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
                    }
                });

        return result;
    }

    /**
     * Cập nhật thông tin nhân viên
     */
    public LiveData<Resource<GarageStaffDto>> updateGarageStaff(int id, GarageStaffUpdateRequest request) {
        MutableLiveData<Resource<GarageStaffDto>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        RetrofitClient.getApiService().updateGarageStaff(id, request)
                .enqueue(new Callback<GarageStaffDetailResponse>() {
                    @Override
                    public void onResponse(Call<GarageStaffDetailResponse> call, Response<GarageStaffDetailResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            result.setValue(Resource.success(response.body().getData()));
                        } else {
                            result.setValue(Resource.error("Lỗi: " + response.code(), null));
                        }
                    }

                    @Override
                    public void onFailure(Call<GarageStaffDetailResponse> call, Throwable t) {
                        Log.e(TAG, "Error updating garage staff", t);
                        result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
                    }
                });

        return result;
    }

    /**
     * Cập nhật ảnh nhân viên
     */
    public LiveData<Resource<GarageStaffDto>> updateGarageStaffImage(int id, File imageFile) {
        MutableLiveData<Resource<GarageStaffDto>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile", imageFile.getName(), requestFile);

        RetrofitClient.getApiService().updateGarageStaffImage(id, body)
                .enqueue(new Callback<GarageStaffDetailResponse>() {
                    @Override
                    public void onResponse(Call<GarageStaffDetailResponse> call, Response<GarageStaffDetailResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            result.setValue(Resource.success(response.body().getData()));
                        } else {
                            result.setValue(Resource.error("Lỗi: " + response.code(), null));
                        }
                    }

                    @Override
                    public void onFailure(Call<GarageStaffDetailResponse> call, Throwable t) {
                        Log.e(TAG, "Error updating garage staff image", t);
                        result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
                    }
                });

        return result;
    }

    /**
     * Xóa nhân viên
     */
    public LiveData<Resource<Boolean>> deleteGarageStaff(int id) {
        MutableLiveData<Resource<Boolean>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        RetrofitClient.getApiService().deleteGarageStaff(id)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful()) {
                            result.setValue(Resource.success(true));
                        } else {
                            result.setValue(Resource.error("Lỗi: " + response.code(), false));
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.e(TAG, "Error deleting garage staff", t);
                        result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), false));
                    }
                });

        return result;
    }
}
