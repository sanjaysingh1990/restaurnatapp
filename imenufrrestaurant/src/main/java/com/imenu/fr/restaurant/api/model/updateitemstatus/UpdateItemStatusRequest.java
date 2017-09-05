package com.imenu.fr.restaurant.api.model.updateitemstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by android on 4/9/17.
 */

public class UpdateItemStatusRequest {

    @SerializedName("store_id")
    @Expose
    private int storeId;
    @SerializedName("order_id")
    @Expose
    private int orderId;
    @SerializedName("status")
    @Expose
    private int status;

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
