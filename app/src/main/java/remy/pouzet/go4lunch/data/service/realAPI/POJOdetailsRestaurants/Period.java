package remy.pouzet.go4lunch.data.service.realAPI.POJOdetailsRestaurants;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Period {
	
	@SerializedName("close") private Close mClose;
	@SerializedName("open") private  Open  mOpen;
	
	public Close getClose() {
		return mClose;
	}
	
	public void setClose(Close close) {
		mClose = close;
	}
	
	public Open getOpen() {
		return mOpen;
	}
	
	public void setOpen(Open open) {
		mOpen = open;
	}
	
}
