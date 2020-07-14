package remy.pouzet.go4lunch.ui.listView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RestaurantsListViewViewModel extends ViewModel {
	
	private MutableLiveData<String> mText;
	
	public RestaurantsListViewViewModel() {
		mText = new MutableLiveData<>();
		mText.setValue("This is listview fragment");
	}
	
	public LiveData<String> getText() {
		return mText;
	}
}