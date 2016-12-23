package raijin.taxi69.models;

/**
 * Created by 1918 on 23-Dec-16.
 */

public class TestPoint {

    private double latitude;
    private double longitude;

    public TestPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public TestPoint() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
