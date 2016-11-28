package pgp.logs.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PgpApplication {

    private List<PgpAlert> alertList = new ArrayList<>();
    private String name;
    private String description;
    private String appId;

    public PgpApplication(JSONObject jsonObject) {

        try {
            setName(jsonObject.getString("name"));
            setDescription(jsonObject.getString("description"));
            setAppId(jsonObject.getString("appId"));
            JSONArray alerts = jsonObject.getJSONArray("alerts");

            for (int i = 0; i < alerts.length(); i++) {
                JSONObject alert = alerts.getJSONObject(i);
                alertList.add(new PgpAlert(alert));
            }
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public List<PgpAlert> getAlertList() {
        return this.alertList;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
