package com.example.prm392_mobile_carlinker.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.garage.Garage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter hiển thị danh sách garage trong RecyclerView
 */
public class GarageAdapter extends RecyclerView.Adapter<GarageAdapter.GarageViewHolder> {

    private List<Garage> garageList;
    private OnGarageClickListener listener;

    public interface OnGarageClickListener {
        void onGarageClick(Garage garage);
        void onCallClick(Garage garage);
    }

    public GarageAdapter(OnGarageClickListener listener) {
        this.garageList = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public GarageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_garage, parent, false);
        return new GarageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GarageViewHolder holder, int position) {
        Garage garage = garageList.get(position);
        holder.bind(garage);
    }

    @Override
    public int getItemCount() {
        return garageList.size();
    }

    public void setGarageList(List<Garage> garageList) {
        this.garageList = garageList;
        notifyDataSetChanged();
    }

    class GarageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgGarage;
        private TextView tvGarageName;
        private TextView tvGarageDescription;
        private TextView tvOperatingTime;
        private TextView tvDistance;
        private TextView tvPhoneNumber;
        private View btnCall;

        public GarageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGarage = itemView.findViewById(R.id.img_garage);
            tvGarageName = itemView.findViewById(R.id.tv_garage_name);
            tvGarageDescription = itemView.findViewById(R.id.tv_garage_description);
            tvOperatingTime = itemView.findViewById(R.id.tv_operating_time);
            tvDistance = itemView.findViewById(R.id.tv_distance);
            tvPhoneNumber = itemView.findViewById(R.id.tv_phone_number);
            btnCall = itemView.findViewById(R.id.btn_call);
        }

        public void bind(final Garage garage) {
            // Load image
            Glide.with(itemView.getContext())
                    .load(garage.getImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(imgGarage);

            // Set data
            tvGarageName.setText(garage.getName());
            tvGarageDescription.setText(garage.getDescription());
            tvOperatingTime.setText("⏰ " + garage.getOperatingTime());
            tvPhoneNumber.setText("📞 " + garage.getPhoneNumber());

            // Hiển thị khoảng cách
            if (garage.getDistance() > 0) {
                tvDistance.setVisibility(View.VISIBLE);
                tvDistance.setText(String.format(Locale.getDefault(),
                        "📍 %.2f km", garage.getDistance()));
            } else {
                tvDistance.setVisibility(View.GONE);
            }

            // Click vào item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onGarageClick(garage);
                    }
                }
            });

            // Click vào nút gọi
            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onCallClick(garage);
                    }
                }
            });
        }
    }
}

