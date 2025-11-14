package com.example.prm392_mobile_carlinker.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prm392_mobile_carlinker.data.model.payment.VNPayResponse;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;
import com.example.prm392_mobile_carlinker.data.remote.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentRepository {
    private ApiService apiService;

    public PaymentRepository() {
        apiService = RetrofitClient.getApiService();
    }

    public LiveData<Resource<VNPayResponse>> createVNPayPayment(int orderId, double moneyToPay, String description) {
        MutableLiveData<Resource<VNPayResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.createVNPayPaymentUrl(orderId, moneyToPay, description).enqueue(new Callback<VNPayResponse>() {
            @Override
            public void onResponse(Call<VNPayResponse> call, Response<VNPayResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Backend returns JSON: {"status": 200, "message": "Success", "data": "payment_url"}
                    VNPayResponse vnPayResponse = response.body();

                    // Validate payment URL exists
                    if (vnPayResponse.getData() != null && !vnPayResponse.getData().trim().isEmpty()) {
                        result.setValue(Resource.success(vnPayResponse));
                    } else {
                        result.setValue(Resource.error("Không nhận được URL thanh toán từ VNPay", null));
                    }
                } else {
                    result.setValue(Resource.error("Không thể tạo link thanh toán VNPay (HTTP " + response.code() + ")", null));
                }
            }

            @Override
            public void onFailure(Call<VNPayResponse> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }
}
