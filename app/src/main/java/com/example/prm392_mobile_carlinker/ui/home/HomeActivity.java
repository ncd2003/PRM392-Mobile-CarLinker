package com.example.prm392_mobile_carlinker.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.ui.auth.LoginActivity;
import com.example.prm392_mobile_carlinker.ui.garage.GarageListActivity;
import com.example.prm392_mobile_carlinker.ui.order.MyOrdersActivity;
import com.example.prm392_mobile_carlinker.ui.servicecategory.ServiceCategoryListActivity;
import com.example.prm392_mobile_carlinker.ui.shop.ProductListActivity;
import com.example.prm392_mobile_carlinker.util.SessionManager;
import com.google.android.material.card.MaterialCardView;

/**
 * HomeActivity - Màn hình chính của ứng dụng
 */
public class HomeActivity extends AppCompatActivity {

    private Button btnEmergency;
    private FrameLayout cardShop, cardBooking, cardGarage, cardSupport;
    private MaterialCardView btnProfileMenu;
    private TextView tvWelcomeUser;
    private Button btnLoginPrompt;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Khởi tạo các view
        initViews();

        // Thiết lập sự kiện click
        setupClickListeners();

        // Load user info hoặc hiển thị nút login
        updateUIBasedOnLoginStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật UI mỗi khi quay lại màn hình này
        updateUIBasedOnLoginStatus();
    }

    /**
     * Khởi tạo các view từ layout
     */
    private void initViews() {
        btnEmergency = findViewById(R.id.btn_emergency);
        cardShop = findViewById(R.id.card_shop);
        cardBooking = findViewById(R.id.card_booking);
        cardGarage = findViewById(R.id.card_garage);
        cardSupport = findViewById(R.id.card_support);
        btnProfileMenu = findViewById(R.id.btnProfileMenu);
        tvWelcomeUser = findViewById(R.id.tvWelcomeUser);
        btnLoginPrompt = findViewById(R.id.btnLoginPrompt);
    }

    /**
     * Cập nhật UI dựa trên trạng thái login
     */
    private void updateUIBasedOnLoginStatus() {
        if (sessionManager.isLoggedIn()) {
            // User đã login - hiển thị thông tin user
            btnProfileMenu.setVisibility(View.VISIBLE);
            btnLoginPrompt.setVisibility(View.GONE);
            loadUserInfo();
        } else {
            // User chưa login - hiển thị nút đăng nhập
            btnProfileMenu.setVisibility(View.GONE);
            btnLoginPrompt.setVisibility(View.VISIBLE);
            tvWelcomeUser.setText("Xin chào!");
        }
    }

    /**
     * Load thông tin user
     */
    private void loadUserInfo() {
        String userName = sessionManager.getUserName();
        tvWelcomeUser.setText("Xin chào, " + userName + "!");
    }

    /**
     * Thiết lập sự kiện click cho các nút
     */
    private void setupClickListeners() {
        // Login Button (khi chưa login)
        btnLoginPrompt.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Profile Menu Button (khi đã login)
        btnProfileMenu.setOnClickListener(v -> showProfileMenu(v));

        // Nút cứu hộ khẩn cấp
        btnEmergency.setOnClickListener(v -> handleEmergency());

        // Card Mua sắm linh kiện
        cardShop.setOnClickListener(v -> openShop());

        // Card Đặt lịch dịch vụ
        cardBooking.setOnClickListener(v -> openBooking());

        // Card Garage gần nhất
        cardGarage.setOnClickListener(v -> openNearbyGarage());

        // Card Hỗ trợ khách hàng
        cardSupport.setOnClickListener(v -> openSupport());
    }

    /**
     * Hiển thị menu profile với các tùy chọn
     */
    private void showProfileMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.menu_profile, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_user_info) {
                openUserInfo();
                return true;
            } else if (itemId == R.id.menu_vehicle_management) {
                openVehicleManagement();
                return true;
            } else if (itemId == R.id.menu_order_history) {
                openOrderHistory();
                return true;
            } else if (itemId == R.id.menu_service_category) {
                openServiceCategoryManagement();
                return true;
            } else if (itemId == R.id.menu_service_item) {
                openServiceItemManagement();
                return true;
            } else if (itemId == R.id.menu_settings) {
                openSettings();
                return true;
            } else if (itemId == R.id.menu_logout) {
                handleLogout();
                return true;
            }

            return false;
        });

        popup.show();
    }

    /**
     * Mở trang thông tin cá nhân
     */
    private void openUserInfo() {
        Intent intent = new Intent(this, com.example.prm392_mobile_carlinker.ui.user.UserInfoActivity.class);
        intent.putExtra("USER_ID", sessionManager.getUserId());
        startActivity(intent);
    }

    /**
     * Mở trang quản lý xe
     */
    private void openVehicleManagement() {
        Intent intent = new Intent(this, com.example.prm392_mobile_carlinker.ui.vehicle.VehicleListActivity.class);
        startActivity(intent);
    }

    /**
     * Mở trang lịch sử đơn hàng
     */
    private void openOrderHistory() {
        Intent intent = new Intent(this, MyOrdersActivity.class);
        startActivity(intent);
    }

    /**
     * Mở trang quản lý gói dịch vụ (Admin only)
     */
    private void openServiceCategoryManagement() {
        Intent intent = new Intent(this, ServiceCategoryListActivity.class);
        startActivity(intent);
    }

    /**
     * Mở trang quản lý dịch vụ (Admin only)
     */
    private void openServiceItemManagement() {
        Intent intent = new Intent(this, com.example.prm392_mobile_carlinker.ui.serviceitem.ServiceItemListActivity.class);
        startActivity(intent);
    }

    /**
     * Mở trang cài đặt
     */
    private void openSettings() {
        Toast.makeText(this, "Mở cài đặt", Toast.LENGTH_SHORT).show();
    }

    /**
     * Xử lý đăng xuất
     */
    private void handleLogout() {
        sessionManager.logout();
        Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        updateUIBasedOnLoginStatus();
    }

    /**
     * Xử lý chức năng cứu hộ khẩn cấp
     */
    private void handleEmergency() {
        Toast.makeText(this, R.string.toast_emergency, Toast.LENGTH_SHORT).show();
    }

    /**
     * Mở trang mua sắm linh kiện
     */
    private void openShop() {
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }

    /**
     * Mở trang đặt lịch dịch vụ
     */
    private void openBooking() {
        // Kiểm tra user đã login chưa
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Vui lòng đăng nhập để đặt lịch dịch vụ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        // Mở màn hình chọn garage trước
        Intent intent = new Intent(this, GarageListActivity.class);
        startActivity(intent);
    }

    /**
     * Mở trang tìm garage gần nhất
     */
    private void openNearbyGarage() {
        Intent intent = new Intent(this, GarageListActivity.class);
        startActivity(intent);
    }

    /**
     * Mở trang hỗ trợ khách hàng
     */
    private void openSupport() {
        Toast.makeText(this, R.string.toast_support, Toast.LENGTH_SHORT).show();
    }
}
