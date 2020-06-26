package remy.pouzet.go4lunch.ui.settings;

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

// ------------------   Functions   ------------------- //
// ------------------   Callbacks   ------------------- //
// ------------------    Adapter    ------------------- //
// ------------------      Menu     ------------------- //
// ------------------ Miscellaneous ------------------- //
// ------------------     Intent    ------------------- //
// ------------------     Navigation & UI    ------------------- //

public class SettingsFragment extends Fragment {

// ------------------   Variables   ------------------- //
	
	private SettingsViewModel mSettingsViewModel;

// ------------------   LifeCycle   ------------------- //
	
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container,
	                         Bundle savedInstanceState) {
		mSettingsViewModel = ViewModelProviders
				.of(this)
				.get(SettingsViewModel.class);
		View           root     = inflater.inflate(R.layout.fragment_settings, container, false);
		final TextView textView = root.findViewById(R.id.text_settings);
		mSettingsViewModel
				.getText()
				.observe(this, new Observer<String>() {
					@Override
					public void onChanged(@Nullable String s) {
						textView.setText(s);
					}
				});
		return root;
	}
}