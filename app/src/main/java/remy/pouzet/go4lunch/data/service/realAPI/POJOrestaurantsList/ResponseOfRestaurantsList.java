package remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ResponseOfRestaurantsList {
	
	@SerializedName("html_attributions") private List<Object> mHtmlAttributions;
	@SerializedName("results") private           List<Result> mResults;
	@SerializedName("status") private            String       mStatus;
	
	public List<Object> getHtmlAttributions() {
		return mHtmlAttributions;
	}
	
	public void setHtmlAttributions(List<Object> htmlAttributions) {
		mHtmlAttributions = htmlAttributions;
	}
	
	public List<Result> getResults() {
		return mResults;
	}
	
	public void setResults(List<Result> results) {
		mResults = results;
	}
	
	public String getStatus() {
		return mStatus;
	}
	
	public void setStatus(String status) {
		mStatus = status;
	}
	
}
