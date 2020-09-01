package remy.pouzet.go4lunch.others.utils;

import java.util.ArrayList;
import java.util.List;

import remy.pouzet.go4lunch.data.repositories.models.Restaurant;
import remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList.ResponseOfRestaurantsList;
import remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList.Result;

/**
 * Created by Remy Pouzet on 28/07/2020.
 */
public class UtilsForRestaurantsList {
	
	public static List<Restaurant> generateRestaurantsFromRestaurantsList(ResponseOfRestaurantsList responseOfRestaurantsList) {
		List<Restaurant> restauraurantsListRestaurants = new ArrayList<>();
		if (responseOfRestaurantsList != null) {
			List<Result> resultsRestaurantsList = responseOfRestaurantsList.getResults();
			for (int x = 0;
			     x <= resultsRestaurantsList.size() - 1;
			     x++) {
				restauraurantsListRestaurants.add(addArticleFromRestaurantsList(resultsRestaurantsList.get(x)));
			}
		}
		return restauraurantsListRestaurants;
	}
	
	private static Restaurant addArticleFromRestaurantsList(Result resultsItemOfRestaurantsList) {
		return new Restaurant(resultsItemOfRestaurantsList.getPlaceId(), "multimediaUrl", "Name", "Adress", "Horair", "Distance", 1, 2, "phone number", "website", resultsItemOfRestaurantsList
				.getGeometry()
				.getLocation()
				.getLat(), resultsItemOfRestaurantsList
				                      .getGeometry()
				                      .getLocation()
				                      .getLng());
	}
	
}
