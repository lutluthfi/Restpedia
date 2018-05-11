package com.brawijaya.filkom.restpedia.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_NAME = "RESTPEDIA";
    private static final String PREF_IS_USER_SIGNED_IN = "PREF_IS_USER_SIGNED_IN";

    private static AppPreferencesHelper mInstance;
    private final SharedPreferences mPrefs;

    private AppPreferencesHelper(Context mContext) {
        mPrefs = mContext.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static PreferencesHelper with(Context context) {
        if (mInstance == null) mInstance = new AppPreferencesHelper(context);
        return mInstance;
    }

    @Override
    public void clear() {
        mPrefs.edit().clear().apply();
    }

    @Override
    public void setIsUserSignedIn(boolean isUserSignedIn) {
        mPrefs.edit().putBoolean(PREF_IS_USER_SIGNED_IN, isUserSignedIn).apply();
    }

    @Override
    public boolean isUserSignedIn() {
        return mPrefs.getBoolean(PREF_IS_USER_SIGNED_IN, true);
    }
}
