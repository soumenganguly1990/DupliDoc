package com.soumen.duplidoc.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soumen.duplidoc.R;
import com.soumen.duplidoc.extras.AppCommonValues;
import com.soumen.duplidoc.utils.TinyDB;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealFrameLayout;

/**
 * Created by Soumen on 10/16/2017.
 */

public class LockSetterActivity extends AppCompatActivity implements View.OnClickListener {

    private int MAX = 8;
    private int LEFT = 8;
    private int CURRENT = 0;
    private Vibrator vibrator;
    private RevealFrameLayout relLockReveal;
    private LinearLayout linLockRevealChild;
    private EditText edtPassCode;
    private TextView txtPassCodeLeft, txtOne, txtTwo, txtThree, txtFour, txtFive, txtSix;
    private TextView txtSeven, txtEight, txtNine, txtDel, txtZero, txtPassCodeOk;
    private AppCompatCheckBox chkRememberMe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lock_setter);

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        relLockReveal = (RevealFrameLayout) findViewById(R.id.relLockReveal);
        linLockRevealChild = (LinearLayout) findViewById(R.id.linLockRevealChild);
        edtPassCode = (EditText) findViewById(R.id.edtPassCode);
        txtPassCodeLeft = (TextView) findViewById(R.id.txtPassCodeLeft);
        txtOne = (TextView) findViewById(R.id.txtOne);
        txtTwo = (TextView) findViewById(R.id.txtTwo);
        txtThree = (TextView) findViewById(R.id.txtThree);
        txtFour = (TextView) findViewById(R.id.txtFour);
        txtFive = (TextView) findViewById(R.id.txtFive);
        txtSix = (TextView) findViewById(R.id.txtSix);
        txtSeven = (TextView) findViewById(R.id.txtSeven);
        txtEight = (TextView) findViewById(R.id.txtEight);
        txtNine = (TextView) findViewById(R.id.txtNine);
        txtZero = (TextView) findViewById(R.id.txtZero);
        txtDel = (TextView) findViewById(R.id.txtDel);
        txtPassCodeOk = (TextView) findViewById(R.id.txtPassCodeOk);
        chkRememberMe = (AppCompatCheckBox) findViewById(R.id.chkRememberMe);

        playCircularRevealAnimation();
        disableSoftInputFromAppearing();

        txtOne.setOnClickListener(this);
        txtTwo.setOnClickListener(this);
        txtThree.setOnClickListener(this);
        txtFour.setOnClickListener(this);
        txtFive.setOnClickListener(this);
        txtSix.setOnClickListener(this);
        txtSeven.setOnClickListener(this);
        txtEight.setOnClickListener(this);
        txtNine.setOnClickListener(this);
        txtZero.setOnClickListener(this);
        txtDel.setOnClickListener(this);
        txtPassCodeOk.setOnClickListener(this);

        edtPassCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CURRENT = edtPassCode.getText().length();
                LEFT = MAX - CURRENT;
                txtPassCodeLeft.setText(LEFT + " characters left");
                if(CURRENT >= AppCommonValues._MINPASSCHARACTER) {
                    txtPassCodeOk.setTextColor(getResources().getColor(android.R.color.white));
                } else {
                    txtPassCodeOk.setTextColor(Color.parseColor("#77FFFFFF"));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    /**
     * Disable soft keyboard from appearing, use in conjunction with android:windowSoftInputMode="stateAlwaysHidden|adjustNothing"
     */
    private void disableSoftInputFromAppearing() {
        edtPassCode.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= 11) {
                    edtPassCode.setRawInputType(InputType.TYPE_CLASS_TEXT);
                    edtPassCode.setTextIsSelectable(true);
                } else {
                    edtPassCode.setRawInputType(InputType.TYPE_NULL);
                    edtPassCode.setFocusable(true);
                }
            }
        });
    }

    private void playCircularRevealAnimation() {
        relLockReveal.post(new Runnable() {
            @Override
            public void run() {
                int cx = (linLockRevealChild.getLeft() + relLockReveal.getLeft()) / 2;
                int cy = (linLockRevealChild.getTop() + relLockReveal.getBottom());
                int dx = Math.max(cx, linLockRevealChild.getWidth() - cx);
                int dy = Math.max(cy, linLockRevealChild.getHeight() - cy);
                float finalRadius = (float) Math.hypot(dx, dy);
                SupportAnimator animator = ViewAnimationUtils.createCircularReveal(linLockRevealChild, cx, cy, 0, finalRadius);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(1500);
                animator.start();
            }
        });
    }

    private void vibrateALittle() {
        vibrator.vibrate(AppCommonValues._VIBRATIONTIME);
    }

    private void checkAndTakeActionOnPasswordChars() {
        TinyDB tinyDB = new TinyDB(LockSetterActivity.this);
        if(!tinyDB.getBoolean(AppCommonValues._ISPASSCODESET)) {
            tinyDB.putString(AppCommonValues._PASSWORD, edtPassCode.getText().toString().trim());
            tinyDB.putBoolean(AppCommonValues._ISPASSCODESET, true);
            vibrateALittle();
            Toast.makeText(LockSetterActivity.this, "Password saved successfully", Toast.LENGTH_SHORT).show();
            saveMyPassword();
            startNextActivity(AppCommonValues._MAINPAGE);
        } else {
            String _oldPassword = tinyDB.getString(AppCommonValues._PASSWORD);
            if(_oldPassword.equalsIgnoreCase(edtPassCode.getText().toString().trim())) {
                saveMyPassword();
                startNextActivity(AppCommonValues._MAINPAGE);
            } else {
                Toast.makeText(LockSetterActivity.this, "Wrong Password !!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveMyPassword() {
        if(chkRememberMe.isChecked()) {
            TinyDB td = new TinyDB(this);
            td.putInt(AppCommonValues._REMEMBER_PWD_TAG, AppCommonValues._DOREMEMBER);
        }
    }

    private void startNextActivity(int whichActivity) {
        if(whichActivity == AppCommonValues._MAINPAGE)
            startActivity(new Intent(LockSetterActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        LockSetterActivity.this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtOne:
                vibrateALittle();
                edtPassCode.setText(edtPassCode.getText().toString().concat("1"));
                break;
            case R.id.txtTwo:
                vibrateALittle();
                edtPassCode.setText(edtPassCode.getText().toString().concat("2"));
                break;
            case R.id.txtThree:
                vibrateALittle();
                edtPassCode.setText(edtPassCode.getText().toString().concat("3"));
                break;
            case R.id.txtFour:
                vibrateALittle();
                edtPassCode.setText(edtPassCode.getText().toString().concat("4"));
                break;
            case R.id.txtFive:
                vibrateALittle();
                edtPassCode.setText(edtPassCode.getText().toString().concat("5"));
                break;
            case R.id.txtSix:
                vibrateALittle();
                edtPassCode.setText(edtPassCode.getText().toString().concat("6"));
                break;
            case R.id.txtSeven:
                vibrateALittle();
                edtPassCode.setText(edtPassCode.getText().toString().concat("7"));
                break;
            case R.id.txtEight:
                vibrateALittle();
                edtPassCode.setText(edtPassCode.getText().toString().concat("8"));
                break;
            case R.id.txtNine:
                vibrateALittle();
                edtPassCode.setText(edtPassCode.getText().toString().concat("9"));
                break;
            case R.id.txtZero:
                vibrateALittle();
                edtPassCode.setText(edtPassCode.getText().toString().concat("0"));
                break;
            case R.id.txtDel:
                try {
                    vibrateALittle();
                    String str = edtPassCode.getText().toString();
                    str = str.substring(0, str.length() - 1);
                    edtPassCode.setText("");
                    edtPassCode.setText(str);
                } catch (Exception e) {
                    Toast.makeText(LockSetterActivity.this, "Nothing to delete", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txtPassCodeOk:
                vibrateALittle();
                /* check if already saved, then it's open attempt
                /* else it's a save attempt */
                if(edtPassCode.getText().toString().trim().length() < AppCommonValues._MINPASSCHARACTER) {
                    Toast.makeText(LockSetterActivity.this, "Passcode requires minimum 4 characters", Toast.LENGTH_SHORT).show();
                } else {
                    checkAndTakeActionOnPasswordChars();
                }
                break;
        }
    }
}
