package com.example.prm392_mobile_carlinker.ui.garage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.garage.Garage;
import com.example.prm392_mobile_carlinker.ui.adapter.GarageAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

/**
 * Activity hiển thị danh sách garage
 * Tự động lấy vị trí GPS của người dùng và sắp xếp theo khoảng cách
 */
public class GarageListActivity extends AppCompatActivity implements GarageAdapter.OnGarageClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private RecyclerView recyclerView;
    private GarageAdapter garageAdapter;
    private GarageListViewModel viewModel;
    private ProgressBar progressBar;
    private TextView tvError;
    private TextView tvLocationInfo;
    private View layoutEmpty;

    private FusedLocationProviderClient fusedLocationClient;
    private double userLatitude = 0;
    private double userLongitude = 0;
    private boolean isSorted = false; // Flag để kiểm tra đã sort hay chưa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_list);

        // Initialize views
        initViews();

        // Setup Toolbar
        setupToolbar();

        // Setup RecyclerView
        setupRecyclerView();

        // Setup ViewModel
        setupViewModel();

        // Initialize Location Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Request location permission and load data
        checkLocationPermissionAndLoadData();
    }

    /**
     * Khởi tạo các view
     */
    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_garages);
        progressBar = findViewById(R.id.progress_bar);
        tvError = findViewById(R.id.tv_error);
        tvLocationInfo = findViewById(R.id.tv_location_info);
        layoutEmpty = findViewById(R.id.layout_empty);
    }

    /**
     * Setup Toolbar
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    /**
     * Setup RecyclerView
     */
    private void setupRecyclerView() {
        garageAdapter = new GarageAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(garageAdapter);
    }

    /**
     * Setup ViewModel
     */
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(GarageListViewModel.class);

        // Observe garage list
        viewModel.getGaragesLiveData().observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        showLoading();
                        break;

                    case SUCCESS:
                        hideLoading();
                        if (resource.getData() != null && !resource.getData().isEmpty()) {
                            showGarages(resource.getData());
                        } else {
                            showEmpty();
                        }
                        break;

                    case ERROR:
                        hideLoading();
                        showError(resource.getMessage());
                        break;
                }
            }
        });

        // Observe user location
        viewModel.getUserLocationLiveData().observe(this, location -> {
            if (location != null) {
                tvLocationInfo.setText(location);
            }
        });
    }

    /**
     * Kiểm tra quyền truy cập vị trí và load dữ liệu
     */
    private void checkLocationPermissionAndLoadData() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission granted, get location
            getCurrentLocationAndLoadGarages();
        }
    }

    /**
     * Lấy vị trí hiện tại và load danh sách garage
     */
    private void getCurrentLocationAndLoadGarages() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        tvLocationInfo.setText("📍 Đang lấy vị trí của bạn...");

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Got last known location
                            userLatitude = location.getLatitude();
                            userLongitude = location.getLongitude();

                            tvLocationInfo.setText(String.format(
                                    "📍 Vị trí của bạn: %.6f, %.6f", userLatitude, userLongitude));

                            // Load garages from API
                            loadGarages();
                        } else {
                            tvLocationInfo.setText("📍 Không thể lấy vị trí. Hiển thị tất cả garage.");
                            // Load garages without location
                            loadGarages();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    tvLocationInfo.setText("📍 Lỗi lấy vị trí. Hiển thị tất cả garage.");
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Load garages without location
                    loadGarages();
                });
    }

    /**
     * Load danh sách garage từ API
     */
    private void loadGarages() {
        viewModel.loadGarages();
    }

    /**
     * Hiển thị danh sách garage và sắp xếp theo khoảng cách
     */
    private void showGarages(List<Garage> garages) {
        if (garages == null || garages.isEmpty()) {
            showEmpty();
            return;
        }

        // Nếu có vị trí người dùng hợp lệ VÀ chưa sort, thì sort
        if (userLatitude != 0 && userLongitude != 0 && !isSorted) {
            isSorted = true; // Đánh dấu đã sort để tránh vòng lặp
            viewModel.sortGaragesByUserLocation(userLatitude, userLongitude);
            return; // ViewModel sẽ trigger observer lại với data đã sort
        }

        // Hiển thị danh sách garage
        garageAdapter.setGarageList(garages);
        recyclerView.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);

        // Log để debug
        android.util.Log.d("GarageListActivity", "Hiển thị " + garages.size() + " garage");
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showEmpty() {
        layoutEmpty.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                getCurrentLocationAndLoadGarages();
            } else {
                // Permission denied
                Toast.makeText(this, "Bạn cần cấp quyền truy cập vị trí để xem garage gần nhất",
                        Toast.LENGTH_LONG).show();
                tvLocationInfo.setText("📍 Không có quyền truy cập vị trí");
                // Load garages without location sorting
                loadGarages();
            }
        }
    }

    @Override
    public void onGarageClick(Garage garage) {
        // Navigate to GarageDetailActivity
        Intent intent = new Intent(this, GarageDetailActivity.class);
        intent.putExtra(GarageDetailActivity.EXTRA_GARAGE_ID, garage.getId());
        startActivity(intent);
    }

    @Override
    public void onCallClick(Garage garage) {
        // Open phone dialer
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + garage.getPhoneNumber()));
        startActivity(intent);
    }
}
