package com.example.prm392_mobile_carlinker.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.auth.LoginRequest;
import com.example.prm392_mobile_carlinker.data.model.auth.LoginResponse;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;
import com.example.prm392_mobile_carlinker.ui.home.HomeActivity;
import com.example.prm392_mobile_carlinker.util.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffLoginActivity extends AppCompatActivity {

    private static final String TAG = "StaffLoginActivity";

    private Toolbar toolbar;
    private TextInputEditText etStaffEmail;
    private TextInputEditText etStaffPassword;
    private Button btnStaffLogin;
    private TextView tvBackToCustomerLogin;
    private ProgressBar progressBar;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_login);

        sessionManager = new SessionManager(this);

        initViews();
        setupToolbar();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etStaffEmail = findViewById(R.id.etStaffEmail);
        etStaffPassword = findViewById(R.id.etStaffPassword);
        btnStaffLogin = findViewById(R.id.btnStaffLogin);
        tvBackToCustomerLogin = findViewById(R.id.tvBackToCustomerLogin);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupListeners() {
        btnStaffLogin.setOnClickListener(v -> handleStaffLogin());

        tvBackToCustomerLogin.setOnClickListener(v -> finish());
    }

    private void handleStaffLogin() {
        String email = etStaffEmail.getText().toString().trim();
        String password = etStaffPassword.getText().toString().trim();

        // Validation
        if (email.isEmpty()) {
            etStaffEmail.setError("Vui lòng nhập email");
            etStaffEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etStaffPassword.setError("Vui lòng nhập mật khẩu");
            etStaffPassword.requestFocus();
            return;
        }

        // Show loading
        showLoading(true);

        // Call API staff login
        callStaffLoginApi(email, password);
    }

    private void callStaffLoginApi(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);

        RetrofitClient.getApiService().staffLogin(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.getStatus() == 200 && loginResponse.getData() != null) {
                        LoginResponse.Data data = loginResponse.getData();
                        LoginResponse.User user = data.getUser();

                        // Lấy thông tin đăng nhập
                        String accessToken = data.getAccessToken();
                        int userId = user.getId();
                        String userEmail = user.getEmail();
                        String userName = userEmail.split("@")[0];
                        String userRole = user.getRole(); // "STAFF" hoặc role tương ứng
                        Integer garageId = user.getGarageId(); // Lấy garageId từ response

                        Log.d(TAG, "Staff login successful!");
                        Log.d(TAG, "User ID: " + userId);
                        Log.d(TAG, "Email: " + userEmail);
                        Log.d(TAG, "Role: " + userRole);
                        Log.d(TAG, "Garage ID: " + garageId);
                        Log.d(TAG, "Access Token: " + accessToken);

                        // Kiểm tra nếu là STAFF hoặc GARAGE nhưng không có garageId
                        if (("STAFF".equalsIgnoreCase(userRole) || "GARAGE".equalsIgnoreCase(userRole)) && garageId == null) {
                            Log.e(TAG, "ERROR: Staff/Garage user has no garageId!");
                            Toast.makeText(StaffLoginActivity.this,
                                "Lỗi: Tài khoản chưa được gán vào garage nào. Vui lòng liên hệ quản trị viên.",
                                Toast.LENGTH_LONG).show();
                            return;
                        }

                        // Lưu session với garageId
                        sessionManager.createLoginSession(userId, userEmail, userName, userRole, garageId, accessToken);

                        Toast.makeText(StaffLoginActivity.this,
                            "Đăng nhập nhân viên thành công!",
                            Toast.LENGTH_SHORT).show();

                        // Navigate to HomeActivity (hoặc StaffHomeActivity nếu có)
                        Intent intent = new Intent(StaffLoginActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(StaffLoginActivity.this,
                                "Đăng nhập thất bại: " + loginResponse.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMsg = "Lỗi: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMsg = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing error body", e);
                    }
                    Toast.makeText(StaffLoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "Staff login failed", t);
                Toast.makeText(StaffLoginActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnStaffLogin.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnStaffLogin.setEnabled(true);
        }
    }
}

