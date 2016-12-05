package pgp.logs.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pgp.logs.R;
import pgp.logs.models.PgpAlert;
import pgp.logs.models.PgpApplication;
import pgp.logs.models.PgpLogAlert;

public class PgpLogAlertAdapter extends ArrayAdapter<PgpLogAlert> {

    private Context context;
    private List<PgpLogAlert> logAlerts;

    //constructor, call on creation
    public PgpLogAlertAdapter(Context context, int resource, List<PgpLogAlert> objects) {
        super(context, resource, objects);

        this.context = context;
        this.logAlerts = objects;
        Log.d("PGP-LOGS", "PgpApplicationAdapter >> constructor >> applicationList.size" + this.logAlerts.size());
    }

    //called when rendering the list
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the property we are displaying
        PgpLogAlert logAlert = logAlerts.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.log_alert_row, null);

        TextView logAlertIdText = (TextView) view.findViewById(R.id.log_alert_id);
        TextView logAlertMessageText = (TextView) view.findViewById(R.id.log_alert_message);

        logAlertIdText.setText(logAlert.getId());
        logAlertMessageText.setText(logAlert.getMessage());

        return view;
    }
}
