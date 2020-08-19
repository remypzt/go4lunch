package remy.pouzet.go4lunch.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.data.repositories.models.Restaurant;
import remy.pouzet.go4lunch.data.repositories.models.User;
import remy.pouzet.go4lunch.data.service.realAPI.UserHelper;
import remy.pouzet.go4lunch.databinding.RestaurantDetailsFragmentBinding;

public class RestaurantDetails extends AppCompatActivity {
	
	private final static String       EXTRA_RESTAURANT = "EXTRA_RESTAURANT";
	public               ImageView    restaurantPicture;
	public               ImageButton  userInterestImageButon;
	public               TextView     restaurantName;
	public               ImageView    restaurantEvaluationImageView;
	public               TextView     restaurantAdress;
	public               ImageButton  callImageButon;
	public               ImageButton  likeImageButon;
	public               ImageButton  websiteImageButon;
	public               User         currentUser;
	public               String       firestorePlaceID;
	public               String       firestorerestaurantName;
	public               List<String> firestoreLikedRestaurants;
	public               int          mRatingScore;
	public               double       mRatingScoreDouble;
	public               Restaurant   restaurant;
	
	public  Drawable                         mEvaluationScore;
	private RestaurantDetailsFragmentBinding mRestaurantDetailsFragmentBinding;
	public  String                           uid = this
			.getCurrentUser()
			.getUid();
	
	public static void startActivity(Context context,
	                                 Restaurant restaurant) {
		Intent intent = new Intent(context, RestaurantDetails.class);
		intent.putExtra(EXTRA_RESTAURANT, restaurant);
		context.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRestaurantDetailsFragmentBinding = RestaurantDetailsFragmentBinding.inflate(getLayoutInflater());
		setContentView(mRestaurantDetailsFragmentBinding.getRoot());
		
		restaurant = (Restaurant) getIntent().getSerializableExtra(EXTRA_RESTAURANT);
		displayingManagement(restaurant);
		butonsManagement(restaurant);
	}
	
	public void displayingManagement(Restaurant restaurant) {
		restaurantPicture             = mRestaurantDetailsFragmentBinding.restaurantPicture;
		userInterestImageButon        = mRestaurantDetailsFragmentBinding.userInterestImageButton;
		restaurantName                = mRestaurantDetailsFragmentBinding.restaurantNameDetailsFragment;
		restaurantEvaluationImageView = mRestaurantDetailsFragmentBinding.restaurantEvaluationDetailsFragment;
		restaurantAdress              = mRestaurantDetailsFragmentBinding.restaurantAdressDetailsFragment;
		callImageButon                = mRestaurantDetailsFragmentBinding.imageButtonCall;
		likeImageButon                = mRestaurantDetailsFragmentBinding.imageButtonLike;
		websiteImageButon             = mRestaurantDetailsFragmentBinding.imageButtonWebsite;
		restaurantName.setText(restaurant.getName());
		restaurantAdress.setText(restaurant.getAdress());
		
		//GLIDE
		RequestOptions mRequestOption = new RequestOptions();
		mRequestOption.placeholder(R.drawable.ic_launcher_background);
		Glide
				.with(restaurantPicture.getContext())
				.load(restaurant.getUrlImage())
				.apply(mRequestOption)
				.into(restaurantPicture);
		Glide
				.with(restaurantEvaluationImageView.getContext())
				.load(getRatingScorePicture(restaurant))
				.into(restaurantEvaluationImageView);
	}
	
