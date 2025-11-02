package com.example.prm392_mobile_carlinker.ui.dealer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392_mobile_carlinker.data.model.cart.BaseResponse;
import com.example.prm392_mobile_carlinker.data.model.order.OrderListResponse;
import com.example.prm392_mobile_carlinker.data.repository.OrderRepository;
import com.example.prm392_mobile_carlinker.data.repository.Resource;

public class DealerOrdersViewModel extends ViewModel {
    private OrderRepository orderRepository;
    private MediatorLiveData<Resource<OrderListResponse>> ordersLiveData;
    private MediatorLiveData<Resource<BaseResponse>> updateStatusLiveData;

    public DealerOrdersViewModel() {
        orderRepository = new OrderRepository();
        ordersLiveData = new MediatorLiveData<>();
        updateStatusLiveData = new MediatorLiveData<>();
    }

    public LiveData<Resource<OrderListResponse>> getAllOrders() {
        return ordersLiveData;
    }

    public LiveData<Resource<BaseResponse>> getUpdateStatusResult() {
        return updateStatusLiveData;
    }

    public void loadAllOrders() {
        LiveData<Resource<OrderListResponse>> source = orderRepository.getAllOrders();
        ordersLiveData.addSource(source, resource -> {
            ordersLiveData.setValue(resource);
            if (resource != null && resource.getStatus() != Resource.Status.LOADING) {
                ordersLiveData.removeSource(source);
            }
        });
    }

    public void updateOrderStatus(int orderId, int newStatus) {
        LiveData<Resource<BaseResponse>> source = orderRepository.updateOrderStatus(orderId, newStatus);
        updateStatusLiveData.addSource(source, resource -> {
            updateStatusLiveData.setValue(resource);
            if (resource != null && resource.getStatus() != Resource.Status.LOADING) {
                updateStatusLiveData.removeSource(source);
            }
        });
    }
}
