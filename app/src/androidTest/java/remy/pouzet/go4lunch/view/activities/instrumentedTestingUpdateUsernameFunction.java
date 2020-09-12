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
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class instrumentedTestingUpdateUsernameFunction {
	
	@Rule public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
	
	@Rule public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant("android.permission.ACCESS_FINE_LOCATION");
	
	@Test
	public void instrumentedTestingUpdateUsername() throws
	                                                InterruptedException {
		FirebaseAuth auth = FirebaseAuth.getInstance();
		
		auth.signInWithEmailAndPassword("test@test.net", "testtt");
		
		ViewInteraction appCompatImageButton = onView(allOf(withContentDescription("Open navigation drawer"), childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withId(R.id.app_bar_app_bar_laout), 0)), 2), isDisplayed()));
		appCompatImageButton.perform(click());
		Thread.sleep(150);
		
		ViewInteraction navigationMenuItemView = onView(allOf(withId(R.id.nav_settings), childAtPosition(allOf(withId(R.id.design_navigation_view), childAtPosition(withId(R.id.nav_view), 0)), 2), isDisplayed()));
		navigationMenuItemView.perform(click());
		Thread.sleep(150);
		
		ViewInteraction textInputEditText3 = onView(allOf(withId(R.id.settings_fragment__edit_text_username), withText("Test1"), childAtPosition(childAtPosition(withId(R.id.settings_fragments_main_linear_layout), 0), 1), isDisplayed()));
		textInputEditText3.perform(replaceText("Testun"));
		
		ViewInteraction textInputEditText4 = onView(allOf(withId(R.id.settings_fragment__edit_text_username), withText("Testun"), childAtPosition(childAtPosition(withId(R.id.settings_fragments_main_linear_layout), 0), 1), isDisplayed()));
		textInputEditText4.perform(closeSoftKeyboard());
		
		ViewInteraction appCompatButton4 = onView(allOf(withId(R.id.settings_fragment_button_update), withText("Update username"), childAtPosition(childAtPosition(withId(R.id.settings_fragments_main_linear_layout), 1), 0), isDisplayed()));
		appCompatButton4.perform(click());
		Thread.sleep(1500);
		
		ViewInteraction appCompatImageButton2 = onView(allOf(withContentDescription("Navigate up"), childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withId(R.id.app_bar_app_bar_laout), 0)), 2), isDisplayed()));
		appCompatImageButton2.perform(click());
		Thread.sleep(150);
		
		ViewInteraction appCompatImageButton3 = onView(allOf(withContentDescription("Open navigation drawer"), childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withId(R.id.app_bar_app_bar_laout), 0)), 2), isDisplayed()));
		appCompatImageButton3.perform(click());
		Thread.sleep(150);
		
		ViewInteraction textView = onView(allOf(withId(R.id.profile_activity_edit_text_username), withText("Testun"), childAtPosition(childAtPosition(IsInstanceOf.instanceOf(android.widget.LinearLayout.class), 1), 0), isDisplayed()));
		textView.check(matches(withText("Testun")));
		
		ViewInteraction appCompatImageButton4 = onView(allOf(withContentDescription("Open navigation drawer"), childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withId(R.id.app_bar_app_bar_laout), 0)), 2), isDisplayed()));
		appCompatImageButton4.perform(click());
		Thread.sleep(150);
		
		ViewInteraction navigationMenuItemView2 = onView(allOf(withId(R.id.nav_settings), childAtPosition(allOf(withId(R.id.design_navigation_view), childAtPosition(withId(R.id.nav_view), 0)), 2), isDisplayed()));
		navigationMenuItemView2.perform(click());
		Thread.sleep(150);
		
		ViewInteraction textInputEditText5 = onView(allOf(withId(R.id.settings_fragment__edit_text_username), withText("Testun"), childAtPosition(childAtPosition(withId(R.id.settings_fragments_main_linear_layout), 0), 1), isDisplayed()));
		textInputEditText5.perform(replaceText("Test1"));
		
		ViewInteraction textInputEditText6 = onView(allOf(withId(R.id.settings_fragment__edit_text_username), withText("Test1"), childAtPosition(childAtPosition(withId(R.id.settings_fragments_main_linear_layout), 0), 1), isDisplayed()));
		textInputEditText6.perform(closeSoftKeyboard());
		
		ViewInteraction appCompatButton5 = onView(allOf(withId(R.id.settings_fragment_button_update), withText("Update username"), childAtPosition(childAtPosition(withId(R.id.settings_fragments_main_linear_layout), 1), 0), isDisplayed()));
		appCompatButton5.perform(click());
		Thread.sleep(1500);
		
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
