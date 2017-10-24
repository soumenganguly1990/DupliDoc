package com.soumen.duplidoc.activities;

import android.support.test.rule.ActivityTestRule;
import android.view.View;
import com.soumen.duplidoc.R;
import com.soumen.duplidoc.extras.AppCommonValues;
import com.soumen.duplidoc.utils.TinyDB;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Created by Soumen on 23-10-2017.
 */
public class SplashActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> splashActivityActivityTestRule = new ActivityTestRule<SplashActivity>(SplashActivity.class);

    /* references */
    private SplashActivity splashActivity = null;

    @Before
    public void setUp() throws Exception {
        splashActivity = splashActivityActivityTestRule.getActivity();
        System.out.println("setUp() called");
    }

    @Test
    public void testLaunch() {
        final View txtSplashBanner = splashActivity.findViewById(R.id.txtSplashBanner);
        final View txtEnjoyLifeMore = splashActivity.findViewById(R.id.txtEnjoyLifeMore);

        assertNotNull(txtSplashBanner);
        assertNotNull(txtEnjoyLifeMore);

        assertEquals(txtSplashBanner.getVisibility(), View.VISIBLE);
        assertEquals(txtSplashBanner.getAnimation().getDuration(), 1000);

        try {Thread.sleep(1000);} catch (Exception e) {}

        assertEquals(txtEnjoyLifeMore.getVisibility(), View.VISIBLE);
        assertEquals(txtEnjoyLifeMore.getAnimation().getDuration(), 350);

        TinyDB td = new TinyDB(splashActivity);
        assertNotNull(td);
        assertTrue(!td.getBoolean(AppCommonValues._ISFIRSTRUNTAG));
    }

    @After
    public void tearDown() throws Exception {
        splashActivity = null;
        System.out.println("tearDown() called");
    }
}