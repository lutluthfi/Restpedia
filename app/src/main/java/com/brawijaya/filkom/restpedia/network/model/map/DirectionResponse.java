package com.brawijaya.filkom.restpedia.network.model.map;

import java.util.List;

public class DirectionResponse {
    private List<RouteResponse> routes;
    private String status;

    public DirectionResponse(List<RouteResponse> routes, String status) {
        this.routes = routes;
        this.status = status;
    }

    public List<RouteResponse> getRoutes() {
        return routes;
    }

    public String getStatus() {
        return status;
    }
}
