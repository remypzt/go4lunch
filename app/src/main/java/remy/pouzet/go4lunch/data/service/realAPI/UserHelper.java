package remy.pouzet.go4lunch.data.service.realAPI;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import remy.pouzet.go4lunch.data.repositories.models.User;

/**
 * Created by Remy Pouzet on 30/07/2020.
 */

public class UserHelper {
	
	private static final String COLLECTION_NAME = "users";
	
	// --- COLLECTION REFERENCE ---
	
	public static Task<Void> createUser(String uid,
	                                    String username,
	                                    String urlPicture) {
		User userToCreate = new User(uid, username, urlPicture);
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
				//TODO
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
	
	// --- DELETE ---
	
	public static Task<Void> deleteUser(String uid) {
		return UserHelper
				.getUsersCollection()
				.document(uid)
				.delete();
	}
	
}

