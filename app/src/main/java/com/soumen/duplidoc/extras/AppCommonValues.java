package com.soumen.duplidoc.extras;

/**
 * Created by Soumen on 10/16/2017.
 */

public class AppCommonValues {

    public static final String HELVETICAROUNDEDBOLD = "fonts/hrb.otf";

    /* --- TinyDB tags here --- */
    public static final String _ISFIRSTRUNTAG = "isFirstRun";
    public static final String _ISPASSCODESET = "isPassCodeSet";
    public static final String _PASSWORD = "password";
    public static final String _REMEMBER_PWD_TAG = "rememberme";
    public static final int _DOREMEMBER = 11;
    public static final int _DONTREMEMBER = 22;

    /* constraints */
    public static final int _MINPASSCHARACTER = 4;

    /* page tag, used for redirection */
    public static final int _INTROPAGE = 1;
    public static final int _LOCKPAGE = 2;
    public static final int _MAINPAGE = 3;

    /* delay values */
    public static final int _REDIRECTDELAY = 400;
    public static final int _VIBRATIONTIME = 60;
    public static final int _DRAWERDELAY = 250;

    /* filetype for which user asked to check duplicate */
    public static String FILETAG = "filetype";
}