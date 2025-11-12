# 🏗️ CẤU TRÚC DỰ ÁN - PRM392 CarLinker Mobile App (Java)

## 📱 Tổng quan dự án

**CarLinker** là ứng dụng di động kết nối khách hàng với các garage/dealer, cung cấp dịch vụ:
- 🛒 Mua sắm linh kiện, phụ tùng xe
- 📅 Đặt lịch bảo dưỡng, sửa chữa
- 🚨 Cứu hộ khẩn cấp
- 🔧 Tìm garage gần nhất
- 💬 Hỗ trợ khách hàng

---

## 📂 Cấu trúc thư mục

```
PRM392-Mobile-CarLinker/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/prm392_mobile_carlinker/
│   │   │   │   ├── data/                      # 📦 Lớp dữ liệu
│   │   │   │   │   ├── model/                 # Model classes
│   │   │   │   │   │   ├── cart/              # Model giỏ hàng
│   │   │   │   │   │   ├── product/           # Model sản phẩm
│   │   │   │   │   │   ├── order/             # Model đơn hàng
│   │   │   │   │   │   └── payment/           # Model thanh toán
│   │   │   │   │   ├── remote/                # API services
│   │   │   │   │   │   ├── ApiService.java
│   │   │   │   │   │   └── RetrofitClient.java
│   │   │   │   │   └── repository/            # Repository pattern
│   │   │   │   │       ├── CartRepository.java
│   │   │   │   │       ├── ProductRepository.java
│   │   │   │   │       ├── OrderRepository.java
│   │   │   │   │       └── PaymentRepository.java
│   │   │   │   │
│   │   │   │   ├── ui/                        # 🎨 Lớp giao diện
│   │   │   │   │   ├── home/                  # Trang chủ
│   │   │   │   │   │   └── HomeActivity.java
│   │   │   │   │   ├── auth/                  # Đăng nhập/Đăng ký
│   │   │   │   │   ├── shop/                  # Mua sắm
│   │   │   │   │   │   ├── ProductListActivity.java
│   │   │   │   │   │   ├── ProductDetailActivity.java
│   │   │   │   │   │   └── ProductViewModel.java
│   │   │   │   │   ├── cart/                  # Giỏ hàng
│   │   │   │   │   │   ├── CartActivity.java
│   │   │   │   │   │   └── CartViewModel.java
│   │   │   │   │   ├── checkout/              # Thanh toán
│   │   │   │   │   │   ├── CheckoutActivity.java
│   │   │   │   │   │   └── CheckoutViewModel.java
│   │   │   │   │   ├── payment/               # Cổng thanh toán
│   │   │   │   │   │   ├── VNPayActivity.java
│   │   │   │   │   │   ├── PaymentSuccessActivity.java
│   │   │   │   │   │   └── PaymentFailedActivity.java
│   │   │   │   │   ├── order/                 # Đơn hàng
│   │   │   │   │   │   ├── MyOrdersActivity.java
│   │   │   │   │   │   └── OrderDetailActivity.java
│   │   │   │   │   ├── booking/               # Đặt lịch dịch vụ
│   │   │   │   │   ├── emergency/             # Cứu hộ khẩn cấp
│   │   │   │   │   ├── service/               # Theo dõi dịch vụ
│   │   │   │   │   ├── vehicle/               # Quản lý xe
│   │   │   │   │   ├── transaction/           # Lịch sử giao dịch
│   │   │   │   │   ├── dealer/                # Quản lý đơn (Dealer)
│   │   │   │   │   ├── fragment/              # Fragments
│   │   │   │   │   └── adapter/               # RecyclerView Adapters
│   │   │   │   │
│   │   │   │   ├── util/                      # 🔧 Tiện ích
│   │   │   │   └── MainActivity.kt            # Activity chính (Kotlin)
│   │   │   │
│   │   │   ├── res/                           # 🎨 Resources
│   │   │   │   ├── layout/                    # XML layouts
│   │   │   │   │   ├── activity_home.xml      # ✨ Layout trang chủ
│   │   │   │   │   ├── activity_product_list.xml
│   │   │   │   │   ├── activity_cart.xml
│   │   │   │   │   └── ...
│   │   │   │   ├── drawable/                  # Icons, backgrounds
│   │   │   │   │   └── btn_emergency_background.xml
│   │   │   │   ├── values/                    # Colors, strings, themes
│   │   │   │   └── menu/                      # Menu resources
│   │   │   │
│   │   │   └── AndroidManifest.xml            # Manifest file
│   │   │
│   │   └── test/                              # Unit tests
│   │
│   └── build.gradle.kts                       # Dependencies & build config
│
├── gradle/                                     # Gradle wrapper
├── build.gradle.kts                           # Root build file
├── settings.gradle.kts                        # Project settings
├── Readme.md                                  # README luồng nghiệp vụ
├── PACKAGE_STRUCTURE_GUIDE.md                 # ✨ Hướng dẫn package (MỚI)
└── PROJECT_STRUCTURE.md                       # ✨ Tổng quan cấu trúc (FILE NÀY)
```

---

## 🎯 6 Luồng nghiệp vụ chính

