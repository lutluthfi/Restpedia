package com.brawijaya.filkom.restpedia.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.brawijaya.filkom.restpedia.R;
import com.brawijaya.filkom.restpedia.prefs.AppPreferencesHelper;
import com.brawijaya.filkom.restpedia.prefs.PreferencesHelper;
import com.brawijaya.filkom.restpedia.ui.base.BaseActivity;
import com.brawijaya.filkom.restpedia.ui.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends BaseActivity {

    @BindView(R.id.edittext_sign_up_name) EditText mNameEditText;
    @BindView(R.id.edittext_sign_up_address) EditText mAddressEditText;
    @BindView(R.id.edittext_sign_up_email) EditText mEmailEditText;
    @BindView(R.id.edittext_sign_up_phone) EditText mPhoneEditText;
    @BindView(R.id.edittext_sign_up_password) EditText mPasswordEditText;
    @BindView(R.id.edittext_sign_up_password_confirmation) EditText mPasswordConfirmationEditText;
    @BindView(R.id.button_sign_up) Button mSignUpButton;

    private PreferencesHelper mPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setUnbinder(ButterKnife.bind(this));
        mPrefs = AppPreferencesHelper.with(this);
        setupView();
    }

    @Override
    public void setupView() {
    }

    public void onSignUpSuccess() {
        // Set preferences for signed in
        mPrefs.setIsUserSignedIn(true);
        Log.d("SignUpActivity", "isUserSignedIn: " + mPrefs.isUserSignedIn());
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void onSignUpFailed() {
        Toast.makeText(getBaseContext(), "Sign up failed", Toast.LENGTH_LONG).show();
        mSignUpButton.setEnabled(true);
    }

    public void onSignUpClick(View view) {
        showLoading();
        mSignUpButton.setEnabled(false);
        String name = mNameEditText.getText().toString();
        String address = mAddressEditText.getText().toString();
        String email = mEmailEditText.getText().toString();
        String mobile = mPhoneEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        String passwordConfirmation = mPasswordConfirmationEditText.getText().toString();
        if (!validateSignUp(name, address, email, mobile, password, passwordConfirmation)) {
            onSignUpFailed();
            return;
        }
        getFirebase().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    hideLoading();
                })
                .addOnFailureListener(this, e -> {
                    onError(e.getMessage());
                    printLog("SignUpActivity", e.getMessage());
                })
                .addOnSuccessListener(this, authResult -> {
                    Toast.makeText(this, "Welcome: " + authResult.getUser().getEmail(), Toast.LENGTH_SHORT).show();
                    onSignUpSuccess();
                });
    }

    public boolean validateSignUp(String name, String address, String email, String mobile, String password, String passwordConfirmation) {
        if (name.isEmpty() || name.length() < 3) {
            mNameEditText.setError("At least 3 characters");
            return false;
        }
        if (address.isEmpty()) {
            mAddressEditText.setError("Enter valid address");
            return false;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailEditText.setError("Enter a valid email address");
            return false;
        }
        if (mobile.isEmpty() || mobile.length() != 10) {
            mPhoneEditText.setError("Enter valid mobile number");
            return false;
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPasswordEditText.setError("Enter between 4 and 10 alphanumeric characters");
            return false;
        }
        if (!(passwordConfirmation.equals(password))) {
            mPasswordConfirmationEditText.setError("Password doesn't match");
            return false;
        }
        return true;
    }
}
