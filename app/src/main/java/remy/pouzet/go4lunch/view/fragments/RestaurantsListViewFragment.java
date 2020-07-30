package remy.pouzet.go4lunch.view.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import remy.pouzet.go4lunch.data.repositories.models.Restaurant;
import remy.pouzet.go4lunch.databinding.FragmentRestaurantsListViewBinding;
import remy.pouzet.go4lunch.view.adaptersAndViewHolders.RestaurantsAdapter;
import remy.pouzet.go4lunch.viewmodel.RestaurantsListViewViewModel;

// ------------------   Functions   ------------------- //
// ------------------   Callbacks   ------------------- //
// ------------------    Adapter    ------------------- //
// ------------------      Menu     ------------------- //
// ------------------ Miscellaneous ------------------- //
// ------------------     Intent    ------------------- //
// ------------------     Navigation & UI    ------------------- //

public class RestaurantsListViewFragment extends Fragment {

// ------------------   Variables   ------------------- //
	
	public RecyclerView mRestaurantsReyclerView;
	
	private             List<Restaurant>   mRestaurants;
	private             RestaurantsAdapter mRestaurantsAdapter;
	public static final String             PREF_KEY_LATITUDE  = "PREF_KEY_LATITUDE";
	public static final String             PREF_KEY_LONGITUDE = "PREF_KEY_LONGITUDE";
	public              SharedPreferences  mPreferences;
	private             double             latitude, longitude;
	
	// ------------------   LifeCycle   ------------------- //
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container,
	                         Bundle savedInstanceState) {

//		mRestaurantsListViewViewModel = ViewModelProviders
//				.of(this)
//				.get(RestaurantsListViewViewModel.class);
//
		//	private RestaurantsListViewViewModel       mRestaurantsListViewViewModel;
		remy.pouzet.go4lunch.databinding.FragmentRestaurantsListViewBinding localFragmentRestaurantsListViewBinding = FragmentRestaurantsListViewBinding.inflate(getLayoutInflater());
		mRestaurantsReyclerView = localFragmentRestaurantsListViewBinding.fragmentRestaurantsRecyclerView;
		View rootView = localFragmentRestaurantsListViewBinding.getRoot();
		this.configureRecyclerView();
		return rootView;
	}
	
	@Override
	public void onViewCreated(@NonNull View view,
	                          @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		getLocation();
		RestaurantsListViewViewModel localViewModelMyNews = new RestaurantsListViewViewModel(latitude, longitude);
		
		localViewModelMyNews
				.getRestaurants()
				.observe((LifecycleOwner) requireContext(), this::updateList);
	}
	
	public void getLocation() {
		mPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
		latitude     = getDouble(mPreferences, PREF_KEY_LATITUDE, 2.0);
		longitude    = getDouble(mPreferences, PREF_KEY_LONGITUDE, 2.0);
	}
	
	public double getDouble(final SharedPreferences prefs,
	                        final String key,
	                        final double defaultValue) {
		return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
	}
	
	public void updateList(List<Restaurant> restaurantsList) {
		mRestaurants.clear();
		if (restaurantsList != null) {
			mRestaurants.addAll(restaurantsList);
			mRestaurantsAdapter.notifyDataSetChanged();
		}
	}
	
	private void configureRecyclerView() {
		
		// 3.1 - Reset list
		this.mRestaurants = new ArrayList<>();
		
		// 3.2 - Create adapter passing the list of articles
		this.mRestaurantsAdapter = new RestaurantsAdapter(this.mRestaurants);
		// 3.3 - Attach the adapter to the recyclerview to populate items
		this.mRestaurantsReyclerView.setAdapter(this.mRestaurantsAdapter);
		// 3.4 - Set layout manager to position the items
		this.mRestaurantsReyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
	}
}