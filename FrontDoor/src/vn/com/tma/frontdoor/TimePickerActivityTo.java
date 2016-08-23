package vn.com.tma.frontdoor;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.frontdoor.R;

public class TimePickerActivityTo extends Activity {
    public static final int TIMEPICKER_RESULT = 65;

    private TextView displayTime, txvSelected;

    private Button pickTime, btnOK;

    private int pHour;

    private int pMinute;

    private int pOfListItem;

    static final int TIME_DIALOG_ID = 0;

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // After choosen time, 2 field will asign new value
            pHour = hourOfDay;
            pMinute = minute;
            // ----
            sendIntentToNotifyActivity();
            // ----
            updateDisplay();
            displayToast();
        }

    };

    private void sendIntentToNotifyActivity() {
        Intent i = getIntent();
        i.putExtra("Result", new StringBuilder().append(pad(pHour)).append(":").append(pad(pMinute)).toString());
        i.putExtra("Return", pOfListItem);
        setResult(TIMEPICKER_RESULT, i);
    }

    private void updateDisplay() {
        displayTime.setBackgroundColor(Color.RED);
        displayTime.setTextColor(Color.WHITE);
        displayTime.setText(new StringBuilder().append(pad(pHour)).append(":").append(pad(pMinute)));
    }

    private void displayToast() {
        Toast.makeText(this, new StringBuilder().append("Time choosen is ").append(displayTime.getText()), Toast.LENGTH_SHORT).show();
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_picker_gui);

        /** Capture our View elements */
        displayTime = (TextView) findViewById(R.id.timeDisplay);
        pickTime = (Button) findViewById(R.id.pickTime);
        btnOK = (Button) findViewById(R.id.btnTimePickerOK);
        btnOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pickTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        showContentOfPickerTime();

        updateDisplay();

    }

    private void showContentOfPickerTime() {
        txvSelected = (TextView) findViewById(R.id.txvSelected);
        Intent intent = getIntent();
        String selected = intent.getStringExtra("Selected");
        // ---
        pOfListItem = intent.getIntExtra("Position", 88);
        String currentTime = intent.getStringExtra("CurrentTime");
        if (currentTime.matches("\\d+:\\d+")) {
            pHour = Integer.parseInt(currentTime.substring(0, currentTime.indexOf(":")));
            pMinute = Integer.parseInt(currentTime.substring(currentTime.indexOf(":") + 1));
        } else {
            /** Get the current time */
            Calendar cal = Calendar.getInstance();
            pHour = cal.get(Calendar.HOUR_OF_DAY);
            pMinute = cal.get(Calendar.MINUTE);
        }

        txvSelected.setBackgroundColor(Color.BLUE);
        txvSelected.setTextColor(Color.WHITE);
        txvSelected.setText(selected);
    }

    /** Create a new dialog for time picker */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case TIME_DIALOG_ID:
            return new TimePickerDialog(this, mTimeSetListener, pHour, pMinute, false);
        }
        return null;
    }
}