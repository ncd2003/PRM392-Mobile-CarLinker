package com.example.prm392_mobile_carlinker.ui.payment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_mobile_carlinker.R;

public class VNPayActivity extends AppCompatActivity {

    private static final String TAG = "VNPayActivity";

    public static final String EXTRA_PAYMENT_URL = "PAYMENT_URL";
    public static final String EXTRA_ORDER_ID = "ORDER_ID";

    private WebView webView;
    private ProgressBar progressBar;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vnpay);

        String paymentUrl = getIntent().getStringExtra(EXTRA_PAYMENT_URL);
        orderId = getIntent().getStringExtra(EXTRA_ORDER_ID);

        if (paymentUrl == null || paymentUrl.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy URL thanh toán", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d(TAG, "Payment URL: " + paymentUrl);
        Log.d(TAG, "Order ID: " + orderId);

        initViews();
        loadPaymentUrl(paymentUrl);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent called");
        handleDeepLink(intent);
    }

    private void handleDeepLink(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            String scheme = data.getScheme();
            String host = data.getHost();

            Log.d(TAG, "Deep link received - Scheme: " + scheme + ", Host: " + host);
            Log.d(TAG, "Full URI: " + data.toString());

            if ("carlinker".equals(scheme)) {
                String orderIdParam = data.getQueryParameter("orderId");

                if ("payment-success".equals(host)) {
                    handlePaymentSuccess(orderIdParam);
                } else if ("payment-failed".equals(host)) {
                    handlePaymentFailure(orderIdParam);
                }
            }
        }
    }

    private void initViews() {
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thanh toán VNPay");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setupWebView();
    }

    private void setupWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setSupportZoom(false);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(TAG, "Page started: " + url);

                // ✅ Kiểm tra deep link ngay khi page bắt đầu load
                if (checkDeepLinkUrl(url)) {
                    view.stopLoading(); // Dừng load nếu là deep link
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "Page finished: " + url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                Log.d(TAG, "shouldOverrideUrlLoading: " + url);

                // ✅ Kiểm tra deep link trước
                if (checkDeepLinkUrl(url)) {
                    return true; // Chặn không cho WebView load
                }

                // Load URL bình thường
                return false;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.e(TAG, "WebView error: " + description + " for URL: " + failingUrl);

                // Nếu là deep link bị lỗi, vẫn xử lý
                if (checkDeepLinkUrl(failingUrl)) {
                    // Deep link đã được xử lý
                    return;
                }

                progressBar.setVisibility(View.GONE);
                Toast.makeText(VNPayActivity.this, "Lỗi tải trang: " + description, Toast.LENGTH_SHORT).show();
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void loadPaymentUrl(String url) {
        webView.loadUrl(url);
    }

    /**
     * Kiểm tra và xử lý deep link URL
     * @return true nếu là deep link, false nếu không phải
     */
    private boolean checkDeepLinkUrl(String url) {
        if (url == null) return false;

        Log.d(TAG, "Checking deep link URL: " + url);

        // Bắt deep link payment-success
        if (url.startsWith("carlinker://payment-success")) {
            Uri uri = Uri.parse(url);
            String orderIdParam = uri.getQueryParameter("orderId");
            Log.d(TAG, "✅ Payment success deep link detected! Order ID: " + orderIdParam);
            handlePaymentSuccess(orderIdParam);
            return true;
        }

        // Bắt deep link payment-failed
        if (url.startsWith("carlinker://payment-failed")) {
            Uri uri = Uri.parse(url);
            String orderIdParam = uri.getQueryParameter("orderId");
            Log.d(TAG, "✅ Payment failed deep link detected! Order ID: " + orderIdParam);
            handlePaymentFailure(orderIdParam);
            return true;
        }

        return false;
    }

    private void handlePaymentSuccess(String orderIdParam) {
        Log.d(TAG, "handlePaymentSuccess called with orderId: " + orderIdParam);

        // Validate orderId
        final String finalOrderId = (orderIdParam != null && !orderIdParam.isEmpty())
                ? orderIdParam : orderId;

        if (finalOrderId == null || finalOrderId.isEmpty()) {
            Log.e(TAG, "Order ID is null or empty!");
            Toast.makeText(this, "Lỗi: Không tìm thấy mã đơn hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ KHÔNG CẦN GỌI API confirmPayment vì backend đã tự động update status trong Callback rồi
        // Backend flow: VNPay → Backend Callback → UpdateOrderStatus → Redirect về app
        Log.d(TAG, "✅ Payment already confirmed by backend callback. OrderId: " + finalOrderId);
        navigateToSuccessScreen(finalOrderId);
    }

    /**
     * Chuyển đến màn hình thanh toán thành công
     */
    private void navigateToSuccessScreen(String orderIdStr) {
        runOnUiThread(() -> {
            Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, PaymentSuccessActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("ORDER_ID", orderIdStr);
            startActivity(intent);
            finish();
        });
    }

    private void handlePaymentFailure(String orderIdParam) {
        Log.d(TAG, "handlePaymentFailure called with orderId: " + orderIdParam);

        // Validate orderId
        final String finalOrderId = (orderIdParam != null && !orderIdParam.isEmpty())
                ? orderIdParam : orderId;

        runOnUiThread(() -> {
            Toast.makeText(this, "Thanh toán thất bại!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, PaymentFailedActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("ORDER_ID", finalOrderId);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Hủy thanh toán?")
                    .setMessage("Bạn có chắc muốn hủy thanh toán?")
                    .setPositiveButton("Có", (dialog, which) -> finish())
                    .setNegativeButton("Không", null)
                    .show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
