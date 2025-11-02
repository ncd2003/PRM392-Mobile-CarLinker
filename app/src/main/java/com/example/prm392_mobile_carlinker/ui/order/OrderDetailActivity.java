package com.example.prm392_mobile_carlinker.ui.order;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.order.OrderResponse;
import com.example.prm392_mobile_carlinker.ui.adapter.OrderItemAdapter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER_ID = "ORDER_ID";

    private TextView tvOrderCode;
    private TextView tvOrderDate;
    private TextView tvOrderStatus;
    private TextView tvFullName;
    private TextView tvPhoneNumber;
    private TextView tvEmail;
    private TextView tvShippingAddress;
    private TextView tvPaymentMethod;
    private TextView tvTotalAmount;
    private RecyclerView recyclerViewOrderItems;
    private ProgressBar progressBar;

    private OrderDetailViewModel viewModel;
    private OrderItemAdapter orderItemAdapter;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Get order ID from intent
        orderId = getIntent().getIntExtra(EXTRA_ORDER_ID, -1);
        if (orderId == -1) {
            Toast.makeText(this, "Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        initViews();

        // Setup RecyclerView
        setupRecyclerView();

        // Setup ViewModel
        setupViewModel();

        // Load order detail
        loadOrderDetail();
    }

    private void initViews() {
        tvOrderCode = findViewById(R.id.tv_order_code);
        tvOrderDate = findViewById(R.id.tv_order_date);
        tvOrderStatus = findViewById(R.id.tv_order_status);
        tvFullName = findViewById(R.id.tv_full_name);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvEmail = findViewById(R.id.tv_email);
        tvShippingAddress = findViewById(R.id.tv_shipping_address);
        tvPaymentMethod = findViewById(R.id.tv_payment_method);
        tvTotalAmount = findViewById(R.id.tv_total_amount);
        recyclerViewOrderItems = findViewById(R.id.recyclerViewOrderItems);
        progressBar = findViewById(R.id.progressBar);

        // Setup toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chi tiết đơn hàng");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecyclerView() {
        orderItemAdapter = new OrderItemAdapter(this);
        recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrderItems.setAdapter(orderItemAdapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(OrderDetailViewModel.class);

        // Observe order detail
        viewModel.getOrderDetail().observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;

                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        if (resource.getData() != null && resource.getData().getData() != null) {
                            displayOrderDetail(resource.getData().getData());
                        }
                        break;

                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Lỗi: " + resource.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void loadOrderDetail() {
        viewModel.loadOrderDetail(orderId);
    }

    private void displayOrderDetail(OrderResponse.OrderData orderData) {
        // Order code and status
        tvOrderCode.setText(orderData.getOrderCode());
        tvOrderStatus.setText(getStatusText(orderData.getStatus()));
        setStatusColor(orderData.getStatus());

        // Order date
        tvOrderDate.setText(formatDate(orderData.getOrderDate()));

        // Customer info
        tvFullName.setText(orderData.getFullName());
        tvPhoneNumber.setText(orderData.getPhoneNumber());
        tvEmail.setText(orderData.getEnail());
        tvShippingAddress.setText(orderData.getShippingAddress());

        // Payment method
        tvPaymentMethod.setText(orderData.getPaymentMethod());

        // Total amount
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvTotalAmount.setText(formatter.format(orderData.getTotalAmount()));

        // Order items - Convert OrderResponse.OrderItem to standalone OrderItem
        if (orderData.getOrderItems() != null && !orderData.getOrderItems().isEmpty()) {
            List<com.example.prm392_mobile_carlinker.data.model.order.OrderItem> convertedItems = new ArrayList<>();
            for (OrderResponse.OrderItem responseItem : orderData.getOrderItems()) {
                com.example.prm392_mobile_carlinker.data.model.order.OrderItem item =
                    new com.example.prm392_mobile_carlinker.data.model.order.OrderItem();

                // Convert ProductVariant
                if (responseItem.getProductVariant() != null) {
                    com.example.prm392_mobile_carlinker.data.model.order.OrderItem.ProductVariant variant =
                        new com.example.prm392_mobile_carlinker.data.model.order.OrderItem.ProductVariant();

                    OrderResponse.ProductVariant responseVariant = responseItem.getProductVariant();
                    variant.setId(responseVariant.getId());
                    variant.setName(responseVariant.getName());
                    variant.setSku(responseVariant.getSku());
                    variant.setPrice(responseVariant.getPrice());
                    variant.setStockQuantity(responseVariant.getStockQuantity());
                    variant.setHoldQuantity(responseVariant.getHoldQuantity());
                    variant.setIsDefault(responseVariant.isDefault());
                    variant.setSelectedOptionValueIds(responseVariant.getSelectedOptionValueIds());

                    item.setProductVariant(variant);
                }

                item.setQuantity(responseItem.getQuantity());
                item.setUnitPrice(responseItem.getUnitPrice());
                item.setSubtotal(responseItem.getSubtotal());

                convertedItems.add(item);
            }
            orderItemAdapter.setOrderItems(convertedItems);
        }
    }

    private String getStatusText(int status) {
        switch (status) {
            case 0: return "Chờ xác nhận";
            case 1: return "Đã xác nhận";
            case 2: return "Đã đóng gói";
            case 3: return "Đang giao hàng";
            case 4: return "Đã giao thành công";
            case 5: return "Đã hủy";
            case 6: return "Giao thất bại";
            default: return "Không xác định";
        }
    }

    private String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return dateString;
        }
    }

    private void setStatusColor(int status) {
        int color;
        switch (status) {
            case 0: // PENDING - Chờ xác nhận
                color = getResources().getColor(android.R.color.holo_orange_dark);
                break;
            case 1: // CONFIRMED - Đã xác nhận
                color = getResources().getColor(android.R.color.holo_blue_dark);
                break;
            case 2: // PACKED - Đã đóng gói
                color = getResources().getColor(android.R.color.holo_blue_light);
                break;
            case 3: // SHIPPING - Đang giao
                color = getResources().getColor(android.R.color.holo_purple);
                break;
            case 4: // DELIVERED - Đã giao
                color = getResources().getColor(android.R.color.holo_green_dark);
                break;
            case 5: // CANCELLED - Đã hủy
                color = getResources().getColor(android.R.color.holo_red_dark);
                break;
            case 6: // FAILED - Giao thất bại
                color = getResources().getColor(android.R.color.holo_red_light);
                break;
            default:
                color = getResources().getColor(android.R.color.darker_gray);
        }
        tvOrderStatus.setTextColor(color);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
