package com.tahir.kahveapp.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tahir.kahveapp.data.models.User;
import com.tahir.kahveapp.utils.SharedPrefData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthRepository {

    private Context mContext;

    private SharedPrefData sharedPrefData;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public AuthRepository(Context mContext) {
        this.mContext = mContext;

        sharedPrefData = new SharedPrefData(mContext);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
    }


    // Process google sign in
    public MutableLiveData<User> signInWithGoogle(AuthCredential authCredential) {
        MutableLiveData<User> authenticatedUser = new MutableLiveData<>();

        mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                    if (firebaseUser != null) {
                        String uid = firebaseUser.getUid();
                        String email = firebaseUser.getEmail();
                        String name = firebaseUser.getDisplayName();

                        User user = new User(uid, name, email);
                        user.setNew(isNewUser);
                        user.setSuccess(true);
                        authenticatedUser.setValue(user);

                        //save user to shared prefs
                        sharedPrefData.saveUser(user);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                User user = new User();
                user.setSuccess(false);
                authenticatedUser.setValue(user);
            }
        });
        return authenticatedUser;
    }

    // Create a new user in cloud db if not exist there GoogleSignIn
    public MutableLiveData<User> createUserInDb(User authenticatedUser) {
        MutableLiveData<User> newUser = new MutableLiveData<>();

        // create user
        Map<String, Object> cloudUser = new HashMap<>();
        cloudUser.put("id", authenticatedUser.getId());
        cloudUser.put("name", authenticatedUser.getName());
        cloudUser.put("email", authenticatedUser.getEmail());

        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        // auth user
        if (mCurrentUser != null) {
            db.collection("users")
                    .document(mCurrentUser.getUid())
                    .set(cloudUser)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            authenticatedUser.setCreated(true);
                            authenticatedUser.setSuccess(true);
                            newUser.setValue(authenticatedUser);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.v("Registration Error : ", e.getMessage());
                    authenticatedUser.setSuccess(false);
                    newUser.setValue(authenticatedUser);
                }
            });
        }


        return newUser;
    }

    //Register with email and password
    public MutableLiveData<User> registerUser(String email, String password, String name) {
        MutableLiveData<User> newUser = new MutableLiveData<>();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                    if (firebaseUser != null) {
                        String uid = firebaseUser.getUid();


                        User user = new User(uid, name, email);
                        user.setNew(isNewUser);
                        user.setSuccess(true);
                        newUser.setValue(user);
                    }
                } else {
                    User user = new User();
                    user.setNew(false);
                    user.setSuccess(false);
                    newUser.setValue(user);
                    Log.v("Register error happen ", task.getException().getMessage());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                User user = new User();
                user.setSuccess(false);
                newUser.setValue(user);
                Log.v("Register error happen ", e.getMessage());
            }
        });
        return newUser;
    }

    //Login with email and password
    public MutableLiveData<User> loginUser(String email, String password) {
        MutableLiveData<User> userLoged = new MutableLiveData<>();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                    if (firebaseUser != null) {
                        String uid = firebaseUser.getUid();

                        db.collection("users")
                                .document(uid)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        if (documentSnapshot != null) {
                                            User user = documentSnapshot.toObject(User.class);
                                            user.setSuccess(true);
                                            userLoged.setValue(user);

                                            //save user to shared prefs
                                            sharedPrefData.saveUser(user);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.v("Login-Get Data error : ", e.getMessage().toString());
                                User user = new User();
                                user.setSuccess(false);
                                userLoged.setValue(user);
                            }
                        });
                    }

                } else {
                    Log.v("Login error : ", task.getException().getMessage().toString());
                    User user = new User();
                    user.setSuccess(false);
                    userLoged.setValue(user);
                }
            }
        });

        return userLoged;
    }


    // log out user from both auth and db
    public MutableLiveData<User> logOut() {
        MutableLiveData<User> userLive = new MutableLiveData<>();

        User user = new User();
        try {
            mAuth.signOut();
            user.setLogOut(true);
            userLive.setValue(user);
        } catch (Exception e) {
            Log.v("Error log out: ", e.getMessage());
            user.setLogOut(false);
            userLive.setValue(user);
        }
        return userLive;
    }

    // log out just from db
    public MutableLiveData<Boolean> logOutFromDb() {
        MutableLiveData<Boolean> success = new MutableLiveData<>();

        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .update("online", false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        success.setValue(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("Error log out: ", e.getMessage());
                success.setValue(false);
            }
        });

        return success;
    }

    // log in just to db
    public MutableLiveData<Boolean> logInToDb() {
        MutableLiveData<Boolean> success = new MutableLiveData<>();

        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .update("online", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        success.setValue(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("Error log out: ", e.getMessage());
                success.setValue(false);
            }
        });

        return success;
    }

}
