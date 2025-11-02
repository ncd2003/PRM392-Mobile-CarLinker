# Hướng dẫn fix lỗi VNPay Callback với ngrok

## 🔴 Lỗi hiện tại:
```
net::ERR_CONNECTION_REFUSED
https://localhost:7151/api/Vnpay/Callback
```

**Nguyên nhân:** VNPay server không thể callback về localhost của máy bạn.

## ✅ Giải pháp: Sử dụng ngrok để expose backend

### Bước 1: Tải và cài đặt ngrok
1. Truy cập: https://ngrok.com/download
2. Tải ngrok cho Windows
3. Giải nén và đặt vào thư mục (ví dụ: C:\ngrok)
4. Đăng ký tài khoản miễn phí tại https://dashboard.ngrok.com/signup
5. Lấy authtoken từ https://dashboard.ngrok.com/get-started/your-authtoken

### Bước 2: Cấu hình ngrok
Mở Command Prompt và chạy:
```bash
cd C:\ngrok
ngrok config add-authtoken YOUR_AUTHTOKEN_HERE
```

### Bước 3: Chạy backend (port 5291)
Chạy backend .NET của bạn như bình thường:
```bash
dotnet run
```
Hoặc chạy từ Visual Studio (F5)

### Bước 4: Expose backend qua ngrok
Mở Command Prompt mới và chạy:
```bash
cd C:\ngrok
ngrok http 5291
```

Bạn sẽ thấy output như này:
```
Session Status                online
Account                       your-account (Plan: Free)
Version                       3.x.x
Region                        United States (us)
Latency                       -
Web Interface                 http://127.0.0.1:4040
Forwarding                    https://abc123.ngrok-free.app -> http://localhost:5291
```

**Lưu ý URL:** `https://abc123.ngrok-free.app` (URL này sẽ khác mỗi lần chạy)

### Bước 5: Cập nhật appsettings.json
Mở file `appsettings.json` trong project backend và sửa:

```json
"Vnpay": {
  "TmnCode": "7UMWJMGA",
  "HashSecret": "Z27UV8KGXFWAJ7FYG0G486XFC18AK627",
  "BaseUrl": "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html",
  "ReturnUrl": "https://abc123.ngrok-free.app/api/Vnpay/Callback"
}
```

**⚠️ Thay `abc123.ngrok-free.app` bằng URL ngrok của bạn!**

### Bước 6: Restart backend
Sau khi sửa appsettings.json, restart backend để áp dụng cấu hình mới.

### Bước 7: Cập nhật API URL trong Android app (nếu cần)
Nếu app của bạn đang gọi API qua localhost, bạn CÓ THỂ giữ nguyên `http://10.0.2.2:5291` vì:
- Emulator vẫn gọi trực tiếp đến localhost
- VNPay sẽ callback qua ngrok URL

**NHƯNG** nếu muốn đồng nhất, bạn có thể đổi sang ngrok URL trong ApiService:

```java
public class ApiClient {
    // Thay đổi tạm thời khi test VNPay
    private static final String BASE_URL = "https://abc123.ngrok-free.app/";
    // private static final String BASE_URL = "http://10.0.2.2:5291/";
}
```

## 🧪 Test luồng thanh toán

1. ✅ Chạy backend (port 5291)
2. ✅ Chạy ngrok: `ngrok http 5291`
3. ✅ Copy ngrok URL (ví dụ: https://abc123.ngrok-free.app)
4. ✅ Cập nhật appsettings.json → ReturnUrl với ngrok URL
5. ✅ Restart backend
6. ✅ Chạy Android app
7. ✅ Thực hiện thanh toán VNPay
8. ✅ VNPay sẽ callback về: https://abc123.ngrok-free.app/api/Vnpay/Callback
9. ✅ Backend nhận callback → Cập nhật order status → Redirect về app

## 📊 Kiểm tra log

### 1. Xem ngrok traffic
Mở browser: http://127.0.0.1:4040
→ Bạn sẽ thấy tất cả request callback từ VNPay

### 2. Backend log
```
[INFO] VNPay Callback received
[INFO] Payment ID: 16
[INFO] Payment Status: SUCCESS
[INFO] Updating order 16 status to CONFIRMED
[INFO] Redirecting to: carlinker://payment-success?orderId=16
```

### 3. Android Logcat
```
VNPayActivity: Page started: carlinker://payment-success?orderId=16
VNPayActivity: Payment success detected! Order ID: 16
```

## ⚠️ Lưu ý quan trọng

### 1. ngrok URL thay đổi mỗi lần chạy (Free plan)
- Mỗi lần chạy ngrok, bạn sẽ có URL mới
- Phải cập nhật lại appsettings.json
- Nếu muốn URL cố định → nâng cấp ngrok (trả phí)

### 2. Network flow
```
VNPay Server → ngrok (internet) → localhost:5291 (backend) → Callback xử lý → Redirect về app
```

### 3. Khi deploy production
- Không dùng ngrok
- Dùng domain thật (ví dụ: https://api.yourapp.com)
- Cập nhật ReturnUrl trong appsettings.json production

## 🎯 Các lệnh cần nhớ

```bash
# Chạy ngrok
ngrok http 5291

# Stop ngrok
Ctrl + C

# Xem traffic ngrok
http://127.0.0.1:4040
```

## 🔄 Giải pháp thay thế (không khuyến nghị cho test)

### Cách 2: Deploy backend lên Azure/AWS
- Deploy backend lên cloud
- Có domain public (ví dụ: https://yourapi.azurewebsites.net)
- Cập nhật ReturnUrl trong appsettings.json
- **Nhược điểm:** Mất thời gian setup, khó debug

### Cách 3: Sử dụng LocalTunnel
Tương tự ngrok nhưng open-source:
```bash
npm install -g localtunnel
lt --port 5291
```

---

## ✅ Kết luận

**Để fix lỗi ERR_CONNECTION_REFUSED:**
1. Cài ngrok
2. Chạy: `ngrok http 5291`
3. Copy ngrok URL
4. Sửa appsettings.json → ReturnUrl = ngrok URL
5. Restart backend
6. Test lại thanh toán

**Vấn đề sẽ được giải quyết!** VNPay sẽ callback thành công và order status sẽ được cập nhật. 🎉

