package remy.pouzet.go4lunch.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.others.ATRIER.GetNearbyPlaces;
//------------------------------------------------------//
// ------------------    Adapter    ------------------- //
// ------------------      Menu     ------------------- //
// ------------------ Miscellaneous ------------------- //
// ------------------     Intent    ------------------- //
// ------------------Navigation & UI------------------- //
//------------------------------------------------------//

public class MapViewFragment extends Fragment
{
	//--------------------------------------------------//
	// ------------------   Variables   --------------- //
	//--------------------------------------------------//
//	private MapViewViewModel mMapViewViewModel;
	FusedLocationProviderClient mFusedLocationClient;
	String                      restaurant = "restaurant";
	
	private int    ProximityRadius = 100;
	private double latitude, longitude;
	private static final int       PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
	private              GoogleMap Mmap;
	
	private boolean locationPermissionGranted;
	
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
		
		
		@Override
		public void onMapReady(GoogleMap map) {
			Mmap = map;
			
			// Prompt the user for permission.
//			getLocationPermission();
			
			// Turn on the My Location layer and the related control on the map.
			updateLocationUI();
			
		}
	};
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mFusedLocationClient.removeLocationUpdates(locationCallback);
	}
	
	//------------------------------------------------------//
	// ------------------   Callbacks   ------------------- //
	//------------------------------------------------------/
	
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
	public void onRequestPermissionsResult(int requestCode,
	                                       String[] permissions,
	                                       int[] grantResults) {
		switch (requestCode) {
			case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					
					// permission was granted, proceed to the normal flow.
					locationPermissionGranted = true;
					updateLocationUI();
					getLocationAndCheckPermission();
				}
		}
	}
	
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

	
	//------------------------------------------------------//
	// ------------------   Functions   ------------------- //
	//------------------------------------------------------//
	
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
	
	public void DisplaysNearbyRestaurant() {
		Object[]        transferData    = new Object[2];
		String          url             = getUrl(latitude, longitude, restaurant);
		GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces(getActivity());
		transferData[0] = Mmap;
		transferData[1] = url;
		
		getNearbyPlaces.execute(transferData);
		
	}
	
	private String getUrl(double latitude,
	                      double longitude,
	                      String restaurant) {
		StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
		googleURL.append("location=" + latitude + "," + longitude);
		
		googleURL.append("&radius=" + ProximityRadius);
		googleURL.append("&type=" + restaurant);
		googleURL.append("&sensor=true");
		googleURL.append("&key=" + "AIzaSyAyT25ijQ8hyslxHA7HZumtLD4emIudaLI");
		//An another key which could be usefull AIzaSyDL-idL-XMKynowoKVqMhtvy--51D_sz_U
		
		return googleURL.toString();
	}
	

	private void getLocationPermission() {
		
		if (ContextCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			locationPermissionGranted = true;
			
		} else {
			ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
		}
	}
	
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
	
}

