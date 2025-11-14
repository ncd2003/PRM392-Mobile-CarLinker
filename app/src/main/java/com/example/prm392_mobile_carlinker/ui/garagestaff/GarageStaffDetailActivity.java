package com.example.prm392_mobile_carlinker.ui.garagestaff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.garagestaff.GarageStaffDto;

public class GarageStaffDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView ivStaffImage;
    private TextView tvFullName;
    private TextView tvEmail;
    private TextView tvPhoneNumber;
    private TextView tvRole;
    private TextView tvStatus;
    private TextView tvGarageId;
    private TextView tvGarageName;
    private TextView tvCreatedAt;
    private Button btnEdit;
    private Button btnDelete;
    private ProgressBar progressBar;

    private GarageStaffViewModel viewModel;
    private int staffId;
    private GarageStaffDto currentStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_staff_detail);

        initViews();
        setupToolbar();
        setupViewModel();

        staffId = getIntent().getIntExtra("STAFF_ID", -1);
        if (staffId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID nhân viên", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadStaffDetail();

        btnEdit.setOnClickListener(v -> openEditForm());
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        ivStaffImage = findViewById(R.id.ivStaffImage);
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvRole = findViewById(R.id.tvRole);
        tvStatus = findViewById(R.id.tvStatus);
        tvGarageId = findViewById(R.id.tvGarageId);
        tvGarageName = findViewById(R.id.tvGarageName);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chi tiết nhân viên");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(GarageStaffViewModel.class);
    }

    private void loadStaffDetail() {
        viewModel.getGarageStaffById(staffId).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        if (resource.getData() != null) {
                            currentStaff = resource.getData();
                            displayStaffInfo(currentStaff);
                        }
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Lỗi: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
            }
        });
    }

    private void displayStaffInfo(GarageStaffDto staff) {
        tvFullName.setText(staff.getFullName());
        tvEmail.setText(staff.getEmail());
        tvPhoneNumber.setText(staff.getPhoneNumber() != null ? staff.getPhoneNumber() : "Chưa có SĐT");
        tvRole.setText(staff.getGarageRoleString());
        tvStatus.setText(staff.getUserStatusString());
        tvGarageId.setText(String.valueOf(staff.getGarageId()));
        tvGarageName.setText(staff.getGarageName() != null ? staff.getGarageName() : "Không xác định");
        tvCreatedAt.setText(staff.getFormattedCreatedAt());

        // Set status color
        int statusColor;
        switch (staff.getUserStatus()) {
            case 0: // ACTIVE
                statusColor = getColor(R.color.green);
                break;
            case 1: // INACTIVE
                statusColor = getColor(R.color.gray);
                break;
            case 2: // SUSPENDED
                statusColor = getColor(R.color.red);
                break;
            default:
                statusColor = getColor(R.color.gray);
        }
        tvStatus.setTextColor(statusColor);

        // Load image
        if (staff.getImage() != null && !staff.getImage().isEmpty()) {
            Glide.with(this)
                    .load(staff.getImage())
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .circleCrop()
                    .into(ivStaffImage);
        } else {
            ivStaffImage.setImageResource(R.drawable.ic_person);
        }
    }

    private void openEditForm() {
        Intent intent = new Intent(this, GarageStaffFormActivity.class);
        intent.putExtra("STAFF_ID", staffId);
        startActivity(intent);
    }

    private void confirmDelete() {
        if (currentStaff == null) return;

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa nhân viên " + currentStaff.getFullName() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteStaff())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteStaff() {
        viewModel.deleteGarageStaff(staffId).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        btnDelete.setEnabled(false);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Xóa nhân viên thành công", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        btnDelete.setEnabled(true);
                        Toast.makeText(this, "Lỗi: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStaffDetail(); // Reload when returning from edit
    }
}
