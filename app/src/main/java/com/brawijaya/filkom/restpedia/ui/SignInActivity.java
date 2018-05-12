package com.brawijaya.filkom.restpedia.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brawijaya.filkom.restpedia.R;
import com.brawijaya.filkom.restpedia.network.model.UserLocal;
import com.brawijaya.filkom.restpedia.prefs.AppPreferencesHelper;
import com.brawijaya.filkom.restpedia.prefs.PreferencesHelper;
import com.brawijaya.filkom.restpedia.ui.base.BaseActivity;
import com.brawijaya.filkom.restpedia.ui.main.MainActivity;
import com.brawijaya.filkom.restpedia.utils.AppConstants;
import com.brawijaya.filkom.restpedia.utils.MapUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.brawijaya.filkom.restpedia.utils.AppConstants.PERMISSION_LOCATION_REQUEST_CODE;

public class SignInActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = SignInActivity.class.getSimpleName();

    @BindView(R.id.edittext_sign_in_email) EditText mEmailEditText;
    @BindView(R.id.edittext_sign_in_password) EditText mPasswordEditText;
    @BindView(R.id.button_sign_in) Button mSignInButton;
    @BindView(R.id.textview_sign_up) TextView mSignUpTextView;

    private PreferencesHelper mPrefs;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        setUnbinder(ButterKnife.bind(this));
        mPrefs = AppPreferencesHelper.with(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (mGoogleApiClient == null)
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        mLocationRequest = MapUtils.createLocationRequest();
        setupView();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onPause() {
        mGoogleApiClient.disconnect();
        super.onPause();
    }

    @Override
    public void setupView() {
    }

    public void onSignInSuccess(String email, String password) {
        hideLoading();
        mPrefs.setIsUserSignedIn(true);
        mPrefs.setUserSignedIn(new UserLocal(AppConstants.USER_NAME, email, AppConstants.USER_PHOTO,
                AppConstants.USER_PHONE, AppConstants.USER_ADDRESS, password));
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void onSignInFailed() {
        hideLoading();
        mSignInButton.setEnabled(true);
        Toast.makeText(this, "Sign in failed", Toast.LENGTH_LONG).show();
    }

    public void onSignInClick(View view) {
        showLoading();
        mSignInButton.setEnabled(false);
        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        if (!validateSignIn(email, password)) {
            onSignInFailed();
            return;
        }
        getFirebase().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> hideLoading())
                .addOnFailureListener(this, e -> {
                    onSignInFailed();
                    onError(e.getMessage());
                    printLog("SingInActivity", e.getMessage());
                }).addOnSuccessListener(this, authResult -> onSignInSuccess(email, password));
    }

    public void onSignUpClick(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public boolean validateSignIn(String email, String password) {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailEditText.setError("Enter a valid email address");
            return false;
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPasswordEditText.setError("Enter password between 4 and 10 alphanumeric characters");
            return false;
        }
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    printLog(TAG, "onConnected: Success");
                    if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                            mPrefs.setUserOriginLocation(new LatLng(location.getLatitude(), location.getLongitude()));
                            printLog(TAG, "onConnected: getLastConnection");
                        });
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_REQUEST_CODE);
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    break;
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        onError(connectionResult.getErrorMessage());
        printLog(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION_REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    onError("onRequestPermissionResult: Denied");
                } else {
                    if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(this, location -> {
                            mPrefs.setUserOriginLocation(new LatLng(location.getLatitude(), location.getLongitude()));
                            printLog(TAG, "onRequestPermissionsResult: createdMarker");
                        });
                    }
                }
            }
        }
    }
}
