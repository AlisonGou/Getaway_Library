package com.example.alisongou.getaway_library;


import java.util.Date;
import java.util.UUID;

/**
 * Created by alisongou on 12/23/18.
 */

public class Bookmark {
    private UUID mbookmarkid;
    private Date bookmarkaddeddate;
    private String bookmarkname;
    private boolean ischecked;

    public Bookmark(){
       mbookmarkid = UUID.randomUUID();
       bookmarkaddeddate = new Date();
    }

    public UUID getMbookmarkid() {
        return mbookmarkid;
    }

    public void setMbookmarkid(UUID mbookmarkid) {
        this.mbookmarkid = mbookmarkid;
    }

    public Date getBookmarkaddeddate() {
        return bookmarkaddeddate;
    }

    public void setBookmarkaddeddate(Date bookmarkaddeddate) {
        this.bookmarkaddeddate = bookmarkaddeddate;
    }

    public String getBookmarkname() {
        return bookmarkname;
    }

    public boolean isIschecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public void setBookmarkname(String bookmarkname) {
        this.bookmarkname = bookmarkname;
    }
}
