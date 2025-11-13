package com.example.prm392_mobile_carlinker.ui.garage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392_mobile_carlinker.data.model.garage.GarageDetail;
import com.example.prm392_mobile_carlinker.data.repository.GarageRepository;
import com.example.prm392_mobile_carlinker.data.repository.Resource;

/**
 * ViewModel for Garage Detail Screen
 */
public class GarageDetailViewModel extends ViewModel {
    private final GarageRepository garageRepository;

    public GarageDetailViewModel() {
        this.garageRepository = new GarageRepository();
    }

    /**
     * Load garage details by ID
     */
    public LiveData<Resource<GarageDetail>> loadGarageDetails(int garageId) {
        return garageRepository.getGarageDetails(garageId);
    }
}

