package com.example.alisongou.getaway_library;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements  PermissionsListener {
    private MapboxMap mMapboxMap;
    private PermissionsManager mPermissionsManager;
    private SearchView searchView;
    private static final String TAG = "MapActivity";
    private GeoJsonSource geoJson;
    private List<Feature> mFeatures;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_interface_fragment);

        //initialize button in map_interface_fragment

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.accesstoken));

        //create fragment
        SupportMapFragment mapFragment;
        if (savedInstanceState == null) {
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            LatLng patagonia = new LatLng(-90, 90);

            //build mapboxmap
            MapboxMapOptions mapboxMapOptions = new MapboxMapOptions();
            mapboxMapOptions.styleUrl(Style.DARK);
            mapboxMapOptions.camera(new CameraPosition.Builder().target(patagonia).zoom(9).build());

            //create mapbox fragment
            mapFragment = SupportMapFragment.newInstance(mapboxMapOptions);
            transaction.add(R.id.map_container, mapFragment, "com.mapbox.map");
            transaction.commit();
        } else {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentByTag("com.mapbox.map");
        }

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                MapActivity.this.mMapboxMap = mapboxMap;
                enablelocationcomponent();
            }
        });

        //setup searchview
        searchView=(SearchView) findViewById(R.id.searchview);
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                hideKeyboard();
                System.out.println("the input word is"+query);

                //execute task
                FetchPOITask fetchPOITask = new FetchPOITask();
                fetchPOITask.execute(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



    }

    private void hideKeyboard() {
        searchView.clearFocus();
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enablelocationcomponent() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            LocationComponent locationComponent = mMapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this);
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);
        }else{
            mPermissionsManager = new PermissionsManager(this);
            mPermissionsManager.requestLocationPermissions(this);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enablelocationcomponent();

        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }




//setup menu to repsonse which will take user to other pages
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.mainpagemenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.gotolist:
                Intent intent  = new Intent(MapActivity.this,GetawayLibrary_List_activity.class);
                startActivity(intent);
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }




    //define AsyncTask to retrieve geocoding
    private class FetchPOITask extends AsyncTask<String,Void,List<CarmenFeature>>{


        @Override
        protected List<CarmenFeature> doInBackground(String... string) {
            try {
                //use geocoding_request to get carmenfeatures
                //return  new Geocoding_request(string[0]).fetchresult();
                MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                        .accessToken(getString(R.string.accesstoken))
                        .query(string[0])
                        .build();
                mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
                    @Override
                    public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                        List<CarmenFeature> results = response.body().features();

                        if (results.size() > 0) {

                            // Log the first results Point.
                            Point firstResultPoint = results.get(0).center();
                            Log.d(TAG, "onResponse: " + firstResultPoint.toString());

                        } else {

                            // No result for your request were found.
                            Log.d(TAG, "onResponse: No result found");

                        }
                    }

                    @Override
                    public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

            } catch (Exception e) {
                Log.e(TAG,"failed to fetch url: ", e);

            }
            return null;
        }

    }



}
