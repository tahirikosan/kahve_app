package com.tahir.kahveapp.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.easing.linear.Linear;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tahir.kahveapp.R;
import com.tahir.kahveapp.data.models.Menu;
import com.tahir.kahveapp.data.models.Order;
import com.tahir.kahveapp.data.models.User;
import com.tahir.kahveapp.data.models.Value;
import com.tahir.kahveapp.ui.adapters.MenuAdapter;
import com.tahir.kahveapp.ui.adapters.OrderAdapter;
import com.tahir.kahveapp.utils.SharedPrefData;
import com.tahir.kahveapp.view_models.AuthViewModel;
import com.tahir.kahveapp.view_models.OrderViewModel;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MenuAdapter.OnItemClickListener{

    private Button btnAdd;
    private ImageView ivOpenMenu;
    private ImageView ivCloseMenu;
    private ImageView ivGotoProfile;
    private Button btnQrRead;
    private RelativeLayout qrCodePane;
    private RelativeLayout ordersPane;
    private CardView menuPane;
    private TextView tvTotalPrice;
    private TextView tvDiscountPrice;

    private LinearLayoutManager linearLayoutManagerMenu = new LinearLayoutManager(this);
    private RecyclerView rvMenuItems;
    private MenuAdapter menuAdapter;

    private RecyclerView rvOrderItems;
    private LinearLayoutManager linearLayoutManagerOrder = new LinearLayoutManager(this);
    private OrderAdapter orderAdapter;

    private String tableID = "jOjkS1hTl49vIea5UBTL";

    private AuthViewModel authViewModel;
    private OrderViewModel orderViewModel;

    private List<Menu> myMenuList;
    private List<Menu> chosenMenuItems = new ArrayList<>();
    private List<Order> myOrderList = new ArrayList<>();
    private List<Order> addOrderList;
    private List<String> orderIDList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTotalPrice = findViewById(R.id.tv_total_price);
        tvDiscountPrice = findViewById(R.id.tv_discount_price);
        btnQrRead = findViewById(R.id.btn_qr_read);
        btnAdd = findViewById(R.id.btn_add);
        qrCodePane = findViewById(R.id.qr_code_pane);
        ordersPane = findViewById(R.id.orders_pane);
        menuPane = findViewById(R.id.menu_pane);
        ivOpenMenu = findViewById(R.id.iv_open_menu);
        ivGotoProfile = findViewById(R.id.iv_goto_profile);
        ivCloseMenu = findViewById(R.id.iv_close_menu);
        rvMenuItems = findViewById(R.id.rv_menu_items);
        rvMenuItems.setLayoutManager(linearLayoutManagerMenu);
        rvMenuItems.setHasFixedSize(true);
        rvOrderItems = findViewById(R.id.rv_my_orders);
        rvOrderItems.setLayoutManager(linearLayoutManagerOrder);
        rvOrderItems.setHasFixedSize(true);

        setOrderViewModel();
        initAuthViewModel();

        showInfo();

        //gets live menu list from view model
        getMenu();

        //gets live order list from view model
        getOrder(tableID);

        btnQrRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readQr();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrder(chosenMenuItems, tableID);
                closeMenuPane();
                clearChosen();
            }
        });

        ivOpenMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuPane();
            }
        });

        ivCloseMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenuPane();
            }
        });

        ivGotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    //checks if user already taken a table
    public void takeTable(String tableID){
        orderViewModel.takeTable(tableID);
        orderViewModel.tookTableLive.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    SharedPrefData sharedPrefData = new SharedPrefData(MainActivity.this);
                    sharedPrefData.saveTableID(tableID);

                    closeQrPane();
                    openOrdersPane();
                }else{
                    Toast.makeText(MainActivity.this, "Masa kayıt işlemi yapılamadı", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //checks if user already taken a table
    public void checkTable(){
        orderViewModel.checkTable();
        orderViewModel.haveTableLive.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    openOrdersPane();
                    closeQrPane();
                }else{
                    openQrPane();
                    closeOrdersPane();
                }
            }
        });
    }

    private void showInfo(){
        SharedPrefData sharedPrefData = new SharedPrefData(this);
        boolean isFirstOpen = sharedPrefData.loadShowInfo();

        if (!isFirstOpen) {
            sharedPrefData.saveShowInfo();
            Intent intent = new Intent(MainActivity.this, InfoActivity.class);
            startActivity(intent);
        }
    }


    private void readQr(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Kod Taranıyor..");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if(result.getContents() != null){
                tableID = result.getContents();

                checkTable();

                takeTable(tableID);
                //gets live order list from view model
                getOrder(tableID);
            }else{
                Toast.makeText(this, "Geçersiz QR Kod, lütfen geçerli bir kod tarayınız.", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    ///get menu list from viewmodel
    private void getMenu(){
        orderViewModel.setMenuLive();
        orderViewModel.menuLive.observe(this, new Observer<List<Menu>>() {
            @Override
            public void onChanged(List<Menu> menuList) {
                if(menuList.get(0).isSucces()){
                    myMenuList = menuList;

                    setMenuAdapter(menuList);
                }else{
                    Toast.makeText(MainActivity.this, menuList.get(0).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    ///get order list from viewmodel
    private void getOrder(String tableID){
        orderViewModel.setOrderLive(tableID);
        orderViewModel.orderLive.observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orderList) {
                if(orderList.get(0).isSucces()){
                    myOrderList = orderList;

                    setOrderAdapter(orderList);

                    setPrices(myOrderList);
                }else{
                    Toast.makeText(MainActivity.this, orderList.get(0).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    ///add new orders to db
    private void addOrder(List<Menu> chosenMenuItems, String tableID){
        orderViewModel.setAddOrderLive(chosenMenuItems, tableID);
        orderViewModel.addOrderLive.observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                if(orders.get(0).isSucces()){
                    addOrderList = orders;
                }else{
                    Toast.makeText(MainActivity.this, orders.get(0).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setMenuAdapter(List<Menu> menuList){
        menuAdapter = new MenuAdapter(menuList);
        menuAdapter.setOnItemClickListener(this);
        rvMenuItems.setAdapter(menuAdapter);
    }

    private void setOrderAdapter(List<Order> orderList){
        orderAdapter = new OrderAdapter(orderList, this);
        rvOrderItems.setAdapter(orderAdapter);
    }

    private void setOrderViewModel(){
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
    }

    private void initAuthViewModel(){
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    private void openOrdersPane(){
        ordersPane.setVisibility(View.VISIBLE);
    }

    private void closeOrdersPane(){
        ordersPane.setVisibility(View.GONE);
    }

    private void openQrPane(){
        qrCodePane.setVisibility(View.VISIBLE);
    }

    private void closeQrPane(){
        qrCodePane.setVisibility(View.GONE);
    }

    private void openMenuPane(){
        menuPane.setVisibility(View.VISIBLE);
    }

    private void closeMenuPane(){
        menuPane.setVisibility(View.GONE);
    }

    @Override
    public void onClickMenuItem(int position, CardView cvItem) {

        if(!myMenuList.get(position).isChoosen()){
            cvItem.setCardBackgroundColor(getResources().getColor(R.color.colorTransparentGreen));
            myMenuList.get(position).setChoosen(true);
            chosenMenuItems.add(myMenuList.get(position));
        }else{
            cvItem.setCardBackgroundColor(getResources().getColor(R.color.colorLowTransparentWhite));
            myMenuList.get(position).setChoosen(false);
            chosenMenuItems.remove(myMenuList.get(position));
        }
    }


    //clear all chosen values
    private void clearChosen(){
        for(int i = 0; i < myMenuList.size(); i++){
            myMenuList.get(i).setChoosen(false);
        }
        chosenMenuItems.clear();
        setMenuAdapter(myMenuList);
    }



    //calculate prices and set UI
    private void setPrices(List<Order> myOrderList){

        authViewModel.setUser();
        authViewModel.userLive.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.isSuccess()) {

                    //get fresh system values
                    orderViewModel.setValueLive();
                    orderViewModel.valueLive.observe(MainActivity.this, new Observer<Value>() {
                        @Override
                        public void onChanged(Value value) {

                            int point = user.getPoint();
                            float discount = 0;
                            float totalPrice = 0;

                            for(Order order : myOrderList){
                                totalPrice += order.getOrderPrice();
                            }

                            if(totalPrice >= 50){
                                discount += totalPrice * value.getDiscountPercent();
                            }
                            discount += value.getStaticDiscount();

                            //control max discount amount
                            if(discount >= value.getMaxDiscount()){
                                discount = value.getMaxDiscount();
                            }


                            String totalPriceStr = String.format("%.2f", (float)totalPrice);
                            String discountStr = String.format("%.2f", (float)discount);
                            tvTotalPrice.setText("Ücret: "+ totalPriceStr +" ₺");
                            tvDiscountPrice.setText("İndirim: "+discountStr+" ₺");
                        }
                    });


                }else{
                    Toast.makeText(MainActivity.this, "Indirim hesaplanamadı.", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}