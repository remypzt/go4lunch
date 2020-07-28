package remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Geometry {
	
	@SerializedName("location") private Location mLocation;
	@SerializedName("viewport") private Viewport mViewport;
	
	public Location getLocation() {
		return mLocation;
	}
	
	public void setLocation(Location location) {
		mLocation = location;
	}
	
	public Viewport getViewport() {
		return mViewport;
	}
	
	public void setViewport(Viewport viewport) {
		mViewport = viewport;
	}
	
}
