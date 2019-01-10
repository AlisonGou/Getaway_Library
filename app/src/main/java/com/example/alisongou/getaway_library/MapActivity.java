package com.example.alisongou.getaway_library;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.maps.MapboxMap;

public class MapActivity extends AppCompatActivity {
    private MapboxMap mMapboxMap;
    private PermissionsManager mPermissionsManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_interface_fragment);

        //initialize button in map_interface_fragment
        Button button = (Button) findViewById(R.id.gotoactiviybutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MapActivity.this,GetawayLibrary_List_activity.class);
                startActivity(intent);
            }
        });
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        /*Mapbox.getInstance(this, getString(R.string.accesstoken));

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
        mPermissionsManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this,"test",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
            enablelocationcomponent();
        }else{
            Toast.makeText(this,"permission not granted",Toast.LENGTH_LONG).show();
            finish();
        }
*/
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.mainpagemenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.gotolist:
                Intent intent  = new Intent(MapActivity.this,GetawayLibrary_Viewpager_activity.class);
                startActivity(intent);
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }*/
}
