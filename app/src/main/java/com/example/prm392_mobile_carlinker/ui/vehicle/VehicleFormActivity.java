package com.example.prm392_mobile_carlinker.ui.vehicle;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleRequest;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleResponse;
import com.example.prm392_mobile_carlinker.data.repository.Result;
import com.example.prm392_mobile_carlinker.data.repository.VehicleRepository;
import com.google.android.material.textfield.TextInputEditText;

public class VehicleFormActivity extends AppCompatActivity {

    private TextInputEditText etLicense, etBrand, etModel, etYear, etFuel, etTransmission, etImage;
    private Button btnSave;
    private VehicleRepository repository;
    private int vehicleId = -1;
    private boolean isEditMode = false;

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
        etImage = findViewById(R.id.etImage);
        btnSave = findViewById(R.id.btnSave);

        repository = new VehicleRepository();

        vehicleId = getIntent().getIntExtra("vehicleId", -1);
        isEditMode = vehicleId != -1;

        if (isEditMode) {
            loadForEdit();
        }

        btnSave.setOnClickListener(v -> saveVehicle());
    }

    private void loadForEdit() {
        repository.getVehicleById(vehicleId).observe(this, res -> {
            if (res == null) return;
            if (res.status == Result.Status.SUCCESS) {
                VehicleResponse vr = res.data;
                if (vr != null && vr.getData() != null) {
                    VehicleResponse.Data d = vr.getData();
                    etLicense.setText(d.getLicensePlate());
                    etBrand.setText(d.getBrand());
                    etModel.setText(d.getModel());
                    etYear.setText(String.valueOf(d.getYear()));
                    etFuel.setText(d.getFuelType());
                    etTransmission.setText(d.getTransmissionType());
                    etImage.setText(d.getImage());
                }
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
        String image = etImage.getText() != null ? etImage.getText().toString().trim() : "";

        if (TextUtils.isEmpty(plate)) { etLicense.setError("Nhập biển số"); return; }
        int year = 0;
        if (!TextUtils.isEmpty(yearStr)) {
            try { year = Integer.parseInt(yearStr); } catch (NumberFormatException e) { etYear.setError("Năm không hợp lệ"); return; }
        }

        VehicleRequest req = new VehicleRequest(plate, fuel, transmission, brand, model, year, image);

        if (isEditMode) {
            repository.updateVehicle(vehicleId, req).observe(this, res -> {
                if (res == null) return;
                if (res.status == Result.Status.SUCCESS) {
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, res.message, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            repository.addVehicle(req).observe(this, res -> {
                if (res == null) return;
                if (res.status == Result.Status.SUCCESS) {
                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, res.message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
