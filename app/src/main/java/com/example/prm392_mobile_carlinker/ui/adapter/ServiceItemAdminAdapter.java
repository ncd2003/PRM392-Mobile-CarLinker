package com.example.prm392_mobile_carlinker.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemDto;

import java.util.ArrayList;
import java.util.List;

public class ServiceItemAdminAdapter extends RecyclerView.Adapter<ServiceItemAdminAdapter.ServiceItemViewHolder> {

    private List<ServiceItemDto> serviceItemList = new ArrayList<>();
    private OnServiceItemClickListener listener;

    public interface OnServiceItemClickListener {
        void onViewClick(ServiceItemDto serviceItem);
        void onEditClick(ServiceItemDto serviceItem);
        void onDeleteClick(ServiceItemDto serviceItem);
    }

    public ServiceItemAdminAdapter(OnServiceItemClickListener listener) {
        this.listener = listener;
    }

    public void setServiceItems(List<ServiceItemDto> serviceItems) {
        this.serviceItemList = serviceItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_item_admin, parent, false);
        return new ServiceItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceItemViewHolder holder, int position) {
        ServiceItemDto item = serviceItemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return serviceItemList.size();
    }

    class ServiceItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemName;
        private TextView tvItemId;
        private TextView tvItemPrice;
        private Button btnView;
        private Button btnEdit;
        private Button btnDelete;

        public ServiceItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemId = itemView.findViewById(R.id.tvItemId);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            btnView = itemView.findViewById(R.id.btnView);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(ServiceItemDto item) {
            tvItemName.setText(item.getName());
            tvItemId.setText("ID: " + item.getId());

            // Format price as Vietnamese currency
            java.text.NumberFormat formatter = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("vi", "VN"));
            tvItemPrice.setText(formatter.format(item.getPrice()));

            btnView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onViewClick(item);
                }
            });

            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(item);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(item);
                }
            });
        }
    }
}
