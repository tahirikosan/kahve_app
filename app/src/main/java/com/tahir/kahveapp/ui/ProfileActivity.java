package com.tahir.kahveapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.tahir.kahveapp.R;
import com.tahir.kahveapp.data.models.User;
import com.tahir.kahveapp.view_models.AuthViewModel;

public class ProfileActivity extends AppCompatActivity {

    public static final String MY_ROSETTE = "MY_ROSETTE";

    private TextView tvName;
    private TextView tvPoint;
    private TextView tvEditName;
    private TextView tvEditEmail;
    private TextView tvEditPassword;
    private TextView tvLogOut;
    private ProgressBar progressBar;
    private ImageView ivSettings;
    private ImageView ivCloseSettings;
    private ImageView ivHelp;
    private CardView cvRosetteMarket;
    private BottomSheetBehavior settingsBottomSheetbBehaviour;
    private RelativeLayout rlSettingsSheet;

    private AuthViewModel authViewModel;

    private int myRosette = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tv_name);
        tvPoint = findViewById(R.id.tv_point);
        tvEditName = findViewById(R.id.tv_edit_name);
        tvEditEmail = findViewById(R.id.tv_edit_email);
        tvEditPassword = findViewById(R.id.tv_edit_password);
        tvLogOut = findViewById(R.id.tv_log_out);
        progressBar = findViewById(R.id.progress_bar);
        ivSettings = findViewById(R.id.iv_settings);
        ivHelp = findViewById(R.id.iv_help);
        cvRosetteMarket = findViewById(R.id.cv_rosette_market);
        ivCloseSettings = findViewById(R.id.iv_close_settings);
        rlSettingsSheet = findViewById(R.id.rl_profile_settings_sheet);

        settingsBottomSheetbBehaviour = BottomSheetBehavior.from(rlSettingsSheet);

        setAuthViewModel();

        getUser();

        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingsPane();
            }
        });

        ivCloseSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSettingsPane();
            }
        });

        tvEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditNameActivity.class);
                startActivity(intent);
            }
        });

        tvEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditPasswordActivity.class);
                startActivity(intent);
            }
        });

        tvEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditEmailActivity.class);
                startActivity(intent);
            }
        });

        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        cvRosetteMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, RosetMarketActivity.class);
                intent.putExtra(MY_ROSETTE, myRosette);
                startActivity(intent);
            }
        });

    }

    private void getUser(){
        authViewModel.setUser();
        authViewModel.userLive.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                if(user.isSuccess()){
                    setUI(user);
                    myRosette = user.getPoint();
                }else{
                    Toast.makeText(ProfileActivity.this, "Kullanıcı verileri alınamadı", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void logOut(){
        authViewModel.logOut();
        authViewModel.logOutUserLive.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user.isLogOut()){
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ProfileActivity.this, "Çıkış yapılamıyor, lütfen tekrar deneyiniz", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAuthViewModel(){
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    private void setUI(User user){
        tvName.setText(user.getName());
        tvPoint.setText(user.getPoint()+"");

        progressBar.setProgress(user.getPoint());
    }

    private void showSettingsPane(){
        settingsBottomSheetbBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        settingsBottomSheetbBehaviour.setHideable(false);
    }
    private void closeSettingsPane(){
        settingsBottomSheetbBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

}