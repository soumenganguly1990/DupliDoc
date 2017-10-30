package com.soumen.duplidoc.activities;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.soumen.duplidoc.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.Espresso.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by Soumen on 26-10-2017.
 */
public class LockSetterActivityTestEspresso {

    @Rule
    public ActivityTestRule<LockSetterActivity> lockSetterActivityActivityTestRule = new ActivityTestRule<LockSetterActivity>(LockSetterActivity.class);

    private LockSetterActivity lockSetterActivity = null;

    @Before
    public void setUp() throws Exception {
        Intents.init();
        lockSetterActivity = lockSetterActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

    @Test
    public void onCreate() throws Exception {
        Thread.sleep(1000);
        onView(withId(R.id.txtOne)).perform(click());
        onView(withId(R.id.txtTwo)).perform(click());

        onView(withId(R.id.edtPassCode)).check(matches(isEditTextValueEqualTo("12")));

        /* click delete thrice */
        onView(withId(R.id.txtDel)).perform(click());
        onView(withId(R.id.txtDel)).perform(click());
        onView(withId(R.id.txtDel)).perform(click());

        onView(withText("Nothing to delete")).inRoot(withDecorView(not(is(
                lockSetterActivityActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

        /* clear the edittext and type 1234 and close the soft keyboard */
        onView(withId(R.id.edtPassCode)).perform(clearText());
        onView(withId(R.id.edtPassCode)).perform(typeText("1234")).perform(ViewActions.closeSoftKeyboard());

        /* match if content is 1234 */
        onView(withId(R.id.edtPassCode)).check(matches(isEditTextValueEqualTo("1234")));

        /* if checkbox is not checked, then check it */
        onView(withId(R.id.chkRememberMe)).check(matches(isNotChecked()))
                .perform(click()).check(matches(isChecked()));
        onView(withId(R.id.txtPassCodeOk)).perform(click());

        /* now check if it was redirected to main acrtivity */
        intended(hasComponent(MainActivity.class.getName()));

        changedToMainActivity();
    }

    @Test
    public void changedToMainActivity() throws Exception {

        onView(withId(R.id.linTexts)).check(matches(isDisplayed()));
        onView(withId(R.id.linImages)).check(matches(isDisplayed()));
        onView(withId(R.id.linVideos)).check(matches(isDisplayed()));
        onView(withId(R.id.linAudio)).check(matches(isDisplayed()));

	    /* we need to perform a test on the do not remember me anymore functionality */
        Thread.sleep(2000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Don\'t Remember Me Anymore")).perform(click());

	    /* sleep for 2 more seconds */
        Thread.sleep(2000);

	    /* te following section is line optional, just need to comment and uncomment some lines in order to get executed */

	    /* perform a click on the audio tile of the main dashboard */
        //onView(withId(R.id.linAudio)).perform(click());

	    /* now perform a click on the images tile of the dashboard */
        onView(withId(R.id.linImages)).perform(click());
	  
	    /* now perform a click on the videos tile of the dashboard */
        //onView(withId(R.id.linVideos)).perform(click());
	  
	    /* now perform a click on the texts tile of the dashboard */
        //onView(withId(R.id.linTexts)).perform(click());

        intended(hasComponent(FileListDetailsActivity.class.getName()));

        changedToFileListActivity();
    }

    @Test
    public void changedToFileListActivity() throws Exception {
        //onView(withId(R.id.cardRecoverableMemory)).check(matches(isDisplayed()));
        //onView(withId(R.id.rclDuplicateList)).check(matches(not(isDisplayed())));
	
	    /* first select sort by name option from the overflow menu, then click on ascending option */
        Thread.sleep(2000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Sort By Name")).perform(click());
        onView(withText("Ascending")).perform(click());

	    /* again select sort by option and hen select descending option */
        Thread.sleep(2000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Sort By Name")).perform(click());
        onView(withText("Descending")).perform(click());

	    /* then perform a click on the sort by size option and select the ascending option */
        Thread.sleep(2000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Sort By Size")).perform(click());
        onView(withText("Ascending")).perform(click());
		
	    /* perform a search on the sort by size option again and then click the descending option */
        Thread.sleep(2000);
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Sort By Size")).perform(click());
        onView(withText("Descending")).perform(click());

        /* done with all the menu clicks, now work on recyclerview */

        Thread.sleep(1000);
        //onView(withId(R.id.rclDuplicateList)).perform(RecyclerViewActions.scrollToPosition(10)).perform(longClick());

        onView(withId(R.id.rclDuplicateList)).perform(RecyclerViewActions.scrollToPosition(10)).perform(longClick());
        Thread.sleep(1000);
        /*onView(withText(R.string.deletemsg))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));*/

        Thread.sleep(2000);
    }

    /**
     * This is just a custom class to check whether two strigs match or not, this is due to the fact that we cannot directly
     * get the value typed
     * into an edittext or textview value
     */
    Matcher<View> isEditTextValueEqualTo(final String content) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Match Edit Text Value with View ID Value : :  " + content);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextView) && !(view instanceof EditText)) {
                    return false;
                }
                if (view != null) {
                    String text;
                    if (view instanceof TextView) {
                        text = ((TextView) view).getText().toString();
                    } else {
                        text = ((EditText) view).getText().toString();
                    }
                    return (text.equalsIgnoreCase(content));
                }
                return false;
            }
        };
    }
}