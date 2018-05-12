package com.brawijaya.filkom.restpedia.prefs;

import com.brawijaya.filkom.restpedia.network.model.UserLocal;
import com.google.android.gms.maps.model.LatLng;

public interface PreferencesHelper {

    void clear();

    void setIsUserSignedIn(boolean isUserSignedIn);

    boolean isUserSignedIn();

    void setUserSignedIn(UserLocal userSignedIn);

    String getUserSignedIn();

    void setUserOriginLocation(LatLng origin);

    String getUserOriginLocation();
}
