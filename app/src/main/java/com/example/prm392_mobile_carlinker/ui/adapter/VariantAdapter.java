package com.example.prm392_mobile_carlinker.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.product.ProductVariant;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VariantAdapter extends RecyclerView.Adapter<VariantAdapter.VariantViewHolder> {

    private Context context;
    private List<ProductVariant> variants;
    private ProductVariant selectedVariant;
    private OnVariantSelectedListener listener;

    public interface OnVariantSelectedListener {
        void onVariantSelected(ProductVariant variant);
    }

    public VariantAdapter(Context context, OnVariantSelectedListener listener) {
        this.context = context;
        this.variants = new ArrayList<>();
        this.listener = listener;
    }

    public void setVariants(List<ProductVariant> variants) {
        this.variants = variants;
        notifyDataSetChanged();
    }

    public void setSelectedVariant(ProductVariant variant) {
        this.selectedVariant = variant;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VariantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_variant, parent, false);
        return new VariantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VariantViewHolder holder, int position) {
        ProductVariant variant = variants.get(position);
        holder.bind(variant);
    }

    @Override
    public int getItemCount() {
        return variants.size();
    }

    class VariantViewHolder extends RecyclerView.ViewHolder {
        private RadioButton radioButton;
        private TextView tvVariantName;
        private TextView tvVariantPrice;
        private TextView tvStock;

        public VariantViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioButton);
            tvVariantName = itemView.findViewById(R.id.tv_variant_name);
            tvVariantPrice = itemView.findViewById(R.id.tv_variant_price);
            tvStock = itemView.findViewById(R.id.tv_stock);
        }

        public void bind(ProductVariant variant) {
            // Set variant name
            tvVariantName.setText(variant.getName());

            // Set variant price
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvVariantPrice.setText(formatter.format(variant.getPrice()));

            // Set stock info - Available Stock = Stock - Hold Stock
            int availableStock = variant.getStockQuantity() - variant.getHoldQuantity();
            if (availableStock > 0) {
                tvStock.setText("Còn hàng: " + availableStock);
                tvStock.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            } else {
                tvStock.setText("Hết hàng");
                tvStock.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            }

            // Set radio button checked state
            radioButton.setChecked(selectedVariant != null && selectedVariant.getId() == variant.getId());

            // Handle click on entire item
            itemView.setOnClickListener(v -> {
                selectedVariant = variant;
                notifyDataSetChanged();
                if (listener != null) {
                    listener.onVariantSelected(variant);
                }
            });

            // Handle radio button click
            radioButton.setOnClickListener(v -> {
                selectedVariant = variant;
                notifyDataSetChanged();
                if (listener != null) {
                    listener.onVariantSelected(variant);
                }
            });
        }
    }
}
