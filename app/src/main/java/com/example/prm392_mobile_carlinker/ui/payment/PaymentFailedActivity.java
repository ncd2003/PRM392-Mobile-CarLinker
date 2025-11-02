package com.example.prm392_mobile_carlinker.ui.payment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.ui.shop.ProductListActivity;

public class PaymentFailedActivity extends AppCompatActivity {

    private TextView tvOrderId;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_failed);

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
        Uri data = intent.getData();
        if (data != null) {
            orderId = data.getQueryParameter("orderId");
        } else {
            orderId = intent.getStringExtra("orderId");
        }

        if (orderId == null) orderId = "-";
        tvOrderId.setText(orderId);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ProductListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
