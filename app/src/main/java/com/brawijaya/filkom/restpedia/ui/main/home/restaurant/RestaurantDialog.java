package com.brawijaya.filkom.restpedia.ui.main.home.restaurant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brawijaya.filkom.restpedia.R;
import com.brawijaya.filkom.restpedia.network.model.firebase.RestaurantResponse;
import com.brawijaya.filkom.restpedia.ui.adapters.MenuAdapter;
import com.brawijaya.filkom.restpedia.ui.base.BaseDialog;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RestaurantDialog extends BaseDialog {

    public static final String TAG = RestaurantDialog.class.getSimpleName();

    @BindView(R.id.imageview_restaurant) CircleImageView mRestaurantImageView;
    @BindView(R.id.textview_restaurant_name) TextView mRestaurantNameTextView;
    @BindView(R.id.textview_restaurant_position) TextView mRestaurantPositinoTextView;
    @BindView(R.id.recyclerview_restaurant_menu) RecyclerView mRecyclerView;

    public static RestaurantDialog newInstance(RestaurantResponse restaurant) {
        RestaurantDialog dialog = new RestaurantDialog();
        Bundle args = new Bundle();
        args.putSerializable("restaurant", restaurant);
        dialog.setArguments(args);
        return dialog;
    }

    private RestaurantResponse getRestaurant() {
        if (getArguments() != null) {
            return (RestaurantResponse) getArguments().getSerializable("restaurant");
        }
        return null;
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_restaurant, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        return view;
    }

    @Override
    protected void setupView(View view) {
        MenuAdapter adapter = new MenuAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        if (getRestaurant() != null) {
            Glide.with(getBaseActivity()).asBitmap().load(getRestaurant().getFoto()).into(mRestaurantImageView);
            mRestaurantNameTextView.setText(getRestaurant().getNama());
            mRestaurantPositinoTextView.setText(String.format("Lat: %s, Long: %s", getRestaurant().getLat(), getRestaurant().getLong()));
            adapter.add(getRestaurant().getMenu());
        }
    }
}
