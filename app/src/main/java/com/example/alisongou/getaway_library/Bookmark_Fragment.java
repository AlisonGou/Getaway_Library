package com.example.alisongou.getaway_library;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.Date;
import java.util.UUID;

/**
 * Created by alisongou on 12/23/18.
 */

public class Bookmark_Fragment extends Fragment {

    private Bookmark mBookmark;
    private EditText mEditText;
    private Button mDateButton;
    private CheckBox mCheckBox;
    private  MapView mapView;
    private static final String ARG_BOOK_ID="book id";
    private static final String DIALOG_DATE="DialogDate";
    private static final int REQUEST_DATE=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        mapView= (MapView) view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                System.out.println("map in book container initialized");
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
}
