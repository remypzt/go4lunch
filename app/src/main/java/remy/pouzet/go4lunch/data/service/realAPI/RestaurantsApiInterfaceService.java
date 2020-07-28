package remy.pouzet.go4lunch.data.service.realAPI;

import remy.pouzet.go4lunch.BuildConfig;
import remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList.ResponseOfRestaurantsList;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Remy Pouzet on 23/07/2020.
 */
public interface RestaurantsApiInterfaceService {
	@GET(BuildConfig.apiKey)
	Call<ResponseOfRestaurantsList> getResponseOfRestaurantsList();
	
}
