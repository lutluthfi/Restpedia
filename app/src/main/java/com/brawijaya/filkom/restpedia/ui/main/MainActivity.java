package com.brawijaya.filkom.restpedia.ui.main;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.brawijaya.filkom.restpedia.R;
import com.brawijaya.filkom.restpedia.prefs.AppPreferencesHelper;
import com.brawijaya.filkom.restpedia.prefs.PreferencesHelper;
import com.brawijaya.filkom.restpedia.ui.base.BaseActivity;
import com.brawijaya.filkom.restpedia.ui.main.home.HomeFragment;
import com.brawijaya.filkom.restpedia.ui.main.notification.NotificationFragment;
import com.brawijaya.filkom.restpedia.ui.main.profile.ProfileFragment;
import com.brawijaya.filkom.restpedia.utils.MapUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.brawijaya.filkom.restpedia.utils.AppConstants.PERMISSION_LOCATION_REQUEST_CODE;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    public static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.navigation) BottomNavigationView mNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUnbinder(ButterKnife.bind(this));
        setSupportActionBar(mToolbar);
        setupView();
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        if (toolbar != null) toolbar.setContentInsetStartWithNavigation(0);
    }
    @Override
    public void setupView() {
        mNavigation.setOnNavigationItemSelectedListener(this);
        changeFragment(HomeFragment.newInstance(), HomeFragment.TAG);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                changeFragment(HomeFragment.newInstance(), HomeFragment.TAG);
                return true;
            case R.id.navigation_notifications:
                changeFragment(NotificationFragment.newInstance(), NotificationFragment.TAG);
                return true;
            case R.id.navigation_account:
                changeFragment(ProfileFragment.newInstance(), ProfileFragment.TAG);
                return true;
        }
        return false;
    }

    private void changeFragment(Fragment fragment, String tag) {
        if (fragment != null ) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment, tag).commit();
        } else onError(R.string.error_general);
    }
}
