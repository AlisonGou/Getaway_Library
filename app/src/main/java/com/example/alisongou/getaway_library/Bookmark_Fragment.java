package com.example.alisongou.getaway_library;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

/**
 * Created by alisongou on 12/23/18.
 */

public class Bookmark_Fragment extends Fragment {

    private Bookmark mBookmark;
    private EditText mEditText;
    private EditText address;
    private Button mDateButton;
    private CheckBox mCheckBox;
    private static final String ARG_BOOK_ID="book id";
    private static final String DIALOG_DATE="DialogDate";
    private static final int REQUEST_DATE=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        UUID bookid = (UUID) getArguments().getSerializable(ARG_BOOK_ID);
        mBookmark=Bookmarklab.get(getActivity()).getbookmark(bookid);




    }

    public static Bookmark_Fragment newInstance(UUID bookid){
        Bundle bundle= new Bundle();
        bundle.putSerializable(ARG_BOOK_ID,bookid);

        Bookmark_Fragment bookmark_fragment = new Bookmark_Fragment();
        bookmark_fragment.setArguments(bundle);
        return  bookmark_fragment;

     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bookmark_fragment,container,false);

        mEditText = view.findViewById(R.id.bookmarkname);
        mEditText.setText(mBookmark.getBookmarkname());
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBookmark.setBookmarkname(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        address = view.findViewById(R.id.address);
        address.setText(mBookmark.getAddress());
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBookmark.setAddress(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = view.findViewById(R.id.datebutton);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePicker_Fragment datePicker_fragment = DatePicker_Fragment.newInstance(mBookmark.getBookmarkaddeddate());

                //set bookmark_fragment as target fragment
                datePicker_fragment.setTargetFragment(Bookmark_Fragment.this,REQUEST_DATE);
                datePicker_fragment.show(fragmentManager,DIALOG_DATE);
            }
        });



        mCheckBox=view.findViewById(R.id.isChecked);
        mCheckBox.setChecked(mBookmark.isIschecked());
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBookmark.setIschecked(isChecked);
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!= Activity.RESULT_OK){return;}

        if (requestCode==REQUEST_DATE){
            Date date = (Date)data.getSerializableExtra(DatePicker_Fragment.EXTRA_DATE);
            mBookmark.setBookmarkaddeddate(date);
            updateDate();
        }
    }

    private void updateDate() {
        mDateButton.setText(mBookmark.getBookmarkaddeddate().toString());
    }

    //set a delete menu for new bookmarkfragment
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_bookmark_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.deletebookmark:
                Bookmarklab.get(getActivity()).deletebookmark(mBookmark);
                //to return to parent activity
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }

    }



    @Override
    public void onPause() {
        super.onPause();
        Bookmarklab.get(getActivity()).updatebookmark(mBookmark);

    }
}


