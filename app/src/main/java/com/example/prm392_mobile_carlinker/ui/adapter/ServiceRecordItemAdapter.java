package com.example.prm392_mobile_carlinker.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.servicerecord.ServiceRecordDto;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying service items in service record detail
 */
public class ServiceRecordItemAdapter extends RecyclerView.Adapter<ServiceRecordItemAdapter.ViewHolder> {

    private List<ServiceRecordDto.ServiceItemDto> serviceItems = new ArrayList<>();

    public void setServiceItems(List<ServiceRecordDto.ServiceItemDto> items) {
        this.serviceItems = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceRecordDto.ServiceItemDto item = serviceItems.get(position);
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

        public void bind(ServiceRecordDto.ServiceItemDto item) {
            tvServiceName.setText(item.getName());

            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvServicePrice.setText(formatter.format(item.getPrice()));
        }
    }
}

