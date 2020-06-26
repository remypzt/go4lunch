package remy.pouzet.go4lunch;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import remy.pouzet.go4lunch.databinding.ActivityLoginBinding;

/**
 * Created by Rémy on 12/01/2018.
 */

//------------------------------------------------------//
// ------------------   Variables   ------------------- //
// ------------------   LifeCycle   ------------------- //
// ------------------   Functions   ------------------- //
// ------------------   Callbacks   ------------------- //
// ------------------    Adapter    ------------------- //
// ------------------      Menu     ------------------- //
// ------------------ Miscellaneous ------------------- //
// ------------------     Intent    ------------------- //
// ------------------Navigation & UI------------------- //
//------------------------------------------------------//

public abstract class BaseActivity extends AppCompatActivity {
    
    // ------------------   Variables   ------------------- //
    
    private ActivityLoginBinding binding;
    
    // ------------------   LifeCycle   ------------------- //
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(this.getFragmentLayout());
        
    }
    
    //TODO what's this method ?
    public abstract int getFragmentLayout();
    
    // ------------------      Menu     ------------------- //
    
    protected void configureToolbar() {
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }
}
