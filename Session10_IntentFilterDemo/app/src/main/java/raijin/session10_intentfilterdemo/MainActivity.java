package raijin.session10_intentfilterdemo;

import android.content.Intent;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        TextView textView = (TextView) findViewById(R.id.content);
        textView.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
    }
}
