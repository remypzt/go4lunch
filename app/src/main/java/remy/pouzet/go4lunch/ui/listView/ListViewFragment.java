package remy.pouzet.go4lunch.ui.listView;

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
import remy.pouzet.go4lunch.databinding.FragmentListViewBinding;

// ------------------   Functions   ------------------- //
// ------------------   Callbacks   ------------------- //
// ------------------    Adapter    ------------------- //
// ------------------      Menu     ------------------- //
// ------------------ Miscellaneous ------------------- //
// ------------------     Intent    ------------------- //
// ------------------     Navigation & UI    ------------------- //

public class ListViewFragment extends Fragment {

// ------------------   Variables   ------------------- //
	
	private ListViewViewModel       mListViewViewModel;
	private FragmentListViewBinding mFragmentListViewBinding;
	
	// ------------------   LifeCycle   ------------------- //
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container,
	                         Bundle savedInstanceState) {
		mFragmentListViewBinding = FragmentListViewBinding.inflate(getLayoutInflater());
		
		mListViewViewModel = ViewModelProviders
				.of(this)
				.get(ListViewViewModel.class);
		View root = inflater.inflate(R.layout.fragment_list_view, container, false);
		
		final TextView textView = root.findViewById(R.id.text_list_view);
//				mFragmentListViewBinding.textListView;
		mListViewViewModel
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