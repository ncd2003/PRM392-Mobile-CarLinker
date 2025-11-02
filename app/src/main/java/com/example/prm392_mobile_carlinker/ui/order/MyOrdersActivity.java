package com.example.prm392_mobile_carlinker.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.order.Order;
import com.example.prm392_mobile_carlinker.ui.adapter.OrderAdapter;

public class MyOrdersActivity extends AppCompatActivity implements OrderAdapter.OnOrderClickListener {

    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;
    private MyOrdersViewModel viewModel;
    private ProgressBar progressBar;
    private LinearLayout emptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        // Initialize views
        initViews();

        // Setup RecyclerView
        setupRecyclerView();

        // Setup ViewModel
        setupViewModel();

        // Load orders
        loadOrders();
    }

    private void initViews() {
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        progressBar = findViewById(R.id.progressBar);
        emptyLayout = findViewById(R.id.emptyLayout);

        // Setup toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Đơn hàng của tôi");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderAdapter(this, this);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrders.setAdapter(orderAdapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(MyOrdersViewModel.class);

        // Setup observers
        setupOrdersObserver();
        setupCancelOrderObserver();
    }

    private void setupOrdersObserver() {
        viewModel.getMyOrders().observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        recyclerViewOrders.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.GONE);
                        break;

                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        if (resource.getData() != null && resource.getData().getData() != null
                                && !resource.getData().getData().isEmpty()) {
                            recyclerViewOrders.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.GONE);
                            orderAdapter.setOrders(resource.getData().getData());
                        } else {
                            recyclerViewOrders.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                        }
                        break;

                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        recyclerViewOrders.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Lỗi: " + resource.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void setupCancelOrderObserver() {
        viewModel.getCancelOrderResult().observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;

                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Hủy đơn hàng thành công", Toast.LENGTH_SHORT).show();
                        // Reload orders
                        viewModel.loadOrders();
                        break;

                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Lỗi: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void loadOrders() {
        viewModel.loadOrders();
    }

    @Override
    public void onOrderClick(Order order) {
        // Navigate to order detail activity
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("ORDER_ID", order.getId());
        startActivity(intent);
    }

    @Override
    public void onCancelOrderClick(Order order) {
        // Show confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận hủy đơn hàng")
                .setMessage("Bạn có chắc chắn muốn hủy đơn hàng " + order.getOrderCode() + " không?")
                .setPositiveButton("Hủy đơn", (dialog, which) -> {
                    // ✅ SỬ DỤNG order.getId() - TRẢ VỀ SỐ NGUYÊN (1, 2, 3...)
                    viewModel.cancelOrder(order.getId());
                })
                .setNegativeButton("Không", null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload orders when returning to this activity
        loadOrders();
    }
}
