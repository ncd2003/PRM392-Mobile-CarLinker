package com.example.prm392_mobile_carlinker.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.auth.LoginResponse;
import com.example.prm392_mobile_carlinker.data.model.auth.RegisterRequest;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;
import com.example.prm392_mobile_carlinker.ui.home.HomeActivity;
import com.example.prm392_mobile_carlinker.util.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private Toolbar toolbar;
    private TextInputEditText etFullName;
    private TextInputEditText etEmail;
    private TextInputEditText etPhoneNumber;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;
    private Button btnRegister;
    private TextView tvBackToLogin;
    private ProgressBar progressBar;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sessionManager = new SessionManager(this);

        initViews();
        setupToolbar();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> handleRegister());

        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void handleRegister() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validation
        if (fullName.isEmpty()) {
            etFullName.setError("Vui lòng nhập họ và tên");
            etFullName.requestFocus();
            return;
        }

        if (fullName.length() > 100) {
            etFullName.setError("Họ và tên không được vượt quá 100 ký tự");
            etFullName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Vui lòng nhập email");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            etEmail.requestFocus();
            return;
        }

        if (email.length() > 100) {
            etEmail.setError("Email không được vượt quá 100 ký tự");
            etEmail.requestFocus();
            return;
        }

        // Phone number is optional, but validate if provided
        if (!phoneNumber.isEmpty() && phoneNumber.length() > 20) {
            etPhoneNumber.setError("Số điện thoại không được vượt quá 20 ký tự");
            etPhoneNumber.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etPassword.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            etConfirmPassword.requestFocus();
            return;
        }

        // Show loading
        showLoading(true);

        // Call API register
        callRegisterApi(fullName, email, phoneNumber, password, confirmPassword);
    }

    private void callRegisterApi(String fullName, String email, String phoneNumber,
                                  String password, String confirmPassword) {
        RegisterRequest request = new RegisterRequest(fullName, email, phoneNumber, password, confirmPassword);

        RetrofitClient.getApiService().register(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse registerResponse = response.body();

                    if (registerResponse.getStatus() == 201 && registerResponse.getData() != null) {
                        LoginResponse.Data data = registerResponse.getData();
                        LoginResponse.User user = data.getUser();

                        // Lấy thông tin đăng ký
                        String accessToken = data.getAccessToken();
                        int userId = user.getId();
                        String userEmail = user.getEmail();
                        String userName = userEmail.split("@")[0];
                        String userRole = user.getRole(); // "CUSTOMER"

                        Log.d(TAG, "Register successful!");
                        Log.d(TAG, "User ID: " + userId);
                        Log.d(TAG, "Email: " + userEmail);
                        Log.d(TAG, "Role: " + userRole);
                        Log.d(TAG, "Access Token: " + accessToken);

                        // Lưu session
                        sessionManager.createLoginSession(userId, userEmail, userName, userRole, accessToken);

                        Toast.makeText(RegisterActivity.this,
                            "Đăng ký thành công! Chào mừng " + fullName,
                            Toast.LENGTH_SHORT).show();

                        // Navigate to HomeActivity
                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Đăng ký thất bại: " + registerResponse.getMessage(),
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
                    Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "Register failed", t);
                Toast.makeText(RegisterActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnRegister.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnRegister.setEnabled(true);
        }
    }
}

