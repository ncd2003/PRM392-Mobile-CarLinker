package com.example.prm392_mobile_carlinker.ui.garagestaff;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392_mobile_carlinker.data.model.garagestaff.GarageStaffCreateRequest;
import com.example.prm392_mobile_carlinker.data.model.garagestaff.GarageStaffDto;
import com.example.prm392_mobile_carlinker.data.model.garagestaff.GarageStaffUpdateRequest;
import com.example.prm392_mobile_carlinker.data.repository.GarageStaffRepository;
import com.example.prm392_mobile_carlinker.data.repository.Resource;

import java.io.File;
import java.util.List;

public class GarageStaffViewModel extends ViewModel {
    private GarageStaffRepository repository;

    public GarageStaffViewModel() {
        repository = new GarageStaffRepository();
    }

    public LiveData<Resource<List<GarageStaffDto>>> getAllGarageStaff(int page, int size, String sortBy, boolean isAsc) {
        return repository.getAllGarageStaff(page, size, sortBy, isAsc);
    }

    public LiveData<Resource<GarageStaffDto>> getGarageStaffById(int id) {
        return repository.getGarageStaffById(id);
    }

    public LiveData<Resource<GarageStaffDto>> createGarageStaff(GarageStaffCreateRequest request) {
        return repository.createGarageStaff(request);
    }

    public LiveData<Resource<GarageStaffDto>> updateGarageStaff(int id, GarageStaffUpdateRequest request) {
        return repository.updateGarageStaff(id, request);
    }

    public LiveData<Resource<GarageStaffDto>> updateGarageStaffImage(int id, File imageFile) {
        return repository.updateGarageStaffImage(id, imageFile);
    }

    public LiveData<Resource<Boolean>> deleteGarageStaff(int id) {
        return repository.deleteGarageStaff(id);
    }
}

