package com.brawijaya.filkom.restpedia.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.View;

import com.brawijaya.filkom.restpedia.utils.CommonUtils;

import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment implements IBaseView {

    private BaseActivity mActivity;
    private ProgressDialog mProgressDialog;
    private Unbinder mUnbinder;

    public abstract void setupView(View view);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            this.mActivity = (BaseActivity) context;
        }
    }

    @Override
    public void onDestroy() {
        if (mUnbinder != null) mUnbinder.unbind();
        super.onDestroy();
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnbinder = unBinder;
    }

    @Override
    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this.getContext());
    }

    @Override
    public boolean isLoading() {
        return mProgressDialog.isShowing();
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void printLog(String tag, String message) {
        mActivity.printLog(tag, message);
    }

    @Override
    public void printLog(String tag, int resId) {
        mActivity.printLog(tag, getString(resId));
    }

    @Override
    public void printLog(String tag, String message, Throwable tr) {
        mActivity.printLog(tag, message, tr);
    }

    @Override
    public void printLog(String tag, int resId, Throwable tr) {
        mActivity.printLog(tag, getString(resId), tr);
    }

    @Override
    public void onError(String message) {
        if (mActivity != null) mActivity.onError(message);
    }

    @Override
    public void onError(@StringRes int resId) {
        if (mActivity != null) mActivity.onError(resId);
    }

    @Override
    public void showMessage(String message) {
        if (mActivity != null) mActivity.showMessage(message);
    }

    @Override
    public void showMessage(@StringRes int resId) {
        if (mActivity != null) mActivity.showMessage(resId);
    }
}
