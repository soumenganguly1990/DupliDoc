package com.soumen.duplidoc.activities;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
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
        final LinearLayout linAudio = (LinearLayout) mainActivity.findViewById(R.id.linAudio);
        final LinearLayout linVideo = (LinearLayout) mainActivity.findViewById(R.id.linVideos);
        final LinearLayout linTexts = (LinearLayout) mainActivity.findViewById(R.id.linTexts);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                linImages.performClick();
                //linAudio.performClick();
                //linVideo.performClick();
                //linTexts.performClick();
            }
        });
    }

    @Test
    private void testNumberOfChildViewsForEachParentTypeLayout() {
        final DrawerLayout dupliDrawer = (DrawerLayout) mainActivity.findViewById(R.id.dupliDrawer);
        final LinearLayout mainLinHolder = (LinearLayout) mainActivity.findViewById(R.id.linCircleBg);
        final LinearLayout linMainRevealChild = (LinearLayout) mainActivity.findViewById(R.id.linMainRevealChild);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int drawerCount = dupliDrawer.getChildCount();
                assertFalse(drawerCount >= 3);
                assertFalse(drawerCount <= 1);
                assertEquals(drawerCount, 2);
            }
        });
    }
}