package raijin.myapplication;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Res2 res;
    TextView textView;

    @Override public Resources getResources() {
        if (res == null) {
            res = new Res2(super.getResources());
        }
        return res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text_view);
        Button colorButton = (Button) findViewById(R.id.color_button);
        Button textButton = (Button) findViewById(R.id.text_button);
        colorButton.setOnClickListener(this);
        textButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.color_button:
                textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.text_button:
                textView.setTextSize(getResources().getDimension(R.dimen.normalText));
                Res2.color = Color.RED;
                break;
            default:
                break;
        }
    }
}
