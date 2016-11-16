package pgp.logs.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import pgp.logs.R;
import pgp.logs.models.FormValidationResult;

public class LoginActivityUtil implements IDestroy {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Context mContext;

    public LoginActivityUtil(Context context, AutoCompleteTextView emailView, EditText passwordView) {
        if (emailView == null || passwordView == null || context == null) {
            throw new IllegalArgumentException("The parameters are invalid.");
        }

        mEmailView = emailView;
        mPasswordView = passwordView;
        mContext = context;
    }

    public FormValidationResult ValidateLoginForm(String email, String password) {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        FormValidationResult validationResult = new FormValidationResult();
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || password.length() < 4) {
            mPasswordView.setError(mContext.getString(R.string.error_invalid_password));
            focusView = mPasswordView;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(mContext.getString(R.string.error_field_required));
            focusView = mEmailView;
        } else if (!email.contains("@")) {
            mEmailView.setError(mContext.getString(R.string.error_invalid_email));
            focusView = mEmailView;
        }

        validationResult.setValid(focusView == null);
        validationResult.setFocusView(focusView);

        return validationResult;
    }

    @Override
    public void onDestroy() {
        mEmailView = null;
        mPasswordView = null;
        mContext = null;
    }
}
