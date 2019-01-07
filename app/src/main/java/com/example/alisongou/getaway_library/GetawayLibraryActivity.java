package com.example.alisongou.getaway_library;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;
//this class is deprecated in this app
public class GetawayLibraryActivity extends GetawayLibrarySingleFragmentActivity {


    private static final String EXTRA_BOOKMARK_ID="com.example.alisongou.getaway_library.bookmark_id";

    public static Intent newintent(Context packagecontext, UUID bookid){
        Intent intent = new Intent(packagecontext,GetawayLibraryActivity.class);
        intent.putExtra(EXTRA_BOOKMARK_ID,bookid);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
       UUID bookid = (UUID) getIntent().getSerializableExtra(EXTRA_BOOKMARK_ID);

       return new Bookmark_Fragment().newInstance(bookid);

    }
}
