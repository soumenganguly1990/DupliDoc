package com.soumen.duplidoc.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.soumen.duplidoc.R;
import com.soumen.duplidoc.enums.FileType;
import com.soumen.duplidoc.extras.AppCommonValues;
import com.soumen.duplidoc.utils.TinyDB;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealLinearLayout;

/**
 * Created by Soumen on 10/17/2017.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout dupliDrawer;
    private NavigationView navMenu;
    private Toolbar dupliToolbar;
    private RevealLinearLayout revMainViewHolder;
    private LinearLayout linMainRevealChild;
    private LinearLayout linTexts, linImages, linAudio, linVideo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getUiComponents();
        setSupportActionBar(dupliToolbar);
        circularRevealEverything(linMainRevealChild, 1000);
        setUpDupliDrawer();
        setUpNavigationController();
        setUpListener();
    }

    private void getUiComponents() {
        revMainViewHolder = (RevealLinearLayout) findViewById(R.id.revMainViewHolder);
        linMainRevealChild = (LinearLayout) findViewById(R.id.linMainRevealChild);
        dupliToolbar = (Toolbar) findViewById(R.id.dupliToolbar);
        dupliDrawer = (DrawerLayout) findViewById(R.id.dupliDrawer);
        navMenu = (NavigationView) findViewById(R.id.navMenu);
        linTexts = (LinearLayout) findViewById(R.id.linTexts);
        linImages = (LinearLayout) findViewById(R.id.linImages);
        linAudio = (LinearLayout) findViewById(R.id.linAudio);
        linVideo = (LinearLayout) findViewById(R.id.linVideos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuDontRememberMeAnymore:
                workOnErasingRememberMeDetails();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void circularRevealEverything(final View view, final int duration) {
        view.post(new Runnable() {
            @Override
            public void run() {
                int cx = (view.getLeft());
                int cy = (view.getBottom());
                int dx = Math.max(cx, view.getWidth());
                int dy = Math.max(cy, view.getHeight());
                float finalRadius = (float) Math.hypot(dx, dy);
                SupportAnimator animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(duration);
                animator.start();
            }
        });
    }

    /**
     * @task sets up the drawerlayout for the activity, crucial to display the hamburger
     */
    private void setUpDupliDrawer() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, dupliDrawer,
                dupliToolbar, R.string.opendrawer, R.string.closedrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        dupliDrawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    /**
     * @task handles the clicks on different nav menu items by id
     */
    private void setUpNavigationController() {
        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.mnuDupli:
                        closeDrawerAndProceed(R.id.mnuDupli);
                        break;
                    case R.id.mnuMe:
                        closeDrawerAndProceed(R.id.mnuMe);
                        break;
                    case R.id.navAbtCapgemini:
                        closeDrawerAndProceed(R.id.navAbtCapgemini);
                        break;
                }
                return false;
            }
        });
    }

    private void setUpListener() {
        linTexts.setOnClickListener(this);
        linImages.setOnClickListener(this);
        linAudio.setOnClickListener(this);
        linVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == linTexts) {
            startNextActivity(FileType.TEXT);
        }
        if(v == linImages) {
            startNextActivity(FileType.IMAGE);
        }
        if(v == linAudio) {
            startNextActivity(FileType.AUDIO);
        }
        if(v == linVideo) {
            startNextActivity(FileType.VIDEO);
        }
    }

    /**
     * Starts a link with apps taking action_view as their intent filter
     * @param link
     */
    private void startWebLinks(String link) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(link));
        startActivity(i);
    }

    /**
     * Closes the drawer and do whatever is needed thereafter
     * @param menuId
     */
    private void closeDrawerAndProceed(int menuId) {
        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dupliDrawer.closeDrawers();
                mHandler.removeCallbacks(this);
            }
        }, AppCommonValues._DRAWERDELAY);
        switch (menuId) {
            case R.id.mnuDupli:
                showAboutDialog();
                break;
            case R.id.mnuMe:
                startWebLinks("https://www.linkedin.com/in/soumenganguly1990");
                break;
            case R.id.navAbtCapgemini:
                startWebLinks("https://www.capgemini.com/in-en/");
                break;
        }
    }

    /**
     * Starts next activity
     * @param _whichKey
     */
    private void startNextActivity(FileType _whichKey) {
        Intent i = new Intent(this, FileListDetailsActivity.class);
        switch (_whichKey) {
            case TEXT:
                i.putExtra(AppCommonValues.FILETAG, FileType.TEXT);
                break;
            case IMAGE:
                i.putExtra(AppCommonValues.FILETAG, FileType.IMAGE);
                break;
            case AUDIO:
                i.putExtra(AppCommonValues.FILETAG, FileType.AUDIO);
                break;
            case VIDEO:
                i.putExtra(AppCommonValues.FILETAG, FileType.VIDEO);
                break;
        }
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    /**
     * Shows a custom dialog with some info on it
     */
    private void showAboutDialog() {
        final Dialog aboutDialog = new Dialog(MainActivity.this);
        aboutDialog.setContentView(R.layout.aboutdialog);
        AppCompatButton btnAboutOk = (AppCompatButton) aboutDialog.findViewById(R.id.btnAboutOk);
        aboutDialog.setCancelable(true);
        aboutDialog.setCanceledOnTouchOutside(true);
        btnAboutOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutDialog.dismiss();
            }
        });
        aboutDialog.show();
    }

    /**
     * Falsify the password remember tag
     */
    private void workOnErasingRememberMeDetails() {
        TinyDB td = new TinyDB(MainActivity.this);
        td.putInt(AppCommonValues._REMEMBER_PWD_TAG, AppCommonValues._DONTREMEMBER);
        Toast.makeText(MainActivity.this, "You have been forgotten", Toast.LENGTH_SHORT).show();
    }
}