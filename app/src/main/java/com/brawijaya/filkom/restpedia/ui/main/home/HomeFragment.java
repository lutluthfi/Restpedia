package com.brawijaya.filkom.restpedia.ui.main.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.brawijaya.filkom.restpedia.R;
import com.brawijaya.filkom.restpedia.network.ApiClient;
import com.brawijaya.filkom.restpedia.network.model.map.DirectionResponse;
import com.brawijaya.filkom.restpedia.network.model.firebase.RestaurantResponse;
import com.brawijaya.filkom.restpedia.prefs.AppPreferencesHelper;
import com.brawijaya.filkom.restpedia.prefs.PreferencesHelper;
import com.brawijaya.filkom.restpedia.ui.base.BaseFragment;
import com.brawijaya.filkom.restpedia.ui.main.home.restaurant.RestaurantDialog;
import com.brawijaya.filkom.restpedia.utils.AppConstants;
import com.brawijaya.filkom.restpedia.utils.MapUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.brawijaya.filkom.restpedia.utils.AppConstants.PERMISSION_LOCATION_REQUEST_CODE;

// link google map json
// https://maps.googleapis.com/maps/api/directions/json?origin=-7.9497491,112.6139224&destination=-7.951598,112.634276&key=AIzaSyCuZCfoPPUV1upJT10kJbCbV71LUqwhFCM

public class HomeFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    public static final String TAG = HomeFragment.class.getSimpleName();

    private String originLocation;
    private List<RestaurantResponse> mRestaurants;

    private GoogleMap mGoogleMap;
    private MarkerOptions mLocationMarker;
    private MarkerOptions mRestaurantMarker;
    private DatabaseReference mDatabaseReference;
    private PreferencesHelper mPrefs;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        mPrefs = AppPreferencesHelper.with(getContext());
        if (mLocationMarker == null) mLocationMarker = new MarkerOptions();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        return view;
    }

    @Override
    public void setupView(View view) {
        getBaseActivity().setTitle(getString(R.string.home));
        mRestaurants = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
        if (mapFragment != null) mapFragment.getMapAsync(this);
        else printLog(TAG, "SupportMapFragment is null");
        printLog(TAG, "onConnected: createdMarker");
    }

    private void setupRestaurantMarker() {
        if (mRestaurantMarker == null) mRestaurantMarker = new MarkerOptions();
        mDatabaseReference.child("restaurant").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot restaurantsSnapShot : dataSnapshot.getChildren()) {
                    RestaurantResponse restaurant = restaurantsSnapShot.getValue(RestaurantResponse.class);
                    if (restaurant != null) {
                        double restaurantLat = Double.parseDouble(restaurant.getLat());
                        double restaurantLong = Double.parseDouble(restaurant.getLong());
                        addLocationMarker(mGoogleMap, new LatLng(restaurantLat, restaurantLong), restaurant.getNama());
                        mRestaurants.add(restaurant);
                        printLog("HomeFragment", "setupRestaurantMarker: " + restaurant.getLat() + "," + restaurant.getLong());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                printLog("HomeFragment", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void addLocationMarker(GoogleMap mapObject, LatLng location, String title) {
        if (mapObject != null) {
            mapObject.addMarker(new MarkerOptions().position(location).title(title));
            mapObject.moveCamera(CameraUpdateFactory.newLatLng(location));
        }
    }

    private void fetchDirectionGoogleMap(String origin, String destination) {
        ApiClient.create().getDirectionFromGoogle(origin, destination, AppConstants.KEY_GOOGLE_API).enqueue(new Callback<DirectionResponse>() {
            @Override
            public void onResponse(@NonNull Call<DirectionResponse> call, @NonNull Response<DirectionResponse> response) {
                if (response.body() != null) {
                    printLog(TAG, "onResponse: " + response.code());
                    printLog(TAG, "onResponse: " + response.toString());
                    List<LatLng> directions = MapUtils.getDirectionPolyline(Objects.requireNonNull(response.body()).getRoutes());
                    MapUtils.drawRouteOnMap(mGoogleMap, directions);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DirectionResponse> call, @NonNull Throwable t) {
                printLog(TAG, t.getMessage());
                onError(t.getMessage());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnMapClickListener(this);
        mGoogleMap.setOnMarkerClickListener(this);
        LatLng origin = new Gson().fromJson(mPrefs.getUserOriginLocation(), LatLng.class);
        addLocationMarker(mGoogleMap, origin, "Current Location");
        MapUtils.addCameraToMap(mGoogleMap, origin);
        originLocation = String.valueOf(origin.latitude) + "," + String.valueOf(origin.longitude);
        mLocationMarker.position(origin);
        setupRestaurantMarker();
        printLog(TAG, "onMapReady");
    }

    @Override
    public void onMapClick(LatLng latLng) {
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mGoogleMap.clear();
        mGoogleMap.addMarker(mLocationMarker);
        setupRestaurantMarker();
        if (marker.getTitle() != null) {
            switch (marker.getTitle()) {
                case "Kober Mie Setan Soekarno Hatta":
                    String destination1 = String.valueOf(mRestaurants.get(0).getLat()) + "," + mRestaurants.get(0).getLong();
                    RestaurantDialog.newInstance(mRestaurants.get(0)).show(getChildFragmentManager(), RestaurantDialog.TAG);
                    fetchDirectionGoogleMap(originLocation, destination1);
                    return true;
                case "Warkop Brewok":
                    String destination2 = String.valueOf(mRestaurants.get(1).getLat()) + "," + mRestaurants.get(1).getLong();
                    RestaurantDialog.newInstance(mRestaurants.get(1)).show(getChildFragmentManager(), RestaurantDialog.TAG);
                    fetchDirectionGoogleMap(originLocation, destination2);
                    return true;
                case "Cak Per Soekarno Hatta":
                    String destination = String.valueOf(mRestaurants.get(2).getLat()) + "," + mRestaurants.get(2).getLong();
                    RestaurantDialog.newInstance(mRestaurants.get(2)).show(getChildFragmentManager(), RestaurantDialog.TAG);
                    fetchDirectionGoogleMap(originLocation, destination);
                    return true;
                case "Soto Ayam Babon":
                    String destination3 = String.valueOf(mRestaurants.get(3).getLat()) + "," + mRestaurants.get(3).getLong();
                    RestaurantDialog.newInstance(mRestaurants.get(3)).show(getChildFragmentManager(), RestaurantDialog.TAG);
                    fetchDirectionGoogleMap(originLocation, destination3);
                    return true;
                case "McDonald's Watugong":
                    String destination4 = String.valueOf(mRestaurants.get(4).getLat()) + "," + mRestaurants.get(4).getLong();
                    RestaurantDialog.newInstance(mRestaurants.get(4)).show(getChildFragmentManager(), RestaurantDialog.TAG);
                    fetchDirectionGoogleMap(originLocation, destination4);
                    return true;
                default:
                    return true;
            }
        }
        return false;
    }
}
