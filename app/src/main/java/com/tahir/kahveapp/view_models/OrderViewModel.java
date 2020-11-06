package com.tahir.kahveapp.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tahir.kahveapp.data.models.Menu;
import com.tahir.kahveapp.data.repositories.OrderRepository;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {

    private OrderRepository repository;
    public LiveData<List<Menu>> menuLive;

    public OrderViewModel(@NonNull Application application) {
        super(application);

        repository = new OrderRepository();
    }

    public void setMenuLive(){
        menuLive = repository.getMenu();
    }


}
