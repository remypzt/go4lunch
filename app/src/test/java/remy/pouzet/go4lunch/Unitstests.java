package remy.pouzet.go4lunch;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import remy.pouzet.go4lunch.data.repositories.RestaurantsRepository;
import remy.pouzet.go4lunch.data.service.realAPI.POJOdetailsRestaurants.ResponseOfPlaceDetailsRestaurants;
import remy.pouzet.go4lunch.data.service.realAPI.POJOdetailsRestaurants.Result;
import remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList.ResponseOfRestaurantsList;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class Unitstests {
	@Test
	public void addition_isCorrect() {
		assertEquals(4, 2 + 2);
	}
	
	@Test
	public void testingGenerateRestaurant() {
		ResponseOfPlaceDetailsRestaurants localResponseOfPlaceDetailsRestaurants = new ResponseOfPlaceDetailsRestaurants();
		Result                            localResult                            = new Result();
		localResult.setName("test");
		localResponseOfPlaceDetailsRestaurants.setResult(localResult);
		TestCase.assertEquals("test", localResponseOfPlaceDetailsRestaurants
				.getResult()
				.getName());
	}
	
	@Test
	public void testingStatus() {
		RestaurantsRepository localRestaurantsRepository = new RestaurantsRepository();

//		TestCase.assertTrue(localRestaurantsRepository.openingStatus());
	
	}
	
	@Test
	public void testingDistance() {
		RestaurantsRepository localRestaurantsRepository = new RestaurantsRepository();
		String                test                       = localRestaurantsRepository.getDistance(37.3026313, -121.864222, 37.2586033, -121.845295);
		TestCase.assertEquals("000 m", test);
//		TestCase.assertEquals("5.2 km", test);
	}
	
	@Test
	public void testingResponseOfRestaurantList() {
		List<remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList.Result> localResultList                = new ArrayList<>();
		remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList.Result       localResult                    = new remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList.Result();
		ResponseOfRestaurantsList                                                  localResponseOfRestaurantsList = new ResponseOfRestaurantsList();
		localResult.setId("testID");
		localResultList.add(localResult);
		localResponseOfRestaurantsList.setResults(localResultList);
		TestCase.assertEquals("testID", localResponseOfRestaurantsList
				.getResults()
				.get(0)
				.getId());
	}
	
	@Test
	public void testingResponseOfRestaurantListSize() {
		List<remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList.Result> localResultList                = new ArrayList<>();
		remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList.Result       localResult                    = new remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList.Result();
		ResponseOfRestaurantsList                                                  localResponseOfRestaurantsList = new ResponseOfRestaurantsList();
		localResultList.add(localResult);
		localResponseOfRestaurantsList.setResults(localResultList);
		TestCase.assertEquals(1, localResponseOfRestaurantsList
				.getResults()
				.size());
	}

//	@Test
//	public void testingAddArticlesFromRestaurantList() {
//		remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList.Result localResult  = new remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList.Result();
//		Restaurant localRestaurant = new Restaurant("Test", null, "testname", "test", "test", "test", 1, 1, "test", "test", 0.0, 0.0);
//		localResult.setName("testname");
//		localResult.setPlaceId("test");
////		localResult.setGeometry(null);
////		localResult.getGeometry().setLocation(null);
//		localResult.getGeometry().getLocation().setLat(0.0);
//		localResult.getGeometry().getLocation().setLng(0.0);
//
//		localRestaurant = UtilsForRestaurantsList.addArticleFromRestaurantsList(localResult);
//		String test = localRestaurant.getName();
//		TestCase.assertEquals("testname", localRestaurant.getName());
////		remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList.Result       localResult     = new remy.pouzet.go4lunch.data.service.realAPI.POJOrestaurantsList.Result();
////		Restaurant localRestaurant = new Restaurant("Test", null, "test", "test", "test", "test", 1, 1, "test", "test", 0.0, 0.0);
////		localResult.setPlaceId("placeID");
////		localRestaurant = UtilsForRestaurantsList.addArticleFromRestaurantsList(localResult);
////		TestCase.assertEquals("Test", localRestaurant.getMplaceID());
//
//	}

//	@Test
//	public void testGetDetailsRestaurantActivity() throws
//	                                               Exception {
//		MainActivity localMainActivity = mock(MainActivity.class);
//
////		when(localMainActivity.currentUserBis).then(FirebaseAuth.getInstance());
//
//	}

//	@Test
//	public void testingSearchFunction() {
//		MainActivity      localMainActivity = new MainActivity();
//		PlacesClient      placesClient      = Places.createClient(localMainActivity);
//		RectangularBounds bounds            = RectangularBounds.newInstance(getCoordinate(37.16877177158805, -121.94460739211524,-10000, -10000), getCoordinate(37.348434828411946, -121.74598260788474, 10000, 10000));
////		localMainActivity.searchFunction("fairmont", bounds, placesClient);
//
//
////		ResponseOfPlaceDetailsRestaurants localResponseOfPlaceDetailsRestaurants = new ResponseOfPlaceDetailsRestaurants();
////		Result                            localResult                            = new Result();
//
//		TestCase.assertTrue(localMainActivity.searchFunction("fairmont", bounds, placesClient));
//	}

//	@Test
//	public void testUserToken() throws Exception {
//
//		MainActivity localMainActivity = mock(MainActivity.class);
//		when(localMainActivity.getTokenFCM()).thenReturn("FakeToken");
//
//		String token = localMainActivity.getTokenFCM();
//
//		// Test token
//	}

}