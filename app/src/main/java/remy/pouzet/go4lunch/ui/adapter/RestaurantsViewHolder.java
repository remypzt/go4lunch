package remy.pouzet.go4lunch.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import remy.pouzet.go4lunch.data.model.Restaurants;
import remy.pouzet.go4lunch.databinding.ContentItemsOfRestaurantsListViewBinding;

/**
 * Created by Remy Pouzet on 14/07/2020.
 */
class RestaurantsViewHolder extends RecyclerView.ViewHolder {
	
	private ContentItemsOfRestaurantsListViewBinding mContentItemsOfRestaurantsListViewBinding;
	
	public  ImageView        mPicture;
	public  TextView         mName;
	public  TextView         mType;
	public  TextView         mAdress;
	public  TextView         mHorair;
	public  ConstraintLayout mConstraintLayout;
	public  TextView         mDistance;
	private Activity         activity;
// TODO worksmate and evaluation
	
	RestaurantsViewHolder(View itemView) {
		super(itemView);
		
		mContentItemsOfRestaurantsListViewBinding = ContentItemsOfRestaurantsListViewBinding.bind(itemView);
		Context localContext = itemView.getContext();
		
		mName = mContentItemsOfRestaurantsListViewBinding.restaurantName;
//		 = mContentItemsOfRestaurantsListViewBinding.restaurantPicture;

// = mContentItemsOfRestaurantsListViewBinding.restaurantType;
// = mContentItemsOfRestaurantsListViewBinding.restaurantAdress;
// = mContentItemsOfRestaurantsListViewBinding.restaurantHorairInformations;
// = mContentItemsOfRestaurantsListViewBinding.ArticlesLayout;
// = mContentItemsOfRestaurantsListViewBinding.restaurantDistanceFromTheUser;
	}
	
	@SuppressLint("SetTextI18n")
	void updateWithRestaurants(Restaurants restaurants) {
		
		restaurants.setName("test");
		mName.setText("test");
		this.mName.setText("test");
		this.mName.setText(restaurants.getName());
		
	}
//		Glide
////				.with(mPicture.getContext())
////				.load(restaurants.getUrlImage())
////				.into(mPicture);
////		}
////
//		this.mName.setText(restaurants.getName());
//		this.mHorair.setText(restaurants.getDate());
//		this.mAdress.setText(restaurants.getTitle());

//		this.mConstraintLayout.setOnClickListener(v -> {
//
//			this.activity = activity;
//			activity.startActivity(new Intent(activity, RestaurantDetails.class));
//
//		}
}

