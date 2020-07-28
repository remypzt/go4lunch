package remy.pouzet.go4lunch.data.repositories;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import remy.pouzet.go4lunch.BuildConfig;
import remy.pouzet.go4lunch.data.repositories.model.Restaurant;
import remy.pouzet.go4lunch.data.service.RetrofitService;
import remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList.ResponseOfRestaurantsList;
import remy.pouzet.go4lunch.data.service.realAPI.RestaurantsApiInterfaceService;
import remy.pouzet.go4lunch.others.utils.UtilsForRestaurantsList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Remy Pouzet on 23/07/2020.
 */
public class RestaurantsRepository {
	
	private static RestaurantsRepository          restaurantsApiRepository;
	private final  RestaurantsApiInterfaceService mRestaurantsApiInterfaceService;
	
	private RestaurantsRepository() {
		mRestaurantsApiInterfaceService = RetrofitService.cteateService(RestaurantsApiInterfaceService.class);
	}
	
	public static RestaurantsRepository getInstance() {
		if (restaurantsApiRepository == null) {
			restaurantsApiRepository = new RestaurantsRepository();
		}
		return restaurantsApiRepository;
	}
	
	public MutableLiveData<List<Restaurant>> getRestaurants(double lat,
	                                                        double lgn) {
		MutableLiveData<List<Restaurant>> restaurants = new MutableLiveData<>();
		mRestaurantsApiInterfaceService
				.getResponseOfRestaurantsList((lat) + "," + (lgn), 15000, BuildConfig.apiKey, "restaurant")
				.enqueue(new Callback<ResponseOfRestaurantsList>() {
					@Override
					public void onResponse(Call<ResponseOfRestaurantsList> call,
					                       Response<ResponseOfRestaurantsList> response) {
						if (response.isSuccessful()) {
							restaurants.setValue(UtilsForRestaurantsList.generateRestaurantsFromRestaurantsList(response.body()));
						} else {
							restaurants.setValue(null);
						}
					}
					
					@Override
					public void onFailure(Call<ResponseOfRestaurantsList> call,
					                      Throwable t) {
						restaurants.setValue(null);
					}
				});
		return restaurants;
	}
	
}
