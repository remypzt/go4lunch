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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import remy.pouzet.go4lunch.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class instrumentedTestingChatFunction {
	
	@Rule public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
	
	@Rule public GrantPermissionRule mGrantPermissionRule = GrantPermissionRule.grant("android.permission.ACCESS_FINE_LOCATION");
	
	@Test
	public void instrumentedTestingChatFunction() throws
	                                              InterruptedException {
		
		String date  = Calendar
				.getInstance()
				.getTime()
				.toString();
		String query = "Bonjour" + date;
		
		FirebaseAuth auth = FirebaseAuth.getInstance();
		
		auth.signInWithEmailAndPassword("test@test.net", "testtt");
		
		ViewInteraction appCompatImageButton = onView(allOf(withContentDescription("Open navigation drawer"), childAtPosition(allOf(withId(R.id.toolbar), childAtPosition(withId(R.id.app_bar_app_bar_laout), 0)), 2), isDisplayed()));
		appCompatImageButton.perform(click());
		Thread.sleep(150);
		
		ViewInteraction navigationMenuItemView = onView(allOf(withId(R.id.nav_chat), childAtPosition(allOf(withId(R.id.design_navigation_view), childAtPosition(withId(R.id.nav_view), 0)), 4), isDisplayed()));
		navigationMenuItemView.perform(click());
		Thread.sleep(150);
		
		ViewInteraction textInputEditText3 = onView(allOf(withId(R.id.activity_chat_message_edit_text), childAtPosition(allOf(withId(R.id.activity_chat_add_message_container), childAtPosition(withClassName(is("android.widget.RelativeLayout")), 3)), 1), isDisplayed()));
		textInputEditText3.perform(replaceText(query), closeSoftKeyboard());
		
		ViewInteraction appCompatButton4 = onView(allOf(withId(R.id.activity_chat_send_button), withText("Send"), childAtPosition(allOf(withId(R.id.activity_chat_add_message_container), childAtPosition(withClassName(is("android.widget.RelativeLayout")), 3)), 2), isDisplayed()));
		appCompatButton4.perform(click());
		Thread.sleep(1500);
		
		ViewInteraction textView = onView(allOf(withText(query)));
		textView.check(matches(withText(query)));
		
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
