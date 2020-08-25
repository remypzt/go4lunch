package remy.pouzet.go4lunch.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.data.service.realAPI.UserHelper;
import remy.pouzet.go4lunch.databinding.ActivityLoginBinding;

//------------------------------------------------------//
// ------------------   Functions   ------------------- //
// ------------------   Callbacks   ------------------- //
// ------------------    Adapter    ------------------- //
// ------------------      Menu     ------------------- //
// ------------------ Miscellaneous ------------------- //
// ------------------     Intent    ------------------- //
// ------------------Navigation & UI------------------- //

public class LoginActivity extends AppCompatActivity
//		extends BaseActivity
{

//------------------------------------------------------//
// ------------------   Variables   ------------------- //
// ------------------------------------------------------//
	
	//FOR DATA
	private static final int RC_SIGN_IN = 123;
	
	private ActivityLoginBinding mActivityLoginBinding;

//------------------------------------------------------//
// ------------------   LifeCycle   ------------------- //
//------------------------------------------------------//
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
		View view = mActivityLoginBinding.getRoot();
		setContentView(view);
		
		CoordinatorLayout coordinatorlayout = mActivityLoginBinding.coordinatorLayout;
		
		loginByEmail();
		loginByGoogle();
		loginByFacebook();
		
	}
	
	//------------------------------------------------------//
	// ------------------   Functions   ------------------- //
	//------------------------------------------------------//
	
	public void loginByEmail() {
		Button mButton = mActivityLoginBinding.mainActivityLoginByEmailButton;
		
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startEmailSignInActivity();
			}
		});
	}
	
	public void loginByGoogle() {
		Button mButton = mActivityLoginBinding.mainActivityLoginByGoogleButton;
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startGoogleSignInActivity();
			}
		});
	}
	
	public void loginByFacebook() {
		Button mButton = mActivityLoginBinding.mainActivityLoginByFbButton;
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startFacebookSignInActivity();
			}
		});
	}

//------------------------------------------------------//
// ------------------     Intent    ------------------- //
//------------------------------------------------------//
	
	//  Launch Sign-In Activity
	private void startEmailSignInActivity() {
		startActivityForResult(AuthUI
				                       .getInstance()
				                       .createSignInIntentBuilder()
				                       .setTheme(R.style.LoginTheme)
				                       .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build()))
				                       .setIsSmartLockEnabled(false, true)
				                       .setLogo(R.drawable.ic_logo_auth)
				                       .build(), RC_SIGN_IN);
	}
	
	private void startGoogleSignInActivity() {
		startActivityForResult(AuthUI
				                       .getInstance()
				                       .createSignInIntentBuilder()
				                       .setTheme(R.style.LoginTheme)
				                       .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build()))
				                       .setIsSmartLockEnabled(false, true)
				                       .setLogo(R.drawable.ic_logo_auth)
				                       .build(), RC_SIGN_IN);
	}
	
	private void startFacebookSignInActivity() {
		startActivityForResult(AuthUI
				                       .getInstance()
				                       .createSignInIntentBuilder()
				                       .setTheme(R.style.LoginTheme)
				                       .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.FacebookBuilder().build()))
				                       .setIsSmartLockEnabled(false, true)
				                       .setLogo(R.drawable.ic_logo_auth)
				                       .build(), RC_SIGN_IN);
		
	}
	
	private void startMainActivity() {
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode,
	                                int resultCode,
	                                Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//  Handle SignIn Activity response on activity result
		this.handleResponseAfterSignIn(requestCode, resultCode, data);
	}
	
	//  Method that handles response after SignIn Activity close
	private void handleResponseAfterSignIn(int requestCode,
	                                       int resultCode,
	                                       Intent data) {
		CoordinatorLayout coordinatorlayout = mActivityLoginBinding.coordinatorLayout;
		IdpResponse       response          = IdpResponse.fromResultIntent(data);
		
		if (requestCode == RC_SIGN_IN) {
			if (resultCode == RESULT_OK) { // SUCCESS
				// 2 - CREATE USER IN FIRESTORE
				this.createUserInFirestore();
				showSnackBar(coordinatorlayout, getString(R.string.connection_succeed));
				// if authentification is success finish this activity because it's not usefull anymore and if I need by an intent it'll make a "millefeuille"
				finish();
				
			} else { // ERRORS
				if (response == null) {
					showSnackBar(coordinatorlayout, getString(R.string.error_authentication_canceled));
				} else if (response
						           .getError()
						           .getErrorCode() == ErrorCodes.NO_NETWORK) {
					showSnackBar(coordinatorlayout, getString(R.string.error_no_internet));
				} else if (response
						           .getError()
						           .getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
					showSnackBar(coordinatorlayout, getString(R.string.error_unknown_error));
				}
			}
		}
	}

//------------------------------------------------------//
// ------------------Navigation & UI------------------- //
//------------------------------------------------------//
	
	// 1 - Http request that create user in firestore
	private void createUserInFirestore() {
		
		if (this.getCurrentUser() != null) {
			
			String urlPicture = (this
					                     .getCurrentUser()
					                     .getPhotoUrl() != null)
			                    ? this
					                    .getCurrentUser()
					                    .getPhotoUrl()
					                    .toString()
			                    : null;
			String username = this
					.getCurrentUser()
					.getDisplayName();
			String uid = this
					.getCurrentUser()
					.getUid();
			String       placeID          = null;
			String       restaurantName   = null;
			String       adressRestaurant = null;
			List<String> likedRestaurants = null;
			
			UserHelper
					.createUser(uid, username, urlPicture, placeID, restaurantName, adressRestaurant, likedRestaurants)
					.addOnFailureListener(this.onFailureListener());
		}
	}
	
	// 2 - Show Snack Bar with a message
	private void showSnackBar(CoordinatorLayout coordinatorLayout,
	                          String message) {
		Snackbar
				.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT)
				.show();
	}
	
	//------------------------------------------------------//
	// ------------------ Miscellaneous ------------------- //
	//------------------------------------------------------//
	
	@Nullable
	protected FirebaseUser getCurrentUser() {
		return FirebaseAuth
				.getInstance()
				.getCurrentUser();
	}
	
	// --------------------
	// UTILS
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
}
	
