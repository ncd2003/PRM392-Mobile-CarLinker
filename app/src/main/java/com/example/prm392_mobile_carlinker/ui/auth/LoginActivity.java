package com.example.prm392_mobile_carlinker.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.auth.LoginRequest;
import com.example.prm392_mobile_carlinker.data.model.auth.LoginResponse;
import com.example.prm392_mobile_carlinker.data.remote.ApiService;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;
import com.example.prm392_mobile_carlinker.ui.shop.ProductListActivity;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;

    private ApiService apiService;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "app_prefs";
    private static final String KEY_TOKEN = "auth_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        apiService = RetrofitClient.getApiService();
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (!isValidEmail(email)) {
            etEmail.setError("Email không hợp lệ");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Nhập mật khẩu");
            etPassword.requestFocus();
            return;
        }

        // Hiển thị progress
        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        LoginRequest request = new LoginRequest(email, password);
        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse body = response.body();
                    // Nếu backend dùng status code trong body
                    if (body.getStatus() == 200) {
                        String token = null;
                        if (body.getData() != null) token = body.getData().getAccessToken();

                        if (token != null && !token.isEmpty()) {
                            saveToken(token);
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            // Chuyển sang MainActivity (thay bằng activity bạn muốn)
                            Intent intent = new Intent(LoginActivity.this, ProductListActivity.class);
                            // nếu bạn có MainActivity thay vào trên
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Token bị thiếu trong response", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, body.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Có thể backend trả error body hoặc status code khác
                    Toast.makeText(LoginActivity.this, "Lỗi: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    // helper
    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    // optionally expose token
    public static String getSavedToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_TOKEN, null);
    }
}
