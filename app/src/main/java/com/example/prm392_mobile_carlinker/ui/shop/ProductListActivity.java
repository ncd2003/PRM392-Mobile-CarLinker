package com.example.prm392_mobile_carlinker.ui.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.product.Product;
import com.example.prm392_mobile_carlinker.ui.adapter.ProductAdapter;
import com.example.prm392_mobile_carlinker.ui.cart.CartActivity;
import com.example.prm392_mobile_carlinker.ui.order.MyOrdersActivity;

public class ProductListActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ProductViewModel viewModel;
    private ProgressBar progressBar;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        // Initialize views
        initViews();

        // Setup RecyclerView
        setupRecyclerView();

        // Setup ViewModel
        setupViewModel();

        // Setup SearchView
        setupSearchView();

        // Load products
        loadProducts();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewProducts);
        progressBar = findViewById(R.id.progressBar);
        searchView = findViewById(R.id.searchView);

        // Setup menu bar click listeners
        findViewById(R.id.img_cart).setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.img_my_orders).setOnClickListener(v -> {
            Intent intent = new Intent(this, MyOrdersActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_dealer_orders).setOnClickListener(v -> {
            Intent intent = new Intent(this, com.example.prm392_mobile_carlinker.ui.dealer.DealerOrdersActivity.class);
            startActivity(intent);
        });

        // Setup toolbar - check if ActionBar exists first
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Cửa hàng");
            // Don't show back button since this is the launcher activity
            // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecyclerView() {
        productAdapter = new ProductAdapter(this, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(productAdapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.trim().isEmpty()) {
                    searchProducts(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.trim().isEmpty()) {
                    loadProducts();
                }
                return true;
            }
        });
    }

    private void loadProducts() {
        viewModel.getProducts().observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        break;

                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        if (resource.getData() != null && !resource.getData().isEmpty()) {
                            productAdapter.setProducts(resource.getData());
                        } else {
                            // Empty data
                            Toast.makeText(this, "Không có sản phẩm nào", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        String errorMsg = resource.getMessage();
                        if (errorMsg != null && errorMsg.contains("Failed to connect")) {
                            Toast.makeText(this, "Không thể kết nối tới server. Vui lòng kiểm tra:\n1. Backend đang chạy?\n2. URL đúng chưa?", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Lỗi: " + errorMsg, Toast.LENGTH_LONG).show();
                        }
                        break;
                }
            }
        });
    }

    private void searchProducts(String keyword) {
        viewModel.searchProducts(keyword).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        break;

                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        if (resource.getData() != null) {
                            productAdapter.setProducts(resource.getData());
                        }
                        break;

                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Tìm kiếm thất bại: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onProductClick(Product product) {
        // Navigate to product detail screen
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, product.getId());
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cart) {
            // Open cart activity
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_my_orders) {
            // Open my orders activity
            Intent intent = new Intent(this, MyOrdersActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_messages) {
            // Open chat room list activity
            Intent intent = new Intent(this, com.example.prm392_mobile_carlinker.ui.chat.ChatRoomListActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_dealer_orders) {
            // Open dealer orders activity
            Intent intent = new Intent(this, com.example.prm392_mobile_carlinker.ui.dealer.DealerOrdersActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
