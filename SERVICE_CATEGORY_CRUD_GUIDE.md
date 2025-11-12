# Hướng dẫn sử dụng Service Category CRUD

## Tổng quan

Đã tạo xong đầy đủ chức năng CRUD (Create, Read, Update, Delete) cho **Service Category** (Gói Dịch Vụ) dành cho ADMIN quản lý.

## Các file đã tạo

### 1. Model Classes (data/model/servicecategory/)
- **ServiceItem.java** - Model cho từng dịch vụ trong gói
- **ServiceCategory.java** - Model cho gói dịch vụ
- **ServiceCategoryResponse.java** - Response cho danh sách gói dịch vụ
- **ServiceCategoryDetailResponse.java** - Response cho chi tiết 1 gói dịch vụ
- **ServiceCategoryCreateRequest.java** - Request để tạo gói dịch vụ mới
- **ServiceCategoryUpdateRequest.java** - Request để cập nhật gói dịch vụ

### 2. API Service
- **ApiService.java** - Đã thêm 5 endpoints:
  - `GET /api/ServiceCategory` - Lấy danh sách tất cả gói dịch vụ
  - `GET /api/ServiceCategory/{id}` - Lấy chi tiết 1 gói dịch vụ
  - `POST /api/ServiceCategory` - Tạo gói dịch vụ mới
  - `PATCH /api/ServiceCategory/{id}` - Cập nhật gói dịch vụ
  - `DELETE /api/ServiceCategory/{id}` - Xóa gói dịch vụ

### 3. Repository
- **ServiceCategoryRepository.java** - Xử lý logic gọi API và trả về LiveData

### 4. ViewModel
- **ServiceCategoryViewModel.java** - ViewModel cho Service Category

### 5. Adapters
- **ServiceCategoryAdapter.java** - Adapter hiển thị danh sách gói dịch vụ
- **ServiceItemAdapter.java** - Adapter hiển thị danh sách dịch vụ trong gói

### 6. Activities
- **ServiceCategoryListActivity.java** - Màn hình danh sách gói dịch vụ (READ, DELETE)
- **ServiceCategoryDetailActivity.java** - Màn hình chi tiết gói dịch vụ (READ)
- **ServiceCategoryFormActivity.java** - Màn hình tạo/sửa gói dịch vụ (CREATE, UPDATE)

### 7. Layout Files
- **activity_service_category_list.xml** - Layout cho danh sách
- **activity_service_category_detail.xml** - Layout cho chi tiết
- **activity_service_category_form.xml** - Layout cho form tạo/sửa
- **item_service_category.xml** - Layout cho item trong RecyclerView
- **item_service_item.xml** - Layout cho dịch vụ trong gói

### 8. AndroidManifest.xml
- Đã thêm 3 Activities mới

## Cách sử dụng

### 1. Mở màn hình quản lý Service Category

Từ bất kỳ Activity nào (ví dụ: HomeActivity), thêm button hoặc menu item:

```java
// Trong HomeActivity hoặc Activity khác
Button btnManageServiceCategories = findViewById(R.id.btnManageServiceCategories);
btnManageServiceCategories.setOnClickListener(v -> {
    Intent intent = new Intent(this, ServiceCategoryListActivity.class);
    startActivity(intent);
});
```

### 2. Chức năng có sẵn

#### **ServiceCategoryListActivity** (Danh sách)
- ✅ Hiển thị tất cả gói dịch vụ
- ✅ Mỗi item hiển thị: Tên gói, số lượng dịch vụ, preview 3 dịch vụ đầu
- ✅ 3 nút cho mỗi item:
  - **Xem**: Xem chi tiết gói dịch vụ
  - **Sửa**: Chỉnh sửa tên gói dịch vụ
  - **Xóa**: Xóa gói dịch vụ (có dialog xác nhận)
- ✅ Nút FAB (+) ở góc dưới phải: Tạo gói dịch vụ mới

