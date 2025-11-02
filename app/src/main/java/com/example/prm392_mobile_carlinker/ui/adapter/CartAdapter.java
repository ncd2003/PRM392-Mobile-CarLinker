package com.example.prm392_mobile_carlinker.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.cart.CartItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private OnCartItemActionListener listener;

    public interface OnCartItemActionListener {
        void onIncreaseQuantity(CartItem item);
        void onDecreaseQuantity(CartItem item);
        void onRemoveItem(CartItem item);
    }

    public CartAdapter(Context context, OnCartItemActionListener listener) {
        this.context = context;
        this.cartItems = new ArrayList<>();
        this.listener = listener;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName;
        private TextView tvProductPrice;
        private TextView tvQuantity;
        private TextView tvTotalPrice;
        private ImageButton btnDecrease;
        private ImageButton btnIncrease;
        private ImageButton btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }

        public void bind(CartItem item) {
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            if (item.getProductVariant() != null) {
                tvProductName.setText(item.getProductVariant().getName());
                tvProductPrice.setText(formatter.format(item.getProductVariant().getPrice()));
            }

            tvQuantity.setText(String.valueOf(item.getQuantity()));
            tvTotalPrice.setText(formatter.format(item.getTotalPrice()));

            btnIncrease.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onIncreaseQuantity(item);
                }
            });

            btnDecrease.setOnClickListener(v -> {
                if (listener != null && item.getQuantity() > 1) {
                    listener.onDecreaseQuantity(item);
                }
            });

            btnRemove.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRemoveItem(item);
                }
            });

            // Disable decrease button if quantity is 1
            btnDecrease.setEnabled(item.getQuantity() > 1);
        }
    }

    public double getTotalCartPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }
}
