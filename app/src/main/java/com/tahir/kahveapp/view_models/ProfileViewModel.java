package com.tahir.kahveapp.view_models;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tahir.kahveapp.data.models.User;
import com.tahir.kahveapp.data.repositories.ProfileRepository;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {
    
    public LiveData<User> editNameLive;
    public LiveData<User> editPasswordLive;
    public LiveData<User> editEmailLive;

    private ProfileRepository repository;


    public ProfileViewModel(@NonNull Application application) {
        super(application);

        repository = new ProfileRepository(application);
    }


    public void editName(String newName){
        editNameLive = repository.editName(newName);
    }

    public void editPassword(String currentPassword ,String newPassword){
        editPasswordLive = repository.editPassword(currentPassword, newPassword);
    }
    public void editEmail(String currentPassword ,String newEmail){
        editEmailLive = repository.editEmail(currentPassword, newEmail);
    }

}
