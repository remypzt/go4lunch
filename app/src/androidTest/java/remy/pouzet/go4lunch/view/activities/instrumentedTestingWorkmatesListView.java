package remy.pouzet.go4lunch.view.activities;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

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

import remy.pouzet.go4lunch.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class instrumentedTestingWorkmatesListView {
	
	@Rule public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
	
	@Rule public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant("android.permission.ACCESS_FINE_LOCATION");
	
	@Test
	public void instrumentedTestingWorkmatesListView() throws
	                                                   InterruptedException {
		String firstRestaurant;
		
		FirebaseAuth auth = FirebaseAuth.getInstance();
		
		auth.signInWithEmailAndPassword("test@test.net", "testtt");
		
		ViewInteraction bottomNavigationItemView = onView(allOf(withId(R.id.navigation_workmates), withContentDescription("Workmates"), childAtPosition(childAtPosition(withId(R.id.nav_view_bottom), 0), 2), isDisplayed()));
		bottomNavigationItemView.perform(click());
		Thread.sleep(1500);
		
		ViewInteraction viewGroup = onView(allOf(withId(R.id.Articles_layout), childAtPosition(allOf(withId(R.id.fragment_workmates_recycler_view), childAtPosition(IsInstanceOf.instanceOf(android.widget.LinearLayout.class), 0)), 0), isDisplayed()));
		viewGroup.check(matches(isDisplayed()));
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
