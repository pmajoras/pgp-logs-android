package pgp.logs.models;

import android.view.View;

public class FormValidationResult {

    private Boolean valid;
    private View focusView;

    public FormValidationResult() {
        valid = true;
    }

    public FormValidationResult(Boolean isValid, View focusView) {
        this.setValid(isValid);
        this.setFocusView(focusView);
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public View getFocusView() {
        return focusView;
    }

    public void setFocusView(View focusView) {
        this.focusView = focusView;
    }

    public Boolean isValid() {
        return valid;
    }
}
