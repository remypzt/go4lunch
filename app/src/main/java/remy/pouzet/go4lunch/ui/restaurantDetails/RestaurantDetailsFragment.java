package remy.pouzet.go4lunch.ui.restaurantDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import remy.pouzet.go4lunch.R;

public class RestaurantDetailsFragment extends Fragment {
	
	private RestaurantDetailsViewModel mViewModel;
	
	public static RestaurantDetailsFragment newInstance() {
		return new RestaurantDetailsFragment();
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.restaurant_details_fragment, container, false);
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mViewModel = ViewModelProviders
				.of(this)
				.get(RestaurantDetailsViewModel.class);
		// TODO: Use the ViewModel
	}
	
}