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

import com.google.android.material.textfield.TextInputLayout;
import com.tahir.kahveapp.R;
import com.tahir.kahveapp.data.models.User;
import com.tahir.kahveapp.view_models.AuthViewModel;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    public static final Pattern GMAIL_ADDRESS
            = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "gmail" +
                    "(" +
                    "\\." +
                    "com" +
                    ")+"
    );

    public static final Pattern HOTMAIL_ADDRESS
            = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "hotmail" +
                    "(" +
                    "\\." +
                    "com" +
                    ")+"
    );

    public static final Pattern OUTLOOK_ADDRESS
            = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "outlook" +
                    "(" +
                    "\\." +
                    "com" +
                    ")+"
    );

    public static final Pattern PASSWORD = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}");

    private TextInputLayout TI_email, TI_password;

    private EditText etEmail;
    private EditText etPassword;
    private EditText etName;
    private Button btnRegister;

    private String email;
    private String password;
    private String name;

    private AuthViewModel authViewModel;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setUIComponents();

        initAuthViewModel();

       /* //set view model
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.authListener = this;
*/

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set text
                setTexts();

                registerUser(email, password, name);
            }
        });

    }

    private void initAuthViewModel(){
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }


    //Set layout components
    private void setUIComponents() {

        TI_email    = findViewById(R.id.TI_email);
        TI_password = findViewById(R.id.TI_password);
        etEmail     = findViewById(R.id.et_email);
        etPassword  = findViewById(R.id.et_password);
        etName      = findViewById(R.id.et_name);
        btnRegister = findViewById(R.id.btn_register);

        progressDialog = new ProgressDialog(this);
    }

    private void setTexts() {
        this.email = etEmail.getText().toString().trim();
        this.password = etPassword.getText().toString().trim();
        this.name = etName.getText().toString().trim();

        if(email.isEmpty() || password.isEmpty() ||name.isEmpty()){
            Toast.makeText(this, "Please fill all inputs", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser(String email, String password, String name){

        if(email.isEmpty() || password.isEmpty() ||name.isEmpty()){
            Toast.makeText(this, "Lütfen bütün boşlukları doldurunuz", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!validateEmail() || !validatePassword()){
            return;
        }

        showProgressDialog();

        authViewModel.registerUser(email, password, name);
        authViewModel.registeredUser.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user.isSuccess()){
                    if(user.isNew()){
                        createUserInDB(user);
                    }else{
                        Toast.makeText(RegisterActivity.this, "Lütfen geçerli bir email ve şifre giriniz", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "Kayıt hatası, lütfen güçlü bir şifre kullanın.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void createUserInDB(User authenticatedUser){

        authViewModel.createUserInDB(authenticatedUser);
        authViewModel.createdUserInDB.observe(this, user -> {
            if(user.isSuccess()){
                if(user.isCreated()){
                    Toast.makeText(this, "Kayıt başarılı", Toast.LENGTH_SHORT).show();
                    goToLoginActivity();
                }else{
                    goToLoginActivity();
                }
                progressDialog.dismiss();
            }else{
                Toast.makeText(this, "kayıt başarısız, lütfen tekrar deneyiniz.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
    }

    private void goToLoginActivity(){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
    }

    private boolean validateEmail() {
        email = TI_email.getEditText().getText().toString().trim();
        if (email.isEmpty()) {
            TI_email.setError("Lütfen e-mail kısmını boş bırakmayınız.");
            return false;
        } else if (!(GMAIL_ADDRESS.matcher(email).matches() || HOTMAIL_ADDRESS.matcher(email).matches() || OUTLOOK_ADDRESS.matcher(email).matches())) {
            TI_email.setError("Lütfen düzgün bir e-mail hesabı giriniz!");
            Toast.makeText(this, "Yalnızca gmail/hotmail/outlook.com uzantılı mailler kabul edilir.", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            TI_email.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        password = TI_password.getEditText().getText().toString().trim();
        if (password.isEmpty()) {
            TI_password.setError("Lütfen boş şifre kısmını boş bırakmayınız.");
            return false;
        }else if(!PASSWORD.matcher(password).matches()){
            TI_password.setError("Özel veya 'ş,ı,ü,ö,ğ,ç' karakterlerini kullanmayınız.");
            return false;
        } else {
            TI_password.setError(null);
            return true;
        }
    }
}