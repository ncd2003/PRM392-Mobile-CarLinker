package com.example.prm392_mobile_carlinker.ui.garage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.garage.Garage;
import com.example.prm392_mobile_carlinker.data.model.garage.GarageDetail;
import com.example.prm392_mobile_carlinker.ui.adapter.GarageServiceCategoryAdapter;
import com.example.prm392_mobile_carlinker.util.SessionManager;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;

/**
 * Activity hiển thị chi tiết garage và danh sách dịch vụ
 */
public class GarageDetailActivity extends AppCompatActivity {
    public static final String EXTRA_GARAGE_ID = "garage_id";
    private static final String TAG = "GarageDetailActivity";

    private GarageDetailViewModel viewModel;
    private GarageServiceCategoryAdapter serviceCategoryAdapter;

    // Views
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView ivGarageImage;
    private TextView tvGarageName;
    private TextView tvGarageDescription;
    private TextView tvOperatingTime;
    private TextView tvPhoneNumber;
    private TextView tvEmail;
    private TextView tvTotalServices;
    private RecyclerView rvServiceCategories;
    private ProgressBar progressBar;
    private MaterialButton btnEmergencyCall;
    private MaterialButton btnBookService;
    private MaterialButton btnChatWithGarage;

    private int garageId;
    private String garagePhoneNumber;
    private String garageName;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_detail);

        // Initialize SessionManager
        sessionManager = new SessionManager(this);

        // Get garage ID from intent
        garageId = getIntent().getIntExtra(EXTRA_GARAGE_ID, -1);
        if (garageId == -1) {
            Toast.makeText(this, R.string.error_garage_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupViewModel();
        setupBackPress();
        loadGarageDetails();
    }

    private void initViews() {
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        ivGarageImage = findViewById(R.id.ivGarageImage);
        tvGarageName = findViewById(R.id.tvGarageName);
        tvGarageDescription = findViewById(R.id.tvGarageDescription);
        tvOperatingTime = findViewById(R.id.tvOperatingTime);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvEmail = findViewById(R.id.tvEmail);
        tvTotalServices = findViewById(R.id.tvTotalServices);
        rvServiceCategories = findViewById(R.id.rvServiceCategories);
        progressBar = findViewById(R.id.progressBar);
        btnEmergencyCall = findViewById(R.id.btnEmergencyCall);
        btnBookService = findViewById(R.id.btnBookService);
        btnChatWithGarage = findViewById(R.id.btnChatWithGarage);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupRecyclerView() {
        serviceCategoryAdapter = new GarageServiceCategoryAdapter();
        rvServiceCategories.setLayoutManager(new LinearLayoutManager(this));
        rvServiceCategories.setAdapter(serviceCategoryAdapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(GarageDetailViewModel.class);
    }

    private void setupBackPress() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void loadGarageDetails() {
        viewModel.loadGarageDetails(garageId).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        showLoading(true);
                        break;

                    case SUCCESS:
                        showLoading(false);
                        if (resource.getData() != null) {
                            displayGarageDetails(resource.getData());
                        }
                        break;

                    case ERROR:
                        showLoading(false);
                        String errorMessage = resource.getMessage() != null ?
                                resource.getMessage() : getString(R.string.error_loading_garage);
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void displayGarageDetails(GarageDetail garageDetail) {
        Garage garage = garageDetail.getGarageDto();

        // Store phone number for emergency call
        garagePhoneNumber = garage.getPhoneNumber();

        // Store garage name for chat
        garageName = garage.getName();

        // Set toolbar title
        collapsingToolbar.setTitle(garage.getName());

        // Load garage image với xử lý URL HTTPS đầy đủ
        String imageUrl = garage.getImage();
        Log.d(TAG, "========== GARAGE DETAIL IMAGE DEBUG ==========");
        Log.d(TAG, "Garage ID: " + garage.getId());
        Log.d(TAG, "Garage Name: " + garage.getName());
        Log.d(TAG, "Image URL from API: " + imageUrl);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Log.d(TAG, "Loading image from: " + imageUrl);

            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .listener(new com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable>() {
                        @Override
                        public boolean onLoadFailed(@androidx.annotation.Nullable com.bumptech.glide.load.engine.GlideException e,
                                                    Object model,
                                                    com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target,
                                                    boolean isFirstResource) {
                            Log.e(TAG, "Failed to load garage detail image: " + imageUrl, e);
                            if (e != null) {
                                Log.e(TAG, "Error causes: ");
                                for (Throwable cause : e.getRootCauses()) {
                                    Log.e(TAG, " - " + cause.getMessage());
                                }
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(android.graphics.drawable.Drawable resource,
                                                      Object model,
                                                      com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target,
                                                      com.bumptech.glide.load.DataSource dataSource,
                                                      boolean isFirstResource) {
                            Log.d(TAG, "Garage detail image loaded successfully: " + imageUrl);
                            return false;
                        }
                    })
                    .into(ivGarageImage);
        } else {
            Log.w(TAG, "Image URL is null or empty, using placeholder");
            ivGarageImage.setImageResource(R.drawable.ic_launcher_background);
        }

        // Set garage info
        tvGarageName.setText(garage.getName());
        tvGarageDescription.setText(garage.getDescription() != null ?
                garage.getDescription() : getString(R.string.no_description));
        tvOperatingTime.setText(garage.getOperatingTime() != null ?
                garage.getOperatingTime() : getString(R.string.no_info));
        tvPhoneNumber.setText(garage.getPhoneNumber() != null ?
                garage.getPhoneNumber() : getString(R.string.no_info));
        tvEmail.setText(garage.getEmail() != null ?
                garage.getEmail() : getString(R.string.no_info));

        // Set total services
        int totalServices = garageDetail.getTotalServiceItems();
        tvTotalServices.setText(getString(R.string.total_services, totalServices));

        // Set service categories
        if (garageDetail.getServiceCategories() != null && !garageDetail.getServiceCategories().isEmpty()) {
            serviceCategoryAdapter.setCategories(garageDetail.getServiceCategories());
        } else {
            Toast.makeText(this, R.string.no_services, Toast.LENGTH_SHORT).show();
        }

        // Setup emergency call button
        setupEmergencyCallButton();

        // Setup chat button
        setupChatButton();
    }

    private void setupEmergencyCallButton() {
        btnEmergencyCall.setOnClickListener(v -> {
            if (garagePhoneNumber != null && !garagePhoneNumber.isEmpty()) {
                // Open phone dialer with garage phone number
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + garagePhoneNumber));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Không có số điện thoại để gọi", Toast.LENGTH_SHORT).show();
            }
        });

        // Setup book service button
        btnBookService.setOnClickListener(v -> {
            Intent intent = new Intent(this, com.example.prm392_mobile_carlinker.ui.service.ServiceBookingActivity.class);
            intent.putExtra("GARAGE_ID", garageId);
            startActivity(intent);
        });
    }

    private void setupChatButton() {
        btnChatWithGarage.setOnClickListener(v -> {
            // Check if user is logged in
            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(this, "Vui lòng đăng nhập để chat với garage", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if user is a customer (not a garage owner)
            String userRole = sessionManager.getUserRoleString();
            if (userRole != null && userRole.equals("GARAGE")) {
                Toast.makeText(this, "Garage không thể chat với garage khác", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get customer ID
            int customerId = sessionManager.getUserId();
            if (customerId <= 0) {
                Toast.makeText(this, "Không thể xác định thông tin người dùng", Toast.LENGTH_SHORT).show();
                return;
            }

            // Open InitiateChatActivity
            Intent intent = new Intent(this, com.example.prm392_mobile_carlinker.ui.chat.InitiateChatActivity.class);
            intent.putExtra(com.example.prm392_mobile_carlinker.ui.chat.InitiateChatActivity.EXTRA_GARAGE_ID, garageId);
            intent.putExtra(com.example.prm392_mobile_carlinker.ui.chat.InitiateChatActivity.EXTRA_GARAGE_NAME, garageName);
            startActivity(intent);
        });
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
