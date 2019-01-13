package com.example.alisongou.getaway_library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.alisongou.database.BookmarkDB_schema;
import com.example.alisongou.database.BookmarkDB_schema.BookmarkTable;
import com.example.alisongou.database.Bookmark_Openhelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Bookmarklab {
    private static Bookmarklab mBookmarklab;

    private SQLiteDatabase mDatabase;
    private Context mcontext;

    public static Bookmarklab get(Context context){
        if(mBookmarklab==null){
            mBookmarklab=new Bookmarklab(context);
        }
        return mBookmarklab;
    }

    private Bookmarklab (Context context){
        mcontext = context.getApplicationContext();
        mDatabase = new Bookmark_Openhelper(context).getWritableDatabase();

    }

    public static ContentValues getContentValues(Bookmark bookmark){
        ContentValues contentValues = new ContentValues();
        contentValues.put(BookmarkTable.cols.UUID,bookmark.getMbookmarkid().toString());
        contentValues.put(BookmarkTable.cols.NAME, bookmark.getBookmarkname());
        contentValues.put(BookmarkTable.cols.Checked,bookmark.isIschecked()?1:0);
        contentValues.put(BookmarkTable.cols.DATE,bookmark.getBookmarkaddeddate().toString());
        return contentValues;
    }

    public List<Bookmark> getBookmarkList() {
        return new ArrayList<>();
    }
    public Bookmark getbookmark(UUID id){

        return null;
    }
    private void addbookmark(Bookmark bookmark){
        ContentValues contentValues = getContentValues(bookmark);
        mDatabase.insert(BookmarkTable.NAME,null,contentValues);

    }

    public  void updatebookmark(Bookmark bookmark){
        String uuidstring = bookmark.getMbookmarkid().toString();
        ContentValues contentValues = getContentValues(bookmark);
        mDatabase.update(BookmarkTable.NAME, contentValues, BookmarkTable.cols.UUID + " = ?", new String[]{uuidstring});

    }
    private void deletebookmark(Bookmark bookmark){}


    private Cursor querybookmarks(String whereclause, String[] whereargs){
        Cursor cursor = mDatabase.query(BookmarkTable.NAME,null,whereclause,whereargs,null,null,null );
        return  cursor;
    }




}
