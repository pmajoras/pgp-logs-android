package pgp.logs.tasks;

import android.os.AsyncTask;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import pgp.logs.models.ApplicationsResult;
import pgp.logs.network.NetworkService;

public class GetApplicationsTask extends AsyncTask<Void, Void, ApplicationsResult> {

    private final String mUserId;
    private final String mToken;
    private ITaskListener<ApplicationsResult> mTaskListener;
    private final NetworkService networkService = new NetworkService();

    public GetApplicationsTask(String userId, String token, ITaskListener<ApplicationsResult> taskListener) {
        mUserId = userId;
        mToken = token;
        mTaskListener = taskListener;
    }

    @Override
    protected ApplicationsResult doInBackground(Void... params) {
        ApplicationsResult result;

        try {
            result = networkService.getApplications(mUserId, mToken);
        } catch (JSONException | ExecutionException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
            result = new ApplicationsResult();
        }

        return result;
    }

    @Override
    protected void onPostExecute(final ApplicationsResult loginResult) {
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