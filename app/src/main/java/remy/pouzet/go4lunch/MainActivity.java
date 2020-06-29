package remy.pouzet.go4lunch;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import remy.pouzet.go4lunch.databinding.ActivityLoginBinding;

//------------------------------------------------------//
// ------------------   Functions   ------------------- //
// ------------------   Callbacks   ------------------- //
// ------------------    Adapter    ------------------- //
// ------------------     Intent    ------------------- //

public class MainActivity extends BaseActivity {
	
	//------------------------------------------------------//
	// ------------------   Variables   ------------------- //
	//------------------------------------------------------//
	
	private ActivityLoginBinding binding;
	DrawerLayout drawer;
	private AppBarConfiguration mAppBarConfiguration;
	NavigationView       navigationView;
	BottomNavigationView navView;
	
	//------------------------------------------------------//
	// ------------------   LifeCycle   ------------------- //
	//------------------------------------------------------//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityLoginBinding.inflate(getLayoutInflater());
		View view = binding.getRoot();
		// setContentView(this.getFragmentLayout());
		
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		navigationDrawerNavigationInitialize();
		bottomNavigationInitialize();
	}
	
	@Override
	public int getFragmentLayout() {
		return R.layout.activity_main;
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
		drawer               = findViewById(R.id.drawer_layout);
		navigationView       = findViewById(R.id.nav_view);
		mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_your_lunch, R.id.nav_settings, R.id.nav_logout)
				.setDrawerLayout(drawer)
				.build();
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
		NavigationUI.setupWithNavController(navigationView, navController);
	}
	
	public void bottomNavigationInitialize() {
		//Bottom navigation menu
		drawer               = findViewById(R.id.drawer_layout);
		navView              = findViewById(R.id.nav_view_bottom);
		mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_map_view, R.id.navigation_list_view, R.id.navigation_workmates)
				.setDrawerLayout(drawer)
				.build();
		NavController navControllerBottom = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupActionBarWithNavController(this, navControllerBottom, mAppBarConfiguration);
		NavigationUI.setupWithNavController(navView, navControllerBottom);
	}
	
	//TODO modify UI with user datas
	
	private void updateUIWhenCreating() {
		
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
						.into(imageViewProfile);
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
			
			//Update views with data
			this.textInputEditTextUsername.setText(username);
			this.textViewEmail.setText(email);
		}
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
