package com.brawijaya.filkom.restpedia.prefs;

import com.brawijaya.filkom.restpedia.network.model.UserLocal;

public interface PreferencesHelper {

    void clear();

    void setIsUserSignedIn(boolean isUserSignedIn);

    boolean isUserSignedIn();
}
