package remy.pouzet.go4lunch.data.repositories.models;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * Created by Remy Pouzet on 30/07/2020.
 */

public class User {
	private           String       uid;
	private           String       username;
	@Nullable private String       urlPicture;
	private           String       placeID;
	private           String       nameRestaurant;
	private           List<String> likedRestaurants;
	
	public User(String uid,
	            String username,
	            String urlPicture,
	            String placeID,
	            String nameRestaurant,
	            List<String> likedRestaurants) {
		this.uid              = uid;
		this.username         = username;
		this.urlPicture       = urlPicture;
		this.placeID          = placeID;
		this.nameRestaurant   = nameRestaurant;
		this.likedRestaurants = likedRestaurants;
	}
	
	public List<String> getLikedRestaurants() {
		return likedRestaurants;
	}
	
	public User() {
	}
	
	public void setLikedRestaurants(List<String> parameterLikedRestaurants) {
		likedRestaurants = parameterLikedRestaurants;
	}
	
	public String getPlaceID() {
		return placeID;
	}
	
	public void setPlaceID(String parameterPlaceID) {
		placeID = parameterPlaceID;
	}
	
	public String getNameRestaurant() {
		return nameRestaurant;
	}
	
	public void setNameRestaurant(String parameterNameRestaurant) {
		nameRestaurant = parameterNameRestaurant;
	}
	
	public String getUid() {
		return uid;
	}
	
	public void setUid(String parameterUid) {
		uid = parameterUid;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String parameterUsername) {
		username = parameterUsername;
	}
	
	@Nullable
	public String getUrlPicture() {
		return urlPicture;
	}
	
	public void setUrlPicture(@Nullable String parameterUrlPicture) {
		urlPicture = parameterUrlPicture;
	}
}
	


