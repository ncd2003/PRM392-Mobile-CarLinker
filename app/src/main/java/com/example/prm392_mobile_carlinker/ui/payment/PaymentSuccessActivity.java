package com.example.prm392_mobile_carlinker.ui.payment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_mobile_carlinker.R;

public class PaymentSuccessActivity extends AppCompatActivity {

    private TextView tvOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        tvOrderId = findViewById(R.id.tvOrderId);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String orderId = null;

        Uri data = intent.getData();
        if (data != null) {
            orderId = data.getQueryParameter("orderId");
        } else {
            orderId = intent.getStringExtra("orderId");
        }

        if (orderId == null) {
            orderId = "-";
        }

        Log.d("VNPay", "orderId = " + orderId);
        tvOrderId.setText(orderId);
    }
}
