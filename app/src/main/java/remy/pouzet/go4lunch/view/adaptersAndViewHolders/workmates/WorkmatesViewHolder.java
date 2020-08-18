package remy.pouzet.go4lunch.view.adaptersAndViewHolders.workmates;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import remy.pouzet.go4lunch.data.repositories.models.User;
import remy.pouzet.go4lunch.databinding.ContentItemsOfWorkmatesListViewBinding;

/**
 * Created by Remy Pouzet on 31/07/2020.
 */
public class WorkmatesViewHolder extends RecyclerView.ViewHolder {
	
	public ImageView imageViewProfile;
	public TextView  textViewProfile;
	public User      currentUser;
	
	private @NonNull ContentItemsOfWorkmatesListViewBinding mFragmentContentWorkmatesListViewBinding;
	
	public WorkmatesViewHolder(View itemView) {
		super(itemView);
		
		mFragmentContentWorkmatesListViewBinding = ContentItemsOfWorkmatesListViewBinding.bind(itemView);
		Context localContext = itemView.getContext();
		
		imageViewProfile = mFragmentContentWorkmatesListViewBinding.workmatesImageviewProfile;
		textViewProfile  = mFragmentContentWorkmatesListViewBinding.workmateNameAndChosenRestaurant;
	}
	
	public void updateWorkmates(User user,
	                            String currentUserId,
	                            RequestManager glide) {
		if (user.getNameRestaurant() != null) {
			this.textViewProfile.setText(user.getUsername() + " is eating to " + user.getNameRestaurant());
			this.textViewProfile.setOnClickListener(v -> {
				//TODO requete place pour avoir les infos pour get Restaurant

//			RestaurantDetails.startActivity(textViewProfile.getContext(),
//			                                restaurant
//			                               );
			});
		} else {
			this.textViewProfile.setText(user.getUsername() + " hasn't decided yet");
		}
		
		glide
				.load(user.getUrlPicture())
				.apply(RequestOptions.circleCropTransform())
				.into(imageViewProfile);
	}
}