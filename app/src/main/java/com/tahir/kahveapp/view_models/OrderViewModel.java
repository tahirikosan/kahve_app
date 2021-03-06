package com.tahir.kahveapp.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tahir.kahveapp.data.models.Menu;
import com.tahir.kahveapp.data.models.Order;
import com.tahir.kahveapp.data.models.Value;
import com.tahir.kahveapp.data.repositories.OrderRepository;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {

    private OrderRepository repository;
    public LiveData<List<Menu>> menuLive;
    public LiveData<List<Order>> addOrderLive;
    public LiveData<List<Order>> orderLive;
    public LiveData<List<String>> orderIDsLive;
    public LiveData<Value> valueLive;
    public LiveData<Boolean> rosetteOrderStatus;
    public LiveData<Boolean> haveTableLive;
    public LiveData<Boolean> tookTableLive;

    public OrderViewModel(@NonNull Application application) {
        super(application);

        repository = new OrderRepository(application);
    }

    //take table
    public void takeTable(String tableID){
        tookTableLive = repository.takeTable(tableID);
    }


    //check if table already taken
    public void checkTable(){
        haveTableLive = repository.checkTable();
    }

    // buy stuffs with rosette
    public void setRosetteOrderStatus(String buyType, int myRosette){
        rosetteOrderStatus = repository.buyWithRosette(buyType, myRosette);
    }

    //sets system values
    public void setValueLive(){
        valueLive = repository.getSystemValue();
    }

    //sets order id list  from repository
    public void setOrderIDsLive(String tableID){
        orderIDsLive = repository.getOrderIDs(tableID);
    }

    //sets order list  from repository
    public void setOrderLive(String tableID){
        orderLive = repository.getOrders(tableID);
    }

    //sets menu list  from repository
    public void setMenuLive(){
        menuLive = repository.getMenu();
    }

    //sets new order list  from repository
    public void setAddOrderLive(List<Menu> chosenMenuItems, String tableID){
        addOrderLive = repository.addOrder(chosenMenuItems, tableID);
    }



}
