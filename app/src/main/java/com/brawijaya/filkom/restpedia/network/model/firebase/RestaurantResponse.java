package com.brawijaya.filkom.restpedia.network.model.firebase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RestaurantResponse implements Serializable {

    @SerializedName("foto")
    @Expose
    private String foto;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("long")
    @Expose
    private String _long;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("menu")
    @Expose
    private List<MenuResponse> menu = null;

    public RestaurantResponse() {
    }

    public RestaurantResponse(String nama, String foto, String lat, String _long, List<MenuResponse> menu) {
        this.nama = nama;
        this.foto = foto;
        this.lat = lat;
        this._long = _long;
        this.menu = menu;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLong() {
        return _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

    public List<MenuResponse> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuResponse> menu) {
        this.menu = menu;
    }
}
