package remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Result {
	
	@SerializedName("business_status") private    String       mBusinessStatus;
	@SerializedName("formatted_address") private  String       mFormattedAddress;
	@SerializedName("geometry") private           Geometry     mGeometry;
	@SerializedName("icon") private               String       mIcon;
	@SerializedName("id") private                 String       mId;
	@SerializedName("name") private               String       mName;
	@SerializedName("opening_hours") private      OpeningHours mOpeningHours;
	@SerializedName("photos") private             List<Photo>  mPhotos;
	@SerializedName("place_id") private           String       mPlaceId;
	@SerializedName("plus_code") private          PlusCode     mPlusCode;
	@SerializedName("rating") private             Double       mRating;
	@SerializedName("reference") private          String       mReference;
	@SerializedName("types") private              List<String> mTypes;
	@SerializedName("user_ratings_total") private Long         mUserRatingsTotal;
	
	public String getBusinessStatus() {
		return mBusinessStatus;
	}
	
	public void setBusinessStatus(String businessStatus) {
		mBusinessStatus = businessStatus;
	}
	
	public String getFormattedAddress() {
		return mFormattedAddress;
	}
	
	public void setFormattedAddress(String formattedAddress) {
		mFormattedAddress = formattedAddress;
	}
	
	public Geometry getGeometry() {
		return mGeometry;
	}
	
	public void setGeometry(Geometry geometry) {
		mGeometry = geometry;
	}
	
	public String getIcon() {
		return mIcon;
	}
	
	public void setIcon(String icon) {
		mIcon = icon;
	}
	
	public String getId() {
		return mId;
	}
	
	public void setId(String id) {
		mId = id;
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		mName = name;
	}
	
	public OpeningHours getOpeningHours() {
		return mOpeningHours;
	}
	
	public void setOpeningHours(OpeningHours openingHours) {
		mOpeningHours = openingHours;
	}
	
	public List<Photo> getPhotos() {
		return mPhotos;
	}
	
	public void setPhotos(List<Photo> photos) {
		mPhotos = photos;
	}
	
	public String getPlaceId() {
		return mPlaceId;
	}
	
	public void setPlaceId(String placeId) {
		mPlaceId = placeId;
	}
	
	public PlusCode getPlusCode() {
		return mPlusCode;
	}
	
	public void setPlusCode(PlusCode plusCode) {
		mPlusCode = plusCode;
	}
	
	public Double getRating() {
		return mRating;
	}
	
	public void setRating(Double rating) {
		mRating = rating;
	}
	
	public String getReference() {
		return mReference;
	}
	
	public void setReference(String reference) {
		mReference = reference;
	}
	
	public List<String> getTypes() {
		return mTypes;
	}
	
	public void setTypes(List<String> types) {
		mTypes = types;
	}
	
	public Long getUserRatingsTotal() {
		return mUserRatingsTotal;
	}
	
	public void setUserRatingsTotal(Long userRatingsTotal) {
		mUserRatingsTotal = userRatingsTotal;
	}
	
}
