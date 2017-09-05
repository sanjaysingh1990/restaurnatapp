package com.imenu.fr.restaurant.api.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 28/8/17.
 */

public class LoginRequest {

    @SerializedName("password")
    @Expose
    private String password;

    @Expose
    private String firstName;
    @SerializedName("email")
    @Expose
    private String email;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
