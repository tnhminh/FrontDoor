package vn.com.tma.task;

import java.text.ParseException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import vn.com.tma.connection.MySQLiteHelper;
import vn.com.tma.frontdoor.DismissActivity;
import vn.com.tma.frontdoor.IgnoreActivity;
import vn.com.tma.frontdoor.MainActivity;
import vn.com.tma.model.IgnoreRuleVO;
import vn.com.tma.model.AcceptRuleVO;
import vn.com.tma.model.StateSensorVO;
import vn.com.tma.util.ConverUtil;
import vn.com.tma.util.TimeConvertUtil;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.frontdoor.R;

public class StateTask extends AsyncTask<StateSensorVO, Void, List<StateSensorVO>> {

    private static final String NOTIFICATION_SERVICE = "notification";

    private MainActivity mainActivity;

    private MySQLiteHelper mysqlHelper;

    private String url = "";

    private String para;

    public StateTask(MainActivity context, String url, String para) {
        this.mainActivity = context;
        this.mysqlHelper = new MySQLiteHelper(mainActivity);
        this.url = url;
        this.para = para;
    }

    @Override
    protected List<StateSensorVO> doInBackground(StateSensorVO... params) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet(url + "/" + para);
        String text = null;
        try {

            HttpResponse response = httpClient.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();
            text = ConverUtil.getASCIIContentFromEntity(entity);
            // Parse JSON String to List State
            List<StateSensorVO> listState = ConverUtil.convertJSONtoListStateSensor(text);
            // Insert into database
            mysqlHelper.addListState(listState);

            return listState;
        } catch (Exception e) {

            e.getMessage();
        }
        return null;
    }

    public boolean checkAcceptItemIsCheckable(List<AcceptRuleVO> acceptList) {
        int i = 0;
        int k = 0;
        while (i < acceptList.size()) {
            if (acceptList.get(i).isChecked()) {
                k++;
            }
            i++;
        }
        if (k > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void checkShowNotification(StateSensorVO state) throws ParseException {
        String d = "2015-08-22"; // currentDay
        String t = "19:21"; // currentTime
        // Get Current Day of Week
        String currentDayOfWeek = ConverUtil.convertNumToDayOfWeek(ConverUtil.getDayOFWeek(d));
        long currentTime = TimeConvertUtil.getSecondTime(t);
        // Get DateItem to Date Of Week
        String dateItem = ConverUtil.convertNumToDayOfWeek(ConverUtil.getDayOFWeek(state.getDate()));
        long timeItem = TimeConvertUtil.getSecondTime(state.getTime());
        // Get Ignore and Accept List
        List<IgnoreRuleVO> ignoreList = mysqlHelper.getAllIgnore();
        List<AcceptRuleVO> acceptList = mysqlHelper.getAllNotificationRule();

        // if Accept rule is available
        if (checkAcceptItemIsCheckable(acceptList)) {
            // If Ignore rule is available
            if (!ignoreList.isEmpty()) {
                checkAcceptRuleWithIgnoreRule(acceptList, ignoreList, currentDayOfWeek, currentTime, dateItem, timeItem, state);
            }
            // if ignore rule is unavalable
            else {
                checkAcceptRule(acceptList, ignoreList, currentDayOfWeek, currentTime, dateItem, timeItem, state);
            }
        }
        // If Accept rule is unavalable
        else {
            // If Ignore rule is avalable
            if (!ignoreList.isEmpty()) {
                checkIgnoreRule(ignoreList, state, currentDayOfWeek, currentTime, dateItem, timeItem);
            }
            // if ignore rule is unavalable
            else {
                showNotification(state);
                updateStateAfterNotify(state);
            }
        }

    }

    public void checkAcceptRuleWithIgnoreRule(List<AcceptRuleVO> acceptList, List<IgnoreRuleVO> ignoreList, String currentDayOfWeek, long currentTime, String dateItem,
            long timeItem, StateSensorVO state) {
        int m = 0;
        while (m < acceptList.size()) {

            if (!acceptList.get(m).getFrom().equals("") && !acceptList.get(m).getTo().equals("")) {
                AcceptRuleVO notificationRule = acceptList.get(m);
                String acceptDayRule = notificationRule.getName().trim();
                long acceptTimeRuleFrom = TimeConvertUtil.getSecondTime(notificationRule.getFrom());
                long acceptTimeRuleTo = TimeConvertUtil.getSecondTime(notificationRule.getTo());
                boolean acceptIsChecked = notificationRule.isChecked();

                if (currentDayOfWeek.equals(acceptDayRule) && (currentTime >= acceptTimeRuleFrom && currentTime <= acceptTimeRuleTo) && dateItem.equals(acceptDayRule)
                        && (timeItem >= acceptTimeRuleFrom && timeItem <= acceptTimeRuleTo && acceptIsChecked)) {
                    checkIgnoreRule(ignoreList, state, currentDayOfWeek, currentTime, dateItem, timeItem);

                }
            }
            m++;
        }
    }

    public void checkAcceptRule(List<AcceptRuleVO> acceptList, List<IgnoreRuleVO> ignoreList, String currentDayOfWeek, long currentTime, String dateItem, long timeItem,
            StateSensorVO state) {
        int m = 0;
        while (m < acceptList.size()) {

            if (!acceptList.get(m).getFrom().equals("") && !acceptList.get(m).getTo().equals("")) {
                AcceptRuleVO notificationRule = acceptList.get(m);
                String acceptDayRule = notificationRule.getName().trim();
                long acceptTimeRuleFrom = TimeConvertUtil.getSecondTime(notificationRule.getFrom());
                long acceptTimeRuleTo = TimeConvertUtil.getSecondTime(notificationRule.getTo());
                boolean acceptIsChecked = notificationRule.isChecked();

                if (currentDayOfWeek.equals(acceptDayRule) && (currentTime >= acceptTimeRuleFrom && currentTime <= acceptTimeRuleTo) && dateItem.equals(acceptDayRule)
                        && (timeItem >= acceptTimeRuleFrom && timeItem <= acceptTimeRuleTo && acceptIsChecked)) {
                    showNotification(state);
                    updateStateAfterNotify(state);
                }
            }
            m++;
        }
    }

    public void checkIgnoreRule(List<IgnoreRuleVO> ignoreList, StateSensorVO state, String currentDayOfWeek, long currentTime, String dateItem, long timeItem) {
        if (ignoreList.isEmpty()) {
            // Show all notification when ignore list is null
            showNotification(state);
            updateStateAfterNotify(state);
        } else {
            // Show filter notification when ignore list is not null
            int i = 0;
            while (i < ignoreList.size()) {

                IgnoreRuleVO ignore = ignoreList.get(i);
                String ignoreDayRule = ignore.getOption().trim();

                long ignoreTimeRule = TimeConvertUtil.getSecondTime(ignore.getTime());

                // TH1 weekday
                if (ignoreDayRule.equals("onWeekDay") && isDayOfWeekDay(dateItem) && ignoreTimeRule == timeItem && isDayOfWeekDay(currentDayOfWeek)
                        && ignoreTimeRule == currentTime) {
                    break;
                }

                // Th2: Weekend
                else if (ignoreDayRule.equals("onWeekEnd") && isDayOfWeekend(dateItem) && ignoreTimeRule == timeItem && isDayOfWeekend(currentDayOfWeek)
                        && ignoreTimeRule == currentTime) {

                    break;
                }
                // TH3: On X day
                else if (ignoreDayRule.equals(currentDayOfWeek) && ignoreTimeRule == currentTime && ignoreTimeRule == timeItem && dateItem.equals(ignoreDayRule)) {
                    break;
                } else {
                    if (i == ignoreList.size() - 1) {
                        showNotification(state);
                        updateStateAfterNotify(state);
                    }
                }

                i++;
            }

        }
    }

    public boolean isDayOfWeekDay(String day) {
        if (day.equals("Monday") || day.equals("Tuesday") || day.equals("Webnesday") || day.equals("Thursday") || day.equals("Friday")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDayOfWeekend(String day) {
        if (day.equals("Saturday") || day.equals("Sunday")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onPostExecute(List<StateSensorVO> results) {
        if (results != null) {
            List<StateSensorVO> listAllState = mysqlHelper.getAllState();
            int i = 0;
            while (i < listAllState.size()) {
                if (listAllState.get(i).getState().equals("new")) {
                    StateSensorVO s = listAllState.get(i);
                    try {
                        checkShowNotification(s);
                    } catch (ParseException e) {
                        e.getMessage();
                    }
                }
                i++;
            }
        }
    }

    public void updateStateAfterNotify(StateSensorVO state) {
        state.setState("notified");
        mysqlHelper.updateState(state);
    }

    public void showNotification(StateSensorVO state) {

        NotificationManager notificationManager = (NotificationManager) mainActivity.getSystemService(NOTIFICATION_SERVICE);
        Intent dismissIntent = new Intent(mainActivity, DismissActivity.class);
        dismissIntent.putExtra("id", state.getId());

        PendingIntent pDismissIntent = PendingIntent.getActivity(mainActivity, state.getId(), dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent ignoreIntent = new Intent(mainActivity, IgnoreActivity.class);
        ignoreIntent.putExtra("time", state.getTime());
        ignoreIntent.putExtra("dayofweek", ConverUtil.getDayOFWeek(state.getDate()));

        PendingIntent pIgnoreIntent = PendingIntent.getActivity(mainActivity, state.getId(), ignoreIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification n = new Notification.Builder(mainActivity).setContentTitle("THE DOOR IS OPEN").setContentText("AT " + state.getTime() + " " + state.getDate())
                .setSmallIcon(R.drawable.bell_notify).addAction(R.drawable.dismiss_button, "Dismiss", pDismissIntent).addAction(R.drawable.ignore_button, "Ignore", pIgnoreIntent)
                .setStyle(new Notification.BigTextStyle().bigText("The door is opened at " + state.getTime() + " " + state.getDate())).setContentIntent(pDismissIntent).build();

        notificationManager.notify(state.getId(), n);

    }

    @Override
    public void onCancelled() {
        super.onCancelled();
        this.cancel(true);
    }

}
