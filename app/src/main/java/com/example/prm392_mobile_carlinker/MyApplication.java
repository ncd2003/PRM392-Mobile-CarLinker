package com.example.prm392_mobile_carlinker;

import android.app.Application;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Khởi tạo RetrofitClient với context để có thể đọc token
        RetrofitClient.init(this);
    }
}

