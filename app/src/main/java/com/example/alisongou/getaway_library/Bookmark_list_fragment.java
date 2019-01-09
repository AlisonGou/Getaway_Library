package com.example.alisongou.getaway_library;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class Bookmark_list_fragment extends Fragment {
    private RecyclerView mRecyclerView;
    private BookmarkAdaptor bookmarkAdaptor;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookmark_list_fragment,container,false);
        mRecyclerView = view.findViewById(R.id.bookmark_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        UpdateUI();
        return view;
    }

    private void UpdateUI(){
        Bookmarklab bookmarklab = Bookmarklab.get(getActivity());
        List<Bookmark> bookmarks = bookmarklab.getBookmarkList();

        if(bookmarkAdaptor==null){
            bookmarkAdaptor = new BookmarkAdaptor(bookmarks);
            mRecyclerView.setAdapter(bookmarkAdaptor);
        }else {
            bookmarkAdaptor.notifyDataSetChanged();
        }


    }

    private class BookmarkHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTextview;
        TextView mDatetextview;
        CheckBox mcheckbox;
        private Bookmark mbookmark;

        public BookmarkHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTextview = (TextView)itemView.findViewById(R.id.list_item_bookmark_title_textview);
            mcheckbox = (CheckBox)itemView.findViewById(R.id.list_item_bookmark_checked);
            mDatetextview=(TextView)itemView.findViewById(R.id.list_item_bookmark_Date_textview);
        }

        public void bindbookmark(Bookmark bookmark){
            mbookmark = bookmark;
            mTextview.setText(mbookmark.getBookmarkname());
            mDatetextview.setText(mbookmark.getBookmarkaddeddate().toString());
            mcheckbox.setChecked(mbookmark.isIschecked());

        }

        @Override
        public void onClick(View view) {
            //start getawaylibraryactivity when clicked on recyclerview
            System.out.print("mbookmarkid is:"+mbookmark.getMbookmarkid().toString());
            Intent intent = GetawayLibrary_Viewpager_activity.newIntent(getActivity(),mbookmark.getMbookmarkid());
            startActivity(intent);

        }
    }

    private class BookmarkAdaptor extends RecyclerView.Adapter<BookmarkHolder>{
        private List<Bookmark> mbookmarks;

        public BookmarkAdaptor(List<Bookmark> bookmarks){
            mbookmarks=bookmarks;

        }


        @Override
        public BookmarkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_bookmark,parent,false);
            return new BookmarkHolder(view);
        }

        @Override
        public void onBindViewHolder(BookmarkHolder holder, int position) {
            Bookmark bookmark = mbookmarks.get(position);
            holder.bindbookmark(bookmark);


        }

        @Override
        public int getItemCount() {
            return mbookmarks.size();

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        UpdateUI();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_bookmark_list,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_bookmark:
                Bookmark bookmark=new Bookmark();
                Bookmarklab.get(getActivity()).getBookmarkList().add(bookmark);
                Intent intent = GetawayLibrary_Viewpager_activity.newIntent(getActivity(),bookmark.getMbookmarkid());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
