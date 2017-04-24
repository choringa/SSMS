package com.indi.mundo;

/**
 * Created by Choringa on 4/23/17.
 */

public class UserBase {
    public String username, email, phoneNumber;


    public UserBase(){
        // Default constructor required for calls to DataSnapshot.getValue(UserBase.class)
    }

    public UserBase(String username, String email, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
