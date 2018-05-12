package com.brawijaya.filkom.restpedia.network.model.map;

import java.util.List;

public class RouteResponse {
    private List<LegsResponse> legs;

    public RouteResponse(List<LegsResponse> legs) {
        this.legs = legs;
    }

    public List<LegsResponse> getLegs() {
        return legs;
    }
}