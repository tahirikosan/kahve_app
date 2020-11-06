package com.tahir.kahveapp.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tahir.kahveapp.data.models.Menu;
import com.tahir.kahveapp.data.models.Order;
import com.tahir.kahveapp.utils.SharedPrefData;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public OrderRepository() {

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
    }


    //Get orders according to table id
    public MutableLiveData<List<Order>> getOrders(String tableID){
        MutableLiveData<List<Order>> ordersLive = new MutableLiveData<>();

        List<Order> orderList = new ArrayList<>();

        //Order for error handling
        Order order = new Order();

        db.collection("tables")
                .document(tableID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){

                            List<String> orderIDs = (List)task.getResult().get("orderIDs");
                            if (orderIDs != null) {
                                if(orderIDs.size() != 0){
                                    order.setSucces(false);
                                    order.setMessage("Bu masa dolu");
                                }else{
                                    //If there is no error

                                }
                            }else{
                                order.setSucces(false);
                                order.setMessage("Beklenmedik bir hata oluştu, lütfen personel ile iletişime geçiniz.");
                            }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ERROR GET ORDERS" , e.getMessage());
            }
        });

        return  ordersLive;
     }


     //Gets menu items from db
    public MutableLiveData<List<Menu>> getMenu(){
        MutableLiveData<List<Menu>> menuItemsLive = new MutableLiveData<>();

        List<Menu> menuItems = new ArrayList<>();

        //Order for error handling
        Menu menuItem = new Menu();

        db.collection("menu")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot childSnapshot: task.getResult().getDocuments()){
                                Menu menu = childSnapshot.toObject(Menu.class);
                                menu.setSucces(true);
                                menuItems.add(menu);
                            }
                        }else{
                            menuItem.setSucces(false);
                            menuItem.setMessage("Menu bulunamadı");
                            menuItems.add(menuItem);
                        }
                        menuItemsLive.setValue(menuItems);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ERROR GET MENU" , e.getMessage());
                menuItem.setSucces(false);
                menuItem.setMessage("Beklenmedik bir hata oluştu, lütfen personel ile iletişime geçiniz.");
                menuItems.add(menuItem);
                menuItemsLive.setValue(menuItems);
            }
        });

        return  menuItemsLive;
    }

}
