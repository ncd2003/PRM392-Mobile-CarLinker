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

        apiService.createVNPayPaymentUrl(orderId, moneyToPay, description).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() || response.code() == 201) {
                    // Backend returns plain text URL in body
                    String paymentUrl = response.body();

                    if (paymentUrl != null && !paymentUrl.trim().isEmpty()) {
                        // Create VNPayResponse object with the URL
                        VNPayResponse vnPayResponse = new VNPayResponse();
                        vnPayResponse.setStatus(response.code());
                        vnPayResponse.setMessage("Success");
                        vnPayResponse.setData(paymentUrl.trim());

                        result.setValue(Resource.success(vnPayResponse));
                    } else {
                        result.setValue(Resource.error("Không nhận được URL thanh toán từ VNPay", null));
                    }
                } else {
                    result.setValue(Resource.error("Không thể tạo link thanh toán VNPay (HTTP " + response.code() + ")", null));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }
}
