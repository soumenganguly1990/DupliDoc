package com.soumen.duplidoc.activities;

import android.os.Handler;
import android.os.Looper;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import com.soumen.duplidoc.R;
import com.soumen.duplidoc.extras.AppCommonValues;
import com.soumen.duplidoc.utils.TinyDB;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Soumen on 23-10-2017.
 */
public class SplashActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> splashActivityActivityTestRule = new ActivityTestRule<SplashActivity>(SplashActivity.class);

    /* references */
    private SplashActivity splashActivity = null;
    private TinyDB td = null;

    @Before
    public void setUp() throws Exception {
        splashActivity = splashActivityActivityTestRule.getActivity();
        td = new TinyDB(splashActivity);
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
    }

    @Test
    public void testReadWhetherFirstTimeOrNotWithFirstTrue() {
        td.putBoolean(AppCommonValues._ISFIRSTRUNTAG, true);
        runFirstReadOrNotFunction();
    }

    @Test
    public void testReadWhetherFirstTimeOrNotWithFirstFalse() {
        td.putBoolean(AppCommonValues._ISFIRSTRUNTAG, false);
        runFirstReadOrNotFunction();
    }

    @Test
    public void testReadIsPassCodeSetTrue() {
        // In this case will be redirected to main activity //
        td.putBoolean(AppCommonValues._ISPASSCODESET, true);
        runFirstReadOrNotFunction();
    }

    @Test
    public void testReadIsPassCodeSetFalse() {
        // in this case will be redirected to passcode setter page //
        td.putBoolean(AppCommonValues._ISPASSCODESET, false);
        runFirstReadOrNotFunction();
    }

    @Test
    public void didUserToldToRememberTrue() {
        td.putInt(AppCommonValues._REMEMBER_PWD_TAG, AppCommonValues._DOREMEMBER);
        runFirstReadOrNotFunction();
    }

    @Test
    public void didUserToldToRememberFalse() {
        td.putInt(AppCommonValues._REMEMBER_PWD_TAG, AppCommonValues._DONTREMEMBER);
        runFirstReadOrNotFunction();
    }

    @Test
    public void routineWhenFirstTimePasscodeNotSet() {
        td.putBoolean(AppCommonValues._ISFIRSTRUNTAG, true);
        td.putBoolean(AppCommonValues._ISPASSCODESET, false);
        runFirstReadOrNotFunction();
    }

    @Test
    public void routineWhenFirstTimePassCodeSet() {
        td.putBoolean(AppCommonValues._ISFIRSTRUNTAG, true);
        td.putBoolean(AppCommonValues._ISPASSCODESET, true);
        runFirstReadOrNotFunction();
    }

    @Test
    public void routineWhenFirstTimePassCodeIsSetAndRemembered() {
        td.putBoolean(AppCommonValues._ISFIRSTRUNTAG, true);
        td.putBoolean(AppCommonValues._ISPASSCODESET, true);
        td.putInt(AppCommonValues._REMEMBER_PWD_TAG, AppCommonValues._DOREMEMBER);
        runFirstReadOrNotFunction();
    }

    @Test
    public void routineWhenFirstTimePassCodeIsSetAndNotRemembered() {
        td.putBoolean(AppCommonValues._ISFIRSTRUNTAG, true);
        td.putBoolean(AppCommonValues._ISPASSCODESET, true);
        td.putInt(AppCommonValues._REMEMBER_PWD_TAG, AppCommonValues._DONTREMEMBER);
        runFirstReadOrNotFunction();
    }

    private void runFirstReadOrNotFunction() {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                splashActivity = splashActivityActivityTestRule.getActivity();
                splashActivity.readWhetherFirstTimeOrNot();
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        splashActivity = null;
        System.out.println("tearDown() called");
    }
}