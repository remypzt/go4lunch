package remy.pouzet.go4lunch.ui.mapview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import remy.pouzet.go4lunch.R;

public class MapViewFragment extends Fragment {
	
	private MapViewViewModel mMapViewViewModel;
	
	/* int                     PERMISSION_ID = 44;
	FusedLocationProviderClient mFusedLocationClient;
	
	double latDouble, lonDouble;
	*/
	
	double latDouble;
	double lonDouble;
	
	private OnMapReadyCallback callback = new OnMapReadyCallback() {
		
		/**
		 * Manipulates the map once available.
		 * This callback is triggered when the map is ready to be used.
		 * This is where we can add markers or lines, add listeners or move the camera.
		 * In this case, we just add a marker near Sydney, Australia.
		 * If Google Play services is not installed on the device, the user will be prompted to
		 * install it inside the SupportMapFragment. This method will only be triggered once the
		 * user has installed Google Play services and returned to the app.
		 */
		@Override
		public void onMapReady(GoogleMap googleMap) {
			
			LatLng sydney = new LatLng(latDouble, 154
			                           //latDouble, lonDouble
			);
			
			googleMap.addMarker(new MarkerOptions()
					                    .position(sydney)
					                    .title("Marker in user location"));
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
		}
	};
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		//return inflater.inflate(R.layout.fragment_maps, container, false);

//		latDouble =  this.getArguments().getDouble("lat");
//		lonDouble = this.getArguments().getDouble("lon");
		
		mMapViewViewModel = ViewModelProviders
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
		return root;
		
		
		/* mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
		
		getLastLocation();*/
	}
	
	@Override
	public void onViewCreated(@NonNull View view,
	                          @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
		if (mapFragment != null) {
			mapFragment.getMapAsync(callback);
		}
	}
	
	// --------------------
	// Maps permissions
	// --------------------
	
	// This method will tell us whether or not the user grant us to access ACCESS_COARSE_LOCATION and ACCESS_FINE_LOCATION.
/*	private boolean checkPermissions() {
		if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
		    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			return true;
		}
		return false;
	}
	
	// This method will request our necessary permissions to the user if they are not already granted.
	private void requestPermissions() {
		ActivityCompat.requestPermissions(
				getActivity(),
				new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
				PERMISSION_ID
		                                 );
	}
	*/
	// This will check if the user has turned on location from the setting, Cause user may grant the app to user location but if the location setting is off then it'll be of no use.
	/*private boolean isLocationEnabled() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
				LocationManager.NETWORK_PROVIDER
		                                                                                                           );
	}*/
	
	// This method is called when a user Allow or Deny our requested permissions. So it will help us to move forward if the permissions are granted.
	/*@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == PERMISSION_ID) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Granted. Start getting the location information
			}
		}
	}*/
	
	// --------------------
	// Get location
	// --------------------
	
	//create a method named getLastLocation() which will use to API and return the last recorder location information of the device.
	// Also this method will check first if our permission is granted or not and if the location setting is turned on.
	/* @SuppressLint("MissingPermission")
	private void getLastLocation(){
		if (checkPermissions()) {
			if (2 == 2/*isLocationEnabled()*/
	/* )
	 {
				mFusedLocationClient.getLastLocation().addOnCompleteListener(
						new OnCompleteListener<Location>() {
							@Override
							public void onComplete(@NonNull Task<Location> task) {
								Location location = task.getResult();
								if (location == null) {
									requestNewLocationData();
								} else {
									
									latDouble   = location.getLatitude();
									lonDouble = location.getLongitude();
								}
							}
						}
				                                                            );
			} else {
				Toast
						.makeText(getActivity(), "Turn on location", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
			}
		} else {
			requestPermissions();
		}
	}
	*/
	
	// cases when the location == null, we called a new method requestNewLocationData() which will record the location information in runtime.
/* 	@SuppressLint("MissingPermission")
	private void requestNewLocationData(){
		
		LocationRequest mLocationRequest = new LocationRequest();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(0);
		mLocationRequest.setFastestInterval(0);
		mLocationRequest.setNumUpdates(1);
		
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
		mFusedLocationClient.requestLocationUpdates(
				mLocationRequest, mLocationCallback,
				Looper.myLooper()
		                                           );
		
	}
	*/
	// get the location update, we set the latitude and longitude values in our TextViews.
	/*private LocationCallback mLocationCallback = new LocationCallback() {
		@Override
		public void onLocationResult(LocationResult locationResult) {
			Location mLastLocation = locationResult.getLastLocation();
			latDouble = mLastLocation.getLatitude();
			lonDouble = mLastLocation.getLongitude();
		}
	};
	
	@Override
	public void onResume() {
		super.onResume();
		if (checkPermissions()) {
			getLastLocation();
		}
		
	}*/
}
