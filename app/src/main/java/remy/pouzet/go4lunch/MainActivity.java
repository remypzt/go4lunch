package remy.pouzet.go4lunch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import remy.pouzet.go4lunch.databinding.ActivityLoginBinding;
import remy.pouzet.go4lunch.ui.mapview.MapViewFragment;

public class MainActivity extends AppCompatActivity {
	
	private AppBarConfiguration  mAppBarConfiguration;
	private ActivityLoginBinding binding;
	
	int                         PERMISSION_ID = 44;
	FusedLocationProviderClient mFusedLocationClient;
	
	double latDouble, lonDouble;
	// get the location update, we set the latitude and longitude values in our TextViews.
	private LocationCallback mLocationCallback = new LocationCallback() {
		@Override
		public void onLocationResult(LocationResult locationResult) {
			Location mLastLocation = locationResult.getLastLocation();
			latDouble = mLastLocation.getLatitude();
			lonDouble = mLastLocation.getLongitude();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityLoginBinding.inflate(getLayoutInflater());
		View view = binding.getRoot();
		//setContentView(this.getFragmentLayout());
		setContentView(R.layout.activity_main);
		
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
		
		getLastLocation();
		
		// example of snack bar, could be usefull
		/*
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar
						.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null)
						.show();
			}
		});
		*/
		
		Bundle bundle = new Bundle();
		
		double lat = latDouble;
		double lon = 33;
		bundle.putDouble("lat", lat);
		bundle.putDouble("lon", lon);
		MapViewFragment fragInfo = new MapViewFragment();
		fragInfo.setArguments(bundle);
		
		DrawerLayout         drawer         = findViewById(R.id.drawer_layout);
		NavigationView       navigationView = findViewById(R.id.nav_view);
		BottomNavigationView navView        = findViewById(R.id.nav_view_bottom);
		
		//Navigation drawer menu
		mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_your_lunch, R.id.nav_settings, R.id.nav_logout)
				.setDrawerLayout(drawer)
				.build();
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
		NavigationUI.setupWithNavController(navigationView, navController);
		
		//Bottom navigation menu
		mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_map_view, R.id.navigation_list_view, R.id.navigation_workmates)
				.setDrawerLayout(drawer)
				.build();
		NavController navControllerBottom = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupActionBarWithNavController(this, navControllerBottom, mAppBarConfiguration);
		NavigationUI.setupWithNavController(navView, navControllerBottom);
	}
	
	@Override
	public boolean onSupportNavigateUp() {
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
		return true;
	}
	
	// --------------------
	// Maps permissions
	// --------------------
	
	//create a method named getLastLocation() which will use to API and return the last recorder location information of the device.
	// Also this method will check first if our permission is granted or not and if the location setting is turned on.
	@SuppressLint("MissingPermission")
	private void getLastLocation() {
		if (checkPermissions()) {
			if (isLocationEnabled()) {
				mFusedLocationClient
						.getLastLocation()
						.addOnSuccessListener(MainActivity.this, location -> {
							
							if (location != null) {
								// Logic to handle location object
								latDouble = location.getLatitude();
								lonDouble = location.getLongitude();
							}
							// Got last known location. In some rare situations this can be null.
							else {
								requestNewLocationData();
							}
						});
				
				/*mFusedLocationClient
						.getLastLocation()
						.addOnCompleteListener(this, new OnCompleteListener<Location>() {
							@Override
							public void onComplete(@NonNull Task<Location> task) {
								Location location = task.getResult();
								if (location == null) {
									requestNewLocationData();
								} else {
									latDouble = location.getLatitude();
									lonDouble = location.getLongitude();
								}
							}
						});*/
			} else {
				Toast
						.makeText(this, "Turn on location", Toast.LENGTH_LONG)
						.show();
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
			}
		} else {
			requestPermissions();
		}
	}
	
	// This method will tell us whether or not the user grant us to access ACCESS_COARSE_LOCATION and ACCESS_FINE_LOCATION.
	private boolean checkPermissions() {
		return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
	}
	
	// This will check if the user has turned on location from the setting, Cause user may grant the app to user location but if the location setting is off then it'll be of no use.
	private boolean isLocationEnabled() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}
	
	// cases when the location == null, we called a new method requestNewLocationData() which will record the location information in runtime.
	@SuppressLint("MissingPermission")
	private void requestNewLocationData() {
		
		LocationRequest mLocationRequest = new LocationRequest();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(0);
		mLocationRequest.setFastestInterval(0);
		mLocationRequest.setNumUpdates(1);
		
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
		mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
		
	}
	
	// --------------------
	// Get location
	// --------------------
	
	// This method will request our necessary permissions to the user if they are not already granted.
	private void requestPermissions() {
		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (checkPermissions()) {
			getLastLocation();
		}
		
	}
	
	// This method is called when a user Allow or Deny our requested permissions. So it will help us to move forward if the permissions are granted.
	@Override
	public void onRequestPermissionsResult(int requestCode,
	                                       String[] permissions,
	                                       int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == PERMISSION_ID) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Granted. Start getting the location information
			}
		}
	}
}