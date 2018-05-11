package com.brawijaya.filkom.restpedia.prefs;

public interface PreferencesHelper {

    void clear();

    void setIsUserSignedIn(boolean isUserSignedIn);

    boolean isUserSignedIn();
}
