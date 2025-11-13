package com.example.prm392_mobile_carlinker.ui.servicecategory;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.servicecategory.ServiceCategoryCreateRequest;
import com.example.prm392_mobile_carlinker.data.model.servicecategory.ServiceCategoryUpdateRequest;
import com.google.android.material.textfield.TextInputEditText;

public class ServiceCategoryFormActivity extends AppCompatActivity {

    private TextInputEditText etCategoryName;
    private Button btnSave;
    private ProgressBar progressBar;
    private ServiceCategoryViewModel viewModel;

    private int categoryId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_category_form);

        // Check if we're editing an existing category
        if (getIntent().hasExtra("CATEGORY_ID")) {
            categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
            isEditMode = true;
        }

        initViews();
        setupToolbar();
        setupViewModel();
        setupListeners();

        if (isEditMode) {
            loadExistingData();
        }
    }

    private void initViews() {
        etCategoryName = findViewById(R.id.etCategoryName);
        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEditMode ? "Sửa Gói Dịch Vụ" : "Thêm Gói Dịch Vụ");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ServiceCategoryViewModel.class);
    }

    private void setupListeners() {
        btnSave.setOnClickListener(v -> saveCategory());
    }

    private void loadExistingData() {
        String categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        if (categoryName != null) {
            etCategoryName.setText(categoryName);
        }
    }

    private void saveCategory() {
        String name = etCategoryName.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên gói dịch vụ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEditMode) {
            updateCategory(name);
        } else {
            createCategory(name);
        }
    }

    private void createCategory(String name) {
        ServiceCategoryCreateRequest request = new ServiceCategoryCreateRequest();
        request.setName(name);
        request.setServiceItems(null); // Can be added later

        viewModel.createServiceCategory(request).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        showLoading(true);
                        break;
                    case SUCCESS:
                        showLoading(false);
                        Toast.makeText(this, "Tạo gói dịch vụ thành công", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case ERROR:
                        showLoading(false);
                        Toast.makeText(this, "Lỗi: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void updateCategory(String name) {
        ServiceCategoryUpdateRequest request = new ServiceCategoryUpdateRequest();
        request.setName(name);
        request.setServiceItems(null); // Keep existing service items

        viewModel.updateServiceCategory(categoryId, request).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        showLoading(true);
                        break;
                    case SUCCESS:
                        showLoading(false);
                        Toast.makeText(this, "Cập nhật gói dịch vụ thành công", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case ERROR:
                        showLoading(false);
                        Toast.makeText(this, "Lỗi: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnSave.setEnabled(!isLoading);
        etCategoryName.setEnabled(!isLoading);
    }
}
