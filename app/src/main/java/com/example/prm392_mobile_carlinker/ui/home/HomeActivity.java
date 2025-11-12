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
import com.example.prm392_mobile_carlinker.ui.garage.GarageListActivity;
import com.example.prm392_mobile_carlinker.ui.order.MyOrdersActivity;
import com.example.prm392_mobile_carlinker.ui.servicecategory.ServiceCategoryListActivity;
import com.example.prm392_mobile_carlinker.ui.shop.ProductListActivity;
import com.google.android.material.card.MaterialCardView;

/**
 * HomeActivity - Màn hình chính của ứng dụng
 * Hiển thị các chức năng chính:
 * - Header với nút profile menu
 * - Nút cứu hộ khẩn cấp
 * - Mua sắm linh kiện
 * - Đặt lịch dịch vụ
 * - Garage gần nhất
 * - Hỗ trợ khách hàng
 */
public class HomeActivity extends AppCompatActivity {

    private Button btnEmergency;
    private FrameLayout cardShop, cardBooking, cardGarage, cardSupport;
    private MaterialCardView btnProfileMenu;
    private TextView tvWelcomeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Khởi tạo các view
        initViews();

        // Thiết lập sự kiện click
        setupClickListeners();

        // Load user info
        loadUserInfo();
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
    }

    /**
     * Load thông tin user
     */
    private void loadUserInfo() {
        // TODO: Load user info from SharedPreferences or API
        String userName = "Người dùng"; // Get from preferences
        tvWelcomeUser.setText("Xin chào, " + userName + "!");
    }

    /**
     * Thiết lập sự kiện click cho các nút
     */
    private void setupClickListeners() {
        // Profile Menu Button
        btnProfileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileMenu(v);
            }
        });

        // Nút cứu hộ khẩn cấp
        btnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEmergency();
            }
        });

        // Card Mua sắm linh kiện
        cardShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShop();
            }
        });

        // Card Đặt lịch dịch vụ
        cardBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBooking();
            }
        });

        // Card Garage gần nhất
        cardGarage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNearbyGarage();
            }
        });

        // Card Hỗ trợ khách hàng
        cardSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSupport();
            }
        });
    }

    /**
     * Hiển thị menu profile với các tùy chọn
     */
    private void showProfileMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.menu_profile, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
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
                } else if (itemId == R.id.menu_settings) {
                    openSettings();
                    return true;
                } else if (itemId == R.id.menu_logout) {
                    handleLogout();
                    return true;
                }

                return false;
            }
        });

        popup.show();
    }

    /**
     * Mở trang thông tin cá nhân
     */
    private void openUserInfo() {
        Intent intent = new Intent(this, com.example.prm392_mobile_carlinker.ui.user.UserInfoActivity.class);
        // TODO: Pass actual user ID from SharedPreferences
        intent.putExtra("USER_ID", 4); // Temporary hardcoded ID
        startActivity(intent);
    }

    /**
     * Mở trang quản lý xe
     */
    private void openVehicleManagement() {
        Toast.makeText(this, "Mở quản lý xe", Toast.LENGTH_SHORT).show();
        // TODO: Navigate to VehicleManagementActivity
        // Intent intent = new Intent(this, VehicleManagementActivity.class);
        // startActivity(intent);
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
     * Mở trang cài đặt
     */
    private void openSettings() {
        Toast.makeText(this, "Mở cài đặt", Toast.LENGTH_SHORT).show();
        // TODO: Navigate to SettingsActivity
        // Intent intent = new Intent(this, SettingsActivity.class);
        // startActivity(intent);
    }

    /**
     * Xử lý đăng xuất
     */
    private void handleLogout() {
        // TODO: Clear user session and navigate to login
        Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        // Clear SharedPreferences
        // Navigate to LoginActivity
        // finish();
    }

    /**
     * Xử lý chức năng cứu hộ khẩn cấp
     */
    private void handleEmergency() {
        // TODO: Implement emergency rescue feature
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
        // TODO: Implement booking service feature
        Toast.makeText(this, R.string.toast_booking, Toast.LENGTH_SHORT).show();
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
        // TODO: Implement customer support feature
        Toast.makeText(this, R.string.toast_support, Toast.LENGTH_SHORT).show();
    }
}
