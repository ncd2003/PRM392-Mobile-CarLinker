package com.example.prm392_mobile_carlinker.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.product.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductManagerAdapter extends RecyclerView.Adapter<ProductManagerAdapter.VH> {

    public interface Listener {
        void onAddVariant(Product p);
        void onUpdate(Product p);
        void onDelete(Product p);
    }

    private final List<Product> items = new ArrayList<>();
    private Listener listener;

    public void setListener(Listener l) { this.listener = l; }

    public void submitList(List<Product> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manager_product, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Product p = items.get(position);
        holder.tvName.setText(p.getName() != null ? p.getName() : "");

        // Safely obtain product description if the model provides a getter at runtime.
        // This avoids compile-time errors when `getDescription()` is absent.
        try {
            java.lang.reflect.Method m = p.getClass().getMethod("getDescription");
            Object desc = m.invoke(p);
            holder.tvDesc.setText(desc != null ? desc.toString() : "");
        } catch (Exception e) {
            // Getter not present or invocation failed: show empty description
            holder.tvDesc.setText("");
        }

        // TODO: load image into holder.iv using your image loader (Glide/Picasso) and product image URL

        holder.btnAddVariant.setOnClickListener(v -> { if (listener != null) listener.onAddVariant(p); });
        holder.btnUpdate.setOnClickListener(v -> { if (listener != null) listener.onUpdate(p); });
        holder.btnDelete.setOnClickListener(v -> { if (listener != null) listener.onDelete(p); });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tvName, tvDesc;
        Button btnAddVariant, btnUpdate, btnDelete;
        VH(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv_product);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvDesc = itemView.findViewById(R.id.tv_product_desc);
            btnAddVariant = itemView.findViewById(R.id.btn_add_variant);
//            btnUpdate = itemView.findViewById(R.id.btn_update);
//            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}