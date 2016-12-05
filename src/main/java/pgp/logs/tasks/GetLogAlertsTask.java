package pgp.logs.tasks;

import android.os.AsyncTask;

import org.json.JSONException;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import pgp.logs.models.ApplicationsResult;
import pgp.logs.models.PgpLogAlert;
import pgp.logs.network.NetworkService;

public class GetLogAlertsTask extends AsyncTask<Void, Void, List<PgpLogAlert>> {

    private final String mUserId;
    private final String mToken;
    private ITaskListener<List<PgpLogAlert>> mTaskListener;
    private final NetworkService networkService = new NetworkService();

    public GetLogAlertsTask(String userId, String token, ITaskListener<List<PgpLogAlert>> taskListener) {
        mUserId = userId;
        mToken = token;
        mTaskListener = taskListener;
    }

    @Override
    protected List<PgpLogAlert> doInBackground(Void... params) {
        List<PgpLogAlert> result;

        try {
            result = networkService.getLogAlerts(mUserId, mToken);
        } catch (JSONException | ExecutionException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
            result = null;
        }

        return result;
    }

    @Override
    protected void onPostExecute(final List<PgpLogAlert> loginResult) {
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