package pgp.logs.tasks;

import android.os.AsyncTask;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import pgp.logs.models.UserLoginResult;
import pgp.logs.network.NetworkService;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserLoginTask extends AsyncTask<Void, Void, UserLoginResult> {

    private final String mEmail;
    private final String mPassword;
    private ITaskListener<UserLoginResult> mTaskListener;
    private final NetworkService networkService = new NetworkService();

    public UserLoginTask(String email, String password, ITaskListener<UserLoginResult> taskListener) {
        mEmail = email;
        mPassword = password;
        mTaskListener = taskListener;
    }

    @Override
    protected UserLoginResult doInBackground(Void... params) {
        UserLoginResult result = null;

        try {
            result = networkService.authenticate(mEmail, mPassword);
        } catch (JSONException | ExecutionException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(final UserLoginResult loginResult) {
        if (mTaskListener != null) {
            mTaskListener.onTaskCompleted(loginResult);
        }
    }

    @Override
    protected void onCancelled() {
        if (mTaskListener != null) {
            mTaskListener.onTaskCancelled();
        }
    }
}
