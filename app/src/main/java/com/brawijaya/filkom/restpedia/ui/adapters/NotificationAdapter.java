package com.brawijaya.filkom.restpedia.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brawijaya.filkom.restpedia.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static List<String> mNotifications;

    public NotificationAdapter() {
        NotificationAdapter.mNotifications = new ArrayList<>();
    }

    public void add(List<String> notifications) {
        NotificationAdapter.mNotifications.addAll(notifications);
        notifyDataSetChanged();
    }

    public void clear(){
        NotificationAdapter.mNotifications.clear();
        notifyDataSetChanged();
    }

    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return NotificationViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((NotificationViewHolder) holder).onBind(position);
    }

    @Override
    public int getItemCount() {
        return NotificationAdapter.mNotifications.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_notification_title) TextView mTitleTextView;

        private static NotificationViewHolder create(ViewGroup parent) {
            return new NotificationViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content_notification, parent, false));
        }

        private NotificationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void onBind(int position) {
            mTitleTextView.setText(mNotifications.get(position));
        }
    }
}
