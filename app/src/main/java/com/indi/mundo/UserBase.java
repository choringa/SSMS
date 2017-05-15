package com.indi.mundo;

/**
 * Created by Choringa on 4/23/17.
 */

public class UserBase {
    public String username, email, phoneNumber;
    public int qbID;


    public UserBase(){
        // Default constructor required for calls to DataSnapshot.getValue(UserBase.class)
    }

    public UserBase(String username, String email, String phoneNumber, int qbID) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.qbID = qbID;
    }
}
