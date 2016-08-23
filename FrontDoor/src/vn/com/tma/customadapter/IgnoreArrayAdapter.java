package vn.com.tma.customadapter;

import java.util.List;

import vn.com.tma.model.IgnoreRuleVO;
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

import com.example.frontdoor.R;

public class IgnoreArrayAdapter extends ArrayAdapter<IgnoreRuleVO> {
    private Activity context = null;

    private List<IgnoreRuleVO> listIgnore = null;

    public IgnoreArrayAdapter(Activity context, List<IgnoreRuleVO> objects) {
        super(context, R.layout.custom_list_ignore_row, objects);
        this.context = context;
        this.listIgnore = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(R.layout.custom_list_ignore_row, null);
        final ImageView imgIcon = (ImageView) row.findViewById(R.id.imvIgnore);
        TextView tvIgnoreItem = (TextView) row.findViewById(R.id.tvIgnoreRow);
        TextView tvTime = (TextView) row.findViewById(R.id.tvIgnoreSubRow);
        final CheckBox cbRow = (CheckBox) row.findViewById(R.id.cbIgnoreRow);
        tvIgnoreItem.setText(listIgnore.get(position).getOption());
        tvTime.setText(listIgnore.get(position).getTime());
        cbRowChecked(imgIcon, cbRow);
        cbRow.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbRow.isChecked()) {
                    imgIcon.setImageResource(R.drawable.ignore_rule_green);
                } else {
                    imgIcon.setImageResource(R.drawable.ignore_rule);

                }
            }
        });
        return row;
    }

    private void cbRowChecked(final ImageView imgIcon, final CheckBox cbRow) {
        if (cbRow.isChecked()) {
            imgIcon.setImageResource(R.drawable.ignore_rule_green);
        } else {
            imgIcon.setImageResource(R.drawable.ignore_rule);

        }
    }
}
