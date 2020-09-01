package remy.pouzet.go4lunch.viewmodel;

import androidx.lifecycle.ViewModel;

import remy.pouzet.go4lunch.data.loginData.LoginRepository;

public class LoginViewModel extends ViewModel {
	
	private LoginRepository                 loginRepository;
	
	LoginViewModel(LoginRepository loginRepository) {
		this.loginRepository = loginRepository;
	}
	
}