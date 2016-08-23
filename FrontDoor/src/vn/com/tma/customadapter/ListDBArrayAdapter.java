package vn.com.tma.customadapter;

import java.util.List;

import vn.com.tma.model.StateSensorVO;
import vn.com.tma.util.ConverUtil;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.frontdoor.R;

public class ListDBArrayAdapter extends ArrayAdapter<StateSensorVO> {
    private Activity context = null;

    private List<StateSensorVO> listState = null;

    public ListDBArrayAdapter(Activity context, List<StateSensorVO> objects) {
        super(context, R.layout.custom_list_db, objects);
        this.context = context;
        this.listState = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(R.layout.custom_list_db, null);
        final ImageView imgIcon = (ImageView) row.findViewById(R.id.imgDB);
        TextView tvStateItem = (TextView) row.findViewById(R.id.tvItemDB);
        TextView tvTime = (TextView) row.findViewById(R.id.tvTimeDB);
        TextView tvId = (TextView) row.findViewById(R.id.tvID);
        tvStateItem.setText(listState.get(position).getDate() + " " + ConverUtil.convertNumToDayOfWeek(ConverUtil.getDayOFWeek(listState.get(position).getDate())));
        tvTime.setText(listState.get(position).getTime());
        tvId.setText(listState.get(position).getId() + "");
        if (listState.get(position).getState().equals("new")) {
            imgIcon.setImageResource(R.drawable.new_mail);
            tvStateItem.setBackgroundColor(android.R.color.holo_blue_bright);
        } else {
            imgIcon.setImageResource(R.drawable.old_mail);
        }
        return row;

    }
}