package remy.pouzet.go4lunch.ui.mapview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import remy.pouzet.go4lunch.R;

public class MapViewFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {
	
	private MapViewViewModel mMapViewViewModel;
	private GoogleMap        mMap;
	
	private MapView mapView;
	
	double latDouble;
	double lonDouble;
	
	private OnMapReadyCallback callback = new OnMapReadyCallback() {
		
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
		public void onMapReady(GoogleMap googleMap) {
			
			latDouble = 25;
			lonDouble = 22;
			
			LatLng userLocation = new LatLng(lonDouble, latDouble);
			
			googleMap.addMarker(new MarkerOptions()
					                    .position(userLocation)
					                    .title("Marker in user location"));
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
		}
	};
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_map_view, container, false);

		
		
		
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
	
	@Override
	public void onMapReady(GoogleMap map) {
		mMap = map;
		// TODO: Before enabling the My Location layer, you must request
		// location permission from the user. This sample does not include
		// a request for location permission.
		if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		mMap.setMyLocationEnabled(true);
		mMap.setOnMyLocationButtonClickListener(this);
		mMap.setOnMyLocationClickListener(this);
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
	
	@Override
	public void onViewCreated(@NonNull View view,
	                          @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
		if (mapFragment != null) {
			mapFragment.getMapAsync(callback);
		}
	}
	
}

