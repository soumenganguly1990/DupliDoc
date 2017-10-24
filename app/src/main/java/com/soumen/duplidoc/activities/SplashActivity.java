package com.soumen.duplidoc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.soumen.duplidoc.R;
import com.soumen.duplidoc.extras.AppCommonValues;
import com.soumen.duplidoc.utils.HelveticaBoldTextView;
import com.soumen.duplidoc.utils.TinyDB;

/**
 * Created by Soumen on 10/16/2017.
 */

public class SplashActivity extends AppCompatActivity {

    HelveticaBoldTextView txtSplashBanner;
    TextView txtEnjoyLifeMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        txtSplashBanner = (HelveticaBoldTextView) findViewById(R.id.txtSplashBanner);
        txtEnjoyLifeMore = (TextView) findViewById(R.id.txtEnjoyLifeMore);
        playAnimationOnView(txtSplashBanner, R.anim.rotation);
    }

    /**
     * Play certain animations on certain views, of course the order and the views
     * are predefined by me
     * @param view
     * @param whichAnimation
     */
    private void playAnimationOnView(final View view, int whichAnimation) {
        Animation animation = AnimationUtils.loadAnimation(this, whichAnimation);
        view.setVisibility(View.VISIBLE);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if(view == txtSplashBanner) {
                    playAnimationOnView(txtEnjoyLifeMore, R.anim.fromleft);
                } else {
                    readWhetherFirstTimeOrNot();
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * Check if the app is being opened for the first time or not
     */
    public void readWhetherFirstTimeOrNot() {
        TinyDB tinyDB = new TinyDB(SplashActivity.this);
        if(!tinyDB.getBoolean(AppCommonValues._ISFIRSTRUNTAG)) {
            /* this is the first run, so redirect to intro page */
            tinyDB.putBoolean(AppCommonValues._ISFIRSTRUNTAG, true);
            reDirectToNextPageAferADelay(AppCommonValues._INTROPAGE);
        } else {
            /* this is not the first time, check if password is already set */
            if(!tinyDB.getBoolean(AppCommonValues._ISPASSCODESET)) {
                Toast.makeText(SplashActivity.this, "Please Set A Pass Code", Toast.LENGTH_SHORT);
                reDirectToNextPageAferADelay(AppCommonValues._LOCKPAGE);
            } else {
                /* check if suer did a remember me */
                if(didUserToldToRemember()) {
                    reDirectToNextPageAferADelay(AppCommonValues._MAINPAGE);
                } else {
                    reDirectToNextPageAferADelay(AppCommonValues._LOCKPAGE);
                }
            }
        }
    }

    /**
     * Check whether the user wanted to be remembered in the last password attempt
     * @return
     */
    private boolean didUserToldToRemember() {
        TinyDB td = new TinyDB(this);
        if(td.getInt(AppCommonValues._REMEMBER_PWD_TAG) == AppCommonValues._DOREMEMBER) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Redirects user to the next page/ screen
     * @param whichPage
     */
    private void reDirectToNextPageAferADelay(final int whichPage) {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (whichPage) {
                    case AppCommonValues._INTROPAGE:
                        startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                        break;
                    case AppCommonValues._LOCKPAGE:
                        startActivity(new Intent(SplashActivity.this, LockSetterActivity.class));
                        break;
                    case AppCommonValues._MAINPAGE:
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        break;
                }
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                SplashActivity.this.finish();
            }
        }, AppCommonValues._REDIRECTDELAY);
    }
}