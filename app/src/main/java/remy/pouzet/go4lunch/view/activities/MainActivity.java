package remy.pouzet.go4lunch.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.databinding.ActivityMainBinding;
import remy.pouzet.go4lunch.databinding.AppBarMainBinding;
import remy.pouzet.go4lunch.databinding.NavHeaderMainBinding;

//------------------------------------------------------//

// ------------------   Callbacks   ------------------- //
// ------------------    Adapter    ------------------- //
// ------------------     Intent    ------------------- //

public class MainActivity extends AppCompatActivity {
	
	//------------------------------------------------------//
	// 0------------------   Binding    ------------------- //
	//------------------------------------------------------//
	//------------------------------------------------------//
	// ------------------   Variables   ------------------- //
	//------------------------------------------------------//
	
	private static final int                 SIGN_OUT_TASK    = 10;
	private static final int                 DELETE_USER_TASK = 20;
	private              ActivityMainBinding mActivityMainBinding;
	private              AppBarMainBinding   mAppBarMainBinding;
	
	private AppBarConfiguration mAppBarConfiguration;
	
	//------------------------------------------------------//
	// ------------------   LifeCycle   ------------------- //
	//------------------------------------------------------//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(mActivityMainBinding.getRoot());

//		setSupportActionBar(mAppBarMainBinding.toolbar);
		Toolbar toolbar = mActivityMainBinding.mainToolbar.toolbar;
		
		setSupportActionBar(toolbar);
		navigationDrawerNavigationInitialize();
		bottomNavigationInitialize();
		updateWithUserStatus();
		signOutButton();
		setSearchViewVisibilityFragmentDepends();
		
	}
	
	//------------------------------------------------------//
	// ------------------   Functions   ------------------- //
	//------------------------------------------------------//
	
	private void setSearchViewVisibilityFragmentDepends() {
		SearchView localSearchView = mActivityMainBinding.mainToolbar.placesAutocompleteSearchBarContainer;
//		localSearchView.setVisibility(View.INVISIBLE);

//		if (getFragmentManager().getBackStackEntryCount() > 1) {
//			Fragment f = getFragmentManager().findFragmentById(R);
//			if (f instanceof MapViewFragment) {
//				// Do something
//			}
//		}

//		Fragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
//		Fragment fragment = navHostFragment
//				.getChildFragmentManager()
//				.getFragments().get(0);

//		NavHostFragment navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment);
//		navHostFragment.getChildFragmentManager().getFragments().get(0);

//		if (getVisibleFragment() instanceof MapViewFragment)
//		{
//			localSearchView.setVisibility(View.INVISIBLE);
//		}

//	Fragment visibleFragment=getCurrentFragment();
//	if (visibleFragment instanceof MapViewFragment){
//		localSearchView.setVisibility(View.INVISIBLE);
//	}
//
	
	}
//
//	private Fragment getVisibleFragment() {
//		FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
//		List<Fragment> fragments = fragmentManager.getFragments();
//		for (Fragment fragment : fragments) {
//			if (fragment != null && fragment.isVisible())
//				return fragment;
//		}
//		return null;
//	}

