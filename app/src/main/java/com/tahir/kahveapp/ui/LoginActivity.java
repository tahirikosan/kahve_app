package com.tahir.kahveapp.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.tahir.kahveapp.R;
import com.tahir.kahveapp.data.models.Order;
import com.tahir.kahveapp.data.models.User;
import com.tahir.kahveapp.view_models.AuthViewModel;

public class LoginActivity extends AppCompatActivity {



    public static final int RC_SIGN_IN = 1;
    public static final String USER = "USER";

    private GoogleSignInClient mClient;
    private AuthViewModel authViewModel;


    private EditText etEmail;
    private EditText etPassword;
    private TextView tvRegister;
    private Button btnLogin;
    private Button btnGoogleSing;

    private String email;
    private String password;

    //  private AuthViewModel authViewModel;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUIComponents();

        initGoogleSignInClient();
        initAuthViewModel();



       /* authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.authListener = this;
*/
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTexts();
                loginUser(email, password);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnGoogleSing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //authViewModel.googleSignIn();
                signIn();
            }
        });

    }

    //Set layout components
    private void setUIComponents() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        btnGoogleSing = findViewById(R.id.btn_google_signin);

        progressDialog = new ProgressDialog(this);
    }

    private void setTexts() {
        this.email = etEmail.getText().toString().trim();
        this.password = etPassword.getText().toString().trim();
    }

    // set google client
    private void initGoogleSignInClient(){
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mClient = GoogleSignIn.getClient(this, gso);
    }

    //start sign in for google sign in
    private void signIn(){
        Intent signInIntent = mClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void loginUser(String email, String password){

        if(!email.isEmpty() && !password.isEmpty()){
            showProgressDialog();

            authViewModel.loginUser(email, password);
            authViewModel.signedInUser.observe(this, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user.isSuccess()) {
                        goToMainActivity();
                        progressDialog.dismiss();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Could not login user, Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(this, "Please fill the inputs", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                if(account != null){
                    getGoogleAuthCredential(account);
                }
            }catch (ApiException e){
                Toast.makeText(this, "an error happened " + e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initAuthViewModel(){
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    private void getGoogleAuthCredential(GoogleSignInAccount account) {
        String googleTokenId = account.getIdToken();
        AuthCredential googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null);
        signInWithGoogleAuthCredential(googleAuthCredential);
    }

    private void signInWithGoogleAuthCredential(AuthCredential googleAuthCredential) {

        showProgressDialog();

        authViewModel.signInWithGoogle(googleAuthCredential);
        authViewModel.authenticatedUser.observe(this, authenticatedUser -> {
            if(authenticatedUser.isSuccess()){
                progressDialog.dismiss();
                if (authenticatedUser.isNew()) {
                    createUserInDB(authenticatedUser);
                } else {
                    goToMainActivity();
                }

            }else{
                Toast.makeText(this, "Google Sign in error, Please try again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void createUserInDB(User authenticatedUser){
        showProgressDialog();

        authViewModel.createUserInDB(authenticatedUser);
        authViewModel.createdUserInDB.observe(this, user -> {
            if(user.isSuccess()){
                if(user.isCreated()){
                    goToMainActivity();
                }
                progressDialog.dismiss();
            }else{
                Toast.makeText(this, "Could not create user, please try again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
    }

    private void goToMainActivity(){

        authViewModel.setUser();
        authViewModel.userLive.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.isSuccess()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(LoginActivity.this, "Kullanıcıya erişilemiyor", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
    }
}