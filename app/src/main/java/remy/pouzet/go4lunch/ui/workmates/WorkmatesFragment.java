package remy.pouzet.go4lunch.ui.workmates;

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

public class WorkmatesFragment extends Fragment {
	
	// ------------------   Variables   ------------------- //
	
	private WorkmatesViewModel mWorkmatesViewModel;
	
	// ------------------   LifeCycle   ------------------- //
	
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container,
	                         Bundle savedInstanceState) {
		mWorkmatesViewModel = ViewModelProviders
				.of(this)
				.get(WorkmatesViewModel.class);
		View           root     = inflater.inflate(R.layout.fragment_workmates, container, false);
		final TextView textView = root.findViewById(R.id.text_workmates);
		mWorkmatesViewModel
				.getText()
				.observe(getViewLifecycleOwner(), new Observer<String>() {
					@Override
					public void onChanged(@Nullable String s) {
						textView.setText(s);
					}
				});
		return root;
	}
}