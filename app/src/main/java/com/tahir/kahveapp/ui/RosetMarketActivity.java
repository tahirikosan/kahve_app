package com.tahir.kahveapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tahir.kahveapp.R;
import com.tahir.kahveapp.view_models.OrderViewModel;

public class RosetMarketActivity extends AppCompatActivity {

    public static final String MY_ROSETTE = "MY_ROSETTE";

    private Button btnBuyCoffee;
    private Button btnBuyMilkShake;
    private Button btnBuyTea;
    private Button btnBuyJuice;
    private Button btnBuyGift;
    private Button btnBuyVip;
    private Button btnBuy;
    private TextView tvBuyTitle;
    private TextView tvRosette;
    private ImageView ivCloseBuyTab;
    private CardView cvBuyAlert;

    private int coffePrice = 500;
    private int milkShakePrice = 500;
    private int teaPrice = 500;
    private int juicePrice = 500;
    private int giftPrice = 2500;
    private int vipPrice = 5000;

    private String buyType;
    private int price;

    private int myRosette;

    private OrderViewModel orderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roset_market);


        btnBuyCoffee = findViewById(R.id.btn_buy_coffe);
        btnBuyMilkShake = findViewById(R.id.btn_buy_milkshake);
        btnBuyJuice = findViewById(R.id.btn_buy_juice);
        btnBuyTea = findViewById(R.id.btn_buy_tea);
        btnBuyGift = findViewById(R.id.btn_buy_gift);
        btnBuyVip = findViewById(R.id.btn_buy_vip);
        btnBuy = findViewById(R.id.btn_buy);
        ivCloseBuyTab = findViewById(R.id.iv_close_alert);
        tvBuyTitle = findViewById(R.id.tv_buy_title);
        tvRosette = findViewById(R.id.tv_rosette);
        cvBuyAlert = findViewById(R.id.cv_buy_alert);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        Intent intent = getIntent();
        myRosette = intent.getIntExtra("MY_ROSETTE", 0);
        tvRosette.setText(myRosette+"");

        btnBuyCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBuyCoffe();
            }
        });

        btnBuyGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBuyGift();
            }
        });

        btnBuyTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBuyTea();
            }
        });

        btnBuyJuice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBuyJuice();
            }
        });

        btnBuyMilkShake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBuyMilkShake();
            }
        });

        btnBuyVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBuyVip();
            }
        });

        ivCloseBuyTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBuyAlert();
            }
        });

        ivCloseBuyTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBuyAlert();
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMyRosetteUI();
                buyItem(buyType, myRosette);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(RosetMarketActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void buyItem(String buyType, int myRosette){
        orderViewModel.setRosetteOrderStatus(buyType, myRosette);
        orderViewModel.rosetteOrderStatus.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Toast.makeText(RosetMarketActivity.this, "Satın alma işlemi başarılı", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RosetMarketActivity.this, "Satın alma işlemi gerçekleştirilemedi", Toast.LENGTH_SHORT).show();
                }
                closeBuyAlert();
            }
        });
    }


    private void openBuyAlert(){
        cvBuyAlert.setVisibility(View.VISIBLE);
    }

    private void closeBuyAlert(){
        cvBuyAlert.setVisibility(View.GONE);
    }



    private void setBuyCoffe(){
        if(myRosette >= coffePrice){
            tvBuyTitle.setText("500 Rozet karşılığında 2 kahve almak üzeresiniz.");
            buyType = "coffee";
            myRosette -= coffePrice;

            openBuyAlert();
        }else{
            Toast.makeText(this, "Yeterli miktarda rozetiniz yok.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setBuyTea(){
        if(myRosette >= teaPrice){
            tvBuyTitle.setText("500 Rozet karşılığında 2 çay almak üzeresiniz.");
            buyType = "tea";
            myRosette -= teaPrice;
            openBuyAlert();
        }else{
            Toast.makeText(this, "Yeterli miktarda rozetiniz yok.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setBuyMilkShake(){
        if(myRosette >= milkShakePrice){
            tvBuyTitle.setText("500 Rozet karşılığında 2 milk shake almak üzeresiniz.");
            buyType = "milk shake";
            myRosette -= milkShakePrice;
            openBuyAlert();
        }else{
            Toast.makeText(this, "Yeterli miktarda rozetiniz yok.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setBuyJuice(){
        if(myRosette >= juicePrice){
            tvBuyTitle.setText("500 Rozet karşılığında 2 meyve suyu almak üzeresiniz.");
            buyType = "juice";
            myRosette -= juicePrice;
            openBuyAlert();
        }else{
            Toast.makeText(this, "Yeterli miktarda rozetiniz yok.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setBuyGift(){
        if(myRosette >= giftPrice){
            tvBuyTitle.setText("2500 Rozet karşılığında sürpriz hediye almak üzeresiniz.");
            buyType = "gift";
            myRosette -= giftPrice;
            openBuyAlert();
        }else{
            Toast.makeText(this, "Yeterli miktarda rozetiniz yok.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setBuyVip(){
        if(myRosette >= vipPrice){
            tvBuyTitle.setText("5000 Rozet karşılığında VIP Arması almak üzeresiniz.");
            buyType = "vip";
            myRosette -= vipPrice;
            openBuyAlert();
        }else{
            Toast.makeText(this, "Yeterli miktarda rozetiniz yok.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setMyRosetteUI(){
        tvRosette.setText(myRosette + "");
    }

}