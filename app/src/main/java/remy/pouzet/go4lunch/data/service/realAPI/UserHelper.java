package remy.pouzet.go4lunch.data.service.realAPI;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import remy.pouzet.go4lunch.data.repositories.models.User;

/**
 * Created by Remy Pouzet on 30/07/2020.
 */

public class UserHelper {
	
	private static final String COLLECTION_NAME = "users";
	
	// --- COLLECTION REFERENCE ---
	
	public static Task<Void> createUser(String uid,
	                                    String username,
	                                    String urlPicture,
	                                    String placeID,
	                                    String resaurantName,
	                                    List<String> likedRestaurants) {
		User userToCreate = new User(uid, username, urlPicture, placeID, resaurantName, likedRestaurants);
		return UserHelper
				.getUsersCollection()
				.document(uid)
				.set(userToCreate);
	}
	
	// --- CREATE ---
	
	public static CollectionReference getUsersCollection() {
		return FirebaseFirestore
				.getInstance()
				.collection(COLLECTION_NAME);
	}
	
	// --- GET ---
	
	public static Task<DocumentSnapshot> getUser(String uid) {
		return UserHelper
				.getUsersCollection()
				.document(uid)
				.get();
	}
	
	public static Query getAllUsers(String user) {
		return UserHelper.getUsersCollection()
				//TODO configure field place id in firebase
//				.orderBy("placeid")
				;
	}
	
	public static Query getInterestedUsers(String user,
	                                       String placeID) {
		return UserHelper
				.getUsersCollection()
				.whereEqualTo("placeID", placeID)
				//TODO configure field place id in firebase
//				.orderBy("placeid")
				;
	}
	
	// --- UPDATE ---
	
	public static Task<Void> updateUsername(String username,
	                                        String uid) {
		return UserHelper
				.getUsersCollection()
				.document(uid)
				.update("username", username);
	}
	
	public static Task<Void> updateChosenRestaurant(String placeID,
	                                                String nameRestaurant,
	                                                String uid) {
		return UserHelper
				.getUsersCollection()
				.document(uid)
				.update("nameRestaurant", nameRestaurant, "placeID", placeID);
	}
	
	public static Task<Void> updateLikedRestaurants(List<String> likedRestaurants,
	                                                String uid) {
		return UserHelper
				.getUsersCollection()
				.document(uid)
				.update("likedRestaurants", likedRestaurants);
	}
	
	// --- DELETE ---
	
	public static Task<Void> deleteUser(String uid) {
		return UserHelper
				.getUsersCollection()
				.document(uid)
				.delete();
	}
	
}

