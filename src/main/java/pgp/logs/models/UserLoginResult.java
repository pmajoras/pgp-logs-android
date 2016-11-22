package pgp.logs.models;

import java.util.Date;

public class UserLoginResult {

    private String token;
    private String id;
    private Date expiresAt;

    public UserLoginResult() {

    }

    public UserLoginResult(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Boolean IsAuthenticated() {
        return this.getToken() != null;
    }
}
