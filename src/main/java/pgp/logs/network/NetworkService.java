package pgp.logs.network;

import android.util.Log;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import pgp.logs.models.UserLoginResult;

public class NetworkService {

    private static final String baseApiUrl = "api/";

    public UserLoginResult authenticate(String username, String password) throws JSONException, InterruptedException, ExecutionException, TimeoutException {
        Log.d("PGP-LOGS", "NetworkService >> authenticate >> Start");

        final RequestQueue mRequestQueue;

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(null, network);

        // Start the queue
        mRequestQueue.start();

        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("password", password);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, baseApiUrl + "authentication/authenticate", body, future, future);

        // Add the request to the RequestQueue.
        mRequestQueue.add(request);
        JSONObject response = future.get(20, TimeUnit.SECONDS);

        mRequestQueue.stop();

        Log.d("PGP-LOGS", "NetworkService >> authenticate >> Finish Method");
        return getLogResultFromJson(response);
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
}
