package com.imenu.fr.restaurant.api.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderResponse {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<OrderData> data = null;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @SerializedName("count")
    @Expose
    private int count;

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    @SerializedName("order_status")
    @Expose
    private int orderStatus;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<OrderData> getData() {
        return data;
    }

    public void setData(List<OrderData> data) {
        this.data = data;
    }

}