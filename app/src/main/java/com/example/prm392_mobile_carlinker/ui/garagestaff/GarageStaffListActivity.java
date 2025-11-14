package com.example.prm392_mobile_carlinker.ui.garagestaff;

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
import com.example.prm392_mobile_carlinker.data.repository.Resource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GarageStaffListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvError;
    private FloatingActionButton fabAdd;

    private GarageStaffViewModel viewModel;
    private GarageStaffAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_staff_list);

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupViewModel();

        fabAdd.setOnClickListener(v -> openAddStaffForm());

        loadData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        tvError = findViewById(R.id.tvError);
        fabAdd = findViewById(R.id.fabAdd);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quản lý nhân viên");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new GarageStaffAdapter(
                staff -> openStaffDetail(staff.getId()),
                staff -> openEditStaffForm(staff.getId()),
                staff -> confirmDeleteStaff(staff.getId(), staff.getFullName())
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(GarageStaffViewModel.class);
    }

    private void loadData() {
        viewModel.getAllGarageStaff(1, 100, null, true).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        showLoading();
                        break;
                    case SUCCESS:
                        showSuccess();
                        if (resource.getData() != null) {
                            adapter.setStaffList(resource.getData());
                        }
                        break;
                    case ERROR:
                        showError(resource.getMessage());
                        break;
                }
            }
        });
    }

    private void openAddStaffForm() {
        Intent intent = new Intent(this, GarageStaffFormActivity.class);
        startActivity(intent);
    }

    private void openStaffDetail(int staffId) {
        Intent intent = new Intent(this, GarageStaffDetailActivity.class);
        intent.putExtra("STAFF_ID", staffId);
        startActivity(intent);
    }

    private void openEditStaffForm(int staffId) {
        Intent intent = new Intent(this, GarageStaffFormActivity.class);
        intent.putExtra("STAFF_ID", staffId);
        startActivity(intent);
    }

    private void confirmDeleteStaff(int staffId, String staffName) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa nhân viên " + staffName + "?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteStaff(staffId))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteStaff(int staffId) {
        viewModel.deleteGarageStaff(staffId).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Xóa nhân viên thành công", Toast.LENGTH_SHORT).show();
                        loadData(); // Reload list
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Lỗi: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
    }

    private void showSuccess() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // Reload when returning from other screens
    }
}
