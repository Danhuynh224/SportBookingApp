package com.example.pjbookingsport.frag;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DateTimePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private final TextView datetimepicker;
    private int selectedYear, selectedMonth, selectedDay;

    public DateTimePickerFragment(TextView datetimepicker) {
        this.datetimepicker = datetimepicker;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        selectedYear = year;
        selectedMonth = month;
        selectedDay = day;

        // Hiển thị hộp thoại chọn thời gian sau khi chọn ngày
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        new TimePickerDialog(requireContext(), this, hour, minute, true).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String datetime = String.format("%02d/%02d/%d %02d:%02d",
                selectedDay, selectedMonth + 1, selectedYear,
                hourOfDay, minute);
        datetimepicker.setText(datetime);
    }

}