package vn.com.tma.frontdoor;

import java.util.TimerTask;

import vn.com.tma.connection.MySQLiteHelper;
import vn.com.tma.task.StateTask;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.frontdoor.R;

public class MainActivity extends Activity implements OnClickListener {

    private static final String LIMIT = "Limit";

    private static final String NEWER = "Newer";

    private static final String NUM_LIMIT = "numlimit";

    private static final String CHOOSE_LIMIT = "chooseLimit";

    private static final String NUM_INTERVAL = "numInterval";

    private static final String CHOOSE_NEWER = "chooseNewer";

    private static final int CHOOSE_CALL = 1091;

    private static final String NEWER_STATE = "newerState";

    private static final String URL = "http://192.168.92.105:8080/RESTService/rest/sensor/showList";

    private StateTask m_Task;

    private MySQLiteHelper m_mysqlHelper = new MySQLiteHelper(this);

    private Button btnSetting;

    private Button btnShowListLog;

    private Button btnShowIgnoreList;

    private Button btnExit;

    private RadioButton rbNewer;

    private RadioButton rbLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Call new intent choose call
        Intent i = new Intent(getApplicationContext(), ChooseCallActivity.class);
        startActivityForResult(i, CHOOSE_CALL);
        btnSetting = (Button) findViewById(R.id.btnSetting);
        btnShowListLog = (Button) findViewById(R.id.btnShowListLog);
        btnShowIgnoreList = (Button) findViewById(R.id.btnShowIgnoreList);
        rbNewer = (RadioButton) findViewById(R.id.rbNewer);
        rbLimit = (RadioButton) findViewById(R.id.rbLimit);
        btnExit = (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(this);
        rbLimit.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnShowListLog.setOnClickListener(this);
        btnShowIgnoreList.setOnClickListener(this);
    }

    public void startSync(final MainActivity context, final String url, final String para, long interval) {
        final Handler handler = new Handler();
        vn.com.tma.task.Timer timer = new vn.com.tma.task.Timer(interval);
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            m_Task = new StateTask(context, url, para);
                            m_Task.execute();
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, interval);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnShowListLog)) {
            if (!m_mysqlHelper.getAllState().isEmpty()) {
                Intent i = new Intent(getApplicationContext(), DatabaseActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Database is empty", Toast.LENGTH_SHORT).show();
            }
        }

        if (v.equals(btnSetting)) {
            Intent intentSetting = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intentSetting);
        }

        if (v.equals(btnShowIgnoreList)) {
            if (!m_mysqlHelper.getAllIgnore().isEmpty()) {
                Intent i = new Intent(getApplicationContext(), IgnoreListActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Ignore List Database is empty", Toast.LENGTH_SHORT).show();
            }
        }

        if (v.equals(btnExit)) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            System.exit(0);
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_CALL) {
            if (resultCode == ChooseCallActivity.NEWER_RESULT) {
                String choosen = data.getStringExtra(CHOOSE_NEWER);
                long interval = data.getLongExtra(NUM_INTERVAL, 7777);
                if (choosen.equals(NEWER)) {
                    rbNewer.setChecked(true);
                    rbLimit.setEnabled(false);
                    if (m_Task != null) {
                        if (m_Task.isCancelled() != true) {
                            m_Task.onCancelled();
                            startSync(this, URL, NEWER_STATE, interval);
                        } else {
                            startSync(this, URL, NEWER_STATE, interval);
                        }
                    } else {
                        startSync(this, URL, NEWER_STATE, interval);
                    }
                }
            } else if (resultCode == ChooseCallActivity.LIMIT_RESULT_TWO) {
                String choosen = data.getStringExtra(CHOOSE_LIMIT);
                String limit = data.getStringExtra(NUM_LIMIT);
                long interval = data.getLongExtra(NUM_INTERVAL, 7777);
                if (choosen.equals(LIMIT)) {
                    rbLimit.setText("Limit: " + limit);
                    rbLimit.setChecked(true);
                    rbNewer.setEnabled(false);
                    if (m_Task != null) {
                        if (m_Task.isCancelled() != true) {
                            m_Task.onCancelled();
                            startSync(this, URL, limit, interval);
                        } else {
                            startSync(this, URL, limit, interval);
                        }
                    } else {
                        startSync(this, URL, limit, interval);
                    }
                }

            }
        }
    }
}
