package com.example.prm392_mobile_carlinker.ui.servicecategory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392_mobile_carlinker.data.model.servicecategory.ServiceCategory;
import com.example.prm392_mobile_carlinker.data.model.servicecategory.ServiceCategoryCreateRequest;
import com.example.prm392_mobile_carlinker.data.model.servicecategory.ServiceCategoryUpdateRequest;
import com.example.prm392_mobile_carlinker.data.repository.Resource;
import com.example.prm392_mobile_carlinker.data.repository.ServiceCategoryRepository;

import java.util.List;

public class ServiceCategoryViewModel extends ViewModel {
    private ServiceCategoryRepository repository;

    public ServiceCategoryViewModel() {
        repository = new ServiceCategoryRepository();
    }

    public LiveData<Resource<List<ServiceCategory>>> getAllServiceCategories(int page, int size, String sortBy, boolean isAsc) {
        return repository.getAllServiceCategories(page, size, sortBy, isAsc);
    }

    public LiveData<Resource<ServiceCategory>> getServiceCategoryById(int id) {
        return repository.getServiceCategoryById(id);
    }

    public LiveData<Resource<ServiceCategory>> createServiceCategory(ServiceCategoryCreateRequest request) {
        return repository.createServiceCategory(request);
    }

    public LiveData<Resource<ServiceCategory>> updateServiceCategory(int id, ServiceCategoryUpdateRequest request) {
        return repository.updateServiceCategory(id, request);
    }

    public LiveData<Resource<String>> deleteServiceCategory(int id) {
        return repository.deleteServiceCategory(id);
    }
}

