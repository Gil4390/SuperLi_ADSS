package BusinessLayer.EmployeeModule;

import BusinessLayer.DeliveryModule.Objects.Truck;
import BusinessLayer.EmployeeModule.controllers.BranchController;
import BusinessLayer.EmployeeModule.controllers.EmployeeController;
import BusinessLayer.EmployeeModule.controllers.JobController;
import BusinessLayer.EmployeeModule.controllers.ShiftController;
import BusinessLayer.EmployeeModule.Objects.*;
import DataAccessLayer.DeliveryModuleDal.DControllers.CacheCleaner;
import DataAccessLayer.EmployeesModuleDal.Mappers.*;

import java.time.LocalDate;
import java.util.*;

public class EmployeeFacade {
    private final EmployeeController employeeController;
    private final ShiftController shiftController;
    private final JobController jobController;
    private final BranchController branchController;


    public EmployeeFacade(CacheCleaner cacheCleaner) {
        EmployeeMapper employeeMapper = EmployeeMapper.getInstance();
        JobConstrainsMapper jobConstrainsMapper = new JobConstrainsMapper();
        ShiftMapper shiftMapper = new ShiftMapper();
        JobsCertificationsMapper jobsCertificationsMapper = new JobsCertificationsMapper();
        DriversConstraintsMapper driversConstraintsMapper = new DriversConstraintsMapper();
        BranchMapper branchMapper = BranchMapper.getInstance();
        MessageMapper MessageMapper = new MessageMapper();

        this.employeeController = new EmployeeController(employeeMapper, jobConstrainsMapper);
        this.shiftController = new ShiftController(shiftMapper, driversConstraintsMapper);
        this.jobController= new JobController(jobsCertificationsMapper, driversConstraintsMapper , MessageMapper);
        this.branchController = new BranchController(branchMapper);

        cacheCleaner.loadMaps(Arrays.asList(employeeMapper, jobConstrainsMapper, shiftMapper, jobsCertificationsMapper, driversConstraintsMapper, branchMapper));
    }

    //employee-----------------------------------------------------------
    public Employee getEmployee(int employeeID) throws Exception{
        return employeeController.getEmployeeByID(employeeID);
    }
    public List<Employee> getEmployees(List<Integer> empIDs)throws Exception {
        return employeeController.getEmployees(empIDs);
    }
    public List<Employee> getAllEmployees() throws Exception{
         return employeeController.getAllEmployees();
    }
    public List<Employee> getAvailableEmployeesForShift(String date, String shiftType) throws Exception{
        return employeeController.getAvailableEmployeesForShift(LocalDate.parse(date), shiftType);
    }
    public void addEmployee(String name, int ID, String bank_Account, int salary, String employment_start_date , String jobTitle, String employmentDetails, String number) throws Exception {
        employeeController.addEmployee(name, ID, bank_Account, salary, employment_start_date,employmentDetails, number);
        jobController.addJobToEmployee(ID, jobTitle);
    }
    public void addEmployee(String name, int ID, String bank_Account, int salary, String employment_start_date,  String jobTitle, String employmentDetails, Vector<String> certifications,String number) throws Exception{
        employeeController.addEmployee(name, ID, bank_Account, salary, employment_start_date, employmentDetails,number);
        jobController.addJobToEmployee(ID, jobTitle, certifications);
    }
    public void removeEmployee(int employeeID) throws Exception {
        shiftController.removeEmployee(employeeID);
        jobController.removeEmployeeJob(employeeID);
        employeeController.removeEmployee(employeeID);
    }
    public void addJobToEmployee(int employeeID, String newJob) throws Exception{
        employeeController.validateEmployeeExists(employeeID);
        jobController.addJobToEmployee(employeeID, newJob);
    }
    public void removeJobFromEmployee(int employeeID, String jobTitle) throws Exception{
        shiftController.validateEmployeeJobRemoval(employeeID,jobTitle);
        jobController.removeJobFromEmployee(employeeID, jobTitle);
    }
    public void addJobConstraint(int employeeID, String date, String shift) throws Exception {
        shiftController.validateAddingEmployeeConstraint(employeeID, date, shift);
        employeeController.addJobConstraintToEmployee(employeeID, date, shift);
    }
    public void updateEmployeeName(int employeeID, String newName) throws Exception {
        employeeController.updateEmployeeName(employeeID, newName);
    }
    public void updateEmployeeBankAccount(int employeeID, String newBankAccount) throws Exception {
        employeeController.updateEmployeeBankAccount(employeeID, newBankAccount);
    }
    public void updateEmployeeSalary(int employeeID, int newSalary) throws Exception {
        employeeController.updateEmployeeSalary(employeeID, newSalary);
    }
    public void updateEmploymentDetails(int employeeID, String employmentDetails) throws Exception {
        employeeController.updateEmploymentDetails(employeeID, employmentDetails);
    }

