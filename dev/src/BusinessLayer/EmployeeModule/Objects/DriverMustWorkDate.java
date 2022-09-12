package BusinessLayer.EmployeeModule.Objects;

import BusinessLayer.DeliveryModule.Objects.TimeStampChecker;

public class DriverMustWorkDate extends TimeStampChecker {
    private int id;
    private String date;

    public DriverMustWorkDate(int id , String date)
    {
        this.date = date;
        this.id = id;
    }

    public int getId() {
        updateTimeStamp();
        return id;
    }

    public String getDate() {
        updateTimeStamp();
        return date;
    }
}
