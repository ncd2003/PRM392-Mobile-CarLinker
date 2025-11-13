package com.example.prm392_mobile_carlinker.ui.vehicle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleRequest;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleResponse;
import com.example.prm392_mobile_carlinker.data.repository.Resource;
import com.example.prm392_mobile_carlinker.data.repository.Result;
import com.example.prm392_mobile_carlinker.data.repository.VehicleRepository;
import com.google.android.material.textfield.TextInputEditText;

public class VehicleFormActivity extends AppCompatActivity {

    private TextInputEditText etLicense, etBrand, etModel, etYear, etFuel, etTransmission;
    private Button btnSave, btnSelectImage;
    private ImageView ivPreview;
    private Uri selectedImageUri = null;

    private VehicleRepository repository;
    private int vehicleId = -1;
    private boolean isEditMode = false;
    private static final int PICK_IMAGE_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_form);

        etLicense = findViewById(R.id.etLicense);
        etBrand = findViewById(R.id.etBrand);
        etModel = findViewById(R.id.etModel);
        etYear = findViewById(R.id.etYear);
        etFuel = findViewById(R.id.etFuel);
        etTransmission = findViewById(R.id.etTransmission);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        ivPreview = findViewById(R.id.ivPreview);
        btnSave = findViewById(R.id.btnSave);

        repository = new VehicleRepository();

        vehicleId = getIntent().getIntExtra("vehicleId", -1);
        isEditMode = vehicleId != -1;

        if (isEditMode) loadForEdit();

        btnSelectImage.setOnClickListener(v -> openImagePicker());
        btnSave.setOnClickListener(v -> saveVehicle());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh xe"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            ivPreview.setImageURI(selectedImageUri);
        }
    }

    private void loadForEdit() {
        repository.getVehicleById(vehicleId).observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case SUCCESS:
                    VehicleResponse.Data d = resource.getData();
                    if (d != null) {
                        etLicense.setText(d.getLicensePlate());
                        etBrand.setText(d.getBrand());
                        etModel.setText(d.getModel());
                        etYear.setText(String.valueOf(d.getYear()));
                        etFuel.setText(d.getFuelType());
                        etTransmission.setText(d.getTransmissionType());
                        if (d.getImage() != null && !d.getImage().isEmpty()) {
                            Glide.with(this).load(d.getImage()).into(ivPreview);
                        }
                    }
                    break;

                case ERROR:
                    Toast.makeText(this, "Lỗi tải dữ liệu: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                    break;

                case LOADING:
                    // Show loading if needed
                    break;
            }
        });
    }

    private void saveVehicle() {
        String plate = etLicense.getText() != null ? etLicense.getText().toString().trim() : "";
        String brand = etBrand.getText() != null ? etBrand.getText().toString().trim() : "";
        String model = etModel.getText() != null ? etModel.getText().toString().trim() : "";
        String yearStr = etYear.getText() != null ? etYear.getText().toString().trim() : "";
        String fuel = etFuel.getText() != null ? etFuel.getText().toString().trim() : "";
        String transmission = etTransmission.getText() != null ? etTransmission.getText().toString().trim() : "";

        if (TextUtils.isEmpty(plate)) { etLicense.setError("Nhập biển số"); return; }
        int year = 0;
        if (!TextUtils.isEmpty(yearStr)) {
            try { year = Integer.parseInt(yearStr); } catch (NumberFormatException e) { etYear.setError("Năm không hợp lệ"); return; }
        }

        VehicleRequest req = new VehicleRequest(plate, fuel, transmission, brand, model, year);
        req.setImageUri(selectedImageUri);

        if (isEditMode) {
            repository.updateVehicle(this, vehicleId, req).observe(this, res -> {
                if (res == null) return;
                if (res.status == Result.Status.SUCCESS) {
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this,"cập nhật thất bại", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            repository.addVehicle(this, req).observe(this, res -> {
                if (res == null) return;
                if (res.status == Result.Status.SUCCESS) {
                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "thêm thất bại", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}