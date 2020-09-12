package remy.pouzet.go4lunch;

import junit.framework.TestCase;

import org.junit.Test;

import remy.pouzet.go4lunch.data.service.realAPI.POJOdetailsRestaurants.ResponseOfPlaceDetailsRestaurants;
import remy.pouzet.go4lunch.data.service.realAPI.POJOdetailsRestaurants.Result;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
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