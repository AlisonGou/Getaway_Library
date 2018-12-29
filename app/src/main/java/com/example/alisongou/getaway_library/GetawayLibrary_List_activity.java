package com.example.alisongou.getaway_library;

import android.support.v4.app.Fragment;

public class GetawayLibrary_List_activity extends GetawayLibrarySingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new Bookmark_list_fragment();
    }
}
