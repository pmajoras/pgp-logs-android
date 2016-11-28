package pgp.logs.models;

import org.json.JSONException;
import org.json.JSONObject;

public class PgpAlert {
    private String name;
    private int count;

    public PgpAlert(JSONObject jsonObject) {

        try {
            setName(jsonObject.getString("name"));
            setCount(jsonObject.getInt("count"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
