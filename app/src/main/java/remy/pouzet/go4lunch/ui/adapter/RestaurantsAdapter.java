package remy.pouzet.go4lunch.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.data.model.Restaurants;

/**
 * Created by Remy Pouzet on 14/07/2020.
 */
public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsViewHolder> {
	
	// FOR DATA
	private List<Restaurants> mRestaurants;
	
	// CONSTRUCTOR
	public RestaurantsAdapter(List<Restaurants> mRestaurants) {
		this.mRestaurants = mRestaurants;
	}
	
	@NonNull
	@Override
	public RestaurantsViewHolder onCreateViewHolder(ViewGroup parent,
	                                                int viewType) {
		// CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
		Context        context  = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		View           root     = inflater.inflate(R.layout.content_items_of_restaurants_list_view, parent, false);
		
		return new RestaurantsViewHolder(root);
	}
	
	// UPDATE VIEW HOLDER WITH A ARTICLES
	@Override
	public void onBindViewHolder(RestaurantsViewHolder viewHolder,
	                             int position) {
		viewHolder.updateWithRestaurants(this.mRestaurants.get(position));
		
	}
	
	// RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
	@Override
	public int getItemCount() {
		return this.mRestaurants.size();
	}
}