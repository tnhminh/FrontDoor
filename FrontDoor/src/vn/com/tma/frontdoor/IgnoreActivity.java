package vn.com.tma.frontdoor;

import java.util.ArrayList;
import java.util.List;

import vn.com.tma.connection.MySQLiteHelper;
import vn.com.tma.model.IgnoreRuleVO;
import vn.com.tma.util.ConverUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.frontdoor.R;

public class IgnoreActivity extends Activity implements OnItemClickListener {

    private List<String> ignoreList = new ArrayList<String>();

    private ListView lvIgnore;

    private MySQLiteHelper m_mysqlHelper = new MySQLiteHelper(this);

    private ArrayAdapter<String> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ignore_activity);
        Intent i = getIntent();
        String time = i.getStringExtra("time");
        int dayOfWeek = i.getIntExtra("dayofweek", 999);
        ignoreList.add("Ignore On " + ConverUtil.convertNumToDayOfWeek(dayOfWeek) + " at " + time);
        ignoreList.add("Ignore On Weekday at " + time);
        ignoreList.add("Ignore On Weekend at " + time);
        lvIgnore = (ListView) findViewById(R.id.lvIgnore);
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, ignoreList);
        lvIgnore.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvIgnore.setAdapter(adapter);
        lvIgnore.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (ignoreList.get(position).indexOf("Weekday") != -1) {
            String ignoreString = ignoreList.get(position);
            IgnoreRuleVO ign = new IgnoreRuleVO(0, "onWeekDay", ignoreString.substring(ignoreString.indexOf("t") + 1).trim());
            m_mysqlHelper.addIgnore(ign);
            finish();
        } else if (ignoreList.get(position).indexOf("Weekend") != -1) {
            String ignoreString = ignoreList.get(position);
            IgnoreRuleVO ign = new IgnoreRuleVO(0, "onWeekEnd", ignoreString.substring(ignoreString.indexOf("t") + 1).trim());
            m_mysqlHelper.addIgnore(ign);
            finish();
        } else {
            String ignoreString = ignoreList.get(position);
            IgnoreRuleVO ign = new IgnoreRuleVO(0, ignoreString.substring(ignoreString.indexOf("On") + 2, ignoreString.lastIndexOf("at")).trim(), ignoreString.substring(
                    ignoreString.lastIndexOf("t") + 1).trim());
            m_mysqlHelper.addIgnore(ign);
            finish();
        }
    }
}
