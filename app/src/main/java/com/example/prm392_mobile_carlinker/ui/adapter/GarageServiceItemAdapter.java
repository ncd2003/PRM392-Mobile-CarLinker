package com.example.prm392_mobile_carlinker.ui.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.garage.GarageUpdateServiceItem;
import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemDto;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Adapter hiển thị danh sách service items cho GARAGE với checkbox và input giá
 */
public class GarageServiceItemAdapter extends RecyclerView.Adapter<GarageServiceItemAdapter.ViewHolder> {

    private List<ServiceItemDto> serviceItems = new ArrayList<>();
    private Map<Integer, Boolean> selectedItems = new HashMap<>();
    private Map<Integer, Double> priceMap = new HashMap<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_garage_service_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceItemDto item = serviceItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return serviceItems.size();
    }

    public void setServiceItems(List<ServiceItemDto> items) {
        this.serviceItems = items;
        notifyDataSetChanged();
    }

    /**
     * Lấy danh sách service items đã chọn với giá
     */
    public List<GarageUpdateServiceItem> getSelectedItemsWithPrice() {
        List<GarageUpdateServiceItem> result = new ArrayList<>();

        for (ServiceItemDto item : serviceItems) {
            Boolean isSelected = selectedItems.get(item.getId());
            if (isSelected != null && isSelected) {
                Double price = priceMap.get(item.getId());
                if (price != null && price > 0) {
                    result.add(new GarageUpdateServiceItem(item.getId(), price));
                }
            }
        }

        return result;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView tvServiceName;
        private EditText etPrice;
        private TextView tvCurrency;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            etPrice = itemView.findViewById(R.id.etPrice);
            tvCurrency = itemView.findViewById(R.id.tvCurrency);
        }

        public void bind(ServiceItemDto item) {
            tvServiceName.setText(item.getName());

            // Set checkbox state
            Boolean isSelected = selectedItems.get(item.getId());
            checkBox.setChecked(isSelected != null && isSelected);

            // Set price if exists
            Double price = priceMap.get(item.getId());
            if (price != null && price > 0) {
                etPrice.setText(String.valueOf(price));
            } else {
                etPrice.setText("");
            }

            // Enable/disable price input based on checkbox
            etPrice.setEnabled(checkBox.isChecked());
            tvCurrency.setEnabled(checkBox.isChecked());

            // Checkbox listener
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                selectedItems.put(item.getId(), isChecked);
                etPrice.setEnabled(isChecked);
                tvCurrency.setEnabled(isChecked);

                if (!isChecked) {
                    etPrice.setText("");
                    priceMap.remove(item.getId());
                }
            });

            // Price input listener
            etPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().isEmpty()) {
                        priceMap.remove(item.getId());
                    } else {
                        try {
                            double price = Double.parseDouble(s.toString());
                            priceMap.put(item.getId(), price);
                        } catch (NumberFormatException e) {
                            priceMap.remove(item.getId());
                        }
                    }
                }
            });
        }
    }
}

