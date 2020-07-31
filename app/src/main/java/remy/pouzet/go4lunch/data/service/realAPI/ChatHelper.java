package remy.pouzet.go4lunch.data.service.realAPI;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by Remy Pouzet on 31/07/2020.
 */
public class ChatHelper {
	
	private static final String COLLECTION_NAME = "chats";
	
	// --- COLLECTION REFERENCE ---
	
	public static CollectionReference getChatCollection() {
		return FirebaseFirestore
				.getInstance()
				.collection(COLLECTION_NAME);
	}
}
