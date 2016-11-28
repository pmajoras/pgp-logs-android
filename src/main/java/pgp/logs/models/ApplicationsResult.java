package pgp.logs.models;

import java.util.ArrayList;
import java.util.List;

public class ApplicationsResult {

    private List<PgpApplication> applicationList = new ArrayList<>();

    public ApplicationsResult() {

    }

    public void add(PgpApplication app) {
        applicationList.add(app);
    }

    public List<PgpApplication> getApplicationList() {
        return applicationList;
    }
}
