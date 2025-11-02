package com.example.prm392_mobile_carlinker.ui.checkout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392_mobile_carlinker.data.model.order.OrderResponse;
import com.example.prm392_mobile_carlinker.data.model.payment.VNPayResponse;
import com.example.prm392_mobile_carlinker.data.repository.OrderRepository;
import com.example.prm392_mobile_carlinker.data.repository.PaymentRepository;
import com.example.prm392_mobile_carlinker.data.repository.Resource;

public class CheckoutViewModel extends ViewModel {
    private OrderRepository orderRepository;
    private PaymentRepository paymentRepository;

    public CheckoutViewModel() {
        orderRepository = new OrderRepository();
        paymentRepository = new PaymentRepository();
    }

    public LiveData<Resource<OrderResponse>> createOrder(String fullName, String email,
                                                         String shippingAddress, String phoneNumber,
                                                         String paymentMethod) {
        return orderRepository.createOrder(fullName, email, shippingAddress, phoneNumber, paymentMethod);
    }

    public LiveData<Resource<VNPayResponse>> createVNPayPayment(int orderId, double moneyToPay, String description) {
        return paymentRepository.createVNPayPayment(orderId, moneyToPay, description);
    }
}
