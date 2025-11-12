package com.example.prm392_mobile_carlinker.ui.vehicle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleResponse;
import com.example.prm392_mobile_carlinker.data.repository.Result;
import com.example.prm392_mobile_carlinker.data.repository.VehicleRepository;
import com.example.prm392_mobile_carlinker.ui.adapter.VehicleAdapter;

import java.util.List;

public class VehicleListActivity extends AppCompatActivity implements VehicleAdapter.VehicleListener {

    private RecyclerView rv;
    private VehicleAdapter adapter;
    private VehicleRepository repository;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

        rv = findViewById(R.id.rvVehicles);
        btnAdd = findViewById(R.id.btnAdd);
        repository = new VehicleRepository();

        adapter = new VehicleAdapter(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(this, VehicleFormActivity.class)); // add mode
        });

        loadVehicles();
    }

    private void loadVehicles() {
        repository.getAllVehicles().observe(this, res -> {
            if (res == null) return;
            if (res.status == Result.Status.LOADING) {
                // show loading if you want
            } else if (res.status == Result.Status.SUCCESS) {
                List<VehicleResponse.Data> list = res.data != null ? res.data.getData() : null;
                adapter.setList(list);
            } else {
                Toast.makeText(this, res.message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onView(VehicleResponse.Data vehicle) {
        Intent i = new Intent(this, VehicleDetailActivity.class);
        i.putExtra("vehicleId", vehicle.getId());
        startActivity(i);
    }

    @Override
    public void onEdit(VehicleResponse.Data vehicle) {
        Intent i = new Intent(this, VehicleFormActivity.class);
        i.putExtra("vehicleId", vehicle.getId());
        startActivity(i);
    }

    @Override
    public void onDelete(VehicleResponse.Data vehicle) {
        repository.deleteVehicle(vehicle.getId()).observe(this, res -> {
            if (res == null) return;
            if (res.status == Result.Status.SUCCESS) {
                Toast.makeText(this, "Xoá thành công", Toast.LENGTH_SHORT).show();
                loadVehicles();
            } else if (res.status == Result.Status.ERROR) {
                Toast.makeText(this, res.message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
