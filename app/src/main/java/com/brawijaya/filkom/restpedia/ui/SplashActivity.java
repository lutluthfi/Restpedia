package com.brawijaya.filkom.restpedia.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.widget.ImageView;

import com.brawijaya.filkom.restpedia.R;
import com.brawijaya.filkom.restpedia.prefs.AppPreferencesHelper;
import com.brawijaya.filkom.restpedia.prefs.PreferencesHelper;
import com.brawijaya.filkom.restpedia.ui.base.BaseActivity;
import com.brawijaya.filkom.restpedia.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    private PreferencesHelper mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setUnbinder(ButterKnife.bind(this));
        mPrefs = AppPreferencesHelper.with(this);
        setupView();
    }

    @Override
    public void setupView() {
        new Handler().postDelayed(() -> {
            if (!mPrefs.isUserSignedIn()) {
                startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                finish();
            } else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 1500);
    }
}
