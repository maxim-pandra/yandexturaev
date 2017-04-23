package com.example.maxim.turaevyandex;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.maxim.turaevyandex.R;
import com.example.maxim.turaevyandex.translator.TranslatorActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Attention: This test will run successfully only on freshly installed application
 *
 * UI test that checks empty screens
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ApplicationBasicSartupTest {

    @Rule
    public ActivityTestRule<TranslatorActivity> mActivityTestRule = new ActivityTestRule<>(TranslatorActivity.class);

    @Test
    public void applicationBasicSartupTest() {


        ViewInteraction textView = onView(
                allOf(withId(R.id.emptyViewTitle), withText("Type text to the field above.\nYou can change translation direction via arrows in the right upper corner"),
                        isDisplayed()));
        textView.check(matches(withText("Type text to the field above.\nYou can change translation direction via arrows in the right upper corner")));

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_history), withContentDescription("History"), isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.noHistoryMain), withText("you have no history"),
                        isDisplayed()));
        textView2.check(matches(withText("you have no history")));

        ViewInteraction bottomNavigationItemView2 = onView(
                allOf(withId(R.id.navigation_settings), withContentDescription("Settings"), isDisplayed()));
        bottomNavigationItemView2.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.settings_text), withText("Coming soon :)"),
                        isDisplayed()));
        textView3.check(matches(withText("Coming soon :)")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
