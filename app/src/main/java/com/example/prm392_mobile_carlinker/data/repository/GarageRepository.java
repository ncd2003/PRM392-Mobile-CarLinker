package com.example.prm392_mobile_carlinker.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prm392_mobile_carlinker.data.model.garage.Garage;
import com.example.prm392_mobile_carlinker.data.model.garage.GarageDetail;
import com.example.prm392_mobile_carlinker.data.model.garage.GarageDetailResponse;
import com.example.prm392_mobile_carlinker.data.model.garage.GarageResponse;
import com.example.prm392_mobile_carlinker.data.remote.ApiService;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository để xử lý logic nghiệp vụ liên quan đến Garage
 */
public class GarageRepository {
    private final ApiService apiService;

    public GarageRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    /**
     * Lấy danh sách garage từ API
     */
    public LiveData<Resource<List<Garage>>> getAllGarages() {
        MutableLiveData<Resource<List<Garage>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.getAllGarages().enqueue(new Callback<GarageResponse>() {
            @Override
            public void onResponse(@NonNull Call<GarageResponse> call, @NonNull Response<GarageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GarageResponse garageResponse = response.body();

                    if (garageResponse.getStatus() == 200 && garageResponse.getData() != null) {
                        List<Garage> garages = garageResponse.getData().getItems();
                        if (garages != null) {
                            result.setValue(Resource.success(garages));
                        } else {
                            result.setValue(Resource.success(new ArrayList<>()));
                        }
                    } else {
                        result.setValue(Resource.error(garageResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Lỗi khi tải danh sách garage", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<GarageResponse> call, @NonNull Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Lấy chi tiết garage từ API theo ID
     */
    public LiveData<Resource<GarageDetail>> getGarageDetails(int garageId) {
        MutableLiveData<Resource<GarageDetail>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        apiService.getGarageDetailsById(garageId).enqueue(new Callback<GarageDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<GarageDetailResponse> call, @NonNull Response<GarageDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GarageDetailResponse garageDetailResponse = response.body();

                    if (garageDetailResponse.getStatus() == 200 && garageDetailResponse.getData() != null) {
                        result.setValue(Resource.success(garageDetailResponse.getData()));
                    } else {
                        result.setValue(Resource.error(garageDetailResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Lỗi khi tải chi tiết garage", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<GarageDetailResponse> call, @NonNull Throwable t) {
                result.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Tính khoảng cách giữa 2 điểm theo tọa độ GPS (đơn vị: km)
     * Sử dụng công thức Haversine
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Bán kính trái đất (km)

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    /**
     * Sắp xếp danh sách garage theo khoảng cách từ gần đến xa
     */
    public static void sortGaragesByDistance(List<Garage> garages, double userLat, double userLon) {
        // Tính khoảng cách cho mỗi garage
        for (Garage garage : garages) {
            double distance = calculateDistance(
                userLat,
                userLon,
                garage.getLatitudeDouble(),
                garage.getLongitudeDouble()
            );
            garage.setDistance(distance);
        }

        // Sắp xếp theo khoảng cách (từ gần đến xa)
        garages.sort((g1, g2) -> Double.compare(g1.getDistance(), g2.getDistance()));
    }
}
