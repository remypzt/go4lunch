package remy.pouzet.go4lunch.view.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.data.repositories.models.User;
import remy.pouzet.go4lunch.data.service.realAPI.UserHelper;
import remy.pouzet.go4lunch.databinding.ActivityMainBinding;
import remy.pouzet.go4lunch.databinding.FragmentSettingsBinding;
import remy.pouzet.go4lunch.view.activities.MainActivity;
import remy.pouzet.go4lunch.viewmodel.SettingsViewModel;

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
	private SettingsViewModel       mSettingsViewModel;
	private FragmentSettingsBinding binding;
	private ActivityMainBinding     mMainBinding;

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
								deleteUserFromFirebase();
							}
						})
						.setNegativeButton(R.string.popup_message_choice_no, null)
						.show();
			}
		});
	}
	
	private void deleteUserFromFirebase() {
		FirebaseUser user = FirebaseAuth
				.getInstance()
				.getCurrentUser();
		
		AuthCredential credential = EmailAuthProvider.getCredential("user@example.com", "password1234");

// Prompt the user to re-provide their sign-in credentials
		user
				.reauthenticate(credential)
				.addOnCompleteListener(new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						//4 - We also delete user from firestore storage
						UserHelper
								.deleteUser(getCurrentUser().getUid())
								.addOnFailureListener(onFailureListener());
						user.delete();
						Intent intent = new Intent(requireContext(), MainActivity.class);
						startActivity(intent);
					}
					
				});
		
	}
	
	protected OnFailureListener onFailureListener() {
		return new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Toast
						.makeText(requireContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG)
						.show();
			}
		};
	}
	
	//TODO MAJ username
	// 3 - Update User Username
//	private void updateUsernameInFirebase(){
//
//		this.progressBar.setVisibility(View.VISIBLE);
//		String username = this.textInputEditTextUsername.getText().toString();
//
//		if (this.getCurrentUser() != null){
//			if (!username.isEmpty() &&  !username.equals(getString(R.string.info_no_username_found))){
//				UserHelper.updateUsername(username, this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener()).addOnSuccessListener(this.updateUIAfterRESTRequestsCompleted(UPDATE_USERNAME));
//			}
//		}
//	}
	
	//------------------------------------------------------//
	// ------------------Navigation & UI------------------- //
	//------------------------------------------------------//
	
	public void seekBarManagement() {
		binding.seekBar2.setMax(5000);
		binding.seekBar2.setProgress(100);
		binding.customizeRadiusTextView.setText(getString(R.string.rayon_de_recherche) + binding.seekBar2.getProgress() + getString(R.string.metres));
		binding.seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar,
			                              int progress,
			                              boolean fromUser) {
				// this operation is to define  set an intertval step
				progress = (progress / 20) * 20;
				seekBar.setProgress(progress);
//				progress = binding.seekBar2.getProgress();
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
	
	//------------------------------------------------------//
	// 9-----------------      Data     ------------------- //
	//------------------------------------------------------//
	
	@Nullable
	protected FirebaseUser getCurrentUser() {
		return FirebaseAuth
				.getInstance()
				.getCurrentUser();
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
		
		// 7 - Get additional data from Firestore (isMentor & Username)
		UserHelper
				.getUser(this
						         .getCurrentUser()
						         .getUid())
				.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
					@Override
					public void onSuccess(DocumentSnapshot documentSnapshot) {
						User   currentUser = documentSnapshot.toObject(User.class);
						String username    = TextUtils.isEmpty(currentUser.getUsername())
						                     ? getString(R.string.info_no_username_found)
						                     : currentUser.getUsername();
//				textInputEditTextUsername.setText(username);
					}
				});
	}
}
