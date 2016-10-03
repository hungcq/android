package raijin.session17_database;

import java.util.List;

import io.realm.Realm;

/**
 * Created by 1918 on 16-Aug-16.
 */
public class DBcontext {
    private Realm realm;
    private static DBcontext inst;

    private DBcontext() {
        realm = Realm.getDefaultInstance();
    }

    public static DBcontext getInst() {
        if (inst == null) {
            inst = new DBcontext();
        }
        return inst;
    }

    public List<Student> getAllStudent() {
        return realm.where(Student.class).findAll();
    }

    public void addStudent(Student student) {
        realm.beginTransaction();
        realm.copyToRealm(student);
        realm.commitTransaction();
    }
}
