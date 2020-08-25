package remy.pouzet.go4lunch.data.service.notificationsService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.data.repositories.models.User;
import remy.pouzet.go4lunch.data.service.realAPI.UserHelper;
import remy.pouzet.go4lunch.view.activities.MainActivity;

/**
 * Created by Remy Pouzet on 21/08/2020.
 */
public class NotificationsService extends FirebaseMessagingService {
	
	private final int    NOTIFICATION_ID  = 007;
	private final String NOTIFICATION_TAG = "FIREBASEOC";
	
	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		if (FirebaseAuth
				    .getInstance()
				    .getCurrentUser() != null) {
			String uid = FirebaseAuth
					.getInstance()
					.getCurrentUser()
					.getUid();
			if (remoteMessage.getNotification() != null) {
				getDatasFromFireBase(uid);

//				String message = remoteMessage
//						.getNotification()
//						.getBody();

//				this.sendVisualNotification("test");
			}
		}
	}
	
	private void getDatasFromFireBase(String uid) {
		
		UserHelper
				.getUser(uid)
				.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
					@Override
					public void onSuccess(DocumentSnapshot documentSnapshot) {
						User currentUser = documentSnapshot.toObject(User.class);
						String username = TextUtils.isEmpty(currentUser.getUsername())
						                  ? getString(R.string.info_no_username_found)
						                  : currentUser.getUsername();
						
						String restaurantName = TextUtils.isEmpty(currentUser.getUsername())
						                        ? getString(R.string.info_no_username_found)
						                        : currentUser.getNameRestaurant();
						
						String placeID = TextUtils.isEmpty(currentUser.getUsername())
						                 ? getString(R.string.info_no_username_found)
						                 : currentUser.getPlaceID();
						
						String restaurantAdress = TextUtils.isEmpty(currentUser.getUsername())
						                          ? getString(R.string.info_no_username_found)
						                          : currentUser.getAdressRestaurant();
						
						String workmatesNames = String.valueOf(UserHelper.getInterestedUsers("user", placeID));
						
						String message = "Bonjour" + username + ", vous avez sélectionné " + restaurantName + "comme restaurant pour ce midi. Retrouvez y " + workmatesNames + " à " + restaurantAdress;
						
						sendVisualNotification(message);
//
//						List<String> datasFromFireBaseList = new ArrayList<>();
//						datasFromFireBaseList.add(username);
//						datasFromFireBaseList.add(restaurantName);
//						datasFromFireBaseList.add(workmatesNames);
//						datasFromFireBaseList.add(restaurantAdress);
					}
				});
	}
	
	// ---
	
	private void sendVisualNotification(String messageBody) {
		
		// 1 - Create an Intent that will be shown when user will click on the Notification
		Intent        intent        = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		
		// 2 - Create a Style for the Notification
		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
		inboxStyle.setBigContentTitle(getString(R.string.notification_title));
		inboxStyle.addLine(messageBody);
		
		// 3 - Create a Channel (Android 8)
		String channelId = getString(R.string.default_notification_channel_id);
		
		// 4 - Build a Notification object
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
				.setSmallIcon(R.drawable.go4)
				.setContentTitle(getString(R.string.app_name))
				.setContentText(getString(R.string.notification_title))
				.setAutoCancel(true)
				.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.setContentIntent(pendingIntent)
				.setStyle(inboxStyle);
		
		// 5 - Add the Notification to the Notification Manager and show it.
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		// 6 - Support Version >= Android 8
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence        channelName = "Message provenant de Firebase";
			int                 importance  = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel mChannel    = new NotificationChannel(channelId, channelName, importance);
			notificationManager.createNotificationChannel(mChannel);
		}
		
		// 7 - Show notification
		notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
	}
}
