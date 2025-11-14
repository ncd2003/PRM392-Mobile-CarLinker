package com.example.prm392_mobile_carlinker.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.cart.CartItem;
import com.example.prm392_mobile_carlinker.ui.checkout.CheckoutActivity;
import com.example.prm392_mobile_carlinker.ui.adapter.CartAdapter;

import java.text.NumberFormat;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemActionListener {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private CartViewModel viewModel;
    private ProgressBar progressBar;
    private LinearLayout contentLayout;
    private LinearLayout emptyCartLayout;
    private TextView txtTotalPrice;
    private Button btnCheckout;
    private Button btnShopNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize views
        initViews();

        // Setup RecyclerView
        setupRecyclerView();

        // Setup ViewModel
        setupViewModel();

        // Load cart items
        loadCartItems();
    }

    private void initViews() {
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        progressBar = findViewById(R.id.progressBar);
        contentLayout = findViewById(R.id.contentLayout);
        emptyCartLayout = findViewById(R.id.emptyCartLayout);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnShopNow = findViewById(R.id.btnShopNow);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        // 2. Đặt nó làm ActionBar
        setSupportActionBar(toolbar);

        // Setup toolbar (ĐOẠN NÀY ĐÃ CÓ SẴN)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Giỏ hàng");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Checkout button
        btnCheckout.setOnClickListener(v -> onCheckoutClicked());

        // Shop now button
        btnShopNow.setOnClickListener(v -> {
            finish(); // Go back to product list
        });
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(this, this);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(cartAdapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);
    }

    private void loadCartItems() {
        viewModel.getCartItems().observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        contentLayout.setVisibility(View.GONE);
                        emptyCartLayout.setVisibility(View.GONE);
                        break;

                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        if (resource.getData() != null && resource.getData().getData() != null
                                && !resource.getData().getData().isEmpty()) {
                            contentLayout.setVisibility(View.VISIBLE);
                            emptyCartLayout.setVisibility(View.GONE);
                            cartAdapter.setCartItems(resource.getData().getData());
                            updateTotalPrice();
                        } else {
                            contentLayout.setVisibility(View.GONE);
                            emptyCartLayout.setVisibility(View.VISIBLE);
                        }
                        break;

                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        contentLayout.setVisibility(View.GONE);
                        emptyCartLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Lỗi: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void updateTotalPrice() {
        double total = cartAdapter.getTotalCartPrice();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        txtTotalPrice.setText(formatter.format(total));
    }

    @Override
    public void onIncreaseQuantity(CartItem item) {
        int newQuantity = item.getQuantity() + 1;

        // Check stock availability from productVariant
        if (item.getProductVariant() != null) {
            int availableStock = item.getProductVariant().getStockQuantity();
            if (newQuantity > availableStock) {
                Toast.makeText(this, "Không đủ hàng trong kho", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        updateQuantity(item.getProductVariantId(), newQuantity);
    }

    @Override
    public void onDecreaseQuantity(CartItem item) {
        int newQuantity = item.getQuantity() - 1;

        if (newQuantity < 1) {
            // If quantity is 0, remove item
            showRemoveConfirmDialog(item);
            return;
        }

        updateQuantity(item.getProductVariantId(), newQuantity);
    }

    @Override
    public void onRemoveItem(CartItem item) {
        showRemoveConfirmDialog(item);
    }

    private void updateQuantity(int productVariantId, int newQuantity) {
        viewModel.updateCartQuantity(productVariantId, newQuantity).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;

                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        if (resource.getData() != null) {
                            Toast.makeText(this, resource.getData().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        loadCartItems(); // Reload cart
                        break;

                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Lỗi: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void showRemoveConfirmDialog(CartItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?")
                .setPositiveButton("Xóa", (dialog, which) -> removeItem(item.getProductVariantId()))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void removeItem(int productVariantId) {
        viewModel.removeFromCart(productVariantId).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;

                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                        loadCartItems(); // Reload cart
                        break;

                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Lỗi: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void onCheckoutClicked() {
        // Navigate to checkout screen
        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra(CheckoutActivity.EXTRA_TOTAL_AMOUNT, cartAdapter.getTotalCartPrice());
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload cart when returning to this activity
        loadCartItems();
    }
}
