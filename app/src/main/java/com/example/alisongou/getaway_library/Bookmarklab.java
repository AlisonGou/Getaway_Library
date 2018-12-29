package com.example.alisongou.getaway_library;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Bookmarklab {
    private static Bookmarklab mBookmarklab;
    private List<Bookmark> mBookmarkList;

    public static Bookmarklab get(Context context){
        if(mBookmarklab==null){
            mBookmarklab=new Bookmarklab(context);
        }
        return mBookmarklab;
    }

    private Bookmarklab (Context context){
        mBookmarkList = new ArrayList<>();

        for(int i=0;i<100;i++){
            Bookmark bookmark=new Bookmark();
            bookmark.setBookmarkname("bookmarkname #"+i);
            bookmark.setIschecked(i%2==0);
            mBookmarkList.add(bookmark);
        }
    }


    public List<Bookmark> getBookmarkList() {
        return mBookmarkList;
    }
    public Bookmark getbookmark(UUID id){
        for (Bookmark bookmark :mBookmarkList){
            if(bookmark.getMbookmarkid().equals(id)){
                return bookmark;
            }
        }
        return null;
    }



}
