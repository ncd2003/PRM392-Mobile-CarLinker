package com.example.prm392_mobile_carlinker.ui.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392_mobile_carlinker.data.model.cart.AddToCartResponse;
import com.example.prm392_mobile_carlinker.data.model.cart.BaseResponse;
import com.example.prm392_mobile_carlinker.data.model.cart.CartResponse;
import com.example.prm392_mobile_carlinker.data.model.cart.UpdateCartResponse;
import com.example.prm392_mobile_carlinker.data.repository.CartRepository;
import com.example.prm392_mobile_carlinker.data.repository.Resource;

public class CartViewModel extends ViewModel {
    private CartRepository cartRepository;

    public CartViewModel() {
        cartRepository = new CartRepository();
    }

    public LiveData<Resource<CartResponse>> getCartItems() {
        return cartRepository.getCartItems();
    }

    public LiveData<Resource<AddToCartResponse>> addToCart(int productVariantId, int quantity) {
        return cartRepository.addToCart(productVariantId, quantity);
    }

    public LiveData<Resource<UpdateCartResponse>> updateCartQuantity(int productVariantId, int newQuantity) {
        return cartRepository.updateCartItem(productVariantId, newQuantity);
    }

    public LiveData<Resource<BaseResponse>> removeFromCart(int productVariantId) {
        return cartRepository.removeFromCart(productVariantId);
    }
}
