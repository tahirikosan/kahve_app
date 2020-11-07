package com.tahir.kahveapp.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.AuthCredential;
import com.tahir.kahveapp.data.models.User;
import com.tahir.kahveapp.data.repositories.AuthRepository;


public class AuthViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    // google sign in values
    public LiveData<User> authenticatedUser;
    public LiveData<User> createdUserInDB;
    //register with email and password, live user
    public LiveData<User> registeredUser;
    //login with email and password,live user
    public LiveData<User> signedInUser;
    public LiveData<User> logOutUserLive;
    public LiveData<User> userLive;


    public AuthViewModel(@NonNull Application application) {
        super(application);

        authRepository = new AuthRepository(application);
    }

    public void signInWithGoogle(AuthCredential authCredential){
        authenticatedUser = authRepository.signInWithGoogle(authCredential);
    }

    public void createUserInDB(User authenticatedUser){
        createdUserInDB = authRepository.createUserInDb(authenticatedUser);
    }

    public void registerUser(String email, String password, String name){
        registeredUser = authRepository.registerUser(email, password, name);
    }

    public void loginUser(String email, String password){
        signedInUser = authRepository.loginUser(email, password);
    }

    public void setUser(){
        userLive = authRepository.getUser();
    }

    public void logOut(){
        logOutUserLive = authRepository.logOut();
    }


}
