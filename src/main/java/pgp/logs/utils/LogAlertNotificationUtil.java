package pgp.logs.utils;



import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Random;
import pgp.logs.R;

public class LogAlertNotificationUtil implements  IDestroy {

    private Context mContext;
    private Class mClass;

    public LogAlertNotificationUtil(Context context, Class activityClass) {
        mContext = context;
        mClass = activityClass;
    }

    public void generateNotification(String title, String content) {
        Log.d("PGP-LOGS", "HomeActivity >> LogAlertNotificationUtil >> generateNotification >> Start");

        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setAutoCancel(true);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(mContext, mClass);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(mClass);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(new Random().nextInt(1000000), builder.build());

        Log.d("PGP-LOGS", "HomeActivity >> LogAlertNotificationUtil >> generateNotification >> Finish");
    }

    @Override
    public void onDestroy() {
        mClass = null;
        mContext = null;
    }
}
