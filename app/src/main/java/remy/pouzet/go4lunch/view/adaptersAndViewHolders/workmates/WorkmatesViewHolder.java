package remy.pouzet.go4lunch.view.adaptersAndViewHolders.workmates;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import remy.pouzet.go4lunch.BuildConfig;
import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.data.repositories.RestaurantsRepository;
import remy.pouzet.go4lunch.data.repositories.models.Restaurant;
import remy.pouzet.go4lunch.data.repositories.models.User;
import remy.pouzet.go4lunch.data.service.realAPI.POJOdetailsRestaurants.ResponseOfPlaceDetailsRestaurants;
import remy.pouzet.go4lunch.databinding.ContentItemsOfWorkmatesListViewBinding;
import remy.pouzet.go4lunch.view.activities.RestaurantDetailsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Remy Pouzet on 31/07/2020.
 */
public class WorkmatesViewHolder extends RecyclerView.ViewHolder {
	
	public ImageView imageViewProfile;
	
	public TextView textViewProfile;
	public User     currentUser;
	
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
				
				clickRestaurant(user.getPlaceID());
			});
		} else {
			this.textViewProfile.setText(user.getUsername() + " hasn't decided yet");
		}
		
		RequestOptions mRequestOption = new RequestOptions();
		mRequestOption.placeholder(R.drawable.ic_anon_user_48dp);
		glide
				.load(user.getUrlPicture())
				.apply(mRequestOption)
				.apply(RequestOptions.circleCropTransform())
				.into(imageViewProfile);
	}
	
	private void clickRestaurant(String placeID) {
		RestaurantsRepository.getInstance().mRestaurantsApiInterfaceService
				.getResponseOfPlaceDetailsRestaurants(placeID, BuildConfig.apiKey)
				.enqueue(new Callback<ResponseOfPlaceDetailsRestaurants>() {
					@Override
					public void onResponse(Call<ResponseOfPlaceDetailsRestaurants> call,
					                       Response<ResponseOfPlaceDetailsRestaurants> response) {
						if (response.isSuccessful()) {
							
							Restaurant restaurant = new Restaurant(placeID, "https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photoreference=" + response
									.body()
									.getResult()
									.getPhotos()
									.get(0)
									.getPhotoReference() + "&key=" + BuildConfig.apiKey, response
									                                       .body()
									                                       .getResult()
									                                       .getName(), response
									                                       .body()
									                                       .getResult()
									                                       .getFormattedAddress(), response
									                                       .body()
									                                       .getStatus(), "distance", 0, response
									                                       .body()
									                                       .getResult()
									                                       .getRating(), response
									                                       .body()
									                                       .getResult()
									                                       .getInternationalPhoneNumber(), response
									                                       .body()
									                                       .getResult()
									                                       .getWebsite(), 0, 0);
							
							RestaurantDetailsActivity.startActivity(textViewProfile.getContext(), restaurant);
						}
					}
					
					@Override
					public void onFailure(Call<ResponseOfPlaceDetailsRestaurants> call,
					                      Throwable t) {
					}
				});
	}
}