package com.example.prm392_mobile_carlinker.ui.service;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.garage.GarageDetail;
import com.example.prm392_mobile_carlinker.data.model.garage.ServiceItem;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleResponse;
import com.example.prm392_mobile_carlinker.ui.adapter.ServiceCategoryBookingAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServiceBookingActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private TextView tvError;
    private View layoutContent;

    private TextView tvGarageName;
    private TextView tvGarageAddress;
    private Spinner spinnerVehicle;
    private TextView tvNoVehicle;
    private RecyclerView rvServiceCategories;
    private TextView tvTotalPrice;
    private Button btnBookService;

    private ServiceBookingViewModel viewModel;
    private ServiceCategoryBookingAdapter categoryAdapter;

    private int garageId = -1;
    private List<VehicleResponse.Data> vehicles = new ArrayList<>();
    private VehicleResponse.Data selectedVehicle;
    private List<ServiceItem> selectedServiceItems = new ArrayList<>();
    private double totalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_booking);

        // Get garage ID from intent
        garageId = getIntent().getIntExtra("GARAGE_ID", -1);
        if (garageId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin garage", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupToolbar();
        setupViewModel();
        setupRecyclerView();
        setupSpinner();
        setupBookButton();

        loadData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);
        tvError = findViewById(R.id.tvError);
        layoutContent = findViewById(R.id.layoutContent);

        tvGarageName = findViewById(R.id.tvGarageName);
        tvGarageAddress = findViewById(R.id.tvGarageAddress);
        spinnerVehicle = findViewById(R.id.spinnerVehicle);
        tvNoVehicle = findViewById(R.id.tvNoVehicle);
        rvServiceCategories = findViewById(R.id.rvServiceCategories);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnBookService = findViewById(R.id.btnBookService);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Đặt Lịch Dịch Vụ");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ServiceBookingViewModel.class);
    }

    private void setupRecyclerView() {
        categoryAdapter = new ServiceCategoryBookingAdapter((selectedItems, price) -> {
            selectedServiceItems = selectedItems;
            totalPrice = price;
            updateTotalPrice();
            updateBookButton();
        });

        rvServiceCategories.setLayoutManager(new LinearLayoutManager(this));
        rvServiceCategories.setAdapter(categoryAdapter);
    }

    private void setupSpinner() {
        spinnerVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < vehicles.size()) {
                    selectedVehicle = vehicles.get(position);
                    updateBookButton();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedVehicle = null;
                updateBookButton();
            }
        });
    }

    private void setupBookButton() {
        btnBookService.setOnClickListener(v -> bookService());
    }

    private void loadData() {
        // Load vehicles
        viewModel.getAllVehicles().observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case SUCCESS:
                        if (resource.getData() != null && !resource.getData().isEmpty()) {
                            vehicles = resource.getData();
                            setupVehicleSpinner(vehicles);
                        } else {
                            showNoVehicleMessage();
                        }
                        break;
                    case ERROR:
                        Toast.makeText(this, "Lỗi tải danh sách xe: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        // Load garage details
        viewModel.getGarageDetails(garageId).observe(this, resource -> {
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

    private void setupVehicleSpinner(List<VehicleResponse.Data> vehicles) {
        List<String> vehicleNames = new ArrayList<>();
        for (VehicleResponse.Data vehicle : vehicles) {
            String display = vehicle.getBrand() + " " + vehicle.getModel() + " - " + vehicle.getLicensePlate();
            vehicleNames.add(display);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_item,
            vehicleNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehicle.setAdapter(adapter);

        spinnerVehicle.setVisibility(View.VISIBLE);
        tvNoVehicle.setVisibility(View.GONE);
    }

    private void showNoVehicleMessage() {
        spinnerVehicle.setVisibility(View.GONE);
        tvNoVehicle.setVisibility(View.VISIBLE);
        btnBookService.setEnabled(false);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        layoutContent.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
    }

    private void showSuccess(GarageDetail data) {
        progressBar.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
        layoutContent.setVisibility(View.VISIBLE);

        if (data != null) {
            // Display garage info
            if (data.getGarageDto() != null) {
                tvGarageName.setText(data.getGarageDto().getName());
                tvGarageAddress.setText(data.getGarageDto().getDescription());
            }

            // Display service categories
            if (data.getServiceCategories() != null) {
                categoryAdapter.setCategories(data.getServiceCategories());
            }
        }
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        layoutContent.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(message != null ? message : "Lỗi khi tải dữ liệu");
    }

    private void updateTotalPrice() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvTotalPrice.setText(formatter.format(totalPrice));
    }

    private void updateBookButton() {
        boolean canBook = selectedVehicle != null
            && !selectedServiceItems.isEmpty()
            && totalPrice > 0;
        btnBookService.setEnabled(canBook);
    }

    private void bookService() {
        if (selectedVehicle == null) {
            Toast.makeText(this, "Vui lòng chọn xe", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedServiceItems.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ít nhất một dịch vụ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create booking request - Lấy ID của ServiceItem
        List<Integer> serviceItemIds = new ArrayList<>();
        for (ServiceItem item : selectedServiceItems) {
            serviceItemIds.add(item.getId());
        }

        // Disable button và hiển thị loading
        btnBookService.setEnabled(false);
        btnBookService.setText("Đang đặt lịch...");

        // Call API to create service record
        viewModel.createServiceRecord(garageId, selectedVehicle.getId(), serviceItemIds)
            .observe(this, result -> {
                if (result != null) {
                    switch (result.status) {
                        case LOADING:
                            // Already showing loading state
                            break;
                        case SUCCESS:
                            Toast.makeText(this,
                                "Đặt lịch thành công!\nXe: " + selectedVehicle.getLicensePlate() +
                                "\nDịch vụ: " + selectedServiceItems.size() +
                                "\nTổng tiền: " + tvTotalPrice.getText(),
                                Toast.LENGTH_LONG).show();
                            // Return to previous screen
                            finish();
                            break;
                        case ERROR:
                            btnBookService.setEnabled(true);
                            btnBookService.setText("Đặt Lịch");
                            Toast.makeText(this,
                                "Lỗi đặt lịch: " + result.message,
                                Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
    }
}
