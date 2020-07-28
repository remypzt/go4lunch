package remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Location {
	
	@SerializedName("lat") private Double mLat;
	@SerializedName("lng") private Double mLng;
	
	public Double getLat() {
		return mLat;
	}
	
	public void setLat(Double lat) {
		mLat = lat;
	}
	
	public Double getLng() {
		return mLng;
	}
	
	public void setLng(Double lng) {
		mLng = lng;
	}
	
}
