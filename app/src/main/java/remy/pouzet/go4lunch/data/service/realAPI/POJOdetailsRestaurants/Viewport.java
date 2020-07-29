package remy.pouzet.go4lunch.data.service.realAPI.POJOdetailsRestaurants;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Viewport {
	
	@SerializedName("northeast") private Northeast mNortheast;
	@SerializedName("southwest") private Southwest mSouthwest;
	
	public Northeast getNortheast() {
		return mNortheast;
	}
	
	public void setNortheast(Northeast northeast) {
		mNortheast = northeast;
	}
	
	public Southwest getSouthwest() {
		return mSouthwest;
	}
	
	public void setSouthwest(Southwest southwest) {
		mSouthwest = southwest;
	}
	
}
