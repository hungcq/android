
package com.online.foodplus.models.jsondirectionmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonPolylineModel {

    @SerializedName("points")
    @Expose
    private String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

}
