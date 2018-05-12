package com.brawijaya.filkom.restpedia.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brawijaya.filkom.restpedia.R;
import com.brawijaya.filkom.restpedia.network.model.firebase.MenuResponse;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static List<MenuResponse> mMenus;

    public MenuAdapter() {
        MenuAdapter.mMenus = new ArrayList<>();
    }

    public void add(List<MenuResponse> menus) {
        MenuAdapter.mMenus.addAll(menus);
        notifyDataSetChanged();
    }

    public void clear() {
        MenuAdapter.mMenus.clear();
        notifyDataSetChanged();
    }

    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MenuViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MenuViewHolder) holder).onBind(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageview_menu_food) ImageView mFoodImageView;
        @BindView(R.id.textview_menu_food_name) TextView mFoodNameTextView;
        @BindView(R.id.textview_menu_food_price) TextView mFoodPriceTextView;
        @BindView(R.id.textview_menu_food_type) TextView mFoodTypeTextView;

        private static MenuViewHolder create(ViewGroup parent) {
            return new MenuViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content_menu, parent, false));
        }

        private MenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void onBind(int position) {
        }
    }
}
