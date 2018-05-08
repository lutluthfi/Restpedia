package com.brawijaya.filkom.restpedia;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.brawijaya.filkom.restpedia.network.VolleySingleton;

public class RestPediaApplication extends Application {
    private RequestQueue requestQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
    }
    public RequestQueue getVolleyRequestQueue(){
        return requestQueue;
    }
}
