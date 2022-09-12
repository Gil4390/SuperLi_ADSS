package BusinessLayer.EmployeeModule.Objects;

import BusinessLayer.DeliveryModule.Objects.TimeStampChecker;

public class JobMessages extends TimeStampChecker {
    private final String job;
    private final String message;
    private final String branch;

    public JobMessages(String branch , String job, String mes) {
        this.job = job;
        this.message = mes;
        this.branch = branch;
    }

    public String getJob() {
        return job;
    }

    public String getMessage() {
        return message;
    }

    public String getBranch() {
        return branch;
    }
}
