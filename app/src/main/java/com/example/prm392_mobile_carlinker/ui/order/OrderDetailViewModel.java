package com.example.prm392_mobile_carlinker.ui.order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392_mobile_carlinker.data.model.order.OrderResponse;
import com.example.prm392_mobile_carlinker.data.repository.OrderRepository;
import com.example.prm392_mobile_carlinker.data.repository.Resource;

public class OrderDetailViewModel extends ViewModel {
    private OrderRepository orderRepository;
    private MediatorLiveData<Resource<OrderResponse>> orderDetailLiveData;

    public OrderDetailViewModel() {
        orderRepository = new OrderRepository();
        orderDetailLiveData = new MediatorLiveData<>();
    }

    public LiveData<Resource<OrderResponse>> getOrderDetail() {
        return orderDetailLiveData;
    }

    public void loadOrderDetail(int orderId) {
        LiveData<Resource<OrderResponse>> source = orderRepository.getOrderDetail(orderId);
        orderDetailLiveData.addSource(source, resource -> {
            orderDetailLiveData.setValue(resource);
            if (resource != null && resource.getStatus() != Resource.Status.LOADING) {
                orderDetailLiveData.removeSource(source);
            }
        });
    }
}

