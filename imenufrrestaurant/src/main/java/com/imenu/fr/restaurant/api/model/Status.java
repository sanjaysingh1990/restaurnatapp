package com.imenu.fr.restaurant.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Copyright 2015 OpenKey. All Rights Reserved
 *
 * @author OpenKey Inc.
 */
public class Status {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }


}