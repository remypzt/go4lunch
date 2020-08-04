package remy.pouzet.go4lunch.data.repositories;

import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import remy.pouzet.go4lunch.BuildConfig;
import remy.pouzet.go4lunch.data.repositories.models.Restaurant;
import remy.pouzet.go4lunch.data.service.RetrofitService;
import remy.pouzet.go4lunch.data.service.realAPI.POJOdetailsRestaurants.ResponseOfPlaceDetailsRestaurants;
import remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList.ResponseOfRestaurantsList;
import remy.pouzet.go4lunch.data.service.realAPI.RestaurantsApiInterfaceService;
import remy.pouzet.go4lunch.databinding.ContentItemsOfRestaurantsListViewBinding;
import remy.pouzet.go4lunch.others.utils.UtilsForRestaurantsList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Remy Pouzet on 23/07/2020.
 */
public class RestaurantsRepository {
	
	private static RestaurantsRepository                    restaurantsApiRepository;
	private final  RestaurantsApiInterfaceService           mRestaurantsApiInterfaceService;
	public         ImageView                                mPicture;
	private        MutableLiveData<List<Restaurant>>        restaurants;
	private        ContentItemsOfRestaurantsListViewBinding binding;
	
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
//		 mPicture = binding.restaurantPicture;
		//Todo pass by loop when project is finish ( for limitate google API free request)
//		for (Restaurant restaurant : restaurantdetails) {
		Restaurant restaurant = restaurantdetails.get(1);
		mRestaurantsApiInterfaceService
				.getResponseOfPlaceDetailsRestaurants(restaurant.getMplaceID(), BuildConfig.apiKey)
				.enqueue(new Callback<ResponseOfPlaceDetailsRestaurants>() {
					@Override
					public void onResponse(Call<ResponseOfPlaceDetailsRestaurants> call,
					                       Response<ResponseOfPlaceDetailsRestaurants> response) {
						if (response.isSuccessful()) {
							restaurant.setName(response
									                   .body()
									                   .getResult()
									                   .getName());
							restaurant.setAdress(response
									                     .body()
									                     .getResult()
									                     .getFormattedAddress());
							restaurant.setUrlImage("https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photoreference=" + response
									.body()
									.getResult()
									.getPhotos()
									.get(0)
									.getPhotoReference() + "&key=" + BuildConfig.apiKey);

//								restaurant.setEvaluation();

//								restaurant.setDistance();

//								restaurant.setHorair();
//								restaurant.setType();
//								restaurant.setWorkmatesInterrested();
							
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

//}
