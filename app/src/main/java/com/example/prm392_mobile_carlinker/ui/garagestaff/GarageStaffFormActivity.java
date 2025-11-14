package com.example.prm392_mobile_carlinker.ui.garagestaff;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.garagestaff.GarageStaffCreateRequest;
import com.example.prm392_mobile_carlinker.data.model.garagestaff.GarageStaffUpdateRequest;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class GarageStaffFormActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView ivStaffImage;
    private Button btnSelectImage;
    private TextInputEditText etFullName;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private TextInputEditText etPhoneNumber;
    private Spinner spinnerRole;
    private Spinner spinnerStatus;
    private Button btnSave;
    private ProgressBar progressBar;

    private GarageStaffViewModel viewModel;
    private int staffId = -1;
    private boolean isEditMode = false;
    private Uri selectedImageUri;
    private File selectedImageFile;

    private ActivityResultLauncher<String> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_staff_form);

        initViews();
        setupToolbar();
        setupSpinners();
        setupViewModel();
        setupImagePicker();

        // Check if edit mode
        if (getIntent().hasExtra("STAFF_ID")) {
            staffId = getIntent().getIntExtra("STAFF_ID", -1);
            isEditMode = true;
            loadStaffData();
        }

        btnSelectImage.setOnClickListener(v -> openImagePicker());
        btnSave.setOnClickListener(v -> saveStaff());
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        ivStaffImage = findViewById(R.id.ivStaffImage);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        spinnerRole = findViewById(R.id.spinnerRole);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEditMode ? "Cập nhật nhân viên" : "Thêm nhân viên");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupSpinners() {
        // Role spinner
        String[] roles = {"Đại lý", "Kho hàng", "Nhân viên"};
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, roles);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(roleAdapter);

        // Status spinner (only for edit mode)
        String[] statuses = {"Hoạt động", "Không hoạt động", "Bị khóa"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statuses);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        // Hide status spinner in create mode
        if (!isEditMode) {
            spinnerStatus.setVisibility(View.GONE);
            findViewById(R.id.labelStatus).setVisibility(View.GONE);
        }
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(GarageStaffViewModel.class);
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        ivStaffImage.setImageURI(uri);
                        // Convert URI to File for upload
                        selectedImageFile = getFileFromUri(uri);
                    }
                }
        );
    }

    private void openImagePicker() {
        imagePickerLauncher.launch("image/*");
    }

    private File getFileFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File file = new File(getCacheDir(), "temp_image_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loadStaffData() {
        // Hide password field in edit mode
        etPassword.setVisibility(View.GONE);
        findViewById(R.id.labelPassword).setVisibility(View.GONE);

        // Hide email field in edit mode (email cannot be changed)
        etEmail.setEnabled(false);

        viewModel.getGarageStaffById(staffId).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        if (resource.getData() != null) {
                            populateForm(resource.getData());
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

    private void populateForm(com.example.prm392_mobile_carlinker.data.model.garagestaff.GarageStaffDto staff) {
        etFullName.setText(staff.getFullName());
        etEmail.setText(staff.getEmail());
        etPhoneNumber.setText(staff.getPhoneNumber());
        spinnerRole.setSelection(staff.getGarageRole());
        spinnerStatus.setSelection(staff.getUserStatus());

        if (staff.getImage() != null && !staff.getImage().isEmpty()) {
            Glide.with(this)
                    .load(staff.getImage())
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .circleCrop()
                    .into(ivStaffImage);
        }
    }

    private void saveStaff() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        int role = spinnerRole.getSelectedItemPosition();

        // Validation
        if (fullName.isEmpty()) {
            etFullName.setError("Vui lòng nhập họ tên");
            return;
        }

        if (!isEditMode) {
            if (email.isEmpty()) {
                etEmail.setError("Vui lòng nhập email");
                return;
            }

            if (password.isEmpty()) {
                etPassword.setError("Vui lòng nhập mật khẩu");
                return;
            }

            if (password.length() < 6) {
                etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
                return;
            }

            // Create new staff
            createStaff(email, password, fullName, phoneNumber, role);
        } else {
            // Update existing staff
            updateStaff(fullName, phoneNumber, role);
        }
    }

    private void createStaff(String email, String password, String fullName, String phoneNumber, int role) {
        GarageStaffCreateRequest request = new GarageStaffCreateRequest(
                email, password, fullName, phoneNumber, role
        );

        viewModel.createGarageStaff(request).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        btnSave.setEnabled(false);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Tạo nhân viên thành công", Toast.LENGTH_SHORT).show();

                        // Upload image if selected
                        if (selectedImageFile != null && resource.getData() != null) {
                            uploadStaffImage(resource.getData().getId());
                        } else {
                            finish();
                        }
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        btnSave.setEnabled(true);
                        Toast.makeText(this, "Lỗi: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void updateStaff(String fullName, String phoneNumber, int role) {
        int status = spinnerStatus.getSelectedItemPosition();

        GarageStaffUpdateRequest request = new GarageStaffUpdateRequest();
        request.setFullName(fullName);
        request.setPhoneNumber(phoneNumber);
        request.setGarageRole(role);
        request.setUserStatus(status);

        viewModel.updateGarageStaff(staffId, request).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        btnSave.setEnabled(false);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Cập nhật nhân viên thành công", Toast.LENGTH_SHORT).show();

                        // Upload image if selected
                        if (selectedImageFile != null) {
                            uploadStaffImage(staffId);
                        } else {
                            finish();
                        }
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        btnSave.setEnabled(true);
                        Toast.makeText(this, "Lỗi: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void uploadStaffImage(int staffId) {
        if (selectedImageFile == null) {
            finish();
            return;
        }

        viewModel.updateGarageStaffImage(staffId, selectedImageFile).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Cập nhật ảnh thành công", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Lỗi cập nhật ảnh: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                        finish(); // Still finish even if image upload fails
                        break;
                }
            }
        });
    }
}
