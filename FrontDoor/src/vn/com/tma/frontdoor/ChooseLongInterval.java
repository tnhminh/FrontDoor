package vn.com.tma.frontdoor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.frontdoor.R;

public class ChooseLongInterval extends Activity {

    private static final String NUM_INTERVAL = "numInterval";

    public static final int RESULT_INTERVAL = 1773;

    private EditText edt;

    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_long_interval);
        edt = (EditText) findViewById(R.id.edtChooseLong);
        btnOk = (Button) findViewById(R.id.btnChooseInterval);
        btnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (edt.getText().toString().equals("") || !edt.getText().toString().matches("\\d+")) {

                    edt.setError("Please input valid data. Example: 1000, 2000...");
                    edt.setFocusable(true);
                    edt.setFocusableInTouchMode(true);
                    return;
                } else {
                    Intent i = getIntent();
                    i.putExtra(NUM_INTERVAL, Long.parseLong(edt.getText().toString()));
                    setResult(RESULT_INTERVAL, i);
                    finish();
                }
            }
        });

    }
}
