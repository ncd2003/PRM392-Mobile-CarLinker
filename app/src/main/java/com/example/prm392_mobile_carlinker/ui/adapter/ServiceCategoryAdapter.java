package com.example.prm392_mobile_carlinker.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.servicecategory.ServiceCategory;
import com.example.prm392_mobile_carlinker.data.model.servicecategory.ServiceItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServiceCategoryAdapter extends RecyclerView.Adapter<ServiceCategoryAdapter.ServiceCategoryViewHolder> {

    private List<ServiceCategory> serviceCategoryList = new ArrayList<>();
    private OnServiceCategoryClickListener listener;

    public interface OnServiceCategoryClickListener {
        void onViewClick(ServiceCategory serviceCategory);
        void onEditClick(ServiceCategory serviceCategory);
        void onDeleteClick(ServiceCategory serviceCategory);
    }

    public ServiceCategoryAdapter(OnServiceCategoryClickListener listener) {
        this.listener = listener;
    }

    public void setServiceCategories(List<ServiceCategory> serviceCategories) {
        this.serviceCategoryList = serviceCategories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_category, parent, false);
        return new ServiceCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceCategoryViewHolder holder, int position) {
        ServiceCategory category = serviceCategoryList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return serviceCategoryList.size();
    }

    class ServiceCategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategoryName;
        private TextView tvServiceItemsCount;
        private TextView tvServiceItems;
        private Button btnView;
        private Button btnEdit;
        private Button btnDelete;

        public ServiceCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvServiceItemsCount = itemView.findViewById(R.id.tvServiceItemsCount);
            tvServiceItems = itemView.findViewById(R.id.tvServiceItems);
            btnView = itemView.findViewById(R.id.btnView);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(ServiceCategory category) {
            tvCategoryName.setText(category.getName());

            // Display service items count
            int itemsCount = category.getServiceItems() != null ? category.getServiceItems().size() : 0;
            tvServiceItemsCount.setText(itemsCount + " dịch vụ");

            // Display service items details
            if (category.getServiceItems() != null && !category.getServiceItems().isEmpty()) {
                StringBuilder sb = new StringBuilder();
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

                for (int i = 0; i < Math.min(3, category.getServiceItems().size()); i++) {
                    ServiceItem item = category.getServiceItems().get(i);
                    sb.append("• ").append(item.getName())
                            .append(" - ").append(formatter.format(item.getPrice()));
                    if (i < Math.min(2, category.getServiceItems().size() - 1)) {
                        sb.append("\n");
                    }
                }

                if (category.getServiceItems().size() > 3) {
                    sb.append("\n... và ").append(category.getServiceItems().size() - 3).append(" dịch vụ khác");
                }

                tvServiceItems.setText(sb.toString());
                tvServiceItems.setVisibility(View.VISIBLE);
            } else {
                tvServiceItems.setVisibility(View.GONE);
            }

            // Set click listeners
            btnView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onViewClick(category);
                }
            });

            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(category);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(category);
                }
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onViewClick(category);
                }
            });
        }
    }
}

