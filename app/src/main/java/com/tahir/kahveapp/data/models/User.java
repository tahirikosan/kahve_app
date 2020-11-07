package com.tahir.kahveapp.data.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.FieldValue;

import java.io.Serializable;

public class User implements Serializable {


    private String id;
    private String name;
    private String email;
    private int point;

    @Exclude
    private boolean isAuthenticated;
    @Exclude
    private boolean isNew;
    @Exclude
    private boolean isCreated;
    @Exclude
    private boolean success;
    @Exclude
    private boolean isLogOut;

    public User(){}

    public User(String id, String name, String email, int point) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.point = point;
    }


    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isLogOut() {
        return isLogOut;
    }

    public void setLogOut(boolean logOut) {
        isLogOut = logOut;
    }
}
