package com.example.prm392_mobile_carlinker.ui.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.user.User;

public class UserInfoActivity extends AppCompatActivity {

    private ImageView ivUserAvatar;
    private TextView tvUserName;
    private TextView tvUserRole;
    private TextView tvEmail;
    private TextView tvPhoneNumber;
    private TextView tvUserId;
    private TextView tvUserStatus;
    private Button btnEditProfile;
    private Button btnChangePassword;
    private ProgressBar progressBar;
    private TextView tvError;
    private LinearLayout layoutContent;

    private UserInfoViewModel viewModel;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // Get user ID from intent or SharedPreferences
        userId = getIntent().getIntExtra("USER_ID", -1);

        if (userId == -1) {
            // TODO: Get from SharedPreferences
            userId = 4; // Default for testing
        }

        initViews();
        setupToolbar();
        setupViewModel();
        setupButtons();
        loadUserInfo();
    }

    private void initViews() {
        ivUserAvatar = findViewById(R.id.ivUserAvatar);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserRole = findViewById(R.id.tvUserRole);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvUserId = findViewById(R.id.tvUserId);
        tvUserStatus = findViewById(R.id.tvUserStatus);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        progressBar = findViewById(R.id.progressBar);
        tvError = findViewById(R.id.tvError);
        layoutContent = findViewById(R.id.layoutContent);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Thông tin cá nhân");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);
    }

    private void setupButtons() {
        btnEditProfile.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng chỉnh sửa thông tin đang phát triển", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to EditProfileActivity
        });

        btnChangePassword.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng đổi mật khẩu đang phát triển", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to ChangePasswordActivity
        });
    }

    private void loadUserInfo() {
        viewModel.getUserById(userId).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        showLoading();
                        break;
                    case SUCCESS:
                        showSuccess(resource.getData());
                        break;
                    case ERROR:
                        showError(resource.getMessage());
                        break;
                }
            }
        });
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        layoutContent.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
    }

    private void showSuccess(User user) {
        progressBar.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
        layoutContent.setVisibility(View.VISIBLE);

        if (user != null) {
            // Set user name
            tvUserName.setText(user.getFullName());

            // Set user role
            tvUserRole.setText(user.getUserRoleText());

            // Set email
            tvEmail.setText(user.getEmail() != null ? user.getEmail() : "Chưa cập nhật");

            // Set phone number
            tvPhoneNumber.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "Chưa cập nhật");

            // Set user ID
            tvUserId.setText("#" + user.getId());

            // Set user status
            tvUserStatus.setText(user.getUserStatusText());
            if (user.getUserStatus() == 0) {
                tvUserStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else {
                tvUserStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }

            // Load avatar
            if (user.getImage() != null && !user.getImage().isEmpty()) {
                Glide.with(this)
                        .load(user.getImage())
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(ivUserAvatar);
            } else {
                // Set default avatar
                ivUserAvatar.setImageResource(R.drawable.ic_launcher_background);
            }
        }
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        layoutContent.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(message != null ? message : "Lỗi khi tải thông tin người dùng");
    }
}

