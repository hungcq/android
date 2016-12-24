package raijin.taxi69.models;

/**
 * Created by 1918 on 23-Dec-16.
 */

public class DriverInfo {

    private String name;
    private String photoUrl;

    public DriverInfo(String name, String photuUrl) {
        this.name = name;
        this.photoUrl = photuUrl;
    }

    public DriverInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
