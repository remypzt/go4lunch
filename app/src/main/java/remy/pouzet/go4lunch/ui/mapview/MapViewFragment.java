package remy.pouzet.go4lunch.ui.mapview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

import remy.pouzet.go4lunch.GetNearbyPlaces;
import remy.pouzet.go4lunch.R;
//------------------------------------------------------//
// ------------------    Adapter    ------------------- //
// ------------------      Menu     ------------------- //
// ------------------ Miscellaneous ------------------- //
// ------------------     Intent    ------------------- //
// ------------------Navigation & UI------------------- //
//------------------------------------------------------//

public class MapViewFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
	//--------------------------------------------------//
	// ------------------   Variables   --------------- //
	//--------------------------------------------------//
	private MapViewViewModel mMapViewViewModel;
	FusedLocationProviderClient mFusedLocationClient;
	String                      restaurant = "restaurant";
	private static final int     DEFAULT_ZOOM    = 15;
	private              MapView mapView;
	private              int     ProximityRadius = 100;
	private              double  latitude, longitude;
	private static final int       PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
	// A default location (Sydney, Australia) and default zoom to use when location permission is not granted.
	private final        LatLng    defaultLocation                          = new LatLng(-33.8523341, 151.2106085);
	private              GoogleMap Mmap;
	private              Location  lastKnownLocation;
	private              boolean   locationPermissionGranted;
	
	//--------------------------------------------------//
	// ------------------ LifeCycle ------------------- //
	//--------------------------------------------------//
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_map_view, container, false);
		
		//TODO understand if I still need this part of code
	/*	mMapViewViewModel = ViewModelProviders
				.of(this)
				.get(MapViewViewModel.class);
		View           root     = inflater.inflate(R.layout.fragment_map_view, container, false);
		final TextView textView = root.findViewById(R.id.text_map_view);

		mMapViewViewModel
				.getText()
				.observe(getViewLifecycleOwner(), new Observer<String>() {
					@Override
					public void onChanged(@Nullable String s) {
						textView.setText(s);
					}
				});
		return root;*/
		
	}
	private OnMapReadyCallback callback         = new OnMapReadyCallback() {
		
		//
		//Manipulates the map once available.
		//This callback is triggered when the map is ready to be used.
		//This is where we can add markers or lines, add listeners or move the camera.
		//In this case, we just add a marker near Sydney, Australia.
		//If Google Play services is not installed on the device, the user will be prompted to
		//install it inside the SupportMapFragment. This method will only be triggered once the
		//user has installed Google Play services and returned to the app.
		//
		
		@Override
		public void onMapReady(GoogleMap map) {
			Mmap = map;
			
			// Prompt the user for permission.
			getLocationPermission();
			
			// Turn on the My Location layer and the related control on the map.
			updateLocationUI();
			
			// Get the current location of the device and set the position of the map.
//			getDeviceLocation();
		
		}
	};
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mFusedLocationClient.removeLocationUpdates(locationCallback);
	}
	
	//------------------------------------------------------//
	// ------------------   Callbacks   ------------------- //
	//------------------------------------------------------//
	private LocationCallback locationCallback = new LocationCallback() {
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
					
					// clear all previous markers
					Mmap.clear();
					DisplaysNearbyRestaurant();
				}
			}
		}
	};
	
	@Override
	public void onViewCreated(@NonNull View view,
	                          @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
		if (mapFragment != null) {
			mapFragment.getMapAsync(callback);
		}
		
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
		LocationRequest locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setInterval(20 * 1000);
		
		if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
	}
	
	//------------------------------------------------------//
	// ------------------   Functions   ------------------- //
	//------------------------------------------------------//
	@Override
	public void onMyLocationClick(@NonNull Location location) {
		Toast
				.makeText(requireContext(), "Current location:\n" + location, Toast.LENGTH_LONG)
				.show();
	}
	
	@Override
	public boolean onMyLocationButtonClick() {
		Toast
				.makeText(requireContext(), "MyLocation button clicked", Toast.LENGTH_SHORT)
				.show();
		// Return false so that we don't consume the event and the default behavior still occurs
		// (the camera animates to the user's current position).
		return false;
	}
	
	public void DisplaysNearbyRestaurant() {
		Object[]        transferData    = new Object[2];
		String          url             = getUrl(latitude, longitude, restaurant);
		GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
		transferData[0] = Mmap;
		transferData[1] = url;
		getNearbyPlaces.execute(transferData);
		Toast
				.makeText(requireContext(), "passing by DisplaysNearbyRestaurant", Toast.LENGTH_SHORT)
				.show();
		
	}
	
	// use for DisplaysNearbyRestaurant
	private String getUrl(double latitude,
	                      double longitude,
	                      String restaurant) {
		StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
		googleURL.append("location=" + latitude + "," + longitude);
		googleURL.append("&radius=" + ProximityRadius);
		googleURL.append("&type=" + restaurant);
		googleURL.append("&sensor=true");
		googleURL.append("&key=" + "AIzaSyCwOvrDss4VieCkqr-66cV3FOVNLa20yNs");
		return googleURL.toString();
	}
	
	// [START maps_current_place_location_permission]
	private void getLocationPermission() {
		/*
		 * Request location permission, so that we can get the location of the
		 * device.
		 */
		if (ContextCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			locationPermissionGranted = true;
		} else {
			ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
		}
	}
	// [END maps_current_place_location_permission]
	
	// [START maps_current_place_update_location_ui]
	private void updateLocationUI() {
		if (Mmap == null) {
			return;
		}
		try {
			if (locationPermissionGranted) {
				Mmap.setMyLocationEnabled(true);
			} else {
				Mmap.setMyLocationEnabled(false);
				
			}
		}
		catch (SecurityException e) {
			Log.e("Exception: %s", e.getMessage());
		}
	}
	// [END maps_current_place_show_current_place]
	
	/**
	 * Gets the current location of the device, and positions the map's camera.
	 */
	// [START maps_current_place_get_device_location]
	private void getDeviceLocation() {
		/*
		 * Get the best and most recent location of the device, which may be null in rare
		 * cases when a location is not available.
		 */
		try {
			if (locationPermissionGranted) {
				Task<Location> locationResult = mFusedLocationClient.getLastLocation();
				locationResult.addOnCompleteListener((Executor) this, new OnCompleteListener<Location>() {
					@Override
					public void onComplete(@NonNull Task<Location> task) {
						if (task.isSuccessful()) {
							// Set the map's camera position to the current location of the device.
							lastKnownLocation = task.getResult();
							if (lastKnownLocation != null) {
								Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
							}
						} else {
//							Log.d(TAG, "Current location is null. Using defaults.");
//							Log.e(TAG, "Exception: %s", task.getException());
							Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
							Mmap
									.getUiSettings()
									.setMyLocationButtonEnabled(false);
						}
					}
				});
			}
		}
		catch (SecurityException e) {
			Log.e("Exception: %s", e.getMessage(), e);
		}
	}
	// [END maps_current_place_open_places_dialog]
	
}

