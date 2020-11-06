package com.tahir.kahveapp.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.easing.linear.Linear;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tahir.kahveapp.R;
import com.tahir.kahveapp.data.models.Menu;
import com.tahir.kahveapp.data.models.Order;
import com.tahir.kahveapp.ui.adapters.MenuAdapter;
import com.tahir.kahveapp.view_models.OrderViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MenuAdapter.OnItemClickListener{

    private Button btnAdd;
    private ImageView ivOpenMenu;
    private ImageView ivCloseMenu;
    private Button btnQrRead;
    private RelativeLayout qrCodePane;
    private RelativeLayout ordersPane;
    private CardView menuPane;

    private RecyclerView rvMenuItems;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    private MenuAdapter menuAdapter;

    private String tableID;

    private OrderViewModel orderViewModel;

    private List<Menu> myMenuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnQrRead = findViewById(R.id.btn_qr_read);
        btnAdd = findViewById(R.id.btn_add);
        qrCodePane = findViewById(R.id.qr_code_pane);
        ordersPane = findViewById(R.id.orders_pane);
        menuPane = findViewById(R.id.menu_pane);
        ivOpenMenu = findViewById(R.id.iv_open_menu);
        ivCloseMenu = findViewById(R.id.iv_close_menu);
        rvMenuItems = findViewById(R.id.rv_menu_items);
        rvMenuItems.setLayoutManager(linearLayoutManager);
        rvMenuItems.setHasFixedSize(true);

        setOrderViewModel();

        //gets live menu list from db
        getMenu();

        btnQrRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readQr();
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

                closeQrPane();
                openOrdersPane();

            }else{
                Toast.makeText(this, "Geçersiz QR Kod, lütfen geçerli bir kod tarayınız.", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

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

    private void setMenuAdapter(List<Menu> menuList){
        menuAdapter = new MenuAdapter(menuList);
        menuAdapter.setOnItemClickListener(this);
        rvMenuItems.setAdapter(menuAdapter);
    }

    private void setOrderViewModel(){
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
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
        }else{
            cvItem.setCardBackgroundColor(getResources().getColor(R.color.white));
            myMenuList.get(position).setChoosen(false);
        }

    }
}