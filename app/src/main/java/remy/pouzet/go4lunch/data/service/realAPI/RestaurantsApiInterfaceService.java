package remy.pouzet.go4lunch.data.service.realAPI;

import remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList.ResponseOfRestaurantsList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Remy Pouzet on 23/07/2020.
 */
public interface RestaurantsApiInterfaceService {
	
	@GET("json"
// AIzaSyAyT25ijQ8hyslxHA7HZumtLD4emIudaLI
//         BuildConfig.apiKey
	     )
	Call<ResponseOfRestaurantsList> getResponseOfRestaurantsList(@Query("location") String location,
	                                                             @Query("radius") int radius,
	                                                             @Query("key") String key,
	                                                             @Query("type") String type);
}
