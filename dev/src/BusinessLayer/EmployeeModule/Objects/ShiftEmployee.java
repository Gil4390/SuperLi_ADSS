package BusinessLayer.EmployeeModule.Objects;

import BusinessLayer.DeliveryModule.Objects.TimeStampChecker;

public class ShiftEmployee extends TimeStampChecker {
    private int shiftID;
    private int employeeID;
    private String jobTitle;

    public ShiftEmployee(int s , int e , String j)
    {
        this.shiftID = s;
        this.employeeID = e;
        this.jobTitle = j;
    }

    public int getEmployeeID() {
        updateTimeStamp();
        return employeeID;
    }

    public int getShiftID() {
        updateTimeStamp();
        return shiftID;
    }

    public String getJobTitle() {
        updateTimeStamp();
        return jobTitle;
    }
}
