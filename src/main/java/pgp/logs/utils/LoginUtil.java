package pgp.logs.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class LoginUtil {

    private static final String mAuthTokenKey = "PGP-LOGS-TOKEN";

    public static void setAuthToken(String token, Context context) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(mAuthTokenKey, token);

        Log.d("PGP-LOGS", "LoginUtil >> setAuthToken >> " + token);
        editor.commit();
    }

    public static String getAuthToken(Context context) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);

        String authToken = settings.getString(mAuthTokenKey, "");

        Log.d("PGP-LOGS", "LoginUtil >> getAuthToken >> " + authToken);
        return authToken;
    }

    public static void ResetAuthToken(Context context) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(mAuthTokenKey);

        editor.apply();
    }
}
