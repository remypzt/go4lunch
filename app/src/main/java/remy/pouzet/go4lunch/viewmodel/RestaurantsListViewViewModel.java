package remy.pouzet.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import remy.pouzet.go4lunch.data.repositories.RestaurantsRepository;
import remy.pouzet.go4lunch.data.repositories.model.Restaurant;

public class RestaurantsListViewViewModel extends ViewModel {
	
	private MutableLiveData<List<Restaurant>> mRestaurants;
	
	public RestaurantsListViewViewModel(double lat,
	                                    double lng) {
		
		RestaurantsRepository localRestaurantsRepository = RestaurantsRepository.getInstance();
		mRestaurants = localRestaurantsRepository.getRestaurants(lat, lng);
		
	}
	
	public LiveData<List<Restaurant>> getRestaurants() {
		return mRestaurants;
	}
	
	public LiveData<List<Restaurant>> getRestaurantsDetails() {
		return mRestaurants;
	}
	
}