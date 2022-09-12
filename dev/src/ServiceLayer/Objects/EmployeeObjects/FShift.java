package ServiceLayer.Objects.EmployeeObjects;

import BusinessLayer.EmployeeModule.Objects.Shift;

import java.time.LocalDate;
import java.util.HashMap;

public class FShift {
    enum ShiftType
    {
        morning , evening
    }
    private int shiftID;
    private LocalDate date;
    private ShiftType shiftType;
    private int shiftManager;
    private HashMap<Integer, String> employees;

    public FShift(Shift shift){
        this.shiftID = shift.getShiftID();
        this.date = shift.getDate();
        this.shiftType = ShiftType.valueOf(shift.getShiftType());
        this.shiftManager = shift.getShiftManager();
        this.employees = shift.getEmployees();
    }

    public int getShiftID() {
            return shiftID;
        }
    public LocalDate getDate() {
        return date;
    }
    public String getShiftType() {
        return shiftType.toString();
    }
    public int getShiftManager() {
        return shiftManager;
    }
    public HashMap<Integer, String> getEmployees() {
        return employees;
    }

}
