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
import com.example.prm392_mobile_carlinker.data.model.auth.PartnerRegisterRequest;
import com.example.prm392_mobile_carlinker.data.model.auth.PartnerRegisterResponse;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;
import com.example.prm392_mobile_carlinker.ui.home.HomeActivity;
import com.example.prm392_mobile_carlinker.util.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnerRegisterActivity extends AppCompatActivity {

    private static final String TAG = "PartnerRegisterActivity";

    private Toolbar toolbar;

    // Personal Information
    private TextInputEditText etFullName;
    private TextInputEditText etEmail;
    private TextInputEditText etPhoneNumber;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;

    // Garage Information
    private TextInputEditText etGarageName;
    private TextInputEditText etGarageEmail;
    private TextInputEditText etGaragePhoneNumber;
    private TextInputEditText etOperatingTime;
    private TextInputEditText etDescription;
    private TextInputEditText etLatitude;
    private TextInputEditText etLongitude;

    private Button btnPartnerRegister;
    private TextView tvBackToLogin;
    private ProgressBar progressBar;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_register);

        sessionManager = new SessionManager(this);

        initViews();
        setupToolbar();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);

        // Personal Information
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        // Garage Information
        etGarageName = findViewById(R.id.etGarageName);
        etGarageEmail = findViewById(R.id.etGarageEmail);
        etGaragePhoneNumber = findViewById(R.id.etGaragePhoneNumber);
        etOperatingTime = findViewById(R.id.etOperatingTime);
        etDescription = findViewById(R.id.etDescription);
        etLatitude = findViewById(R.id.etLatitude);
        etLongitude = findViewById(R.id.etLongitude);

        btnPartnerRegister = findViewById(R.id.btnPartnerRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupListeners() {
        btnPartnerRegister.setOnClickListener(v -> handlePartnerRegister());
        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void handlePartnerRegister() {
        // Get personal information
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Get garage information
        String garageName = etGarageName.getText().toString().trim();
        String garageEmail = etGarageEmail.getText().toString().trim();
        String garagePhoneNumber = etGaragePhoneNumber.getText().toString().trim();
        String operatingTime = etOperatingTime.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String latitude = etLatitude.getText().toString().trim();
        String longitude = etLongitude.getText().toString().trim();

        // Validation - Personal Information
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

        if (phoneNumber.isEmpty()) {
            etPhoneNumber.setError("Vui lòng nhập số điện thoại");
            etPhoneNumber.requestFocus();
            return;
        }

        if (phoneNumber.length() > 20) {
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

        // Validation - Garage Information
        if (garageName.isEmpty()) {
            etGarageName.setError("Vui lòng nhập tên garage");
            etGarageName.requestFocus();
            return;
        }

        if (garageName.length() > 200) {
            etGarageName.setError("Tên garage không được vượt quá 200 ký tự");
            etGarageName.requestFocus();
            return;
        }

        if (garageEmail.isEmpty()) {
            etGarageEmail.setError("Vui lòng nhập email garage");
            etGarageEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(garageEmail).matches()) {
            etGarageEmail.setError("Email garage không hợp lệ");
            etGarageEmail.requestFocus();
            return;
        }

        if (garageEmail.length() > 100) {
            etGarageEmail.setError("Email garage không được vượt quá 100 ký tự");
            etGarageEmail.requestFocus();
            return;
        }

        if (garagePhoneNumber.isEmpty()) {
            etGaragePhoneNumber.setError("Vui lòng nhập số điện thoại garage");
            etGaragePhoneNumber.requestFocus();
            return;
        }

        if (garagePhoneNumber.length() > 20) {
            etGaragePhoneNumber.setError("Số điện thoại garage không được vượt quá 20 ký tự");
            etGaragePhoneNumber.requestFocus();
            return;
        }

        if (operatingTime.isEmpty()) {
            etOperatingTime.setError("Vui lòng nhập giờ hoạt động");
            etOperatingTime.requestFocus();
            return;
        }

        if (operatingTime.length() > 100) {
            etOperatingTime.setError("Giờ hoạt động không được vượt quá 100 ký tự");
            etOperatingTime.requestFocus();
            return;
        }

        if (description.length() > 1000) {
            etDescription.setError("Mô tả không được vượt quá 1000 ký tự");
            etDescription.requestFocus();
            return;
        }

        // Validate Latitude (optional, max 50 characters)
        if (!latitude.isEmpty() && latitude.length() > 50) {
            etLatitude.setError("Vĩ độ không được vượt quá 50 ký tự");
            etLatitude.requestFocus();
            return;
        }

        // Validate Longitude (optional, max 50 characters)
        if (!longitude.isEmpty() && longitude.length() > 50) {
            etLongitude.setError("Kinh độ không được vượt quá 50 ký tự");
            etLongitude.requestFocus();
            return;
        }

        // Show loading
        showLoading(true);

        // Call API partner register
        callPartnerRegisterApi(fullName, email, phoneNumber, password, confirmPassword,
                garageName, garageEmail, garagePhoneNumber, operatingTime, description, latitude, longitude);
    }

    private void callPartnerRegisterApi(String fullName, String email, String phoneNumber,
                                        String password, String confirmPassword,
                                        String garageName, String garageEmail, String garagePhoneNumber,
                                        String operatingTime, String description, String latitude, String longitude) {

        PartnerRegisterRequest request = new PartnerRegisterRequest();

        // Set personal information
        request.setFullName(fullName);
        request.setEmail(email);
        request.setPhoneNumber(phoneNumber);
        request.setPassword(password);
        request.setConfirmPassword(confirmPassword);

        // Set garage information
        request.setGarageName(garageName);
        request.setGarageEmail(garageEmail);
        request.setGaragePhoneNumber(garagePhoneNumber);
        request.setOperatingTime(operatingTime);
        request.setDescription(description.isEmpty() ? null : description);
        request.setLatitude(latitude);
        request.setLongitude(longitude);

        RetrofitClient.getApiService().partnerRegister(request).enqueue(new Callback<PartnerRegisterResponse>() {
            @Override
            public void onResponse(Call<PartnerRegisterResponse> call, Response<PartnerRegisterResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    PartnerRegisterResponse registerResponse = response.body();

                    if (registerResponse.getStatusCode() == 201 && registerResponse.getData() != null) {
                        PartnerRegisterResponse.PartnerData data = registerResponse.getData();

                        // Lấy thông tin đăng ký
                        String accessToken = data.getAccessToken();
                        int userId = data.getUserId();
                        String userEmail = data.getUserEmail();
                        String userName = userEmail.split("@")[0];
                        String userRole = data.getUserRoleString(); // Use getUserRoleString() instead of getUserRole()

                        Log.d(TAG, "Partner register successful!");
                        Log.d(TAG, "User ID: " + userId);
                        Log.d(TAG, "Email: " + userEmail);
                        Log.d(TAG, "Role: " + userRole);
                        Log.d(TAG, "Garage ID: " + data.getGarageId());
                        Log.d(TAG, "Garage Name: " + data.getGarageName());
                        Log.d(TAG, "Access Token: " + accessToken);

                        // Không lưu session - yêu cầu đăng nhập lại
                        // sessionManager.createLoginSession(userId, userEmail, userName, userRole, accessToken);

                        Toast.makeText(PartnerRegisterActivity.this,
                                "Đăng ký đối tác thành công!\nVui lòng đăng nhập để tiếp tục",
                                Toast.LENGTH_LONG).show();

                        // Navigate to LoginActivity
                        Intent intent = new Intent(PartnerRegisterActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(PartnerRegisterActivity.this,
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
                    Toast.makeText(PartnerRegisterActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PartnerRegisterResponse> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "Partner register failed", t);
                Toast.makeText(PartnerRegisterActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnPartnerRegister.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnPartnerRegister.setEnabled(true);
        }
    }

    /**
     * Tự động tạo Garage sau khi đăng ký Partner thành công
     */
    private void createGarageAfterRegister(String garageName, String garageEmail,
                                           String garagePhoneNumber, String operatingTime,
                                           String description) {
        showLoading(true);

        // Chuẩn bị RequestBody cho Multipart
        okhttp3.RequestBody nameBody = okhttp3.RequestBody.create(
                okhttp3.MediaType.parse("text/plain"), garageName);
        okhttp3.RequestBody emailBody = okhttp3.RequestBody.create(
                okhttp3.MediaType.parse("text/plain"), garageEmail);
        okhttp3.RequestBody descriptionBody = okhttp3.RequestBody.create(
                okhttp3.MediaType.parse("text/plain"), description != null ? description : "");
        okhttp3.RequestBody operatingTimeBody = okhttp3.RequestBody.create(
                okhttp3.MediaType.parse("text/plain"), operatingTime);
        okhttp3.RequestBody phoneNumberBody = okhttp3.RequestBody.create(
                okhttp3.MediaType.parse("text/plain"), garagePhoneNumber);
        okhttp3.RequestBody latitudeBody = okhttp3.RequestBody.create(
                okhttp3.MediaType.parse("text/plain"), "0"); // Default location
        okhttp3.RequestBody longitudeBody = okhttp3.RequestBody.create(
                okhttp3.MediaType.parse("text/plain"), "0"); // Default location

        // Không có image file
        okhttp3.MultipartBody.Part imageFilePart = null;

        RetrofitClient.getApiService().createGarage(
                nameBody, emailBody, descriptionBody, operatingTimeBody,
                phoneNumberBody, latitudeBody, longitudeBody, imageFilePart
        ).enqueue(new Callback<com.example.prm392_mobile_carlinker.data.model.garage.GarageCreateResponse>() {
            @Override
            public void onResponse(Call<com.example.prm392_mobile_carlinker.data.model.garage.GarageCreateResponse> call,
                                 Response<com.example.prm392_mobile_carlinker.data.model.garage.GarageCreateResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Garage created successfully!");

                    Toast.makeText(PartnerRegisterActivity.this,
                            "Đăng ký đối tác và tạo garage thành công!",
                            Toast.LENGTH_LONG).show();

                    // Navigate to Home
                    Intent intent = new Intent(PartnerRegisterActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e(TAG, "Failed to create garage: " + response.code());
                    Toast.makeText(PartnerRegisterActivity.this,
                            "Đăng ký thành công nhưng không thể tạo garage. Vui lòng tạo thủ công.",
                            Toast.LENGTH_LONG).show();

                    // Vẫn navigate về Home
                    Intent intent = new Intent(PartnerRegisterActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<com.example.prm392_mobile_carlinker.data.model.garage.GarageCreateResponse> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "Error creating garage", t);

                Toast.makeText(PartnerRegisterActivity.this,
                        "Đăng ký thành công nhưng không thể tạo garage. Vui lòng tạo thủ công.",
                        Toast.LENGTH_LONG).show();

                // Vẫn navigate về Home
                Intent intent = new Intent(PartnerRegisterActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
