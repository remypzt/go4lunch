package remy.pouzet.go4lunch.data.service.realAPI;

import remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList.ResponseOfRestaurantsList;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Remy Pouzet on 23/07/2020.
 */
public interface RestaurantsApiInterfaceService {
	@GET("json?query=123+main+street&key=AIzaSyAyT25ijQ8hyslxHA7HZumtLD4emIudaLI"
//BuildConfig.apiKey
	     )
	Call<ResponseOfRestaurantsList> getResponseOfRestaurantsList();
	
}
