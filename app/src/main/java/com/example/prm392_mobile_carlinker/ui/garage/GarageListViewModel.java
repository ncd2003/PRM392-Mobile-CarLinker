package com.example.prm392_mobile_carlinker.ui.garage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392_mobile_carlinker.data.model.garage.Garage;
import com.example.prm392_mobile_carlinker.data.repository.GarageRepository;
import com.example.prm392_mobile_carlinker.data.repository.Resource;

import java.util.List;
import java.util.Locale;

/**
 * ViewModel quản lý dữ liệu danh sách garage
 */
public class GarageListViewModel extends ViewModel {
    private final GarageRepository garageRepository;
    private final MediatorLiveData<Resource<List<Garage>>> garagesLiveData;
    private final MutableLiveData<String> userLocationLiveData;

    public GarageListViewModel() {
        garageRepository = new GarageRepository();
        garagesLiveData = new MediatorLiveData<>();
        userLocationLiveData = new MutableLiveData<>();
    }

    /**
     * Lấy danh sách garage từ API
     */
    public void loadGarages() {
        LiveData<Resource<List<Garage>>> source = garageRepository.getAllGarages();
        garagesLiveData.addSource(source, resource -> {
            garagesLiveData.setValue(resource);
            // Remove source sau khi nhận được data để tránh observe mãi mãi
            if (resource != null && (resource.getStatus() == Resource.Status.SUCCESS || resource.getStatus() == Resource.Status.ERROR)) {
                garagesLiveData.removeSource(source);
            }
        });
    }

    /**
     * Sắp xếp danh sách garage theo khoảng cách từ vị trí người dùng
     */
    public void sortGaragesByUserLocation(double userLat, double userLon) {
        Resource<List<Garage>> currentResource = garagesLiveData.getValue();

        if (currentResource != null && currentResource.getData() != null) {
            List<Garage> garages = currentResource.getData();
            GarageRepository.sortGaragesByDistance(garages, userLat, userLon);
            garagesLiveData.setValue(Resource.success(garages));

            // Cập nhật thông tin vị trí người dùng
            userLocationLiveData.setValue(String.format(Locale.getDefault(),
                "📍 Vị trí của bạn: %.6f, %.6f", userLat, userLon));
        }
    }

    public LiveData<Resource<List<Garage>>> getGaragesLiveData() {
        return garagesLiveData;
    }

    public LiveData<String> getUserLocationLiveData() {
        return userLocationLiveData;
    }
}
