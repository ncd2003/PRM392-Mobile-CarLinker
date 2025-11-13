package com.example.prm392_mobile_carlinker.Application;

import android.app.Application;

import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // init RetrofitClient với context app
        RetrofitClient.init(this);
    }
}
