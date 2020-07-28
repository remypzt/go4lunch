package remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class OpeningHours {
	
	@SerializedName("open_now") private Boolean mOpenNow;
	
	public Boolean getOpenNow() {
		return mOpenNow;
	}
	
	public void setOpenNow(Boolean openNow) {
		mOpenNow = openNow;
	}
	
}
