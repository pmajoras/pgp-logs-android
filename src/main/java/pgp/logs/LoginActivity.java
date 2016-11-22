package pgp.logs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;

import java.util.ArrayList;
import java.util.List;

import pgp.logs.models.FormValidationResult;
import pgp.logs.models.UserLoginResult;
import pgp.logs.tasks.ITaskListener;
import pgp.logs.tasks.UserLoginTask;
import pgp.logs.utils.LoginActivityUtil;
import pgp.logs.utils.LoginUtil;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private LoginActivityUtil mLoginActivityUtil = null;
    private UserLoginTask mAuthTask = null;
    private ITaskListener<UserLoginResult> mAuthTaskListener = new ITaskListener<UserLoginResult>() {
        @Override
        public void onTaskCompleted(UserLoginResult loginResult) {
            onUserTaskCompleted(loginResult);
        }

        @Override
        public void onTaskCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    };

    public void onUserTaskCompleted(UserLoginResult loginResult) {
        mAuthTask = null;

        if (loginResult.IsAuthenticated()) {
            LoginUtil.setAuthToken(loginResult.getToken(), this);
            redirectToHome(loginResult);
        } else {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
        }
        showProgress(false);
    }

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mLoginActivityUtil = new LoginActivityUtil(LoginActivity.this, mEmailView, mPasswordView);

        String currentToken = LoginUtil.getAuthToken(getBaseContext());
        if (!TextUtils.isEmpty(currentToken)) {
            redirectToHome(new UserLoginResult(currentToken));
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
    }

    /**
     * Called when the user clicks the Send button
     */
    public void login(View view) {
        if (mAuthTask != null) {
            return;
        }

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        FormValidationResult result = mLoginActivityUtil.ValidateLoginForm(email, password);

        if (!result.isValid()) {
            result.getFocusView().requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password, mAuthTaskListener);
            mAuthTask.execute((Void) null);
        }
    }

    private void redirectToHome(UserLoginResult loginResult) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("token", loginResult.getToken());
        startActivity(intent);
        finish();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("PGP-LOGS", "LoginActivity >> EVENT >> onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("PGP-LOGS", "LoginActivity >> EVENT >> onStop");
    }

    @Override
    protected void onDestroy() {
        mLoginActivityUtil.onDestroy();
        super.onDestroy();
        Log.d("PGP-LOGS", "LoginActivity >> EVENT >> onDestroy");
    }
}

