package remy.pouzet.go4lunch.data.repositories;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import remy.pouzet.go4lunch.BuildConfig;
import remy.pouzet.go4lunch.data.repositories.models.Restaurant;
import remy.pouzet.go4lunch.data.service.RetrofitService;
import remy.pouzet.go4lunch.data.service.realAPI.POJOdetailsRestaurants.ResponseOfPlaceDetailsRestaurants;
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
	
	private static RestaurantsRepository             restaurantsApiRepository;
	private final  RestaurantsApiInterfaceService    mRestaurantsApiInterfaceService;
	private        MutableLiveData<List<Restaurant>> restaurants;
	
	private RestaurantsRepository() {
		mRestaurantsApiInterfaceService = RetrofitService.cteateService(RestaurantsApiInterfaceService.class);
		restaurants                     = new MutableLiveData<>();
	}
	
	public static RestaurantsRepository getInstance() {
		if (restaurantsApiRepository == null) {
			restaurantsApiRepository = new RestaurantsRepository();
		}
		return restaurantsApiRepository;
	}
	
	public MutableLiveData<List<Restaurant>> getRestaurants(double lat,
	                                                        double lgn) {
		mRestaurantsApiInterfaceService
				.getResponseOfRestaurantsList((lat) + "," + (lgn), 15000, BuildConfig.apiKey, "restaurant")
				.enqueue(new Callback<ResponseOfRestaurantsList>() {
					@Override
					public void onResponse(Call<ResponseOfRestaurantsList> call,
					                       Response<ResponseOfRestaurantsList> response) {
						if (response.isSuccessful()) {
							
							getRestaurantsDetails(UtilsForRestaurantsList.generateRestaurantsFromRestaurantsList(response.body()));
							
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
	
	private void getRestaurantsDetails(List<Restaurant> restaurantdetails) {
		for (Restaurant restaurant : restaurantdetails) {
			mRestaurantsApiInterfaceService
					.getResponseOfPlaceDetailsRestaurants(restaurant.getMplaceID(), BuildConfig.apiKey)
					.enqueue(new Callback<ResponseOfPlaceDetailsRestaurants>() {
						@Override
						public void onResponse(Call<ResponseOfPlaceDetailsRestaurants> call,
						                       Response<ResponseOfPlaceDetailsRestaurants> response) {
							if (response.isSuccessful()) {
								restaurant.setName("test");
								restaurant.setAdress(response
										                     .body()
										                     .getResult()
										                     .getAdrAddress());
								restaurants.setValue(restaurantdetails);
								
							}
						}
						
						@Override
						public void onFailure(Call<ResponseOfPlaceDetailsRestaurants> call,
						                      Throwable t) {
							//TODO toast
						}
					});
			
		}

//		return restaurants;
	}
	
}
