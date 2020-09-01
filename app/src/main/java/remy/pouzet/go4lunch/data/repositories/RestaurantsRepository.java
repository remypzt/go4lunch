package remy.pouzet.go4lunch.data.repositories;

import android.content.SharedPreferences;
import android.location.Location;
import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;

import java.text.DecimalFormat;
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
	
	public static final String                         PREF_KEY_LATITUDE  = "PREF_KEY_LATITUDE";
	public static final String                         PREF_KEY_LONGITUDE = "PREF_KEY_LONGITUDE";
	private static      RestaurantsRepository          restaurantsApiRepository;
	public final        RestaurantsApiInterfaceService mRestaurantsApiInterfaceService;
	public              ImageView                      mPicture;
	public              String                         origin;
	public              String                         distance;
	public              String                         destination;
	public              String                         horairs, status;
	public  SharedPreferences                 mPreferences;
	private MutableLiveData<List<Restaurant>> restaurants;
	//	private             ContentItemsOfRestaurantsListViewBinding binding;
	private double                            latitude, longitude, destinationLat, destinationLng;
	private float unformatedDistance;
	
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
	
	public MutableLiveData<List<Restaurant>> getRestaurants(double userLat,
	                                                        double userLng) {
		mRestaurantsApiInterfaceService
				.getResponseOfRestaurantsList((userLat) + "," + (userLng), 15000, BuildConfig.apiKey, "restaurant")
				.enqueue(new Callback<ResponseOfRestaurantsList>() {
					@Override
					public void onResponse(Call<ResponseOfRestaurantsList> call,
					                       Response<ResponseOfRestaurantsList> response) {
						if (response.isSuccessful()) {
							
							getRestaurantsDetails(UtilsForRestaurantsList.generateRestaurantsFromRestaurantsList(response.body()), userLat, userLng);
							
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
	
	public void getRestaurantsDetails(List<Restaurant> restaurantdetails,
	                                  double userLat,
	                                  double userLng) {
		//LIMITE
//		for (Restaurant restaurant : restaurantdetails) {
		Restaurant restaurant = restaurantdetails.get(0);
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
								if (response
										    .body()
										    .getResult()
										    .getPhotos() == null) {
									restaurant.setUrlImage("https://i.ytimg.com/vi/OiH5YMXQwYg/maxresdefault.jpg");
								} else {
									restaurant.setUrlImage("https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photoreference=" + response
											.body()
											.getResult()
											.getPhotos()
											.get(0)
											.getPhotoReference() + "&key=" + BuildConfig.apiKey);
								}
								
								if (response
										    .body()
										    .getResult()
										    .getRating() != null) {
									restaurant.setEvaluation(response
											                         .body()
											                         .getResult()
											                         .getRating());
								}
								
								destinationLat = response
										.body()
										.getResult()
										.getGeometry()
										.getLocation()
										.getLat();
								destinationLng = response
										.body()
										.getResult()
										.getGeometry()
										.getLocation()
										.getLng();
								
								restaurant.setDistance(getDistance(destinationLat, destinationLng, userLat, userLng));
								
								restaurant.setHorair(getStatus(response));
								
								restaurant.setWebsite(response
										                      .body()
										                      .getResult()
										                      .getWebsite());
								restaurant.setMphoneNumber(response
										                           .body()
										                           .getResult()
										                           .getInternationalPhoneNumber());
								
								restaurants.setValue(restaurantdetails);
							}
						}
						
						@Override
						public void onFailure(Call<ResponseOfPlaceDetailsRestaurants> call,
						                      Throwable t) {
						}
					});
		}
//	}
	
	public String getDistance(double destinationLat,
	                          double destinationLng,
	                          double userLat,
	                          double userLng) {
		Location locationOrigin = new Location("");
		locationOrigin.setLatitude(userLat);
		locationOrigin.setLongitude(userLng);
		
		Location locationDestination = new Location("");
		locationDestination.setLatitude(destinationLat);
		locationDestination.setLongitude(destinationLng);
		
		DecimalFormat df1 = new DecimalFormat("0.0");
		DecimalFormat df2 = new DecimalFormat("00.0");
		DecimalFormat df3 = new DecimalFormat("000");
		DecimalFormat df4 = new DecimalFormat("0000");
		
		unformatedDistance = locationOrigin.distanceTo(locationDestination) / 1000;
		
		if (unformatedDistance > 1) {
			distance = df1.format(locationOrigin.distanceTo(locationDestination) / 1000) + " km";
		} else if (unformatedDistance > 10) {
			distance = df2.format(locationOrigin.distanceTo(locationDestination) / 1000) + " km";
		} else if (unformatedDistance > 100) {
			distance = df3.format(locationOrigin.distanceTo(locationDestination) / 1000) + " km";
		} else if (unformatedDistance > 1000) {
			distance = df4.format(locationOrigin.distanceTo(locationDestination) / 1000) + " km";
		} else {
			distance = df3.format(locationOrigin.distanceTo(locationDestination)) + " m";
		}
		
		return distance;
	}

//	}
	
	public String getStatus(Response<ResponseOfPlaceDetailsRestaurants> response) {
		if (response != null && response.body() != null && response
				                                                   .body()
				                                                   .getResult() != null && response
						                                                                           .body()
						                                                                           .getResult()
						                                                                           .getOpeningHours() != null && response
								                                                                                                         .body()
								                                                                                                         .getResult()
								                                                                                                         .getOpeningHours()
								                                                                                                         .getOpenNow() != null) {
			if (response
					.body()
					.getResult()
					.getOpeningHours()
					.getOpenNow()) {
				status = "ouvert";
			} else {
				status = "fermé";
			}
		} else {
			status = "horaires indisponibles";
		}
		return status;
	}
	
	public String getLocation() {
		origin = latitude + "|" + longitude;
		return origin;
	}
}
