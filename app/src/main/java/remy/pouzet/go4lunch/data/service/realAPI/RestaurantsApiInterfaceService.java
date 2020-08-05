package remy.pouzet.go4lunch.data.service.realAPI;

import remy.pouzet.go4lunch.data.service.realAPI.POJOdetailsRestaurants.ResponseOfPlaceDetailsRestaurants;
import remy.pouzet.go4lunch.data.service.realAPI.POJOmatrix.ResponseOfMatrix;
import remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList.ResponseOfRestaurantsList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Remy Pouzet on 23/07/2020.
 */
public interface RestaurantsApiInterfaceService {
	
	@GET("place/nearbysearch/json")
	Call<ResponseOfRestaurantsList> getResponseOfRestaurantsList(@Query("location") String location,
	                                                             @Query("radius") int radius,
	                                                             @Query("key") String key,
	                                                             @Query("type") String type);
	
	@GET("place/details/json?")
	Call<ResponseOfPlaceDetailsRestaurants> getResponseOfPlaceDetailsRestaurants(@Query("place_id") String placeID,
	                                                                             @Query("key") String key);
	
	@GET("place/details/json?")
	Call<ResponseOfMatrix> getResponseOfMatrix(@Query("units") String units,
	                                           @Query("origins") String origins,
	                                           @Query("destinations") String destinations,
	                                           @Query("key") String key);
	
}
