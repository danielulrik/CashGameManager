package com.example.rank.component;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel Ulrik
 * Date: 04/05/2015
 * Time: 15:45
 */
public class SimpleDatePickerDialog extends Dialog implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialog datePickerDialog;
    private Context context;
    private OnSelectedDate onSelectedDate;

    public SimpleDatePickerDialog(Context context) {
        super(context);
        this.context = context;
    }

    public Dialog createDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(context, this, year, month, day);
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DATE, dayOfMonth);
        onSelectedDate.selectedDate(calendar.getTime());
    }

    public void setOnSelectedDate(OnSelectedDate onSelectedDate) {
        this.onSelectedDate = onSelectedDate;
    }

    public void updateDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void updateDate(int year, int monthOfYear, int dayOfMonth) {
        if (datePickerDialog != null) {
            datePickerDialog.updateDate(year, monthOfYear, dayOfMonth);
        }
    }

    public interface OnSelectedDate {
        void selectedDate(Date selectedDate);
    }
}
