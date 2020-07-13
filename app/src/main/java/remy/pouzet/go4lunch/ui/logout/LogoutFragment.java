package remy.pouzet.go4lunch.ui.logout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.databinding.FragmentLogoutBinding;

// ------------------   Functions   ------------------- //
// ------------------   Callbacks   ------------------- //
// ------------------    Adapter    ------------------- //
// ------------------      Menu     ------------------- //
// ------------------ Miscellaneous ------------------- //

public class LogoutFragment extends Fragment {
	//------------------------------------------------------//
	// ------------------   Variables   ------------------- //
	//------------------------------------------------------//
	
	private LogoutViewModel       mLogoutViewModel;
	private FragmentLogoutBinding mFragmentLogoutBinding;
	
	//------------------------------------------------------//
	// ------------------   LifeCycle   ------------------- //
	//------------------------------------------------------//
	
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container,
	                         Bundle savedInstanceState) {
		mFragmentLogoutBinding = FragmentLogoutBinding.inflate(getLayoutInflater());
		
		mLogoutViewModel = ViewModelProviders
				.of(this)
				.get(LogoutViewModel.class);
		View           root     = inflater.inflate(R.layout.fragment_logout, container, false);
		final TextView textView = mFragmentLogoutBinding.textLogout;
		mLogoutViewModel
				.getText()
				.observe(requireActivity(), new Observer<String>() {
					@Override
					public void onChanged(@Nullable String s) {
						textView.setText(s);
					}
				});
		return root;
	}
}