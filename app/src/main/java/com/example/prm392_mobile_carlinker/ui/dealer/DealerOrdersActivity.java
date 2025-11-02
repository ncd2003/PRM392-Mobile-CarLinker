package com.example.prm392_mobile_carlinker.ui.dealer;

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
import com.example.prm392_mobile_carlinker.data.model.order.Order;
import com.example.prm392_mobile_carlinker.ui.adapter.DealerOrderAdapter;
import com.example.prm392_mobile_carlinker.ui.order.OrderDetailActivity;

public class DealerOrdersActivity extends AppCompatActivity implements
        DealerOrderAdapter.OnOrderClickListener,
        DealerOrderAdapter.OnUpdateStatusClickListener {

    private RecyclerView recyclerViewOrders;
    private DealerOrderAdapter orderAdapter;
    private DealerOrdersViewModel viewModel;
    private ProgressBar progressBar;
    private LinearLayout emptyLayout;
    private TextView tvTotalOrders;

    private Button btnFilterAll;
    private Button btnFilterPending;
    private Button btnFilterConfirmed;
    private Button btnFilterPacked;
    private Button btnFilterShipping;
    private Button btnFilterDelivered;
    private Button btnFilterCancelled;

    private int currentFilter = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_orders);

        initViews();
        setupRecyclerView();
        setupViewModel();
        setupFilterButtons();
        setupUpdateStatusObserver();
        loadOrders();
    }

    private void initViews() {
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        progressBar = findViewById(R.id.progressBar);
        emptyLayout = findViewById(R.id.emptyLayout);
        tvTotalOrders = findViewById(R.id.tv_total_orders);

        btnFilterAll = findViewById(R.id.btn_filter_all);
        btnFilterPending = findViewById(R.id.btn_filter_pending);
        btnFilterConfirmed = findViewById(R.id.btn_filter_confirmed);
        btnFilterPacked = findViewById(R.id.btn_filter_packed);
        btnFilterShipping = findViewById(R.id.btn_filter_shipping);
        btnFilterDelivered = findViewById(R.id.btn_filter_delivered);
        btnFilterCancelled = findViewById(R.id.btn_filter_cancelled);
    }

    private void setupRecyclerView() {
        orderAdapter = new DealerOrderAdapter(this, this);
        orderAdapter.setUpdateStatusListener(this);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrders.setAdapter(orderAdapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(DealerOrdersViewModel.class);
        setupOrdersObserver();
    }

    private void setupOrdersObserver() {
        viewModel.getAllOrders().observe(this, resource -> {
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
                            updateTotalOrders();
                        } else {
                            recyclerViewOrders.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                            tvTotalOrders.setText("Tổng: 0 đơn hàng");
                        }
                        break;

                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        recyclerViewOrders.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Lỗi: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void setupFilterButtons() {
        btnFilterAll.setOnClickListener(v -> filterOrders(-1));
        btnFilterPending.setOnClickListener(v -> filterOrders(0));
        btnFilterConfirmed.setOnClickListener(v -> filterOrders(1));
        btnFilterPacked.setOnClickListener(v -> filterOrders(2));
        btnFilterShipping.setOnClickListener(v -> filterOrders(3));
        btnFilterDelivered.setOnClickListener(v -> filterOrders(4));
        btnFilterCancelled.setOnClickListener(v -> filterOrders(5));
    }

    private void filterOrders(int status) {
        currentFilter = status;
        orderAdapter.filterByStatus(status);
        updateFilterButtonsUI();
        updateTotalOrders();
    }

    private void updateFilterButtonsUI() {
        resetButtonStyle(btnFilterAll);
        resetButtonStyle(btnFilterPending);
        resetButtonStyle(btnFilterConfirmed);
        resetButtonStyle(btnFilterPacked);
        resetButtonStyle(btnFilterShipping);
        resetButtonStyle(btnFilterDelivered);
        resetButtonStyle(btnFilterCancelled);

        Button selectedButton = null;
        switch (currentFilter) {
            case -1: selectedButton = btnFilterAll; break;
            case 0: selectedButton = btnFilterPending; break;
            case 1: selectedButton = btnFilterConfirmed; break;
            case 2: selectedButton = btnFilterPacked; break;
            case 3: selectedButton = btnFilterShipping; break;
            case 4: selectedButton = btnFilterDelivered; break;
            case 5: selectedButton = btnFilterCancelled; break;
        }

        if (selectedButton != null) {
            selectedButton.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_blue_dark));
        }
    }

    private void resetButtonStyle(Button button) {
        button.setBackgroundTintList(getResources().getColorStateList(android.R.color.darker_gray));
    }

    private void updateTotalOrders() {
        int total = orderAdapter.getTotalOrders();
        tvTotalOrders.setText("Tổng: " + total + " đơn hàng");
    }

    private void loadOrders() {
        viewModel.loadAllOrders();
    }

    @Override
    public void onOrderClick(Order order) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("ORDER_ID", order.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }

    private void setupUpdateStatusObserver() {
        viewModel.getUpdateStatusResult().observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        // Show loading if needed
                        break;

                    case SUCCESS:
                        Toast.makeText(this, "Cập nhật trạng thái thành công!", Toast.LENGTH_SHORT).show();
                        // Reload orders after successful update
                        loadOrders();
                        break;

                    case ERROR:
                        Toast.makeText(this, "Lỗi: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onUpdateStatusClick(Order order) {
        showUpdateStatusDialog(order);
    }

    private void showUpdateStatusDialog(Order order) {
        // Show all available statuses (excluding current status)
        String[] allStatusOptions = {
            "⏳ Chờ xác nhận (PENDING)",
            "✅ Đã xác nhận (CONFIRMED)",
            "📦 Đã đóng gói (PACKED)",
            "🚚 Đang giao hàng (SHIPPING)",
            "✅ Đã giao thành công (DELIVERED)",
            "❌ Đã hủy (CANCELLED)",
            "❌ Giao thất bại (FAILED)"
        };

        int[] allStatusValues = {0, 1, 2, 3, 4, 5, 6};

        // Filter out current status and create available options
        java.util.ArrayList<String> availableOptions = new java.util.ArrayList<>();
        java.util.ArrayList<Integer> availableValues = new java.util.ArrayList<>();

        int currentStatus = order.getStatus();
        for (int i = 0; i < allStatusValues.length; i++) {
            if (allStatusValues[i] != currentStatus) {
                availableOptions.add(allStatusOptions[i]);
                availableValues.add(allStatusValues[i]);
            }
        }

        // Convert to arrays
        final String[] statusOptions = availableOptions.toArray(new String[0]);
        final int[] statusValues = new int[availableValues.size()];
        for (int i = 0; i < availableValues.size(); i++) {
            statusValues[i] = availableValues.get(i);
        }

        // Track selected status
        final int[] selectedStatusIndex = {-1};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cập nhật trạng thái đơn hàng " + order.getOrderCode());

        // Use single choice items (radio buttons) - don't use setMessage when using setSingleChoiceItems
        builder.setSingleChoiceItems(statusOptions, -1, (dialog, which) -> {
            selectedStatusIndex[0] = which;
        });

        builder.setPositiveButton("XÁC NHẬN", (dialog, which) -> {
            if (selectedStatusIndex[0] >= 0) {
                int newStatus = statusValues[selectedStatusIndex[0]];
                updateOrderStatus(order, newStatus, statusOptions[selectedStatusIndex[0]]);
            } else {
                Toast.makeText(this, "Vui lòng chọn trạng thái mới!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("HỦY", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateOrderStatus(Order order, int newStatus, String newStatusText) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận cập nhật")
                .setMessage("Bạn có chắc muốn cập nhật trạng thái đơn hàng " +
                           order.getOrderCode() + " sang:\n\n" + newStatusText + "?")
                .setPositiveButton("XÁC NHẬN", (dialog, which) -> {
                    viewModel.updateOrderStatus(order.getId(), newStatus);
                })
                .setNegativeButton("HỦY", null)
                .show();
    }
}
