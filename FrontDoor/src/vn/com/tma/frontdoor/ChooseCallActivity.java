package vn.com.tma.frontdoor;

import com.example.frontdoor.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChooseCallActivity extends Activity implements OnClickListener {
    private static final String CHOOSE_NEWER = "chooseNewer";

    private static final String NUM_INTERVAL = "numInterval";

    private static final String NUM_LIMIT = "numlimit";

    private static final String CHOOSE_LIMIT = "chooseLimit";

    private static final int CHOOSE_LONG_INTERVAL = 2228;

    public static final int LIMIT_RESULT_TWO = 569;

    public static final int LIMIT_RESULT = 568;

    private static final int CLICK_LIMIT = 1092;

    public static final int NEWER_RESULT = 567;

    private Button btnNewer;

    private Button btnLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_call);
        btnLimit = (Button) findViewById(R.id.btnLimit);
        btnNewer = (Button) findViewById(R.id.btnNewer);
        btnLimit.setOnClickListener(this);
        btnNewer.setOnClickListener(this);
        setFinishOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnNewer)) {
            Intent iChooseLong = new Intent(getApplicationContext(), ChooseLongInterval.class);
            startActivityForResult(iChooseLong, CHOOSE_LONG_INTERVAL);
        }
        if (v.equals(btnLimit)) {
            Intent i = new Intent(getApplicationContext(), LimitInputActivity.class);
            startActivityForResult(i, CLICK_LIMIT);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CLICK_LIMIT) {
            if (resultCode == LIMIT_RESULT) {
                String numLimit = data.getStringExtra("limit").trim();
                long numInterval = data.getLongExtra(NUM_INTERVAL, 8888);
                Intent i = getIntent();
                i.putExtra(CHOOSE_LIMIT, "Limit");
                i.putExtra(NUM_LIMIT, numLimit);
                i.putExtra(NUM_INTERVAL, numInterval);
                setResult(LIMIT_RESULT_TWO, i);
                finish();

            }
        }

        if (requestCode == CHOOSE_LONG_INTERVAL) {
            if (resultCode == ChooseLongInterval.RESULT_INTERVAL) {
                long numInterval = data.getLongExtra(NUM_INTERVAL, 8888);
                Intent i = getIntent();
                i.putExtra(CHOOSE_NEWER, "Newer");
                i.putExtra(NUM_INTERVAL, numInterval);
                setResult(NEWER_RESULT, i);
                finish();

            }
        }
    }
}
