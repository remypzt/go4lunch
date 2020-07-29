package remy.pouzet.go4lunch.data.service.realAPI.POJOdetailsRestaurants;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ResponseOfPlaceDetailsRestaurants {
	
	@SerializedName("html_attributions") private List<Object> mHtmlAttributions;
	@SerializedName("result") private            Result       mResult;
	@SerializedName("status") private            String       mStatus;
	
	public List<Object> getHtmlAttributions() {
		return mHtmlAttributions;
	}
	
	public void setHtmlAttributions(List<Object> htmlAttributions) {
		mHtmlAttributions = htmlAttributions;
	}
	
	public Result getResult() {
		return mResult;
	}
	
	public void setResult(Result result) {
		mResult = result;
	}
	
	public String getStatus() {
		return mStatus;
	}
	
	public void setStatus(String status) {
		mStatus = status;
	}
	
}
