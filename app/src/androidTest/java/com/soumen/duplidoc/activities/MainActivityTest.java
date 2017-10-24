package com.soumen.duplidoc.activities;

import android.support.test.rule.ActivityTestRule;
import android.widget.LinearLayout;

import com.soumen.duplidoc.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Soumen on 24-10-2017.
 */
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class, false, true);

    private MainActivity mainActivity = null;

    @Before
    public void setUp() throws Exception {
        mainActivity = mainActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }

    @Test
    public void onCreate() throws Exception {
        final LinearLayout linImages = (LinearLayout) mainActivity.findViewById(R.id.linImages);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                linImages.performClick();
            }
        });
    }

    @Test
    public void onClick() throws Exception {
    }
}