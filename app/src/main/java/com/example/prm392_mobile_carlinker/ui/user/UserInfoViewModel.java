package com.example.prm392_mobile_carlinker.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392_mobile_carlinker.data.model.user.User;
import com.example.prm392_mobile_carlinker.data.repository.Resource;
import com.example.prm392_mobile_carlinker.data.repository.UserRepository;

public class UserInfoViewModel extends ViewModel {
    private UserRepository repository;

    public UserInfoViewModel() {
        repository = new UserRepository();
    }

    public LiveData<Resource<User>> getUserById(int id) {
        return repository.getUserById(id);
    }
}

