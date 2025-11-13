# 🎯 HƯỚNG DẪN TEST CRUD SERVICE ITEM CHO ADMIN

## ✅ Đã hoàn thành

### 1. Tất cả các file đã được tạo và sửa lỗi
- ✅ 5 Model classes (DTO, Request, Response)
- ✅ API endpoints trong ApiService.java
- ✅ ServiceItemRepository.java
- ✅ ServiceItemViewModel.java
- ✅ 3 Activities (List, Form, Detail)
- ✅ ServiceItemAdminAdapter.java
- ✅ 4 Layout files XML
- ✅ AndroidManifest.xml đã đăng ký activities
- ✅ Menu profile đã thêm item mới
- ✅ HomeActivity đã xử lý navigation

### 2. Không còn lỗi ERROR
Chỉ còn một số WARNING nhỏ không ảnh hưởng:
- "Method never used" - bình thường vì IDE chưa phát hiện việc sử dụng
- "Field can be local variable" - không quan trọng
- Các imports và annotations - không ảnh hưởng chức năng

---

## 🚀 LUỒNG TEST ĐẦY ĐỦ CHO ADMIN

### Bước 1: Chuẩn bị
1. **Đồng bộ project:**
   - File → Sync Project with Gradle Files
   - Build → Clean Project
   - Build → Rebuild Project

2. **Khởi động backend API:**
   - Đảm bảo backend đang chạy trên cổng đã cấu hình
   - Kiểm tra URL trong RetrofitClient.java

3. **Chuẩn bị tài khoản Admin:**
   - Username: admin
   - Password: admin123
   - Role: ADMIN

### Bước 2: Đăng nhập với tài khoản Admin
1. Mở app
2. Nhấn nút "Đăng nhập" trên HomeActivity
3. Nhập thông tin admin
4. Đăng nhập thành công

### Bước 3: Truy cập quản lý Service Items
1. Trên HomeActivity, nhấn vào biểu tượng **Profile** (góc trên bên phải)
2. Trong menu popup, chọn **"Quản lý Dịch Vụ (Admin)"**
3. Màn hình ServiceItemListActivity sẽ hiển thị

### Bước 4: Xem danh sách Service Items (READ)
✅ **Kiểm tra:**
- Danh sách các service items hiển thị trong RecyclerView
- Mỗi item hiển thị: ID và tên
- Có 3 nút: Xem, Sửa, Xóa
- Nút FAB (+) ở góc dưới bên phải

🔍 **API được gọi:**
```
GET /api/ServiceItem?page=1&size=100&sortBy=null&isAsc=true
```

### Bước 5: Tạo Service Item mới (CREATE)
1. Nhấn nút **FAB (+)** ở góc dưới
2. Màn hình ServiceItemFormActivity mở ra
3. Nhập tên service item (ví dụ: "Thay nhớt máy")
4. Nhấn nút **"Lưu"**

✅ **Kiểm tra:**
- Hiển thị ProgressBar khi đang xử lý
- Toast "Tạo dịch vụ thành công" xuất hiện
- Quay lại danh sách tự động
- Service item mới xuất hiện trong danh sách

🔍 **API được gọi:**
```
POST /api/ServiceItem
Body: { "name": "Thay nhớt máy" }
```

### Bước 6: Xem chi tiết Service Item (READ Detail)
1. Từ danh sách, nhấn nút **"Xem"** của một item
2. Màn hình ServiceItemDetailActivity hiển thị

✅ **Kiểm tra:**
- Hiển thị ID service item
- Hiển thị tên service item
- Layout đẹp với CardView

🔍 **API được gọi:**
```
GET /api/ServiceItem/{id}
```

### Bước 7: Cập nhật Service Item (UPDATE)
1. Từ danh sách, nhấn nút **"Sửa"** của một item
2. Màn hình ServiceItemFormActivity mở với dữ liệu hiện tại
3. Thay đổi tên (ví dụ: "Thay nhớt máy cao cấp")
4. Nhấn nút **"Lưu"**

✅ **Kiểm tra:**
- Tên cũ đã được điền sẵn trong form
- Toast "Cập nhật dịch vụ thành công" xuất hiện
- Quay lại danh sách tự động
- Tên service item đã được cập nhật trong danh sách

🔍 **API được gọi:**
```
PATCH /api/ServiceItem/{id}
Body: { "name": "Thay nhớt máy cao cấp" }
```

### Bước 8: Xóa Service Item (DELETE)
1. Từ danh sách, nhấn nút **"Xóa"** của một item
2. Dialog xác nhận xuất hiện
3. Nhấn **"Xóa"** để confirm

✅ **Kiểm tra:**
- Dialog "Bạn có chắc chắn muốn xóa..." hiển thị
- Toast "Xóa dịch vụ thành công" xuất hiện
- Service item biến mất khỏi danh sách
- Danh sách tự động refresh

🔍 **API được gọi:**
```
DELETE /api/ServiceItem/{id}
```

