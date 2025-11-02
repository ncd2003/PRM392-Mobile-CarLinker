# Luồng thanh toán VNPay - Hướng dẫn chi tiết

## 📋 Tổng quan luồng thanh toán

### Backend (C# .NET)
```
1. CheckoutActivity tạo order → API: POST /api/Order/create-order
2. Nhận orderId từ response
3. Gọi API VNPay → GET /api/Vnpay/CreatePaymentUrl?orderId={orderId}&moneyToPay={amount}&description={desc}
4. Backend trả về payment URL của VNPay
5. Mở VNPayActivity với payment URL
6. Người dùng thanh toán trên trang VNPay
7. VNPay callback về Backend → GET /api/Vnpay/Callback
8. Backend cập nhật order status (CONFIRMED hoặc FAILED)
9. Backend redirect về app → carlinker://payment-success?orderId={orderId} hoặc carlinker://payment-failed?orderId={orderId}
10. App nhận deep link và chuyển đến PaymentSuccessActivity hoặc PaymentFailedActivity
```

## 🔧 Cấu hình Backend

### appsettings.json
```json
"Vnpay": {
  "TmnCode": "7UMWJMGA",
  "HashSecret": "Z27UV8KGXFWAJ7FYG0G486XFC18AK627",
  "BaseUrl": "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html",
  "ReturnUrl": "https://localhost:7151/api/Vnpay/Callback"
}
```

### VnpayController.cs - Callback
```csharp
[HttpGet("Callback")]
public async Task<ActionResult<string>> Callback()
{
    if (Request.QueryString.HasValue)
    {
        try
        {
            var paymentResult = _vnpay.GetPaymentResult(Request.Query);
            int paymentId = (int)paymentResult.PaymentId;

            if (paymentResult.IsSuccess)
            {
                // ✅ Cập nhật order status thành CONFIRMED
                await _orderRepository.UpdateOrderStatus(paymentId, OrderStatus.CONFIRMED);
                // ✅ Redirect về app mobile
                return Redirect($"carlinker://payment-success?orderId={paymentId}");
            }
            
            // ❌ Thanh toán thất bại
            await _orderRepository.UpdateOrderStatus(paymentId, OrderStatus.FAILED);
            return Redirect($"carlinker://payment-failed?orderId={paymentId}");
        }
        catch (Exception ex)
        {
            return BadRequest(ex.Message);
        }
    }
    return NotFound("Không tìm thấy thông tin thanh toán.");
}
```

## 📱 Cấu hình Android App

### AndroidManifest.xml - Deep Link
```xml
<activity
    android:name=".ui.payment.VNPayActivity"
    android:exported="true"
    android:launchMode="singleTop"
    android:parentActivityName=".ui.checkout.CheckoutActivity"
    android:label="Thanh toán VNPay">
    
    <!-- Deep Link để nhận callback từ backend -->
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data android:scheme="carlinker" />
    </intent-filter>
</activity>
```

### CheckoutActivity.java - Tạo thanh toán
```java
private void createVNPayPayment(int orderId, double totalAmount, String description) {
    viewModel.createVNPayPayment(orderId, totalAmount, description)
            .observe(this, resource -> {
                if (resource != null && resource.getStatus() == Resource.Status.SUCCESS) {
                    if (resource.getData() != null) {
                        // Mở VNPayActivity với payment URL và orderId
                        openVNPayPayment(
                            resource.getData().getPaymentUrl(), 
                            String.valueOf(orderId)
                        );
                    }
                }
            });
}

private void openVNPayPayment(String paymentUrl, String orderId) {
    Intent intent = new Intent(this, VNPayActivity.class);
    intent.putExtra(VNPayActivity.EXTRA_PAYMENT_URL, paymentUrl);
    intent.putExtra(VNPayActivity.EXTRA_ORDER_ID, orderId); // ✅ Truyền orderId (số nguyên dưới dạng string)
    startActivity(intent);
    finish();
}
```

### VNPayActivity.java - Xử lý WebView và Deep Link
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_vnpay);

    String paymentUrl = getIntent().getStringExtra(EXTRA_PAYMENT_URL);
    orderId = getIntent().getStringExtra(EXTRA_ORDER_ID);
    
    // Load VNPay payment page
    webView.loadUrl(paymentUrl);
}

