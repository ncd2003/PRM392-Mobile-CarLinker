package com.example.prm392_mobile_carlinker.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prm392_mobile_carlinker.data.model.cart.BaseResponse;
import com.example.prm392_mobile_carlinker.data.model.servicecategory.ServiceCategory;
import com.example.prm392_mobile_carlinker.data.model.servicecategory.ServiceCategoryCreateRequest;
import com.example.prm392_mobile_carlinker.data.model.servicecategory.ServiceCategoryDetailResponse;
import com.example.prm392_mobile_carlinker.data.model.servicecategory.ServiceCategoryResponse;
import com.example.prm392_mobile_carlinker.data.model.servicecategory.ServiceCategoryUpdateRequest;
import com.example.prm392_mobile_carlinker.data.remote.ApiService;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceCategoryRepository {
    private ApiService apiService;

    public ServiceCategoryRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    // Get all service categories
    public LiveData<Resource<List<ServiceCategory>>> getAllServiceCategories(int page, int size, String sortBy, boolean isAsc) {
        MutableLiveData<Resource<List<ServiceCategory>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.getAllServiceCategories(page, size, sortBy, isAsc).enqueue(new Callback<ServiceCategoryResponse>() {
            @Override
            public void onResponse(Call<ServiceCategoryResponse> call, Response<ServiceCategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ServiceCategoryResponse serviceCategoryResponse = response.body();
                    if (serviceCategoryResponse.getStatus() == 200 && serviceCategoryResponse.getData() != null) {
                        // Extract items array from paginated data
                        List<ServiceCategory> items = serviceCategoryResponse.getData().getItems();
                        result.setValue(Resource.success(items));
                    } else {
                        result.setValue(Resource.error("Error: " + serviceCategoryResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Error: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<ServiceCategoryResponse> call, Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    // Get service category by id
    public LiveData<Resource<ServiceCategory>> getServiceCategoryById(int id) {
        MutableLiveData<Resource<ServiceCategory>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.getServiceCategoryById(id).enqueue(new Callback<ServiceCategoryDetailResponse>() {
            @Override
            public void onResponse(Call<ServiceCategoryDetailResponse> call, Response<ServiceCategoryDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ServiceCategoryDetailResponse detailResponse = response.body();
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
            public void onFailure(Call<ServiceCategoryDetailResponse> call, Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    // Create service category
    public LiveData<Resource<ServiceCategory>> createServiceCategory(ServiceCategoryCreateRequest request) {
        MutableLiveData<Resource<ServiceCategory>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.createServiceCategory(request).enqueue(new Callback<ServiceCategoryDetailResponse>() {
            @Override
            public void onResponse(Call<ServiceCategoryDetailResponse> call, Response<ServiceCategoryDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ServiceCategoryDetailResponse detailResponse = response.body();
                    if (detailResponse.getStatus() == 201 && detailResponse.getData() != null) {
                        result.setValue(Resource.success(detailResponse.getData()));
                    } else {
                        result.setValue(Resource.error("Error: " + detailResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Error: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<ServiceCategoryDetailResponse> call, Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    // Update service category
    public LiveData<Resource<ServiceCategory>> updateServiceCategory(int id, ServiceCategoryUpdateRequest request) {
        MutableLiveData<Resource<ServiceCategory>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.updateServiceCategory(id, request).enqueue(new Callback<ServiceCategoryDetailResponse>() {
            @Override
            public void onResponse(Call<ServiceCategoryDetailResponse> call, Response<ServiceCategoryDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ServiceCategoryDetailResponse detailResponse = response.body();
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
            public void onFailure(Call<ServiceCategoryDetailResponse> call, Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    // Delete service category
    public LiveData<Resource<String>> deleteServiceCategory(int id) {
        MutableLiveData<Resource<String>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.deleteServiceCategory(id).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse baseResponse = response.body();
                    if (baseResponse.getStatus() == 200) {
                        result.setValue(Resource.success(baseResponse.getMessage()));
                    } else {
                        result.setValue(Resource.error("Error: " + baseResponse.getMessage(), null));
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
