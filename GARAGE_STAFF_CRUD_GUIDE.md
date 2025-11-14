# GARAGE STAFF CRUD - Hướng dẫn sử dụng

## 📋 Tổng quan

Module CRUD quản lý nhân viên Garage cho phép Role GARAGE tạo, xem, cập nhật và xóa nhân viên của garage mình.

## 🏗️ Cấu trúc File

### 1. **Models (Data Layer)**
```
data/model/garagestaff/
├── GarageStaffDto.java                  // Model nhân viên
├── GarageStaffCreateRequest.java        // Request tạo mới
├── GarageStaffUpdateRequest.java        // Request cập nhật
├── GarageStaffListResponse.java         // Response danh sách
└── GarageStaffDetailResponse.java       // Response chi tiết
```

### 2. **Repository**
```
data/repository/
└── GarageStaffRepository.java           // Xử lý API calls
```

### 3. **UI Layer**
```
ui/garagestaff/
├── GarageStaffListActivity.java         // Màn hình danh sách
├── GarageStaffDetailActivity.java       // Màn hình chi tiết
├── GarageStaffFormActivity.java         // Màn hình thêm/sửa
├── GarageStaffAdapter.java              // RecyclerView Adapter
└── GarageStaffViewModel.java            // ViewModel
```

### 4. **Layouts**
```
res/layout/
├── activity_garage_staff_list.xml       // Layout danh sách
├── activity_garage_staff_detail.xml     // Layout chi tiết
├── activity_garage_staff_form.xml       // Layout form
└── item_garage_staff.xml                // Layout item RecyclerView
```

## 🔧 Mapping Enum GarageRole

```java
// Backend RoleGarage enum mapping
0 = DEALER     // Đại lý/Nhà cung cấp
1 = WAREHOUSE  // Kho hàng
2 = STAFF      // Nhân viên
```

**Lưu ý:** Mapping này đã được cập nhật để khớp chính xác với backend C#.

## 📡 API Endpoints

### 1. **GET - Lấy danh sách nhân viên**
```
GET /api/GarageStaff?page=1&size=30&sortBy=null&isAsc=true
Authorization: Bearer {token}
Role Required: GARAGE
```

**Response:**
```json
{
  "status": 200,
  "message": "Lấy danh sách nhân viên thành công",
  "data": {
    "size": 30,
    "page": 1,
    "total": 1,
    "totalPages": 1,
    "items": [...]
  }
}
```

### 2. **GET - Lấy chi tiết nhân viên**
```
GET /api/GarageStaff/{id}
Authorization: Bearer {token}
Role Required: GARAGE
```

### 3. **POST - Tạo nhân viên mới**
```
POST /api/GarageStaff
Authorization: Bearer {token}
Role Required: GARAGE
Content-Type: application/json

{
  "fullName": "Nguyễn Văn A",
  "email": "user@example.com",
  "phoneNumber": "0901234567",
  "password": "password123",
  "garageRole": 0
}
```

### 4. **PATCH - Cập nhật thông tin**
```
PATCH /api/GarageStaff/{id}
Authorization: Bearer {token}
Role Required: GARAGE
Content-Type: application/json

{
  "fullName": "Nguyễn Văn A Updated",
  "phoneNumber": "0901234567",
  "garageRole": 1,
  "userStatus": 0
}
```

### 5. **PATCH - Cập nhật ảnh**
```
PATCH /api/GarageStaff/image/{id}
Authorization: Bearer {token}
Role Required: GARAGE
Content-Type: multipart/form-data

imageFile: [file]
```

### 6. **DELETE - Xóa nhân viên**
```
DELETE /api/GarageStaff/{id}
Authorization: Bearer {token}
Role Required: GARAGE
```

## 💻 Cách sử dụng trong Code

### 1. Mở màn hình danh sách nhân viên
```java
Intent intent = new Intent(context, GarageStaffListActivity.class);
startActivity(intent);
```

### 2. Thêm vào AndroidManifest.xml
```xml
<!-- Garage Staff List Activity -->
<activity
    android:name=".ui.garagestaff.GarageStaffListActivity"
    android:exported="false"
    android:label="Quản lý nhân viên"
    android:parentActivityName=".ui.home.HomeActivity" />

<!-- Garage Staff Detail Activity -->
<activity
    android:name=".ui.garagestaff.GarageStaffDetailActivity"
    android:exported="false"
    android:label="Chi tiết nhân viên"
    android:parentActivityName=".ui.garagestaff.GarageStaffListActivity" />

<!-- Garage Staff Form Activity -->
<activity
    android:name=".ui.garagestaff.GarageStaffFormActivity"
    android:exported="false"
    android:label="Thêm/Sửa nhân viên"
    android:parentActivityName=".ui.garagestaff.GarageStaffListActivity" />
```

