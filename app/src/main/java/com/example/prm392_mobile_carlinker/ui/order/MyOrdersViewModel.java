package com.example.prm392_mobile_carlinker.ui.order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392_mobile_carlinker.data.model.cart.BaseResponse;
import com.example.prm392_mobile_carlinker.data.model.order.OrderListResponse;
import com.example.prm392_mobile_carlinker.data.repository.OrderRepository;
import com.example.prm392_mobile_carlinker.data.repository.Resource;

public class MyOrdersViewModel extends ViewModel {
    private OrderRepository orderRepository;
    private MediatorLiveData<Resource<OrderListResponse>> ordersLiveData;
    private MediatorLiveData<Resource<BaseResponse>> cancelOrderResult;

    public MyOrdersViewModel() {
        orderRepository = new OrderRepository();
        ordersLiveData = new MediatorLiveData<>();
        cancelOrderResult = new MediatorLiveData<>();
    }

    public LiveData<Resource<OrderListResponse>> getMyOrders() {
        return ordersLiveData;
    }

    public void loadOrders() {
        LiveData<Resource<OrderListResponse>> source = orderRepository.getMyOrders();
        ordersLiveData.addSource(source, resource -> {
            ordersLiveData.setValue(resource);
            if (resource != null && resource.getStatus() != Resource.Status.LOADING) {
                ordersLiveData.removeSource(source);
            }
        });
    }

    public LiveData<Resource<BaseResponse>> getCancelOrderResult() {
        return cancelOrderResult;
    }

    public void cancelOrder(int orderId) {
        LiveData<Resource<BaseResponse>> source = orderRepository.cancelOrder(orderId);
        cancelOrderResult.addSource(source, resource -> {
            cancelOrderResult.setValue(resource);
            if (resource != null && resource.getStatus() != Resource.Status.LOADING) {
                cancelOrderResult.removeSource(source);
            }
        });
    }
}
