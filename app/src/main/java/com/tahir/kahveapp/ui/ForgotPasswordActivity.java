package com.tahir.kahveapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tahir.kahveapp.R;
import com.tahir.kahveapp.view_models.AuthViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnReset;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnReset = findViewById(R.id.btn_reset);
        etEmail = findViewById(R.id.et_email);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword(etEmail.getText().toString().trim());
            }
        });
    }

    private void resetPassword(String email){
        authViewModel.resetPassword(email);
        authViewModel.isResetMailSend.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Toast.makeText(ForgotPasswordActivity.this, "Sıfırlama maili, epostanıza gönderildi.Lütfen gelen kutunuzu kontrol edin", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ForgotPasswordActivity.this, "Sıfırlama maili gönderilemedi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}