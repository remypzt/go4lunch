package remy.pouzet.go4lunch.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

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
	
	private AppBarConfiguration mAppBarConfiguration;
	
	// Creating identifier to identify REST REQUEST (Update username)
	private static final int UPDATE_USERNAME = 30;
	
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
	
	private void setSearchViewVisibilityFragmentDepends() {
		SearchView localSearchView = mActivityMainBinding.mainToolbar.placesAutocompleteSearchBarContainer;
//		localSearchView.setVisibility(View.INVISIBLE);
	
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
		
		// 7 - Get additional data from Firestore (isMentor & Username)
		UserHelper
				.getUser(this
						         .getCurrentUser()
						         .getUid())
				.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
					@Override
					public void onSuccess(DocumentSnapshot documentSnapshot) {
						User   currentUser = documentSnapshot.toObject(User.class);
						String username    = TextUtils.isEmpty(currentUser.getUsername())
						                     ? getString(R.string.info_no_username_found)
						                     : currentUser.getUsername();
//				textInputEditTextUsername.setText(username);
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
