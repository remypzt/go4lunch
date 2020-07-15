package remy.pouzet.go4lunch.ui.listView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import remy.pouzet.go4lunch.data.model.Restaurants;

public class RestaurantsListViewViewModel extends ViewModel {
	
	private MutableLiveData<List<Restaurants>> mRestaurants;
	
	private MutableLiveData<String> mText;
	
	public RestaurantsListViewViewModel() {
		
		mText = new MutableLiveData<>();
		mText.setValue("This is listview fragment");
//		mRestaurants.setValue()
	}
	
	public LiveData<List<Restaurants>> getRestaurants() {
		return mRestaurants;
	}
	
}