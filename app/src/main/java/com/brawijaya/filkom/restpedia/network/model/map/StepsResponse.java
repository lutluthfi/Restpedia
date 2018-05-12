package com.brawijaya.filkom.restpedia.network.model.map;

public class StepsResponse {
    private PolylineResponse polyline;

    public StepsResponse(PolylineResponse polyline) {
        this.polyline = polyline;
    }

    public PolylineResponse getPolyline() {
        return polyline;
    }
}