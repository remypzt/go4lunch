package remy.pouzet.go4lunch.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.databinding.FragmentWorkmatesListViewBinding;
import remy.pouzet.go4lunch.viewmodel.WorkmatesViewModel;

// ------------------   Functions   ------------------- //
// ------------------   Callbacks   ------------------- //
// ------------------    Adapter    ------------------- //
// ------------------      Menu     ------------------- //
// ------------------ Miscellaneous ------------------- //
// ------------------     Intent    ------------------- //
// ------------------Navigation & UI------------------- //

public class WorkmatesFragment extends Fragment {
	//------------------------------------------------------//
	// ------------------   Variables   ------------------- //
	//------------------------------------------------------//
	
	private WorkmatesViewModel               mWorkmatesViewModel;
	private FragmentWorkmatesListViewBinding mFragmentWorkmatesListViewBinding;
	
	//------------------------------------------------------//
	// ------------------   LifeCycle   ------------------- //
	//------------------------------------------------------//
	
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container,
	                         Bundle savedInstanceState) {
		mWorkmatesViewModel = ViewModelProviders
				.of(this)
				.get(WorkmatesViewModel.class);
		
		mFragmentWorkmatesListViewBinding = FragmentWorkmatesListViewBinding.inflate(getLayoutInflater());
		
		View root = inflater.inflate(R.layout.fragment_workmates_list_view, container, false);
		return root;
	}
}