package com.brawijaya.filkom.restpedia.network;

import com.brawijaya.filkom.restpedia.network.model.map.DirectionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiHelper {

    @GET("maps/api/directions/json")
    Call<DirectionResponse> getDirectionFromGoogle(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String key);
}
