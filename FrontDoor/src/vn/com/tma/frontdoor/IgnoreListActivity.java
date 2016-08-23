package vn.com.tma.frontdoor;

import java.util.ArrayList;
import java.util.List;

import vn.com.tma.connection.MySQLiteHelper;
import vn.com.tma.customadapter.IgnoreArrayAdapter;
import vn.com.tma.model.IgnoreRuleVO;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.frontdoor.R;

public class IgnoreListActivity extends Activity implements OnClickListener, OnItemLongClickListener {
    private List<IgnoreRuleVO> ignoreList = new ArrayList<IgnoreRuleVO>();

    private IgnoreArrayAdapter adapter = null;

    private ListView lvIgnoreList;

    private Button btnClearIgnoreList;

    private MySQLiteHelper m_mysqlHelper = new MySQLiteHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ignore_list_db);
        btnClearIgnoreList = (Button) findViewById(R.id.btnClearIgnoreList);
        btnClearIgnoreList.setOnClickListener(this);
        lvIgnoreList = (ListView) findViewById(R.id.lvIgnoreList);
        ignoreList = m_mysqlHelper.getAllIgnore();
        adapter = new IgnoreArrayAdapter(this, ignoreList);
        lvIgnoreList.setAdapter(adapter);
        lvIgnoreList.setOnItemLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnClearIgnoreList)) {
            m_mysqlHelper.cleanAllIgnore();
            m_mysqlHelper.resetIDOnTable();
            ignoreList = m_mysqlHelper.getAllIgnore();
            adapter = new IgnoreArrayAdapter(this, ignoreList);
            lvIgnoreList.setAdapter(adapter);
            adapter.setNotifyOnChange(true);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {

        AlertDialog.Builder b = new AlertDialog.Builder(IgnoreListActivity.this);
        b.setTitle("Warning");
        b.setMessage("Do you really want to delete this Rule???");
        b.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        b.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    IgnoreRuleVO ignore = ignoreList.get(position);
                    int id = m_mysqlHelper.deleteIgnore(ignore);
                    Toast.makeText(getApplicationContext(), "A Rule Option " + ignore.getOption() + "at " + ignore.getTime() + " was DELETED from the list", Toast.LENGTH_SHORT)
                            .show();
                    ignoreList.remove(position);
                    adapter.setNotifyOnChange(true);
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        b.create().show();
        return false;

    }

}
