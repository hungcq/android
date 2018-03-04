package raijin.doitlater.database;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.Sort;
import raijin.doitlater.database.models.NoteModel;

/**
 * Created by Qk Lahpita on 7/23/2016.
 */
public class RealmHandle {

    private static RealmHandle inst;

    public static RealmHandle getInst() {
        if (inst == null) {
            inst = new RealmHandle();
        }
        return inst;
    }

    private RealmHandle() {

    }

    public void addNoteToRealm(NoteModel noteModel) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(noteModel);
        realm.commitTransaction();
    }

    public int getID() {
        Realm realm = Realm.getDefaultInstance();
        int numberOfObject = (int) realm.where(NoteModel.class).count();
        if (numberOfObject == 0) {
            return 0;
        } else {
            int nextID = realm.where(NoteModel.class).max("ID").intValue() + 1;
            return nextID;
        }
    }

    public List<NoteModel> getToDoList() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(NoteModel.class).equalTo("isCompleted", false).findAll();
    }

    public List<NoteModel> getSortedPriorityToDoList() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(NoteModel.class).equalTo("isCompleted", false).findAllSorted("priority", Sort.DESCENDING);
    }

    public List<NoteModel> getSortedTitleToDoList() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(NoteModel.class).equalTo("isCompleted", false).findAllSorted("title", Sort.ASCENDING);
    }

    public List<NoteModel> getCompletedList() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(NoteModel.class).equalTo("isCompleted", true).findAll();
    }

    public List<NoteModel> getSortedTitleCompletedList() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(NoteModel.class).equalTo("isCompleted", true).findAllSorted("title", Sort.ASCENDING);
    }

    public List<NoteModel> getSortedPriorityCompletedList() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(NoteModel.class).equalTo("isCompleted", true).findAllSorted("priority", Sort.DESCENDING);
    }

    public List<NoteModel> getAllNoteList() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(NoteModel.class).findAll();
    }

    public List<NoteModel> getSortedPriorityAllNoteList() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(NoteModel.class).findAllSorted("priority", Sort.DESCENDING);
    }

    public List<NoteModel> getSortedTitleAllNoteList() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(NoteModel.class).findAllSorted("title", Sort.ASCENDING);
    }

    public void setCurrentNoteIsCompleted(NoteModel noteModel) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        noteModel.setCompleted(true);
        realm.commitTransaction();
    }

    public NoteModel getNodeByID(int ID) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(NoteModel.class).equalTo("ID", ID).findFirst();
    }

    public void setCurrentNoteIsNotCompleted(NoteModel noteModel) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        noteModel.setCompleted(false);
        realm.commitTransaction();
    }

    public void editNoteFromRealm(NoteModel noteModel, String title, String detail, String location,
                                  String picturePath, String timeReminder, String dateReminder, int priority) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        noteModel.setTitle(title);
        noteModel.setDetail(detail);
        noteModel.setLocation(location);
        noteModel.setImagePath(picturePath);
        noteModel.setTimeReminder(timeReminder);
        noteModel.setDateReminder(dateReminder);
        noteModel.setPriority(priority);
        realm.commitTransaction();
    }

    public void deleteNoteFromRealm(NoteModel noteModel) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        noteModel.deleteFromRealm();
        realm.commitTransaction();
    }

    public void setHasAudio(NoteModel noteModel) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        noteModel.setHasAudio(true);
        realm.commitTransaction();
    }

    public void setHasNoAudio(NoteModel noteModel) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        noteModel.setHasAudio(false);
        realm.commitTransaction();
    }

    public List<NoteModel> findNoteByTitleOrDetail(String searchString, boolean isCompleted) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(NoteModel.class)
                .equalTo("isCompleted", isCompleted)
                .contains("title", searchString, Case.INSENSITIVE)
                .or()
                .equalTo("isCompleted", isCompleted)
                .contains("detail", searchString, Case.INSENSITIVE)
                .findAll();
    }

    public List<NoteModel> findNoteByTitleOrDetail(String searchString) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(NoteModel.class)
                .contains("title", searchString, Case.INSENSITIVE)
                .or()
                .contains("detail", searchString, Case.INSENSITIVE)
                .findAll();
    }

    public void setRequestCodeAlarm(NoteModel noteModel) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        try {
            noteModel.setRequestCodeAlarm(realm.where(NoteModel.class).max("requestCodeAlarm").intValue() + 1);
        } catch (Exception e) {
            noteModel.setRequestCodeAlarm(0);
        }

        realm.commitTransaction();
    }

    public void setPathOfAudio(NoteModel noteModel, String pathOfAudio) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        noteModel.setPathOfAudio(pathOfAudio);
        realm.commitTransaction();

    }

}
