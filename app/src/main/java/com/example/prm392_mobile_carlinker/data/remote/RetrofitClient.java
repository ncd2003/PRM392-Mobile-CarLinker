package com.example.prm392_mobile_carlinker.data.remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    // TODO: Thay đổi URL này thành URL backend thực tế của bạn
    // URL backend từ launchSettings.json (profile "http" và "https") là http://localhost:5291
    // Trên trình giả lập Android, localhost được thay thế bằng 10.0.2.2
    private static final String BASE_URL = "http://10.0.2.2:5291/";

    // LƯU Ý QUAN TRỌNG:
    // 1. URL này yêu cầu bạn phải thêm 'android:usesCleartextTraffic="true"' vào AndroidManifest.xml
    // 2. URL này yêu cầu bạn phải VÔ HIỆU HÓA 'app.UseHttpsRedirection();' trong file Program.cs (backend)
    //
    // Nếu bạn MUỐN dùng HTTPS (https://10.0.2.2:7151/), bạn sẽ gặp lỗi SSL
    // vì chứng chỉ dev của .NET không được tin cậy.

    // Nếu test trên thiết bị thật, dùng IP máy: "http://192.168.x.x:5291/"

    private static Retrofit retrofit = null;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            // Logging interceptor for debugging
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // OkHttp client with timeout and logging
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
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