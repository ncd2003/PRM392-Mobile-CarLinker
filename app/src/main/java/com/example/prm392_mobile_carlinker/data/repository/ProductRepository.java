package com.example.prm392_mobile_carlinker.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prm392_mobile_carlinker.data.model.product.Product;
import com.example.prm392_mobile_carlinker.data.model.product.ProductDetail;
import com.example.prm392_mobile_carlinker.data.model.product.ProductDetailResponse;
import com.example.prm392_mobile_carlinker.data.model.product.ProductResponse;
import com.example.prm392_mobile_carlinker.data.remote.ApiService;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private ApiService apiService;

    public ProductRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    // Get all products (không filter)
    public LiveData<Resource<List<Product>>> getProducts() {
        return getProducts(null, null, null);
    }

    // Get products với filters
    public LiveData<Resource<List<Product>>> getProducts(Integer categoryId, Integer brandId, String sortBy) {
        MutableLiveData<Resource<List<Product>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.getProducts(categoryId, brandId, sortBy).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductResponse productResponse = response.body();
                    if (productResponse.getStatus() == 200 && productResponse.getData() != null) {
                        result.setValue(Resource.success(productResponse.getData()));
                    } else {
                        result.setValue(Resource.error("Error: " + productResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Error: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<List<Product>>> searchProducts(String keyword) {
        MutableLiveData<Resource<List<Product>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.searchProducts(keyword).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductResponse productResponse = response.body();
                    if (productResponse.getStatus() == 200 && productResponse.getData() != null) {
                        result.setValue(Resource.success(productResponse.getData()));
                    } else {
                        result.setValue(Resource.error("Error: " + productResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Error: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<Resource<ProductDetail>> getProductDetail(int id) {
        MutableLiveData<Resource<ProductDetail>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.getProductDetail(id).enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductDetailResponse detailResponse = response.body();
                    if (detailResponse.getStatus() == 200 && detailResponse.getData() != null) {
                        result.setValue(Resource.success(detailResponse.getData()));
                    } else {
                        result.setValue(Resource.error("Error: " + detailResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Error: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }
}
