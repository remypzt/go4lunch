package remy.pouzet.go4lunch.data.service.realAPI.POJOdetailsRestaurants;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Close {
	
	@SerializedName("day") private  Long   mDay;
	@SerializedName("time") private String mTime;
	
	public Long getDay() {
		return mDay;
	}
	
	public void setDay(Long day) {
		mDay = day;
	}
	
	public String getTime() {
		return mTime;
	}
	
	public void setTime(String time) {
		mTime = time;
	}
	
}
