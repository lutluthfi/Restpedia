package com.brawijaya.filkom.restpedia.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.widget.ImageView;

import com.brawijaya.filkom.restpedia.R;
import com.brawijaya.filkom.restpedia.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setUnbinder(ButterKnife.bind(this));
        setupView();
    }

    @Override
    public void setupView() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, SignInActivity.class));
            finish();
        }, 1500);
    }
}
