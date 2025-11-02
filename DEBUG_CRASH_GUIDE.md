# HƯỚNG DẪN KHẮC PHỤC LỖI "STOP WORKING"

## ✅ CÁC VẤN ĐỀ ĐÃ SỬA

### 1. **Theme Configuration - VẤN ĐỀ QUAN TRỌNG NHẤT**
**Nguyên nhân:** Theme cũ `android:Theme.Material.Light.NoActionBar` không tương thích với `AppCompatActivity` và `getSupportActionBar()` trả về `null`.

**Đã sửa:**
```xml
<!-- Trước -->
<style name="Theme.PRM392MobileCarLinker" parent="android:Theme.Material.Light.NoActionBar" />

<!-- Sau -->
<style name="Theme.PRM392MobileCarLinker" parent="Theme.AppCompat.Light.DarkActionBar">
    <item name="colorPrimary">#6200EE</item>
    <item name="colorPrimaryDark">#3700B3</item>
    <item name="colorAccent">#03DAC5</item>
</style>
```

### 2. **ProductListActivity - Back Button**
**Nguyên nhân:** `setDisplayHomeAsUpEnabled(true)` trên launcher activity gây lỗi vì không có activity trước đó để back về.

**Đã sửa:** Đã comment out dòng này vì ProductListActivity là màn hình đầu tiên.

### 3. **Error Handling cải thiện**
Đã thêm xử lý lỗi chi tiết hơn để hiển thị thông báo rõ ràng khi:
- Không kết nối được backend
- Không có dữ liệu
- Lỗi network

## 🔧 KIỂM TRA THÊM

### Bước 1: Sync Gradle
```
File → Sync Project with Gradle Files
```

### Bước 2: Clean & Rebuild
```
Build → Clean Project
Build → Rebuild Project
```

### Bước 3: Kiểm tra Backend
**QUAN TRỌNG:** Đảm bảo backend API đang chạy!

Trong file `RetrofitClient.java`:
```java
private static final String BASE_URL = "http://10.0.2.2:5291/";
```

**Kiểm tra:**
1. Backend .NET đang chạy trên port 5291?
2. Đã vô hiệu hóa `app.UseHttpsRedirection()` trong Program.cs?
3. Nếu test trên thiết bị thật, đổi thành: `http://[YOUR_PC_IP]:5291/`

### Bước 4: Chạy lại app
Uninstall app cũ trước, rồi chạy lại từ Android Studio.

## 🐛 NẾU VẪN BỊ CRASH

### Xem Logcat để tìm lỗi cụ thể:
1. Mở Logcat trong Android Studio (View → Tool Windows → Logcat)
2. Filter: chọn "Error" hoặc tìm kiếm "prm392_mobile_carlinker"
3. Tìm dòng có "FATAL EXCEPTION" hoặc "Caused by:"
4. Copy stack trace và gửi lại cho tôi

### Các lỗi thường gặp:

#### 1. NetworkOnMainThreadException
- Không xảy ra vì đã dùng Retrofit với callbacks

#### 2. NullPointerException
- Kiểm tra xem các View trong layout có đúng ID không
- Kiểm tra findViewById() có trả về null không

#### 3. ClassNotFoundException hoặc NoClassDefFoundError
- Chạy: Build → Clean Project
- Chạy: File → Invalidate Caches / Restart

#### 4. Failed to connect / Network error
- Backend chưa chạy
- URL sai
- Permissions INTERNET chưa có (đã thêm rồi)

## 📋 CHECKLIST TRƯỚC KHI CHẠY

- [x] Đã sync Gradle
- [x] Đã rebuild project
- [ ] Backend API đang chạy
- [ ] URL trong RetrofitClient.java đúng
- [ ] Uninstall app cũ
- [ ] Chạy app mới

## 🎯 TEST ĐƠN GIẢN

Nếu app vẫn crash ngay khi mở, thử test bằng cách:

1. Tạm thời comment toàn bộ code trong `loadProducts()` method
2. Chỉ để lại:
```java
private void loadProducts() {
    Toast.makeText(this, "App đã chạy được!", Toast.LENGTH_LONG).show();
}
```
3. Chạy lại app
4. Nếu thấy Toast → Lỗi ở network/backend
5. Nếu vẫn crash → Lỗi ở layout hoặc initialization

## 📞 CÁCH LẤY LOG CHI TIẾT

Trong Android Studio:
1. Chạy app
2. Khi crash, vào Logcat
3. Click "Restart" icon (nếu có)
4. Tìm dòng đỏ với "AndroidRuntime: FATAL EXCEPTION"
5. Copy toàn bộ stack trace từ đó đến hết
6. Gửi lại cho tôi để phân tích chi tiết

## 🚀 GIẢI PHÁP ĐÃ THỰC HIỆN

1. ✅ Đổi theme sang AppCompat
2. ✅ Xóa setDisplayHomeAsUpEnabled cho launcher activity
3. ✅ Cải thiện error handling
4. ✅ Thêm null checks
5. ✅ Thêm thông báo lỗi chi tiết

Nếu vẫn bị lỗi, hãy gửi log chi tiết để tôi phân tích thêm!

