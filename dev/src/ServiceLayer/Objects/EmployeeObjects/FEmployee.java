package ServiceLayer.Objects.EmployeeObjects;

import BusinessLayer.EmployeeModule.Objects.Employee;
import BusinessLayer.EmployeeModule.Objects.Job;

import java.time.LocalDate;
import java.util.Map;
import java.util.Vector;

public class FEmployee {
    private String name;
    private int ID;
    private String bankAccountDetails;
    private int salary;
    private LocalDate employmentStartDate;
    private String employmentDetails;
    private Map<LocalDate, String> jobConstraints;

    public FEmployee(Employee employee){
        this.name = employee.getName();
        this.ID = employee.getID();
        this.bankAccountDetails = employee.getBankAccountDetails();
        this.salary = employee.getSalary();
        this.employmentStartDate = employee.getEmploymentStartDate();
        this.employmentDetails = employee.getEmploymentDetails();
        this.jobConstraints = employee.getJobConstraints();
    }

//    private Vector<FJobConstraint> businessToServiceJobConstraint(Vector<JobConstraint> jobConstraints){
//        Vector<FJobConstraint> result = new Vector<>();
//        for (JobConstraint jobConstraint : jobConstraints){
//            result.add(new FJobConstraint(jobConstraint));
//        }
//        return result;
//    }

    private Vector<FJob> businessToServiceJob(Vector<Job> jobs){
        Vector<FJob> result = new Vector<>();
        for (Job job : jobs){
            result.add(new FJob(job));
        }
        return result;
    }

    public String getName() {
        return name;
    }
    public int getID() {
        return ID;
    }
    public String getBankAccountDetails() {
        return bankAccountDetails;
    }
    public int getSalary() {
        return salary;
    }
    public LocalDate getEmploymentStartDate() {
        return employmentStartDate;
    }
    public String getEmploymentDetails() {
        return employmentDetails;
    }
    public Map<LocalDate,String> getJobConstraints() {
        return jobConstraints;
    }

}
