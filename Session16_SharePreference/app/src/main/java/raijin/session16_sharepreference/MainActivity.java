package raijin.session16_sharepreference;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String KEY_VALUE = "key_value";
    private static final String FILE_NAME = "1918";

    EditText editText;
    TextView textView;
    Button saveButton;
    Button showButton;

    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        addListener();

        sharedPreferences = this.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void init() {
        editText = (EditText) findViewById(R.id.edit_text);
        textView = (TextView) findViewById(R.id.text_view);
        saveButton = (Button) findViewById(R.id.save_button);
        showButton = (Button) findViewById(R.id.show_button);
    }

    private void addListener() {
        showButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_button:
                editor.putString(KEY_VALUE, editText.getText().toString());
                editor.commit();
                break;
            case R.id.show_button:
//                textView.setText(sharedPreferences.getString(KEY_VALUE, "Empty"));
                if (isConnected()) Toast.makeText(MainActivity.this,"Connected",Toast.LENGTH_LONG).show();
                else Toast.makeText(MainActivity.this,"Internet is unavailable",Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
}
