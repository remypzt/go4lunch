package remy.pouzet.go4lunch.view.adaptersAndViewHolders.workmates;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.data.repositories.models.User;

/**
 * Created by Remy Pouzet on 31/07/2020.
 */
public class WorkmatesAdapter extends FirestoreRecyclerAdapter<User, WorkmatesViewHolder> {
	
	//FOR DATA
	private final RequestManager glide;
	private final String         idCurrentUser;
	//FOR COMMUNICATION
	private       Listener       callback;
	
	public WorkmatesAdapter(@NonNull FirestoreRecyclerOptions<User> options,
	                        RequestManager glide,
	                        Listener callback,
	                        String idCurrentUser) {
		super(options);
		this.glide         = glide;
		this.callback      = callback;
		this.idCurrentUser = idCurrentUser;
	}
	
	@Override
	public WorkmatesViewHolder onCreateViewHolder(ViewGroup parent,
	                                              int viewType) {
		return new WorkmatesViewHolder(LayoutInflater
				                               .from(parent.getContext())
				                               .inflate(R.layout.content_items_of_workmates_list_view, parent, false));
	}
	
	@Override
	public void onDataChanged() {
		super.onDataChanged();
		this.callback.onDataChanged();
	}
	
	@Override
	protected void onBindViewHolder(@NonNull WorkmatesViewHolder holder,
	                                int position,
	                                @NonNull User user) {
		holder.updateWorkmates(user, this.idCurrentUser, this.glide);
		
	}
	
	public interface Listener {
		void onDataChanged();
	}
}
