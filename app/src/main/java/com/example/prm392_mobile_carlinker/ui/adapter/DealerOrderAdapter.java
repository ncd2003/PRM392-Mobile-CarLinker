package com.example.prm392_mobile_carlinker.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class DealerOrderAdapter extends RecyclerView.Adapter<DealerOrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orders;
    private List<Order> ordersFiltered;
    private OnOrderClickListener listener;
    private OnUpdateStatusClickListener updateStatusListener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public interface OnUpdateStatusClickListener {
        void onUpdateStatusClick(Order order);
    }

    public DealerOrderAdapter(Context context, OnOrderClickListener listener) {
        this.context = context;
        this.orders = new ArrayList<>();
        this.ordersFiltered = new ArrayList<>();
        this.listener = listener;
    }

    public void setUpdateStatusListener(OnUpdateStatusClickListener listener) {
        this.updateStatusListener = listener;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders != null ? orders : new ArrayList<>();
        this.ordersFiltered = new ArrayList<>(this.orders);
        notifyDataSetChanged();
    }

    public void filterByStatus(int status) {
        ordersFiltered.clear();
        if (status == -1) {
            // Show all
            ordersFiltered.addAll(orders);
        } else {
            // Filter by status
            for (Order order : orders) {
                if (order.getStatus() == status) {
                    ordersFiltered.add(order);
                }
            }
        }
        notifyDataSetChanged();
    }

    public int getTotalOrders() {
        return ordersFiltered.size();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dealer_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = ordersFiltered.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return ordersFiltered.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderCode;
        private TextView tvCustomerName;
        private TextView tvCustomerPhone;
        private TextView tvOrderDate;
        private TextView tvOrderStatus;
        private TextView tvTotalAmount;
        private TextView tvPaymentMethod;
        private TextView tvShippingAddress;
        private Button btnUpdateStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderCode = itemView.findViewById(R.id.tv_order_code);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            tvCustomerPhone = itemView.findViewById(R.id.tv_customer_phone);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvTotalAmount = itemView.findViewById(R.id.tv_total_amount);
            tvPaymentMethod = itemView.findViewById(R.id.tv_payment_method);
            tvShippingAddress = itemView.findViewById(R.id.tv_shipping_address);
            btnUpdateStatus = itemView.findViewById(R.id.btn_update_status);
        }

        public void bind(Order order) {
            // Order code
            tvOrderCode.setText(order.getOrderCode());

            // Customer info
            tvCustomerName.setText(order.getFullName());
            tvCustomerPhone.setText(order.getPhoneNumber());

            // Order date
            tvOrderDate.setText(formatDate(order.getOrderDate()));

            // Order status
            tvOrderStatus.setText(order.getStatusText());
            setStatusColor(order.getStatus());

            // Total amount
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvTotalAmount.setText(formatter.format(order.getTotalAmount()));

            // Payment method
            tvPaymentMethod.setText(order.getPaymentMethod());

            // Shipping address
            tvShippingAddress.setText(order.getShippingAddress());

            // Update status button - always visible for dealer to update
            btnUpdateStatus.setVisibility(View.VISIBLE);
            btnUpdateStatus.setOnClickListener(v -> {
                if (updateStatusListener != null) {
                    updateStatusListener.onUpdateStatusClick(order);
                }
            });

            // Click listener for the whole item
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
