package ServiceLayer.Services;

import BusinessLayer.ApplicationFacade;
import BusinessLayer.EmployeeModule.Objects.Branch;
import BusinessLayer.EmployeeModule.Objects.Employee;
import BusinessLayer.EmployeeModule.Objects.Shift;
import ServiceLayer.Objects.EmployeeObjects.FEmployee;
import ServiceLayer.Objects.EmployeeObjects.FShift;
import ServiceLayer.Responses.Response;
import ServiceLayer.Responses.ResponseT;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class EmployeeService {

    private ApplicationFacade applicationFacade;

    public EmployeeService(ApplicationFacade applicationFacade) {
        this.applicationFacade = applicationFacade;
    }

    public ResponseT<FEmployee> getEmployee(int employeeID) {
        try {
            Employee employee = applicationFacade.getEmployee(employeeID);
            return new ResponseT<>(new FEmployee(employee));
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }
    public ResponseT<List<FEmployee>> getAllEmployees() {
        try {
            List<Employee> employees = applicationFacade.getAllEmployees();
            List<FEmployee> fEmployees = new Vector<>();
            for(Employee employee : employees)
                fEmployees.add(new FEmployee(employee));
            return new ResponseT<>(fEmployees);
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }
    public ResponseT<List<FEmployee>> getAvailableEmployeesForShift(String date, String shiftType) {
        try {
            List<Employee> employees = applicationFacade.getAvailableEmployeesForShift(date, shiftType);
            List<FEmployee> fEmployees = new Vector<>();
            for(Employee employee : employees)
                fEmployees.add(new FEmployee(employee));
            return new ResponseT<>(fEmployees);
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }
    public Response addEmployee(String name, int ID, String bank_Account, int salary, String employment_start_date , String jobTitle, String employmentDetails, String number) {
        try {
            applicationFacade.addEmployee(name, ID, bank_Account, salary, employment_start_date, jobTitle, employmentDetails,number);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
    public Response addEmployee(String name, int ID, String bank_Account, int salary, String employment_start_date , String jobTitle, String employmentDetails, Vector<String> certifications, String number) {
        try {
            applicationFacade.addEmployee(name, ID, bank_Account, salary, employment_start_date, jobTitle, employmentDetails, certifications,number);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
    public Response removeEmployee(int employeeID){
        try {
            applicationFacade.removeEmployee(employeeID);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }
    public Response addJobToEmployee(int employeeID, String newJob){
        try{
            applicationFacade.addJobToEmployee(employeeID, newJob);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }
    public Response removeJobFromEmployee(int employeeID, String jobTitle){
        try{
            applicationFacade.removeJobFromEmployee(employeeID, jobTitle);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }
    public Response addCertToEmployee(int empID, String jobTitle, String certName) {
        try{
            applicationFacade.addCertToEmployee(empID, jobTitle, certName);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }
    public Response addJobConstraint(int employeeID, String date, String shift){
        try{
            applicationFacade.addJobConstraint(employeeID, date, shift);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }
    public Response updateEmployeeName(int employeeID, String newName) {
        try {
            applicationFacade.updateEmployeeName(employeeID, newName);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
    public Response updateEmployeeBankAccount(int employeeID, String newBankAccount) {
        try {
            applicationFacade.updateEmployeeBankAccount(employeeID, newBankAccount);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
    public Response updateEmployeeSalary(int employeeID, int newSalary) {
        try {
            applicationFacade.updateEmployeeSalary(employeeID, newSalary);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
    public Response updateEmploymentDetails(int employeeID, String employmentDetails) {
        try {
            applicationFacade.updateEmploymentDetails(employeeID, employmentDetails);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    //////////////////SHIFTS////////////////////////
    public ResponseT<FShift> getShift(int shiftID) {
        try {
            Shift shift = applicationFacade.getShift(shiftID);
            return new ResponseT<>(new FShift(shift));
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }
    public String getBranchOfShift(int shiftID) {
        try {
            return applicationFacade.getBranchOfShift(shiftID);
        }
        catch (Exception e) {
            return "Not Found";
        }
    }
    public ResponseT<List<FShift>> getAllShifts() {
        try {
            List<Shift> shifts = applicationFacade.getAllShifts();
            List<FShift> fShifts = new Vector<>();
            for(Shift shift : shifts)
                fShifts.add(new FShift(shift));
            return new ResponseT<>(fShifts);
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }
    public ResponseT<List<FShift>> getShiftsOfEmployee(int employeeID){
        try {
            List<Shift> shifts = applicationFacade.getShiftsOfEmployee(employeeID);
            List<FShift> fShifts = new Vector<>();
            for(Shift shift : shifts)
                fShifts.add(new FShift(shift));
            return new ResponseT<>(fShifts);
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }
    public Response addShift(String addressBranch, String date, String shift_type, HashMap<Integer,String> employees){
        try {
            applicationFacade.addShift(addressBranch, date, shift_type, employees);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
    public Response removeShift(int shiftID){
        try {
            applicationFacade.removeShift(shiftID);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }
    public Response addEmployeeToShift(int shiftID, int eID, String job) {
        try {
            applicationFacade.addEmployeeToShift(shiftID, eID, job);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }
    public Response removeEmployeeFromShift(int shiftID, int oldID) {
        try {
            applicationFacade.removeEmployeeFromShift(shiftID, oldID);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }
    public ResponseT<FShift> getShiftByDateTypeAndBranch(String date, String typeShift, String branch) {
        try {
            Shift shift = applicationFacade.getShiftByDateTypeAndBranch(date, typeShift, branch);
            return new ResponseT<>(new FShift(shift));
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }
    public ResponseT<Boolean> validEmployeeHasJob(String jobTitle, String employeeID) {
        try {
            boolean ans = applicationFacade.validEmployeeHasJob(jobTitle,employeeID);
            return new ResponseT<>(ans);
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }
    public Response validEmployeesInShiftJob(HashMap<Integer, String> employeesToShift) {
        try {
            applicationFacade.validEmployeesInShiftJob(employeesToShift);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }
    public ResponseT<List<FShift>> getShiftLastMonth(String morning_evening, int employeeID) {
        try {
            List<Shift> shifts = applicationFacade.getShiftLastMonth(morning_evening, employeeID);
            List<FShift> fShifts = new Vector<>();
            for(Shift shift : shifts)
                fShifts.add(new FShift(shift));
            return new ResponseT<>(fShifts);
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }
    public ResponseT<List<FEmployee>> getDriversRelevant(int maxWeight, String date) {
        try {
            List<Employee> employees = applicationFacade.getDriversRelevant(maxWeight, date);
            List<FEmployee> fEmployees = new LinkedList<>();
            for(Employee employee : employees)
                fEmployees.add(new FEmployee(employee));
            return new ResponseT<>(fEmployees);
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }
    public ResponseT<List<String>> getDriverLicencesForDriver(int id) {
        try {
            List<String> ans = applicationFacade.getDriverLicencesForDriver(id);
            return new ResponseT<>(ans);
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    /////////////BRANCH/////////////////
    public ResponseT<List<String>> getAllBranchesByAddress() {
        try {
            List<String> branches = applicationFacade.getAllBranchesByAddress();
            return new ResponseT<>(branches);
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<Branch> getAddressOfHQ() {
        try {
            String hq = applicationFacade.getAddressOfHQ();
            return new ResponseT<>(new Branch(hq,1,"s"));
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<Integer>>  getDriversToSpecificShift(String date)throws Exception  {
        try {
            List<Integer> drivers = applicationFacade.getDriversToSpecificShift(date);
            return new ResponseT<>(drivers);
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<FEmployee>> getAvailableDrivers(String date) {
        try {
            List<Employee> employees = applicationFacade.getAvailableDrivers(date);
            List<FEmployee> fEmployees = new Vector<>();
            for(Employee employee : employees)
                fEmployees.add(new FEmployee(employee));
            return new ResponseT<>(fEmployees);
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<String>> pullMessages(String branchAddress,String job) {
        try {
            List<String> branches = applicationFacade.pullMessages(branchAddress,job);
            return new ResponseT<>(branches);
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<String>> pullMessagesForJob(String job) {
        try {
            List<String> branches = applicationFacade.pullMessagesForJob(job);
            return new ResponseT<>(branches);
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<String>> getAllBranchesEmployeeWorksIn(int id) {
        try {
            List<String> branches = applicationFacade.getAllBranchesEmployeeWorksIn(id);
            return new ResponseT<>(branches);
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }

    }

    public ResponseT<String> getBranchOfManager(int managerID) {
        try {
            String branch = applicationFacade.getBranchOfManager(managerID);
            ResponseT<String> responseT = new ResponseT<>();
            responseT.setValue(branch);
            return responseT;
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public Response addbranch(String addressBranch, String shippingArea, int managerID) {
        try {
            applicationFacade.addbranch(addressBranch, shippingArea, managerID);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removebranch(String addressBranch) {
        try {
            applicationFacade.removebranch(addressBranch);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
}
