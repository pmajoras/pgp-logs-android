package pgp.logs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import pgp.logs.models.ApplicationsResult;
import pgp.logs.models.PgpApplication;
import pgp.logs.services.LogAlertServiceHandler;
import pgp.logs.tasks.GetApplicationsTask;
import pgp.logs.tasks.ITaskListener;
import pgp.logs.utils.LogAlertNotificationUtil;
import pgp.logs.utils.LoginUtil;
import pgp.logs.adapters.PgpApplicationAdapter;

public class HomeActivity extends AppCompatActivity {

    private View mProgressView;
    private View mContentView;
    private GetApplicationsTask mGetApplicationsTask = null;
    private ITaskListener<ApplicationsResult> mGetApplicationsTaskListener = new ITaskListener<ApplicationsResult>() {
        @Override
        public void onTaskCompleted(ApplicationsResult result) {
            onGetApplicationsTask(result);
        }

        @Override
        public void onTaskCancelled() {
            mGetApplicationsTask = null;
            showProgress(false);
        }
    };

    public void onGetApplicationsTask(ApplicationsResult result) {
        //create our new array adapter
        ArrayAdapter<PgpApplication> adapter = new PgpApplicationAdapter(this, 0, result.getApplicationList());
        //Find list view and bind it with the custom adapter
        ListView listView = (ListView) findViewById(R.id.home_app_list);
        listView.setAdapter(adapter);
        showProgress(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mProgressView = findViewById(R.id.home_progress);
        mContentView = findViewById(R.id.scroll_content);

        this.showProgress(true);
        mGetApplicationsTask = new GetApplicationsTask(LoginUtil.getUserId(this), LoginUtil.getAuthToken(this), mGetApplicationsTaskListener);
        mGetApplicationsTask.execute();
        LogAlertServiceHandler.startLogAlertService(this);
        Log.d("PGP-LOGS", "HomeActivity >> EVENT >> onCreate");
    }

    public void redirectToLogAlerts(View view) {
        Intent intent = new Intent(this, LogAlertsActivity.class);
        startActivity(intent);
        finish();
    }

    public void logout() {
        LoginUtil.ResetAuthToken(this);
        redirectToLogin();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
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

    @Override
    public void onStart() {
        super.onStart();
        Log.d("PGP-LOGS", "HomeActivity >> EVENT >> onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("PGP-LOGS", "HomeActivity >> EVENT >> onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("PGP-LOGS", "HomeActivity >> EVENT >> onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                break;
        }
        return true;
    }
}
