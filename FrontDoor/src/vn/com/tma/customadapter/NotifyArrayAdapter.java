package vn.com.tma.customadapter;

import java.util.List;

import vn.com.tma.connection.MySQLiteHelper;
import vn.com.tma.model.AcceptRuleVO;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontdoor.R;

public class NotifyArrayAdapter extends ArrayAdapter<AcceptRuleVO> {
    private Activity context;

    private List<AcceptRuleVO> listNotifyOptions = null;

    private CheckBox cbDisplay;

    private TextView txvDisplayTo;

    private TextView txvDisplayFrom;

    private MySQLiteHelper m_MysqlHelper = null;

    private TextView txvName;

    private TextView txvLine;

    public NotifyArrayAdapter(Activity context, List<AcceptRuleVO> objects) {
        super(context, R.layout.custom_list_notify_row, objects);
        this.context = context;
        this.listNotifyOptions = objects;
        m_MysqlHelper = new MySQLiteHelper(context);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(R.layout.custom_list_notify_row, null);
        txvName = (TextView) row.findViewById(R.id.txvRowName);
        txvDisplayFrom = (TextView) row.findViewById(R.id.txvFrom);
        txvDisplayTo = (TextView) row.findViewById(R.id.txvTo);
        cbDisplay = (CheckBox) row.findViewById(R.id.cbRow);
        final ImageView imgView = (ImageView) row.findViewById(R.id.imgCustom);
        txvLine = (TextView) row.findViewById(R.id.txvLine);
        autoChangeImageWhenChecked(cbDisplay, imgView);
        cbDisplay.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imgView.setImageResource(R.drawable.time_green);
                    AcceptRuleVO no = listNotifyOptions.get(position);
                    no.setChecked(true);
                    AcceptRuleVO noRule = new AcceptRuleVO(position + 1, no.getName(), no.getFrom(), no.getTo(), no.isChecked());
                    m_MysqlHelper.updateNotification(noRule);
                    Toast.makeText(context, "Accept Rule at " + noRule.getName() + " from " + noRule.getFrom() + " to " + noRule.getTo() + " was added into Rule completely",
                            (int) 0.3).show();
                } else {
                    imgView.setImageResource(R.drawable.time_while);
                    AcceptRuleVO no = listNotifyOptions.get(position);
                    no.setChecked(false);
                    AcceptRuleVO noRule = new AcceptRuleVO(position + 1, no.getName(), no.getFrom(), no.getTo(), no.isChecked());
                    m_MysqlHelper.updateNotification(noRule);
                    Toast.makeText(context, "Accept Rule at " + noRule.getName() + " from " + noRule.getFrom() + " to " + noRule.getTo() + " was released from Rule completely",
                            (int) 0.3).show();
                }
            }
        });

        txvName.setText(listNotifyOptions.get(position).getName());
        txvDisplayFrom.setText(listNotifyOptions.get(position).getFrom());
        txvDisplayTo.setText(listNotifyOptions.get(position).getTo());
        cbDisplay.setChecked(listNotifyOptions.get(position).isChecked());
        autoCheckedWhenTrue(position, cbDisplay);
        autoAddLineBetweenFromTo(txvDisplayTo, txvLine);
        autoInvisibleIfFromToIsNull(txvDisplayFrom, txvDisplayTo, cbDisplay);
        return row;
    }

    private void autoInvisibleIfFromToIsNull(TextView txvFrom, TextView txvTo, CheckBox cb) {
        if (txvFrom.getText().toString().equals("") || txvTo.getText().toString().equals("")) {
            cb.setEnabled(false);
        } else {
            cb.setEnabled(true);

        }
    }

    private void autoAddLineBetweenFromTo(TextView txvDisplayTo, TextView txvLine) {
        if (!txvDisplayTo.getText().toString().equals("")) {
            txvLine.setText("-");
        }
    }

    private void autoCheckedWhenTrue(int position, CheckBox cbDisplay) {
        if (listNotifyOptions.get(position).isChecked()) {
            cbDisplay.setChecked(true);
        } else {
            cbDisplay.setChecked(false);
        }
    }

    private void autoChangeImageWhenChecked(CheckBox cbDisplay, ImageView imgView) {
        if (cbDisplay.isChecked()) {
            imgView.setImageResource(R.drawable.time_green);
        } else {
            imgView.setImageResource(R.drawable.time_while);
        }
    }

}
