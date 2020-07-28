package remy.pouzet.go4lunch.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
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

import java.util.List;

import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.data.repositories.model.Restaurant;
import remy.pouzet.go4lunch.viewmodel.RestaurantsListViewViewModel;
//------------------------------------------------------//
// ------------------    Adapter    ------------------- //
// ------------------      Menu     ------------------- //
// ------------------ Miscellaneous ------------------- //
// ------------------     Intent    ------------------- //
// ------------------Navigation & UI------------------- //
//------------------------------------------------------//

public class MapViewFragment extends Fragment {
	private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
	//--------------------------------------------------//
	// ------------------   Variables   --------------- //
	//--------------------------------------------------//
//	private MapViewViewModel MmapViewViewModel;
	FusedLocationProviderClient mFusedLocationClient;
	String                      restaurant = "restaurant";
	private int    ProximityRadius = 100;
	private double latitude, longitude;
	private GoogleMap Mmap;

//	private RestaurantsListViewViewModel       mRestaurantsListViewViewModel;
	
	private boolean            locationPermissionGranted;
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
	private LocationCallback   locationCallback = new LocationCallback() {
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
					
					new RestaurantsListViewViewModel(latitude, longitude)
							.getRestaurants()
							.observe((LifecycleOwner) requireContext(), restaurants -> displayRestaurants(restaurants));
				}
			}
		}
	};
	
	public void displayRestaurants(List<Restaurant> restaurants) {
		for (Restaurant restaurant : restaurants) {
			MarkerOptions markerOptions = new MarkerOptions();
			
			String nameOfPlace = restaurant.getName();
			double lat         = restaurant.getMlat();
			double lng         = restaurant.getMlon();
			
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
			
			Mmap.addMarker(markerOptions);
			Mmap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
			Mmap.animateCamera(CameraUpdateFactory.zoomTo(10));
			Mmap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
				@Override
				public void onInfoWindowClick(Marker parameterMarker) {
					getActivity().startActivity(new Intent(getActivity(), RestaurantDetails.class));
					
					//TODO define an id for each marker will be use to define right intent
//					String actionId = markerMap.get(marker.getId());
//					if (actionId.equals("action_one")) {
//						Intent i = new Intent(MainActivity.this, ActivityOne.class);
//					markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//					Mmap.addMarker(markerOptions);
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
	
	//------------------------------------------------------//
	// ------------------   Callbacks   ------------------- //
	//------------------------------------------------------/
	
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
	/*	MmapViewViewModel = ViewModelProviders
				.of(this)
				.get(MapViewViewModel.class);
		View           root     = inflater.inflate(R.layout.fragment_map_view, container, false);
		final TextView textView = root.findViewById(R.id.text_map_view);

		MmapViewViewModel
				.getText()
				.observe(getViewLifecycleOwner(), new Observer<String>() {
					@Override
					public void onChanged(@Nullable String s) {
						textView.setText(s);
					}
				});
		return root;*/
		
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
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mFusedLocationClient.removeLocationUpdates(locationCallback);
	}
	
	//------------------------------------------------------//
	// ------------------   Functions   ------------------- //
	//------------------------------------------------------//
	
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

