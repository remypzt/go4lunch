package remy.pouzet.go4lunch;

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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import remy.pouzet.go4lunch.databinding.ActivityMainBinding;
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
	private              ActivityMainBinding binding;
	private              AppBarConfiguration mAppBarConfiguration;
	
	//------------------------------------------------------//
	// ------------------   LifeCycle   ------------------- //
	//------------------------------------------------------//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		navigationDrawerNavigationInitialize();
		bottomNavigationInitialize();
		signOut();
		
		this.updateUIWhenCreating();
	}
	
	//------------------------------------------------------//
	// ------------------   Functions   ------------------- //
	//------------------------------------------------------//
	
	public void navigationDrawerNavigationInitialize() {
		
		//Navigation drawer menu
		mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_your_lunch, R.id.nav_settings, R.id.nav_logout)
				.setDrawerLayout(binding.drawerLayout)
				.build();
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
		NavigationUI.setupWithNavController(binding.navView, navController);
	}
	
	//------------------------------------------------------//
	// ------------------Navigation & UI------------------- //
	//------------------------------------------------------//
	
	@Override
	public boolean onSupportNavigateUp() {
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
	}
	
	public void bottomNavigationInitialize() {
		//Bottom navigation menu
		mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_map_view, R.id.navigation_list_view, R.id.navigation_workmates)
				.setDrawerLayout(binding.drawerLayout)
				.build();
		NavController navControllerBottom = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupActionBarWithNavController(this, navControllerBottom, mAppBarConfiguration);
		NavigationUI.setupWithNavController(binding.navView, navControllerBottom);
	}
	
	public void signOut() {
		//TODO how to use View Binding here ?
		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView
				.getMenu()
				.findItem(R.id.nav_logout)
				.setOnMenuItemClickListener(menuItem -> {
					signOutUserFromFirebase();
					return true;
				});
	}
	
	private void updateUIWhenCreating() {
		// Binding header xml element with viewbinding
		NavHeaderMainBinding header = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0));
		
		if (this.getCurrentUser() != null) {
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
	
	// Create OnCompleteListener called after tasks ended
	private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin) {
		return new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
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
			}
		};
	}
	
	//------------------------------------------------------//
	// ------------------   Callbacks   ------------------- //
	//------------------------------------------------------//
	
	public void onClickSignOutButton() {
		
		this.signOutUserFromFirebase();
	}
	
	//------------------------------------------------------//
	// ------------------ Miscellaneous ------------------- //
	//------------------------------------------------------//
	
	// example of snack bar, could be usefull
//
//		fab.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				Snackbar
//						.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//						.setAction("Action", null)
//						.show();
//			}
//		});
//
}
