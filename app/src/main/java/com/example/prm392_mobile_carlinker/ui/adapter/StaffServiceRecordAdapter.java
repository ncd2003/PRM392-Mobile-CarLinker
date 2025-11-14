package com.example.prm392_mobile_carlinker.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.servicerecord.ServiceRecordDto;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying service records in STAFF view
 */
public class StaffServiceRecordAdapter extends RecyclerView.Adapter<StaffServiceRecordAdapter.ViewHolder> {

    private List<ServiceRecordDto> serviceRecords = new ArrayList<>();
    private Context context;
    private OnServiceRecordClickListener listener;

    public interface OnServiceRecordClickListener {
        void onServiceRecordClick(ServiceRecordDto record);
    }

    public StaffServiceRecordAdapter(Context context, OnServiceRecordClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setServiceRecords(List<ServiceRecordDto> records) {
        this.serviceRecords = records != null ? records : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_staff_service_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceRecordDto record = serviceRecords.get(position);
        holder.bind(record);
    }

    @Override
    public int getItemCount() {
        return serviceRecords.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView tvRecordId;
        private TextView tvCustomerName;
        private TextView tvVehicle;
        private TextView tvStatus;
        private TextView tvStartTime;
        private TextView tvTotalCost;
        private TextView tvServiceCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            tvRecordId = itemView.findViewById(R.id.tvRecordId);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvVehicle = itemView.findViewById(R.id.tvVehicle);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvTotalCost = itemView.findViewById(R.id.tvTotalCost);
            tvServiceCount = itemView.findViewById(R.id.tvServiceCount);
        }

        public void bind(ServiceRecordDto record) {
            // Record ID
            tvRecordId.setText("#" + record.getId());

            // Customer info
            if (record.getUser() != null) {
                tvCustomerName.setText(record.getUser().getFullName());
            }

            // Vehicle info
            if (record.getVehicle() != null) {
                String vehicleInfo = record.getVehicle().getLicensePlate() + " - " +
                        record.getVehicle().getBrand() + " " + record.getVehicle().getModel();
                tvVehicle.setText(vehicleInfo);
            }

            // Status
            tvStatus.setText(record.getStatusText());
            int statusColor = getStatusColor(record.getServiceRecordStatus());
            tvStatus.setTextColor(statusColor);

            // Start time
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                java.util.Date date = inputFormat.parse(record.getStartTime());
                if (date != null) {
                    tvStartTime.setText(outputFormat.format(date));
                }
            } catch (Exception e) {
                tvStartTime.setText(record.getStartTime());
            }

            // Total cost
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvTotalCost.setText(formatter.format(record.getTotalCost()));

            // Service count
            int serviceCount = record.getServiceItems() != null ? record.getServiceItems().size() : 0;
            tvServiceCount.setText(serviceCount + " dịch vụ");

            // Click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onServiceRecordClick(record);
                }
            });
        }

        private int getStatusColor(int status) {
            switch (status) {
                case 0: // Pending
                    return Color.parseColor("#FF9800"); // Orange
                case 1: // InProgress
                    return Color.parseColor("#2196F3"); // Blue
                case 2: // Completed
                    return Color.parseColor("#4CAF50"); // Green
                case 3: // Cancelled
                    return Color.parseColor("#F44336"); // Red
                default:
                    return Color.parseColor("#9E9E9E"); // Gray
            }
        }
    }
}

