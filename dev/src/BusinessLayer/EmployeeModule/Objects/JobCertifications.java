package BusinessLayer.EmployeeModule.Objects;

import BusinessLayer.DeliveryModule.Objects.TimeStampChecker;

public class JobCertifications extends TimeStampChecker {

    private int employeeID;
    private final String JobTitle;
    private final String CertificationName;

    public JobCertifications(int id , String jobTitle, String certificationName)
    {
        employeeID = id;
        JobTitle = jobTitle;
        CertificationName = certificationName;
    }

    public int getEmployeeID() {
        updateTimeStamp();
        return employeeID;
    }

    public String getCertificationName() {
        updateTimeStamp();
        return CertificationName;
    }

    public String getJobTitle() {
        updateTimeStamp();
        return JobTitle;
    }

}
