package com.brawijaya.filkom.restpedia.network.model.map;

import java.util.List;

public class LegsResponse {
    private List<StepsResponse> steps;

    public LegsResponse(List<StepsResponse> steps) {
        this.steps = steps;
    }

    public List<StepsResponse> getSteps() {
        return steps;
    }
}