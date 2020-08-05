package remy.pouzet.go4lunch.data.service.realAPI;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import remy.pouzet.go4lunch.data.repositories.models.Message;
import remy.pouzet.go4lunch.data.repositories.models.User;

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
	
	public static Task<DocumentReference> createMessageForChat(String textMessage,
	                                                           String chat,
	                                                           User userSender) {
		
		// 1 - Create the Message object
		Message message = new Message(textMessage, userSender);
		
		// 2 - Store Message to Firestore
		return ChatHelper
				.getChatCollection()
				.document(chat)
				.collection(COLLECTION_NAME)
				.add(message);
	}
}