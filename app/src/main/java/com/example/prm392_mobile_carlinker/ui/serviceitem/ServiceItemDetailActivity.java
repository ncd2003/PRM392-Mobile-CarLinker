package com.example.prm392_mobile_carlinker.ui.serviceitem;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemDto;

public class ServiceItemDetailActivity extends AppCompatActivity {

    private TextView tvItemId;
    private TextView tvItemName;
    private TextView tvItemPrice;
    private TextView tvError;
    private ProgressBar progressBar;
    private CardView cardDetail;
    private ServiceItemViewModel viewModel;
    private int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_item_detail);

        initViews();
        setupToolbar();
        setupViewModel();
        loadServiceItemDetail();
    }

    private void initViews() {
        tvItemId = findViewById(R.id.tvItemId);
        tvItemName = findViewById(R.id.tvItemName);
        tvItemPrice = findViewById(R.id.tvItemPrice);
        tvError = findViewById(R.id.tvError);
        progressBar = findViewById(R.id.progressBar);
        cardDetail = findViewById(R.id.cardDetail);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chi tiết Dịch Vụ");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ServiceItemViewModel.class);
    }

    private void loadServiceItemDetail() {
        itemId = getIntent().getIntExtra("ITEM_ID", -1);

        if (itemId == -1) {
            showError("ID dịch vụ không hợp lệ");
            return;
        }

        viewModel.getServiceItemById(itemId).observe(this, resource -> {
            if (resource != null) {
                switch (resource.getStatus()) {
                    case LOADING:
                        showLoading();
                        break;
                    case SUCCESS:
                        showSuccess(resource.getData());
                        break;
                    case ERROR:
                        showError(resource.getMessage());
                        break;
                }
            }
        });
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        cardDetail.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
    }

    private void showSuccess(ServiceItemDto data) {
        progressBar.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
        cardDetail.setVisibility(View.VISIBLE);

        if (data != null) {
            tvItemId.setText(String.valueOf(data.getId()));
            tvItemName.setText(data.getName());

            // Format price as Vietnamese currency
            java.text.NumberFormat formatter = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("vi", "VN"));
            tvItemPrice.setText(formatter.format(data.getPrice()));
        }
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        cardDetail.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(message != null ? message : "Lỗi khi tải dữ liệu");
    }
}
