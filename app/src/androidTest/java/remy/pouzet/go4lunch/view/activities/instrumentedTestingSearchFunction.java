package remy.pouzet.go4lunch.view.activities;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.lifecycle.MutableLiveData;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import remy.pouzet.go4lunch.R;
import remy.pouzet.go4lunch.data.repositories.RestaurantsRepository;
import remy.pouzet.go4lunch.data.repositories.models.Restaurant;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class instrumentedTestingSearchFunction {
	
	@Rule public ActivityTestRule<MainActivity> mActivityTestRule    = new ActivityTestRule<>(MainActivity.class);
	@Rule public GrantPermissionRule            mGrantPermissionRule = GrantPermissionRule.grant("android.permission.ACCESS_FINE_LOCATION");
	private      double                         latitude, longitude;
	
	@Test
	public void instrumentedTestingUpdateUsername() throws
	                                                InterruptedException {
		String firstRestaurant;
		
		FirebaseAuth auth = FirebaseAuth.getInstance();
		
		auth.signInWithEmailAndPassword("test@test.net", "testtt");
		
		ViewInteraction bottomNavigationItemView = onView(allOf(withId(R.id.navigation_list_view), withContentDescription("List View"), childAtPosition(childAtPosition(withId(R.id.nav_view_bottom), 0), 1), isDisplayed()));
		bottomNavigationItemView.perform(click());
		Thread.sleep(3500);
		
		MutableLiveData<List<Restaurant>> localRestaurantsRepository = RestaurantsRepository
				.getInstance()
				.getRestaurants(37.2586033, -121.845295);
		firstRestaurant = localRestaurantsRepository
				.getValue()
				.get(0)
				.getName();
		
		ViewInteraction viewGroup = onView(allOf(withId(R.id.Articles_layout), childAtPosition(allOf(withId(R.id.fragment_restaurants_recycler_view), childAtPosition(IsInstanceOf.instanceOf(android.widget.LinearLayout.class), 0)), 0), isDisplayed()));
		viewGroup.check(matches(isDisplayed()));
		
		ViewInteraction viewGroup2 = onView(allOf(withId(R.id.Articles_layout), childAtPosition(allOf(withId(R.id.fragment_restaurants_recycler_view), childAtPosition(IsInstanceOf.instanceOf(android.widget.LinearLayout.class), 0)), 3), isDisplayed()));
		viewGroup2.check(matches(isDisplayed()));
		
		ViewInteraction appCompatImageView = onView(allOf(withClassName(is("androidx.appcompat.widget.AppCompatImageView")), withContentDescription("Search"), childAtPosition(allOf(withClassName(is("android.widget.LinearLayout")), childAtPosition(withId(R.id.places_autocomplete_search_bar_container), 0)), 1), isDisplayed()));
		appCompatImageView.perform(click());
		Thread.sleep(1500);
		
		ViewInteraction searchAutoComplete = onView(allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")), childAtPosition(allOf(withClassName(is("android.widget.LinearLayout")), childAtPosition(withClassName(is("android.widget.LinearLayout")), 1)), 0), isDisplayed()));
		searchAutoComplete.perform(replaceText(firstRestaurant), closeSoftKeyboard());
		Thread.sleep(4000);
		
		viewGroup.check(matches(isDisplayed()));
		viewGroup2.check(doesNotExist());
		
	}
	
	private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher,
	                                             final int position) {
		
		return new TypeSafeMatcher<View>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("Child at position " + position + " in parent ");
				parentMatcher.describeTo(description);
			}
			
			@Override
			public boolean matchesSafely(View view) {
				ViewParent parent = view.getParent();
				return parent instanceof ViewGroup && parentMatcher.matches(parent) && view.equals(((ViewGroup) parent).getChildAt(position));
			}
		};
	}
}

