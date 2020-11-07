package com.tahir.kahveapp.data.repositories;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tahir.kahveapp.data.models.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileRepository {


    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public ProfileRepository(Context context) {

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

    }

    //edit name from both auth and firestore db
    public MutableLiveData<User> editName(String newName) {
        MutableLiveData<User> userLive = new MutableLiveData<>();

        UserProfileChangeRequest nameChange = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build();

        // Prompt the user to re-provide their sign-in credentials
        auth.getCurrentUser().updateProfile(nameChange)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        User user = new User();

                        db.collection("users")
                                .document(auth.getCurrentUser().getUid())
                                .update("name", newName)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            user.setSuccess(true);
                                            userLive.setValue(user);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.v("Error Update name", "FireStore error  : " + task.getException().getMessage());
                                user.setSuccess(false);
                                userLive.setValue(user);
                            }
                        });
                    }
                });

        return userLive;
    }


    //edit password from auth
    public MutableLiveData<User> editPassword(String currentPassword, String newPassword) {
        MutableLiveData<User> userLive = new MutableLiveData<>();

        AuthCredential credential = EmailAuthProvider
                .getCredential(auth.getCurrentUser().getEmail(), currentPassword);

        // Prompt the user to re-provide their sign-in credentials
        auth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        User user = new User();

                        auth.getCurrentUser().updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user.setSuccess(true);
                                            userLive.setValue(user);
                                        }else{
                                            Log.v("Error Update Password", "Task failed : " + task.getException().getMessage());
                                            user.setSuccess(false);
                                            userLive.setValue(user);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.v("Error Update Password", "Exception : " + e.getMessage());
                                user.setSuccess(false);
                                userLive.setValue(user);
                            }
                        });
                    }
                });

        return userLive;
    }

    //edit Email both from auth and firestore db
    public MutableLiveData<User> editEmail(String currentPassword, String newEmail) {
        MutableLiveData<User> userLive = new MutableLiveData<>();

        AuthCredential credential = EmailAuthProvider
                .getCredential(auth.getCurrentUser().getEmail(), currentPassword);

        // Prompt the user to re-provide their sign-in credentials
        auth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        User user = new User();

                        auth.getCurrentUser().updateEmail(newEmail)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            db.collection("users")
                                                    .document(auth.getCurrentUser().getUid())
                                                    .update("email", newEmail)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                user.setSuccess(true);
                                                                userLive.setValue(user);
                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.v("Error Update Email", "FireStore error  : " + task.getException().getMessage());
                                                    user.setSuccess(false);
                                                    userLive.setValue(user);
                                                }
                                            });
                                        }else{
                                            Log.v("Error Update Email", "Task failed : " + task.getException().getMessage());
                                            user.setSuccess(false);
                                            userLive.setValue(user);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.v("Error Update Email", "Exception : " + e.getMessage());
                                user.setSuccess(false);
                                userLive.setValue(user);
                            }
                        });
                    }
                });

        return userLive;
    }

}