    //shift-------------------------------------------------------------------
    public Shift getShift(int shiftID) throws Exception {
        return shiftController.getShiftByID(shiftID);
    }
    public List<Shift> getAllShifts() throws Exception {
        return shiftController.getAllShifts();
    }
    public List<Shift> getShiftsOfEmployee(int employeeID) throws Exception {
        employeeController.validateEmployeeExists(employeeID);
        return shiftController.getShiftsOfEmployee(employeeID);
    }
    public void addShift(String addressBranch, String date, String shift_type, HashMap<Integer,String>  employees) throws Exception {
        boolean onlyDrivers = true;
        String shiftMnagaerOrCEO= "";
        String HQAddress = getAddressOfHQ();
        for (String s: employees.values())
        {
            if (s.equals("CEO") || s.equals("shift manager"))
            {
                if (s.equals("CEO"))
                    shiftMnagaerOrCEO = "CEO";
                else
                    shiftMnagaerOrCEO = "shift manager";
            }
            else {
                if (!s.equals("driver")) {
                    onlyDrivers = false;
                }
                else{
                    if(!addressBranch.equals(HQAddress)){
                        throw new Exception("Drivers can only be assigned to shifts in the HQ");
                    }
                }
            }

        }
        if (onlyDrivers && shiftMnagaerOrCEO.equals("shift manager"))
            throw new Exception("shift manager of drivers is the CEO");
        if (onlyDrivers && !(addressBranch.equals(getAddressOfHQ())))
        {
            throw new Exception("Drivers can assign to shift only on HQ");
        }
        if (!addressBranch.equals(getAddressOfHQ()) && employees.containsValue("driver") && onlyDrivers)
            throw new Exception("Drivers can't assign in shift except the HQ");

        employeeController.validateJobConstraintsOfEmployeesForShift(employees, date, shift_type);
        shiftController.addShift(addressBranch,date, shift_type,findShiftManager(employees),employees);
    }
    public void removeShift(int shiftID) throws Exception {
        shiftController.removeShift(shiftID);
    }
    public Shift getShiftByDateTypeAndBranch(String date , String shiftType, String branch) throws Exception {
        return shiftController.getShiftByDateTypeAndBranch(date, shiftType, branch);
    }
    private int findShiftManager(HashMap<Integer,String>employees) throws Exception {
        for (Map.Entry<Integer, String> entry: employees.entrySet())
            if (entry.getValue().equals("shift manager") || entry.getValue().equals("CEO"))
                if(jobController.hasJob(entry.getKey(), "shift manager") || jobController.hasJob(entry.getKey(), "CEO"))
                    return entry.getKey();
        throw new Exception("employee list does not contain shift manager");
    }

    public void addEmployeeToShift(int shiftID, int employeeID,String job) throws Exception{
        Shift shift = shiftController.getShiftByID(shiftID);
        if (validEmployeeHasJob(job,employeeID+"")){
            employeeController.validateJobConstraintOfEmployee(employeeID, shift.getDate(), shift.getShiftType());

            shiftController.addEmployeeToShift(shiftID, employeeID,job);
        }
        else
        {
            throw new Exception("Employee hasn't qualified to work at this job");
        }
    }
    public void removeEmployeeFromShift(int shiftID, int employeeID) throws Exception{
        shiftController.removeEmployeeFromShift(shiftID, employeeID);
    }
    public boolean validEmployeeHasJob(String jobTitle, String employeeID) throws Exception {
        return jobController.hasJob(Integer.parseInt(employeeID), jobTitle);
    }
    public void validEmployeesInShiftJob(HashMap<Integer, String> employeesToShift) throws Exception{
        shiftController.validJobsInShift(employeesToShift);
    }
    public List<Shift> getShiftLastMonth(String morning_evening , int employeeID) throws Exception{
        employeeController.validateEmployeeExists(employeeID);
        return shiftController.shiftLastMonth(morning_evening, employeeID);
    }

    public void checkDriverValidity(String date, int driverId, int maxTruckWeight) throws Exception {
        jobController.validateDriver(date, driverId, maxTruckWeight);
    }

    public List<Truck> getRelevanteTrucks(List<Truck> trucks, int driverId) throws Exception {
        List<Truck> finalTrucks = new LinkedList<>();
        for (Truck truck : trucks) {
            if (jobController.checkTruckForDriver(driverId, truck.getMaxWeight()))
                finalTrucks.add(truck);
        }
        return finalTrucks;
    }

    public void setDriverUnavailable(int driverId, String date) throws Exception {
        jobController.setDriverUnAvailable(driverId, date);
    }


