package raijin.session4_hw;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout haiphongLayout;
    private LinearLayout namdinhLayout;
    private LinearLayout hanamLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        haiphongLayout = (LinearLayout) findViewById(R.id.haiphong);
        namdinhLayout = (LinearLayout) findViewById(R.id.namdinh);
        hanamLayout = (LinearLayout) findViewById(R.id.hanam);
        haiphongLayout.setOnClickListener(this);
        namdinhLayout.setOnClickListener(this);
        hanamLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent haiphongIntent = new Intent(this,HaiPhongActivity.class);
        Intent namdinhIntent = new Intent(this,NamDinhActivity.class);
        Intent hanamIntent = new Intent(this,HaNamActivity.class);
        switch (v.getId()) {
            case R.id.haiphong: {
                startActivity(haiphongIntent);
                break;
            }
            case R.id.namdinh: {
                startActivity(namdinhIntent);
                break;
            }
            case R.id.hanam: {
                startActivity(hanamIntent);
                break;
            }
        }
    }
}
