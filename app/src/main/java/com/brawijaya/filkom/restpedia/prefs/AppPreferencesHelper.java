package com.brawijaya.filkom.restpedia.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.brawijaya.filkom.restpedia.network.model.UserLocal;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_NAME = "RESTPEDIA";
    private static final String PREF_IS_USER_SIGNED_IN = "PREF_IS_USER_SIGNED_IN";
    private static final String PREF_USER_SIGNED_IN = "PREF_USER_SIGNED_IN";
    private static final String PREF_USER_ORIGIN_LOCATION = "PREF_USER_ORIGIN_LOCATION";

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
        return mPrefs.getBoolean(PREF_IS_USER_SIGNED_IN, false);
    }

    @Override
    public void setUserSignedIn(UserLocal userSignedIn) {
        mPrefs.edit().putString(PREF_USER_SIGNED_IN, new Gson().toJson(userSignedIn)).apply();
    }

    @Override
    public String getUserSignedIn() {
        return mPrefs.getString(PREF_USER_SIGNED_IN, "");
    }

    @Override
    public void setUserOriginLocation(LatLng origin) {
        mPrefs.edit().putString(PREF_USER_ORIGIN_LOCATION, new Gson().toJson(origin)).apply();
    }

    @Override
    public String getUserOriginLocation() {
        return mPrefs.getString(PREF_USER_ORIGIN_LOCATION, "");
    }
}
