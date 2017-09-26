package com.imenu.fr.restaurant.api.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Tookan implements Serializable {

@SerializedName("tookan_latitude")
@Expose
private String tookanLatitude;
@SerializedName("tookan_longitude")
@Expose
private String tookanLongitude;

public String getTookanLatitude() {
return tookanLatitude;
}

public void setTookanLatitude(String tookanLatitude) {
this.tookanLatitude = tookanLatitude;
}

public String getTookanLongitude() {
return tookanLongitude;
}

public void setTookanLongitude(String tookanLongitude) {
this.tookanLongitude = tookanLongitude;
}

}