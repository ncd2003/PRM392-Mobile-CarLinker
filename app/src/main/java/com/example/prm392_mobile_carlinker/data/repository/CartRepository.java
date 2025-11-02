package com.example.prm392_mobile_carlinker.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prm392_mobile_carlinker.data.model.cart.AddToCartRequest;
import com.example.prm392_mobile_carlinker.data.model.cart.AddToCartResponse;
import com.example.prm392_mobile_carlinker.data.model.cart.BaseResponse;
import com.example.prm392_mobile_carlinker.data.model.cart.CartResponse;
import com.example.prm392_mobile_carlinker.data.model.cart.UpdateCartRequest;
import com.example.prm392_mobile_carlinker.data.model.cart.UpdateCartResponse;
import com.example.prm392_mobile_carlinker.data.remote.ApiService;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartRepository {
    private ApiService apiService;

    public CartRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    // Get cart items
    public LiveData<Resource<CartResponse>> getCartItems() {
        MutableLiveData<Resource<CartResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.getCartItems().enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CartResponse cartResponse = response.body();
                    if (cartResponse.getStatus() == 200) {
                        result.setValue(Resource.success(cartResponse));
                    } else {
                        result.setValue(Resource.error(cartResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Error: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    // Add to cart
    public LiveData<Resource<AddToCartResponse>> addToCart(int productVariantId, int quantity) {
        MutableLiveData<Resource<AddToCartResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        AddToCartRequest request = new AddToCartRequest(productVariantId, quantity);

        apiService.addToCart(request).enqueue(new Callback<AddToCartResponse>() {
            @Override
            public void onResponse(Call<AddToCartResponse> call, Response<AddToCartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AddToCartResponse addToCartResponse = response.body();
                    if (addToCartResponse.getStatus() == 200) {
                        result.setValue(Resource.success(addToCartResponse));
                    } else {
                        result.setValue(Resource.error(addToCartResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Error: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<AddToCartResponse> call, Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    // Update cart item
    public LiveData<Resource<UpdateCartResponse>> updateCartItem(int productVariantId, int quantity) {
        MutableLiveData<Resource<UpdateCartResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        UpdateCartRequest request = new UpdateCartRequest(productVariantId, quantity);

        apiService.updateCartItem(request).enqueue(new Callback<UpdateCartResponse>() {
            @Override
            public void onResponse(Call<UpdateCartResponse> call, Response<UpdateCartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UpdateCartResponse updateCartResponse = response.body();
                    if (updateCartResponse.getStatus() == 200) {
                        result.setValue(Resource.success(updateCartResponse));
                    } else {
                        result.setValue(Resource.error(updateCartResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Error: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<UpdateCartResponse> call, Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    // Remove from cart
    public LiveData<Resource<BaseResponse>> removeFromCart(int productVariantId) {
        MutableLiveData<Resource<BaseResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.removeFromCart(productVariantId).enqueue(new Callback<BaseResponse>() {
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
}
