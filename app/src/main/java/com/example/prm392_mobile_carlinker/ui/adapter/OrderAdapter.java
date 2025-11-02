package com.example.prm392_mobile_carlinker.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.order.Order;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orders;
    private OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
        void onCancelOrderClick(Order order);
    }

    public OrderAdapter(Context context, OnOrderClickListener listener) {
        this.context = context;
        this.orders = new ArrayList<>();
        this.listener = listener;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders != null ? orders : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderCode;
        private TextView tvOrderDate;
        private TextView tvOrderStatus;
        private TextView tvTotalAmount;
        private TextView tvItemsCount;
        private TextView tvPaymentMethod;
        private android.widget.Button btnCancelOrder;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderCode = itemView.findViewById(R.id.tv_order_code);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvTotalAmount = itemView.findViewById(R.id.tv_total_amount);
            tvItemsCount = itemView.findViewById(R.id.tv_items_count);
            tvPaymentMethod = itemView.findViewById(R.id.tv_payment_method);
            btnCancelOrder = itemView.findViewById(R.id.btn_cancel_order);
        }

        public void bind(Order order) {
            // Order code
            tvOrderCode.setText(order.getOrderCode());

            // Order date
            tvOrderDate.setText(formatDate(order.getOrderDate()));

            // Order status
            tvOrderStatus.setText(order.getStatusText());
            setStatusColor(order.getStatus());

            // Total amount
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvTotalAmount.setText(formatter.format(order.getTotalAmount()));

            // Items count
            int itemsCount = order.getTotalItemsCount();
            tvItemsCount.setText(itemsCount + " sản phẩm");

            // Payment method
            tvPaymentMethod.setText(order.getPaymentMethod());

            // Show/Hide cancel button based on order status
            if (order.canBeCancelled()) {
                btnCancelOrder.setVisibility(View.VISIBLE);
                btnCancelOrder.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onCancelOrderClick(order);
                    }
                });
            } else {
                btnCancelOrder.setVisibility(View.GONE);
            }

            // Click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onOrderClick(order);
                }
            });
        }

        private String formatDate(String dateString) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                Date date = inputFormat.parse(dateString);
                return outputFormat.format(date);
            } catch (ParseException e) {
                return dateString;
            }
        }

        private void setStatusColor(int status) {
            int color;
            switch (status) {
                case 0: // PENDING - Chờ xác nhận
                    color = context.getResources().getColor(android.R.color.holo_orange_dark);
                    break;
                case 1: // CONFIRMED - Đã xác nhận
                    color = context.getResources().getColor(android.R.color.holo_blue_dark);
                    break;
                case 2: // PACKED - Đã đóng gói
                    color = context.getResources().getColor(android.R.color.holo_blue_light);
                    break;
                case 3: // SHIPPING - Đang giao
                    color = context.getResources().getColor(android.R.color.holo_purple);
                    break;
                case 4: // DELIVERED - Đã giao
                    color = context.getResources().getColor(android.R.color.holo_green_dark);
                    break;
                case 5: // CANCELLED - Đã hủy
                    color = context.getResources().getColor(android.R.color.holo_red_dark);
                    break;
                case 6: // FAILED - Giao thất bại
                    color = context.getResources().getColor(android.R.color.holo_red_light);
                    break;
                default:
                    color = context.getResources().getColor(android.R.color.darker_gray);
            }
            tvOrderStatus.setTextColor(color);
        }
    }
}
