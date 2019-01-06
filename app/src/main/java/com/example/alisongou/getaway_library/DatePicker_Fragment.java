package com.example.alisongou.getaway_library;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by alisongou on 1/6/19.
 */

public class DatePicker_Fragment extends android.support.v4.app.DialogFragment {
    private static final String ARG_DATE ="date";

    private DatePicker datePicker;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date)getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v= LayoutInflater.from(getActivity()).inflate(R.layout.datepicker,null);
        datePicker=(DatePicker) v.findViewById(R.id.dialog_ate_picker);
        datePicker.init(year,month,day,null);

        return  new AlertDialog.Builder(getActivity()).setView(v).setTitle(R.string.date_picker_title).setPositiveButton(android.R.string.ok,null).create();
    }

    public static DatePicker_Fragment newInstance(Date date){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DATE,date);
        DatePicker_Fragment fragment = new DatePicker_Fragment();
        fragment.setArguments(bundle);
        return  fragment;

    }
}