---

## 📱 NAVIGATION FLOW

```
HomeActivity
    ↓ (Click Profile Icon)
Menu Popup
    ↓ (Click "Quản lý Dịch Vụ (Admin)")
ServiceItemListActivity
    ├─→ (Click FAB +) → ServiceItemFormActivity (Create)
    ├─→ (Click "Xem") → ServiceItemDetailActivity
    ├─→ (Click "Sửa") → ServiceItemFormActivity (Update)
    └─→ (Click "Xóa") → Confirm Dialog → Delete
```

---

## 🔐 AUTHENTICATION & AUTHORIZATION

### Token Authentication
- Tất cả API calls tự động gửi Bearer token
- Token được lưu trong SessionManager
- Nếu token hết hạn, user sẽ cần đăng nhập lại

### Role-based Access
Theo backend controller, tất cả endpoints yêu cầu role **ADMIN**:
- GET /api/ServiceItem - ✅ ADMIN only
- GET /api/ServiceItem/{id} - ✅ ADMIN, GARAGE
- POST /api/ServiceItem - ✅ ADMIN only
- PATCH /api/ServiceItem/{id} - ✅ ADMIN only
- DELETE /api/ServiceItem/{id} - ✅ ADMIN only

---

## 🧪 TEST CASES

### Test Case 1: Tạo Service Item thành công
**Input:** name = "Thay lốp xe"
**Expected:** Toast success, item xuất hiện trong danh sách

### Test Case 2: Tạo Service Item với tên trống
**Input:** name = ""
**Expected:** Toast "Vui lòng nhập tên dịch vụ"

### Test Case 3: Cập nhật Service Item thành công
**Input:** Sửa name từ "A" → "B"
**Expected:** Toast success, tên cập nhật trong danh sách

### Test Case 4: Xóa Service Item thành công
**Input:** Confirm xóa
**Expected:** Toast success, item biến mất

### Test Case 5: Xóa Service Item bị hủy
**Input:** Nhấn "Hủy" trong dialog
**Expected:** Dialog đóng, item vẫn còn

### Test Case 6: Load danh sách khi không có items
**Expected:** Text "Chưa có dịch vụ nào" hiển thị

### Test Case 7: API error handling
**Input:** Backend không hoạt động
**Expected:** Text lỗi hiển thị với message từ API

---

## 🐛 TROUBLESHOOTING

### Lỗi: Cannot resolve symbol 'ServiceItemUpdateRequest'
**Giải pháp:**
1. File → Invalidate Caches → Invalidate and Restart
2. Build → Clean Project
3. Build → Rebuild Project

### Lỗi: Network error
**Kiểm tra:**
1. Backend có đang chạy không?
2. URL trong RetrofitClient.java đúng chưa?
3. AndroidManifest.xml có permission INTERNET chưa?
4. network_security_config.xml cho phép HTTP chưa?

### Lỗi: Unauthorized (401)
**Giải pháp:**
1. Đăng nhập lại
2. Kiểm tra token trong SessionManager
3. Kiểm tra role của user (phải là ADMIN)

### Lỗi: App crash khi mở ServiceItemListActivity
**Kiểm tra:**
1. Activity đã đăng ký trong AndroidManifest.xml chưa?
2. Layout file có tồn tại không?
3. Check logcat để xem error message chi tiết

---

## 📊 RESPONSE FORMAT TỪ BACKEND

### Success Response (200, 201)
```json
{
  "status": 200,
  "message": "Lấy danh sách dịch vụ thành công",
  "data": [
    {
      "id": 1,
      "name": "Thay dầu máy"
    }
  ]
}
```

### Error Response (400, 404, 500)
```json
{
  "status": 400,
  "message": "Lỗi khi tạo dịch vụ",
  "data": null
}
```

---

## ✨ FEATURES HOÀN CHỈNH

✅ **CREATE** - Tạo service item mới với validation
✅ **READ** - Xem danh sách và chi tiết service item
✅ **UPDATE** - Cập nhật thông tin service item
✅ **DELETE** - Xóa service item với dialog xác nhận
✅ **Navigation** - Từ HomeActivity → Menu → ServiceItemListActivity
✅ **Error Handling** - Xử lý lỗi network và API
✅ **Loading States** - Hiển thị ProgressBar khi loading
✅ **Validation** - Kiểm tra input trước khi submit
✅ **Auto Refresh** - Danh sách tự động refresh sau CRUD
✅ **Material Design** - Giao diện đẹp với Material Components

---

## 🎬 HOÀN THÀNH!

Project đã sẵn sàng để test toàn bộ luồng CRUD Service Item cho admin!

**Bước tiếp theo:**
1. Sync project với Gradle
2. Build và chạy app
3. Đăng nhập với tài khoản admin
4. Test từng chức năng theo hướng dẫn trên

**Lưu ý:** Đảm bảo backend API đang chạy trước khi test!

