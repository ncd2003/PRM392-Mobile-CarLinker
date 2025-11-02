package com.example.prm392_mobile_carlinker.ui.shop;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392_mobile_carlinker.data.model.product.Product;
import com.example.prm392_mobile_carlinker.data.model.product.ProductDetail;
import com.example.prm392_mobile_carlinker.data.repository.ProductRepository;
import com.example.prm392_mobile_carlinker.data.repository.Resource;

import java.util.List;

public class ProductViewModel extends ViewModel {
    private ProductRepository productRepository;

    public ProductViewModel() {
        productRepository = new ProductRepository();
    }

    // Get all products (không filter)
    public LiveData<Resource<List<Product>>> getProducts() {
        return productRepository.getProducts();
    }

    // Get products với filters
    public LiveData<Resource<List<Product>>> getProducts(Integer categoryId, Integer brandId, String sortBy) {
        return productRepository.getProducts(categoryId, brandId, sortBy);
    }

    public LiveData<Resource<List<Product>>> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }

    public LiveData<Resource<ProductDetail>> getProductDetail(int id) {
        return productRepository.getProductDetail(id);
    }
}
