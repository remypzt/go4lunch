package remy.pouzet.go4lunch.ui.login;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;

import remy.pouzet.go4lunch.BaseActivity;
import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity {
	
	private ActivityLoginBinding binding;
	
	@Override
	public int getFragmentLayout() {
		return R.layout.activity_login;
	}
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityLoginBinding.inflate(getLayoutInflater());
		View view = binding.getRoot();
		setContentView(view);
		
		//binding.main_activity_login_by_email.setText(viewModel.main_activity_login_by_email);
		binding.main_activity_login_by_email.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View parameterView) {
			
			}
			//viewModel.userClicked()
		});
	}
	
	//FOR DATA
	// 1 - Identifier for Sign-In Activity
	private static final int RC_SIGN_IN = 123;
	
	// --------------------
	// ACTIONS
	// --------------------
	
	//@OnClick(R.id.main_activity_button_login)
	public void onClickLoginButton() {
		// 3 - Launch Sign-In Activity when user clicked on Login Button
		this.startSignInActivity();
	}
	
	// --------------------
	// NAVIGATION
	// --------------------
	
	// 2 - Launch Sign-In Activity
	private void startSignInActivity() {
		startActivityForResult(AuthUI
				                       .getInstance()
				                       .createSignInIntentBuilder()
				                       .setTheme(R.style.LoginTheme)
				                       .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()))
				                       .setIsSmartLockEnabled(false, true)
				                       .setLogo(R.drawable.ic_logo_auth)
				                       .build(), RC_SIGN_IN);
	}
	
}