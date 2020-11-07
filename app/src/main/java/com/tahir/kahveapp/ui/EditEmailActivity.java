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

public class EditEmailActivity extends AppCompatActivity {

    private EditText etCurrentPassword;
    private EditText etNewEmail;
    private Button btnDone;

    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        etCurrentPassword = findViewById(R.id.et_current_password);
        etNewEmail = findViewById(R.id.et_new_email);
        btnDone = findViewById(R.id.btn_done);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeEmail();
            }
        });

    }


    //email değiştir
    private void changeEmail(){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newEmail = etNewEmail.getText().toString().trim();

        if(validateInputs(currentPassword , newEmail)){
            profileViewModel.editEmail(currentPassword, newEmail);
            profileViewModel.editEmailLive.observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user.isSuccess()) {
                        Toast.makeText(EditEmailActivity.this, "Email değiştirildi", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(EditEmailActivity.this, "Email değiştirilemedi, lütfen tekrar deneyiniz", Toast.LENGTH_SHORT).show();
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