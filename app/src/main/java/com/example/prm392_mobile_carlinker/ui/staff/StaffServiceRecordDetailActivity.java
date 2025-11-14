package com.example.prm392_mobile_carlinker.ui.staff;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.cart.BaseResponse;
import com.example.prm392_mobile_carlinker.data.model.servicerecord.ServiceRecordDto;
import com.example.prm392_mobile_carlinker.data.model.servicerecord.ServiceRecordListResponse;
import com.example.prm392_mobile_carlinker.data.model.servicerecord.UpdateServiceRecordStatusRequest;
import com.example.prm392_mobile_carlinker.data.remote.ApiService;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;
import com.example.prm392_mobile_carlinker.ui.adapter.ServiceRecordItemAdapter;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity for STAFF to view and update service record details
 */
public class StaffServiceRecordDetailActivity extends AppCompatActivity {

    private static final String TAG = "StaffSRDetail";

    private ProgressBar progressBar;
    private TextView tvRecordId, tvCustomerName, tvCustomerPhone, tvCustomerEmail;
    private TextView tvVehicle, tvFuelType, tvTransmission;
    private ImageView ivVehicle;
    private TextView tvStatus, tvStartTime, tvEndTime, tvTotalCost;
    private RecyclerView rvServiceItems;
    private Button btnStartService, btnCompleteService, btnCancelService;

