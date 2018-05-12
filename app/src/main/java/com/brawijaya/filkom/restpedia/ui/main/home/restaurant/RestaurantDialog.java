package com.brawijaya.filkom.restpedia.ui.main.home.restaurant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brawijaya.filkom.restpedia.R;
import com.brawijaya.filkom.restpedia.network.model.firebase.RestaurantResponse;
import com.brawijaya.filkom.restpedia.ui.base.BaseDialog;

import butterknife.ButterKnife;

public class RestaurantDialog extends BaseDialog {

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

    }
}
