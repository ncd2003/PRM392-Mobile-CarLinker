package com.example.prm392_mobile_carlinker.ui.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.product.ProductDetail;
import com.example.prm392_mobile_carlinker.data.model.product.ProductVariant;
import com.example.prm392_mobile_carlinker.ui.adapter.VariantAdapter;
import com.example.prm392_mobile_carlinker.ui.cart.CartActivity;
import com.example.prm392_mobile_carlinker.ui.cart.CartViewModel;
import com.example.prm392_mobile_carlinker.ui.order.MyOrdersActivity;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity implements VariantAdapter.OnVariantSelectedListener {

    public static final String EXTRA_PRODUCT_ID = "PRODUCT_ID";

    private ProductViewModel viewModel;
    private CartViewModel cartViewModel;
    private ProgressBar progressBar;
    private View contentLayout;

    // UI Components
    private ImageView productImage;
    private TextView productName;
    private TextView productPrice;
    private TextView productDescription;
    private TextView categoryName;
    private TextView brandName;
    private TextView manufacturerName;
    private TextView warrantyPeriod;
    private RecyclerView variantsRecyclerView;
    private Button addToCartButton;

    private VariantAdapter variantAdapter;
    private ProductDetail currentProduct;
    private ProductVariant selectedVariant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Get product ID from intent
        int productId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, -1);
        if (productId == -1) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        initViews();

        // Setup ViewModel
        setupViewModel();

        // Load product detail
        loadProductDetail(productId);
    }

    private void initViews() {
        // Setup toolbar FIRST
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chi tiết sản phẩm");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBar = findViewById(R.id.progressBar);
        contentLayout = findViewById(R.id.contentLayout);

        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        categoryName = findViewById(R.id.categoryName);
        brandName = findViewById(R.id.brandName);
        manufacturerName = findViewById(R.id.manufacturerName);
        warrantyPeriod = findViewById(R.id.warrantyPeriod);
        variantsRecyclerView = findViewById(R.id.variantsRecyclerView);
        addToCartButton = findViewById(R.id.addToCartButton);

        // Setup RecyclerView for variants
        variantAdapter = new VariantAdapter(this, this);
        variantsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        variantsRecyclerView.setAdapter(variantAdapter);

        // Setup add to cart button
        addToCartButton.setOnClickListener(v -> onAddToCartClicked());
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
    }

    private void loadProductDetail(int productId) {
        viewModel.getProductDetail(productId).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        progressBar.setVisibility(View.VISIBLE);
                        contentLayout.setVisibility(View.GONE);
                        break;

                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        contentLayout.setVisibility(View.VISIBLE);
                        if (resource.getData() != null) {
                            currentProduct = resource.getData();
                            displayProductDetail(currentProduct);
                        }
                        break;

                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        contentLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(this, resource.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void displayProductDetail(ProductDetail product) {
        // Product name
        productName.setText(product.getName());

        // Product price
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        productPrice.setText(formatter.format(product.getPrice()));

        // Description
        if (product.getDescription() != null && !product.getDescription().isEmpty()) {
            productDescription.setText(product.getDescription());
            productDescription.setVisibility(View.VISIBLE);
        } else {
            productDescription.setVisibility(View.GONE);
        }

        // Category, Brand, Manufacturer
        categoryName.setText("Danh mục: " + product.getCategoryName());
        brandName.setText("Thương hiệu: " + product.getBrandName());
        manufacturerName.setText("Nhà sản xuất: " + product.getManufacturerName());

        // Warranty
        warrantyPeriod.setText("Bảo hành: " + product.getWarrantyPeriod() + " tháng");

        // Load product image
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            Glide.with(this)
                    .load(product.getImage())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(productImage);
        }

        // Display variants
        if (product.getProductVariants() != null && !product.getProductVariants().isEmpty()) {
            variantAdapter.setVariants(product.getProductVariants());

            // Auto-select default variant
            for (ProductVariant variant : product.getProductVariants()) {
                if (variant.isDefault()) {
                    selectedVariant = variant;
                    variantAdapter.setSelectedVariant(variant);
                    updatePriceWithVariant(variant);
                    break;
                }
            }
        }
    }

    @Override
    public void onVariantSelected(ProductVariant variant) {
        selectedVariant = variant;
        updatePriceWithVariant(variant);
    }

    private void updatePriceWithVariant(ProductVariant variant) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        productPrice.setText(formatter.format(variant.getPrice()));
    }

    private void onAddToCartClicked() {
        if (selectedVariant == null) {
            Toast.makeText(this, "Vui lòng chọn phiên bản sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedVariant.getStockQuantity() <= 0) {
            Toast.makeText(this, "Sản phẩm này hiện đã hết hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add to cart with quantity = 1
        addToCartButton.setEnabled(false);
        cartViewModel.addToCart(selectedVariant.getId(), 1)
                .observe(this, resource -> {
                    if (resource != null) {
                        switch (resource.getStatus()) {
                            case LOADING:
                                progressBar.setVisibility(View.VISIBLE);
                                break;

                            case SUCCESS:
                                progressBar.setVisibility(View.GONE);
                                addToCartButton.setEnabled(true);
                                if (resource.getData() != null) {
                                    String message = resource.getData().getMessage();
                                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                                }
                                break;

                            case ERROR:
                                progressBar.setVisibility(View.GONE);
                                addToCartButton.setEnabled(true);
                                Toast.makeText(this, "Lỗi: " + resource.getMessage(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_detail, menu);
        Log.d("ProductDetailActivity", "onCreateOptionsMenu called, menu size: " + menu.size());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("ProductDetailActivity", "Menu item clicked: " + id);

        // Handle back button
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        // Handle action bar item clicks here
        if (id == R.id.action_cart) {
            Log.d("ProductDetailActivity", "Cart icon clicked");
            // Open cart activity
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_my_orders) {
            Log.d("ProductDetailActivity", "My orders icon clicked");
            // Open my orders activity
            Intent intent = new Intent(this, MyOrdersActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
