package com.brawijaya.filkom.restpedia.ui.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brawijaya.filkom.restpedia.R;
import com.brawijaya.filkom.restpedia.network.ApiClient;
import com.brawijaya.filkom.restpedia.network.model.DirectionResponse;
import com.brawijaya.filkom.restpedia.ui.base.BaseFragment;
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
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.brawijaya.filkom.restpedia.utils.AppConstants.PERMISSION_LOCATION_REQUEST_CODE;

public class HomeFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = HomeFragment.class.getSimpleName();

    private List<LatLng> mLatLongs;

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private MarkerOptions mLocationMarker;

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
        return view;
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void setupView(View view) {
        getBaseActivity().setTitle(getString(R.string.home));
        mLatLongs = new ArrayList<>();
        if (mGoogleApiClient == null)
            mGoogleApiClient = new GoogleApiClient.Builder(getBaseActivity())
                    .addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mLocationRequest = MapUtils.createLocationRequest();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
        if (mapFragment != null) mapFragment.getMapAsync(this);
        else printLog(TAG, "SupportMapFragment is null");
    }

    private void assignLocationValues(Location currentLocation) {
        if (currentLocation != null) {
            printLog(TAG, "assignLocationValues: " + currentLocation.toString());
            markStartingLocationOnMap(mGoogleMap, new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
            MapUtils.addCameraToMap(mGoogleMap, new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
        }
    }

    private void markStartingLocationOnMap(GoogleMap mapObject, LatLng location) {
        if (mapObject != null) {
            mapObject.addMarker(new MarkerOptions().position(location).title("Current location"));
            mapObject.moveCamera(CameraUpdateFactory.newLatLng(location));
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    printLog(TAG, "onConnected: Success");
                    if (ActivityCompat.checkSelfPermission(getBaseActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getBaseActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        LocationServices.getFusedLocationProviderClient(getBaseActivity()).getLastLocation().addOnSuccessListener(getBaseActivity(), location -> {
                            assignLocationValues(location);
                            if (mLocationMarker == null) mLocationMarker = new MarkerOptions();
                            mLocationMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
                            printLog(TAG, "onConnected: createdMarker");
                        });
                    } else {
                        ActivityCompat.requestPermissions(getBaseActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_REQUEST_CODE);
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    break;
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        onError(connectionResult.getErrorMessage());
        printLog(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION_REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    onError("onRequestPermissionResult: Denied");
                } else {
                    if (ActivityCompat.checkSelfPermission(getBaseActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getBaseActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        LocationServices.getFusedLocationProviderClient(getBaseActivity()).getLastLocation().addOnSuccessListener(getBaseActivity(), location -> {
                            assignLocationValues(location);
                            if (mLocationMarker == null) mLocationMarker = new MarkerOptions();
                            mLocationMarker.position(new LatLng(location.getLatitude(), location.getLongitude()));
                            printLog(TAG, "onRequestPermissionsResult: createdMarker");
                        });
                    }
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnMapClickListener(this);
        printLog(TAG, "onMapReady");
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mLatLongs.size() > 0) {
            mGoogleMap.clear();
            mLatLongs.clear();
        }
        mLatLongs.add(latLng);
        printLog(TAG, "LatLong: " + latLng.toString());
        printLog(TAG, "Marker number: " + mLatLongs.size());
        mGoogleMap.addMarker(mLocationMarker);
        mGoogleMap.addMarker(new MarkerOptions().position(latLng));
        LatLng defaultLocation = mLocationMarker.getPosition();
        printLog(TAG, "defaultLocation: " + defaultLocation.toString());
        printLog(TAG, "destinationLocation: " + latLng.toString());
        // Use Google Direction API to get the route between these Locations
        String origin = String.valueOf(defaultLocation.latitude) + "," + String.valueOf(defaultLocation.longitude);
        String destination = String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude);
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
}
