package remy.pouzet.go4lunch.others.ATRIER;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import remy.pouzet.go4lunch.view.fragments.RestaurantDetails;

/**
 * get from this tuto https://www.youtube.com/watch?v=Iz4y0ofVTk4
 */

public class GetNearbyPlaces extends AsyncTask<Object, String, String> {
	private String googleplaceData, url;
	private GoogleMap mMap;
	private Activity  activity;
	
	public GetNearbyPlaces(Activity activity) {
		this.activity = activity;
	}

//
//	Activity activity;
//	private Context   actvitiyContext = activity.getApplicationContext();
	
	@Override
	protected String doInBackground(Object... objects) {
		mMap = (GoogleMap) objects[0];
		url  = (String) objects[1];
		
		DownloadUrl downloadUrl = new DownloadUrl();
		try {
			googleplaceData = downloadUrl.ReadTheURL(url);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return googleplaceData;
	}
	
	@Override
	protected void onPostExecute(String s) {
		List<HashMap<String, String>> nearByPlacesList = null;
		DataParser                    dataParser       = new DataParser();
		nearByPlacesList = dataParser.parse(s);
		
		DisplayNearbyPlaces(nearByPlacesList);
//		activity.startActivity(new Intent(activity, RestaurantDetails.class));
	}
	
	private void DisplayNearbyPlaces(List<HashMap<String, String>> nearByPlacesList) {
		
		for (int i = 0;
		     i < nearByPlacesList.size();
		     i++) {
			MarkerOptions markerOptions = new MarkerOptions();
			
			HashMap<String, String> googleNearbyPlace = nearByPlacesList.get(i);
			String                  nameOfPlace       = googleNearbyPlace.get("place_name");
			String                  vicinity          = googleNearbyPlace.get("vicinity");
			double                  lat               = Double.parseDouble(Objects.requireNonNull(googleNearbyPlace.get("lat")));
			double                  lng               = Double.parseDouble(Objects.requireNonNull(googleNearbyPlace.get("lng")));
			
			LatLng latLng = new LatLng(lat, lng);
			markerOptions
					.position(latLng)
					.title(nameOfPlace
//					       TODO check how fix vicinity
//					       + " : " + vicinity
					      )
//			.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_your_lunch))
					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//			//TODO define an id for each marker will be use to define right intent
////			String id = marker.getId();
//			markerMap.put(id, "action_one");
			
			mMap.addMarker(markerOptions);
			mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
			mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
			mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
				@Override
				public void onInfoWindowClick(Marker parameterMarker) {
					activity.startActivity(new Intent(activity, RestaurantDetails.class));
					
					//TODO define an id for each marker will be use to define right intent
//					String actionId = markerMap.get(marker.getId());
//					if (actionId.equals("action_one")) {
//						Intent i = new Intent(MainActivity.this, ActivityOne.class);
//					markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//					mMap.addMarker(markerOptions);
//						startActivity(i);
//					} else if (actionId.equals("action_two")) {
//						Intent i = new Intent(MainActivity.this, ActivityTwo.class);
//						startActivity(i);
//
//
				}
			});
		}
		
	}
	
}
