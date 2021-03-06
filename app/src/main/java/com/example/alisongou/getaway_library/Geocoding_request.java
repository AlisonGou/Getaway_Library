package com.example.alisongou.getaway_library;

import android.net.Uri;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Geocoding_request{
    private static final String TAG ="fetch geocoding json";
    private static final String token ="pk.eyJ1IjoiYWxpc29uZ291IiwiYSI6ImNqcGFwdXc5czAyOWgzbG9mZmsyNTh2a2wifQ.FSS06iLZDD3cJ4exXRyZuA";
    private String keywords;
    private String joinstring;
    private List<CarmenFeature> geofeatures;
    private List<CarmenFeature> features;
    List<POI> mPOIList;
    List<LatLng> latLngList;

    public Geocoding_request(String querykeyword){
        keywords=querykeyword;
        return;
    }

    public  Geocoding_request(){
        return;
    }


    public byte[] getURLBytes(String urlSpc) throws IOException{
        URL url = new URL(urlSpc);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try{
            ByteArrayOutputStream out=new ByteArrayOutputStream();

            //getinputstream is used to connect to urlSpc
            InputStream in=connection.getInputStream();
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage()+"url"+urlSpc);
            }
            int bytesread=0;
            byte[] buffer = new byte[1024];

            //keep reading bytes which would later be writen in out, closes connection till all data returns
            while((bytesread = in.read(buffer))>0){
                out.write(buffer,0,bytesread);
        }
            out.close();
            return out.toByteArray();
        }finally {
            connection.disconnect();
        }

    }
    public String getURLString (String urlSpc) throws IOException{

        return new String(getURLBytes(urlSpc));
    }
    public List<CarmenFeature> fetchcarmenfeatures() {
        try {
            //construct url
            String url = Uri.parse("https://api.mapbox.com/geocoding/v5/mapbox.places/").buildUpon().
                    appendPath(keywords + ".json").appendQueryParameter("access_token", token).build().toString();
            System.out.println("url is :" + url);

            //receive result as string
            joinstring = getURLString(url);
            features = GeocodingResponse.fromJson(joinstring).features();
            System.out.println("carmenfeatures are "+features);

            //Log.i(TAG, "received json: " + joinstring);
        } catch (IOException e) {
            //Log.e(TAG, "failed to fetch json", e);

        }
        return features;
    }
    public List<POI> fetchPOI() {
        mPOIList = new ArrayList<>();
        try {
            //construct url
            String url = Uri.parse("https://api.mapbox.com/geocoding/v5/mapbox.places/").buildUpon().
                    appendPath(keywords + ".json").appendQueryParameter("access_token", token).build().toString();
            System.out.println("url is :" + url);

            //receive result as string
            joinstring = getURLString(url);
            features = GeocodingResponse.fromJson(joinstring).features();
            System.out.println("carmenfeatures are "+features);
            parsefeatures(mPOIList,features);

            //Log.i(TAG, "received json: " + joinstring);
        } catch (IOException e) {
            //Log.e(TAG, "failed to fetch json", e);

        }
        //System.out.println("fetchresult is : "+joinstring);
        return mPOIList;


    }
    public void parsefeatures(List<POI> poilist, List<CarmenFeature> carmenFeatures){
        POI poi=new POI();
        System.out.println("carmen size is"+carmenFeatures.size());
        for(int i=0;i<carmenFeatures.size();i++){
            //poi.setLat(point.latitude());
            //poi.setLon(point.longitude());
            //poi.setPlacename(carmenFeatures.get(i).placeName());
            //System.out.println("carmenfeature geometry point lat"+point.latitude());

            poilist.add(poi);
        }

        System.out.println("list of POI is :" + poilist);
    }
    public List<LatLng> getLanLngList () {
        latLngList = new ArrayList<>();
        try {
            //construct url
            String url = Uri.parse("https://api.mapbox.com/geocoding/v5/mapbox.places/").buildUpon().
                    appendPath(keywords + ".json").appendQueryParameter("access_token", token).build().toString();
            System.out.println("url is :" + url);

            //receive result as string
            joinstring = getURLString(url);
            features = GeocodingResponse.fromJson(joinstring).features();
            System.out.println("carmenfeatures are "+features);
            returnLatLnglist(latLngList,features);

            //Log.i(TAG, "received json: " + joinstring);
        } catch (IOException e) {
            //Log.e(TAG, "failed to fetch json", e);

        }
        //System.out.println("fetchresult is : "+joinstring);
        return latLngList;


    }
    public List<LatLng> returnLatLnglist(List<LatLng> latLngList,List<CarmenFeature> carmenFeatures){
        LatLng latLng = new LatLng();
        for(int i=0;i<carmenFeatures.size();i++){
            latLng.setLatitude(carmenFeatures.get(i).center().latitude());
            latLng.setLongitude(carmenFeatures.get(i).center().longitude());
            latLngList.add(latLng);
        }
        return latLngList;
    }

}
