package vn.com.tma.frontdoor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.frontdoor.R;

public class LimitInputActivity extends Activity implements OnClickListener {
    private static final int CHOOSE_LONG_INTERVAL = 7643;

    private Button btnOKLimit;

    private EditText edtLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.limit_input);
        btnOKLimit = (Button) findViewById(R.id.btnLimit);
        edtLimit = (EditText) findViewById(R.id.edtLimit);
        btnOKLimit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.equals(btnOKLimit)) {

            if (edtLimit.getText().toString().equals("") || !edtLimit.getText().toString().matches("\\d+")) {
                edtLimit.setError("Please input valid data. Example: 1,2,3..");
                edtLimit.setFocusable(true);
                edtLimit.setFocusableInTouchMode(true);
                return;
            } else {
                Intent i = new Intent(getApplicationContext(), ChooseLongInterval.class);
                startActivityForResult(i, CHOOSE_LONG_INTERVAL);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_LONG_INTERVAL) {
            if (resultCode == ChooseLongInterval.RESULT_INTERVAL) {
                String numLimit = edtLimit.getText().toString();
                long numInterval = data.getLongExtra("numInterval", 8888);
                Intent i = getIntent();
                i.putExtra("limit", numLimit);
                i.putExtra("numInterval", numInterval);
                setResult(ChooseCallActivity.LIMIT_RESULT, i);
                finish();
            }
        }

    }

}
