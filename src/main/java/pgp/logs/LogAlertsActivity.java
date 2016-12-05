package pgp.logs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pgp.logs.adapters.PgpLogAlertAdapter;
import pgp.logs.models.PgpLogAlert;
import pgp.logs.tasks.GetLogAlertsTask;
import pgp.logs.tasks.ITaskListener;
import pgp.logs.utils.LogAlertNotificationUtil;
import pgp.logs.utils.LoginUtil;

public class LogAlertsActivity extends AppCompatActivity {

    private View mProgressView;
    private View mContentView;
    private GetLogAlertsTask mGetLogAlertsTask;
    private ITaskListener<List<PgpLogAlert>> mGetLogAlertsListener = new ITaskListener<List<PgpLogAlert>>() {
        @Override
        public void onTaskCompleted(List<PgpLogAlert> result) {
            onGetLogAlerts(result);
        }

        @Override
        public void onTaskCancelled() {
            mGetLogAlertsTask = null;
            showProgress(false);
        }
    };

    public void onGetLogAlerts(List<PgpLogAlert> result) {
        setListAdapter(result);
        showProgress(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_alerts);
        mProgressView = findViewById(R.id.log_alerts_progress);
        mContentView = findViewById(R.id.log_alerts_list);

        Bundle extras = getIntent().getExtras();

        if (extras != null && extras.containsKey(LogAlertNotificationUtil.LogAlertNotificationExtra)) {
            ArrayList<PgpLogAlert> list = extras.getParcelableArrayList(LogAlertNotificationUtil.LogAlertNotificationExtra);
            setListAdapter(list);
        } else {
            this.showProgress(true);
            mGetLogAlertsTask = new GetLogAlertsTask(LoginUtil.getUserId(this), LoginUtil.getAuthToken(this), mGetLogAlertsListener);
            mGetLogAlertsTask.execute();
        }

        Log.d("PGP-LOGS", "LogAlertsActivity >> EVENT >> onCreate");
    }

    private void setListAdapter(List<PgpLogAlert> list) {
        if (list == null) {
            list = new ArrayList<>();
        }

        ArrayAdapter<PgpLogAlert> adapter = new PgpLogAlertAdapter(this, 0, list);
        //Find list view and bind it with the custom adapter
        ListView listView = (ListView) findViewById(R.id.log_alerts_list);
        listView.setAdapter(adapter);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mContentView.setVisibility(show ? View.GONE : View.VISIBLE);
            mContentView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mContentView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mContentView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
