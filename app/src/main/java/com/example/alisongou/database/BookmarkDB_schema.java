package com.example.alisongou.database;

import com.example.alisongou.getaway_library.Bookmark;

public class BookmarkDB_schema {
    public static final class BookmarkTable{
        public static final String NAME="boomkartable";
        public static class cols{
            public static final String DATE ="date";
            public static final String Checked ="ischecked";
            public static final String NAME="name";
            public static final String UUID="uuid";
        }
    }


}
