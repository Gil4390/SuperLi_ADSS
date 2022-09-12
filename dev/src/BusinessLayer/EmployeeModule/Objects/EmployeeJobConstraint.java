package BusinessLayer.EmployeeModule.Objects;

import BusinessLayer.DeliveryModule.Objects.TimeStampChecker;

public class EmployeeJobConstraint extends TimeStampChecker {
    private final int employeeID;
    private final String Date;
    private final String ShiftType;

    public EmployeeJobConstraint(int id , String date, String shiftType)
    {
        employeeID = id;
        Date = date;
        ShiftType = shiftType;
    }

    public String getShiftType() {
        updateTimeStamp();
        return ShiftType;
    }

    public String getDate() {
        updateTimeStamp();
        return Date;
    }

    public int getEmployeeID() {
        updateTimeStamp();
        return employeeID;
    }
}
