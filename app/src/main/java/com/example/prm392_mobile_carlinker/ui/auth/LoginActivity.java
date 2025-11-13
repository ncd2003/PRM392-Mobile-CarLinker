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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private Button btnLogin;
    private Button btnContinueAsGuest;
    private TextView tvForgotPassword;
    private TextView tvRegister;
    private ProgressBar progressBar;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnContinueAsGuest = findViewById(R.id.btnContinueAsGuest);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());

        btnContinueAsGuest.setOnClickListener(v -> continueAsGuest());

        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng quên mật khẩu đang phát triển", Toast.LENGTH_SHORT).show();
        });

        tvRegister.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng đăng ký đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validation
        if (email.isEmpty()) {
            etEmail.setError("Vui lòng nhập email");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return;
        }

        // Show loading
        showLoading(true);

        // Call API login
        callLoginApi(email, password);
    }

    private void callLoginApi(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);

        RetrofitClient.getApiService().login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.getStatus() == 200 && loginResponse.getData() != null) {
                        LoginResponse.Data data = loginResponse.getData();
                        LoginResponse.User user = data.getUser();

                        // Lưu thông tin đăng nhập với accessToken thực
                        String accessToken = data.getAccessToken();
                        int userId = user.getId();
                        String userEmail = user.getEmail();
                        String userName = userEmail.split("@")[0]; // Lấy phần trước @ làm tên
                        String userRole = user.getRole(); // Role là string: "CUSTOMER", "DEALER", etc.

                        Log.d(TAG, "Login successful!");
                        Log.d(TAG, "User ID: " + userId);
                        Log.d(TAG, "Email: " + userEmail);
                        Log.d(TAG, "Role: " + userRole);
                        Log.d(TAG, "Access Token: " + accessToken);

                        // Lưu session với accessToken thực và role string
                        sessionManager.createLoginSession(userId, userEmail, userName, userRole, accessToken);

                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                        // Navigate to HomeActivity
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Đăng nhập thất bại: " + loginResponse.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Lỗi: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "Login failed", t);
                Toast.makeText(LoginActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void continueAsGuest() {
        // Navigate to HomeActivity as guest (without saving session)
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
            btnContinueAsGuest.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
            btnContinueAsGuest.setEnabled(true);
        }
    }
}
