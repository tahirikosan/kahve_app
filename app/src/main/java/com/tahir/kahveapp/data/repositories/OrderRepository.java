package com.tahir.kahveapp.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.tahir.kahveapp.data.models.Menu;
import com.tahir.kahveapp.data.models.Order;
import com.tahir.kahveapp.data.models.Value;
import com.tahir.kahveapp.utils.SharedPrefData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser authUser;

    public OrderRepository() {

        mAuth = FirebaseAuth.getInstance();
        authUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }


    //add new order to db
    public MutableLiveData<List<Order>> addOrder(List<Menu> chosenMenuItems, String tableID){
        MutableLiveData<List<Order>> orderListLive = new MutableLiveData<>();

        List<Order> orderList = new ArrayList<>();

        for(Menu menuItem : chosenMenuItems){
            DocumentReference newOrderReference = db.collection("orders").document();

            Map<String, Object> newOrderMap = new HashMap<>();
            newOrderMap.put("tableID", tableID);
            newOrderMap.put("orderID", newOrderReference.getId());
            newOrderMap.put("customerID", authUser.getUid());
            newOrderMap.put("orderName", menuItem.getItemName());
            newOrderMap.put("orderPrice", menuItem.getItemPrice());
            newOrderMap.put("orderImageUrl", menuItem.getItemImageUrl());
            newOrderMap.put("orderStatus", "prepare");

            newOrderReference.set(newOrderMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            //add order id to table collection
                            db.collection("tables")
                                    .document(tableID)
                                    .update("orderIDs", FieldValue.arrayUnion(newOrderReference.getId()),
                                        "tableOwnerID", authUser.getUid())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Order newOrder = menuToOrder(menuItem, tableID);
                                            newOrder.setSucces(true);
                                            newOrder.setOrderID(newOrderReference.getId());
                                            orderList.add(newOrder);
                                            orderListLive.setValue(orderList);
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Order ADD ERROR : ", "Error writing document", e);
                            Order newOrder = new Order();
                            newOrder.setSucces(false);
                            newOrder.setMessage("Sipariş eklenemedi, lütfen personel ile iletişime geçiniz.");
                            orderList.add(newOrder);
                            orderListLive.setValue(orderList);
                        }
                    });
        }

        return orderListLive;
    }

    //Get orders according to table id
    public MutableLiveData<List<String>> getOrderIDs(String tableID){
        MutableLiveData<List<String>> orderIDsLive = new MutableLiveData<>();

        db.collection("tables")
                .document(tableID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){

                            List<String> orderIDs = (List)task.getResult().get("orderIDs");
                            orderIDsLive.setValue(orderIDs);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ERROR GET ORDERS IDs" , e.getMessage());
            }
        });

        return orderIDsLive;
     }


    //Get orders according to table id
    public MutableLiveData<List<Order>> getOrders(String tableID){
        MutableLiveData<List<Order>> ordersLive = new MutableLiveData<>();


        //Order for error handling
        Order orderItem = new Order();

                if (tableID != null && !tableID.isEmpty()) {
                    //If there is no error then get orders
                    db.collection("orders")
                            .whereEqualTo("tableID", tableID)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                    List<Order> orderList = new ArrayList<>();

                                    if (value != null && !value.isEmpty()) {
                                        for(DocumentSnapshot childSnapshot: value.getDocuments()){
                                            Order orderItem = childSnapshot.toObject(Order.class);
                                            orderItem.setSucces(true);
                                            orderList.add(orderItem);
                                        }

                                        ordersLive.setValue(orderList);
                                    }

                                    if(error != null){
                                        orderItem.setSucces(false);
                                        orderItem.setMessage("Bu masada henüz sipariş verilmemiş.");
                                        orderList.add(orderItem);
                                    }

                                }
                            });
                        }

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

    public MutableLiveData<Value> getSystemValue(){
        MutableLiveData<Value> valueLive  = new MutableLiveData<>();

        db.collection("values")
                .document("tRPQcwNvwjSCclW29OYx")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Value value = documentSnapshot.toObject(Value.class);
                        valueLive.setValue(value);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("System values" , e.getMessage());
            }
        });

        return valueLive;
    }


    //Convert menu item to Order item
    private Order menuToOrder(Menu menuItem, String tableID){
        Order order = new Order(tableID,
                authUser.getUid(),
                menuItem.getItemName(),
                menuItem.getItemImageUrl(),
                menuItem.getItemPrice()
                , "prepare"
        );
        return order;
    }

}
