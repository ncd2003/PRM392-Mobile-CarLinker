package com.example.prm392_mobile_carlinker.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.garage.ServiceCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying service categories in Garage Detail (read-only, no CRUD buttons)
 */
public class GarageServiceCategoryAdapter extends RecyclerView.Adapter<GarageServiceCategoryAdapter.ViewHolder> {

    private List<ServiceCategory> categories = new ArrayList<>();

    public void setCategories(List<ServiceCategory> categories) {
        this.categories = categories != null ? categories : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_garage_service_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceCategory category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategoryName;
        private RecyclerView rvServiceItems;
        private GarageServiceItemAdapter serviceItemAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            rvServiceItems = itemView.findViewById(R.id.rvServiceItems);

            // Setup nested RecyclerView for service items
            serviceItemAdapter = new GarageServiceItemAdapter();
            rvServiceItems.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            rvServiceItems.setAdapter(serviceItemAdapter);
            rvServiceItems.setNestedScrollingEnabled(false);
        }

        public void bind(ServiceCategory category) {
            tvCategoryName.setText(category.getName());

            if (category.getServiceItems() != null && !category.getServiceItems().isEmpty()) {
                serviceItemAdapter.setServiceItems(category.getServiceItems());
                rvServiceItems.setVisibility(View.VISIBLE);
            } else {
                rvServiceItems.setVisibility(View.GONE);
            }
        }
    }
}
