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

public class PgpApplicationAdapter extends ArrayAdapter<PgpApplication> {

    private Context context;
    private List<PgpApplication> applicationList;

    //constructor, call on creation
    public PgpApplicationAdapter(Context context, int resource, List<PgpApplication> objects) {
        super(context, resource, objects);

        this.context = context;
        this.applicationList = objects;
        Log.d("PGP-LOGS", "PgpApplicationAdapter >> constructor >> applicationList.size" + this.applicationList.size());
    }

    //called when rendering the list
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the property we are displaying
        PgpApplication application = applicationList.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.application_row, null);

        TextView applicationNameText = (TextView) view.findViewById(R.id.application_name);
        TextView alertCountText = (TextView) view.findViewById(R.id.alert_count);

        int alertCount = 0;
        List<PgpAlert> alertList = application.getAlertList();
        for (int i = 0; i < alertList.size(); i++) {
            alertCount += alertList.get(i).getCount();
        }

        applicationNameText.setText(application.getAppId() + " - " + application.getName());
        alertCountText.setText("Alerts: " + String.valueOf(alertCount));

        return view;
    }
}
