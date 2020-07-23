package remy.pouzet.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import remy.pouzet.go4lunch.data.repositories.model.Restaurants;

public class RestaurantsListViewViewModel extends ViewModel {
	
	private MutableLiveData<List<Restaurants>> mRestaurants;
	

	
	public RestaurantsListViewViewModel() {

//		RestaurantsRepository localRestaurantsRepository = RestaurantsRepository.getInstance();
//		mRestaurants = localRestaurantsRepository ;
	
	}
	
	public LiveData<List<Restaurants>> getRestaurants() {
		return mRestaurants;
	}
	
}