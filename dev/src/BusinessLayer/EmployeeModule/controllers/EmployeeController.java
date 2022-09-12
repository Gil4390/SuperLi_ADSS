package BusinessLayer.EmployeeModule.controllers;

import BusinessLayer.EmployeeModule.Objects.Employee;
import DataAccessLayer.EmployeesModuleDal.Mappers.EmployeeMapper;
import DataAccessLayer.EmployeesModuleDal.Mappers.JobConstrainsMapper;
import BusinessLayer.EmployeeModule.Objects.EmployeeJobConstraint;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class EmployeeController {

    //private HashMap<Integer, Employee> Employees;
    private EmployeeMapper employeeMapper;
    private JobConstrainsMapper jobConstrainsMapper;
    public EmployeeController(EmployeeMapper employeeMapper, JobConstrainsMapper jobConstrainsMapper) {
        //this.Employees = new HashMap<>();
        this.employeeMapper = employeeMapper;
        this.jobConstrainsMapper = jobConstrainsMapper;
    };

    public Employee getEmployeeByID(int ID) throws Exception{
        Employee e =employeeMapper.getEmployee(ID) ;
        return e;
    }
    public List<Employee> getAllEmployees() throws Exception {
        return employeeMapper.selectAllEmployees();
    }
    public Vector<Employee> getAvailableEmployeesForShift(LocalDate date, String shiftType) throws Exception{
        Vector<Employee> availableEmp = new Vector<>();
        for (Employee employee : getAllEmployees()){
            boolean toAdd = true;
            for (EmployeeJobConstraint e: jobConstrainsMapper.selectJobContainsOfEmployee(employee.getID()))
            {
                if (LocalDate.parse(e.getDate()).equals(date) && e.getShiftType().equals(shiftType)){
                    toAdd = false;
                }
            }
            if(toAdd){
                availableEmp.add(employee);
            }
        }
        return availableEmp;
    }
    public void addEmployee(String name, int ID, String bank_Account, int salary, String employment_start_date, String employmentDetails, String number) throws Exception{
        if (getEmployeeByID(ID)!=null){
            throw new Exception("Employee with this ID already exists");
        }
        LocalDate ld = LocalDate.now();
        if (!employment_start_date.equals("Today"))
        {
            ld = LocalDate.parse(employment_start_date);
        }
        if (!employeeMapper.insert(ID,name,salary,bank_Account,ld.toString(),employmentDetails,number))
            throw new Exception("Couldnt save the new employee");
    }

    public void removeEmployee(int ID) throws Exception{
        if (getEmployeeByID(ID)==null){
            throw new Exception("Employee with this ID already exists");
        }
        for (EmployeeJobConstraint ejc: jobConstrainsMapper.selectJobContainsOfEmployee(ID))
            jobConstrainsMapper.deleteJobConstarint(ejc.getEmployeeID(),ejc.getDate(), ejc.getShiftType());
        employeeMapper.deleteEmployee(ID);

    }



    public void updateEmployeeName(int employeeID, String newName) throws Exception {
        validateEmployeeExists(employeeID);
        employeeMapper.updateEmployee(employeeID,newName,"Name");
    }
    public void updateEmployeeBankAccount(int employeeID, String newBankAccount) throws Exception {
        validateEmployeeExists(employeeID);
        employeeMapper.updateEmployee(employeeID,newBankAccount,"BankDetails");
    }
    public void updateEmployeeSalary(int employeeID, int newSalary) throws Exception {
        validateEmployeeExists(employeeID);
        employeeMapper.updateEmployee(employeeID,newSalary,"Salary");
    }
    public void updateEmploymentDetails(int employeeID, String employmentDetails) throws Exception {
        validateEmployeeExists(employeeID);
        employeeMapper.updateEmployee(employeeID,employmentDetails,"EmployeeDetails");
    }


    public List<Employee> getEmployees(List<Integer> employeesID) throws Exception {
        List<Employee> result = new LinkedList<>();
        for(Employee emp : employeeMapper.selectAllEmployees()){
            if (employeesID.contains(emp.getID()))
                result.add(emp);
        }
        return result;
    }

    public void validateEmployeeExists(int employeeID) throws Exception {
        if (getEmployeeByID(employeeID)==null){
            throw new Exception("Employee with ID:" + employeeID + " does not exist");
        }
    }
    public void addJobConstraintToEmployee(int employeeID, String strDate, String shift) throws Exception{
        LocalDate date = LocalDate.parse(strDate);
        if(date.isBefore(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.THURSDAY)))){
            throw new Exception("the job constraint you entered is invalid");
        }
        jobConstrainsMapper.insert(employeeID,strDate,shift);
        getEmployeeByID(employeeID).addJobConstraint(LocalDate.parse(strDate),shift);
    }

//    public void removePassedDateConstraints() {
//        for(Employee employee : Employees.values())
//        {
//            employee.removePassesDateConstraints();
//        }
//    }
    public void validateJobConstraintOfEmployee(int eID, LocalDate date, String shiftType) throws Exception {
        for (EmployeeJobConstraint dejc: jobConstrainsMapper.selectJobContainsOfEmployee(eID))
        {
            if (LocalDate.parse(dejc.getDate()).equals(date) && dejc.getShiftType().equals(shiftType))
                throw new Exception("Employee with ID: " + eID + " cannot be assigned to this shift");
        }
    }
    public void validateJobConstraintsOfEmployeesForShift(HashMap<Integer, String> employees, String date, String shift_type) throws Exception{
        for (int eID: employees.keySet()) {
            validateJobConstraintOfEmployee(eID, LocalDate.parse(date), shift_type);
        }
    }

    public void deleteAllData() {
        jobConstrainsMapper.deleteAllData();
        employeeMapper.deleteAllData();
    }
}
