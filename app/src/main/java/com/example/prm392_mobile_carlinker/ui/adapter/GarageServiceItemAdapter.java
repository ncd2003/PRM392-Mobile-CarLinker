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
 * Adapter for displaying service items in Garage Detail
 */
public class GarageServiceItemAdapter extends RecyclerView.Adapter<GarageServiceItemAdapter.ServiceItemViewHolder> {

    private List<ServiceItem> serviceItemList = new ArrayList<>();

    public void setServiceItems(List<ServiceItem> serviceItems) {
        this.serviceItemList = serviceItems != null ? serviceItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_item, parent, false);
        return new ServiceItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceItemViewHolder holder, int position) {
        ServiceItem item = serviceItemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return serviceItemList.size();
    }

    static class ServiceItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvServiceItemName;
        private TextView tvServiceItemPrice;

        public ServiceItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceItemName = itemView.findViewById(R.id.tvServiceItemName);
            tvServiceItemPrice = itemView.findViewById(R.id.tvServiceItemPrice);
        }

        public void bind(ServiceItem item) {
            tvServiceItemName.setText(item.getName());

            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvServiceItemPrice.setText(formatter.format(item.getPrice()));
        }
    }
}

