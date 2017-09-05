package com.imenu.fr.restaurant.api.model.devicetoken;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdteTokenRequest {

@SerializedName("user_id")
@Expose
private int userId;
@SerializedName("device_token")
@Expose
private String deviceToken;

public int getUserId() {
return userId;
}

public void setUserId(int userId) {
this.userId = userId;
}

public String getDeviceToken() {
return deviceToken;
}

public void setDeviceToken(String deviceToken) {
this.deviceToken = deviceToken;
}

}