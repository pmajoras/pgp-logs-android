package pgp.logs.utils;



import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;
import pgp.logs.R;
import pgp.logs.models.PgpLogAlert;

public class LogAlertNotificationUtil implements  IDestroy {

    public static final String LogAlertNotificationExtra = "LOG_ALERT_NOTIFICATION_EXTRA_DATA";

    private Context mContext;
    private Class mClass;

    public LogAlertNotificationUtil(Context context, Class activityClass) {
        mContext = context;
        mClass = activityClass;
    }

    public void generateNotification(String title, String content) {
        generateNotification(title, content, null);
    }

    public void generateNotification(String title, String content, ArrayList<PgpLogAlert> parceableParam) {
        Log.d("PGP-LOGS", "HomeActivity >> LogAlertNotificationUtil >> generateNotification >> Start");
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_light)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setSound(soundUri)
                        .setAutoCancel(true);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(mContext, mClass);
        if (parceableParam != null) {
            resultIntent.putParcelableArrayListExtra(LogAlertNotificationUtil.LogAlertNotificationExtra, parceableParam);
            //resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

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
                        new Random().nextInt(1000000),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        //PendingIntent.getActivity(mContext, new Random().nextInt(1000000), resultIntent, 0);
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
