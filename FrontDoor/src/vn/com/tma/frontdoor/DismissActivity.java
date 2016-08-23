package vn.com.tma.frontdoor;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;

import com.example.frontdoor.R;

public class DismissActivity extends Activity {
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dismiss_activity);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        Intent i = getIntent();
        int id = i.getIntExtra("id", 999);
        notificationManager.cancel(id);
        finish();

    };
}
