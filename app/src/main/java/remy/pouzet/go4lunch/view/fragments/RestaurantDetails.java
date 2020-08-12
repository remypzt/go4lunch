package remy.pouzet.go4lunch.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.data.repositories.models.Restaurant;

public class RestaurantDetails extends AppCompatActivity {
	
	private final static String EXTRA_RESTAURANT = "EXTRA_RESTAURANT";
	
	public static void startActivity(Context context,
	                                 Restaurant restaurant) {
		Intent intent = new Intent(context, RestaurantDetails.class);
		intent.putExtra(EXTRA_RESTAURANT, restaurant);
		context.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant_details_fragment);
		Restaurant restaurant;
		restaurant = (Restaurant) getIntent().getSerializableExtra(EXTRA_RESTAURANT);
		
		Toast
				.makeText(this, restaurant.getName(), Toast.LENGTH_LONG)
				.show();
	}
}