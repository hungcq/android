package raijin.chapter7_alertdialog;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void confirmDelete(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete").setMessage("Are you sure?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MainActivity.this, "OK Pressed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MainActivity.this, "Cancel Pressed", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
        // add neutral button (left-align button)
        // builder.setNeutralButton()

        // add icon
        // .setIcon(R.mipmap.ic_launcher)

        // create a list
        // .setItems()
        // .setAdapter()
        // .setSingleChoiceItems()
        // .setMultiChoiceItems()

        // custom layout
        // .setView()
        // must implement close method using hide()/dismiss()
    }
}
