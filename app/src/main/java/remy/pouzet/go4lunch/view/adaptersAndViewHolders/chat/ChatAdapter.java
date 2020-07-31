package remy.pouzet.go4lunch.view.adaptersAndViewHolders.chat;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.data.repositories.models.Message;

/**
 * Created by Remy Pouzet on 31/07/2020.
 */
public class ChatAdapter extends FirestoreRecyclerAdapter<Message, MessageViewHolder> {
	
	//FOR DATA
	private final RequestManager glide;
	private final String         idCurrentUser;
	//FOR COMMUNICATION
	private       Listener       callback;
	
	public ChatAdapter(@NonNull FirestoreRecyclerOptions<Message> options,
	                   RequestManager glide,
	                   Listener callback,
	                   String idCurrentUser) {
		super(options);
		this.glide         = glide;
		this.callback      = callback;
		this.idCurrentUser = idCurrentUser;
	}
	
	@Override
	public MessageViewHolder onCreateViewHolder(ViewGroup parent,
	                                            int viewType) {
		return new MessageViewHolder(LayoutInflater
				                             .from(parent.getContext())
				                             .inflate(R.layout.activity_chat_item, parent, false));
	}
	
	@Override
	public void onDataChanged() {
		super.onDataChanged();
		this.callback.onDataChanged();
	}
	
	@Override
	protected void onBindViewHolder(@NonNull MessageViewHolder holder,
	                                int position,
	                                @NonNull Message model) {
		holder.updateWithMessage(model, this.idCurrentUser, this.glide);
	}
	
	public interface Listener {
		void onDataChanged();
	}
}
