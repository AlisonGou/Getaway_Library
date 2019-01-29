package com.example.alisongou.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.alisongou.database.BookmarkDB_schema.BookmarkTable;
import com.example.alisongou.getaway_library.Bookmark;

import java.util.Date;
import java.util.UUID;

/**
 * Created by alisongou on 1/13/19.
 */

public class BookmarkCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public BookmarkCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Bookmark getbookmark(){
        String uuidstring = getString(getColumnIndex(BookmarkTable.cols.UUID));
        String name = getString(getColumnIndex(BookmarkTable.cols.NAME));
        String address = getString(getColumnIndex(BookmarkTable.cols.ADDRESS));
        int checked = getInt(getColumnIndex(BookmarkTable.cols.Checked));
        long date = getLong(getColumnIndex(BookmarkTable.cols.DATE));

        Bookmark bookmark = new Bookmark(UUID.fromString(uuidstring));
        bookmark.setBookmarkname(name);
        bookmark.setAddress(address);
        bookmark.setBookmarkaddeddate(new Date(date));
        bookmark.setIschecked(checked !=0 );
        return bookmark;
    }



}
