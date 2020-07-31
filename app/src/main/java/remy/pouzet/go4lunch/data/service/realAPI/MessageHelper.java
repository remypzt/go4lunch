package remy.pouzet.go4lunch.data.service.realAPI;

import com.google.firebase.firestore.Query;

/**
 * Created by Remy Pouzet on 31/07/2020.
 */

public class MessageHelper {
	
	private static final String COLLECTION_NAME = "messages";
	
	// --- GET ---
	
	public static Query getAllMessageForChat(String chat) {
		return ChatHelper
				.getChatCollection()
				.document(chat)
				.collection(COLLECTION_NAME)
				.orderBy("dateCreated")
				.limit(50);
	}
}