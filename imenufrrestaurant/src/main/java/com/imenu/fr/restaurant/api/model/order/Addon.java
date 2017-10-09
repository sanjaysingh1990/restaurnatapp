package com.imenu.fr.restaurant.api.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Addon implements Serializable {

@SerializedName("id")
@Expose
private String id;
@SerializedName("shoppingcart_id")
@Expose
private String shoppingcartId;
@SerializedName("addon_id")
@Expose
private String addonId;
@SerializedName("quantity")
@Expose
private String quantity;
@SerializedName("price")
@Expose
private String price;
@SerializedName("addon_name")
@Expose
private String addonName;
@SerializedName("created")
@Expose
private String created;
@SerializedName("updated")
@Expose
private String updated;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getShoppingcartId() {
return shoppingcartId;
}

public void setShoppingcartId(String shoppingcartId) {
this.shoppingcartId = shoppingcartId;
}

public String getAddonId() {
return addonId;
}

public void setAddonId(String addonId) {
this.addonId = addonId;
}

public String getQuantity() {
return quantity;
}

public void setQuantity(String quantity) {
this.quantity = quantity;
}

public String getPrice() {
return price;
}

public void setPrice(String price) {
this.price = price;
}

public String getAddonName() {
return addonName;
}

public void setAddonName(String addonName) {
this.addonName = addonName;
}

public String getCreated() {
return created;
}

public void setCreated(String created) {
this.created = created;
}

public String getUpdated() {
return updated;
}

public void setUpdated(String updated) {
this.updated = updated;
}

}