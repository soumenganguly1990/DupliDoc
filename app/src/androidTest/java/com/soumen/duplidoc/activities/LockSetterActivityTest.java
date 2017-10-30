package com.soumen.duplidoc.activities;

import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soumen.duplidoc.R;
import com.soumen.duplidoc.extras.AppCommonValues;
import com.soumen.duplidoc.utils.TinyDB;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import io.codetail.widget.RevealFrameLayout;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

/**
 * Created by Soumen on 24-10-2017.
 */
public class LockSetterActivityTest {

    @Rule
    public ActivityTestRule<LockSetterActivity> lockSetterActivityActivityTestRule = new ActivityTestRule<LockSetterActivity>(LockSetterActivity.class);

    /* reference */
    private LockSetterActivity lockSetterActivity = null;

    private int MAX = 8;
    private int LEFT = 8;
    private int CURRENT = 0;
    private RevealFrameLayout relLockReveal;
    private LinearLayout linLockRevealChild;
    private EditText edtPassCode;
    private TextView txtPassCodeLeft, txtOne, txtTwo, txtThree, txtFour, txtFive, txtSix;
    private TextView txtSeven, txtEight, txtNine, txtDel, txtZero, txtPassCodeOk;
    private AppCompatCheckBox chkRememberMe;

    TinyDB td = null;

    @Before
    public void setUp() throws Exception {
        lockSetterActivity = lockSetterActivityActivityTestRule.getActivity();
        td = new TinyDB(lockSetterActivity);
    }

    @Test
    public void onCreate() throws Exception {

        relLockReveal = (RevealFrameLayout) lockSetterActivity.findViewById(R.id.relLockReveal);
        linLockRevealChild = (LinearLayout) lockSetterActivity.findViewById(R.id.linLockRevealChild);
        edtPassCode = (EditText) lockSetterActivity.findViewById(R.id.edtPassCode);
        txtPassCodeLeft = (TextView) lockSetterActivity.findViewById(R.id.txtPassCodeLeft);
        txtOne = (TextView) lockSetterActivity.findViewById(R.id.txtOne);
        txtTwo = (TextView) lockSetterActivity.findViewById(R.id.txtTwo);
        txtThree = (TextView) lockSetterActivity.findViewById(R.id.txtThree);
        txtFour = (TextView) lockSetterActivity.findViewById(R.id.txtFour);
        txtFive = (TextView) lockSetterActivity.findViewById(R.id.txtFive);
        txtSix = (TextView) lockSetterActivity.findViewById(R.id.txtSix);
        txtSeven = (TextView) lockSetterActivity.findViewById(R.id.txtSeven);
        txtEight = (TextView) lockSetterActivity.findViewById(R.id.txtEight);
        txtNine = (TextView) lockSetterActivity.findViewById(R.id.txtNine);
        txtZero = (TextView) lockSetterActivity.findViewById(R.id.txtZero);
        txtDel = (TextView) lockSetterActivity.findViewById(R.id.txtDel);
        txtPassCodeOk = (TextView) lockSetterActivity.findViewById(R.id.txtPassCodeOk);
        chkRememberMe = (AppCompatCheckBox) lockSetterActivity.findViewById(R.id.chkRememberMe);

        assertNotNull(relLockReveal);
        assertNotNull(linLockRevealChild);
        assertNotNull(edtPassCode);
        assertNotNull(txtPassCodeLeft);
        assertNotNull(txtOne);
        assertNotNull(txtTwo);
        assertNotNull(txtThree);
        assertNotNull(txtFour);
        assertNotNull(txtFive);
        assertNotNull(txtSix);
        assertNotNull(txtSeven);
        assertNotNull(txtEight);
        assertNotNull(txtNine);
        assertNotNull(txtZero);
        assertNotNull(txtDel);
        assertNotNull(txtPassCodeOk);
        assertNotNull(chkRememberMe);

        edtPassCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CURRENT = edtPassCode.getText().length();
                LEFT = MAX - CURRENT;
                txtPassCodeLeft.setText(LEFT + " characters left");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        lockSetterActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                edtPassCode.setText("0");
                assertEquals(edtPassCode.getText().toString().length(), 1);
                assertEquals(MAX - edtPassCode.getText().toString().length(), LEFT);
                assertEquals(MAX - edtPassCode.getText().toString().length(), 7);

                edtPassCode.setText(edtPassCode.getText().toString() + "00");
                assertEquals(edtPassCode.getText().toString().length(), 3);
                assertEquals(MAX - edtPassCode.getText().toString().length(), LEFT);
                assertEquals(MAX - edtPassCode.getText().toString().length(), 5);

                txtDel.performClick();
                assertEquals(edtPassCode.getText().toString().length(), 2);
                assertEquals(MAX - edtPassCode.getText().toString().length(), LEFT);
                assertEquals(MAX - edtPassCode.getText().toString().length(), 6);

                txtOne.performClick();
                assertEquals(edtPassCode.getText().toString().contains("1"), true);

                txtDel.performClick();
                txtDel.performClick();
                txtDel.performClick();
                txtDel.performClick();

                assertTrue(edtPassCode.getText().toString().equals(""));

                edtPassCode.setText("");

                txtOne.performClick();
                txtTwo.performClick();
                txtThree.performClick();
                txtFour.performClick();
                txtFive.performClick();
                txtSix.performClick();
                txtSeven.performClick();
                txtEight.performClick();
                txtNine.performClick();

                assertFalse(edtPassCode.getText().toString().contains("0"));
                assertTrue(edtPassCode.getText().toString().equals("12345678"));

                edtPassCode.setText("1234");

                chkRememberMe.setChecked(true);

                txtPassCodeOk.performClick();

                assertTrue(td.getBoolean(AppCommonValues._ISPASSCODESET));
                assertEquals(td.getString(AppCommonValues._PASSWORD), edtPassCode.getText().toString());

                if(lockSetterActivity != null) {
                    onView(withText("Password saved successfully")).inRoot(withDecorView(not(is(lockSetterActivity
                            .getWindow().getDecorView())))).check(matches(isDisplayed()));
                }

                if (chkRememberMe.isChecked()) {
                    assertEquals(td.getInt(AppCommonValues._REMEMBER_PWD_TAG), AppCommonValues._DOREMEMBER);
                    assertNotEquals(td.getInt(AppCommonValues._REMEMBER_PWD_TAG), AppCommonValues._DONTREMEMBER);
                } else {
                    assertEquals(td.getInt(AppCommonValues._REMEMBER_PWD_TAG), AppCommonValues._DONTREMEMBER);
                }
            }
        });
    }

    @Test
    public void testMultipleTouchonTwoViewsAtSameTime() {
        final CyclicBarrier gate = new CyclicBarrier(4);
        edtPassCode.setText("1234");
        Thread t1 = new Thread() {
            @Override
            public void run() {
                try {
                    gate.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                txtDel.performClick();
            }
        };
        Thread t2 = new Thread() {
            @Override
            public void run() {
                try {
                    gate.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                txtPassCodeOk.performClick();
            }
        };
        t1.start();
        t2.start();
        try {
            gate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void onClick() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        lockSetterActivity = null;
    }
}