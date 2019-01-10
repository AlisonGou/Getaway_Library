package com.example.alisongou.getaway_library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.List;
import java.util.UUID;

/**
 * Created by alisongou on 1/6/19.
 */

public class GetawayLibrary_Viewpager_activity extends AppCompatActivity {
    private ViewPager mViewpager;
    private List<Bookmark> mbookmarks;
    private MapView mapView;
    private static  final String EXTRA_BOOKMARK_ID="com.example.alisongou.getaway_library.bookmark_id";

    public static Intent newIntent(Context packagecontext, UUID bookid){
        Intent intent = new Intent(packagecontext,GetawayLibrary_Viewpager_activity.class);
        intent.putExtra(EXTRA_BOOKMARK_ID,bookid);
        return intent;

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getawaylibrary_viewpager_activity);
        mViewpager=(ViewPager) findViewById(R.id.getawayactivity_viewpager);

        Mapbox.getInstance(this, getString(R.string.accesstoken));


        mbookmarks=Bookmarklab.get(this).getBookmarkList();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewpager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Bookmark bookmark = mbookmarks.get(position);


                return Bookmark_Fragment.newInstance(bookmark.getMbookmarkid());
            }

            @Override
            public int getCount() {
                return  mbookmarks.size();
            }
        });

        UUID bookid = (UUID) getIntent().getSerializableExtra(EXTRA_BOOKMARK_ID);

        for (int i=0; i <mbookmarks.size();i++){
            if(mbookmarks.get(i).getMbookmarkid().equals(bookid)){
                mViewpager.setCurrentItem(i);
                break;
            }
        }








    }
}
