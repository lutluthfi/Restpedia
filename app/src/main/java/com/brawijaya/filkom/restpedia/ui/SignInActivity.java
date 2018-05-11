package com.brawijaya.filkom.restpedia.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brawijaya.filkom.restpedia.R;
import com.brawijaya.filkom.restpedia.ui.base.BaseActivity;
import com.brawijaya.filkom.restpedia.ui.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends BaseActivity {

    @BindView(R.id.edittext_sign_in_email) EditText mEmailEditText;
    @BindView(R.id.edittext_sign_in_password) EditText mPasswordEditText;
    @BindView(R.id.button_sign_in) Button mSignInButton;
    @BindView(R.id.textview_sign_up) TextView mSignUpTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        setUnbinder(ButterKnife.bind(this));
        setupView();
    }

    @Override
    public void setupView() {
    }

    public void onSignInSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void onSignInFailed() {
        Toast.makeText(getBaseContext(), "Sign in failed", Toast.LENGTH_LONG).show();
        mSignInButton.setEnabled(true);
        hideLoading();
    }

    public void onSignInClick(View view) {
        showLoading();
        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        if (!validateSignIn(email, password)) {
            onSignInFailed();
            return;
        }
        mSignInButton.setEnabled(false);
        getFirebase().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> hideLoading())
                .addOnFailureListener(this, e -> {
                    onError(e.getMessage());
                    printLog("SingInActivity", e.getMessage());
                }).addOnSuccessListener(this, authResult -> {
                    // TODO : set preference user
                    onSignInSuccess();
                });
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
        return email.equalsIgnoreCase("admin@example.com") && password.equalsIgnoreCase("secret");
    }
}
