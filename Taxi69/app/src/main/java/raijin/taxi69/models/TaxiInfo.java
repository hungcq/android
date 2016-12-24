package raijin.taxi69.models;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by 1918 on 23-Dec-16.
 */

public class TaxiInfo {

    private int price;
    private int type;
    private double latitude;
    private double longitude;
    private double angle;
    private Marker marker;

    public TaxiInfo(int price, int type, double latitude, double longitude, double angle) {
        this.price = price;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.angle = angle;
    }

    public TaxiInfo() {
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
