package pgp.logs.network;

import com.android.volley.VolleyError;

public interface IResponseListener<T> {
    void onResponse(final T returnValue);

    void onErrorResponse(VolleyError error);
}
