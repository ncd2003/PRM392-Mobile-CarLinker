package com.example.prm392_mobile_carlinker.data.repository;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleListResponse;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleRequest;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleResponse;
import com.example.prm392_mobile_carlinker.data.remote.ApiService;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleRepository {
    private ApiService apiService;

    public VehicleRepository() {
        apiService = RetrofitClient.getApiService();
    }
    private RequestBody toForm(String s) {
        return RequestBody.create(s, okhttp3.MultipartBody.FORM);
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

    public LiveData<Result<VehicleResponse>> addVehicle(VehicleRequest req) {
        MutableLiveData<Result<VehicleResponse>> live = new MutableLiveData<>();
        live.setValue(Result.loading(null));

        // Prepare form parts (bảo vệ null)
        RequestBody rbLicense = toForm(req.getLicensePlate() == null ? "" : req.getLicensePlate());
        RequestBody rbFuel = toForm(req.getFuelType() == null ? "" : req.getFuelType());
        RequestBody rbTrans = toForm(req.getTransmissionType() == null ? "" : req.getTransmissionType());
        RequestBody rbBrand = toForm(req.getBrand() == null ? "" : req.getBrand());
        RequestBody rbModel = toForm(req.getModel() == null ? "" : req.getModel());
        RequestBody rbYear = toForm(String.valueOf(req.getYear()));

        // We don't upload file now — backend accepts nullable imageFile, so pass null.
        MultipartBody.Part imagePart = null;

        Call<VehicleResponse> call = apiService.addVehicle(
                rbLicense,
                rbFuel,
                rbTrans,
                rbBrand,
                rbModel,
                rbYear,
                imagePart
        );

        call.enqueue(new Callback<VehicleResponse>() {
            @Override
            public void onResponse(Call<VehicleResponse> call, Response<VehicleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    live.postValue(Result.success(response.body()));
                } else {
                    String err = "Error: " + response.code();
                    try {
                        if (response.errorBody() != null) err = response.errorBody().string();
                    } catch (Exception ignored) {}
                    live.postValue(Result.error(err, null));
                }
            }

            @Override
            public void onFailure(Call<VehicleResponse> call, Throwable t) {
                live.postValue(Result.error("Network error: " + t.getMessage(), null));
            }
        });

        return live;
    }

    public LiveData<Result<VehicleResponse>> updateVehicle(int id, VehicleRequest req) {
        MutableLiveData<Result<VehicleResponse>> live = new MutableLiveData<>();
        live.setValue(Result.loading(null));

        // Prepare form parts (bảo vệ null)
        RequestBody rbLicense = toForm(req.getLicensePlate() == null ? "" : req.getLicensePlate());
        RequestBody rbFuel = toForm(req.getFuelType() == null ? "" : req.getFuelType());
        RequestBody rbTrans = toForm(req.getTransmissionType() == null ? "" : req.getTransmissionType());
        RequestBody rbBrand = toForm(req.getBrand() == null ? "" : req.getBrand());
        RequestBody rbModel = toForm(req.getModel() == null ? "" : req.getModel());
        RequestBody rbYear = toForm(String.valueOf(req.getYear()));
        // Backend update endpoint expects an "Image" string part (for URL) + optional file part
        RequestBody rbImage = toForm(req.getImageFile() == null ? "" : req.getImageFile());

        // No file upload for now
        MultipartBody.Part imagePart = null;

        apiService.updateVehicle(
                id,
                rbLicense,
                rbFuel,
                rbTrans,
                rbBrand,
                rbModel,
                rbYear,
                imagePart
        ).enqueue(new Callback<VehicleResponse>() {
            @Override
            public void onResponse(Call<VehicleResponse> call, Response<VehicleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    live.postValue(Result.success(response.body()));
                } else {
                    String err = "Error: " + response.code();
                    try {
                        if (response.errorBody() != null) err = response.errorBody().string();
                    } catch (Exception ignored) {}
                    live.postValue(Result.error(err, null));
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


