package remy.pouzet.go4lunch.view.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import butterknife.OnClick;
import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.data.repositories.models.Message;
import remy.pouzet.go4lunch.data.repositories.models.User;
import remy.pouzet.go4lunch.data.service.realAPI.MessageHelper;
import remy.pouzet.go4lunch.data.service.realAPI.UserHelper;
import remy.pouzet.go4lunch.databinding.ActivityChatBinding;
import remy.pouzet.go4lunch.view.adaptersAndViewHolders.chat.ChatAdapter;

/**
 * Created by Remy Pouzet on 31/07/2020.
 */
public class ChatActivity extends AppCompatActivity implements ChatAdapter.Listener {
	
	// STATIC DATA FOR CHAT (3)
	private static final String CHAT_NAME_ANDROID  = "android";
	private static final String CHAT_NAME_BUG      = "bug";
	private static final String CHAT_NAME_FIREBASE = "firebase";
	// FOR DESIGN
	// 1 - Getting all views needed
	RecyclerView      recyclerView;
	TextView          textViewRecyclerViewEmpty;
	TextInputEditText editTextMessage;
	ImageView         imageViewPreview;
	// FOR DATA
	// 2 - Declaring Adapter and data
	private           ChatAdapter         mentorChatAdapter;
	@Nullable private User                modelCurrentUser;
	private           String              currentChatName;
	private           ActivityChatBinding mActivityChatBinding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		recyclerView              = mActivityChatBinding.activityChatRecyclerView;
		textViewRecyclerViewEmpty = mActivityChatBinding.activityChatTextViewRecyclerViewEmpty;
		editTextMessage           = mActivityChatBinding.activityChatMessageEditText;
		imageViewPreview          = mActivityChatBinding.activityChatImageChosenPreview;
		
		super.onCreate(savedInstanceState);
		
		this.configureRecyclerView(CHAT_NAME_ANDROID);
//		this.configureToolbar();
		this.getCurrentUserFromFirestore();
	}
	
	// --------------------
	// UI
	// --------------------
	// 5 - Configure RecyclerView with a Query
	private void configureRecyclerView(String chatName) {
		//Track current chat name
		this.currentChatName = chatName;
		//Configure Adapter & RecyclerView
		this.mentorChatAdapter = new ChatAdapter(generateOptionsForAdapter(MessageHelper.getAllMessageForChat(this.currentChatName)), Glide.with(this), this, this
				.getCurrentUser()
				.getUid());
		mentorChatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
			@Override
			public void onItemRangeInserted(int positionStart,
			                                int itemCount) {
				recyclerView.smoothScrollToPosition(mentorChatAdapter.getItemCount()); // Scroll to bottom on new messages
			}
		});
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(this.mentorChatAdapter);
	}
	
	// --------------------
	// ACTIONS
	// --------------------
	
	// --------------------
	// REST REQUESTS
	// --------------------
	// 4 - Get Current User from Firestore
	private void getCurrentUserFromFirestore() {
		UserHelper
				.getUser(getCurrentUser().getUid())
				.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
					@Override
					public void onSuccess(DocumentSnapshot documentSnapshot) {
						modelCurrentUser = documentSnapshot.toObject(User.class);
					}
				});
	}
	
	// 6 - Create options for RecyclerView from a Query
	private FirestoreRecyclerOptions<Message> generateOptionsForAdapter(Query query) {
		return new FirestoreRecyclerOptions.Builder<Message>()
				.setQuery(query, Message.class)
				.setLifecycleOwner(this)
				.build();
	}
	
	@Override
	public int getFragmentLayout() {
		return R.layout.activity_chat;
	}
	
	@OnClick(R.id.activity_chat_send_button)
	public void onClickSendMessage() {
	}
	
	@OnClick({R.id.activity_chat_android_chat_button, R.id.activity_chat_firebase_chat_button, R.id.activity_chat_bug_chat_button})
	public void onClickChatButtons(ImageButton imageButton) {
		// 8 - Re-Configure the RecyclerView depending chosen chat
		switch (Integer.valueOf(imageButton
				                        .getTag()
				                        .toString())) {
			case 10:
				this.configureRecyclerView(CHAT_NAME_ANDROID);
				break;
			case 20:
				this.configureRecyclerView(CHAT_NAME_FIREBASE);
				break;
			case 30:
				this.configureRecyclerView(CHAT_NAME_BUG);
				break;
		}
	}
	
	@OnClick(R.id.activity_chat_add_file_button)
	public void onClickAddFile() {
	}
	
	// --------------------
	// CALLBACK
	// --------------------
	
	@Override
	public void onDataChanged() {
		// 7 - Show TextView in case RecyclerView is empty
		textViewRecyclerViewEmpty.setVisibility(this.mentorChatAdapter.getItemCount() == 0
		                                        ? View.VISIBLE
		                                        : View.GONE);
	}
}
