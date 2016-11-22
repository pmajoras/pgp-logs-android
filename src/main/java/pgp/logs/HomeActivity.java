package pgp.logs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import pgp.logs.utils.LogAlertNotificationUtil;
import pgp.logs.utils.LoginUtil;

public class HomeActivity extends AppCompatActivity {

    private LogAlertNotificationUtil mLogAlertNotificationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();

        String token = intent.getExtras().getString("token");
        mLogAlertNotificationUtil = new LogAlertNotificationUtil(this, LoginActivity.class);
        mLogAlertNotificationUtil.generateNotification("Pgp-Logs", "Test!!!!!");
        Log.d("PGP-LOGS", "HomeActivity >> EVENT >> onCreate >> token >>" + token);
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void logout(View view) {
        LoginUtil.ResetAuthToken(this);
        redirectToLogin();
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
        mLogAlertNotificationUtil.onDestroy();
        super.onDestroy();
        Log.d("PGP-LOGS", "HomeActivity >> EVENT >> onDestroy");
    }
}
