package com.example.prm392_mobile_carlinker.ui.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.order.OrderResponse;
import com.example.prm392_mobile_carlinker.ui.payment.VNPayActivity;
import com.example.prm392_mobile_carlinker.ui.shop.ProductListActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    public static final String EXTRA_TOTAL_AMOUNT = "TOTAL_AMOUNT";

    private TextInputEditText etFullName;
    private TextInputEditText etEmail;
    private TextInputEditText etPhoneNumber;
    private TextInputEditText etShippingAddress;
    private RadioGroup rgPaymentMethod;
    private TextView tvSubtotal;
    private TextView tvTotalAmount;
    private Button btnPlaceOrder;
    private ProgressBar progressBar;

    private CheckoutViewModel viewModel;
    private double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Get total amount from intent
        totalAmount = getIntent().getDoubleExtra(EXTRA_TOTAL_AMOUNT, 0);

        // Initialize views
        initViews();

        // Setup ViewModel
        setupViewModel();

        // Display total amount
        displayTotalAmount();
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etShippingAddress = findViewById(R.id.etShippingAddress);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        progressBar = findViewById(R.id.progressBar);

        // Setup toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thanh toán");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Setup button click
        btnPlaceOrder.setOnClickListener(v -> validateAndPlaceOrder());
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);
    }

    private void displayTotalAmount() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedAmount = formatter.format(totalAmount);
        tvSubtotal.setText(formattedAmount);
        tvTotalAmount.setText(formattedAmount);
    }

    private void validateAndPlaceOrder() {
        // Get input values
        String fullName = etFullName.getText() != null ? etFullName.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String phoneNumber = etPhoneNumber.getText() != null ? etPhoneNumber.getText().toString().trim() : "";
        String shippingAddress = etShippingAddress.getText() != null ? etShippingAddress.getText().toString().trim() : "";

        // Validate inputs
        if (fullName.isEmpty()) {
            etFullName.setError("Vui lòng nhập họ tên");
            etFullName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Vui lòng nhập email");
            etEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            etEmail.requestFocus();
            return;
        }

        if (phoneNumber.isEmpty()) {
            etPhoneNumber.setError("Vui lòng nhập số điện thoại");
            etPhoneNumber.requestFocus();
            return;
        }

        if (phoneNumber.length() < 10) {
            etPhoneNumber.setError("Số điện thoại không hợp l��");
            etPhoneNumber.requestFocus();
            return;
        }

        if (shippingAddress.isEmpty()) {
            etShippingAddress.setError("Vui lòng nhập địa chỉ giao hàng");
            etShippingAddress.requestFocus();
            return;
        }

        // Get payment method
        String paymentMethod = getSelectedPaymentMethod();

        // Place order
        placeOrder(fullName, email, shippingAddress, phoneNumber, paymentMethod);
    }

    private String getSelectedPaymentMethod() {
        int selectedId = rgPaymentMethod.getCheckedRadioButtonId();
        if (selectedId == R.id.rbCOD) {
            return "COD";
        } else if (selectedId == R.id.rbVNPay) {
            return "vnpay";
        } else if (selectedId == R.id.rbBankTransfer) {
            return "Bank Transfer";
        } else if (selectedId == R.id.rbCreditCard) {
            return "Credit Card";
        }
        return "COD"; // Default
    }

    private void placeOrder(String fullName, String email, String shippingAddress,
                           String phoneNumber, String paymentMethod) {
        btnPlaceOrder.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        viewModel.createOrder(fullName, email, shippingAddress, phoneNumber, paymentMethod)
                .observe(this, resource -> {
                    if (resource != null) {
                        switch (resource.getStatus()) {
                            case LOADING:
                                // Already showing progress
                                break;

                            case SUCCESS:
                                if (resource.getData() != null && resource.getData().getData() != null) {
                                    OrderResponse.OrderData orderData = resource.getData().getData();

                                    // Check if payment method is VNPay
                                    if ("vnpay".equalsIgnoreCase(paymentMethod)) {
                                        // Call VNPay API to get payment URL
                                        createVNPayPayment(orderData.getId(), orderData.getTotalAmount(),
                                                         "Thanh toán đơn hàng " + orderData.getOrderCode());
                                    } else {
                                        // For COD or other methods, show success dialog
                                        progressBar.setVisibility(View.GONE);
                                        btnPlaceOrder.setEnabled(true);
                                        showOrderSuccessDialog(orderData.getOrderCode());
                                    }
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    btnPlaceOrder.setEnabled(true);
                                }
                                break;

                            case ERROR:
                                progressBar.setVisibility(View.GONE);
                                btnPlaceOrder.setEnabled(true);
                                Toast.makeText(this, "Lỗi: " + resource.getMessage(),
                                        Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
    }

    private void createVNPayPayment(int orderId, double totalAmount, String description) {
        // Keep progress bar visible
        viewModel.createVNPayPayment(orderId, totalAmount, description)
                .observe(this, resource -> {
                    if (resource != null) {
                        switch (resource.getStatus()) {
                            case LOADING:
                                // Already showing progress
                                break;

                            case SUCCESS:
                                progressBar.setVisibility(View.GONE);
                                btnPlaceOrder.setEnabled(true);
                                if (resource.getData() != null &&
                                    resource.getData().getPaymentUrl() != null &&
                                    !resource.getData().getPaymentUrl().isEmpty()) {
                                    // Open VNPay payment page - truyền orderId (số nguyên)
                                    openVNPayPayment(resource.getData().getPaymentUrl(), String.valueOf(orderId));
                                } else {
                                    Toast.makeText(this, "Không thể tạo URL thanh toán",
                                            Toast.LENGTH_SHORT).show();
                                }
                                break;

                            case ERROR:
                                progressBar.setVisibility(View.GONE);
                                btnPlaceOrder.setEnabled(true);
                                Toast.makeText(this, "Lỗi tạo thanh toán: " + resource.getMessage(),
                                             Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
    }

    private void openVNPayPayment(String paymentUrl, String orderId) {
        Intent intent = new Intent(this, VNPayActivity.class);
        intent.putExtra(VNPayActivity.EXTRA_PAYMENT_URL, paymentUrl);
        intent.putExtra(VNPayActivity.EXTRA_ORDER_ID, orderId);
        startActivity(intent);
        finish(); // Close checkout activity
    }

    private void showOrderSuccessDialog(String orderNumber) {
        new AlertDialog.Builder(this)
                .setTitle("Đặt hàng thành công!")
                .setMessage("Mã đơn hàng: " + orderNumber + "\n\nCảm ơn bạn đã đặt hàng. Chúng tôi sẽ liên hệ với bạn sớm nhất.")
                .setPositiveButton("Về trang chủ", (dialog, which) -> {
                    // Navigate back to product list and clear back stack
                    Intent intent = new Intent(CheckoutActivity.this, ProductListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
