package remy.pouzet.go4lunch.viewmodel;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.facebook.login.LoginResult;

import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.data.loginData.LoggedInUser;
import remy.pouzet.go4lunch.data.loginData.LoginFormState;
import remy.pouzet.go4lunch.data.loginData.LoginRepository;
import remy.pouzet.go4lunch.data.loginData.Result;

public class LoginViewModel extends ViewModel {
	
	private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
	private MutableLiveData<LoginResult>    loginResult    = new MutableLiveData<>();
	private LoginRepository                 loginRepository;
	
	LoginViewModel(LoginRepository loginRepository) {
		this.loginRepository = loginRepository;
	}
	
	LiveData<LoginFormState> getLoginFormState() {
		return loginFormState;
	}
	
	LiveData<LoginResult> getLoginResult() {
		return loginResult;
	}
	
	public void login(String username,
	                  String password) {
		// can be launched in a separate asynchronous job
		Result<LoggedInUser> result = loginRepository.login(username, password);
	}
	
	public void loginDataChanged(String username,
	                             String password) {
		if (!isUserNameValid(username)) {
			loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
		} else if (!isPasswordValid(password)) {
			loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
		} else {
			loginFormState.setValue(new LoginFormState(true));
		}
	}
	
	// A placeholder username validation check
	private boolean isUserNameValid(String username) {
		if (username == null) {
			return false;
		}
		if (username.contains("@")) {
			return Patterns.EMAIL_ADDRESS
					.matcher(username)
					.matches();
		} else {
			return !username
					.trim()
					.isEmpty();
		}
	}
	
	// A placeholder password validation check
	private boolean isPasswordValid(String password) {
		return password != null && password
				                           .trim()
				                           .length() > 5;
	}
}