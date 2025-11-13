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
import com.example.prm392_mobile_carlinker.data.model.garage.ServiceItem;

import java.util.ArrayList;
import java.util.List;

public class ServiceCategoryBookingAdapter extends RecyclerView.Adapter<ServiceCategoryBookingAdapter.ViewHolder> {

    private List<ServiceCategory> categories = new ArrayList<>();
    private ServiceItemBookingAdapter.OnServiceItemSelectionListener listener;

    public ServiceCategoryBookingAdapter(ServiceItemBookingAdapter.OnServiceItemSelectionListener listener) {
        this.listener = listener;
    }

    public void setCategories(List<ServiceCategory> categories) {
        this.categories = categories != null ? categories : new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<ServiceItem> getAllSelectedItems() {
        List<ServiceItem> allSelected = new ArrayList<>();
        // Collect selected items from all child adapters
        // This will be managed by the activity
        return allSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_category_booking, parent, false);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategoryName;
        private RecyclerView rvServiceItems;
        private ServiceItemBookingAdapter itemAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            rvServiceItems = itemView.findViewById(R.id.rvServiceItems);

            rvServiceItems.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            itemAdapter = new ServiceItemBookingAdapter(listener);
            rvServiceItems.setAdapter(itemAdapter);
        }

        public void bind(ServiceCategory category) {
            tvCategoryName.setText(category.getName());
            itemAdapter.setServiceItems(category.getServiceItems());
        }
    }
}

