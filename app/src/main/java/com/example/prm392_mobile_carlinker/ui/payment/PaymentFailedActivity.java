package com.example.prm392_mobile_carlinker.ui.payment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.ui.order.MyOrdersActivity;
import com.example.prm392_mobile_carlinker.ui.shop.ProductListActivity;

public class PaymentFailedActivity extends AppCompatActivity {

    private static final String TAG = "PaymentFailedActivity";
    private TextView tvOrderId;
    private Button btnRetryPayment;
    private Button btnViewOrder;
    private Button btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_failed);

        Log.d(TAG, "PaymentFailedActivity created");

        initViews();
        handleIntent(getIntent());
        setupButtons();
        setupBackPressHandler();
    }

    private void initViews() {
        tvOrderId = findViewById(R.id.tvOrderId);
        btnRetryPayment = findViewById(R.id.btn_retry_payment);
        btnViewOrder = findViewById(R.id.btn_view_order);
        btnBackHome = findViewById(R.id.btn_back_home);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String orderId;
        Uri data = intent.getData();
        if (data != null) {
            orderId = data.getQueryParameter("orderId");
            Log.d(TAG, "Order ID from URI: " + orderId);
        } else {
            orderId = intent.getStringExtra("ORDER_ID");
            Log.d(TAG, "Order ID from Intent: " + orderId);
        }

        if (orderId == null || orderId.isEmpty()) {
            orderId = "-";
        }
        tvOrderId.setText(orderId);
    }

    private void setupButtons() {
        // Nút thử thanh toán lại - Ẩn tạm thời vì cần implement logic phức tạp hơn
        btnRetryPayment.setEnabled(false);
        btnRetryPayment.setAlpha(0.5f);

        // Nút xem đơn hàng
        btnViewOrder.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyOrdersActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Nút về trang chủ
        btnBackHome.setOnClickListener(v -> {
            navigateToHome();
        });
    }

    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateToHome();
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
