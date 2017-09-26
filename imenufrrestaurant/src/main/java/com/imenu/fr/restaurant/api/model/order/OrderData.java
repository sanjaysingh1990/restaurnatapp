package com.imenu.fr.restaurant.api.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.imenu.fr.restaurant.utils.Constants;

import java.io.Serializable;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class OrderData implements Serializable {

    @SerializedName("order_id")
    @Expose
    private int orderId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("deliveryDate")
    @Expose
    private String deliveryDate;
    @SerializedName("deliveryTime")
    @Expose
    private String deliveryTime;
    @SerializedName("buyer_address")
    @Expose
    private String buyerAddress;
    @SerializedName("buyer_phone_no")
    @Expose
    private String buyerPhoneNo;
    @SerializedName("buyer_name")
    @Expose
    private String buyerName;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("items")
    @Expose
    private List<OrderItem> items = null;


    public Tookan getTookan() {
        return tookan;
    }

    public void setTookan(Tookan tookan) {
        this.tookan = tookan;
    }

    @SerializedName("tookan")
    @Expose
    private Tookan tookan;

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    @SerializedName("read_status")
    @Expose
    private int readStatus;

    private int dataType;

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public String getBuyerPhoneNo() {
        return buyerPhoneNo;
    }

    public void setBuyerPhoneNo(String buyerPhoneNo) {
        this.buyerPhoneNo = buyerPhoneNo;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTotalAmount() {
        return Constants.EURO + totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

}