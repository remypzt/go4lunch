package remy.pouzet.go4lunch.view.adaptersAndViewHolders.restaurants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.data.repositories.models.Restaurant;
import remy.pouzet.go4lunch.databinding.ContentItemsOfRestaurantsListViewBinding;
import remy.pouzet.go4lunch.view.activities.RestaurantDetailsActivity;

/**
 * Created by Remy Pouzet on 14/07/2020.
 */
class RestaurantsViewHolder extends RecyclerView.ViewHolder {
	
	public int    mWorkmartesScore;
	public double mRatingScoreDouble;
	
	public  ImageView                                mPicture;
	public  TextView                                 mName;
	public  TextView                                 mAdress;
	public  TextView                                 mHorair;
	public  ConstraintLayout                         mConstraintLayout;
	public  TextView                                 mDistance;
	public  ImageView                                mEvaluation;
	public  ImageView                                mWorkmatesInterrested;
	public  Drawable                                 mEvaluationScore;
	public  ImageView                                mWorkmatesInterrestedScore;
	private ContentItemsOfRestaurantsListViewBinding mContentItemsOfRestaurantsListViewBinding;

// TODO worksmate and evaluation
	
	public RestaurantsViewHolder(View itemView) {
		super(itemView);
		
		mContentItemsOfRestaurantsListViewBinding = ContentItemsOfRestaurantsListViewBinding.bind(itemView);
		Context localContext = itemView.getContext();
		
		mEvaluation           = mContentItemsOfRestaurantsListViewBinding.evaluation;
		mWorkmatesInterrested = mContentItemsOfRestaurantsListViewBinding.interrestingWorkmatesNumber;
		mName                 = mContentItemsOfRestaurantsListViewBinding.restaurantName;
		mPicture              = mContentItemsOfRestaurantsListViewBinding.restaurantPicture;
		mAdress           = mContentItemsOfRestaurantsListViewBinding.restaurantAdress;
		mHorair           = mContentItemsOfRestaurantsListViewBinding.restaurantHorairInformations;
		mConstraintLayout = mContentItemsOfRestaurantsListViewBinding.ArticlesLayout;
		mDistance         = mContentItemsOfRestaurantsListViewBinding.restaurantDistanceFromTheUser;
		
	}
	
	@SuppressLint("SetTextI18n")
	public void updateWithRestaurants(Restaurant restaurant) {
		
		this.mName.setText(restaurant.getName());
		this.mAdress.setText(restaurant.getAdress());
		RequestOptions mRequestOption = new RequestOptions();
		mRequestOption.placeholder(R.drawable.ic_launcher_background);
		
		Glide
				.with(mPicture.getContext())
				.load(restaurant.getUrlImage())
				.apply(mRequestOption)
				.into(mPicture);
		
		Glide
				.with(mEvaluation.getContext())
				.load(getRatingScorePicture(restaurant))
				.into(mEvaluation);
		
		this.mDistance.setText(restaurant.getDistance());
		
		this.mHorair.setText(restaurant.getHorair());
		
		this.mConstraintLayout.setOnClickListener(v -> {
			RestaurantDetailsActivity.startActivity(mConstraintLayout.getContext(), restaurant);
			
		});

//		Glide
//				.with(mWorkmatesInterrested.getContext())
//				.load(
//						getWorkmatesScorePicture();
//				     )
////				.placeholder(R.drawable.ic_launcher_background)
//				.into(mWorkmatesInterrested);

//		this.mConstraintLayout.setOnClickListener(v -> {
//
//			this.activity = activity;
//			activity.startActivity(new Intent(activity, RestaurantDetails.class));
//}
	}
	
	public Drawable getRatingScorePicture(Restaurant restaurants) {
		mRatingScoreDouble = restaurants.getEvaluation();
		Resources resources = itemView.getResources();
		
		if (mRatingScoreDouble < 1.6) {
			mEvaluationScore = ResourcesCompat.getDrawable(resources, R.drawable.ic_star_border_24, null);
		} else if (mRatingScoreDouble > 3.3) {
			mEvaluationScore = ResourcesCompat.getDrawable(resources, R.drawable.ic_star, null);
		} else if (mRatingScoreDouble > 1.6 && mRatingScoreDouble < 3.3) {
			mEvaluationScore = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_star_half_24, null);
		} else {
			mEvaluationScore = ResourcesCompat.getDrawable(resources, R.drawable.invisible, null);
		}
		return mEvaluationScore;
	}

}