#### **ServiceCategoryDetailActivity** (Chi tiết)
- ✅ Hiển thị tên gói dịch vụ
- ✅ Hiển thị danh sách đầy đủ các dịch vụ trong gói
- ✅ Mỗi dịch vụ hiển thị: Tên và Giá (format VNĐ)

#### **ServiceCategoryFormActivity** (Tạo/Sửa)
- ✅ Nhập tên gói dịch vụ
- ✅ Nút "Lưu" để tạo mới hoặc cập nhật
- ✅ Hỗ trợ cả chế độ Create và Update
- ✅ Validation: Kiểm tra tên không được rỗng

### 3. API Authentication

**Lưu ý quan trọng:** Tất cả các API đều yêu cầu role **ADMIN**

Để sử dụng được, bạn cần:
1. Đăng nhập với tài khoản ADMIN
2. Lưu token vào SharedPreferences hoặc Session
3. Thêm token vào header của mỗi request

**Cách thêm token vào Retrofit:**

Tạo AuthInterceptor:
```java
public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        
        // Lấy token từ SharedPreferences
        String token = getTokenFromPreferences();
        
        if (token != null) {
            Request request = original.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();
            return chain.proceed(request);
        }
        
        return chain.proceed(original);
    }
}
```

Thêm vào RetrofitClient:
```java
OkHttpClient okHttpClient = new OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .addInterceptor(new AuthInterceptor()) // Thêm dòng này
    .addInterceptor(loggingInterceptor)
    .build();
```

## API Backend đã có sẵn

Backend của bạn đã implement đầy đủ các API:
- ✅ GET /api/ServiceCategory (với pagination)
- ✅ GET /api/ServiceCategory/{id}
- ✅ POST /api/ServiceCategory
- ✅ PATCH /api/ServiceCategory/{id}
- ✅ DELETE /api/ServiceCategory/{id}

## Ví dụ Response từ Backend

### GET /api/ServiceCategory
```json
{
  "status": 200,
  "message": "Lấy danh sách gói dịch vụ thành công",
  "data": [
    {
      "id": 1,
      "name": "Bảo dưỡng định kỳ",
      "serviceItems": [
        {
          "id": 1,
          "name": "Thay dầu động cơ",
          "price": 300000
        },
        {
          "id": 2,
          "name": "Kiểm tra phanh",
          "price": 150000
        }
      ]
    }
  ]
}
```

## Testing

### Test từ màn hình chính:
1. Thêm button vào HomeActivity để mở ServiceCategoryListActivity
2. Chạy app
3. Đăng nhập với tài khoản ADMIN
4. Click button để xem danh sách gói dịch vụ

### Test các chức năng:
1. **Xem danh sách**: Mở ServiceCategoryListActivity
2. **Tạo mới**: Click nút FAB (+) → Nhập tên → Click "Lưu"
3. **Xem chi tiết**: Click nút "Xem" trên item
4. **Sửa**: Click nút "Sửa" → Thay đổi tên → Click "Lưu"
5. **Xóa**: Click nút "Xóa" → Xác nhận trong dialog

## Lưu ý quan trọng

1. **Backend phải chạy** trên http://10.0.2.2:5291 (emulator) hoặc IP máy thật
2. **Phải đăng nhập ADMIN** mới có quyền truy cập
3. **Token authentication** cần được implement để gọi API thành công
4. Hiện tại form chỉ cho phép thêm/sửa **tên gói**, chưa có chức năng chọn ServiceItems (có thể thêm sau)

## Next Steps (Tùy chọn)

Nếu muốn hoàn thiện hơn, có thể thêm:
1. **Multi-select ServiceItems** khi tạo/sửa gói dịch vụ
2. **Search/Filter** trong danh sách gói dịch vụ
3. **Pagination** cho danh sách dài
4. **Pull to refresh** để reload danh sách
5. **Offline caching** với Room database

## Kết luận

✅ Đã hoàn thành đầy đủ CRUD cho Service Category
✅ Tất cả files không có lỗi compile
✅ Sẵn sàng để test và sử dụng

