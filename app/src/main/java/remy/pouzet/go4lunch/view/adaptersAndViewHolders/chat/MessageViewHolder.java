package remy.pouzet.go4lunch.view.adaptersAndViewHolders.chat;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.data.repositories.models.Message;
import remy.pouzet.go4lunch.databinding.ActivityChatItemBinding;

/**
 * Created by Remy Pouzet on 31/07/2020.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder {
	
	//FOR DATA
	private final int                     colorCurrentUser;
	private final int                     colorRemoteUser;
	//ROOT VIEW
	public        RelativeLayout          rootView;
	//PROFILE CONTAINER
	public        LinearLayout            profileContainer;
	public        ImageView               imageViewProfile;
	//MESSAGE CONTAINER
	public        RelativeLayout          messageContainer;
	//IMAGE SENDED CONTAINER
	public        CardView                cardViewImageSent;
	public        ImageView               imageViewSent;
	//TEXT MESSAGE CONTAINER
	public        LinearLayout            textMessageContainer;
	public        TextView                textViewMessage;
	//DATE TEXT
	public        TextView                textViewDate;
	private       ActivityChatItemBinding mChatActivityItemBinding;
	
	public MessageViewHolder(View itemView) {
		super(itemView);
		
		mChatActivityItemBinding = ActivityChatItemBinding.bind(itemView);
		Context localContext = itemView.getContext();
		
		rootView         = mChatActivityItemBinding.activityChatItemRootView;
		profileContainer = mChatActivityItemBinding.activityChatItemProfileContainer;
		imageViewProfile = mChatActivityItemBinding.activityChatItemProfileContainerProfileImage;
		messageContainer = mChatActivityItemBinding.activityChatItemMessageContainer;
		
		cardViewImageSent = mChatActivityItemBinding.activityChatItemMessageContainerImageSentCardview;
		
		imageViewSent        = mChatActivityItemBinding.activityChatItemMessageContainerImageSentCardviewImage;
		textMessageContainer = mChatActivityItemBinding.activityChatItemMessageContainerTextMessageContainer;
		textViewMessage      = mChatActivityItemBinding.activityChatItemMessageContainerTextMessageContainerTextView;
		textViewDate         = mChatActivityItemBinding.activityChatItemMessageContainerTextViewDate;
		
		colorCurrentUser = ContextCompat.getColor(itemView.getContext(), R.color.colorAccent);
		colorRemoteUser  = ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary);
	}
	
	public void updateWithMessage(Message message,
	                              String currentUserId,
	                              RequestManager glide) {
		
		// Check if current user is the sender
		Boolean isCurrentUser = message
				.getUserSender()
				.getUid()
				.equals(currentUserId);
		
		// Update message TextView
		this.textViewMessage.setText(message.getMessage());
		this.textViewMessage.setTextAlignment(isCurrentUser
		                                      ? View.TEXT_ALIGNMENT_TEXT_END
		                                      : View.TEXT_ALIGNMENT_TEXT_START);
		
		// Update date TextView
		if (message.getDateCreated() != null) {
			this.textViewDate.setText(this.convertDateToHour(message.getDateCreated()));
		}
		
		// Update profile picture ImageView
		if (message
				    .getUserSender()
				    .getUrlPicture() != null) {
			glide
					.load(message
							      .getUserSender()
							      .getUrlPicture())
					.apply(RequestOptions.circleCropTransform())
					.into(imageViewProfile);
		}
		
		// Update image sent ImageView
		if (message.getUrlImage() != null) {
			glide
					.load(message.getUrlImage())
					.into(imageViewSent);
			this.imageViewSent.setVisibility(View.VISIBLE);
		} else {
			this.imageViewSent.setVisibility(View.GONE);
		}
		
		//Update Message Bubble Color Background
		((GradientDrawable) textMessageContainer.getBackground()).setColor(isCurrentUser
		                                                                   ? colorCurrentUser
		                                                                   : colorRemoteUser);
		
		// Update all views alignment depending is current user or not
		this.updateDesignDependingUser(isCurrentUser);
	}
	
	private String convertDateToHour(Date date) {
		DateFormat dfTime = new SimpleDateFormat("HH:mm");
		return dfTime.format(date);
	}
	
	// ---
	
	private void updateDesignDependingUser(Boolean isSender) {
		
		// PROFILE CONTAINER
		RelativeLayout.LayoutParams paramsLayoutHeader = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsLayoutHeader.addRule(isSender
		                           ? RelativeLayout.ALIGN_PARENT_RIGHT
		                           : RelativeLayout.ALIGN_PARENT_LEFT);
		this.profileContainer.setLayoutParams(paramsLayoutHeader);
		
		// MESSAGE CONTAINER
		RelativeLayout.LayoutParams paramsLayoutContent = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsLayoutContent.addRule(isSender
		                            ? RelativeLayout.LEFT_OF
		                            : RelativeLayout.RIGHT_OF, R.id.activity_chat_item_profile_container);
		this.messageContainer.setLayoutParams(paramsLayoutContent);
		
		// CARDVIEW IMAGE SEND
		RelativeLayout.LayoutParams paramsImageView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsImageView.addRule(isSender
		                        ? RelativeLayout.ALIGN_LEFT
		                        : RelativeLayout.ALIGN_RIGHT, R.id.activity_chat_item_message_container_text_message_container);
		this.cardViewImageSent.setLayoutParams(paramsImageView);
		
		this.rootView.requestLayout();
	}
}