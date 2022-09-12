package BusinessLayer.EmployeeModule.Objects;


import BusinessLayer.DeliveryModule.Objects.TimeStampChecker;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Employee extends TimeStampChecker {
    private String name;
    private int ID;
    private String bankAccountDetails;
    private int salary;
    private LocalDate employmentStartDate;
    private String employmentDetails;
    private String number;
//    private Vector<JobConstraint> jobConstraints;
//    private Vector<Job> jobs;
//    private Vector<String> jobs;
    private Map<LocalDate, String> jobConstraints;


    public Employee(String name, int ID, String bankAccountDetails, int salary, LocalDate employment_start_date , String employmentDetails , String number) {
        this.name = name;
        this.ID = ID;
        this.bankAccountDetails = bankAccountDetails;
        this.salary = salary;
        this.employmentStartDate = employment_start_date;
        this.employmentDetails = employmentDetails;
        this.number = number;
        this.jobConstraints = new HashMap<>();
    }


    public void addJobConstraint(LocalDate date, String shift){
        updateTimeStamp();
        this.jobConstraints.put(date, shift);
    }
    public void removeConstrains(Map<LocalDate, String> forRemove)
    {
        updateTimeStamp();
        for (Map.Entry <LocalDate, String> entry: forRemove.entrySet())
            if (jobConstraints.containsKey(entry.getKey()) && jobConstraints.get(entry.getKey()).equals(entry.getValue()))
                this.jobConstraints.remove(entry.getKey());
    }


    //================getters & setters=====================
    public String getName() {
        updateTimeStamp();
        return name;
    }
    public void setName(String name) {
        updateTimeStamp();
        this.name = name;
    }
    public int getID() {
        updateTimeStamp();
        return ID;
    }
    public void setID(int ID) {
        updateTimeStamp();
        this.ID = ID;
    }
    public String getBankAccountDetails() {
        updateTimeStamp();
        return bankAccountDetails;
    }
    public void setBankAccountDetails(String bankAccountDetails) {
        updateTimeStamp();
        this.bankAccountDetails = bankAccountDetails;
    }
    public int getSalary() {
        updateTimeStamp();
        return salary;
    }
    public void setSalary(int salary) {
        updateTimeStamp();
        this.salary = salary;
    }
    public LocalDate getEmploymentStartDate() {
        updateTimeStamp();
        return employmentStartDate;
    }
    public String getEmploymentDetails() {
        updateTimeStamp();
        return employmentDetails;
    }
    public void setEmploymentDetails(String employmentDetails) {
        updateTimeStamp();
        this.employmentDetails = employmentDetails;
    }
    public Map<LocalDate, String> getJobConstraints() {
        updateTimeStamp();
        return jobConstraints;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        updateTimeStamp();
        return
                "Name=" + name +" | "+
                ", ID=" + ID +" | "+
                ", bankAccountDetails=" + bankAccountDetails.toString() +" | "+
                ", Salary=" + salary +" | "+
                ", EmploymentStartDate=" + employmentStartDate.toString() +" | "
                +"\""
                ;
    }
}
