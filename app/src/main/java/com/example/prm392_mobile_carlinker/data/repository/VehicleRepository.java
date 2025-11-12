package com.example.prm392_mobile_carlinker.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleListResponse;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleRequest;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleResponse;
import com.example.prm392_mobile_carlinker.data.remote.ApiService;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;

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

        Call<VehicleResponse> call =   apiService.addVehicle(
                toForm(req.getLicensePlate()),
                toForm(req.getFuelType()),
                toForm(req.getTransmissionType()),
                toForm(req.getBrand()),
                toForm(req.getModel()),
                toForm(String.valueOf(req.getYear())),
                toForm(req.getImage())
        );

        call.enqueue(new Callback<VehicleResponse>() {
            @Override
            public void onResponse(Call<VehicleResponse> call, Response<VehicleResponse> response) {
                live.postValue(Result.success(response.body()));
            }

            @Override
            public void onFailure(Call<VehicleResponse> call, Throwable t) {
                live.postValue(Result.error(t.getMessage(), null));
            }
        });

        return live;
    }

    public LiveData<Result<VehicleResponse>> updateVehicle(int id, VehicleRequest req) {
        MutableLiveData<Result<VehicleResponse>> live = new MutableLiveData<>();

        apiService.updateVehicle(
                id,
                toForm(req.getLicensePlate()),
                toForm(req.getFuelType()),
                toForm(req.getTransmissionType()),
                toForm(req.getBrand()),
                toForm(req.getModel()),
                toForm(String.valueOf(req.getYear())),
                toForm(req.getImage())
        ).enqueue(new Callback<VehicleResponse>() {
            @Override
            public void onResponse(Call<VehicleResponse> call, Response<VehicleResponse> response) {
                live.postValue(Result.success(response.body()));
            }

            @Override
            public void onFailure(Call<VehicleResponse> call, Throwable t) {
                live.postValue(Result.error(t.getMessage(), null));
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
