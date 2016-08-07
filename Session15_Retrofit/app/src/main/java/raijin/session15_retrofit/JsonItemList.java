package raijin.session15_retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 1918 on 07-Aug-16.
 */
public class JsonItemList {
    @SerializedName("d")
    private List<JsonItem> jsonItemList;

    public List<JsonItem> getJsonItemList() {
        return jsonItemList;
    }

    public void setJsonItemList(List<JsonItem> jsonItemList) {
        this.jsonItemList = jsonItemList;
    }
}
