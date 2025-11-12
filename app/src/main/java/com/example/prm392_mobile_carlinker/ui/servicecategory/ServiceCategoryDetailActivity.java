package com.example.prm392_mobile_carlinker.ui.servicecategory;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.servicecategory.ServiceCategory;
import com.example.prm392_mobile_carlinker.ui.adapter.ServiceItemAdapter;
import com.google.android.material.card.MaterialCardView;

public class ServiceCategoryDetailActivity extends AppCompatActivity {

    private TextView tvCategoryName;
    private RecyclerView rvServiceItems;
    private TextView tvNoServiceItems;
    private ProgressBar progressBar;
    private TextView tvError;
    private MaterialCardView cardContent;
    private ServiceCategoryViewModel viewModel;
    private ServiceItemAdapter adapter;
    private int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_category_detail);

        categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);

        if (categoryId == -1) {
            finish();
            return;
        }

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupViewModel();
        loadCategoryDetails();
    }

    private void initViews() {
        tvCategoryName = findViewById(R.id.tvCategoryName);
        rvServiceItems = findViewById(R.id.rvServiceItems);
        tvNoServiceItems = findViewById(R.id.tvNoServiceItems);
        progressBar = findViewById(R.id.progressBar);
        tvError = findViewById(R.id.tvError);
        cardContent = findViewById(R.id.cardContent);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chi tiết Gói Dịch Vụ");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new ServiceItemAdapter();
        rvServiceItems.setLayoutManager(new LinearLayoutManager(this));
        rvServiceItems.setAdapter(adapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ServiceCategoryViewModel.class);
    }

    private void loadCategoryDetails() {
        viewModel.getServiceCategoryById(categoryId).observe(this, resource -> {
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
        cardContent.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
    }

    private void showSuccess(ServiceCategory category) {
        progressBar.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
        cardContent.setVisibility(View.VISIBLE);

        if (category != null) {
            tvCategoryName.setText(category.getName());

            if (category.getServiceItems() != null && !category.getServiceItems().isEmpty()) {
                adapter.setServiceItems(category.getServiceItems());
                rvServiceItems.setVisibility(View.VISIBLE);
                tvNoServiceItems.setVisibility(View.GONE);
            } else {
                rvServiceItems.setVisibility(View.GONE);
                tvNoServiceItems.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        cardContent.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(message != null ? message : "Lỗi khi tải dữ liệu");
    }
}