	public Drawable getRatingScorePicture(Restaurant restaurants) {
		mRatingScoreDouble = restaurants.getEvaluation();
		mRatingScoreDouble = (mRatingScoreDouble * 3) / 5;
		mRatingScore       = ((int) Math.round(mRatingScoreDouble));
		Resources resources = getResources();
		
		switch (mRatingScore) {
			case 1:
				mEvaluationScore = ResourcesCompat.getDrawable(resources, R.drawable.ic_star_border_24, null);
				break;
			case 2:
				mEvaluationScore = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_star_half_24, null);
				break;
			case 3:
				mEvaluationScore = ResourcesCompat.getDrawable(resources, R.drawable.ic_star, null);
			default:
				mEvaluationScore = ResourcesCompat.getDrawable(resources, R.drawable.invisible, null);
				break;
		}
		return mEvaluationScore;
	}
	
	public void butonsManagement(Restaurant restaurant) {
		callImageButonManagement(restaurant);
		websiteImageButtonManagement(restaurant);
		likeImageButonManagement(restaurant);
		userInterestImageButonManagement(restaurant);
	}
	
	public void callImageButonManagement(Restaurant restaurant) {
		callImageButon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", restaurant.getMphoneNumber(), null)));
			}
		});
	}
	
	public void websiteImageButtonManagement(Restaurant restaurant) {
		websiteImageButon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent Getintent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getWebsite()));
				startActivity(Getintent);
			}
		});
	}
	
	public void likeImageButonManagement(Restaurant restaurant) {
		getDatasFromCurrentUserFromFirestore(restaurant);
		likeImageButon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (firestoreLikedRestaurants == null) {
					firestoreLikedRestaurants = new ArrayList<>();
					firestoreLikedRestaurants.add(restaurant.getMplaceID());
				} else {
					if (!firestoreLikedRestaurants.contains(restaurant.getMplaceID())) {
						firestoreLikedRestaurants.add(restaurant.getMplaceID());
					} else {
						firestoreLikedRestaurants.remove(restaurant.getMplaceID());
					}
				}
				updateLikedRestaurantsInFirestore();
				displayingRightLikeImageButon(firestoreLikedRestaurants, restaurant);
			}
		});
	}
	
	private void getDatasFromCurrentUserFromFirestore(Restaurant restaurant) {
		UserHelper
				.getUser(uid)
				.addOnSuccessListener((OnSuccessListener<DocumentSnapshot>) documentSnapshot -> {
					currentUser               = documentSnapshot.toObject(User.class);
					firestorePlaceID          = !TextUtils.isEmpty(currentUser.getPlaceID())
					                            ? currentUser.getPlaceID()
					                            : null;
					firestoreLikedRestaurants = (currentUser.getLikedRestaurants() == null && currentUser
							.getLikedRestaurants()
							.contains(restaurant.getMplaceID()))
					                            ? null
					                            : currentUser.getLikedRestaurants();
					displayingRightUserInterestImageButon(firestorePlaceID, restaurant);
					displayingRightLikeImageButon(firestoreLikedRestaurants, restaurant);
				});
	}
	
	public void displayingRightLikeImageButon(List<String> firestoreLikedRestaurants,
	                                          Restaurant restaurant) {
		if (firestoreLikedRestaurants != null && firestoreLikedRestaurants.size() > 0 && firestoreLikedRestaurants.contains(restaurant.getMplaceID())) {
			likeImageButon.setImageResource(R.drawable.ic_star);
		} else {
			likeImageButon.setImageResource(R.drawable.ic_star_border_24);
		}
	}
	
	public void displayingRightUserInterestImageButon(String firestorePlaceID,
	                                                  Restaurant restaurant) {
		if (firestorePlaceID != null && firestorePlaceID.equals(restaurant.getMplaceID())) {
			userInterestImageButon.setImageResource(R.drawable.ic_baseline_check_circle_green_24);
		} else {
			userInterestImageButon.setImageResource(R.drawable.ic_baseline_check_circle_24);
		}
	}
	
	public void userInterestImageButonManagement(Restaurant restaurant) {
		getDatasFromCurrentUserFromFirestore(restaurant);
		userInterestImageButon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (firestorePlaceID == null || !firestorePlaceID.equals(restaurant.getMplaceID())) {
					firestorePlaceID        = restaurant.getMplaceID();
					firestorerestaurantName = restaurant.getName();
				} else {
					firestorePlaceID        = null;
					firestorerestaurantName = null;
				}
				updateChosenRestaurantInFirestore();
				displayingRightUserInterestImageButon(firestorePlaceID, restaurant);
			}
		});
	}
	
	private void updateLikedRestaurantsInFirestore() {
		UserHelper.updateLikedRestaurants(firestoreLikedRestaurants, uid);
	}
	
	private void updateChosenRestaurantInFirestore() {
		UserHelper.updateChosenRestaurant(firestorePlaceID, firestorerestaurantName, uid);
	}
	
	@Nullable
	protected FirebaseUser getCurrentUser() {
		return FirebaseAuth
				.getInstance()
				.getCurrentUser();
	}
}
	
