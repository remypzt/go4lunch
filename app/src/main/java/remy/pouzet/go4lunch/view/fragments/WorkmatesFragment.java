package remy.pouzet.go4lunch.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

import remy.pouzet.go4lunch.data.repositories.models.User;
import remy.pouzet.go4lunch.data.service.realAPI.UserHelper;
import remy.pouzet.go4lunch.databinding.FragmentWorkmatesListViewBinding;
import remy.pouzet.go4lunch.view.adaptersAndViewHolders.workmates.WorkmatesAdapter;
import remy.pouzet.go4lunch.viewmodel.WorkmatesViewModel;

// ------------------   Functions   ------------------- //
// ------------------   Callbacks   ------------------- //
// ------------------    Adapter    ------------------- //
// ------------------      Menu     ------------------- //
// ------------------ Miscellaneous ------------------- //
// ------------------     Intent    ------------------- //
// ------------------Navigation & UI------------------- //

public class WorkmatesFragment extends Fragment implements WorkmatesAdapter.Listener {
	//------------------------------------------------------//
	// ------------------   Variables   ------------------- //
	//------------------------------------------------------//
	
	private WorkmatesViewModel mWorkmatesViewModel;
	
	public FragmentWorkmatesListViewBinding mFragmentWorkmatesListViewBinding;
	// FOR DESIGN
	// 1 - Getting all views needed
	RecyclerView recyclerView;
	// FOR DATA
	// 2 - Declaring Adapter and data
	private           WorkmatesAdapter mWorkmatesAdapter;
	@Nullable private User             modelCurrentUser;
	
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
		recyclerView                      = mFragmentWorkmatesListViewBinding.fragmentWorkmatesRecyclerView;
		View root = mFragmentWorkmatesListViewBinding.getRoot();
		this.configureRecyclerView();
//		this.getCurrentUserFromFirestore();
		return root;
		
	}
	
	private void configureRecyclerView() {
		//Configure Adapter & RecyclerView
		this.mWorkmatesAdapter = new WorkmatesAdapter(generateOptionsForAdapter(UserHelper.getAllUsers("user")), Glide.with(requireActivity()), this, FirebaseAuth
				.getInstance()
				.getCurrentUser()
				.getUid());
		mWorkmatesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
			@Override
			public void onItemRangeInserted(int positionStart,
			                                int itemCount) {
				recyclerView.smoothScrollToPosition(mWorkmatesAdapter.getItemCount());
			}
		});
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		recyclerView.setAdapter(this.mWorkmatesAdapter);
	}
	
	// --------------------
//	// REST REQUESTS
//	// --------------------
//	// 4 - Get Current User from Firestore
//	private void getCurrentUserFromFirestore() {
//		UserHelper
//				.getUser(FirebaseAuth
//						         .getInstance()
//						         .getCurrentUser()
//						         .getUid())
//				.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//					@Override
//					public void onSuccess(DocumentSnapshot documentSnapshot) {
//						modelCurrentUser = documentSnapshot.toObject(User.class);
//					}
//				});
//	}
	
	// 6 - Create options for RecyclerView from a Query
	private FirestoreRecyclerOptions<User> generateOptionsForAdapter(Query query) {
		return new FirestoreRecyclerOptions.Builder<User>()
				.setQuery(query, User.class)
				.setLifecycleOwner(this)
				.build();
	}
	
	@Override
	public void onDataChanged() {
	
	}
}