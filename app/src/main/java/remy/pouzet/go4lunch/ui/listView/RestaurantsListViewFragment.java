package remy.pouzet.go4lunch.ui.listView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import remy.pouzet.go4lunch.data.model.Restaurants;
import remy.pouzet.go4lunch.databinding.FragmentRestaurantsListViewBinding;
import remy.pouzet.go4lunch.ui.adapter.RestaurantsAdapter;

// ------------------   Functions   ------------------- //
// ------------------   Callbacks   ------------------- //
// ------------------    Adapter    ------------------- //
// ------------------      Menu     ------------------- //
// ------------------ Miscellaneous ------------------- //
// ------------------     Intent    ------------------- //
// ------------------     Navigation & UI    ------------------- //

public class RestaurantsListViewFragment extends Fragment {

// ------------------   Variables   ------------------- //
	
	public  RecyclerView                       mRestaurantsReyclerView;
	private List<Restaurants>                  mRestaurants;
	private RestaurantsAdapter                 mRestaurantsAdapter;
	private RestaurantsListViewViewModel       mRestaurantsListViewViewModel;
	private FragmentRestaurantsListViewBinding mFragmentRestaurantsListViewBinding;
	
	// ------------------   LifeCycle   ------------------- //
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container,
	                         Bundle savedInstanceState) {
		mFragmentRestaurantsListViewBinding = FragmentRestaurantsListViewBinding.inflate(getLayoutInflater());
		mRestaurantsReyclerView             = mFragmentRestaurantsListViewBinding.fragmentRestaurantsRecyclerView;
		
		mRestaurantsListViewViewModel = ViewModelProviders
				.of(this)
				.get(RestaurantsListViewViewModel.class);
		configureRecyclerView();
		View rootView = mFragmentRestaurantsListViewBinding.getRoot();
		return rootView;
	}
	
	@Override
	public void onViewCreated(@NonNull View view,
	                          @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		RestaurantsListViewViewModel localViewModelMyNews = new RestaurantsListViewViewModel();

//		localViewModelMyNews.getRestaurants()
//				.observe(this, this::updateList);
	}
	
	private void configureRecyclerView() {
		
		Restaurants test = new Restaurants("test");
		// 3.1 - Reset list
		this.mRestaurants = new ArrayList<>();
		
		mRestaurants.add(test);
		
		// 3.2 - Create adapter passing the list of articles
		this.mRestaurantsAdapter = new RestaurantsAdapter(this.mRestaurants);
		// 3.3 - Attach the adapter to the recyclerview to populate items
		this.mRestaurantsReyclerView.setAdapter(this.mRestaurantsAdapter);
		// 3.4 - Set layout manager to position the items
		this.mRestaurantsReyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
	}
	
	public void updateList(List<Restaurants> restaurantsList) {
		mRestaurants.clear();
		if (restaurantsList != null) {
			mRestaurants.addAll(restaurantsList);
			mRestaurantsAdapter.notifyDataSetChanged();
		}
	}
}