package pgp.logs.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import pgp.logs.services.LogAlertService;
import pgp.logs.services.LogAlertServiceHandler;

public class BootReceiver extends BroadcastReceiver {
    static final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        // BOOT_COMPLETED” start Service
        if (intent.getAction().equals(BOOT_ACTION)) {
            //Service
            LogAlertServiceHandler.startLogAlertService(context);
        }
    }
}
