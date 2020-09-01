package remy.pouzet.go4lunch.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.data.repositories.models.Restaurant;
import remy.pouzet.go4lunch.data.service.realAPI.UserHelper;
import remy.pouzet.go4lunch.view.activities.RestaurantDetailsActivity;
import remy.pouzet.go4lunch.viewmodel.RestaurantsListViewViewModel;
//------------------------------------------------------//
// ------------------    Adapter    ------------------- //
// ------------------      Menu     ------------------- //
// ------------------ Miscellaneous ------------------- //
// ------------------     Intent    ------------------- //
// ------------------Navigation & UI------------------- //
//------------------------------------------------------//

public class MapViewFragment extends Fragment {
	
	public static final  String            PREFS                                    = "PREFS";
	public static final  String            PREF_KEY_LONGITUDE                       = "PREF_KEY_LONGITUDE";
	public static final  String            PREF_KEY_LATITUDE                        = "PREF_KEY_LATITUDE";
	private static final int               PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
	public               SharedPreferences mPreferences;
	String                      restaurant = "restaurant";
	//--------------------------------------------------//
	// ------------------   Variables   --------------- //
	//--------------------------------------------------//
	FusedLocationProviderClient mFusedLocationClient;
	private int    ProximityRadius = 100;
	private double latitude, longitude;
	private GoogleMap             mMap;
	private ArrayList<Restaurant> mRestaurants     = new ArrayList<>();
	public  LocationCallback      locationCallback = new LocationCallback() {
		@SuppressLint("CommitPrefEdits")
		@Override
		public void onLocationResult(LocationResult locationResult) {
			if (locationResult == null) {
				return;
			}
			for (Location location : locationResult.getLocations()) {
				if (location != null) {
					Log.e("MapViewFragment", "latitude: " + location.getLatitude() + " - longitude: " + location.getLongitude());
					latitude  = location.getLatitude();
					longitude = location.getLongitude();
					
					saveLocation();
					
					mMap.clear();
					
					new RestaurantsListViewViewModel(latitude, longitude)
							.getRestaurants()
							.observe((LifecycleOwner) requireContext(), restaurants -> {
								mRestaurants.clear();
								mRestaurants.addAll(restaurants);
								displayRestaurants(restaurants);
							});
				}
			}
		}
	};
	private boolean               locationPermissionGranted;
	//------------------------------------------------------//
	// ------------------   Callbacks   ------------------- //
	//------------------------------------------------------/
	private OnMapReadyCallback    callback         = new OnMapReadyCallback() {
		
		@Override
		public void onMapReady(GoogleMap map) {
			mMap = map;
			// Turn on the My Location layer and the related control on the map.
			updateLocationUI();
		}
	};
	
	public void saveLocation() {
		mPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
		putDouble(mPreferences.edit(), PREF_KEY_LATITUDE, latitude).apply();
		putDouble(mPreferences.edit(), PREF_KEY_LONGITUDE, longitude).apply();
	}
	
	public SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit,
	                                          final String key,
	                                          final double value) {
		return edit.putLong(key, Double.doubleToRawLongBits(value));
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode,
	                                       String[] permissions,
	                                       int[] grantResults) {
		if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				
				// permission was granted, proceed to the normal flow.
				locationPermissionGranted = true;
				updateLocationUI();
				getLocationAndCheckPermission();
			}
		}
	}
	
	//--------------------------------------------------//
	// ------------------ LifeCycle ------------------- //
	//--------------------------------------------------//
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_map_view, container, false);
	}
	
	//------------------------------------------------------//
	// ------------------   Functions   ------------------- //
	//------------------------------------------------------//
	
	@SuppressLint("MissingPermission")
	@Override
	public void onViewCreated(@NonNull View view,
	                          @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
		if (mapFragment != null) {
			mapFragment.getMapAsync(callback);
		}
		
		requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
		
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
		getLocationAndCheckPermission();
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (mMap != null) {
			displayRestaurants(mRestaurants);
		}
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mFusedLocationClient.removeLocationUpdates(locationCallback);
	}
	
	public void displayRestaurants(List<Restaurant> restaurants) {
		mMap.clear();
		for (Restaurant restaurant : restaurants) {
			getInterrestedWorkmatesFromFirebase(restaurant);
		}
	}
	
	public void getInterrestedWorkmatesFromFirebase(Restaurant restaurant) {
		
		UserHelper
				.getInterestedUsers("user", restaurant.getMplaceID())
				.get()
				.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
					@Override
					public void onSuccess(QuerySnapshot parameterQueryDocumentSnapshots) {
						manageMarker(restaurant, parameterQueryDocumentSnapshots.size());
					}
				});
	}
	
	public void manageMarker(Restaurant restaurant,
	                         int nbWorkmates) {
		MarkerOptions markerOptions = new MarkerOptions();
		float         markerColor;
		String        nameOfPlace   = restaurant.getName();
		double        lat           = restaurant.getMlat();
		double        lng           = restaurant.getMlon();
		LatLng        latLng        = new LatLng(lat, lng);
		
		if (nbWorkmates > 0) {
			markerColor = BitmapDescriptorFactory.HUE_AZURE;
		} else {
			markerColor = BitmapDescriptorFactory.HUE_RED;
		}
		
		markerOptions
				.position(latLng)
				.title(nameOfPlace)
				.icon(BitmapDescriptorFactory.defaultMarker(markerColor));
		
		Marker marker = mMap.addMarker(markerOptions);
		marker.setTag(restaurant);
		mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
		mMap.setOnInfoWindowClickListener(parameterMarker -> {
			Restaurant localRestaurant = (Restaurant) parameterMarker.getTag();
			RestaurantDetailsActivity.startActivity(getActivity(), localRestaurant);
		});
	}
	
	private void updateLocationUI() {
		if (mMap == null) {
			return;
		}
		try {
			if (locationPermissionGranted) {
				mMap.setMyLocationEnabled(true);
			} else {
				mMap.setMyLocationEnabled(false);
			}
		}
		catch (SecurityException e) {
			Log.e("Exception: %s", e.getMessage());
		}
	}
	
	@SuppressLint("MissingPermission")
	public void getLocationAndCheckPermission() {
		
		LocationRequest locationRequest = LocationRequest
				.create()
				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
				.setSmallestDisplacement(50)
				.setInterval(20 * 1000);
		getLocationPermission();
		mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
	}
	
	private void getLocationPermission() {
		
		if (ContextCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			locationPermissionGranted = true;
			
		} else {
			ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
		}
	}
}

