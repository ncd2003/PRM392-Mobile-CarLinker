package com.example.prm392_mobile_carlinker.ui.managerproduct;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.example.prm392_mobile_carlinker.R;
import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends Fragment {
    // Khai báo Views
    private EditText etName, etDescription, etWarrantyPeriod;
    private Spinner spinnerCategory, spinnerManufacturer, spinnerBrand;
    private SwitchMaterial switchIsActive, switchIsFeatured;
    private Button btnSelectImages, btnAddProduct;
    private TextView tvImageCount;
    // private RecyclerView rvImagePreview; // Để hiển thị ảnh đã chọn

    private static final int PICK_IMAGES_REQUEST = 1;
    private List<Uri> selectedImageUris = new ArrayList<>(); // Danh sách Uri của ảnh

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_product_form, container, false);

        // 1. Ánh xạ View
        initViews(view);

        // 2. Thiết lập Spinner (Tạm thời dùng dữ liệu giả)
        setupSpinners();

        // 3. Thiết lập Listener
        btnSelectImages.setOnClickListener(v -> openImageChooser());
        btnAddProduct.setOnClickListener(v -> validateAndAddProduct());

        return view;
    }

    private void initViews(View view) {
        etName = view.findViewById(R.id.et_name);
        etDescription = view.findViewById(R.id.et_description);
        etWarrantyPeriod = view.findViewById(R.id.et_warranty_period);
        spinnerCategory = view.findViewById(R.id.spinner_category);
        spinnerManufacturer = view.findViewById(R.id.spinner_manufacturer);
        spinnerBrand = view.findViewById(R.id.spinner_brand);
        switchIsActive = view.findViewById(R.id.switch_is_active);
        switchIsFeatured = view.findViewById(R.id.switch_is_featured);
        btnSelectImages = view.findViewById(R.id.btn_select_images);
        btnAddProduct = view.findViewById(R.id.btn_add_product);
        tvImageCount = view.findViewById(R.id.tv_image_count);
        // rvImagePreview = view.findViewById(R.id.rv_image_preview);

        // Thiết lập giá trị mặc định cho Switch (theo DTO)
        switchIsActive.setChecked(true); // default = true
        switchIsFeatured.setChecked(false); // default = false
        etWarrantyPeriod.setText("0"); // default = 0
    }

    private void setupSpinners() {
        // Dữ liệu giả định
        String[] categories = {"Chọn danh mục...", "Phụ tùng", "Dầu nhớt", "Lốp xe"};
        String[] manufacturers = {"Chọn nhà SX...", "Bosch", "Michelin", "Honda"};
        String[] brands = {"Chọn thương hiệu...", "Denso", "Castrol", "Yamaha"};

        // Tạo Adapter
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Gán Adapter
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerManufacturer.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, manufacturers));
        spinnerBrand.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, brands));
        // ... (Bạn sẽ cần fetch dữ liệu thực tế từ API và gán ID tương ứng)
    }

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

        if (requestCode == PICK_IMAGES_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUris.clear(); // Xóa danh sách cũ

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
            // Cần cập nhật RecyclerView (rvImagePreview) ở đây để hiển thị ảnh preview
        }
    }

    private void validateAndAddProduct() {
        // Lấy dữ liệu
        String name = etName.getText().toString().trim();
        // Lấy ID từ Spinner (Cần logic phức tạp hơn, ở đây chỉ lấy vị trí/giá trị)
        int categoryIndex = spinnerCategory.getSelectedItemPosition();
        int manufacturerIndex = spinnerManufacturer.getSelectedItemPosition();
        int brandIndex = spinnerBrand.getSelectedItemPosition();

        // 2. Validate (Giả lập logic ModelState.IsValid)
        if (name.isEmpty() || categoryIndex == 0 || manufacturerIndex == 0 || brandIndex == 0) {
            Toast.makeText(getContext(), "Vui lòng điền đủ thông tin bắt buộc (*)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy các dữ liệu còn lại
        String description = etDescription.getText().toString().trim();
        int warrantyPeriod;
        try {
            warrantyPeriod = Integer.parseInt(etWarrantyPeriod.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Thời gian bảo hành không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isActive = switchIsActive.isChecked();
        boolean isFeatured = switchIsFeatured.isChecked();

        // 3. Chuẩn bị dữ liệu gửi (Map sang DTO và chuyển Uri sang File/MultipartBody)
        // **Quan trọng:** Bạn cần chuyển các `Uri` trong `selectedImageUris` sang các `MultipartBody.Part`
        // để gửi lên server bằng thư viện như Retrofit, tương ứng với tham số `List<IFormFile> imageFiles`
        // và các trường DTO sẽ đi kèm trong một `Multipart` request.

        // TODO: Gọi ViewModel/API Service để gửi request
        Toast.makeText(getContext(), "Đang thêm sản phẩm: " + name, Toast.LENGTH_LONG).show();
    }

}
