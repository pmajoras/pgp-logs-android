package pgp.logs.models;
import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;
import java.util.Date;

public class PgpLogAlert implements Parcelable {

    private String id;
    private String message;
    private Date alertDate;

    public PgpLogAlert(JSONObject jsonObject) {

        try {
            setId(jsonObject.getString("_id"));
            setMessage(jsonObject.getJSONArray("message").getString(0));
            //String alertDateStr = jsonObject.getString("alertDate");
            //2016-12-04T23:40:18.506Z
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            setAlertDate(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(Date alertDate) {
        this.alertDate = alertDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    protected PgpLogAlert(Parcel in) {
        id = in.readString();
        message = in.readString();
        long tmpAlertDate = in.readLong();
        alertDate = tmpAlertDate != -1 ? new Date(tmpAlertDate) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(message);
        dest.writeLong(alertDate != null ? alertDate.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PgpLogAlert> CREATOR = new Parcelable.Creator<PgpLogAlert>() {
        @Override
        public PgpLogAlert createFromParcel(Parcel in) {
            return new PgpLogAlert(in);
        }

        @Override
        public PgpLogAlert[] newArray(int size) {
            return new PgpLogAlert[size];
        }
    };
}
