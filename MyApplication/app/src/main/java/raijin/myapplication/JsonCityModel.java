package raijin.myapplication;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 1918 on 30-Nov-16.
 */

public class JsonCityModel {
    private static final String Id = "id";
    private static final String NAME = "ten";
    private static final String ASC_NAME = "ten_asc";
    private static final String ZIP_CODE = "zipcode";
    private static final String STATUS = "trangthai";
    private static final String COUNTRY_CODE = "maquocgia";

    @SerializedName(Id)
    private int ID;

    @SerializedName(NAME)
    private String name;

    @SerializedName(ASC_NAME)
    private String ascName;

    @SerializedName(ZIP_CODE)
    private int zipCode;

    @SerializedName(STATUS)
    private int status;

    @SerializedName(COUNTRY_CODE)
    private int countryCode;

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getAscName() {
        return ascName;
    }

    public int getZipCode() {
        return zipCode;
    }

    public int getStatus() {
        return status;
    }

    public int getCountryCode() {
        return countryCode;
    }
}
