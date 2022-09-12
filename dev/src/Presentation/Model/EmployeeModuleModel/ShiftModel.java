package Presentation.Model.EmployeeModuleModel;

import ServiceLayer.Objects.EmployeeObjects.FShift;

import java.time.LocalDate;
import java.util.HashMap;

public class ShiftModel {
    enum ShiftType
    {
        morning , evening
    }
    private int shiftID;
    private LocalDate date;
    private ShiftType shiftType;
    private int shiftManager;
    private HashMap<Integer, String> employees;

    public ShiftModel(FShift shift){
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
    public String toString() {

        String strEmployees = "";
        for(int employeeID : employees.keySet()){
            strEmployees += employeeID+","+employees.get(employeeID)+ "  |  ";
        }
        return "ShiftID:" + shiftID +" | "+
                " date:" + date.toString() +" | "+
                " shiftType:" + shiftType.toString()+" | "+
                " shiftManager:" + shiftManager +" | "+
                " employees in shift:\n" + strEmployees;
    }
}