### 1️⃣ Login & Register (Đăng nhập & Đăng ký)
```
📂 ui/auth/
   ├── LoginActivity.java
   ├── RegisterActivity.java
   └── AuthViewModel.java
```

**Chức năng:**
- Đăng nhập bằng email/số điện thoại
- Đăng ký tài khoản mới
- Quên mật khẩu
- Lưu token authentication

---

### 2️⃣ Booking Service (Đặt lịch dịch vụ)
```
📂 ui/booking/
   ├── BookingListActivity.java         # Danh sách lịch hẹn
   ├── CreateBookingActivity.java       # Tạo lịch hẹn mới
   ├── BookingDetailActivity.java       # Chi tiết lịch hẹn
   └── BookingViewModel.java
```

**Chức năng:**
- Xem danh sách garage
- Chọn khung giờ trống
- Đặt lịch bảo dưỡng/sửa chữa
- Xác nhận từ garage
- Xem lịch sử đặt lịch

---

### 3️⃣ Buying Parts (Mua linh kiện)
```
📂 ui/shop/                              # Cửa hàng
   ├── ProductListActivity.java          # Danh sách sản phẩm
   ├── ProductDetailActivity.java        # Chi tiết sản phẩm
   └── ProductViewModel.java

📂 ui/cart/                              # Giỏ hàng
   ├── CartActivity.java
   └── CartViewModel.java

📂 ui/checkout/                          # Thanh toán
   ├── CheckoutActivity.java
   └── CheckoutViewModel.java

📂 ui/payment/                           # Cổng thanh toán
   ├── VNPayActivity.java
   ├── PaymentSuccessActivity.java
   └── PaymentFailedActivity.java

📂 ui/order/                             # Đơn hàng
   ├── MyOrdersActivity.java
   └── OrderDetailActivity.java
```

**Chức năng:**
- Tìm kiếm sản phẩm
- Chọn variant (size, màu...)
- Thêm vào giỏ hàng
- Thanh toán (COD/VNPay)
- Theo dõi đơn hàng

---

### 4️⃣ Tracking Service (Theo dõi dịch vụ)
```
📂 ui/service/
   ├── ServiceHistoryActivity.java      # Lịch sử dịch vụ
   ├── ServiceDetailActivity.java       # Chi tiết dịch vụ
   └── ServiceViewModel.java
```

**Chức năng:**
- Xem dịch vụ đã sử dụng
- Trạng thái dịch vụ (đang xử lý/hoàn tất/hủy)
- Chi phí dịch vụ

---

### 5️⃣ Emergency Rescue (Cứu hộ khẩn cấp)
```
📂 ui/emergency/
   ├── EmergencyActivity.java           # Màn hình cứu hộ
   └── EmergencyViewModel.java
```

**Chức năng:**
- Gửi yêu cầu cứu hộ
- Gửi vị trí GPS
- Mô tả tình trạng xe
- Garage gần nhất nhận thông báo

---

### 6️⃣ Vehicle & Transaction Management (Quản lý xe & giao dịch)
```
📂 ui/vehicle/                           # Quản lý xe
   ├── VehicleListActivity.java
   ├── AddVehicleActivity.java
   └── VehicleDetailActivity.java

📂 ui/transaction/                       # Lịch sử giao dịch
   ├── TransactionHistoryActivity.java
   └── TransactionDetailActivity.java
```

**Chức năng:**
- Thêm/sửa/xóa thông tin xe
- Xem lịch sử giao dịch
- Xem lịch sử thanh toán

---

## 🏠 TRANG HOME MỚI (HomeActivity)

### 📍 Vị trí file:
```
ui/home/HomeActivity.java
res/layout/activity_home.xml
res/drawable/btn_emergency_background.xml
```

### 🎨 Giao diện:

```
┌─────────────────────────────────────┐
│  CarLinker                          │
│  Chào mừng bạn đến với dịch vụ...  │
│                                      │
│  ┌───────────────────────────────┐  │
│  │  🚨 CỨU HỘ NGAY              │  │  ← Nút đỏ, chiếm 80% chiều ngang
│  └───────────────────────────────┘  │
│                                      │
│  Tiện Ích                           │
│                                      │
│  ┌──────────┐  ┌──────────┐        │
│  │    🛒    │  │    📅    │        │
│  │ Mua sắm  │  │ Đặt lịch │        │
│  │ linh kiện│  │ dịch vụ  │        │
│  └──────────┘  └──────────┘        │
│                                      │
│  ┌──────────┐  ┌──────────┐        │
│  │    🔧    │  │    💬    │        │
│  │  Garage  │  │ Hỗ trợ   │        │
│  │ gần nhất │  │khách hàng│        │
│  └──────────┘  └──────────┘        │
└─────────────────────────────────────┘
```

### 🔧 Chức năng các nút:

1. **🚨 CỨU HỘ NGAY** (Nút đỏ lớn)
   - Gửi yêu cầu cứu hộ khẩn cấp
   - Mở `EmergencyActivity` (TODO)

2. **🛒 Mua sắm linh kiện** (Card xanh lá)
   - Mở `ProductListActivity`
   - Hiển thị danh sách sản phẩm

