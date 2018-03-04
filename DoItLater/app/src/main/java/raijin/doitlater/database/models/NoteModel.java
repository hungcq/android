package raijin.doitlater.database.models;

import io.realm.RealmObject;

/**
 * Created by Qk Lahpita on 7/21/2016.
 */
public class NoteModel extends RealmObject {
    //
    private int ID;
    private String pathOfAudio;
    private String title;
    private String detail;
    private String location;
    private String timeReminder;
    private String imagePath;
    private boolean hasAudio;
    private String dateReminder;
    private boolean isCompleted;
    private int requestCodeAlarm;
    private int priority;

    public NoteModel(int ID, String pathOfAudio, String title, String detail, String location, String timeReminder, String imagePath,
                     boolean hasAudio, String dateReminder, int requestCodeAlarm, boolean isCompleted, int priority) {
        this.ID = ID;
        this.pathOfAudio = pathOfAudio;
        this.title = title;
        this.detail = detail;
        this.location = location;
        this.timeReminder = timeReminder;
        this.imagePath = imagePath;
        this.hasAudio = hasAudio;
        this.dateReminder = dateReminder;
        this.requestCodeAlarm = requestCodeAlarm;
        this.isCompleted = isCompleted;
        this.priority = priority;
    }

    public NoteModel() {
        this.priority = 1;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getPathOfAudio() {
        return pathOfAudio;
    }

    public void setPathOfAudio(String pathOfAudio) {
        this.pathOfAudio = pathOfAudio;
    }

    public int getRequestCodeAlarm() {
        return requestCodeAlarm;
    }

    public void setRequestCodeAlarm(int requestCodeAlarm) {
        this.requestCodeAlarm = requestCodeAlarm;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getDateReminder() {
        return dateReminder;
    }

    public void setDateReminder(String dateReminder) {
        this.dateReminder = dateReminder;
    }

    public String getTimeReminder() {
        return timeReminder;
    }

    public void setTimeReminder(String timeReminder) {
        this.timeReminder = timeReminder;
    }

    public boolean isHasAudio() {
        return hasAudio;
    }

    public void setHasAudio(boolean hasAudio) {
        this.hasAudio = hasAudio;
    }


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
