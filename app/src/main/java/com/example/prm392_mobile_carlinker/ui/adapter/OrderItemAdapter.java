package com.example.prm392_mobile_carlinker.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.order.OrderItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private Context context;
    private List<OrderItem> orderItems;

    public OrderItemAdapter(Context context) {
        this.context = context;
        this.orderItems = new ArrayList<>();
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems != null ? orderItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_detail, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem item = orderItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName;
        private TextView tvProductSku;
        private TextView tvQuantity;
        private TextView tvUnitPrice;
        private TextView tvSubtotal;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductSku = itemView.findViewById(R.id.tv_product_sku);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvUnitPrice = itemView.findViewById(R.id.tv_unit_price);
            tvSubtotal = itemView.findViewById(R.id.tv_subtotal);
        }

        public void bind(OrderItem item) {
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            if (item.getProductVariant() != null) {
                tvProductName.setText(item.getProductVariant().getName());
                tvProductSku.setText("SKU: " + item.getProductVariant().getSku());
            }

            tvQuantity.setText("x" + item.getQuantity());
            tvUnitPrice.setText(formatter.format(item.getUnitPrice()));
            tvSubtotal.setText(formatter.format(item.getSubtotal()));
        }
    }
}

