package pgp.logs.services;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class LogAlertServiceHandler {

    public static void startLogAlertService(Context context) {
        if (!isServiceRunning(context, LogAlertService.class)) {
            Intent logAlertIntent = new Intent(context, LogAlertService.class);
            context.startService(logAlertIntent);
        }
    }

    private static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        Log.d("PGP-LOGS", "LogAlertServiceHandler >> isServiceRunning >> start");
        boolean isRunning = false;

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                isRunning = true;
                break;
            }
        }

        Log.d("PGP-LOGS", "LogAlertServiceHandler >> isServiceRunning >>" + isRunning);
        return isRunning;
    }
}