    private ServiceRecordItemAdapter serviceItemAdapter;
    private ApiService apiService;
    private int serviceRecordId;
    private ServiceRecordDto currentRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_service_record_detail);

        serviceRecordId = getIntent().getIntExtra("SERVICE_RECORD_ID", -1);
        if (serviceRecordId == -1) {
            Toast.makeText(this, "Invalid service record", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupToolbar();
        setupRecyclerView();

        apiService = RetrofitClient.getApiService();

        loadServiceRecordDetail();
        setupButtons();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        tvRecordId = findViewById(R.id.tvRecordId);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        tvCustomerEmail = findViewById(R.id.tvCustomerEmail);
        tvVehicle = findViewById(R.id.tvVehicle);
        tvFuelType = findViewById(R.id.tvFuelType);
        tvTransmission = findViewById(R.id.tvTransmission);
        ivVehicle = findViewById(R.id.ivVehicle);
        tvStatus = findViewById(R.id.tvStatus);
        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndTime = findViewById(R.id.tvEndTime);
        tvTotalCost = findViewById(R.id.tvTotalCost);
        rvServiceItems = findViewById(R.id.rvServiceItems);
        btnStartService = findViewById(R.id.btnStartService);
        btnCompleteService = findViewById(R.id.btnCompleteService);
        btnCancelService = findViewById(R.id.btnCancelService);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chi tiết dịch vụ");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        serviceItemAdapter = new ServiceRecordItemAdapter();
        rvServiceItems.setLayoutManager(new LinearLayoutManager(this));
        rvServiceItems.setAdapter(serviceItemAdapter);
    }

    private void setupButtons() {
        btnStartService.setOnClickListener(v -> updateStatus(1, null)); // InProgress
        btnCompleteService.setOnClickListener(v -> showCompleteDialog());
        btnCancelService.setOnClickListener(v -> showCancelDialog());
    }

    private void loadServiceRecordDetail() {
        showLoading(true);

        // Load all records and find the one we need
        Call<ServiceRecordListResponse> call = apiService.getAllServiceRecords(1, 100, null, true);
        call.enqueue(new Callback<ServiceRecordListResponse>() {
            @Override
            public void onResponse(Call<ServiceRecordListResponse> call, Response<ServiceRecordListResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    ServiceRecordListResponse listResponse = response.body();
                    if (listResponse.getStatus() == 200 && listResponse.getData() != null) {
                        // Find the record with matching ID
                        for (ServiceRecordDto record : listResponse.getData().getItems()) {
                            if (record.getId() == serviceRecordId) {
                                currentRecord = record;
                                displayServiceRecord(record);
                                return;
                            }
                        }
                        Toast.makeText(StaffServiceRecordDetailActivity.this,
                                "Không tìm thấy dịch vụ", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(StaffServiceRecordDetailActivity.this,
                            "Không thể tải chi tiết dịch vụ", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Load failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ServiceRecordListResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(StaffServiceRecordDetailActivity.this,
                        "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Load error", t);
            }
        });
    }

    private void displayServiceRecord(ServiceRecordDto record) {
        // Record info
        tvRecordId.setText("#" + record.getId());
        tvStatus.setText(record.getStatusText());

        // Customer info
        if (record.getUser() != null) {
            tvCustomerName.setText(record.getUser().getFullName());
            tvCustomerPhone.setText(record.getUser().getPhoneNumber());
            tvCustomerEmail.setText(record.getUser().getEmail());
        }

        // Vehicle info
        if (record.getVehicle() != null) {
            ServiceRecordDto.VehicleDto vehicle = record.getVehicle();
            tvVehicle.setText(vehicle.getBrand() + " " + vehicle.getModel() + " (" + vehicle.getYear() + ")");
            tvFuelType.setText(vehicle.getFuelType());
            tvTransmission.setText(vehicle.getTransmissionType());

            // Load vehicle image
            if (vehicle.getImage() != null && !vehicle.getImage().isEmpty()) {
                Glide.with(this)
                        .load(vehicle.getImage())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(ivVehicle);
            }
        }

        // Time info
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

            java.util.Date startDate = inputFormat.parse(record.getStartTime());
            if (startDate != null) {
                tvStartTime.setText(outputFormat.format(startDate));
            }

            if (record.getEndTime() != null) {
                java.util.Date endDate = inputFormat.parse(record.getEndTime());
                if (endDate != null) {
                    tvEndTime.setText(outputFormat.format(endDate));
                }
            } else {
                tvEndTime.setText("Chưa hoàn thành");
            }
        } catch (Exception e) {
            tvStartTime.setText(record.getStartTime());
            tvEndTime.setText(record.getEndTime() != null ? record.getEndTime() : "Chưa hoàn thành");
        }

        // Total cost
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvTotalCost.setText(formatter.format(record.getTotalCost()));

        // Service items
        if (record.getServiceItems() != null) {
            serviceItemAdapter.setServiceItems(record.getServiceItems());
        }

        // Update button visibility based on status
        updateButtonVisibility(record.getServiceRecordStatus());
    }

    private void updateButtonVisibility(int status) {
        // Hide all buttons first
        btnStartService.setVisibility(View.GONE);
        btnCompleteService.setVisibility(View.GONE);
        btnCancelService.setVisibility(View.GONE);

        switch (status) {
            case 0: // Pending
                btnStartService.setVisibility(View.VISIBLE);
                btnCancelService.setVisibility(View.VISIBLE);
                break;
            case 1: // InProgress
                btnCompleteService.setVisibility(View.VISIBLE);
                btnCancelService.setVisibility(View.VISIBLE);
                break;
            case 2: // Completed
            case 3: // Cancelled
                // No actions available
                break;
        }
    }

    private void showCompleteDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_complete_service, null);
        EditText etTotalCost = dialogView.findViewById(R.id.etTotalCost);

        new AlertDialog.Builder(this)
                .setTitle("Hoàn thành dịch vụ")
                .setMessage("Nhập tổng chi phí:")
                .setView(dialogView)
                .setPositiveButton("Hoàn thành", (dialog, which) -> {
                    String costStr = etTotalCost.getText().toString().trim();
                    if (!costStr.isEmpty()) {
                        try {
                            double totalCost = Double.parseDouble(costStr);
                            updateStatus(2, totalCost); // Completed
                        } catch (NumberFormatException e) {
                            Toast.makeText(this, "Chi phí không hợp lệ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Vui lòng nhập chi phí", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showCancelDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Hủy dịch vụ")
                .setMessage("Bạn có chắc chắn muốn hủy dịch vụ này?")
                .setPositiveButton("Hủy dịch vụ", (dialog, which) -> updateStatus(3, null)) // Cancelled
                .setNegativeButton("Không", null)
                .show();
    }

    private void updateStatus(int newStatus, Double totalCost) {
        showLoading(true);

        UpdateServiceRecordStatusRequest request = new UpdateServiceRecordStatusRequest(newStatus, totalCost);
        Call<BaseResponse> call = apiService.updateServiceRecordStatus(serviceRecordId, request);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse baseResponse = response.body();
                    if (baseResponse.getStatus() == 200) {
                        Toast.makeText(StaffServiceRecordDetailActivity.this,
                                "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        // Reload data
                        loadServiceRecordDetail();
                    } else {
                        Toast.makeText(StaffServiceRecordDetailActivity.this,
                                "Lỗi: " + baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(StaffServiceRecordDetailActivity.this,
                            "Không thể cập nhật", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Update failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(StaffServiceRecordDetailActivity.this,
                        "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Update error", t);
            }
        });
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