    public List<Employee> getDriversRelevant(int maxWeight, String date) throws Exception {
        List<Integer> relevantDrivers= new LinkedList<>();
        //In case : Has shift , bring who works in shift and doesn't have a delivery
        List<Integer> employeeThatWorksInShift= new LinkedList<>();
        //Employees working in shift in date
        //list of drivers that have delivery
        List<Integer> HaveDeliveryDrivers= jobController.getDriversToSpecificShift(date);
        if (shiftExist(date, "morning", getAddressOfHQ()) | shiftExist(date, "evening", getAddressOfHQ()))
        {
            employeeThatWorksInShift = shiftController.getDriversOfShift(date, getAddressOfHQ());


            //filter in shift & no delivery
            for(int empID :employeeThatWorksInShift)
                if (!HaveDeliveryDrivers.contains(empID)) relevantDrivers.add(empID);
        }
        else
        {
            //In case : Has no shift, bring doesn't have job constrains
            List<Employee> employees = getAllEmployees();
            for(Employee empID: employees)
            {
                try
                {
                    // If go throw these 2 validations, employee doesn't have constrains in the date
                    employeeController.validateJobConstraintOfEmployee(empID.getID(), LocalDate.parse(date),"morning");
                    employeeController.validateJobConstraintOfEmployee(empID.getID(), LocalDate.parse(date),"evening");
                    if(!HaveDeliveryDrivers.contains(empID.getID()))
                        relevantDrivers.add(empID.getID());
                }
                catch (Exception ignored){}
            }
        }
        List<Integer> relevantIDs = jobController.filterDriversByMaxWeight(relevantDrivers, maxWeight);
        return employeeController.getEmployees(relevantIDs);
    }

    private boolean shiftExist(String date, String type, String branch) {
        try { shiftController.getShiftByDateTypeAndBranch(date, type, branch); return true;}
        catch (Exception e){return false;}
    }

    public void checkDriverValidityForTruck(int driverId, int maxTruckWeight) throws Exception {
        jobController.checkTruckForDriver(driverId, maxTruckWeight);
    }

    public List<String> getDriverLicencesForDriver(int id) throws Exception {
         return jobController.getDriverLicencesForDriver(id);
    }

    public List<String> getAllBranchesByAddress() throws Exception {
        return branchController.getAllBranchesByAddress();
    }

    public String getAddressOfHQ() throws Exception{
        return branchController.getAddressOfHQ().get(0);
    }


    public List<Integer> getDriversToSpecificShift(String date) throws Exception {
        return jobController.getDriversToSpecificShift(date);
    }

    public void addCertToEmployee(int empID, String jobTitle, String certName) throws Exception {
        employeeController.validateEmployeeExists(empID);
        jobController.addCertToEmployee(empID, jobTitle, certName);
    }

    public List<Employee> getAvailableDrivers(String date) throws Exception {
        List<Employee> freeInMorning = new LinkedList<>(getAvailableEmployeesForShift(date, "morning"));
        List<Employee> freeInNight = new LinkedList<>(getAvailableEmployeesForShift(date, "evening"));
        List<Employee> freeInDate = new LinkedList<>();
        for (Employee emp1: freeInNight) {
            for (Employee emp2 : freeInMorning) {
                if (emp1.getID() == emp2.getID()) {
                    if (validEmployeeHasJob("driver", emp1.getID() + ""))
                        freeInDate.add(emp1);
                }
            }
        }
        return freeInDate;
    }

    public void deleteAllData() {
        shiftController.deleteAllData();
        jobController.deleteAllData();
        employeeController.deleteAllData();
        branchController.deleteAllData();
    }

    public List<String> pullMessages(String branchAddress,String job) throws Exception {
        return jobController.pullMessages(branchAddress,job);
    }

    public List<String> pullMessagesForJob(String job) throws Exception {
        return jobController.pullMessagesForJob(job);
    }

    public List<String> getAllBranchesEmployeeWorksIn(int id) throws Exception {
        List<String> branches = new LinkedList<>();
        List<Shift> shifts = getShiftsOfEmployee(id);
        for (Shift s : shifts)
        {
            if (!branches.contains(s.getBranchAddress()))
                branches.add(s.getBranchAddress());
        }
        return branches;
    }

    public void pushMessage(String job, String Branch, String msg){
        jobController.pushMessage(Branch,job,msg);

    }

    public void pushMessage(String job, String msg) throws Exception {
        for (String address : getAllBranchesByAddress())
        {
            jobController.pushMessage(address,job,msg);
        }
    }

    public boolean isABranch(String siteAddress) throws Exception {
        for (String address : getAllBranchesByAddress())
        {
            if (address.equals(siteAddress))
                return true;
        }
        return false;
    }

    public String getBranchOfManage(int managerID) throws Exception {
        return branchController.getBranchOfManage(managerID);
    }

    public void addbranch(String addressBranch, String shippingArea, int managerID) throws Exception {
        branchController.addBranch(addressBranch,managerID,shippingArea);
    }

    public void removebranch(String addressBranch)throws Exception {
        branchController.removeBranch(addressBranch);
    }

}
