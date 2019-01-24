package com.example.alisongou.getaway_library;

public class POI{
    String placename;
    double lat;
    double lon;

    public POI(){
        return;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getPlacename() {
        return placename;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
    }
}
