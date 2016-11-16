package pgp.logs.tasks;

import android.os.AsyncTask;

import pgp.logs.R;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String mEmail;
    private final String mPassword;
    private ITaskListener<Boolean> mTaskListener;

    public UserLoginTask(String email, String password, ITaskListener<Boolean> taskListener) {
        mEmail = email;
        mPassword = password;
        mTaskListener = taskListener;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.

        try {
            // Simulate network access.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (mTaskListener != null) {
            mTaskListener.onTaskCompleted(success);
        }
    }

    @Override
    protected void onCancelled() {
        if (mTaskListener != null) {
            mTaskListener.onTaskCancelled();
        }
    }
}
