package com.example.prm392_mobile_carlinker.ui.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392_mobile_carlinker.data.model.garage.GarageDetail;
import com.example.prm392_mobile_carlinker.data.model.servicerecord.ServiceRecordResponse;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleResponse;
import com.example.prm392_mobile_carlinker.data.repository.GarageRepository;
import com.example.prm392_mobile_carlinker.data.repository.Resource;
import com.example.prm392_mobile_carlinker.data.repository.Result;
import com.example.prm392_mobile_carlinker.data.repository.ServiceRecordRepository;
import com.example.prm392_mobile_carlinker.data.repository.VehicleRepository;

import java.util.List;

public class ServiceBookingViewModel extends ViewModel {
    private VehicleRepository vehicleRepository;
    private GarageRepository garageRepository;
    private ServiceRecordRepository serviceRecordRepository;

    public ServiceBookingViewModel() {
        vehicleRepository = new VehicleRepository();
        garageRepository = new GarageRepository();
        serviceRecordRepository = new ServiceRecordRepository();
    }

    public LiveData<Resource<List<VehicleResponse.Data>>> getAllVehicles() {
        return vehicleRepository.getAllVehicles();
    }

    public LiveData<Resource<GarageDetail>> getGarageDetails(int garageId) {
        return garageRepository.getGarageDetails(garageId);
    }

    /**
     * Tạo service record mới (đặt lịch dịch vụ)
     *
     * @param garageId ID của garage
     * @param vehicleId ID của vehicle
     * @param serviceItemIds Danh sách ID của các service items
     * @return LiveData chứa kết quả
     */
    public LiveData<Result<ServiceRecordResponse>> createServiceRecord(
            int garageId,
            int vehicleId,
            List<Integer> serviceItemIds) {
        return serviceRecordRepository.createServiceRecord(garageId, vehicleId, serviceItemIds);
    }
}
