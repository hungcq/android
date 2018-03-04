package raijin.doitlater.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import io.realm.Realm;
import raijin.doitlater.R;
import raijin.doitlater.database.RealmHandle;
import raijin.doitlater.database.models.NoteModel;
import raijin.doitlater.fragments.AddReminderFragment;
import raijin.doitlater.managers.FragmentType;
import raijin.doitlater.managers.MyAlarmReceiver;
import raijin.doitlater.managers.ScreenManager;
import raijin.doitlater.utils.Utils;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private EditText titleEditText;
    private EditText detailEditText;
    private EditText locationEditText;
    private Button showOnMapButton;
    private Button btnPlay, btnRecord, btnStop;
    private LinearLayout locationLayout;
    private LinearLayout recordAudioLayout;
    private MediaRecorder recorder;
    private String outputFile = null;
    private RealmHandle realmHandle;
    private NoteModel noteModel;
    private TextView imageTextView;
    private ImageView image;
    private String imagePath;
    private RadioButton rbHigh, rbNormal, rbLow;
    private RadioGroup rbGroup;
    private ActionMode actionMode;
    //
    private View viewToast;
    private TextView txtContent;
    private Toast toast;
    //
    private static int RESULT_LOAD_IMAGE = 1;
    public static TextView tvReminder;
    public static ActivityType activityType;
    public static boolean checkCaller = false;


    public NoteActivity() {
        realmHandle = RealmHandle.getInst();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initialize();
        addListener();
        setData();
        setUpToolbar();
    }

    private void setUpToolbar() {
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.child_toolbar);
        setSupportActionBar(myChildToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        if (activityType == ActivityType.ADDNOTE) {
            ab.setTitle(R.string.activity_addnote_title);
        } else if (activityType == ActivityType.EDITNOTE) {
            ab.setTitle(R.string.activity_editnote_title);
        }
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void initialize() {

        if (getIntent().getIntExtra("widget", 0) == 1918) {
            activityType = ActivityType.EDITNOTE;
        }
        //
        viewToast = LayoutInflater.from(this).inflate(R.layout.custom_toast, null);
        txtContent = (TextView) viewToast.findViewById(R.id.txt_content);
        toast = new Toast(this);
        //
        btnPlay = (Button) findViewById(R.id.btn_Play);
        btnRecord = (Button) findViewById(R.id.btn_Record);
        btnStop = (Button) findViewById(R.id.btn_Stop);
        recordAudioLayout = (LinearLayout) findViewById(R.id.recordAudio_layout);
        titleEditText = (EditText) findViewById(R.id.title_edit_text);
        detailEditText = (EditText) findViewById(R.id.detail_edit_text);
        showOnMapButton = (Button) findViewById(R.id.show_on_map_button);
        locationLayout = (LinearLayout) findViewById(R.id.location_layout);
        locationEditText = (EditText) findViewById(R.id.location_edit_text);
        imageTextView = (TextView) findViewById(R.id.image_text);
        image = (ImageView) findViewById(R.id.img);
        tvReminder = (TextView) findViewById(R.id.tv_reminder);
        rbHigh = (RadioButton) findViewById(R.id.rb_priority_high);
        rbNormal = (RadioButton) findViewById(R.id.rb_priority_normal);
        rbLow = (RadioButton) findViewById(R.id.rb_priority_low);
        rbGroup = (RadioGroup) findViewById(R.id.rb_group);

        if (activityType == ActivityType.ADDNOTE) {
            noteModel = new NoteModel();
            noteModel.setID(realmHandle.getID());
            realmHandle.setRequestCodeAlarm(noteModel);
        } else if (activityType == ActivityType.EDITNOTE) {
            int id = getIntent().getExtras().getInt("ID");
            noteModel = realmHandle.getNodeByID(id);
            outputFile = noteModel.getPathOfAudio();
        }

        initRecord();

        btnPlay.setEnabled(false);
        btnStop.setEnabled(false);

        if (checkCaller) {
            overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
            checkCaller = false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_back_in, R.anim.trans_back_out);
    }

    private void setData() {

        if (ScreenManager.isOpeningMap) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                titleEditText.setText(bundle.getString("title"));
                detailEditText.setText(bundle.getString("detail"));
                String location = bundle.getString("location");
                if (location != null && !location.equals("")) {
                    locationEditText.setText(location);
                    locationLayout.setVisibility(View.VISIBLE);
                }
                int id = bundle.getInt("pathOfAudio");
                if (id != -1) {
                    displayAudioLayout();
                }

                int priority = bundle.getInt("priority");
                if (priority == 2) {
                    rbHigh.setChecked(true);
                } else if (priority == 1) {
                    rbNormal.setChecked(true);
                } else if (priority == 0) {
                    rbLow.setChecked(true);
                }

                imagePath = bundle.getString("image_path");
                if (imagePath != null && !imagePath.equals("")) {
                    image.setImageBitmap(Utils.decodeImageFile(imagePath));
                    imageTextView.setVisibility(View.GONE);
                }
                String dateReminder = bundle.getString("date_reminder");
                String timeReminder = bundle.getString("time_reminder");
                if (dateReminder != null && !dateReminder.equals("")) {
                    tvReminder.setText("Reminder: " + dateReminder + ", " + timeReminder);
                } else {
                    tvReminder.setText("Reminder: None");
                }
            }
            ScreenManager.isOpeningMap = false;
        } else {
            titleEditText.setText(noteModel.getTitle());
            detailEditText.setText(noteModel.getDetail());
            imagePath = noteModel.getImagePath();

            int priority = noteModel.getPriority();
            if (priority == 2) {
                rbHigh.setChecked(true);
            } else if (priority == 1) {
                rbNormal.setChecked(true);
            } else if (priority == 0) {
                rbLow.setChecked(true);
            }

            if (imagePath != null && !imagePath.equals("")) {
                image.setImageBitmap(Utils.decodeImageFile(imagePath));
                imageTextView.setVisibility(View.GONE);
            }
            if (noteModel.getDateReminder() != null && !noteModel.getDateReminder().equals("")) {
                tvReminder.setText("Reminder: " + noteModel.getDateReminder() + ", " + noteModel.getTimeReminder());
            } else {
                tvReminder.setText("Reminder: None");
            }
            if (noteModel.isHasAudio()) {
                displayAudioLayout();
            }
            if (noteModel.getLocation() != null && !noteModel.getLocation().equals("")) {
                locationEditText.setText(noteModel.getLocation());
                locationLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initRecord() {
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording" + noteModel.getID() + ".3gp";
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setOutputFile(outputFile);
    }

    private void addListener() {
        showOnMapButton.setOnClickListener(this);
        locationEditText.setOnClickListener(this);
        btnRecord.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        tvReminder.setOnClickListener(this);
        imageTextView.setOnClickListener(this);
        image.setOnClickListener(this);
        image.setOnLongClickListener(this);
        recordAudioLayout.setOnLongClickListener(this);
        if (activityType == ActivityType.ADDNOTE) {
            realmHandle.setHasNoAudio(noteModel);
        }
        rbLow.setOnClickListener(this);
        rbNormal.setOnClickListener(this);
        rbHigh.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_activity_menu, menu);
        return true;
    }

    private void saveData() throws IOException {
        if (activityType == ActivityType.ADDNOTE) {
            realmHandle.setPathOfAudio(noteModel, outputFile);
            noteModel.setTitle(titleEditText.getText().toString());
            noteModel.setDetail(detailEditText.getText().toString());
            noteModel.setLocation(locationEditText.getText().toString());
            noteModel.setCompleted(false);
            noteModel.setImagePath(imagePath);

            if (rbHigh.getId() == rbGroup.getCheckedRadioButtonId()) {
                noteModel.setPriority(2);
            } else if (rbNormal.getId() == rbGroup.getCheckedRadioButtonId()) {
                noteModel.setPriority(1);
            } else if (rbLow.getId() == rbGroup.getCheckedRadioButtonId()) {
                noteModel.setPriority(0);
            }

            if (!tvReminder.getText().equals("Reminder: None")) {
                setAlarmForNote(noteModel);
            }
            if (!titleEditText.getText().toString().equals("")) {
                realmHandle.addNoteToRealm(noteModel);
                backToMain();
            } else {
                showRemindToast();
            }

        } else if (activityType == ActivityType.EDITNOTE) {
            int priority = -1;
            if (rbHigh.getId() == rbGroup.getCheckedRadioButtonId()) {
                priority = 2;
            } else if (rbNormal.getId() == rbGroup.getCheckedRadioButtonId()) {
                priority = 1;
            } else if (rbLow.getId() == rbGroup.getCheckedRadioButtonId()) {
                priority = 0;
            }
            if (!tvReminder.getText().equals("Reminder: None")) {
                if (!titleEditText.getText().toString().equals("")) {
                    realmHandle.editNoteFromRealm(noteModel, titleEditText.getText().toString(),
                            detailEditText.getText().toString(), locationEditText.getText().toString(), imagePath,
                            Utils.getTimeReminder(tvReminder.getText() + ""), Utils.getDateReminder(tvReminder.getText() + ""),
                            priority);
                    setAlarmForNote(noteModel);
                    backToMain();
                } else {
                    showRemindToast();
                }
            } else {
                if (!titleEditText.getText().toString().equals("")) {
                    cancelAlarmForNote(noteModel);
                    realmHandle.editNoteFromRealm(noteModel, titleEditText.getText().toString(),
                            detailEditText.getText().toString(), locationEditText.getText().toString(), imagePath,
                            null, null, priority);
                    backToMain();
                } else {
                    showRemindToast();
                }
            }
        }
    }

    private void backToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.trans_back_in, R.anim.trans_back_out);
        finish();
    }

    private void showRemindToast() {
        txtContent.setText("Please fill in title field");
        txtContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.empt_icon, 0, 0, 0);
        toast.setView(viewToast);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    private void setAlarmForNote(NoteModel noteModel) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        noteModel.setTimeReminder(Utils.getTimeReminder(tvReminder.getText() + ""));
        noteModel.setDateReminder(Utils.getDateReminder(tvReminder.getText() + ""));
        realm.commitTransaction();

        Context context = NoteActivity.this.getApplicationContext();

        Calendar currentCalendar = new GregorianCalendar();
        currentCalendar.setTimeInMillis(System.currentTimeMillis());

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, Utils.getDayFromDateReminder(noteModel.getDateReminder()));
        calendar.set(Calendar.MONTH, Utils.getMonthFromDateReminder(noteModel.getDateReminder()) - 1);
        calendar.set(Calendar.YEAR, Utils.getYearFromDateReminder(noteModel.getDateReminder()));
        calendar.set(Calendar.HOUR_OF_DAY, Utils.getHourFromTimeReminder(noteModel.getTimeReminder()));
        calendar.set(Calendar.MINUTE, Utils.getMinuteFromTimeReminder(noteModel.getTimeReminder()));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Intent myIntent = new Intent(context, MyAlarmReceiver.class);
        myIntent.putExtra("title", noteModel.getTitle());
        myIntent.putExtra("detail", noteModel.getDetail());
        myIntent.putExtra("ID", noteModel.getRequestCodeAlarm());
        Bundle bundle = new Bundle();
        bundle.putInt("ID", noteModel.getID());
        myIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, noteModel.getRequestCodeAlarm(), myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        txtContent.setText("Alarm has been set at " + noteModel.getDateReminder()
                + ", " + noteModel.getTimeReminder());
        txtContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_small_alarmclock, 0, 0, 0);
        toast.setView(viewToast);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    private void cancelAlarmForNote(NoteModel noteModel) {
        Context context = NoteActivity.this.getApplicationContext();
        Intent myIntent = new Intent(context, MyAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, noteModel.getRequestCodeAlarm(), myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                try {
                    saveData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;

            case R.id.item_addnote_location:
                locationLayout.setVisibility(View.VISIBLE);
                return true;

            case R.id.item_addnote_record:
                displayAudioLayout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayAudioLayout() {
        recordAudioLayout.setVisibility(View.VISIBLE);
        if (noteModel.isHasAudio()) {
            btnPlay.setEnabled(true);
        } else {
            btnPlay.setEnabled(false);
        }
    }

    protected void startIntent(Uri data) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(data);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_on_map_button:
                if (locationEditText.getText().toString().isEmpty()) {
                    txtContent.setText("Please fill in the location");
                    txtContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_small_map, 0, 0, 0);
                    toast.setView(viewToast);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    String encodedData = Uri.encode(locationEditText.getText().toString());
                    Uri intentData = Uri.parse("google.navigation:q=" + encodedData + "&mode=d");
                    startIntent(intentData);
                }
                break;
            case R.id.location_edit_text:
                ScreenManager.isOpeningMap = true;
                Intent intent = new Intent(this, OpenMapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", titleEditText.getText().toString());
                bundle.putString("detail", detailEditText.getText().toString());
                bundle.putString("image_path", imagePath);

                int priority;
                if (rbHigh.getId() == rbGroup.getCheckedRadioButtonId()) {
                    priority = 2;
                } else if (rbNormal.getId() == rbGroup.getCheckedRadioButtonId()) {
                    priority = 1;
                } else {
                    priority = 0;
                }
                bundle.putInt("priority", priority);

                if (activityType == ActivityType.EDITNOTE) {
                    bundle.putInt("ID", noteModel.getID());
                }
                if (!tvReminder.getText().equals("Reminder: None")) {
                    bundle.putString("date_reminder", Utils.getDateReminder(tvReminder.getText() + ""));
                    bundle.putString("time_reminder", Utils.getTimeReminder(tvReminder.getText() + ""));
                }
                if (noteModel.isHasAudio()) {
                    bundle.putInt("pathOfAudio", noteModel.getID());
                } else {
                    bundle.putInt("pathOfAudio", -1);
                }
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btn_Record:
                txtContent.setText("Recording");
                txtContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_small_record, 0, 0, 0);
                toast.setView(viewToast);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                realmHandle.setHasAudio(noteModel);
                try {
                    recorder.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                recorder.start();
                btnRecord.setEnabled(false);
                if (!btnStop.isEnabled()) {
                    btnStop.setEnabled(true);
                }
                if (btnPlay.isEnabled()) {
                    btnPlay.setEnabled(false);
                }
                btnRecord.setEnabled(false);
                break;
            case R.id.btn_Play:
                txtContent.setText("Playing");
                txtContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_small_play, 0, 0, 0);
                toast.setView(viewToast);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                MediaPlayer player = new MediaPlayer();
                try {
                    player.setDataSource(outputFile);
                    Log.d("bug", "onClick: " + outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    player.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.start();
                if (btnStop.isEnabled()) {
                    btnStop.setEnabled(false);
                }
                break;
            case R.id.btn_Stop:
                txtContent.setText("Stopping");
                txtContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_small_stop, 0, 0, 0);
                toast.setView(viewToast);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                recorder.stop();
                recorder.release();
                recorder = null;
                btnStop.setEnabled(false);
                if (!btnPlay.isEnabled()) {
                    btnPlay.setEnabled(true);
                }
                break;
            case R.id.tv_reminder:
                FragmentManager fragmentManager = this.getSupportFragmentManager();
                if (fragmentManager != null) {
                    AddReminderFragment addReminderFragment = new AddReminderFragment();
                    addReminderFragment.show(fragmentManager, "");
                }
                break;
            case R.id.image_text:
                Intent intent1 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent1, RESULT_LOAD_IMAGE);
                break;
            case R.id.img:
                Intent intent2 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent2, RESULT_LOAD_IMAGE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imagePath = cursor.getString(columnIndex);
            cursor.close();

            image.setImageBitmap(Utils.decodeImageFile(imagePath));
            image.setVisibility(View.VISIBLE);
            imageTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img:
                image.setOnClickListener(null);
                if (actionMode != null) return false;
                actionMode = startSupportActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        mode.getMenuInflater().inflate(R.menu.context_delete_menu, menu);
                        mode.setTitle("Remove Image");
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        if (item.getItemId() == R.id.context_menu_delete) {
                            imagePath = null;
                            image.setVisibility(View.GONE);
                            imageTextView.setVisibility(View.VISIBLE);
                        }
                        mode.finish();
                        image.setOnClickListener(NoteActivity.this);
                        return true;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        image.setOnClickListener(NoteActivity.this);
                        actionMode = null;
                    }
                });
                break;
            case R.id.recordAudio_layout:
                if (actionMode != null) return false;
                actionMode = startSupportActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        mode.getMenuInflater().inflate(R.menu.context_delete_menu, menu);
                        mode.setTitle("Remove Record");
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        if (item.getItemId() == R.id.context_menu_delete) {
                            outputFile = null;
                            realmHandle.setHasNoAudio(noteModel);
                            recordAudioLayout.setVisibility(View.GONE);
                        }
                        mode.finish();
                        return true;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        actionMode = null;
                    }
                });
        }
        return false;
    }
}
