package raijin.session22_firebasecloudmessage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NotiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
