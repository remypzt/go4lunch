package remy.pouzet.go4lunch.view.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;

import remy.pouzet.go4lunch.BuildConfig;
import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.data.repositories.RestaurantsRepository;
import remy.pouzet.go4lunch.data.repositories.models.Restaurant;
import remy.pouzet.go4lunch.data.repositories.models.User;
import remy.pouzet.go4lunch.data.service.realAPI.POJOdetailsRestaurants.ResponseOfPlaceDetailsRestaurants;
import remy.pouzet.go4lunch.data.service.realAPI.UserHelper;
import remy.pouzet.go4lunch.databinding.ActivityMainBinding;
import remy.pouzet.go4lunch.databinding.AppBarMainBinding;
import remy.pouzet.go4lunch.databinding.NavHeaderMainBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
	
	public static final  String              PREF_KEY_LATITUDE  = "PREF_KEY_LATITUDE";
	public static final  String              PREF_KEY_LONGITUDE = "PREF_KEY_LONGITUDE";
	private static final int                 SIGN_OUT_TASK      = 10;
	private static final int                 DELETE_USER_TASK   = 20;
	private static final String              TAG                = MainActivity.class.getSimpleName();
	private static final int                 UPDATE_USERNAME    = 30;
	public               SharedPreferences   mPreferences;
	public               User                currentUser;
	public               FirebaseUser        currentUserBis;
	public               String              firestorePlaceID;
	private              ActivityMainBinding mActivityMainBinding;
	private              AppBarMainBinding   mAppBarMainBinding;
	private              AppBarConfiguration mAppBarConfiguration;
	private              SearchView          mSearchView;
	private              TextView            mSearchResult;
	private              StringBuilder       mResult;
	private              double              latitude, longitude;
	
	//------------------------------------------------------//
	// ------------------   LifeCycle   ------------------- //
	//------------------------------------------------------//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(mActivityMainBinding.getRoot());
		currentUserBis = null;
		
		Toolbar toolbar = mActivityMainBinding.mainToolbar.toolbar;
		mSearchView = mActivityMainBinding.mainToolbar.placesAutocompleteSearchBarContainer;
		
		getTokenFCM();
		setSupportActionBar(toolbar);
		navigationDrawerNavigationInitialize();
		bottomNavigationInitialize();
		autoCompleteSearchAPI();
		updateWithUserStatus();
		signOutButton();
		chatButon();
		yourLunchButon();
	}
	
	@Override
	public boolean onSupportNavigateUp() {
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
	}
	
	//------------------------------------------------------//
	// ------------------   Functions   ------------------- //
	//------------------------------------------------------//
	
	public void getTokenFCM() {
		FirebaseInstanceId
				.getInstance()
				.getInstanceId()
				.addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
					@Override
					public void onComplete(@NonNull Task<InstanceIdResult> task) {
						if (!task.isSuccessful()) {
							Log.w(TAG, "getInstanceId failed", task.getException());
							return;
						}
						
						// Get new Instance ID token
						String token = task
								.getResult()
								.getToken();
						
					}
				});
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
		
		navControllerBottom.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
			@Override
			public void onDestinationChanged(@NonNull NavController controller,
			                                 @NonNull NavDestination destination,
			                                 @Nullable Bundle arguments) {
				
				SearchView localSearchView = mActivityMainBinding.mainToolbar.placesAutocompleteSearchBarContainer;
				if (destination
						    .getLabel()
						    .toString()
						    .equals("Map View") || (destination
								                            .getLabel()
								                            .toString()
								                            .equals("List View"))) {
					
					localSearchView.setVisibility(View.VISIBLE);
				} else {
					localSearchView.setVisibility(View.INVISIBLE);
					
				}
			}
		});
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
		
		//PROGRAMMATICALY
		mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				searchFunction(query, bounds, placesClient);
				return searchFunction(query, bounds, placesClient);
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				searchFunction(newText, bounds, placesClient);
				return searchFunction(newText, bounds, placesClient);
			}
		});
		
	}
	
	private void updateWithUserStatus() {
		// Binding header xml element with viewbinding
		currentUserBis = this.getCurrentUser();
		if (currentUserBis != null) {
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
	
	public void chatButon() {
		mActivityMainBinding.navView
				.getMenu()
				.findItem(R.id.nav_chat)
				.setOnMenuItemClickListener(menuItem -> {
					Intent chatActivityIntent = new Intent(this, ChatActivity.class);
					startActivity(chatActivityIntent);
					return true;
				});
	}
	
	private void yourLunchButon() {
		mActivityMainBinding.navView
				.getMenu()
				.findItem(R.id.nav_your_lunch)
				.setOnMenuItemClickListener(menuItem -> {
					UserHelper
							.getUser(FirebaseAuth
									         .getInstance()
									         .getCurrentUser()
									         .getUid())
							.addOnSuccessListener(documentSnapshot -> {
								currentUser      = documentSnapshot.toObject(User.class);
								firestorePlaceID = !TextUtils.isEmpty(currentUser.getPlaceID())
								                   ? currentUser.getPlaceID()
								                   : null;
								
								if (firestorePlaceID != null) {
									intentDetailsRestaurant(firestorePlaceID);
									
								} else {
									Toast
											.makeText(this, "vous n'avez pas encore selectionné de restaurant pour ce midi", Toast.LENGTH_LONG)
											.show();
								}
								
							});
					return true;
				});
		
	}
	
	public void getLocation() {
		mPreferences = getPreferences(Context.MODE_PRIVATE);
		latitude     = getDouble(mPreferences, PREF_KEY_LATITUDE, 2.0);
		longitude    = getDouble(mPreferences, PREF_KEY_LONGITUDE, 2.0);
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
	
	public boolean searchFunction(String query,
	                              RectangularBounds bounds,
	                              PlacesClient placesClient) {
		AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
		
		// Use the builder to create a FindAutocompletePredictionsRequest.
		FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest
				.builder()
				.setLocationRestriction(bounds)
				.setTypeFilter(TypeFilter.ESTABLISHMENT)
				.setSessionToken(token)
				.setQuery(mSearchView
						          .getQuery()
						          .toString())
				.build();
		placesClient
				.findAutocompletePredictions(request)
				.addOnSuccessListener(response -> {
					mResult = new StringBuilder();
					ArrayList<Restaurant> restaurantsList = new ArrayList<>();
					for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
						if (prediction
								.getPlaceTypes()
								.contains(Place.Type.RESTAURANT)) {
							Restaurant restaurant = new Restaurant(prediction.getPlaceId(), "multimediaUrl", "Name", "Adress", "Horair", "Distance", 0, 0f, "phone number", "website",
							
							                                       latitude, longitude);
							restaurantsList.add(restaurant);
						}
					}
					RestaurantsRepository
							.getInstance()
							.getRestaurantsDetails(restaurantsList, latitude, longitude);
					
				})
				.addOnFailureListener((exception) -> {
					if (exception instanceof ApiException) {
						ApiException apiException = (ApiException) exception;
						
						Toast
								.makeText(MainActivity.this, "error", Toast.LENGTH_LONG)
								.show();
					}
				});
		
		return false;
	}
	
	@Nullable
	protected FirebaseUser getCurrentUser() {
		return FirebaseAuth
				.getInstance()
				.getCurrentUser();
	}
	
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
		
		//BUG
		// 7 - Get additional data from Firestore (Username)
		UserHelper
				.getUser(this
						         .getCurrentUser()
						         .getUid())
				.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
					@Override
					public void onSuccess(DocumentSnapshot documentSnapshot) {
						User currentUser = documentSnapshot.toObject(User.class);
						if (currentUser != null) {
							String username = TextUtils.isEmpty(currentUser.getUsername())
							                  ? getString(R.string.info_no_username_found)
							                  : currentUser.getUsername();
						} else {
							String username = "non renseigné";
						}
					}
				});
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
	
	private void intentDetailsRestaurant(String placeID) {
		RestaurantsRepository.getInstance().mRestaurantsApiInterfaceService
				.getResponseOfPlaceDetailsRestaurants(placeID, BuildConfig.apiKey)
				.enqueue(new Callback<ResponseOfPlaceDetailsRestaurants>() {
					@Override
					public void onResponse(Call<ResponseOfPlaceDetailsRestaurants> call,
					                       Response<ResponseOfPlaceDetailsRestaurants> response) {
						if (response.isSuccessful()) {
							
							Restaurant restaurant = new Restaurant(placeID, "https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photoreference=" + response
									.body()
									.getResult()
									.getPhotos()
									.get(0)
									.getPhotoReference() + "&key=" + BuildConfig.apiKey, response
									                                       .body()
									                                       .getResult()
									                                       .getName(), response
									                                       .body()
									                                       .getResult()
									                                       .getFormattedAddress(), response
									                                       .body()
									                                       .getStatus(), "distance", 0, response
									                                       .body()
									                                       .getResult()
									                                       .getRating(), response
									                                       .body()
									                                       .getResult()
									                                       .getInternationalPhoneNumber(), response
									                                       .body()
									                                       .getResult()
									                                       .getWebsite(), 0, 0);
							RestaurantDetailsActivity.startActivity(MainActivity.this, restaurant);
							
						}
					}
					
					@Override
					public void onFailure(Call<ResponseOfPlaceDetailsRestaurants> call,
					                      Throwable t) {
					}
				});
	}
	
	public double getDouble(final SharedPreferences prefs,
	                        final String key,
	                        final double defaultValue) {
		return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
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
					break;
				case DELETE_USER_TASK:
					break;
				// 8 - Hiding Progress bar after request completed
				case UPDATE_USERNAME:
					break;
				default:
					break;
			}
		};
	}
	
	// --------------------
	// ERROR HANDLER
	// --------------------
	
	@Override
	protected void onResume() {
		super.onResume();
//		updateWithUserStatus();
	}
	
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
