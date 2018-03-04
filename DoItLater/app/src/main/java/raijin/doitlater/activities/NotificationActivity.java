package raijin.doitlater.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import raijin.doitlater.R;
import raijin.doitlater.utils.Utils;

public class NotificationActivity extends AppCompatActivity {
    private TextView txtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        init();
    }

    private void init() {
        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.child_toolbar);
        setSupportActionBar(myChildToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        ab.setTitle("Notification");
        ab.setDisplayHomeAsUpEnabled(true);
        //
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("mypackage");
        txtContent = (TextView) findViewById(R.id.txt_content);
        txtContent.setText(bundle.getString("content"));

    }
}
