package com.example.prm392_mobile_carlinker.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.garage.ServiceItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying service items in garage detail (read-only view)
 */
public class GarageDetailServiceItemAdapter extends RecyclerView.Adapter<GarageDetailServiceItemAdapter.ViewHolder> {

    private List<ServiceItem> serviceItems = new ArrayList<>();

    public void setServiceItems(List<ServiceItem> items) {
        this.serviceItems = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_garage_detail_service_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceItem item = serviceItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return serviceItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvServiceName;
        private TextView tvServicePrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvServicePrice = itemView.findViewById(R.id.tvServicePrice);
        }

        public void bind(ServiceItem item) {
            tvServiceName.setText(item.getName());

            // Format price
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvServicePrice.setText(formatter.format(item.getPrice()));
        }
    }
}

