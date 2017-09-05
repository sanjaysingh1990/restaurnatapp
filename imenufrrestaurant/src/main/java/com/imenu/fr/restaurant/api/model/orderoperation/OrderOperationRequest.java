package com.imenu.fr.restaurant.api.model.orderoperation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderOperationRequest {

@SerializedName("store_id")
@Expose
private int storeId;
@SerializedName("order_id")
@Expose
private int orderId;
@SerializedName("order_status")
@Expose
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

}