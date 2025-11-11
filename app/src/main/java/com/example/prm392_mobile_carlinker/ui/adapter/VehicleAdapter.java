package com.example.prm392_mobile_carlinker.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleResponse;

import java.util.ArrayList;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.Vh> {

    public interface VehicleListener {
        void onView(VehicleResponse.Data vehicle);
        void onEdit(VehicleResponse.Data vehicle);
        void onDelete(VehicleResponse.Data vehicle);
    }

    private List<VehicleResponse.Data> list = new ArrayList<>();
    private VehicleListener listener;

    public VehicleAdapter(VehicleListener listener) { this.listener = listener; }

    public void setList(List<VehicleResponse.Data> data) {
        list.clear();
        if (data != null) list.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle, parent, false);
        return new Vh(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        VehicleResponse.Data v = list.get(position);
        holder.tvPlate.setText("Biển: " + v.getLicensePlate());
        holder.tvBrandModel.setText((v.getBrand() == null ? "" : v.getBrand()) + " " + (v.getModel() == null ? "" : v.getModel()));
        holder.itemView.setOnClickListener(view -> {
            if (listener != null) listener.onView(v);
        });
        holder.btnEdit.setOnClickListener(view -> {
            if (listener != null) listener.onEdit(v);
        });
        holder.btnDelete.setOnClickListener(view -> {
            if (listener != null) listener.onDelete(v);
        });
    }

    @Override public int getItemCount() { return list.size(); }

    static class Vh extends RecyclerView.ViewHolder {
        TextView tvPlate, tvBrandModel;
        ImageButton btnEdit, btnDelete;
        Vh(@NonNull View itemView) {
            super(itemView);
            tvPlate = itemView.findViewById(R.id.tvPlate);
            tvBrandModel = itemView.findViewById(R.id.tvBrandModel);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
