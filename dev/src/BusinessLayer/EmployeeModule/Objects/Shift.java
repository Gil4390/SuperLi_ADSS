package BusinessLayer.EmployeeModule.Objects;


import BusinessLayer.DeliveryModule.Objects.TimeStampChecker;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class Shift extends TimeStampChecker {
    enum ShiftType
    {
        morning , evening
    };
    private int shiftID;
    private LocalDate date;
    private ShiftType shiftType;
    private int shiftManager;

    private String branchAddress;
    private HashMap<Integer,String> employees;

    private String[] managementJobs = {"hr", "branchManager", "acquisition"};
    //private String[] HQJobs

    public Shift(int shiftID,String branchAddress, LocalDate date,String shift_type,int shiftManagerID, HashMap<Integer,String> employees)
    {
        this.shiftID = shiftID;
        this.date = date;
        this.shiftType = ShiftType.valueOf(shift_type.toLowerCase());
        this.shiftManager = shiftManagerID;
        this.branchAddress = branchAddress;
        if (this.shiftType == ShiftType.evening) {validateNoManagement(employees.values());}
        this.employees = employees;
        //managementJobs = {"branchManager"};
        //HQJobs={"hr", "acquisition", "driver", "deliveryManager"}

    }

    private void validateNoManagement(Collection<String> jobs) {
        updateTimeStamp();
        for (String job : jobs){
            if(Arrays.asList(managementJobs).contains(job))
                throw new IllegalArgumentException("Management doesn't work night shifts");
        }
    }


    public void addEmployee(int employeeID , String job) {
        updateTimeStamp();
        employees.put(employeeID,job);
    }
    public void removeEmployee(int employeeID) {
        updateTimeStamp();
        employees.remove(employeeID);
    }

    public boolean isEmployeeInShift(int employeeID){
        updateTimeStamp();
        return employees.containsKey(employeeID);
    }

    //================getters & setters=====================
    public LocalDate getDate() {
        updateTimeStamp();
        return date;
    }
    public String getShiftType() {
        updateTimeStamp();
        return shiftType.toString();
    }
    public int getShiftManager() {
        updateTimeStamp();
        return shiftManager;
    }
    public void setShiftManager(int shiftManager) {
        updateTimeStamp();
            this.shiftManager = shiftManager;
        }
    public HashMap<Integer,String> getEmployees() {
        updateTimeStamp();
        return employees;
    }
    public int getShiftID() {
        updateTimeStamp();
        return shiftID;
    }

    public String getBranchAddress() {
        updateTimeStamp();
        return branchAddress;
    }

    @Override
    public String toString() {
        updateTimeStamp();

        String strEmployees = "";
        for(int employeeID : employees.keySet()){
            strEmployees += employeeID+"\n";
        }
        return "ShiftID:" + shiftID +
                ", date:" + date.toString() +
                ", shiftType:" + shiftType.toString() +
                ", shiftManager:" + shiftManager +
                ", employees in shift:\n" + strEmployees;
    }
}
