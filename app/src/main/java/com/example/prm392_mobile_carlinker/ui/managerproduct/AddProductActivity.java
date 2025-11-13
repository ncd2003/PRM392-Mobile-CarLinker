package com.example.prm392_mobile_carlinker.ui.managerproduct;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_mobile_carlinker.data.model.auth.LoginResponse;
import com.example.prm392_mobile_carlinker.data.model.brand.BrandResponse;
import com.example.prm392_mobile_carlinker.data.model.category.CategoryResponse;
import com.example.prm392_mobile_carlinker.data.remote.ApiService;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;
import com.example.prm392_mobile_carlinker.ui.auth.LoginActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.example.prm392_mobile_carlinker.R;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    // Khai báo Views
    private EditText etName, etDescription, etWarrantyPeriod;
    private Spinner spinnerCategory, spinnerBrand;
    private Button btnSelectImages, btnAddProduct;
    private TextView tvImageCount;
    // private RecyclerView rvImagePreview;

    private ApiService apiService;

    private static final int PICK_IMAGES_REQUEST = 1;
    private List<Uri> selectedImageUris = new ArrayList<>(); // Danh sách Uri của ảnh

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) { // Sửa từ onCreateView thành onCreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_form); // Thiết lập layout cho Activity
        apiService = RetrofitClient.getApiService();
        // 1. Ánh xạ View
        initViews(); // Bỏ tham số view vì đã dùng setContentView

        // 2. Thiết lập Spinner (Tạm thời dùng dữ liệu giả)
        setupSpinners();

        // 3. Thiết lập Listener
        btnSelectImages.setOnClickListener(v -> openImageChooser());
        btnAddProduct.setOnClickListener(v -> validateAndAddProduct());
    }

    // Phương thức này hiện tại không còn cần tham số View vì đã dùng findViewById trực tiếp
    private void initViews() {
        etName = findViewById(R.id.et_name);
        etDescription = findViewById(R.id.et_description);
        etWarrantyPeriod = findViewById(R.id.et_warranty_period);
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerBrand = findViewById(R.id.spinner_brand);
        btnSelectImages = findViewById(R.id.btn_select_images);
        btnAddProduct = findViewById(R.id.btn_add_product);
        tvImageCount = findViewById(R.id.tv_image_count);
        // rvImagePreview = findViewById(R.id.rv_image_preview);

        // Thiết lập giá trị mặc định cho Switch
        etWarrantyPeriod.setText("0"); // default = 0
    }

    private void setupSpinners() {
        // 1. Category Setup (Giữ nguyên)
        List<String> initialCategories = new ArrayList<>();
        initialCategories.add("Đang tải danh mục...");
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, initialCategories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
        fetchCategories(categoryAdapter); // Gọi API

        // 2. Brand Setup (Thêm logic tải API)
        List<String> initialBrands = new ArrayList<>();
        initialBrands.add("Đang tải thương hiệu...");
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, initialBrands);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBrand.setAdapter(brandAdapter); // Gán Brand Adapter
        fetchBrand(brandAdapter); // Gọi API mới
    }

    private List<CategoryResponse.Data> categoryList = new ArrayList<>(); // Danh sách Category (Khai báo ở Activity level)

    private void fetchCategories(ArrayAdapter<String> adapter) {
        // Gọi API. Giả sử getAllCategory() trả về Call<CategoryResponse>
        apiService.getAllCategory().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                // Kiểm tra HTTP status code (2xx)
                if (response.isSuccessful() && response.body() != null) {
                    CategoryResponse body = response.body();

                    // Kiểm tra status code trong body (ví dụ: 200) và dữ liệu có tồn tại không
                    if (body.getStatus() == 200 && body.getData() != null) {

                        List<CategoryResponse.Data> categoriesFromApi = body.getData();

                        // 1. Lưu danh sách Category vào biến toàn cục của Activity
                        categoryList.clear();
                        categoryList.addAll(categoriesFromApi);

                        // 2. Chuẩn bị danh sách Tên (String) cho Spinner
                        List<String> categoryNames = new ArrayList<>();
                        categoryNames.add("Chọn danh mục..."); // Mục mặc định

                        for (CategoryResponse.Data category : categoryList) {
                            categoryNames.add(category.getName());
                        }

                        // 3. Cập nhật Adapter của Spinner
                        adapter.clear();
                        adapter.addAll(categoryNames);
                        adapter.notifyDataSetChanged();

                        Toast.makeText(AddProductActivity.this, body.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        // Xử lý lỗi từ backend (ví dụ: status code không phải 200)
                        Toast.makeText(AddProductActivity.this, "Lỗi tải danh mục: " + body.getMessage(), Toast.LENGTH_LONG).show();
                        handleCategoryLoadError(adapter, "Lỗi API: " + body.getMessage());
                    }
                } else {
                    // Xử lý lỗi HTTP (ví dụ: 404, 500)
                    Toast.makeText(AddProductActivity.this, "Lỗi kết nối Server: " + response.code(), Toast.LENGTH_LONG).show();
                    handleCategoryLoadError(adapter, "Lỗi HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                // Xử lý lỗi mạng hoặc lỗi parsing
                Toast.makeText(AddProductActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
                handleCategoryLoadError(adapter, "Lỗi mạng hoặc kết nối");
            }

        });
    }

    private List<BrandResponse.Data> brandList = new ArrayList<>();

    private void handleBrandLoadError(ArrayAdapter<String> adapter, String message) {
        adapter.clear();
        adapter.add("Không thể tải thương hiệu (" + message + ")");
        adapter.notifyDataSetChanged();
    }

    private void fetchBrand(ArrayAdapter<String> adapter) {
        apiService.getAllBrand().enqueue(new Callback<BrandResponse>() {
            @Override
            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BrandResponse body = response.body();
                    if (body.getStatus() == 200 && body.getData() != null) {

                        List<BrandResponse.Data> brandFromApi = body.getData();

                        // 1. Lưu danh sách Brand
                        brandList.clear();
                        brandList.addAll(brandFromApi);

                        // 2. Chuẩn bị danh sách Tên (String) cho Spinner
                        List<String> brandNames = new ArrayList<>();
                        brandNames.add("Chọn thương hiệu..."); // Mục mặc định

                        for (BrandResponse.Data brand : brandList) {
                            // Lấy tên từ đối tượng Brand
                            brandNames.add(brand.getName());
                        }

                        // 3. Cập nhật Adapter của Spinner
                        adapter.clear();
                        adapter.addAll(brandNames);
                        adapter.notifyDataSetChanged();

                        Toast.makeText(AddProductActivity.this, body.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(AddProductActivity.this, "Lỗi tải thương hiệu: " + body.getMessage(), Toast.LENGTH_LONG).show();
                        handleBrandLoadError(adapter, "Lỗi API: " + body.getMessage());
                    }
                } else {
                    Toast.makeText(AddProductActivity.this, "Lỗi kết nối Server: " + response.code(), Toast.LENGTH_LONG).show();
                    handleBrandLoadError(adapter, "Lỗi HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<BrandResponse> call, Throwable t) {
                Toast.makeText(AddProductActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
                handleBrandLoadError(adapter, "Lỗi mạng hoặc kết nối");
            }
        });
    }

//    private void validateAndAddProduct() {
//        // 1. Lấy dữ liệu và vị trí đã chọn
//        String name = etName.getText().toString().trim();
//        int categoryIndex = spinnerCategory.getSelectedItemPosition();
//        int manufacturerIndex = spinnerManufacturer.getSelectedItemPosition();
//        int brandIndex = spinnerBrand.getSelectedItemPosition();
//
//        // 2. Validate
//        if (name.isEmpty() || categoryIndex == 0 || manufacturerIndex == 0 || brandIndex == 0) {
//            Toast.makeText(this, "Vui lòng điền đủ thông tin bắt buộc (*)", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // *************************************************************
//        // >>> LẤY CATEGORY ID <<<
//        // *************************************************************
//        String categoryId = null;
//        if (categoryIndex > 0 && categoryIndex - 1 < categoryList.size()) {
//            categoryId = categoryList.get(categoryIndex - 1).getId();
//        }
//
//        // *************************************************************
//        // >>> LẤY BRAND ID <<<
//        // *************************************************************
//        String brandId = null;
//        if (brandIndex > 0 && brandIndex - 1 < brandList.size()) {
//            brandId = brandList.get(brandIndex - 1).getId();
//        }
//
//        // manufacturerId (Nếu Manufacturer cũng là API, bạn cần tạo logic tương tự)
//        // Hiện tại: manufacturerId chỉ là manufacturerIndex nếu dùng dữ liệu cứng.
//        int manufacturerId = manufacturerIndex; // Giả sử ID = index
//
//
//        // Lấy các dữ liệu còn lại
//        String description = etDescription.getText().toString().trim();
//        int warrantyPeriod;
//        try {
//            warrantyPeriod = Integer.parseInt(etWarrantyPeriod.getText().toString());
//        } catch (NumberFormatException e) {
//            Toast.makeText(this, "Thời gian bảo hành không hợp lệ", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        boolean isActive = switchIsActive.isChecked();
//        boolean isFeatured = switchIsFeatured.isChecked();
//
//        // 3. Chuẩn bị dữ liệu gửi
//        String debugMessage = String.format("Thêm SP: %s, Cat ID: %s, Brand ID: %s",
//                name, categoryId, brandId);
//        Toast.makeText(this, debugMessage, Toast.LENGTH_LONG).show();
//
//        // TODO: Tạo DTO/Model Sản phẩm và gửi lên API
//    }

    // Phương thức hỗ trợ xử lý lỗi
    private void handleCategoryLoadError(ArrayAdapter<String> adapter, String message) {
        adapter.clear();
        adapter.add("Không thể tải danh mục (" + message + ")");
        adapter.notifyDataSetChanged();
        // Có thể thêm logic ẩn/hiện view lỗi khác tại đây
    }

    // ... (Phương thức openImageChooser() giữ nguyên)

    // Mở intent chọn nhiều ảnh
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Cho phép chọn nhiều ảnh
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh sản phẩm"), PICK_IMAGES_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // ... (Logic onActivityResult giữ nguyên)

        if (requestCode == PICK_IMAGES_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUris.clear();

            if (data.getClipData() != null) {
                // Đã chọn nhiều ảnh
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    selectedImageUris.add(data.getClipData().getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                // Chỉ chọn 1 ảnh
                selectedImageUris.add(data.getData());
            }

            // Cập nhật giao diện
            tvImageCount.setText("Đã chọn " + selectedImageUris.size() + " ảnh");
        }
    }

    private void validateAndAddProduct() {
        // Lấy dữ liệu
        String name = etName.getText().toString().trim();
        int categoryIndex = spinnerCategory.getSelectedItemPosition();
        int brandIndex = spinnerBrand.getSelectedItemPosition();

        // 2. Validate (Sử dụng 'this' thay cho getContext())
        if (name.isEmpty() || categoryIndex == 0 || brandIndex == 0) {
            Toast.makeText(this, "Vui lòng điền đủ thông tin bắt buộc (*)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy các dữ liệu còn lại
        String description = etDescription.getText().toString().trim();
        int warrantyPeriod;
        try {
            warrantyPeriod = Integer.parseInt(etWarrantyPeriod.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Thời gian bảo hành không hợp lệ", Toast.LENGTH_SHORT).show(); // Sửa getContext() -> this
            return;
        }

        // TODO: Gọi ViewModel/API Service để gửi request
        Toast.makeText(this, "Đang thêm sản phẩm: " + name, Toast.LENGTH_LONG).show(); // Sửa getContext() -> this
    }
}