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
	public  ImageView                                mPicture          = mContentItemsOfRestaurantsListViewBinding.restaurantPicture;
	public  TextView                                 mName             = mContentItemsOfRestaurantsListViewBinding.restaurantName;
	public  TextView                                 mType             = mContentItemsOfRestaurantsListViewBinding.restaurantType;
	public  TextView                                 mAdress           = mContentItemsOfRestaurantsListViewBinding.restaurantAdress;
	public  TextView                                 mHorair           = mContentItemsOfRestaurantsListViewBinding.restaurantHorairInformations;
	public  ConstraintLayout                         mConstraintLayout = mContentItemsOfRestaurantsListViewBinding.ArticlesLayout;
	public  TextView                                 mDistance         = mContentItemsOfRestaurantsListViewBinding.restaurantDistanceFromTheUser;
	private Activity                                 activity;
// TODO worksmate and evaluation
	
	RestaurantsViewHolder(View itemView) {
		super(itemView);
		ContentItemsOfRestaurantsListViewBinding.bind(itemView);
		Context localContext = itemView.getContext();
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

