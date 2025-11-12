// java
package com.example.prm392_mobile_carlinker.ui.managerproduct;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.product.Product;
import com.example.prm392_mobile_carlinker.ui.adapter.ProductManagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ManagerProductListActivity extends AppCompatActivity implements ProductManagerAdapter.Listener {

    private EditText etFilterName, etFilterCategory;
    private ImageButton btnSearch;
    private RecyclerView rvProducts;
    private TextView tvTotal;
    private FloatingActionButton fabAdd;
    private ProductManagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_product_list);

        etFilterName = findViewById(R.id.et_filter_name);
        etFilterCategory = findViewById(R.id.et_filter_category);
        btnSearch = findViewById(R.id.btn_search);
        rvProducts = findViewById(R.id.rv_products);
        tvTotal = findViewById(R.id.tv_total_products);
        fabAdd = findViewById(R.id.fab_add_product);

        adapter = new ProductManagerAdapter();
        adapter.setListener(this);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        rvProducts.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            // TODO: open Add Product screen / dialog
        });

        btnSearch.setOnClickListener(v -> doSearch());

        loadProducts();
    }

    private void loadProducts() {
        // TODO: replace with real network/ViewModel call
        List<Product> dummy = new ArrayList<>(); // placeholder list
        adapter.submitList(dummy);
        tvTotal.setText("Total: " + dummy.size());
    }

    private void doSearch() {
        String name = etFilterName.getText().toString().trim();
        String category = etFilterCategory.getText().toString().trim();
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(category)) {
            loadProducts();
            return;
        }
        // TODO: call search API with name/category and update adapter
        // For now, just clear list:
        adapter.submitList(new ArrayList<>());
        tvTotal.setText("Total: 0");
    }

    // Adapter callbacks
    @Override
    public void onAddVariant(Product p) {
        // TODO: open Add Variant dialog linked to product p
    }

    @Override
    public void onUpdate(Product p) {
        // TODO: open Edit Product UI
    }

    @Override
    public void onDelete(Product p) {
        // TODO: call delete API, then reload
    }
}