3. **📅 Đặt lịch dịch vụ** (Card xanh dương)
   - Mở `BookingActivity` (TODO)
   - Đặt lịch bảo dưỡng

4. **🔧 Garage gần nhất** (Card cam)
   - Mở `GarageListActivity` (TODO)
   - Tìm garage gần nhất

5. **💬 Hỗ trợ khách hàng** (Card tím)
   - Mở `SupportActivity` (TODO)
   - Chat với support

---

## 🛠️ Kiến trúc: MVVM Pattern

```
┌─────────────┐
│    View     │  Activity/Fragment
│ (UI Layer)  │  - Hiển thị dữ liệu
└──────┬──────┘  - Nhận input user
       │
       ↕ observe LiveData
       │
┌──────┴──────┐
│  ViewModel  │  - Giữ UI state
│             │  - Xử lý UI logic
└──────┬──────┘  - Gọi Repository
       │
       ↕ call methods
       │
┌──────┴──────┐
│ Repository  │  - Business logic
│             │  - Cache data
└──────┬──────┘  - Gọi API
       │
       ↕ network calls
       │
┌──────┴──────┐
│ ApiService  │  - Retrofit interface
│  (Retrofit) │  - HTTP requests
└─────────────┘
```

---

## 📚 Fragment là gì?

**Fragment** là một phần giao diện có thể tái sử dụng trong Android.

### 🔑 Đặc điểm:
- Fragment phải được đặt trong một Activity
- Một Activity có thể chứa nhiều Fragment
- Fragment có lifecycle riêng
- Thường dùng cho Tab, Navigation Drawer

### 📝 Ví dụ:

```java
// OrdersActivity có 3 tabs (3 fragments)
OrdersActivity
├── PendingOrdersFragment      (Tab: Đang xử lý)
├── CompletedOrdersFragment    (Tab: Đã hoàn thành)
└── CancelledOrdersFragment    (Tab: Đã hủy)
```

```java
// HomeFragment có thể được dùng trong MainActivity
MainActivity
├── BottomNavigationView
│   ├── HomeFragment           (Tab: Trang chủ)
│   ├── ShopFragment           (Tab: Cửa hàng)
│   ├── ProfileFragment        (Tab: Hồ sơ)
│   └── SettingsFragment       (Tab: Cài đặt)
```

---

## 📦 Các dependencies chính

```gradle
// AndroidX Libraries
androidx.appcompat:appcompat
androidx.cardview:cardview
androidx.recyclerview:recyclerview

// Lifecycle & ViewModel
androidx.lifecycle:lifecycle-viewmodel
androidx.lifecycle:lifecycle-livedata

// Retrofit (Networking)
com.squareup.retrofit2:retrofit
com.squareup.retrofit2:converter-gson

// Glide (Image Loading)
com.github.bumptech.glide:glide
```

---

## 🚀 Cách chạy dự án

### 1. Yêu cầu hệ thống:
- Android Studio Ladybug (2024.2+)
- JDK 11+
- Android SDK 26+ (Android 8.0+)
- Gradle 8.0+

### 2. Cài đặt:
```bash
# Clone project (nếu có git)
git clone <repository-url>

# Mở project trong Android Studio
File → Open → Chọn thư mục dự án

# Sync Gradle
File → Sync Project with Gradle Files

# Run app
Nhấn nút Run (Shift + F10)
```

### 3. Cấu hình Backend API:
- Mở `data/remote/RetrofitClient.java`
- Thay đổi `BASE_URL` thành URL backend của bạn

```java
private static final String BASE_URL = "http://your-api-url.com/";
```

---

## ✅ TODO: Các chức năng cần hoàn thiện

### Đã hoàn thành ✓
- [x] Trang chủ (HomeActivity)
- [x] Danh sách sản phẩm (ProductListActivity)
- [x] Chi tiết sản phẩm (ProductDetailActivity)
- [x] Giỏ hàng (CartActivity)
- [x] Thanh toán (CheckoutActivity)
- [x] Đơn hàng (MyOrdersActivity, OrderDetailActivity)
- [x] Thanh toán VNPay (VNPayActivity)

### Cần làm tiếp □
- [ ] Đăng nhập/Đăng ký (AuthActivity)
- [ ] Đặt lịch dịch vụ (BookingActivity)
- [ ] Cứu hộ khẩn cấp (EmergencyActivity)
- [ ] Tìm garage gần nhất (GarageListActivity)
- [ ] Hỗ trợ khách hàng (SupportActivity)
- [ ] Quản lý xe (VehicleActivity)
- [ ] Lịch sử dịch vụ (ServiceHistoryActivity)
- [ ] Lịch sử giao dịch (TransactionHistoryActivity)

---

## 📞 Liên hệ & Hỗ trợ

Nếu bạn có câu hỏi, vui lòng tham khảo:
- `PACKAGE_STRUCTURE_GUIDE.md` - Hướng dẫn chi tiết về các package
- `Readme.md` - Luồng nghiệp vụ FE Mobile

---

**Cập nhật lần cuối:** 2025-11-10  
**Version:** 1.0

