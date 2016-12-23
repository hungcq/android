package raijin.taxi69.models;

/**
 * Created by 1918 on 23-Dec-16.
 */

public class TaxiInfo {

    private int price;
    private int type;

    public TaxiInfo(int price, int type) {
        this.price = price;
        this.type = type;
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
}
