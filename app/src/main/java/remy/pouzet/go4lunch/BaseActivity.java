package remy.pouzet.go4lunch;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import remy.pouzet.go4lunch.databinding.ActivityLoginBinding;

/**
 * Created by Philippe on 12/01/2018.
 */

public abstract class BaseActivity extends AppCompatActivity {
    
    // --------------------
    // LIFE CYCLE
    // --------------------
    
    private ActivityLoginBinding binding;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(this.getFragmentLayout());
        
    }
    
    public abstract int getFragmentLayout();
    
    // --------------------
    // UI
    // --------------------
    
    protected void configureToolbar() {
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }
}
