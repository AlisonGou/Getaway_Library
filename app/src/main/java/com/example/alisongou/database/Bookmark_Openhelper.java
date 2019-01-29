package com.example.alisongou.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.alisongou.database.BookmarkDB_schema.BookmarkTable;

/**
 * Created by alisongou on 1/12/19.
 */

public class Bookmark_Openhelper extends SQLiteOpenHelper{
    private static final int Version=1;
    private static final String Datebase_Name="bookmark.db";

    public Bookmark_Openhelper(Context context){
        super(context,Datebase_Name,null,Version);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + BookmarkTable.NAME + "(" + " _id integer primary key autoincrement, " +
        BookmarkTable.cols.UUID + ", " + BookmarkTable.cols.Checked + ", " + BookmarkTable.cols.DATE
        + ", " + BookmarkTable.cols.NAME + ", " + BookmarkTable.cols.ADDRESS + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
