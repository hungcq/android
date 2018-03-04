package raijin.doitlater.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import raijin.doitlater.R;
import raijin.doitlater.activities.NoteActivity;
import raijin.doitlater.managers.ScreenManager;
import raijin.doitlater.utils.Utils;

/**
 * Created by Qk Lahpita on 8/28/2016.
 */
public class AddReminderFragment extends DialogFragment implements View.OnClickListener {

    private TextView etDate, etTime;
    private Button btDelete, btSave;
    private Activity activity;

    int day = 0;
    int month = 0;
    int year = 0;
    int hour = 0;
    int minute = 0;

    public static AddReminderFragment create(ScreenManager screenManager) {
        AddReminderFragment addReminderFragment = new AddReminderFragment();
        return addReminderFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_reminder, container, false);
        etDate = (TextView) view.findViewById(R.id.et_reminder_pick_date);
        etTime = (TextView) view.findViewById(R.id.et_reminder_pick_time);
        btDelete = (Button) view.findViewById(R.id.bt_reminder_delete);
        btSave = (Button) view.findViewById(R.id.bt_reminder_save);
        btDelete.setOnClickListener(this);
        btSave.setOnClickListener(this);
        setCurrentDateAndTime();
        pickDateAndTime();

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Add Reminder");
        activity = getActivity();
        return dialog;
    }

    public void setCurrentDateAndTime() {
        etDate.setText(Utils.getCurrentDate());
        etTime.setText(Utils.getCurrentTime());
    }

    public void pickDateAndTime() {
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        etDate.setText(Utils.getDateString(i, i1, i2));
                    }
                };
                String s = etDate.getText() + "";
                String strArrtmp[] = s.split("-");
                day = Integer.parseInt(strArrtmp[0]);
                month = Integer.parseInt(strArrtmp[1]) - 1;
                year = Integer.parseInt(strArrtmp[2]);
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, onDateSetListener, year, month, day);
                datePickerDialog.setTitle("Pick date reminder");
                datePickerDialog.show();
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        etTime.setText(Utils.getTimeString(i, i1));
                    }
                };
                String s = etTime.getText() + "";
                String strArrtmp[] = s.split(":");
                hour = Integer.parseInt(strArrtmp[0]);
                minute = Integer.parseInt(strArrtmp[1]);
                TimePickerDialog timePickerDialog = new TimePickerDialog(activity, onTimeSetListener, hour, minute, true);
                timePickerDialog.setTitle("Pick time reminder");
                timePickerDialog.show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_reminder_delete: {
                dismiss();
                NoteActivity.tvReminder.setText("Reminder: None");
                break;
            }
            case R.id.bt_reminder_save: {
                dismiss();
                NoteActivity.tvReminder.setText("Reminder: " + etDate.getText() + ", " + etTime.getText());
                break;
            }
        }
    }
}
