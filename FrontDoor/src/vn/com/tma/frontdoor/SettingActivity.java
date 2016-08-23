package vn.com.tma.frontdoor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.frontdoor.R;

public class SettingActivity extends Activity {
    private Button btnNotify;

    private Button btnSave;

    private Button btnBack;

    private CheckBox cbTurnOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_gui);
        btnNotify = (Button) findViewById(R.id.btnNotificationChoice);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnBack = (Button) findViewById(R.id.btnBack);
        cbTurnOn = (CheckBox) findViewById(R.id.cbTurnOn);
        btnNotify.setEnabled(false);
        cbTurnOn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    btnNotify.setEnabled(true);

                } else {
                    btnNotify.setEnabled(false);

                }
            }
        });

        btnSave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnNotify.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intentIgnore = new Intent(getApplicationContext(), NotifyActivity.class);
                startActivity(intentIgnore);

            }
        });
    }

}
