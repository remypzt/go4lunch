package remy.pouzet.go4lunch.view.adaptersAndViewHolders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import remy.pouzet.go4lunch.data.repositories.model.Restaurant;
import remy.pouzet.go4lunch.databinding.ContentItemsOfRestaurantsListViewBinding;

/**
 * Created by Remy Pouzet on 14/07/2020.
 */
class RestaurantsViewHolder extends RecyclerView.ViewHolder {
	
	public  ImageView                                mPicture;
	public  TextView                                 mName;
	public  TextView                                 mType;
	public  TextView                                 mAdress;
	public  TextView                                 mHorair;
	public  ConstraintLayout                         mConstraintLayout;
	public  TextView                                 mDistance;
	private ContentItemsOfRestaurantsListViewBinding mContentItemsOfRestaurantsListViewBinding;
	private Activity                                 activity;
// TODO worksmate and evaluation
	
	RestaurantsViewHolder(View itemView) {
		super(itemView);
		
		mContentItemsOfRestaurantsListViewBinding = ContentItemsOfRestaurantsListViewBinding.bind(itemView);
		Context localContext = itemView.getContext();
		
		mName    = mContentItemsOfRestaurantsListViewBinding.restaurantName;
		mPicture = mContentItemsOfRestaurantsListViewBinding.restaurantPicture;
//		mType = mContentItemsOfRestaurantsListViewBinding.restaurantType;
		mAdress = mContentItemsOfRestaurantsListViewBinding.restaurantAdress;
		mHorair = mContentItemsOfRestaurantsListViewBinding.restaurantHorairInformations;
		//mConstraintLayout = mContentItemsOfRestaurantsListViewBinding.ArticlesLayout;
		mDistance = mContentItemsOfRestaurantsListViewBinding.restaurantDistanceFromTheUser;
	}
	
	@SuppressLint("SetTextI18n")
	void updateWithRestaurants(Restaurant restaurants) {
		
		this.mName.setText(restaurants.getName());
		this.mHorair.setText(restaurants.getHorair());
//		this.mType.setText(restaurants.getType());
		this.mAdress.setText(restaurants.getAdress());
		this.mDistance.setText(restaurants.getDistance());
		
		Glide
				.with(mPicture.getContext())
				.load(restaurants.getUrlImage())
//				.placeholder(R.drawable.ic_launcher_background)
				.into(mPicture);

//		this.mConstraintLayout.setOnClickListener(v -> {
//
//			this.activity = activity;
//			activity.startActivity(new Intent(activity, RestaurantDetails.class));
//}
	}
}

