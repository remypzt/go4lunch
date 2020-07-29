package remy.pouzet.go4lunch.data.service.realAPI.POJOdetailsRestaurants;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class PlusCode {
	
	@SerializedName("compound_code") private String mCompoundCode;
	@SerializedName("global_code") private   String mGlobalCode;
	
	public String getCompoundCode() {
		return mCompoundCode;
	}
	
	public void setCompoundCode(String compoundCode) {
		mCompoundCode = compoundCode;
	}
	
	public String getGlobalCode() {
		return mGlobalCode;
	}
	
	public void setGlobalCode(String globalCode) {
		mGlobalCode = globalCode;
	}
	
}
