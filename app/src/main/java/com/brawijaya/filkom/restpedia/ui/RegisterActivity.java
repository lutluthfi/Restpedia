package com.brawijaya.filkom.restpedia.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brawijaya.filkom.restpedia.R;
import com.brawijaya.filkom.restpedia.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.input_name) EditText mName;
    @BindView(R.id.input_address) EditText mAddress;
    @BindView(R.id.input_email) EditText mEmail;
    @BindView(R.id.input_mobile) EditText mMobile;
    @BindView(R.id.input_password) EditText mPassword;
    @BindView(R.id.input_reEnterPassword) EditText mPasswordConfirmation;
    @BindView(R.id.btn_register) Button mRegister;
    @BindView(R.id.link_login) TextView mLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setUnbinder(ButterKnife.bind(this));
        mRegister.setOnClickListener(this);
        mLogin.setOnClickListener(this);
    }

    public void signup() {

        String name = mName.getText().toString();
        String address = mAddress.getText().toString();
        String email = mEmail.getText().toString();
        String mobile = mMobile.getText().toString();
        String password = mPassword.getText().toString();
        String passwordConfirmation = mPasswordConfirmation.getText().toString();

        if (!validate(name, address, email, mobile, password, passwordConfirmation)) {
            onSignupFailed();
            return;
        }

        mRegister.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        // TODO: Implement your own signup logic here.

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onSignupSuccess();
                progressDialog.dismiss();
            }
        }, 3000);
    }


    public void onSignupSuccess() {
        mRegister.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        mRegister.setEnabled(true);
    }

    public boolean validate(String name, String address, String email, String mobile, String password, String passwordConfirmation) {
        boolean valid = true;

        if (name.isEmpty() || name.length() < 3) {
            mName.setError("at least 3 characters");
            valid = false;
        } else {
            mName.setError(null);
        }

        if (address.isEmpty()) {
            mAddress.setError("enter valid address");
            valid = false;
        } else {
            mAddress.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("enter a valid email address");
            valid = false;
        } else {
            mEmail.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            mMobile.setError("enter valid mobile number");
            valid = false;
        } else {
            mMobile.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        if (!(passwordConfirmation.equals(password))) {
            mPasswordConfirmation.setError("password doesn't match");
            valid = false;
        } else {
            mPasswordConfirmation.setError(null);
        }

        return valid;
    }

    @Override
    public void setupView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                signup();
                break;
            case R.id.link_login:
                Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            default:
                break;
        }
    }
}