//	Fragment getCurrentFragment()
//	{
//		Fragment currentFragment = getSupportFragmentManager()
//				.findFragmentById(R.id.nav_host_fragment);
//		return currentFragment;
//	}
	
	private void updateWithUserStatus() {
		// Binding header xml element with viewbinding
		if (this.getCurrentUser() != null) {
			updateUIWhenCreating();
		} else {
			passByLoginActivity();
		}
	}
	
	//------------------------------------------------------//
	// ------------------Navigation & UI------------------- //
	//------------------------------------------------------//
	
	@Override
	public boolean onSupportNavigateUp() {
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
	}
	
	public void navigationDrawerNavigationInitialize() {
		
		//Navigation drawer menu
		mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_your_lunch, R.id.nav_settings, R.id.nav_logout)
				.setDrawerLayout(mActivityMainBinding.drawerLayout)
				.build();
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
		NavigationUI.setupWithNavController(mActivityMainBinding.navView, navController);
	}
	
	public void bottomNavigationInitialize() {
		//Bottom navigation menu
		mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_map_view, R.id.navigation_list_view, R.id.navigation_workmates)
				.setDrawerLayout(mActivityMainBinding.drawerLayout)
				.build();
		NavController navControllerBottom = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupActionBarWithNavController(this, navControllerBottom, mAppBarConfiguration);
		NavigationUI.setupWithNavController(mActivityMainBinding.navViewBottom, navControllerBottom);
	}
	
	public void signOutButton() {
		mActivityMainBinding.navView
				.getMenu()
				.findItem(R.id.nav_logout)
				.setOnMenuItemClickListener(menuItem -> {
					signOutUserFromFirebase();
					passByLoginActivity();
					return true;
				});
	}
	
	//TODO this method could be share with SettingsFragment
	public void updateUIWhenCreating() {
		NavHeaderMainBinding header = NavHeaderMainBinding.bind(mActivityMainBinding.navView.getHeaderView(0));
		//Get picture URL from Firebase
		if (this
				    .getCurrentUser()
				    .getPhotoUrl() != null) {
			Glide
					.with(this)
					.load(this
							      .getCurrentUser()
							      .getPhotoUrl())
					.apply(RequestOptions.circleCropTransform())
					.into(header.profileActivityImageviewProfile);
		}
		
		//Get email & username from Firebase
		String email = TextUtils.isEmpty(this
				                                 .getCurrentUser()
				                                 .getEmail())
		               ? getString(R.string.info_no_email_found)
		               : this
				               .getCurrentUser()
				               .getEmail();
		String username = TextUtils.isEmpty(this
				                                    .getCurrentUser()
				                                    .getDisplayName())
		                  ? getString(R.string.info_no_username_found)
		                  : this
				                  .getCurrentUser()
				                  .getDisplayName();
		
		//Update views with data's user
		header.profileActivityEditTextUsername.setText(username);
		header.profileActivityTextViewEmail.setText(email);
	}
	
	public void passByLoginActivity() {
		Intent loginActivityIntent = new Intent(this, LoginActivity.class);
		startActivity(loginActivityIntent);
	}
	
	private void signOutUserFromFirebase() {
		AuthUI
				.getInstance()
				.signOut(this)
				.addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));
	}
	
	//------------------------------------------------------//
	// ------------------      Menu     ------------------- //
	//------------------------------------------------------//
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
		return true;
	}
	
	//------------------------------------------------------//
	// 9-----------------      Data     ------------------- //
	//------------------------------------------------------//
	
	@Nullable
	protected FirebaseUser getCurrentUser() {
		return FirebaseAuth
				.getInstance()
				.getCurrentUser();
	}
	
	//TODO could be share with SettingsFragment
	// Create OnCompleteListener called after tasks ended
	private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin) {
		return aVoid -> {
			switch (origin) {
				case SIGN_OUT_TASK:
					finish();
					break;
				case DELETE_USER_TASK:
					finish();
					break;
				default:
					break;
			}
		};
	}
//
//	public void autoCompleteSearch() {
//		// Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
//		// and once again when the user makes a selection (for example when calling fetchPlace()).
//		AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
//
//		// Create a RectangularBounds object.
//		RectangularBounds bounds = RectangularBounds.newInstance(new LatLng(-33.880490, 151.184363), new LatLng(-33.858754, 151.229596));
//		// Use the builder to create a FindAutocompletePredictionsRequest.
//		FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
//		                                                                               // Call either setLocationBias() OR setLocationRestriction().
////		                                                                               .setLocationBias(bounds)
////		                                                                               //.setLocationRestriction(bounds)
////		                                                                               .setOrigin(new LatLng(-33.8749937, 151.2041382))
////		                                                                               .setCountries("AU", "NZ")
////		                                                                               .setTypeFilter(TypeFilter.ADDRESS)
////		                                                                               .setSessionToken(token)
//		                                                                               .setQuery(query)
//		                                                                               .build();
//
//		placesClient
//				.findAutocompletePredictions(request)
//				.addOnSuccessListener((response) -> {
//					for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
//						Log.i(TAG, prediction.getPlaceId());
//						Log.i(TAG, prediction
//								.getPrimaryText(null)
//								.toString());
//					}
//				})
//				.addOnFailureListener((exception) -> {
//					if (exception instanceof ApiException) {
//						ApiException apiException = (ApiException) exception;
//						Log.e(TAG, "Place not found: " + apiException.getStatusCode());
//					}
//				});
//	}
//
}
