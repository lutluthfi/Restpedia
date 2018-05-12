package com.brawijaya.filkom.restpedia.ui.main;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.brawijaya.filkom.restpedia.R;
import com.brawijaya.filkom.restpedia.ui.base.BaseActivity;
import com.brawijaya.filkom.restpedia.ui.main.home.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

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
                showMessage("Notifications");
                return true;
            case R.id.navigation_account:
                showMessage("Account");
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
