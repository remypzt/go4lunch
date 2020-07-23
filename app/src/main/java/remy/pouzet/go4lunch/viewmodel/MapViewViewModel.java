package remy.pouzet.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapViewViewModel extends ViewModel {
	
	private MutableLiveData<String> mText;
	
	public MapViewViewModel() {
		mText = new MutableLiveData<>();
		mText.setValue("This is mapview fragment");
	}
	
	public LiveData<String> getText() {
		return mText;
	}
}