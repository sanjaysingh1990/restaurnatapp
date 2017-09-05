package com.imenu.fr.restaurant.api.model.orderoperation;

/**
 * Created by android on 1/9/17.
 */

public class OrderAcceptReject {
    int type;
    private int storeId;
    private int orderId;
    private int orderStatus;

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

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
