package com.brawijaya.filkom.restpedia.ui.main.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brawijaya.filkom.restpedia.R;
import com.brawijaya.filkom.restpedia.network.model.UserLocal;
import com.brawijaya.filkom.restpedia.prefs.AppPreferencesHelper;
import com.brawijaya.filkom.restpedia.prefs.PreferencesHelper;
import com.brawijaya.filkom.restpedia.ui.SignInActivity;
import com.brawijaya.filkom.restpedia.ui.base.BaseFragment;
import com.brawijaya.filkom.restpedia.ui.main.home.restaurant.RestaurantDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends BaseFragment{

    public static final String TAG = ProfileFragment.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.imageview_profile_photo) CircleImageView mPhotoImageView;
    @BindView(R.id.textview_profile_name) TextView mNameTextView;
    @BindView(R.id.textview_profile_email) TextView mEmailTextView;
    @BindView(R.id.textview_profile_phone) TextView mPhoneTextView;
    @BindView(R.id.container_bottom_edit_profile) CoordinatorLayout mEditProfileLayout;

    private PreferencesHelper mPrefs;
    private BottomSheetBehavior mBottomSheetBehavior;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        mPrefs = AppPreferencesHelper.with(getContext());
        return view;
    }

    @Override
    public void setupView(View view) {
        mBottomSheetBehavior = BottomSheetBehavior.from(mEditProfileLayout);
        UserLocal user = new Gson().fromJson(mPrefs.getUserSignedIn(), UserLocal.class);
        Glide.with(this).asBitmap().load(user.getPhoto()).into(mPhotoImageView);
        mNameTextView.setText(user.getName());
        mEmailTextView.setText(user.getEmail());
        mPhoneTextView.setText(user.getPhone());
    }

    @OnClick(R.id.button_profile_edit)
    public void onEditProfileClick() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            mToolbar.setTitle("Edit Profile");
        }
    }

    @OnClick(R.id.layout_profile_terms_service)
    public void onTermsServiceClick() {
        Toast.makeText(getContext(), "Terms of Service", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.layout_profile_privacy_policy)
    public void onPrivacyPolicyClick() {
        Toast.makeText(getContext(), "Privacy Policy", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_profile_sign_out)
    public void onSignOutClick() {
        mPrefs.clear();
        startActivity(new Intent(getBaseActivity(), SignInActivity.class));
        getBaseActivity().finish();
    }

    @OnClick(R.id.button_edit_profile_save)
    public void onSaveProfileClick() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}