### 3. Thêm vào API Service
```java
// ApiService.java
@GET("api/GarageStaff")
Call<GarageStaffListResponse> getAllGarageStaff(
    @Query("page") int page,
    @Query("size") int size,
    @Query("sortBy") String sortBy,
    @Query("isAsc") boolean isAsc
);

@GET("api/GarageStaff/{id}")
Call<GarageStaffDetailResponse> getGarageStaffById(@Path("id") int id);

@POST("api/GarageStaff")
Call<GarageStaffDetailResponse> createGarageStaff(@Body GarageStaffCreateRequest request);

@PATCH("api/GarageStaff/{id}")
Call<GarageStaffDetailResponse> updateGarageStaff(
    @Path("id") int id,
    @Body GarageStaffUpdateRequest request
);

@Multipart
@PATCH("api/GarageStaff/image/{id}")
Call<GarageStaffDetailResponse> updateGarageStaffImage(
    @Path("id") int id,
    @Part MultipartBody.Part imageFile
);

@DELETE("api/GarageStaff/{id}")
Call<BaseResponse> deleteGarageStaff(@Path("id") int id);
```

## 🎯 Tính năng chính

### ✅ CREATE (Tạo mới)
- Form nhập đầy đủ: Họ tên, Email, Mật khẩu, SĐT
- Chọn vai trò từ Spinner (Kỹ thuật viên, Quản lý, Lễ tân)
- Upload ảnh nhân viên
- Validation đầy đủ các trường
- Mật khẩu tối thiểu 6 ký tự

### ✅ READ (Xem)
- Danh sách nhân viên với RecyclerView
- Hiển thị: Avatar, Tên, Email, SĐT, Vai trò, Trạng thái, Ngày tạo
- Xem chi tiết đầy đủ thông tin nhân viên
- Load ảnh với Glide
- Màu sắc trạng thái: Xanh (Hoạt động), Xám (Không hoạt động), Đỏ (Bị khóa)

### ✅ UPDATE (Cập nhật)
- Cập nhật thông tin: Họ tên, SĐT, Vai trò, Trạng thái
- Upload/thay đổi ảnh
- Email không thể thay đổi (readonly)
- Mật khẩu ẩn trong edit mode

### ✅ DELETE (Xóa)
- Dialog xác nhận trước khi xóa
- Hiển thị tên nhân viên trong dialog
- Auto reload danh sách sau khi xóa

## 🔒 Phân quyền

- **Role Required:** GARAGE (Chủ garage)
- **Authorization:** Bearer Token
- **Scope:** Chỉ quản lý được nhân viên của garage mình

## 🐛 Xử lý lỗi

Tất cả các lỗi đều được xử lý qua Resource pattern:
```java
switch (resource.getStatus()) {
    case LOADING:
        // Show progress bar
        break;
    case SUCCESS:
        // Handle success
        break;
    case ERROR:
        // Show error message
        break;
}
```

## 📱 UI/UX

### Danh sách
- RecyclerView với LinearLayoutManager
- FAB (+) để thêm nhân viên mới
- Mỗi item có nút Edit và Delete
- Pull to refresh khi quay lại màn hình

### Form
- Material Design TextInputLayout
- Spinner cho Role và Status
- Image picker với preview
- Button Lưu với loading state

### Chi tiết
- Card view thông tin
- Avatar lớn ở trên
- Nút Edit và Delete ở dưới

## 🚀 Build & Run

1. **Sync Gradle**: Đảm bảo tất cả dependencies đã được tải
2. **Build Project**: Build > Make Project hoặc Ctrl+F9
3. **Clean & Rebuild**: Build > Clean Project, sau đó Build > Rebuild Project
4. **Run App**: Shift+F10

## ⚠️ Lưu ý

1. **Backend API**: Đảm bảo backend API đang chạy và có thể truy cập được
2. **Token**: Phải login với Role GARAGE trước khi sử dụng
3. **Enum Mapping**: Kiểm tra enum RoleGarage trong backend khớp với mapping trong Android
4. **Image Upload**: Cần permission READ_EXTERNAL_STORAGE trong AndroidManifest

## 📞 Troubleshooting

### Lỗi "Cannot resolve symbol R"
- **Solution**: Build > Clean Project, sau đó Rebuild Project

### Lỗi "Cannot resolve symbol GarageStaffViewModel"
- **Solution**: Kiểm tra package name và import statements

### API trả về 401 Unauthorized
- **Solution**: Kiểm tra token có hợp lệ không, login lại nếu cần

### API trả về 403 Forbidden
- **Solution**: Đảm bảo user có Role GARAGE

### Image upload fail
- **Solution**: Kiểm tra permission và file size (backend có thể giới hạn)

## 📝 TODO / Future Enhancement

- [ ] Thêm search/filter nhân viên
- [ ] Thêm sorting options
- [ ] Pagination với infinite scroll
- [ ] Export danh sách nhân viên
- [ ] Thống kê nhân viên theo vai trò
- [ ] Lịch sử hoạt động của nhân viên

---

**Created:** 2025-11-14  
**Version:** 1.0.0  
**Author:** AI Assistant
