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
        Intent intent = new Intent(this,DetailsActivity.class);
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.haiphong: {
                bundle.putString("name","Hải Phòng");
                bundle.putInt("imageResource",R.mipmap.rainy);
                bundle.putString("tem","29");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            case R.id.namdinh: {
                bundle.putString("name","Nam Định");
                bundle.putInt("imageResource",R.mipmap.partly_sunny);
                bundle.putString("tem","35");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            case R.id.hanam: {
                bundle.putString("name","Hà Nam");
                bundle.putInt("imageResource",R.mipmap.snowy);
                bundle.putString("tem","-10");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
        }
    }
}
