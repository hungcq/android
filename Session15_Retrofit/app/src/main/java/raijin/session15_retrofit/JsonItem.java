package raijin.session15_retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by 1918 on 07-Aug-16.
 */
public class JsonItem {

    @SerializedName("Id")
    private int id;

    @SerializedName("Thumb")
    private JsonThumbItem jsonThumbItem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public JsonThumbItem getJsonThumbItem() {
        return jsonThumbItem;
    }

    public void setJsonThumbItem(JsonThumbItem jsonThumbItem) {
        this.jsonThumbItem = jsonThumbItem;
    }
}
