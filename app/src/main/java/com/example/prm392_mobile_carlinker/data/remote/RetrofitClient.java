package com.example.prm392_mobile_carlinker.data.remote;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.0.2.2:5291/";
    private static Retrofit retrofit = null;

    private static Context appContext;

    /**
     * Gọi 1 lần khi app khởi động (ví dụ trong Application.onCreate())
     */
    public static void init(Context context) {
        appContext = context.getApplicationContext();
    }

    /**
     * Đọc token từ SharedPreferences "UserPrefs" với key "authToken"
     * (phù hợp với SessionManager.createLoginSession)
     */
    private static String getToken() {
        if (appContext == null) {
            android.util.Log.w("RetrofitClient", "appContext is null, cannot get token");
            return null;
        }
        SharedPreferences prefs = appContext.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("authToken", null);
        if (token != null) {
            android.util.Log.d("RetrofitClient", "Token found: " + token.substring(0, Math.min(20, token.length())) + "...");
        } else {
            android.util.Log.w("RetrofitClient", "No token found in SharedPreferences");
        }
        return token;
    }

    public static Retrofit getInstance() {
        if (retrofit == null) {

            // Logging interceptor (body) để debug request/response
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Interceptor thêm Authorization header tự động nếu token tồn tại
            Interceptor authInterceptor = chain -> {
                Request original = chain.request();
                Request.Builder builder = original.newBuilder();

                String token = getToken();
                if (token != null && !token.isEmpty()) {
                    builder.addHeader("Authorization", "Bearer " + token);
                }

                // preserve original method & body
                builder.method(original.method(), original.body());
                return chain.proceed(builder.build());
            };

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(authInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getInstance().create(ApiService.class);
    }
}
