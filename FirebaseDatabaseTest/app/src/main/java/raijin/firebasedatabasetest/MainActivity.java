package raijin.firebasedatabasetest;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private EditText nameEditText, numberEditText;
    private TextView avatarTextView;
    private Button avatarButton, saveButton, viewListButton;
    private Uri avatarUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fir-databasetest-16683.appspot.com");

        nameEditText = (EditText) findViewById(R.id.edit_text_name);
        numberEditText = (EditText) findViewById(R.id.edit_text_number);
        avatarTextView = (TextView) findViewById(R.id.text_view_avatar);
        avatarButton = (Button) findViewById(R.id.button_avatar);
        saveButton = (Button) findViewById(R.id.button_save);
        viewListButton = (Button) findViewById(R.id.button_view_list);

        avatarButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        viewListButton.setOnClickListener(this);
    }

    private void pickImage() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1918);
    }

    private void uploadAndSaveDataToDatabase() {
        if (avatarUri != null) {
            UploadTask uploadTask = storageReference.child("avatar/" + avatarUri.getLastPathSegment()).putFile(avatarUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    UserInfo userInfo = new UserInfo(nameEditText.getText().toString(), numberEditText.getText().toString(),
                            taskSnapshot.getDownloadUrl().toString());
                    databaseReference.push().setValue(userInfo);
                    Toast.makeText(MainActivity.this, "Info saved", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            UserInfo userInfo = new UserInfo(nameEditText.getText().toString(), numberEditText.getText().toString(), "");
            databaseReference.push().setValue(userInfo);
            Toast.makeText(this, "Info saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1918 && resultCode == RESULT_OK && null != data) {
            avatarUri = data.getData();
            avatarTextView.setText(avatarUri + "");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_avatar:
                pickImage();
                break;
            case R.id.button_save:
                uploadAndSaveDataToDatabase();
                break;
            case R.id.button_view_list:
                startActivity(new Intent(this, UserListActivity.class));
                break;
        }
    }
}
