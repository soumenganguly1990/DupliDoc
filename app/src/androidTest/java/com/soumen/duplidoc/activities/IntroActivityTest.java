package com.soumen.duplidoc.activities;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.soumen.duplidoc.R;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Soumen on 24-10-2017.
 */
public class IntroActivityTest {

    @Rule
    public ActivityTestRule<IntroActivity> introActivityActivityTestRule = new ActivityTestRule<IntroActivity>(IntroActivity.class);

    /* reference */
    private IntroActivity introActivity = null;

    private View btnSkip = null;
    private View btnNext = null;

    @Before
    public void setUp() throws Exception {
        introActivity = introActivityActivityTestRule.getActivity();
    }

    @Test
    public void onCreate() throws Exception {
        final View viewPager = introActivity.findViewById(R.id.view_pager);
        View dotsLayout = introActivity.findViewById(R.id.layoutDots);
        btnSkip = introActivity.findViewById(R.id.btn_skip);
        btnNext = introActivity.findViewById(R.id.btn_next);

        assertNotNull(viewPager);
        assertNotNull(dotsLayout);
        assertNotNull(btnSkip);
        assertNotNull(btnNext);

        assertArrayEquals("expected and actual arrays", new int[] {R.layout.fragment_intro_1,
                        R.layout.fragment_intro_2, R.layout.fragment_intro_3}, introActivity.layouts);

        assertNotNull(introActivity.myViewPagerAdapter);

        introActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                introActivity = introActivityActivityTestRule.getActivity();
                ((ViewPager)viewPager).setAdapter(introActivity.myViewPagerAdapter);
            }
        });
    }

    @Test
    public void performThreeNextClicks() {
        introActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                introActivity = introActivityActivityTestRule.getActivity();
                btnNext.performClick();
                btnNext.performClick();
                btnNext.performClick();
            }
        });
    }

    @Test
    public void performSkipButtonClick() {
        introActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                introActivity = introActivityActivityTestRule.getActivity();
                btnSkip.performClick();
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        introActivity = null;
    }
}