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

public class ListViewFragment extends Fragment {
	
	private ListViewViewModel mListViewViewModel;
	
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container,
	                         Bundle savedInstanceState) {
		mListViewViewModel = ViewModelProviders
				.of(this)
				.get(ListViewViewModel.class);
		View           root     = inflater.inflate(R.layout.fragment_list_view, container, false);
		final TextView textView = root.findViewById(R.id.text_list_view);
		mListViewViewModel
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