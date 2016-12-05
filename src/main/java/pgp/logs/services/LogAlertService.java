package pgp.logs.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import pgp.logs.LoginActivity;
import pgp.logs.models.ApplicationsResult;
import pgp.logs.models.PgpLogAlert;
import pgp.logs.network.NetworkService;
import pgp.logs.utils.LogAlertNotificationUtil;
import pgp.logs.utils.LoginUtil;

public class LogAlertService extends Service {
    public static final String ACTION = "pgp.logs.services.LogAlertService";

    private Timer mTimer;
    private List<PgpLogAlert> logAlerts = new ArrayList<>();

    public LogAlertService() {
        super();
    }

    @Override
    public void onCreate() {
        Log.d("PGP-LOGS", "LogAlertService >> Start >> onCreate");
        mTimer = new Timer();
        Log.d("PGP-LOGS", "LogAlertService >> Finish >> onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Service self = this;

        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    Log.d("PGP-LOGS", "scheduleAtFixedRate >> run >> onStartCommand >> start");
                    if (LoginUtil.isAuthenticated(self)) {
                        NetworkService networkService = new NetworkService();
                        ArrayList<PgpLogAlert> result = networkService.getLogAlerts(LoginUtil.getUserId(self), LoginUtil.getAuthToken(self));

                        if (logAlertsChanged(result)) {
                            logAlerts = result;
                            LogAlertNotificationUtil logAlertNotificationUtil = new LogAlertNotificationUtil(self, LoginActivity.class);
                            logAlertNotificationUtil.generateNotification("A new log alert have been generated!", "Click here to check.", result);
                            logAlertNotificationUtil.onDestroy();
                        }
                    }
                } catch (Exception e) {
                    Log.d("PGP-LOGS", "scheduleAtFixedRate >> run >> onStartCommand >> error" + e.toString());
                }

                Log.d("PGP-LOGS", "scheduleAtFixedRate >> run >> onStartCommand >> finish");
            }
        }, 1000 * 10, 1000 * 30);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("PGP-LOGS", "LogAlertService >> onDestroy >> start");
        super.onDestroy();
        mTimer.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean logAlertsChanged(List<PgpLogAlert> result) {
        for (PgpLogAlert newLogAlert : result) {
            boolean wasFound = false;

            for (PgpLogAlert oldLogAlert : logAlerts) {
                if (newLogAlert.getId().equals(oldLogAlert.getId())) {
                    wasFound = true;
                }
            }

            if (!wasFound) {
                return true;
            }
        }

        return false;
    }
}
