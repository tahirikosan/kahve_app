package com.tahir.kahveapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.tahir.kahveapp.R;
import com.tahir.kahveapp.data.models.User;
import com.tahir.kahveapp.view_models.SplashViewModel;

public class SplashActivity extends AppCompatActivity {

    //public static final String USER = "USER";
    private SplashViewModel splashViewModel;
    private ImageView ivSplashBee;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivSplashBee = findViewById(R.id.iv_splash_bee);

        YoYo.with(Techniques.SlideInUp)
                .duration(1000)
                .repeat(5)
                .playOn(ivSplashBee);

        initSplashViewModel();
        checkIfUserAuth();

    }

    private void initSplashViewModel(){
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
    }

    private void checkIfUserAuth(){
        splashViewModel.checkIfUserAuth();
        splashViewModel.isUserAuth.observe(this, user -> {
            if(!user.isAuthenticated()){
                // user not auth, then go to login to do auth
                goToLoginActivity();
            }else{
                getUserDataFromDb(user.getId());
            }
        });
    }


    private void getUserDataFromDb(String uid){
        splashViewModel.getUser(uid);
        splashViewModel.userWithData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                goToMainActivity(user);
            }
        });
    }

    private void goToLoginActivity(){
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMainActivity(User user){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
