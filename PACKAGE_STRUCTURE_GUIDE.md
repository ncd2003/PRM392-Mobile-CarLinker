# 📦 HƯỚNG DẪN CẤU TRÚC PACKAGE - PRM392 CarLinker Mobile App

## 🏗️ Tổng quan kiến trúc

Dự án sử dụng kiến trúc **MVVM (Model-View-ViewModel)** với các package được tổ chức theo chức năng.

```
com.example.prm392_mobile_carlinker/
├── data/              # Lớp dữ liệu
├── ui/                # Lớp giao diện người dùng
├── util/              # Các tiện ích chung
└── MainActivity.kt    # Activity chính
```

---

## 📂 Chi tiết các Package

### 1️⃣ **data/** - Lớp Dữ Liệu

Chứa tất cả các thành phần liên quan đến xử lý dữ liệu, API, và model.

#### 📁 **data/model/** - Các Model dữ liệu
Chứa các class đại diện cho cấu trúc dữ liệu của ứng dụng.

- **data/model/cart/** - Model giỏ hàng
  - `CartItem.java` - Đại diện cho 1 item trong giỏ hàng
  - `CartResponse.java` - Response khi lấy giỏ hàng từ API
  - `AddToCartRequest.java` - Request thêm sản phẩm vào giỏ
  - `AddToCartResponse.java` - Response sau khi thêm vào giỏ
  - `UpdateCartRequest.java` - Request cập nhật số lượng
  - `UpdateCartResponse.java` - Response sau khi cập nhật
  - `ProductVariant.java` - Biến thể sản phẩm trong giỏ hàng
  - `BaseResponse.java` - Response cơ bản chung

- **data/model/product/** - Model sản phẩm
  - `Product.java` - Thông tin sản phẩm
  - `ProductDetail.java` - Chi tiết sản phẩm
  - `ProductVariant.java` - Biến thể sản phẩm (size, màu...)
  - `ProductOption.java` - Tùy chọn sản phẩm
  - `OptionValue.java` - Giá trị của tùy chọn
  - `ProductResponse.java` - Response danh sách sản phẩm
  - `ProductDetailResponse.java` - Response chi tiết sản phẩm

- **data/model/order/** - Model đơn hàng
  - `Order.java` - Thông tin đơn hàng
  - `OrderItem.java` - Item trong đơn hàng
  - `CreateOrderRequest.java` - Request tạo đơn hàng mới
  - `OrderResponse.java` - Response thông tin đơn hàng
  - `OrderListResponse.java` - Response danh sách đơn hàng
  - `UpdateOrderStatusRequest.java` - Request cập nhật trạng thái đơn

- **data/model/payment/** - Model thanh toán
  - `VNPayResponse.java` - Response từ VNPay gateway

#### 📁 **data/remote/** - Kết nối API
Chứa các class xử lý kết nối với server backend.

- `ApiService.java` - Interface định nghĩa các API endpoints
- `RetrofitClient.java` - Client Retrofit để gọi API

#### 📁 **data/repository/** - Repository Pattern
Lớp trung gian giữa ViewModel và data source, xử lý logic nghiệp vụ dữ liệu.

- `CartRepository.java` - Xử lý logic giỏ hàng
- `ProductRepository.java` - Xử lý logic sản phẩm
- `OrderRepository.java` - Xử lý logic đơn hàng
- `PaymentRepository.java` - Xử lý logic thanh toán
- `Resource.java` - Wrapper class cho trạng thái dữ liệu (Success/Error/Loading)

---

### 2️⃣ **ui/** - Lớp Giao Diện Người Dùng

Chứa tất cả các Activity, Fragment, ViewModel và Adapter cho giao diện.

#### 📁 **ui/home/** - Trang chủ
**Chức năng:** Màn hình chính hiển thị các chức năng chính của app

- `HomeActivity.java` - Activity trang chủ
  - Hiển thị nút cứu hộ khẩn cấp
  - 4 nút tiện ích: Mua sắm, Đặt lịch, Garage, Hỗ trợ

#### 📁 **ui/auth/** - Xác thực
**Chức năng:** Đăng nhập, đăng ký, quên mật khẩu

- `LoginActivity.java` - Màn hình đăng nhập
- `RegisterActivity.java` - Màn hình đăng ký
- `AuthViewModel.java` - ViewModel quản lý trạng thái xác thực

#### 📁 **ui/shop/** - Cửa hàng
**Chức năng:** Mua sắm linh kiện, phụ tùng xe

- `ProductListActivity.java` - Danh sách sản phẩm
  - Hiển thị grid sản phẩm
  - Tìm kiếm sản phẩm
  - Phân loại sản phẩm
  
- `ProductDetailActivity.java` - Chi tiết sản phẩm
  - Hiển thị thông tin chi tiết
  - Chọn biến thể (size, màu...)
  - Thêm vào giỏ hàng
  
- `ProductViewModel.java` - ViewModel quản lý dữ liệu sản phẩm

#### 📁 **ui/cart/** - Giỏ hàng
**Chức năng:** Quản lý giỏ hàng

- `CartActivity.java` - Màn hình giỏ hàng
  - Hiển thị danh sách sản phẩm trong giỏ
  - Tăng/giảm số lượng
  - Xóa sản phẩm
  - Tính tổng tiền
  
- `CartViewModel.java` - ViewModel quản lý giỏ hàng

#### 📁 **ui/checkout/** - Thanh toán
**Chức năng:** Xử lý thanh toán đơn hàng

- `CheckoutActivity.java` - Màn hình thanh toán
  - Nhập thông tin giao hàng
  - Chọn phương thức thanh toán (COD/VNPay)
  - Xác nhận đơn hàng
  
- `CheckoutViewModel.java` - ViewModel quản lý thanh toán

#### 📁 **ui/payment/** - Cổng thanh toán
**Chức năng:** Xử lý thanh toán online qua VNPay

- `VNPayActivity.java` - WebView hiển thị VNPay
- `PaymentSuccessActivity.java` - Màn hình thanh toán thành công
- `PaymentFailedActivity.java` - Màn hình thanh toán thất bại

#### 📁 **ui/order/** - Đơn hàng
**Chức năng:** Quản lý đơn hàng của khách

- `MyOrdersActivity.java` - Danh sách đơn hàng của tôi
  - Hiển thị tất cả đơn hàng
  - Lọc theo trạng thái
  
- `MyOrdersViewModel.java` - ViewModel quản lý danh sách đơn
  
- `OrderDetailActivity.java` - Chi tiết đơn hàng
  - Thông tin chi tiết đơn hàng
  - Trạng thái đơn hàng
  - Danh sách sản phẩm trong đơn
  
- `OrderDetailViewModel.java` - ViewModel chi tiết đơn

#### 📁 **ui/dealer/** - Quản lý đơn hàng (Dealer)
**Chức năng:** Dành cho garage/dealer quản lý đơn hàng

- `DealerOrdersActivity.java` - Danh sách đơn hàng dealer
  - Xem tất cả đơn hàng
  - Cập nhật trạng thái đơn
  
- `DealerOrdersViewModel.java` - ViewModel quản lý đơn dealer

#### 📁 **ui/booking/** - Đặt lịch dịch vụ
**Chức năng:** Đặt lịch bảo dưỡng, sửa chữa xe tại garage

- `BookingListActivity.java` - Danh sách lịch hẹn
- `CreateBookingActivity.java` - Tạo lịch hẹn mới
- `BookingDetailActivity.java` - Chi tiết lịch hẹn
- `BookingViewModel.java` - ViewModel quản lý booking

#### 📁 **ui/emergency/** - Cứu hộ khẩn cấp
**Chức năng:** Gọi cứu hộ khi xe gặp sự cố

- `EmergencyActivity.java` - Màn hình cứu hộ
  - Gửi vị trí GPS
  - Mô tả tình trạng xe
  - Liên hệ garage gần nhất

#### 📁 **ui/service/** - Theo dõi dịch vụ
**Chức năng:** Xem lịch sử dịch vụ đã sử dụng tại garage

- `ServiceHistoryActivity.java` - Lịch sử dịch vụ
- `ServiceDetailActivity.java` - Chi tiết dịch vụ
- `ServiceViewModel.java` - ViewModel quản lý service record

#### 📁 **ui/vehicle/** - Quản lý xe
**Chức năng:** Quản lý thông tin xe của người dùng

- `VehicleListActivity.java` - Danh sách xe
- `AddVehicleActivity.java` - Thêm xe mới
- `VehicleDetailActivity.java` - Chi tiết xe
- `VehicleViewModel.java` - ViewModel quản lý xe

#### 📁 **ui/transaction/** - Lịch sử giao dịch
**Chức năng:** Xem lịch sử giao dịch, thanh toán

- `TransactionHistoryActivity.java` - Lịch sử giao dịch
- `TransactionDetailActivity.java` - Chi tiết giao dịch

#### 📁 **ui/fragment/** - Fragment
**Chức năng:** Chứa các Fragment sử dụng trong app

**Fragment là gì?**
- Fragment là một phần của giao diện có thể tái sử dụng
- Fragment phải được đặt trong một Activity
- Một Activity có thể chứa nhiều Fragment
- Fragment có lifecycle riêng nhưng phụ thuộc vào Activity

**Ví dụ sử dụng Fragment:**
```java
// Fragment cho tab "Đang xử lý" trong OrdersActivity
public class PendingOrdersFragment extends Fragment {
    // Hiển thị danh sách đơn hàng đang xử lý
}

// Fragment cho tab "Đã hoàn thành"
public class CompletedOrdersFragment extends Fragment {
    // Hiển thị danh sách đơn hàng đã hoàn thành
}
```

#### 📁 **ui/adapter/** - RecyclerView Adapter
**Chức năng:** Adapter để hiển thị danh sách dữ liệu trong RecyclerView

- `ProductAdapter.java` - Adapter hiển thị danh sách sản phẩm
- `OrderAdapter.java` - Adapter hiển thị danh sách đơn hàng
- `OrderItemAdapter.java` - Adapter hiển thị items trong đơn
- `DealerOrderAdapter.java` - Adapter hiển thị đơn hàng dealer
- `VariantAdapter.java` - Adapter hiển thị biến thể sản phẩm

#### 📁 **ui/theme/** - Theme
**Chức năng:** Định nghĩa theme, color, typography cho app

---

### 3️⃣ **util/** - Tiện ích

Chứa các class tiện ích, helper functions.

- `Constants.java` - Các hằng số dùng chung
- `SharedPrefsHelper.java` - Quản lý SharedPreferences
- `NetworkUtils.java` - Kiểm tra kết nối mạng
- `DateUtils.java` - Xử lý date/time
- `ValidationUtils.java` - Validate input

---

## 🔄 Luồng hoạt động (MVVM Pattern)

```
View (Activity/Fragment)
    ↕️
ViewModel
    ↕️
Repository
    ↕️
ApiService (Retrofit)
    ↕️
Backend API
```

### Giải thích:

1. **View (Activity/Fragment):** 
   - Hiển thị UI và nhận input từ user
   - Observe dữ liệu từ ViewModel
   - Không chứa business logic

2. **ViewModel:**
   - Giữ và quản lý dữ liệu cho View
   - Gọi Repository để lấy/cập nhật dữ liệu
   - Sống lâu hơn Activity (survive configuration changes)

3. **Repository:**
   - Trung gian giữa ViewModel và data source
   - Quyết định lấy dữ liệu từ đâu (API, Database, Cache)
   - Xử lý business logic liên quan đến dữ liệu

4. **ApiService:**
   - Interface định nghĩa các API endpoints
   - Sử dụng Retrofit để gọi API

---

## 📝 Ví dụ thực tế: Luồng mua hàng

### Bước 1: User xem danh sách sản phẩm

```java
// ProductListActivity.java (View)
├── Hiển thị RecyclerView sản phẩm
├── Observe ProductViewModel.products
└── Click vào sản phẩm → mở ProductDetailActivity

// ProductViewModel.java (ViewModel)
├── Gọi productRepository.getProducts()
└── Cập nhật LiveData<List<Product>>

// ProductRepository.java (Repository)
├── Gọi apiService.getProducts()
└── Trả về kết quả cho ViewModel
```

### Bước 2: User thêm sản phẩm vào giỏ

```java
// ProductDetailActivity.java (View)
├── User chọn variant và nhấn "Thêm vào giỏ"
└── Gọi cartViewModel.addToCart(productId, variantId, quantity)

// CartViewModel.java (ViewModel)
├── Gọi cartRepository.addToCart()
└── Cập nhật LiveData<Resource<CartResponse>>

// CartRepository.java (Repository)
├── Tạo AddToCartRequest
├── Gọi apiService.addToCart(request)
└── Trả về kết quả
```

### Bước 3: User xem giỏ hàng

```java
// CartActivity.java (View)
├── Hiển thị danh sách items trong giỏ
├── Observe CartViewModel.cartItems
└── Cho phép tăng/giảm số lượng, xóa items

// CartViewModel.java (ViewModel)
├── Gọi cartRepository.getCart()
├── Gọi cartRepository.updateQuantity()
└── Gọi cartRepository.removeItem()
```

### Bước 4: User thanh toán

```java
// CheckoutActivity.java (View)
├── Nhập thông tin giao hàng
├── Chọn phương thức thanh toán
└── Gọi checkoutViewModel.createOrder()

// CheckoutViewModel.java (ViewModel)
├── Gọi orderRepository.createOrder()
└── Nếu VNPay → gọi paymentRepository.createVNPayPayment()

// PaymentRepository.java (Repository)
├── Tạo đơn hàng qua orderRepository
├── Gọi apiService.createVNPayPayment()
└── Trả về payment URL
```

---

## 🎨 Quy tắc đặt tên

### Activity
- Format: `[Feature]Activity.java`
- Ví dụ: `ProductListActivity`, `CheckoutActivity`

### Fragment
- Format: `[Feature]Fragment.java`
- Ví dụ: `HomeFragment`, `ProfileFragment`

### ViewModel
- Format: `[Feature]ViewModel.java`
- Ví dụ: `ProductViewModel`, `CartViewModel`

### Repository
- Format: `[Feature]Repository.java`
- Ví dụ: `ProductRepository`, `OrderRepository`

### Adapter
- Format: `[Item]Adapter.java`
- Ví dụ: `ProductAdapter`, `OrderAdapter`

### Model
- Format: `[Entity].java` hoặc `[Entity]Response.java`
- Ví dụ: `Product.java`, `ProductResponse.java`

### Layout XML
- Activity: `activity_[name].xml` → `activity_home.xml`
- Fragment: `fragment_[name].xml` → `fragment_profile.xml`
- Item: `item_[name].xml` → `item_product.xml`

---

## 🚀 Hướng dẫn thêm chức năng mới

### Ví dụ: Thêm chức năng "Đánh giá sản phẩm"

#### 1. Tạo Model
```
data/model/review/
├── Review.java
├── ReviewRequest.java
└── ReviewResponse.java
```

#### 2. Thêm API vào ApiService
```java
@GET("api/reviews/{productId}")
Call<ReviewResponse> getReviews(@Path("productId") String productId);

@POST("api/reviews")
Call<Review> createReview(@Body ReviewRequest request);
```

#### 3. Tạo Repository
```
data/repository/ReviewRepository.java
```

#### 4. Tạo ViewModel
```
ui/review/ReviewViewModel.java
```

#### 5. Tạo UI
```
ui/review/ReviewListActivity.java
ui/review/CreateReviewActivity.java
```

#### 6. Tạo Layout
```
res/layout/activity_review_list.xml
res/layout/activity_create_review.xml
res/layout/item_review.xml
```

#### 7. Tạo Adapter
```
ui/adapter/ReviewAdapter.java
```

---

## 📚 Tài liệu tham khảo

- **MVVM Pattern:** https://developer.android.com/topic/architecture
- **Retrofit:** https://square.github.io/retrofit/
- **LiveData & ViewModel:** https://developer.android.com/topic/libraries/architecture/livedata
- **RecyclerView:** https://developer.android.com/guide/topics/ui/layout/recyclerview

---

## ✅ Checklist khi tạo feature mới

- [ ] Tạo Model classes trong `data/model/`
- [ ] Thêm API endpoints vào `ApiService.java`
- [ ] Tạo Repository trong `data/repository/`
- [ ] Tạo ViewModel trong `ui/[feature]/`
- [ ] Tạo Activity/Fragment trong `ui/[feature]/`
- [ ] Tạo Layout XML trong `res/layout/`
- [ ] Tạo Adapter nếu có RecyclerView
- [ ] Thêm Activity vào `AndroidManifest.xml`
- [ ] Thêm permissions nếu cần
- [ ] Test chức năng

---

**Cập nhật lần cuối:** 2025-11-10
**Version:** 1.0

