package pgp.logs.tasks;

import android.os.AsyncTask;

import pgp.logs.models.UserLoginResult;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserLoginTask extends AsyncTask<Void, Void, UserLoginResult> {

    private final String mEmail;
    private final String mPassword;
    private ITaskListener<UserLoginResult> mTaskListener;

    public UserLoginTask(String email, String password, ITaskListener<UserLoginResult> taskListener) {
        mEmail = email;
        mPassword = password;
        mTaskListener = taskListener;
    }

    @Override
    protected UserLoginResult doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.

        UserLoginResult result = new UserLoginResult();

        try {
            // Simulate network access.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return result;
        }

        result.setToken("fakeTokenTest");
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
