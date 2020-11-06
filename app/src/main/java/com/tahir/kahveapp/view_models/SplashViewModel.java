package com.tahir.kahveapp.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tahir.kahveapp.data.models.User;
import com.tahir.kahveapp.data.repositories.SplashRepository;


public class SplashViewModel extends AndroidViewModel {

    private SplashRepository repository;
    public LiveData<User> isUserAuth;
    public LiveData<User> userWithData;


    public SplashViewModel(@NonNull Application application) {
        super(application);

        repository = new SplashRepository(application);
    }

    // check if user authenticated
    public void checkIfUserAuth(){
        isUserAuth = repository.checkUserAuth();
    }

    public void getUser(String uid){
        userWithData = repository.getUserData(uid);
    }

}
