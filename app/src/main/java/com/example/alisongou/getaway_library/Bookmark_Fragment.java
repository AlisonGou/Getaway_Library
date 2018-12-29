package com.example.alisongou.getaway_library;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

/**
 * Created by alisongou on 12/23/18.
 */

public class Bookmark_Fragment extends Fragment {
    private Bookmark mBookmark;
    private EditText mEditText;
    private Button mDateButton;
    private CheckBox mCheckBox;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBookmark=new Bookmark();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bookmark_fragment,container,false);

        mEditText = view.findViewById(R.id.bookmarkname);
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
        mDateButton.setText(mBookmark.getBookmarkaddeddate().toString());
        mDateButton.setEnabled(false);


        mCheckBox=view.findViewById(R.id.isChecked);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBookmark.setIschecked(isChecked);
            }
        });
        return view;
    }
}
