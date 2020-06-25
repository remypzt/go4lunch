package remy.pouzet.go4lunch.ui.mapview;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import remy.pouzet.go4lunch.GetNearbyPlaces;
import remy.pouzet.go4lunch.R;

public class MapViewFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener
		//   , OnMapReadyCallback
{
	
	// ------------------ Variables ------------------- //
	
	private MapViewViewModel mMapViewViewModel;
	FusedLocationProviderClient mFusedLocationClient;
	String                      restaurant = "restaurant";
	private GoogleMap mMap;
	private MapView   mapView;
	private int       ProximityRadius = 100;
	private double    latitude, longitude;
	
	// ------------------ LifeCycle ------------------- //
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
		
		@SuppressLint("MissingPermission")
		//TODO ask for permission
		@Override
		public void onMapReady(GoogleMap map) {
			mMap = map;
			map.setMyLocationEnabled(true);
			
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
					mMap.clear();
					DisplaysNearbyRestaurant();
				}
			}
		}
	};
	
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
	
	// ------------------ Functions ------------------- //
	
	@SuppressLint("MissingPermission")
	//TODO ask for permission
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
		
		mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
		mFusedLocationClient.removeLocationUpdates(locationCallback);
	}
	
	@Override
	public void onMyLocationClick(@NonNull Location location) {
		Toast
				.makeText(getActivity(), "Current location:\n" + location, Toast.LENGTH_LONG)
				.show();
	}
	
	@Override
	public boolean onMyLocationButtonClick() {
		Toast
				.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT)
				.show();
		// Return false so that we don't consume the event and the default behavior still occurs
		// (the camera animates to the user's current position).
		return false;
	}
	
	// ------------------ Callbacks ------------------- //
	
	public void DisplaysNearbyRestaurant() {
		Object[]        transferData    = new Object[2];
		String          url             = getUrl(latitude, longitude, restaurant);
		GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
		transferData[0] = mMap;
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
	
}

