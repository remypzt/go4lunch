package remy.pouzet.go4lunch.data.repositories;

import android.content.SharedPreferences;
import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import remy.pouzet.go4lunch.BuildConfig;
import remy.pouzet.go4lunch.data.repositories.models.Restaurant;
import remy.pouzet.go4lunch.data.service.RetrofitService;
import remy.pouzet.go4lunch.data.service.realAPI.POJOdetailsRestaurants.ResponseOfPlaceDetailsRestaurants;
import remy.pouzet.go4lunch.data.service.realAPI.POJOmatrix.ResponseOfMatrix;
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
	
	public static final String                                   PREF_KEY_LATITUDE  = "PREF_KEY_LATITUDE";
	public static final String                                   PREF_KEY_LONGITUDE = "PREF_KEY_LONGITUDE";
	private static      RestaurantsRepository                    restaurantsApiRepository;
	private final       RestaurantsApiInterfaceService           mRestaurantsApiInterfaceService;
	public              ImageView                                mPicture;
	public              String                                   origin;
	public              String                                   distance;
	public              String                                   destination;
	public              String                                   horairs;
	public              SharedPreferences                        mPreferences;
	private             MutableLiveData<List<Restaurant>>        restaurants;
//	private             ContentItemsOfRestaurantsListViewBinding binding;
	private             double                                   latitude, longitude;
	
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
		//Todo pass by loop when project is finish ( it's like that for limitate google API free request)
//		for (Restaurant restaurant : restaurantdetails) {
		Restaurant restaurant = restaurantdetails.get(1);
		mRestaurantsApiInterfaceService
				.getResponseOfPlaceDetailsRestaurants(restaurant.getMplaceID(), BuildConfig.apiKey)
				.enqueue(new Callback<ResponseOfPlaceDetailsRestaurants>() {
					@Override
					public void onResponse(Call<ResponseOfPlaceDetailsRestaurants> call,
					                       Response<ResponseOfPlaceDetailsRestaurants> response) {
						if (response.isSuccessful()) {
							
							destination = "place_id:" + restaurant.getMplaceID();
							
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
							
							restaurant.setEvaluation(response
									                         .body()
									                         .getResult()
									                         .getRating());

//							restaurant.setDistance(getDistanceFromMatrixAPI(destination));

//							restaurant.setHorair(getHorairs(response));

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
	
	public String getDistanceFromMatrixAPI(String destination) {
		
		origin = getLocation();
		
		mRestaurantsApiInterfaceService
				.getResponseOfMatrix("metric", origin, destination, BuildConfig.apiKey)
				.enqueue(new Callback<ResponseOfMatrix>() {
					@Override
					public void onResponse(Call<ResponseOfMatrix> call,
					                       Response<ResponseOfMatrix> response) {
						if (response.isSuccessful()) {
							
							distance = response
									.body()
									.getRows()
									.get(0)
									.getElements()
									.get(0)
									.getDistance()
									.toString();
						}
					}
					
					@Override
					public void onFailure(Call<ResponseOfMatrix> call,
					                      Throwable t) {
						//TODO toast
						
					}
				});
		
		return distance;
	}
	
	public String getHorairs(Response<ResponseOfPlaceDetailsRestaurants> response) {
		//Todo getTime
		//Todo getdate
		//todo switch date
		//todo inside switch date get horair from date and get time
		// if user time-open time is - it's close so display it and openhour
		// if user time-open time is + it's open...
		// or pass by OpenNow
		// if user tim-open time is close to 0 display appropriate message
		// how manage it when there's severals open-close the same day ?
		
		response
				.body()
				.getResult()
				.getOpeningHours()
				.getOpenNow();
		response
				.body()
				.getResult()
				.getOpeningHours()
				.getPeriods()
				.get(0)
				.getClose();
		
		return horairs;
		
	}

//
//	public void onLocationResult(LocationResult locationResult) {
//		if (locationResult == null) {
//			return;
//		}
//		for (Location location : locationResult.getLocations()) {
//			if (location != null) {
//				latitude  = location.getLatitude();
//				longitude = location.getLongitude();
//			}
//		}
//	}
	
	public String getLocation() {
//		Location location = null;
//		latitude  = location.getLatitude();
//		longitude = location.getLongitude();
		
		//TODO get location as origin parameter

//		mPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
//		latitude     = getDouble(mPreferences, PREF_KEY_LATITUDE, 2.0);
//		longitude    = getDouble(mPreferences, PREF_KEY_LONGITUDE, 2.0);

//		onLocationResult();
		
		origin = latitude + "|" + longitude;
		
		return origin;
	}
	
	public double getDouble(final SharedPreferences prefs,
	                        final String key,
	                        final double defaultValue) {
		return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
	}
}

//}
