package com.tahir.kahveapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tahir.kahveapp.R;
import com.tahir.kahveapp.data.models.User;
import com.tahir.kahveapp.view_models.ProfileViewModel;

public class EditPasswordActivity extends AppCompatActivity {

    private EditText etCurrentPassword;
    private EditText etNewPassword;
    private Button btnDone;

    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        etCurrentPassword = findViewById(R.id.et_current_password);
        etNewPassword = findViewById(R.id.et_new_password);
        btnDone = findViewById(R.id.btn_done);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPassword();
            }
        });

    }


    //şifre değiştir
    private void editPassword(){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newEmail = etNewPassword.getText().toString().trim();

        if(validateInputs(currentPassword , newEmail)){
            profileViewModel.editPassword(currentPassword, newEmail);
            profileViewModel.editPasswordLive.observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user.isSuccess()) {
                        Toast.makeText(EditPasswordActivity.this, "Şifre değiştirildi", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(EditPasswordActivity.this, "Şifre değiştirilemedi, lütfen tekrar deneyiniz", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            });
        }

    }

    private boolean validateInputs(String currentPassword, String newEmail){

        if(!currentPassword.isEmpty() && !newEmail.isEmpty()){
            return true;
        }else{
            Toast.makeText(this, "Lütfen bütün alanları doldurunuz", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}