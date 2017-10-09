package com.imenu.fr.restaurant.api.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.imenu.fr.restaurant.utils.Constants;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class OrderItem implements Serializable {

    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("unit_price")
    @Expose
    private float unitPrice;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("item_desc")
    @Expose
    private String itemDesc;
    @SerializedName("item_img")
    @Expose
    private String itemImg;
    @SerializedName("item_price")
    @Expose
    private float itemPrice;
    @SerializedName("order_id")
    @Expose
    private String orderId;

    public List<Addon> getAddons() {
        return addons;
    }

    public void setAddons(List<Addon> addons) {
        this.addons = addons;
    }

    @SerializedName("addons")
    @Expose
    private List<Addon> addons = null;


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

}