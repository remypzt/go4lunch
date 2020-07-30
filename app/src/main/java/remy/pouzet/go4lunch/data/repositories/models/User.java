package remy.pouzet.go4lunch.data.repositories.models;

import androidx.annotation.Nullable;

/**
 * Created by Remy Pouzet on 30/07/2020.
 */

public class User {
	
	private           String uid;
	private           String username;
	@Nullable private String urlPicture;
	
	public User() {
	}
	
	public User(String uid,
	            String username,
	            String urlPicture) {
		this.uid        = uid;
		this.username   = username;
		this.urlPicture = urlPicture;
		
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
	


