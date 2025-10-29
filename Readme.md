Dưới đây là **tóm tắt luồng nghiệp vụ FE (Mobile App)** cho hệ thống Carlinker — chỉ tập trung vào trải nghiệm người dùng:

---

### 📱 **1️⃣ Luồng đăng nhập & đăng ký**

* Người dùng mở app → chọn **Đăng nhập** hoặc **Đăng ký mới**.
* Nhập thông tin (số điện thoại, email, mật khẩu).
* Sau khi đăng nhập, hệ thống chuyển đến **trang chủ (Dashboard)**.

---

### 🚗 **2️⃣ Luồng đặt lịch dịch vụ**

* Người dùng chọn **Danh sách Gara** → xem chi tiết Gara.
* Hệ thống hiển thị **lịch trống** (khung giờ, khu vực).
* Người dùng chọn khung giờ → xác nhận đặt lịch.
* Gara nhận thông báo → xác nhận lại.
* Người dùng xem trạng thái lịch hẹn trong mục **Lịch sử đặt lịch**.

---

### 🧰 **3️⃣ Luồng mua linh kiện**

* Từ trang **Cửa hàng**, người dùng tìm kiếm sản phẩm.
* Chọn sản phẩm → chọn **variant** (kích cỡ, màu sắc...).
* Thêm vào giỏ hàng → chọn **thanh toán COD hoặc VNPay**.
* Nhận xác nhận đơn hàng → theo dõi trạng thái trong **Đơn hàng của tôi**.

---

### 🔧 **4️⃣ Luồng theo dõi dịch vụ tại Gara**

* Khi Gara tạo dịch vụ cho xe của khách (Service Record),
  người dùng (nếu có tài khoản) sẽ thấy dịch vụ đó trong **mục Theo dõi dịch vụ**.
* Hiển thị: tên dịch vụ, trạng thái (đang xử lý / hoàn tất / hủy), chi phí.

---

### 🚨 **5️⃣ Luồng cứu hộ khẩn cấp**

* Người dùng mở tính năng **Cứu hộ khẩn cấp**.
* Gửi yêu cầu cứu hộ với vị trí GPS và mô tả tình trạng.
* Gara gần nhất nhận thông báo và phản hồi.

---

### 🚘 **6️⃣ Quản lý xe & giao dịch**

* Người dùng có thể thêm / sửa / xóa xe trong **Quản lý xe**.
* Trong **Lịch sử giao dịch**, người dùng xem lại các đơn hàng, thanh toán, dịch vụ đã dùng.

---

👉 **Tóm lại**, FE Mobile gồm 6 flow chính:

1. Login / Register
2. Booking Service
3. Buying Parts
4. Tracking Service
5. Emergency Rescue
6. Vehicle & Transaction Management

---

Bạn có muốn tôi **vẽ sơ đồ tóm tắt 6 luồng này dạng “mobile UX flow”** (từng màn hình + hướng điều hướng) để dễ dùng cho team FE không?
