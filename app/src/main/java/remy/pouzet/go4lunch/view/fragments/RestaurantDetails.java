package remy.pouzet.go4lunch.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.data.repositories.models.Restaurant;
import remy.pouzet.go4lunch.databinding.RestaurantDetailsFragmentBinding;

public class RestaurantDetails extends AppCompatActivity {
	
	private final static String                           EXTRA_RESTAURANT = "EXTRA_RESTAURANT";
	public               ImageView                        restaurantPicture;
	public               ImageButton                      userInterestImageButon;
	public               TextView                         restaurantName;
	public               ImageView                        restaurantEvaluationImageView;
	public               TextView                         restaurantAdress;
	public               ImageButton                      callImageButon;
	public               ImageButton                      likeImageButon;
	public               ImageButton                      websiteImageButon;
	public               int                              mRatingScore;
	public               double                           mRatingScoreDouble;
	public               Drawable                         mEvaluationScore;
	private              RestaurantDetailsFragmentBinding mRestaurantDetailsFragmentBinding;
	
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
		
		displayingManagement();
		
	}
	
	public void displayingManagement() {
		Restaurant restaurant;
		restaurant = (Restaurant) getIntent().getSerializableExtra(EXTRA_RESTAURANT);
		
		restaurantPicture             = mRestaurantDetailsFragmentBinding.restaurantPicture;
		userInterestImageButon        = mRestaurantDetailsFragmentBinding.userInterestImageButton;
		restaurantName                = mRestaurantDetailsFragmentBinding.restaurantNameDetailsFragment;
		restaurantEvaluationImageView = mRestaurantDetailsFragmentBinding.restaurantEvaluationDetailsFragment;
		restaurantAdress              = mRestaurantDetailsFragmentBinding.restaurantAdressDetailsFragment;
		callImageButon                = mRestaurantDetailsFragmentBinding.imageButtonCall;
		likeImageButon                = mRestaurantDetailsFragmentBinding.imageButtonLike;
		websiteImageButon             = mRestaurantDetailsFragmentBinding.imageButtonWebsite;
		
		RequestOptions mRequestOption = new RequestOptions();
		mRequestOption.placeholder(R.drawable.ic_launcher_background);
		Glide
				.with(restaurantPicture.getContext())
				.load(restaurant.getUrlImage())
				.apply(mRequestOption)
				.into(restaurantPicture);
		
		restaurantName.setText(restaurant.getName());
		
		Glide
				.with(restaurantEvaluationImageView.getContext())
				.load(getRatingScorePicture(restaurant))
//				.placeholder(R.drawable.ic_launcher_background)
				.into(restaurantEvaluationImageView);
		
		restaurantAdress.setText(restaurant.getAdress());
		
		callImageButonManagement(restaurant);
		websiteImageButtonManagement(restaurant);
		likeImageButonManagement(restaurant);
		userInterestImageButonManagement(restaurant);
		
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
		displayingRightLikeImageButon();
		
		likeImageButon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			
			}
		});
	}
	
	public void userInterestImageButonManagement(Restaurant restaurant) {
		displayingRightUserInterestImageButon();
		
		likeImageButon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			
			}
		});
	}
	
	public void displayingRightLikeImageButon() {
//		likeImageButon.setImageResource(R.drawable.ic_star);
		likeImageButon.setImageResource(R.drawable.ic_star_border_24);
	}
	
	public void displayingRightUserInterestImageButon() {

//		userInterestImageButon.setImageResource(R.drawable.ic_baseline_check_circle_24);
		userInterestImageButon.setImageResource(R.drawable.ic_baseline_check_circle_green_24);
		
	}
}