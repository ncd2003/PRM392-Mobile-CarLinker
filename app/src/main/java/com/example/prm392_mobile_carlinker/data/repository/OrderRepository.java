package com.example.prm392_mobile_carlinker.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prm392_mobile_carlinker.data.model.cart.BaseResponse;
import com.example.prm392_mobile_carlinker.data.model.order.CreateOrderRequest;
import com.example.prm392_mobile_carlinker.data.model.order.OrderListResponse;
import com.example.prm392_mobile_carlinker.data.model.order.OrderResponse;
import com.example.prm392_mobile_carlinker.data.model.order.UpdateOrderStatusRequest;
import com.example.prm392_mobile_carlinker.data.remote.ApiService;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {
    private final ApiService apiService;

    public OrderRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    // Get my orders
    public LiveData<Resource<OrderListResponse>> getMyOrders() {
        MutableLiveData<Resource<OrderListResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.getMyOrders().enqueue(new Callback<OrderListResponse>() {
            @Override
            public void onResponse(Call<OrderListResponse> call, Response<OrderListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OrderListResponse orderListResponse = response.body();
                    if (orderListResponse.getStatus() == 200) {
                        result.setValue(Resource.success(orderListResponse));
                    } else {
                        result.setValue(Resource.error(orderListResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Error: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<OrderListResponse> call, Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    // Create order
    public LiveData<Resource<OrderResponse>> createOrder(String fullName, String email,
                                                         String shippingAddress, String phoneNumber,
                                                         String paymentMethod) {
        MutableLiveData<Resource<OrderResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        CreateOrderRequest request = new CreateOrderRequest(fullName, email, shippingAddress,
                                                            phoneNumber, paymentMethod);

        apiService.createOrder(request).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OrderResponse orderResponse = response.body();
                    if (orderResponse.getStatus() == 200) {
                        result.setValue(Resource.success(orderResponse));
                    } else {
                        result.setValue(Resource.error(orderResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Error: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    // Cancel order
    public LiveData<Resource<BaseResponse>> cancelOrder(int orderId) {
        MutableLiveData<Resource<BaseResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.cancelOrder(orderId).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse baseResponse = response.body();
                    if (baseResponse.getStatus() == 200) {
                        result.setValue(Resource.success(baseResponse));
                    } else {
                        result.setValue(Resource.error(baseResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Error: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    // Get all orders for dealer
    public LiveData<Resource<OrderListResponse>> getAllOrders() {
        MutableLiveData<Resource<OrderListResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.getAllOrders().enqueue(new Callback<OrderListResponse>() {
            @Override
            public void onResponse(Call<OrderListResponse> call, Response<OrderListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OrderListResponse orderListResponse = response.body();
                    if (orderListResponse.getStatus() == 200) {
                        result.setValue(Resource.success(orderListResponse));
                    } else {
                        result.setValue(Resource.error(orderListResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Error: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<OrderListResponse> call, Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    // Get order detail by ID
    public LiveData<Resource<OrderResponse>> getOrderDetail(int orderId) {
        MutableLiveData<Resource<OrderResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.getOrderDetail(orderId).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OrderResponse orderResponse = response.body();
                    if (orderResponse.getStatus() == 200) {
                        result.setValue(Resource.success(orderResponse));
                    } else {
                        result.setValue(Resource.error(orderResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Lỗi: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    // Update order status (Dealer)
    public LiveData<Resource<BaseResponse>> updateOrderStatus(int orderId, int newStatus) {
        MutableLiveData<Resource<BaseResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        UpdateOrderStatusRequest request = new UpdateOrderStatusRequest(orderId, newStatus);

        apiService.updateOrderStatus(request).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse baseResponse = response.body();
                    if (baseResponse.getStatus() == 200) {
                        result.setValue(Resource.success(baseResponse));
                    } else {
                        // Backend trả về status khác 200 trong body
                        result.setValue(Resource.error(baseResponse.getMessage(), null));
                    }
                } else {
                    // Response không thành công (400, 500, etc.)
                    // Cần parse error body để lấy message từ backend
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            // Parse JSON error response
                            org.json.JSONObject jsonObject = new org.json.JSONObject(errorBody);
                            String errorMessage = jsonObject.optString("message", "Lỗi không xác định");
                            result.setValue(Resource.error(errorMessage, null));
                        } else {
                            result.setValue(Resource.error("Lỗi: " + response.message(), null));
                        }
                    } catch (Exception e) {
                        result.setValue(Resource.error("Lỗi: " + response.message(), null));
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }
}

