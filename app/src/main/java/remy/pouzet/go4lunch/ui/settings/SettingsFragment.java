package remy.pouzet.go4lunch.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import remy.pouzet.go4lunch.MainActivity;
import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.databinding.FragmentSettingsBinding;

// ------------------   Functions   ------------------- //
// ------------------   Callbacks   ------------------- //
// ------------------    Adapter    ------------------- //
// ------------------      Menu     ------------------- //
// ------------------ Miscellaneous ------------------- //
// ------------------     Intent    ------------------- //
// ------------------Navigation & UI------------------- //

public class SettingsFragment extends Fragment {
//------------------------------------------------------//
// ------------------   Variables   ------------------- //
//------------------------------------------------------//
	
	private static final int SIGN_OUT_TASK = 10;
	
	private static final String                  LOGTAG           = "SeekBarDemo";
	private static       int                     DELTA_VALUE      = 5;
	private              SeekBar                 mSeekBar;
	private              TextView                mTextViewSeekBar;
	private              SettingsViewModel       mSettingsViewModel;
	private static final int                     DELETE_USER_TASK = 20;
	private              FragmentSettingsBinding binding;
//------------------------------------------------------//
// ------------------   LifeCycle   ------------------- //
//------------------------------------------------------//
	
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container,
	                         Bundle savedInstanceState) {
		
		binding = FragmentSettingsBinding.inflate(inflater, container, false);
		View view = binding.getRoot();
		updateUIWhenCreating();
		seekBarManagement();
		return view;

//		mSettingsViewModel = ViewModelProviders
//				.of(this)
//				.get(SettingsViewModel.class);
//		View           root     = inflater.inflate(R.layout.fragment_settings, container, false);
//
//		mSettingsViewModel
//				.getText()
//				.observe(this, new Observer<String>() {
//					@Override
//					public void onChanged(@Nullable String s) {
//
//					}
//				});
//		return root;
	
	}
	
	@Override
	public void onViewCreated(@NonNull View view,
	                          @Nullable Bundle savedInstanceState) {
		onClickDeleteButton();
		
	}
	
	//------------------------------------------------------//
	// ------------------   Functions   ------------------- //
	//------------------------------------------------------//
	
	public void onClickDeleteButton() {
		binding.settingsFragmentButtonDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(requireContext())
						
						.setMessage(R.string.popup_message_confirmation_delete_account)
						.setPositiveButton(R.string.popup_message_choice_yes, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface,
							                    int i) {
//
								AuthUI
										.getInstance()
										.delete(requireContext());
//
//								AuthUI
//////										.getInstance()
//////										.signOut(requireContext());
////								passByMainActivity();
							}
						})
						.setNegativeButton(R.string.popup_message_choice_no, null)
						.show();
			}
		});
	}
	
	public void seekBarManagement() {
		binding.seekBar2.setMax(5000);
		
		binding.customizeRadiusTextView.setText(getString(R.string.rayon_de_recherche) + binding.seekBar2.getProgress() + getString(R.string.metres));
		binding.seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			int progress = 0;
			
			@Override
			public void onProgressChanged(SeekBar seekBar,
			                              int progress,
			                              boolean fromUser) {
				progress = binding.seekBar2.getProgress();
				binding.customizeRadiusTextView.setText(getString(R.string.rayon_de_recherche) + binding.seekBar2.getProgress() + getString(R.string.metres));
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				binding.customizeRadiusTextView.setText(getString(R.string.rayon_de_recherche) + binding.seekBar2.getProgress() + getString(R.string.metres));
				
			}
		});
	}
	
	public void updateUIWhenCreating() {
		//Get picture URL from Firebase
		if (this
				    .getCurrentUser()
				    .getPhotoUrl() != null) {
			Glide
					.with(requireContext())
					.load(this
							      .getCurrentUser()
							      .getPhotoUrl())
					.apply(RequestOptions.circleCropTransform())
					.into(binding.settingsFragmentImageviewProfile);
			
		}
		
		//Get email & username from Firebase
		String email = TextUtils.isEmpty(this
				                                 .getCurrentUser()
				                                 .getEmail())
		               ? getString(R.string.info_no_email_found)
		               : this
				               .getCurrentUser()
				               .getEmail();
		String username = TextUtils.isEmpty(this
				                                    .getCurrentUser()
				                                    .getDisplayName())
		                  ? getString(R.string.info_no_username_found)
		                  : this
				                  .getCurrentUser()
				                  .getDisplayName();
		
		//Update views with data's user
		binding.settingsFragmentTextViewEmail.setText(email);
		binding.settingsFragmentEditTextUsername.setText(username);
	}

//				AuthUI
//						.getInstance()
//						.delete(requireContext());
//				getActivity().finish();
//			}
//		});
//	}

//
//				((View.OnClickListener) new AlertDialog.Builder(requireContext())
//
//			.setMessage(R.string.popup_message_confirmation_delete_account)
//			.setPositiveButton(R.string.popup_message_choice_yes, new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialogInterface, int i) {
//					deleteUserFromFirebase();
//				}
//			})
//			.setNegativeButton(R.string.popup_message_choice_no, null)
//			.show());
//	}
	
	//------------------------------------------------------//
	// ------------------Navigation & UI------------------- //
	//------------------------------------------------------//
	
	@Nullable
	protected FirebaseUser getCurrentUser() {
		return FirebaseAuth
				.getInstance()
				.getCurrentUser();
	}
	
	//------------------------------------------------------//
	// 9-----------------      Data     ------------------- //
	//------------------------------------------------------//
	
	public void passByMainActivity() {
		Intent mainActivityIntent = new Intent(requireContext(), MainActivity.class);
		startActivity(mainActivityIntent);
	}
	
	private void deleteUserFromFirebase() {
		if (this.getCurrentUser() != null) {
			AuthUI
					.getInstance()
					.delete(requireContext());
//					.addOnSuccessListener(requireContext(), requireContext().updateUIAfterRESTRequestsComplete(DELETE_USER_TASK));
//			getActivity().finish();
		}
	}
}
