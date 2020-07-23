package remy.pouzet.go4lunch.data.repositories;

/**
 * Created by Remy Pouzet on 23/07/2020.
 */
class RestaurantsRepository {
	
	private static RestaurantsRepository restaurantsApiRepository;
//	private final  RestaurantsApiInterfaceService mRestaurantsApiInterfaceService;
	
	private RestaurantsRepository() {
//		mRestaurantsApiInterfaceService = RetrofitService.cteateService(RestaurantsApiInterfaceService.class);
	}
	
	public static RestaurantsRepository getInstance() {
		if (restaurantsApiRepository == null) {
			restaurantsApiRepository = new RestaurantsRepository();
		}
		return restaurantsApiRepository;
	}

//	public MutableLiveData<List<Restaurants>> getRestaurants(String section) {
//		MutableLiveData<List<Restaurants>> restaurants = new MutableLiveData<>();
//		mRestaurantsApiInterfaceService
//				.getResponseOf
//				.enqueue(new Callback<ResponseOfRestaurants>() {
//					@Override
//					public void onResponse(Call<ResponseOfRestaurants> call,
//					                       Response<ResponseOfRestaurants> response) {
//						if (response.isSuccessful()) {
//							restaurants.setValue(UtilsForRestaurants.generateArticlesFromRestaurants(response.body()));
//						} else {
//							restaurants.setValue(null);
//						}
//					}
//
//					@Override
//					public void onFailure(Call<ResponseOfRestaurants> call,
//					                      Throwable t) {
//						restaurants.setValue(null);
//					}
//				});
//		return restaurants;
//	}

}
