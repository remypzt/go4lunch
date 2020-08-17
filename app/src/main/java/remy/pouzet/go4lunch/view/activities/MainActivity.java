package remy.pouzet.go4lunch.view.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import remy.pouzet.go4lunch.BuildConfig;
import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.data.repositories.models.User;
import remy.pouzet.go4lunch.data.service.realAPI.UserHelper;
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
	
	public static final  String PREF_KEY_LATITUDE  = "PREF_KEY_LATITUDE";
	public static final  String PREF_KEY_LONGITUDE = "PREF_KEY_LONGITUDE";
	private static final String TAG                = MainActivity.class.getSimpleName();
	
	private AppBarConfiguration mAppBarConfiguration;
	public  SharedPreferences   mPreferences;
	private EditText            queryText;
	private Button              mSearchButton;
	private TextView            mSearchResult;
	private StringBuilder       mResult;
	
	// Creating identifier to identify REST REQUEST (Update username)
	private static final int    UPDATE_USERNAME = 30;
	private              double latitude, longitude;
	
	//TODO MAJ username
//	@OnClick(R.id.profile_activity_button_update)
//	public void onClickUpdateButton() { this.updateUsernameInFirebase();
	
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
		queryText     = mActivityMainBinding.mainToolbar.inputEditText;
		mSearchButton = mActivityMainBinding.mainToolbar.searchButton;
		
		setSupportActionBar(toolbar);
		navigationDrawerNavigationInitialize();
		bottomNavigationInitialize();
		updateWithUserStatus();
		signOutButton();
		chatButton();
		setSearchViewVisibilityFragmentDepends();
		
	}
	
	//------------------------------------------------------//
	// ------------------   Functions   ------------------- //
	//------------------------------------------------------//
	
	@Override
	public boolean onSupportNavigateUp() {
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
	}
	
	public void navigationDrawerNavigationInitialize() {
		
		//Navigation drawer menu
		mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_your_lunch, R.id.nav_settings, R.id.nav_logout, R.id.nav_chat)
				.setDrawerLayout(mActivityMainBinding.drawerLayout)
				.build();
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
		NavigationUI.setupWithNavController(mActivityMainBinding.navView, navController);
	}
	
	//------------------------------------------------------//
	// ------------------Navigation & UI------------------- //
	//------------------------------------------------------//
	
	public void bottomNavigationInitialize() {
		//Bottom navigation menu
		mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_map_view, R.id.navigation_list_view, R.id.navigation_workmates)
				.setDrawerLayout(mActivityMainBinding.drawerLayout)
				.build();
		NavController navControllerBottom = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupActionBarWithNavController(this, navControllerBottom, mAppBarConfiguration);
		NavigationUI.setupWithNavController(mActivityMainBinding.navViewBottom, navControllerBottom);
		
		autoCompleteSearchAPI();
		
		navControllerBottom.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
			@Override
			public void onDestinationChanged(@NonNull NavController controller,
			                                 @NonNull NavDestination destination,
			                                 @Nullable Bundle arguments) {

//				android.widget.fragment localSearchView = mActivityMainBinding.mainToolbar.autocompleteFragment;
//				SearchView localSearchView = mActivityMainBinding.mainToolbar.placesAutocompleteSearchBarContainer;
				
				if (destination
						    .getLabel()
						    .toString()
						    .equals("Map View") || (destination
								                            .getLabel()
								                            .toString()
								                            .equals("List View"))) {

//					localSearchView.setVisibility(View.VISIBLE);
				} else {
//					localSearchView.setVisibility(View.INVISIBLE);
				
				}
			}
		});
	}
	
	private void setSearchViewVisibilityFragmentDepends() {
//		SearchView localSearchView = mActivityMainBinding.mainToolbar.placesAutocompleteSearchBarContainer;
//		localSearchView.setVisibility(View.INVISIBLE);
	
	}
	
	public void autoCompleteSearchAPI() {
		getLocation();
		String apiKey = BuildConfig.apiKey;
		
		if (!Places.isInitialized()) {
			Places.initialize(getApplicationContext(), apiKey);
		}

// Create a new Places client instance.
		PlacesClient placesClient = Places.createClient(this);
		
		RectangularBounds bounds = RectangularBounds.newInstance(getCoordinate(latitude, longitude, -10000, -10000), getCoordinate(latitude, longitude, 10000, 10000));
		
		//WIDGET
//		// Initialize the AutocompleteSupportFragment.
//		AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//				getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//
//		// Specify the types of place data to return.
//		assert autocompleteFragment != null;
//		autocompleteFragment
//				.setLocationRestriction(bounds)
////				.setTypeFilter(TypeFilter.ESTABLISHMENT)
//
//
//				.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
//
//		// Set up a PlaceSelectionListener to handle the response.
//		autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//			@Override
//			public void onPlaceSelected(@NonNull Place place) {
//				// TODO: Get info about the selected place.
//
//				Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
//			}
//
//
//			@Override
//			public void onError(@NonNull Status status) {
//				// TODO: Handle the error.
//				Log.i("TAG", "An error occurred: " + status);
//			}
//		});
		
		//PROGRAMMATICALY
		mSearchButton.setOnClickListener(v -> {
			Toast
					.makeText(MainActivity.this, queryText
							.getText()
							.toString(), Toast.LENGTH_SHORT)
					.show();
			// Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
			// and once again when the user makes a selection (for example when calling fetchPlace()).
			AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
			
			// Use the builder to create a FindAutocompletePredictionsRequest.
			FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
//			                                                                               .setLocationBias(bounds)
                                                                                           .setLocationRestriction(bounds)
//			                                                                               .setOrigin(new LatLng(latitude, longitude)
                                                                                           .setTypeFilter(TypeFilter.ESTABLISHMENT)
                                                                                           .setSessionToken(token)
                                                                                           .setQuery(queryText
		                                                                                                     .getText()
		                                                                                                     .toString())
                                                                                           .build();
			
			placesClient
					.findAutocompletePredictions(request)
					.addOnSuccessListener(response -> {
						mResult = new StringBuilder();
						for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
							mResult
									.append(" ")
									.append(prediction.getFullText(null))
									.append("\n");
//
							Toast
									.makeText(MainActivity.this, prediction.getPrimaryText(null) + "-" + prediction.getSecondaryText(null), Toast.LENGTH_LONG)
									.show();
						}
						//TODO maj view
						Toast
								.makeText(this, String.valueOf(mResult), Toast.LENGTH_LONG)
								.show();
						
					})
					.addOnFailureListener((exception) -> {
						if (exception instanceof ApiException) {
							ApiException apiException = (ApiException) exception;
//					Log.e(TAG, "Place not found: " + apiException.getStatusCode());
							Toast
									.makeText(this, "error", Toast.LENGTH_LONG)
									.show();
						}
					});
		});
		
	}
	
	public void getLocation() {
		mPreferences = getPreferences(Context.MODE_PRIVATE);
		latitude     = getDouble(mPreferences, PREF_KEY_LATITUDE, 2.0);
		longitude    = getDouble(mPreferences, PREF_KEY_LONGITUDE, 2.0);
	}
	
	private void updateWithUserStatus() {
		// Binding header xml element with viewbinding
		if (this.getCurrentUser() != null) {
			updateUIWhenCreating();
		} else {
			passByLoginActivity();
		}
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
	
	public void chatButton() {
		mActivityMainBinding.navView
				.getMenu()
				.findItem(R.id.nav_chat)
				.setOnMenuItemClickListener(menuItem -> {
					Intent chatActivityIntent = new Intent(this, ChatActivity.class);
					startActivity(chatActivityIntent);
					return true;
				});
	}
	
	@Nullable
	protected FirebaseUser getCurrentUser() {
		return FirebaseAuth
				.getInstance()
				.getCurrentUser();
	}
	
	public static LatLng getCoordinate(double lat0,
	                                   double lng0,
	                                   long dy,
	                                   long dx) {
		double earthCircumference = 6378137;
		double lat                = lat0 + (180 / Math.PI) * (dy / earthCircumference);
		double lng                = lng0 + (180 / Math.PI) * (dx / earthCircumference) / Math.cos(lat0);
		return new LatLng(lat, lng);
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
		
		// 7 - Get additional data from Firestore (Username)
		UserHelper
				.getUser(this
						         .getCurrentUser()
						         .getUid())
				.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
					@Override
					public void onSuccess(DocumentSnapshot documentSnapshot) {
						User currentUser = documentSnapshot.toObject(User.class);
						String username = TextUtils.isEmpty(currentUser.getUsername())
						                  ? getString(R.string.info_no_username_found)
						                  : currentUser.getUsername();
//				textInputEditTextUsername.setText(username);
					}
				});
	}
	
	public double getDouble(final SharedPreferences prefs,
	                        final String key,
	                        final double defaultValue) {
		return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
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
	// 9-----------------      Data     ------------------- //
	//------------------------------------------------------//
	
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
				// 8 - Hiding Progress bar after request completed
				case UPDATE_USERNAME:
//					progressBar.setVisibility(View.INVISIBLE);
					break;
				default:
					break;
			}
		};
	}
	
	// --------------------
	// ERROR HANDLER
	// --------------------
	
	protected OnFailureListener onFailureListener() {
		return new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Toast
						.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG)
						.show();
			}
		};
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
	
}
