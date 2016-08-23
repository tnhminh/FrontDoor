package vn.com.tma.frontdoor;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import vn.com.tma.connection.MySQLiteHelper;
import vn.com.tma.customadapter.ListDBArrayAdapter;
import vn.com.tma.model.StateSensorVO;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.frontdoor.R;

public class DatabaseActivity extends Activity {
    private MySQLiteHelper mysqlHelper = new MySQLiteHelper(this);

    private EditText edtResult;

    private ListView lv;

    private Button btnClearData;

    private ListDBArrayAdapter adapter = null;

    private List<StateSensorVO> listState = new ArrayList<StateSensorVO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_activity);
        edtResult = (EditText) findViewById(R.id.edtResult);
        lv = (ListView) findViewById(R.id.lvSensor);
        btnClearData = (Button) findViewById(R.id.btnClearData);
        btnClearData.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mysqlHelper.cleanAllState();
                loadDataOnListView();
                adapter.setNotifyOnChange(true);
                adapter.notifyDataSetChanged();
            }
        });
        loadDataOnListView();
        edtResult.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                String search = cs.toString();
                List<StateSensorVO> stateList = mysqlHelper.getAllState();
                List<StateSensorVO> newList = new ArrayList<StateSensorVO>();
                int i = 0;
                while (i < stateList.size()) {
                    if (stateList.get(i).getDate().indexOf(search) != -1 || stateList.get(i).getTime().indexOf(search) != -1) {
                        newList.add(stateList.get(i));
                    }
                    i++;
                }
                adapter = new ListDBArrayAdapter(DatabaseActivity.this, newList);
                lv.setAdapter(adapter);
                adapter.setNotifyOnChange(true);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void loadDataOnListView() {
        listState = mysqlHelper.getAllState();
        if (!listState.isEmpty()) {
            adapter = new ListDBArrayAdapter(this, listState);
            lv.setAdapter(adapter);
        } else {
            adapter = new ListDBArrayAdapter(this, listState);
            lv.setAdapter(adapter);
            Toast.makeText(getApplicationContext(), "Database is empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void showTextStateSensor(ListIterator<StateSensorVO> liteState) {
        String allState = "";
        while (liteState.hasNext()) {
            StateSensorVO s = liteState.next();
            allState = allState + "ID=" + s.getId() + "|" + "DATE=" + s.getDate() + "|" + "TIME=" + s.getTime() + "|" + "STATE=" + s.getState() + "|" + "\n";
            Log.d("String Result", allState);
        }
        edtResult.setText(allState);
    }

    public void insertStateSensor() {
        StateSensorVO s1 = new StateSensorVO("6-August", "7:54", "open");
        StateSensorVO s2 = new StateSensorVO("7-August", "6:54", "open");
        StateSensorVO s3 = new StateSensorVO("8-August", "5:54", "open");
        StateSensorVO s4 = new StateSensorVO("9-August", "2:54", "open");
        mysqlHelper.addState(s1);
        mysqlHelper.addState(s2);
        mysqlHelper.addState(s3);
        mysqlHelper.addState(s4);
    }
}
