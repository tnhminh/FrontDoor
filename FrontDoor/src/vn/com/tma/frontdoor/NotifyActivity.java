package vn.com.tma.frontdoor;

import java.util.ArrayList;
import java.util.List;

import vn.com.tma.connection.MySQLiteHelper;
import vn.com.tma.customadapter.NotifyArrayAdapter;
import vn.com.tma.model.AcceptRuleVO;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.frontdoor.R;

public class NotifyActivity extends Activity implements OnClickListener {
    private static final int SEND_TIMEPICKER_FROM = 8888;

    private static final int SEND_TIMEPICKER_TO = 8889;

    private static final int TIME_PICKER_FROM = 452;

    private static final int TIME_PICKER_TO = 453;

    private MySQLiteHelper m_MysqlHelper = new MySQLiteHelper(this);

    private Button btnBack;

    private ListView listView;

    private List<AcceptRuleVO> notifys = new ArrayList<AcceptRuleVO>();

    private NotifyArrayAdapter notifyArrayAdapter;

    private View v;

    private TextView txvSubtextFrom;

    private TextView txvSubtextTo;

    private int positionResult = 0;

    private String resultTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_gui);
        btnBack = (Button) findViewById(R.id.btnBackNotify);
        btnBack.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listNotifyOption);
        setUpListView();

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                choosePicker(position, TIME_PICKER_FROM);
            }

        });

        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                choosePicker(position, TIME_PICKER_TO);
                return false;
            }
        });

    }

    private void choosePicker(int position, int choosen) {
        switch (choosen) {
        case TIME_PICKER_FROM:
            Intent intentTimePickerFrom = new Intent(getApplicationContext(), TimePickerActivityFrom.class);
            intentTimePickerFrom.putExtra("Selected", notifys.get(position).getName());
            intentTimePickerFrom.putExtra("Position", position);
            TextView txvSubRowSendFrom = (TextView) listView.getChildAt(position).findViewById(R.id.txvFrom);
            intentTimePickerFrom.putExtra("CurrentTime", txvSubRowSendFrom.getText().toString());

            startActivityForResult(intentTimePickerFrom, SEND_TIMEPICKER_FROM);
            break;

        case TIME_PICKER_TO:
            Intent intentTimePickerTo = new Intent(getApplicationContext(), TimePickerActivityTo.class);
            intentTimePickerTo.putExtra("Selected", notifys.get(position).getName());
            intentTimePickerTo.putExtra("Position", position);
            TextView txvSubRowSendTo = (TextView) listView.getChildAt(position).findViewById(R.id.txvTo);
            intentTimePickerTo.putExtra("CurrentTime", txvSubRowSendTo.getText().toString());

            startActivityForResult(intentTimePickerTo, SEND_TIMEPICKER_TO);
            break;

        default:
            break;
        }
    }

    private void createNotificationObject() {
        m_MysqlHelper.addNotificationRule(new AcceptRuleVO(1, "Monday", "", "", false));
        m_MysqlHelper.addNotificationRule(new AcceptRuleVO(2, "Tuesday", "", "", false));
        m_MysqlHelper.addNotificationRule(new AcceptRuleVO(3, "Webnesday", "", "", false));
        m_MysqlHelper.addNotificationRule(new AcceptRuleVO(4, "Thursday", "", "", false));
        m_MysqlHelper.addNotificationRule(new AcceptRuleVO(5, "Friday", "", "", false));
        m_MysqlHelper.addNotificationRule(new AcceptRuleVO(6, "Saturday", "", "", false));
        m_MysqlHelper.addNotificationRule(new AcceptRuleVO(7, "Sunday", "", "", false));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {

            if (requestCode == SEND_TIMEPICKER_FROM) {
                if (resultCode == TimePickerActivityFrom.TIMEPICKER_RESULT) {
                    resultTime = data.getStringExtra("Result");
                    positionResult = data.getIntExtra("Return", 88);
                    v = listView.getChildAt(positionResult);
                    txvSubtextFrom = (TextView) v.findViewById(R.id.txvFrom);
                    txvSubtextFrom.setText(resultTime);

                    // Update Notification Rule
                    AcceptRuleVO noRu = notifys.get(positionResult);
                    AcceptRuleVO no = new AcceptRuleVO(positionResult + 1, noRu.getName(), txvSubtextFrom.getText().toString(), noRu.getTo(), noRu.isChecked());
                    m_MysqlHelper.updateNotification(no);

                    // Load and update new DB to listview
                    setUpListView();

                }
            }
            if (requestCode == SEND_TIMEPICKER_TO) {
                if (resultCode == TimePickerActivityTo.TIMEPICKER_RESULT) {
                    resultTime = data.getStringExtra("Result");
                    positionResult = data.getIntExtra("Return", 88);
                    v = listView.getChildAt(positionResult);
                    txvSubtextTo = (TextView) v.findViewById(R.id.txvTo);
                    txvSubtextTo.setText(resultTime);

                    // Update Notification Rule
                    AcceptRuleVO noRu = notifys.get(positionResult);
                    AcceptRuleVO no = new AcceptRuleVO(positionResult + 1, noRu.getName(), noRu.getFrom(), txvSubtextTo.getText().toString(), noRu.isChecked());
                    m_MysqlHelper.updateNotification(no);

                    // Load and update new DB to listview
                    setUpListView();

                    if (!txvSubtextTo.getText().toString().equals("")) {
                        TextView txvLine = (TextView) v.findViewById(R.id.txvLine);
                        txvLine.setText("-");
                    }
                }
            }
        }
    }

    private void setUpListView() {
        notifys = m_MysqlHelper.getAllNotificationRule();
        notifyArrayAdapter = new NotifyArrayAdapter(this, notifys);
        listView.setAdapter(notifyArrayAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            finish();
        }
    }

}
