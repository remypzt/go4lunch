package remy.pouzet.go4lunch;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import remy.pouzet.go4lunch.databinding.ActivityLoginBinding;

/**
 * Created by Rémy on 12/01/2018.
 */

//------------------------------------------------------//
// 1-----------------   Variables   ------------------- //
// 2-----------------   LifeCycle   ------------------- //
// 3-----------------   Functions   ------------------- //
// 4-----------------   Callbacks   ------------------- //
// 5-----------------    Adapter    ------------------- //
// 6-----------------     Intent    ------------------- //
// 7-----------------      Menu     ------------------- //
// 8-----------------Navigation & UI------------------- //
// 9-----------------      Data     ------------------- //
// 10---------------- Miscellaneous ------------------- //

//------------------------------------------------------//

public abstract class BaseActivity extends AppCompatActivity {
    //------------------------------------------------------//
    // ------------------   Variables   ------------------- //
    //------------------------------------------------------//
    
    private ActivityLoginBinding binding;
    //------------------------------------------------------//
    // ------------------   LifeCycle   ------------------- //
    //------------------------------------------------------//
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(this.getFragmentLayout());
        
    }
    
    //TODO what's this method ?
    public abstract int getFragmentLayout();
    //------------------------------------------------------//
    // ------------------      Menu     ------------------- //
    //------------------------------------------------------//
    
    protected void configureToolbar() {
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }
    
    // --------------------
    // UTILS
    // --------------------
    
    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }
    
    @Nullable
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth
                .getInstance()
                .getCurrentUser();
    }
    
}
