package com.example.prm392_mobile_carlinker.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prm392_mobile_carlinker.data.model.cart.BaseResponse;
import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemCreateRequest;
import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemDetailResponse;
import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemDto;
import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemResponse;
import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemUpdateRequest;
import com.example.prm392_mobile_carlinker.data.remote.ApiService;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceItemRepository {
    private ApiService apiService;

    public ServiceItemRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    // Get all service items
    public LiveData<Resource<List<ServiceItemDto>>> getAllServiceItems(int page, int size, String sortBy, boolean isAsc) {
        MutableLiveData<Resource<List<ServiceItemDto>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.getAllServiceItems(page, size, sortBy, isAsc).enqueue(new Callback<ServiceItemResponse>() {
            @Override
            public void onResponse(Call<ServiceItemResponse> call, Response<ServiceItemResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ServiceItemResponse serviceItemResponse = response.body();
                    if (serviceItemResponse.getStatus() == 200 && serviceItemResponse.getData() != null) {
                        // Extract items array from paginated data
                        List<ServiceItemDto> items = serviceItemResponse.getData().getItems();
                        result.setValue(Resource.success(items));
                    } else {
                        result.setValue(Resource.error("Error: " + serviceItemResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Error: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<ServiceItemResponse> call, Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    // Get service item by id
    public LiveData<Resource<ServiceItemDto>> getServiceItemById(int id) {
        MutableLiveData<Resource<ServiceItemDto>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.getServiceItemById(id).enqueue(new Callback<ServiceItemDetailResponse>() {
            @Override
            public void onResponse(Call<ServiceItemDetailResponse> call, Response<ServiceItemDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ServiceItemDetailResponse detailResponse = response.body();
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
            public void onFailure(Call<ServiceItemDetailResponse> call, Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    // Create service item
    public LiveData<Resource<ServiceItemDto>> createServiceItem(ServiceItemCreateRequest request) {
        MutableLiveData<Resource<ServiceItemDto>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.createServiceItem(request).enqueue(new Callback<ServiceItemDetailResponse>() {
            @Override
            public void onResponse(Call<ServiceItemDetailResponse> call, Response<ServiceItemDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ServiceItemDetailResponse detailResponse = response.body();
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
            public void onFailure(Call<ServiceItemDetailResponse> call, Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    // Update service item
    public LiveData<Resource<ServiceItemDto>> updateServiceItem(int id, ServiceItemUpdateRequest request) {
        MutableLiveData<Resource<ServiceItemDto>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.updateServiceItem(id, request).enqueue(new Callback<ServiceItemDetailResponse>() {
            @Override
            public void onResponse(Call<ServiceItemDetailResponse> call, Response<ServiceItemDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ServiceItemDetailResponse detailResponse = response.body();
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
            public void onFailure(Call<ServiceItemDetailResponse> call, Throwable t) {
                result.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    // Delete service item
    public LiveData<Resource<String>> deleteServiceItem(int id) {
        MutableLiveData<Resource<String>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.deleteServiceItem(id).enqueue(new Callback<BaseResponse>() {
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
