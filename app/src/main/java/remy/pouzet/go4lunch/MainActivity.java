package remy.pouzet.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;

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

import remy.pouzet.go4lunch.databinding.ActivityMainBinding;
import remy.pouzet.go4lunch.databinding.AppBarMainBinding;
import remy.pouzet.go4lunch.databinding.NavHeaderMainBinding;
import remy.pouzet.go4lunch.ui.login.LoginActivity;

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
		
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		navigationDrawerNavigationInitialize();
		bottomNavigationInitialize();
		updateWithUserStatus();
		signOutButton();
		
	}
	
	//------------------------------------------------------//
	// ------------------   Functions   ------------------- //
	//------------------------------------------------------//
	
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
		getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
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
	
}
