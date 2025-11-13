package com.example.prm392_mobile_carlinker.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prm392_mobile_carlinker.data.model.servicerecord.ServiceRecordCreateRequest;
import com.example.prm392_mobile_carlinker.data.model.servicerecord.ServiceRecordResponse;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceRecordRepository {
    private static final String TAG = "ServiceRecordRepository";

    /**
     * Tạo service record mới (đặt lịch dịch vụ)
     *
     * @param garageId ID của garage
     * @param vehicleId ID của vehicle
     * @param serviceItemIds Danh sách ID của các service items
     * @return LiveData chứa kết quả
     */
    public LiveData<Result<ServiceRecordResponse>> createServiceRecord(
            int garageId,
            int vehicleId,
            java.util.List<Integer> serviceItemIds) {

        MutableLiveData<Result<ServiceRecordResponse>> result = new MutableLiveData<>();
        result.setValue(Result.loading(null));

        // Tạo request object
        ServiceRecordCreateRequest request = new ServiceRecordCreateRequest(vehicleId, serviceItemIds);

        // Gọi API
        RetrofitClient.getApiService().createServiceRecord(garageId, request)
                .enqueue(new Callback<ServiceRecordResponse>() {
                    @Override
                    public void onResponse(Call<ServiceRecordResponse> call, Response<ServiceRecordResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d(TAG, "Đặt lịch dịch vụ thành công: " + response.body().getMessage());
                            result.setValue(Result.success(response.body()));
                        } else {
                            String errorMsg = "Lỗi: " + response.code();
                            try {
                                if (response.errorBody() != null) {
                                    errorMsg = response.errorBody().string();
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing error body", e);
                            }
                            Log.e(TAG, "Lỗi khi đặt lịch: " + errorMsg);
                            result.setValue(Result.error(errorMsg, null));
                        }
                    }

                    @Override
                    public void onFailure(Call<ServiceRecordResponse> call, Throwable t) {
                        Log.e(TAG, "Lỗi kết nối: " + t.getMessage());
                        result.setValue(Result.error("Lỗi kết nối: " + t.getMessage(), null));
                    }
                });

        return result;
    }

    /**
     * Tạo service record mới với callback
     *
     * @param garageId ID của garage
     * @param vehicleId ID của vehicle
     * @param serviceItemIds Danh sách ID của các service items
     * @param callback Callback để xử lý kết quả
     */
    public void createServiceRecordWithCallback(
            int garageId,
            int vehicleId,
            java.util.List<Integer> serviceItemIds,
            ServiceRecordCallback callback) {

        ServiceRecordCreateRequest request = new ServiceRecordCreateRequest(vehicleId, serviceItemIds);

        RetrofitClient.getApiService().createServiceRecord(garageId, request)
                .enqueue(new Callback<ServiceRecordResponse>() {
                    @Override
                    public void onResponse(Call<ServiceRecordResponse> call, Response<ServiceRecordResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d(TAG, "Đặt lịch dịch vụ thành công: " + response.body().getMessage());
                            callback.onSuccess(response.body());
                        } else {
                            String errorMsg = "Lỗi: " + response.code();
                            try {
                                if (response.errorBody() != null) {
                                    errorMsg = response.errorBody().string();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.e(TAG, "Lỗi khi đặt lịch: " + errorMsg);
                            callback.onError(errorMsg);
                        }
                    }

                    @Override
                    public void onFailure(Call<ServiceRecordResponse> call, Throwable t) {
                        Log.e(TAG, "Lỗi kết nối: " + t.getMessage());
                        callback.onError("Lỗi kết nối: " + t.getMessage());
                    }
                });
    }

    /**
     * Interface callback để xử lý kết quả
     */
    public interface ServiceRecordCallback {
        void onSuccess(ServiceRecordResponse response);
        void onError(String error);
    }
}
