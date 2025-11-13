package com.example.prm392_mobile_carlinker.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.garage.ServiceItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ServiceItemBookingAdapter extends RecyclerView.Adapter<ServiceItemBookingAdapter.ViewHolder> {

    private List<ServiceItem> serviceItems = new ArrayList<>();
    private Set<Integer> selectedItemIds = new HashSet<>();
    private OnServiceItemSelectionListener listener;

    public interface OnServiceItemSelectionListener {
        void onSelectionChanged(List<ServiceItem> selectedItems, double totalPrice);
    }

    public ServiceItemBookingAdapter(OnServiceItemSelectionListener listener) {
        this.listener = listener;
    }

    public void setServiceItems(List<ServiceItem> items) {
        this.serviceItems = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<ServiceItem> getSelectedItems() {
        List<ServiceItem> selected = new ArrayList<>();
        for (ServiceItem item : serviceItems) {
            if (selectedItemIds.contains(item.getId())) {
                selected.add(item);
            }
        }
        return selected;
    }

    public void clearSelection() {
        selectedItemIds.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_item_booking, parent, false);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cbServiceItem;
        private TextView tvServicePrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbServiceItem = itemView.findViewById(R.id.cbServiceItem);
            tvServicePrice = itemView.findViewById(R.id.tvServicePrice);
        }

        public void bind(ServiceItem item) {
            cbServiceItem.setText(item.getName());

            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvServicePrice.setText(formatter.format(item.getPrice()));

            cbServiceItem.setChecked(selectedItemIds.contains(item.getId()));

            cbServiceItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedItemIds.add(item.getId());
                } else {
                    selectedItemIds.remove(item.getId());
                }

                if (listener != null) {
                    double totalPrice = 0;
                    List<ServiceItem> selectedItems = getSelectedItems();
                    for (ServiceItem selectedItem : selectedItems) {
                        totalPrice += selectedItem.getPrice();
                    }
                    listener.onSelectionChanged(selectedItems, totalPrice);
                }
            });
        }
    }
}

