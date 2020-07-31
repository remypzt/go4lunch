package remy.pouzet.go4lunch.data.repositories.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Created by Remy Pouzet on 30/07/2020.
 */
public class Message {
	
	private String message;
	private Date   dateCreated;
	private User   userSender;
	private String urlImage;
	
	public Message() {
	}
	
	public Message(String message,
	               User userSender) {
		this.message    = message;
		this.userSender = userSender;
	}
	
	public Message(String message,
	               String urlImage,
	               User userSender) {
		this.message    = message;
		this.urlImage   = urlImage;
		this.userSender = userSender;
	}
	
	// --- GETTERS ---
	public String getMessage() {
		return message;
	}
	
	// --- SETTERS ---
	public void setMessage(String message) {
		this.message = message;
	}
	
	@ServerTimestamp
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public User getUserSender() {
		return userSender;
	}
	
	public void setUserSender(User userSender) {
		this.userSender = userSender;
	}
	
	public String getUrlImage() {
		return urlImage;
	}
	
	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}
}
