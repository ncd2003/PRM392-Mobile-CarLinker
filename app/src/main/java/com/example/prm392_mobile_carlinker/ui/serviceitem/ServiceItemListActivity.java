package com.example.prm392_mobile_carlinker.ui.serviceitem;

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
import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemDto;
import com.example.prm392_mobile_carlinker.data.repository.Resource;
import com.example.prm392_mobile_carlinker.ui.adapter.ServiceItemAdminAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ServiceItemListActivity extends AppCompatActivity implements ServiceItemAdminAdapter.OnServiceItemClickListener {

    private RecyclerView rvServiceItems;
    private ProgressBar progressBar;
    private TextView tvError;
    private FloatingActionButton fabAddItem;
    private ServiceItemAdminAdapter adapter;
    private ServiceItemViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_item_list);

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupViewModel();
        setupFab();
        loadServiceItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadServiceItems();
    }

    private void initViews() {
        rvServiceItems = findViewById(R.id.rvServiceItems);
        progressBar = findViewById(R.id.progressBar);
        tvError = findViewById(R.id.tvError);
        fabAddItem = findViewById(R.id.fabAddItem);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quản lý Dịch Vụ");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new ServiceItemAdminAdapter(this);
        rvServiceItems.setLayoutManager(new LinearLayoutManager(this));
        rvServiceItems.setAdapter(adapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ServiceItemViewModel.class);
    }

    private void setupFab() {
        fabAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(this, ServiceItemFormActivity.class);
            startActivity(intent);
        });
    }

    private void loadServiceItems() {
        viewModel.getAllServiceItems(1, 100, null, true).observe(this, resource -> {
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
        rvServiceItems.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
    }

    private void showSuccess(java.util.List<ServiceItemDto> data) {
        progressBar.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
        rvServiceItems.setVisibility(View.VISIBLE);

        if (data != null && !data.isEmpty()) {
            adapter.setServiceItems(data);
        } else {
            tvError.setText("Chưa có dịch vụ nào");
            tvError.setVisibility(View.VISIBLE);
            rvServiceItems.setVisibility(View.GONE);
        }
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        rvServiceItems.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(message != null ? message : "Lỗi khi tải dữ liệu");
    }

    @Override
    public void onViewClick(ServiceItemDto serviceItem) {
        Intent intent = new Intent(this, ServiceItemDetailActivity.class);
        intent.putExtra("ITEM_ID", serviceItem.getId());
        startActivity(intent);
    }

    @Override
    public void onEditClick(ServiceItemDto serviceItem) {
        Intent intent = new Intent(this, ServiceItemFormActivity.class);
        intent.putExtra("ITEM_ID", serviceItem.getId());
        intent.putExtra("ITEM_NAME", serviceItem.getName());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(ServiceItemDto serviceItem) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa dịch vụ \"" + serviceItem.getName() + "\" không?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteServiceItem(serviceItem.getId()))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteServiceItem(int id) {
        viewModel.deleteServiceItem(id).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Xóa dịch vụ thành công", Toast.LENGTH_SHORT).show();
                        loadServiceItems();
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Lỗi: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}