// Xử lý khi backend redirect về app
private boolean checkReturnUrl(String url) {
    if (url == null) return false;

    if (url.startsWith("carlinker://payment-success")) {
        Uri uri = Uri.parse(url);
        String orderIdParam = uri.getQueryParameter("orderId");
        handlePaymentSuccess(orderIdParam);
        return true;
    }

    if (url.startsWith("carlinker://payment-failed")) {
        Uri uri = Uri.parse(url);
        String orderIdParam = uri.getQueryParameter("orderId");
        handlePaymentFailure(orderIdParam);
        return true;
    }

    return false;
}

private void handlePaymentSuccess(String orderIdParam) {
    runOnUiThread(() -> {
        Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_LONG).show();
        
        Intent intent = new Intent(this, PaymentSuccessActivity.class);
        intent.putExtra("ORDER_ID", orderIdParam != null ? orderIdParam : orderId);
        startActivity(intent);
        finish();
    });
}
```

## ✅ Kiểm tra luồng hoạt động

### 1. Log từ Backend
```
[INFO] VNPay Callback received
[INFO] Payment ID: 15
[INFO] Payment Status: SUCCESS
[INFO] Updating order 15 status to CONFIRMED
[INFO] Redirecting to: carlinker://payment-success?orderId=15
```

### 2. Log từ Android (Logcat)
```
VNPayActivity: Payment URL: https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?...
VNPayActivity: Order ID: 15
VNPayActivity: Page started: https://sandbox.vnpayment.vn/...
VNPayActivity: Page finished: https://sandbox.vnpayment.vn/...
VNPayActivity: shouldOverrideUrlLoading: carlinker://payment-success?orderId=15
VNPayActivity: Checking return URL: carlinker://payment-success?orderId=15
VNPayActivity: Payment success detected! Order ID: 15
VNPayActivity: handlePaymentSuccess called with orderId: 15
```

## 🔍 Debug khi gặp vấn đề

### Vấn đề: Không nhận được callback từ backend
**Kiểm tra:**
1. Backend có chạy và accessible từ emulator không? (http://10.0.2.2:5291)
2. Deep link scheme `carlinker://` đã đăng ký trong AndroidManifest.xml chưa?
3. WebView có enable JavaScript không? (`setJavaScriptEnabled(true)`)

### Vấn đề: Order status không được cập nhật
**Kiểm tra:**
1. Backend callback có được gọi không? (Check log backend)
2. `UpdateOrderStatus` có hoạt động đúng không?
3. OrderId truyền từ app có đúng không? (Phải là số nguyên, không phải chuỗi "ORD000015")

### Vấn đề: App không chuyển đến PaymentSuccessActivity
**Kiểm tra:**
1. Log trong `checkReturnUrl()` có hiện không?
2. URL callback có đúng format `carlinker://payment-success?orderId={id}` không?
3. `handlePaymentSuccess()` có được gọi không?

## 📝 Lưu ý quan trọng

1. **OrderId vs OrderCode:**
   - `orderId`: Số nguyên (1, 2, 3...) - Dùng cho API VNPay và callback
   - `orderCode`: Chuỗi ("ORD000001", "ORD000002"...) - Chỉ dùng để hiển thị

2. **Deep Link Scheme:**
   - Phải đăng ký trong AndroidManifest.xml
   - Backend phải redirect đúng scheme: `carlinker://`
   - App phải xử lý trong WebViewClient

3. **Emulator Network:**
   - Localhost backend: `http://10.0.2.2:5291`
   - Không dùng `http://localhost:5291`

4. **Order Status Flow:**
   ```
   PENDING (0) → [Thanh toán VNPay] → CONFIRMED (1) hoặc FAILED (6)
   ```

## 🚀 Test luồng thanh toán

1. Thêm sản phẩm vào giỏ hàng
2. Vào giỏ hàng → Checkout
3. Nhập thông tin đơn hàng
4. Chọn phương thức thanh toán: **VNPay**
5. Bấm "Đặt hàng"
6. App chuyển đến VNPayActivity với WebView
7. Thanh toán trên trang VNPay sandbox:
   - Ngân hàng: NCB
   - Số thẻ: 9704198526191432198
   - Tên: NGUYEN VAN A
   - Ngày phát hành: 07/15
   - Mật khẩu OTP: 123456
8. Xác nhận thanh toán
9. VNPay callback về backend
10. Backend cập nhật order status → CONFIRMED
11. Backend redirect về app: `carlinker://payment-success?orderId=15`
12. App nhận deep link và chuyển đến PaymentSuccessActivity
13. Kiểm tra order status đã được cập nhật thành CONFIRMED

✅ Hoàn thành!

