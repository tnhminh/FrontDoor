package vn.com.tma.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.tma.model.StateSensorVO;
import android.util.Log;

public class ConverUtil {

    public static String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
        InputStream in = entity.getContent();
        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n > 0) {
            byte[] b = new byte[4096];
            n = in.read(b);
            if (n > 0)
                out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    public static List<StateSensorVO> convertJSONtoListStateSensor(String JSONString) {
        try {
            List<StateSensorVO> listState = new ArrayList<StateSensorVO>();
            JSONArray jArr = new JSONArray(JSONString);
            int i = 0;
            while (i < jArr.length()) {
                JSONObject jObj = jArr.getJSONObject(i);
                int id = jObj.getInt("id");
                String date = jObj.getString("date");
                String time = jObj.getString("time");
                String state = new String("new");
                listState.add(new StateSensorVO(id, date, time, state));
                i++;
            }
            return listState;
        } catch (JSONException e) {
            Log.d("JSON Parser", e.getMessage());
        }
        return null;
    }

    public static String convertNumToDayOfWeek(int key) {
        switch (key) {
        case 1:

            return "Sunday";
        case 2:

            return "Monday";
        case 3:

            return "Tuesday";
        case 4:

            return "Webnesday";
        case 5:

            return "Thursday";
        case 6:

            return "Friday";
        case 7:

            return "Saturday";
        default:
            break;
        }
        return null;

    }

    public static int getDayOFWeek(String day) {
        SimpleDateFormat parserDate = new SimpleDateFormat("yyyy-MM-dd");
        int dayOfWeek = 0;
        Calendar c = Calendar.getInstance();
        Date d;
        try {
            d = parserDate.parse(day);
            c.setTime(d);
            dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            return dayOfWeek;

        } catch (ParseException e) {
            e.getMessage();
        }
        return 0;
    }

}