package com.example.alisongou.getaway_library;


import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
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
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
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

import java.util.ArrayList;
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
    private List<CarmenFeature> mFeatures;
    private String COLUMN_NAME_ADDRESS ="address";
    private String lat;
    private String lng;
    private String[] mCollumnNames = {BaseColumns._ID,COLUMN_NAME_ADDRESS,lat,lng};




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
                //System.out.println("searchview the input word is:"+query);

                //execute task
                //FetchPOITask fetchPOITask = new FetchPOITask();
                //fetchPOITask.execute(query);
                //searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText!=null&newText.length()>1){
                    MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                            .accessToken("pk.eyJ1IjoiYWxpc29uZ291IiwiYSI6ImNqcGFwdXc5czAyOWgzbG9mZmsyNTh2a2wifQ.FSS06iLZDD3cJ4exXRyZuA")
                            .query(newText)
                            .build();
                    mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
                        @Override
                        public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                            try {
                                MapActivity.this.mMapboxMap.clear();
                                List<CarmenFeature> results = response.body().features();
                                System.out.println("geocoding resut is "+results);
                                Icon icon = IconFactory.getInstance(MapActivity.this).fromResource(R.drawable.ic_menu_gotolist);
                                for (int i=0;i<results.size();i++){
                                    //get placename to put as bookmarkname
                                    String placeName = results.get(i).text();
                                    System.out.println("pa");
                                    //get address to put as memos
                                    String address = results.get(i).address();
                                    MapActivity.this.mMapboxMap.addMarker(new MarkerOptions().position(new LatLng(results.get(i).center().latitude(),results.get(i).center().longitude())).setTitle(results.get(i).placeName()).setIcon(icon));
                                    MapActivity.this.mMapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(@NonNull Marker marker) {
                                            Bookmark bookmark=new Bookmark();
                                            bookmark.setBookmarkname(placeName);


                                            Bookmarklab.get(MapActivity.this).addbookmark(bookmark);
                                            Intent intent = GetawayLibrary_Viewpager_activity.newIntent(MapActivity.this,bookmark.getMbookmarkid());
                                            startActivity(intent);
                                            return false;
                                        }
                                    });
                                    //System.out.println("latlng is :"+ results.get(i));
                                }

                                MatrixCursor suggestioncursor = new MatrixCursor(mCollumnNames);
                                int key=0;
                                //add each address of carmenfeature to a new row
                                for (CarmenFeature carmenFeature : results){
                                    suggestioncursor.addRow(new Object[]{key++,carmenFeature.placeName(),carmenFeature.center().latitude(),carmenFeature.center().longitude()});
                                }
                                String[] cols=new String[]{COLUMN_NAME_ADDRESS};
                                int[] to = new int[]{R.id.suggestion_address};
                                //define simplecursoradaptor
                                SimpleCursorAdapter suggestionCursorAdapter = new SimpleCursorAdapter(MapActivity.this,R.layout.suggestion,suggestioncursor,cols,to,0);
                                searchView.setSuggestionsAdapter(suggestionCursorAdapter);
                                //handle an address suggestion being chose
                                searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                                    @Override
                                    public boolean onSuggestionSelect(int position) {
                                        Toast.makeText(MapActivity.this, "onSuggestionSelect", Toast.LENGTH_LONG).show();
                                        return true;
                                    }

                                    @Override
                                    public boolean onSuggestionClick(int position) {
                                        Toast.makeText(MapActivity.this, "onSuggestionClick", Toast.LENGTH_LONG).show();
                                       //get the selected row
                                        MatrixCursor selectedrow = (MatrixCursor)suggestionCursorAdapter.getItem(position);
                                        //get rows index
                                        System.out.println("clicled postion is " +position);
                                        int selectedcursorindex = selectedrow.getColumnIndex(COLUMN_NAME_ADDRESS);
                                        String address = selectedrow.getString(selectedcursorindex);
                                        System.out.println("address is"+address);
                                        //System.out.println("selectedrow is "+selectedrow.getColumnCount()+selectedrow.getCount());

                                        Double lat = Double.parseDouble(selectedrow.getString(selectedcursorindex+1));
                                        Double lng = Double.parseDouble(selectedrow.getString(selectedcursorindex+2));

                                        System.out.println("lat and lng are "+lat+" , "+lng);

                                        mMapboxMap.clear();
                                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat,lng)).zoom(20).build();
                                        mMapboxMap.setCameraPosition(cameraPosition);
                                        IconFactory iconFactory = IconFactory.getInstance(MapActivity.this);
                                        Icon icon = iconFactory.fromResource(R.drawable.mapbox_logo_icon);
                                        MapActivity.this.mMapboxMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).icon(icon));

                                        return true;
                                    }
                                });
                            } catch (Exception e) {
                                //System.out.println("geocode suggestion error :"+e.getCause());
                            }
                        }

                        @Override
                        public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
                }else if (newText!=null&newText.length()<=1){
                    Toast.makeText(MapActivity.this,"tell me your dream place",Toast.LENGTH_LONG).show();
                }

                return true;
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
    public class FetchPOITask extends AsyncTask<String,Void,List<POI>>{
        @Override
        protected List<POI> doInBackground(String...strings) {

                //System.out.println("string [0] is :"+string[0]);
                //use geocoding_request to get POI in carmenfeatures returned by geocoding request
            return  new Geocoding_request(strings[0]).fetchPOI();
            //use mapboxgeocdoing wrapper to get carmenfeatures
            //return  new Geocoding_request(strings[0]).returnfeature();
        }

        @Override
        protected void onPostExecute(List<POI> POIlist) {
            //draw returned POI icons
            System.out.println("onpost poi list is"+POIlist);
            Icon icon = IconFactory.getInstance(MapActivity.this).fromResource(R.drawable.mapbox_logo_icon);
            for (int i=0;i<POIlist.size();i++){
                MapActivity.this.mMapboxMap.addMarker(new MarkerOptions().position(new LatLng(POIlist.get(i).getLat(),POIlist.get(i).getLon())).setIcon(icon));
                System.out.println("latlng is :"+ POIlist.get(i));
            }
        }
    }



}
