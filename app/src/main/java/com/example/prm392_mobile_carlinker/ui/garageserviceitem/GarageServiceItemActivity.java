package com.example.prm392_mobile_carlinker.ui.garageserviceitem;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.cart.BaseResponse;
import com.example.prm392_mobile_carlinker.data.model.garage.GarageUpdateServiceItem;
import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemDto;
import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemResponse;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;
import com.example.prm392_mobile_carlinker.data.remote.ApiService;
import com.example.prm392_mobile_carlinker.ui.adapter.GarageServiceItemAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity cho GARAGE role để quản lý danh sách dịch vụ và giá
 */
public class GarageServiceItemActivity extends AppCompatActivity {

    private static final String TAG = "GarageServiceItemAct";

    private RecyclerView recyclerView;
    private GarageServiceItemAdapter adapter;
    private ProgressBar progressBar;
    private Button btnSave;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_service_item);

        // Initialize views
        initViews();

        // Setup toolbar
        setupToolbar();

        // Initialize API service
        apiService = RetrofitClient.getApiService();

        // Load service items
        loadServiceItems();

        // Setup save button
        btnSave.setOnClickListener(v -> saveServiceItems());
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        btnSave = findViewById(R.id.btnSave);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GarageServiceItemAdapter();
        recyclerView.setAdapter(adapter);
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

    /**
     * Load danh sách service items từ API
     */
    private void loadServiceItems() {
        showLoading(true);

        Call<ServiceItemResponse> call = apiService.getAllServiceItems(1, 100, "Name", true);
        call.enqueue(new Callback<ServiceItemResponse>() {
            @Override
            public void onResponse(Call<ServiceItemResponse> call, Response<ServiceItemResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    ServiceItemResponse serviceItemResponse = response.body();

                    if (serviceItemResponse.getStatus() == 200 && serviceItemResponse.getData() != null) {
                        List<ServiceItemDto> serviceItems = serviceItemResponse.getData().getItems();

                        if (serviceItems != null && !serviceItems.isEmpty()) {
                            adapter.setServiceItems(serviceItems);
                        } else {
                            Toast.makeText(GarageServiceItemActivity.this,
                                "Không có dịch vụ nào", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(GarageServiceItemActivity.this,
                            "Lỗi: " + serviceItemResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GarageServiceItemActivity.this,
                        "Không thể tải danh sách dịch vụ", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Load failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ServiceItemResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(GarageServiceItemActivity.this,
                    "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Load error", t);
            }
        });
    }

    /**
     * Lưu danh sách service items đã chọn với giá
     */
    private void saveServiceItems() {
        List<GarageUpdateServiceItem> selectedItems = adapter.getSelectedItemsWithPrice();

        if (selectedItems.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ít nhất một dịch vụ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate prices
        for (GarageUpdateServiceItem item : selectedItems) {
            if (item.getPrice() <= 0) {
                Toast.makeText(this, "Vui lòng nhập giá hợp lệ cho tất cả dịch vụ đã chọn",
                    Toast.LENGTH_SHORT).show();
                return;
            }
        }

        showLoading(true);

        Call<BaseResponse> call = apiService.updateGarageServiceItems(selectedItems);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse baseResponse = response.body();

                    if (baseResponse.getStatus() == 200) {
                        Toast.makeText(GarageServiceItemActivity.this,
                            "Cập nhật dịch vụ thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(GarageServiceItemActivity.this,
                            "Lỗi: " + baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GarageServiceItemActivity.this,
                        "Không thể cập nhật dịch vụ", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Update failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(GarageServiceItemActivity.this,
                    "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Update error", t);
            }
        });
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnSave.setEnabled(!isLoading);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
