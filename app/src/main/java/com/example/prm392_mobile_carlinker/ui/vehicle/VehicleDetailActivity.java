package com.example.prm392_mobile_carlinker.ui.vehicle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleResponse;
import com.example.prm392_mobile_carlinker.data.repository.Resource;
import com.example.prm392_mobile_carlinker.data.repository.VehicleRepository;

public class VehicleDetailActivity extends AppCompatActivity {

    private TextView tvPlate, tvBrandModel, tvYear, tvFuel, tvTransmission;
    private ImageView ivImage;
    private Button btnEdit;
    private VehicleRepository repository;
    private int vehicleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);

        tvPlate = findViewById(R.id.tvPlate);
        tvBrandModel = findViewById(R.id.tvBrandModel);
        tvYear = findViewById(R.id.tvYear);
        tvFuel = findViewById(R.id.tvFuel);
        tvTransmission = findViewById(R.id.tvTransmission);
        ivImage = findViewById(R.id.ivImage);
        btnEdit = findViewById(R.id.btnEdit);

        repository = new VehicleRepository();

        vehicleId = getIntent().getIntExtra("vehicleId", -1);
        if (vehicleId == -1) {
            Toast.makeText(this, "Vehicle not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadDetail();

        btnEdit.setOnClickListener(v -> {
            Intent i = new Intent(this, VehicleFormActivity.class);
            i.putExtra("vehicleId", vehicleId);
            startActivity(i);
        });
    }

    private void loadDetail() {
        repository.getVehicleById(vehicleId).observe(this, resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    // loading
                    break;

                case SUCCESS:
                    VehicleResponse.Data d = resource.getData();
                    if (d != null) {
                        tvPlate.setText("Biển: " + d.getLicensePlate());
                        tvBrandModel.setText(d.getBrand() + " " + d.getModel());
                        tvYear.setText("Năm: " + d.getYear());
                        tvFuel.setText("Nhiên liệu: " + d.getFuelType());
                        tvTransmission.setText("Hộp số: " + d.getTransmissionType());
                        if (d.getImage() != null && !d.getImage().isEmpty()) {
                            Glide.with(this).load(d.getImage()).into(ivImage);
                        }
                    }
                    break;

                case ERROR:
                    Toast.makeText(this, "Error: " + resource.getMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }
}
