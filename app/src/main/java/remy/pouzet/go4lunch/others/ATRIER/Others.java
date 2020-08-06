package remy.pouzet.go4lunch.others.ATRIER;

/**
 * Created by Remy Pouzet on 06/08/2020.
 */
class Others {

//
//	public String getDistanceFromMatrixAPI(String destination,
//	                                       double lat,
//	                                       double lgn) {
//
////		restaurant.setDistance(getDistanceFromMatrixAPI(destination, lat, lgn));
//
//		origin = lat + "|" + lgn;
//
//		mRestaurantsApiInterfaceService
//				.getResponseOfMatrix("metric", origin, destination, BuildConfig.apiKey)
//				.enqueue(new Callback<ResponseOfMatrix>() {
//					@Override
//					public void onResponse(Call<ResponseOfMatrix> call,
//					                       Response<ResponseOfMatrix> response) {
//						if (response.isSuccessful()) {
//
//							distance = response
//									.body()
//									.getRows()
//									.get(1)
//									.getElements()
//									.get(0)
//									.getDistance()
//									.toString();
//						}
//					}
//
//					@Override
//					public void onFailure(Call<ResponseOfMatrix> call,
//					                      Throwable t) {
//						//TODO toast
//
//					}
//				});
//
//		return distance;
//	}
//
//	public String getHorairs(Response<ResponseOfPlaceDetailsRestaurants> response) {
//		//Todo getTime
//		//Todo getdate
//		//todo switch date
//		//todo inside switch date get horair from date and get time
//		// if user time-open time is - it's close so display it and openhour
//		// if user time-open time is + it's open...
//		// or pass by OpenNow
//		// if user tim-open time is close to 0 display appropriate message
//		// how manage it when there's severals open-close the same day ?
//
//		response
//				.body()
//				.getResult()
//				.getOpeningHours()
//				.getOpenNow();
//		response
//				.body()
//				.getResult()
//				.getOpeningHours()
//				.getPeriods()
//				.get(0)
//				.getClose();
//
//		return horairs;
//
//	}

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
}
