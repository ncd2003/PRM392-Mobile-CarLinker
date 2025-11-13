package com.example.prm392_mobile_carlinker.ui.serviceitem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemCreateRequest;
import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemUpdateRequest;
import com.google.android.material.textfield.TextInputEditText;

public class ServiceItemFormActivity extends AppCompatActivity {

    private TextInputEditText etItemName;
    private Button btnSave;
    private ProgressBar progressBar;
    private ServiceItemViewModel viewModel;
    private int itemId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_item_form);

        initViews();
        setupToolbar();
        setupViewModel();
        checkEditMode();
        setupSaveButton();
    }

    private void initViews() {
        etItemName = findViewById(R.id.etItemName);
        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ServiceItemViewModel.class);
    }

    private void checkEditMode() {
        if (getIntent().hasExtra("ITEM_ID")) {
            isEditMode = true;
            itemId = getIntent().getIntExtra("ITEM_ID", -1);
            String itemName = getIntent().getStringExtra("ITEM_NAME");

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Sửa Dịch Vụ");
            }

            etItemName.setText(itemName);
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Thêm Dịch Vụ");
            }
        }
    }

    private void setupSaveButton() {
        btnSave.setOnClickListener(v -> {
            String name = etItemName.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên dịch vụ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isEditMode) {
                updateServiceItem(name);
            } else {
                createServiceItem(name);
            }
        });
    }

    private void createServiceItem(String name) {
        ServiceItemCreateRequest request = new ServiceItemCreateRequest(name);

        viewModel.createServiceItem(request).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        showLoading(true);
                        break;
                    case SUCCESS:
                        showLoading(false);
                        Toast.makeText(this, "Tạo dịch vụ thành công", Toast.LENGTH_SHORT).show();
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

    private void updateServiceItem(String name) {
        ServiceItemUpdateRequest request = new ServiceItemUpdateRequest(name);

        viewModel.updateServiceItem(itemId, request).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        showLoading(true);
                        break;
                    case SUCCESS:
                        showLoading(false);
                        Toast.makeText(this, "Cập nhật dịch vụ thành công", Toast.LENGTH_SHORT).show();
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
    }
}

