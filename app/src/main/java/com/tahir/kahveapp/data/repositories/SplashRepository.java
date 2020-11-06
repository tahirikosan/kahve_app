package com.tahir.kahveapp.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tahir.kahveapp.data.models.User;
import com.tahir.kahveapp.utils.SharedPrefData;


public class SplashRepository {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private User user = new User();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private SharedPrefData sharedPrefData;

    public SplashRepository(Context context) {
        sharedPrefData = new SharedPrefData(context);
    }

    // check if user signed in or not
    public MutableLiveData<User> checkUserAuth(){
        MutableLiveData<User> isUserAuth = new MutableLiveData<>();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if(firebaseUser == null){
            user.setAuthenticated(false);
            isUserAuth.setValue(user);
        }else{
            user.setId(firebaseUser.getUid());
            user.setAuthenticated(true);
            isUserAuth.setValue(user);
        }
        return isUserAuth;
    }

    //Gets user data from firebase db
    public MutableLiveData<User> getUserData(String uid){
        MutableLiveData<User> userWithData = new MutableLiveData<>();

        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot != null){
                            User user = documentSnapshot.toObject(User.class);
                            userWithData.setValue(user);

                            sharedPrefData.saveUser(user);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.v("Firebase User Error", "Error while fetching user from firebase : "+ e.getMessage());
            }
        });

        return userWithData;
    }

}
