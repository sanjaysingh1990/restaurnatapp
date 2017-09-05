package com.imenu.fr.restaurant.api.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

@SerializedName("user_id")
@Expose
private String userId;
@SerializedName("name")
@Expose
private String name;
@SerializedName("store_id")
@Expose
private String storeId;

public String getUserId() {
return userId;
}

public void setUserId(String userId) {
this.userId = userId;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getStoreId() {
return storeId;
}

public void setStoreId(String storeId) {
this.storeId = storeId;
}

}