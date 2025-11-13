package com.example.prm392_mobile_carlinker.ui.serviceitem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemCreateRequest;
import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemDto;
import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemUpdateRequest;
import com.example.prm392_mobile_carlinker.data.repository.Resource;
import com.example.prm392_mobile_carlinker.data.repository.ServiceItemRepository;

import java.util.List;

public class ServiceItemViewModel extends ViewModel {
    private ServiceItemRepository repository;

    public ServiceItemViewModel() {
        repository = new ServiceItemRepository();
    }

    public LiveData<Resource<List<ServiceItemDto>>> getAllServiceItems(int page, int size, String sortBy, boolean isAsc) {
        return repository.getAllServiceItems(page, size, sortBy, isAsc);
    }

    public LiveData<Resource<ServiceItemDto>> getServiceItemById(int id) {
        return repository.getServiceItemById(id);
    }

    public LiveData<Resource<ServiceItemDto>> createServiceItem(ServiceItemCreateRequest request) {
        return repository.createServiceItem(request);
    }

    public LiveData<Resource<ServiceItemDto>> updateServiceItem(int id, ServiceItemUpdateRequest request) {
        return repository.updateServiceItem(id, request);
    }

    public LiveData<Resource<String>> deleteServiceItem(int id) {
        return repository.deleteServiceItem(id);
    }
}

