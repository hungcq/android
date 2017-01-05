package raijin.taxi69.models;

/**
 * Created by 1918 on 23-Dec-16.
 */

public class TaxiType {
    private int category;
    private String photoUrl;
    private String name;

    public TaxiType(int category, String photoUrl, String name) {
        this.category = category;
        this.photoUrl = photoUrl;
        this.name = name;
    }

    public TaxiType() {
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
