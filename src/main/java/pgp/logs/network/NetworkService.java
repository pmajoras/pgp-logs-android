package pgp.logs.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NoCache;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import pgp.logs.models.ApplicationsResult;
import pgp.logs.models.PgpApplication;
import pgp.logs.models.PgpLogAlert;
import pgp.logs.models.UserLoginResult;

public class NetworkService {

    private static final String baseApiUrl = "https://pgp-logs-app.herokuapp.com/api/";

    public UserLoginResult authenticate(String username, String password) throws JSONException, InterruptedException, ExecutionException, TimeoutException {
        Log.d("PGP-LOGS", "NetworkService >> authenticate >> Start");
        long startNow = android.os.SystemClock.uptimeMillis();

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        RequestQueue requestQueue = new RequestQueue(new NoCache(), network);

        // Start the queue
        requestQueue.start();

        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("password", password);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, baseApiUrl + "authentication/authenticate/", body, future, future);

        // Add the request to the RequestQueue.
        requestQueue.add(request);
        JSONObject response = future.get(20, TimeUnit.SECONDS);

        requestQueue.stop();

        long endNow = android.os.SystemClock.uptimeMillis();
        Log.d("PGP-LOGS", "NetworkService >> authenticate >> Execution time: " + (endNow - startNow) + " ms");
        Log.d("PGP-LOGS", "NetworkService >> authenticate >> Finish Method");
        return getLogResultFromJson(response);
    }

    public ApplicationsResult getApplications(String userId, final String token) throws JSONException, InterruptedException, ExecutionException, TimeoutException {
        String url = baseApiUrl + "user/" + userId + "/applications/";

        Log.d("PGP-LOGS", "NetworkService >> getApplications >> Start");
        long startNow = android.os.SystemClock.uptimeMillis();

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        RequestQueue requestQueue = new RequestQueue(new NoCache(), network);

        // Start the queue
        requestQueue.start();
        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, future, future) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authentication", token);
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        requestQueue.add(request);
        JSONArray response = future.get(20, TimeUnit.SECONDS);

        requestQueue.stop();

        long endNow = android.os.SystemClock.uptimeMillis();
        Log.d("PGP-LOGS", "NetworkService >> getApplications >> Execution time: " + (endNow - startNow) + " ms");
        Log.d("PGP-LOGS", "NetworkService >> getApplications >> Finish Method");
        return getApplicationsResultFromJsonArray(response);
    }

    public ArrayList<PgpLogAlert> getLogAlerts(String userId, final String token) throws JSONException, InterruptedException, ExecutionException, TimeoutException {
        String url = baseApiUrl + "user/" + userId + "/logAlerts/";

        Log.d("PGP-LOGS", "NetworkService >> getLogAlerts >> Start");
        long startNow = android.os.SystemClock.uptimeMillis();

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        RequestQueue requestQueue = new RequestQueue(new NoCache(), network);

        // Start the queue
        requestQueue.start();
        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, future, future) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authentication", token);
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        requestQueue.add(request);
        JSONArray response = future.get(20, TimeUnit.SECONDS);

        requestQueue.stop();

        long endNow = android.os.SystemClock.uptimeMillis();
        Log.d("PGP-LOGS", "NetworkService >> getLogAlerts >> Execution time: " + (endNow - startNow) + " ms");
        Log.d("PGP-LOGS", "NetworkService >> getLogAlerts >> Finish Method");
        return getLogAlertsResultFromJsonArray(response);
    }


    private UserLoginResult getLogResultFromJson(JSONObject response) {
        UserLoginResult result = new UserLoginResult();

        try {
            result.setToken(response.getString("token"));
            result.setId(response.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private ApplicationsResult getApplicationsResultFromJsonArray(JSONArray response) {
        ApplicationsResult result = new ApplicationsResult();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject application = response.getJSONObject(i);
                result.add(new PgpApplication(application));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private ArrayList<PgpLogAlert> getLogAlertsResultFromJsonArray(JSONArray response) {
        ArrayList<PgpLogAlert> result = new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject application = response.getJSONObject(i);
                result.add(new PgpLogAlert(application));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
