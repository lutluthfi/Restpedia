package com.brawijaya.filkom.restpedia.ui.main.notification;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brawijaya.filkom.restpedia.R;
import com.brawijaya.filkom.restpedia.ui.adapters.NotificationAdapter;
import com.brawijaya.filkom.restpedia.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationFragment extends BaseFragment {

    public static final String TAG = NotificationFragment.class.getSimpleName();

    @BindView(R.id.recyclerview_notification) RecyclerView mRecyclerView;

    private NotificationAdapter mAdapter;

    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        return view;
    }

    @Override
    public void setupView(View view) {
        mAdapter = new NotificationAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.add(getNotifications());
    }

    private List<String> getNotifications() {
        List<String> notifications = new ArrayList<>();
        notifications.add("Notifications");
        notifications.add("Notifications");
        notifications.add("Notifications");
        notifications.add("Notifications");
        notifications.add("Notifications");
        notifications.add("Notifications");
        return notifications;
    }
}
