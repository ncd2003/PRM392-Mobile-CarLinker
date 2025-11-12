package com.example.prm392_mobile_carlinker.ui.servicecategory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.servicecategory.ServiceCategory;
import com.example.prm392_mobile_carlinker.data.repository.Resource;
import com.example.prm392_mobile_carlinker.ui.adapter.ServiceCategoryAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ServiceCategoryListActivity extends AppCompatActivity implements ServiceCategoryAdapter.OnServiceCategoryClickListener {

    private RecyclerView rvServiceCategories;
    private ProgressBar progressBar;
    private TextView tvError;
    private FloatingActionButton fabAddCategory;
    private ServiceCategoryAdapter adapter;
    private ServiceCategoryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_category_list);

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupViewModel();
        setupFab();
        loadServiceCategories();
    }

    private void initViews() {
        rvServiceCategories = findViewById(R.id.rvServiceCategories);
        progressBar = findViewById(R.id.progressBar);
        tvError = findViewById(R.id.tvError);
        fabAddCategory = findViewById(R.id.fabAddCategory);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quản lý Gói Dịch Vụ");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new ServiceCategoryAdapter(this);
        rvServiceCategories.setLayoutManager(new LinearLayoutManager(this));
        rvServiceCategories.setAdapter(adapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ServiceCategoryViewModel.class);
    }

    private void setupFab() {
        fabAddCategory.setOnClickListener(v -> {
            Intent intent = new Intent(this, ServiceCategoryFormActivity.class);
            startActivity(intent);
        });
    }

    private void loadServiceCategories() {
        viewModel.getAllServiceCategories(1, 100, null, true).observe(this, resource -> {
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
        rvServiceCategories.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
    }

    private void showSuccess(java.util.List<ServiceCategory> data) {
        progressBar.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
        rvServiceCategories.setVisibility(View.VISIBLE);

        if (data != null && !data.isEmpty()) {
            adapter.setServiceCategories(data);
        } else {
            tvError.setText("Chưa có gói dịch vụ nào");
            tvError.setVisibility(View.VISIBLE);
            rvServiceCategories.setVisibility(View.GONE);
        }
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        rvServiceCategories.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(message != null ? message : "Lỗi khi tải dữ liệu");
    }

    @Override
    public void onViewClick(ServiceCategory serviceCategory) {
        Intent intent = new Intent(this, ServiceCategoryDetailActivity.class);
        intent.putExtra("CATEGORY_ID", serviceCategory.getId());
        startActivity(intent);
    }

    @Override
    public void onEditClick(ServiceCategory serviceCategory) {
        Intent intent = new Intent(this, ServiceCategoryFormActivity.class);
        intent.putExtra("CATEGORY_ID", serviceCategory.getId());
        intent.putExtra("CATEGORY_NAME", serviceCategory.getName());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(ServiceCategory serviceCategory) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa gói dịch vụ \"" + serviceCategory.getName() + "\" không?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteServiceCategory(serviceCategory.getId()))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteServiceCategory(int id) {
        viewModel.deleteServiceCategory(id).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Xóa gói dịch vụ thành công", Toast.LENGTH_SHORT).show();
                        loadServiceCategories(); // Reload list
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Lỗi: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadServiceCategories(); // Reload when returning to this activity
    }
}
