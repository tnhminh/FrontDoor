package vn.com.tma.frontdoor;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.frontdoor.R;

public class DatePickerActivity extends Activity {

    private TextView mDateDisplay, lbNotifyDay;

    private Button mPickDate, btnOkDate;

    private int mYear;

    private int mMonth;

    private int mDay;

    private TextView mTimeDisplay;

    private Button mPickTime;

    private int mhour;

    private int mminute;

    static final int TIME_DIALOG_ID = 1;

    static final int DATE_DIALOG_ID = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_time_picker_gui);
        lbNotifyDay = (TextView) findViewById(R.id.lbNotifyDay);
        lbNotifyDay.setBackgroundColor(Color.BLUE);
        lbNotifyDay.setTextColor(Color.WHITE);

        btnOkDate = (Button) findViewById(R.id.btnOKDate);
        btnOkDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDateDisplay = (TextView) findViewById(R.id.date);
        mPickDate = (Button) findViewById(R.id.datepicker);
        mTimeDisplay = (TextView) findViewById(R.id.time);
        mPickTime = (Button) findViewById(R.id.timepicker);

        // Pick time's click event listener
        mPickTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        // PickDate's click event listener
        mPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mhour = c.get(Calendar.HOUR_OF_DAY);
        mminute = c.get(Calendar.MINUTE);

        updateDate();
        updatetime();
    }

    private void updateDate() {
        mDateDisplay.setTextColor(Color.WHITE);
        mDateDisplay.setBackgroundColor(Color.RED);
        mDateDisplay.setText(new StringBuilder()
        // Month is 0 based so add 1
                .append(mDay).append("/").append(mMonth + 1).append("/").append(mYear).append(" "));
        // showDialog(TIME_DIALOG_ID);
    }

    public void updatetime() {
        mTimeDisplay.setTextColor(Color.WHITE);
        mTimeDisplay.setBackgroundColor(Color.RED);
        mTimeDisplay.setText(new StringBuilder().append(pad(mhour)).append(":").append(pad(mminute)));
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    // Datepicker dialog generation
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDate();
        }
    };

    // Timepicker dialog generation
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mhour = hourOfDay;
            mminute = minute;
            updatetime();
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);

        case TIME_DIALOG_ID:
            return new TimePickerDialog(this, mTimeSetListener, mhour, mminute, false);

        }
        return null;
    }
}