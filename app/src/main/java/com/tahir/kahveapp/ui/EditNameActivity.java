package com.tahir.kahveapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tahir.kahveapp.R;
import com.tahir.kahveapp.data.models.User;
import com.tahir.kahveapp.view_models.ProfileViewModel;

public class EditNameActivity extends AppCompatActivity {

    private EditText etNewName;
    private Button btnDone;

    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        etNewName = findViewById(R.id.et_new_name);
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

        String currentPassword = etNewName.getText().toString().trim();

        if(validateInputs(currentPassword)){
            profileViewModel.editName(currentPassword);
            profileViewModel.editNameLive.observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user.isSuccess()) {
                        Toast.makeText(EditNameActivity.this, "Kullanıcı Adı değiştirildi", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(EditNameActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(EditNameActivity.this, "Kullanıcı Adı değiştirilemedi, lütfen tekrar deneyiniz", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            });
        }

    }

    private boolean validateInputs(String newName){

        if(!newName.isEmpty()){
            return true;
        }else{
            Toast.makeText(this, "Lütfen bütün alanları doldurunuz", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}