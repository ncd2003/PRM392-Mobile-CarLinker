package com.example.prm392_mobile_carlinker.ui.staff;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.servicerecord.ServiceRecordDto;
import com.example.prm392_mobile_carlinker.data.model.servicerecord.ServiceRecordListResponse;
import com.example.prm392_mobile_carlinker.data.remote.ApiService;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;
import com.example.prm392_mobile_carlinker.ui.adapter.StaffServiceRecordAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity for STAFF to manage service records
 */
public class StaffServiceRecordActivity extends AppCompatActivity implements StaffServiceRecordAdapter.OnServiceRecordClickListener {

    private static final String TAG = "StaffServiceRecord";

    private RecyclerView recyclerView;
    private StaffServiceRecordAdapter adapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ApiService apiService;

    private int currentPage = 1;
    private static final int PAGE_SIZE = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_service_record);

        initViews();
        setupToolbar();
        setupRecyclerView();

        apiService = RetrofitClient.getApiService();

        loadServiceRecords();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentPage = 1;
            loadServiceRecords();
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quản lý dịch vụ");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new StaffServiceRecordAdapter(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadServiceRecords() {
        showLoading(true);

        Call<ServiceRecordListResponse> call = apiService.getAllServiceRecords(
                currentPage,
                PAGE_SIZE,
                "StartTime",
                false // newest first
        );

        call.enqueue(new Callback<ServiceRecordListResponse>() {
            @Override
            public void onResponse(Call<ServiceRecordListResponse> call, Response<ServiceRecordListResponse> response) {
                showLoading(false);
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    ServiceRecordListResponse serviceRecordResponse = response.body();

                    if (serviceRecordResponse.getStatus() == 200 && serviceRecordResponse.getData() != null) {
                        List<ServiceRecordDto> records = serviceRecordResponse.getData().getItems();

                        if (records != null && !records.isEmpty()) {
                            adapter.setServiceRecords(records);
                        } else {
                            adapter.setServiceRecords(new ArrayList<>());
                            Toast.makeText(StaffServiceRecordActivity.this,
                                    "Không có dịch vụ nào", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(StaffServiceRecordActivity.this,
                                "Lỗi: " + serviceRecordResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(StaffServiceRecordActivity.this,
                            "Không thể tải danh sách dịch vụ", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Load failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ServiceRecordListResponse> call, Throwable t) {
                showLoading(false);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(StaffServiceRecordActivity.this,
                        "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Load error", t);
            }
        });
    }

    @Override
    public void onServiceRecordClick(ServiceRecordDto record) {
        // Open detail screen
        android.content.Intent intent = new android.content.Intent(this, StaffServiceRecordDetailActivity.class);
        intent.putExtra("SERVICE_RECORD_ID", record.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data when returning from detail screen
        loadServiceRecords();
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

