package BusinessLayer.EmployeeModule.controllers;

import BusinessLayer.EmployeeModule.Objects.Shift;
import DataAccessLayer.EmployeesModuleDal.Mappers.DriversConstraintsMapper;
import DataAccessLayer.EmployeesModuleDal.Mappers.ShiftMapper;

import java.time.LocalDate;
import java.util.*;

public class ShiftController {

    private ShiftMapper shiftMapper;
    private DriversConstraintsMapper driversConstraintsMapper;
    private final String[] jobsInShift= {"warehouse","usher","cashier","shift manager"};
    public ShiftController(ShiftMapper shiftMapper, DriversConstraintsMapper driversConstraintsMapper) {
        this.shiftMapper = shiftMapper;
        this.driversConstraintsMapper = driversConstraintsMapper;
        this.removePassedDateShifts();
    }
    public int getNEXT_SHIFT_ID() throws Exception
    {
        //return shiftMapper.selectAllShifts().size();
        int max = 0;
        for (Shift shift:shiftMapper.selectAllShifts())
        {
            if (max < shift.getShiftID())
                max = shift.getShiftID();
        }
        return max;
    }
    public Shift getShiftByID(int shiftID) throws Exception{
        validateShiftExist(shiftID);
        return shiftMapper.getShift(shiftID);
    }
    public Shift getShiftByDateTypeAndBranch(String date, String shiftType, String branch) throws Exception{
        Shift shift = shiftMapper.getShift(date, shiftType, branch);
        if (shift==null)
            throw new Exception("Shift with given date, type and branch was not found");
        return shift;
    }
    public List<Shift> getAllShifts() {
        return shiftMapper.selectAllShifts();
    }
    public int addShift(String branchAddress,String date, String shift_type, int shiftManagerID , HashMap<Integer,String> employees) throws Exception {
        if (getShiftsByDateAndType(date,shift_type)!= null)
        {
            for (Shift shift: getShiftsByDateAndType(date,shift_type))
            {
                if (shift.getBranchAddress().equals(branchAddress))
                    throw new Exception("Shift already exist");
            }
        }
        validJobsInShift(employees);
        for (int empId : employees.keySet())
        {
            for (String dateMust:driversConstraintsMapper.getMustsOfDriver(empId))
                if (dateMust.equals(date))
                {
                    throw new Exception("Can't add driver with delivery set in the date");
                }
        }
        shiftMapper.insert(getNEXT_SHIFT_ID()+1,branchAddress, date, shift_type, shiftManagerID ,employees);
        return getNEXT_SHIFT_ID();
    }
    public void removeShift(int shiftID) {
        validateShiftExist(shiftID);
        shiftMapper.deleteShift(shiftID);
    }

    public void addEmployeeToShift(int shiftID, int employeeID, String jobInShift) throws Exception {
        validateShiftExist(shiftID);
        String date = String.valueOf(getShiftByID(shiftID).getDate());
        String type = getShiftByID(shiftID).getShiftType();
        validateEmployeeIsAvailableOnDateAndType(employeeID, date, type);
        for (String dateMust:driversConstraintsMapper.getMustsOfDriver(employeeID))
            if (dateMust.equals(date))
            {
                throw new Exception("Can't add driver with delivery set in the date");
            }
        shiftMapper.addEmployeeToShift(shiftID, employeeID, jobInShift);
    }

    private void validateEmployeeIsAvailableOnDateAndType(int employeeID, String date, String type) throws Exception {
        for (Shift shift : shiftMapper.getShifts(date, type)){
            if(shift.isEmployeeInShift(employeeID)){
                throw new Exception("employee already assigned to a shift on a different branch");
            }
        }
    }

    public void removeEmployeeFromShift(int shiftID, int employeeID) throws Exception {
        validateShiftExist(shiftID);
        Shift shift = getShiftByID(shiftID);
        HashMap<Integer,String> temp= shift.getEmployees();
        temp.remove(employeeID);
        if (!temp.containsValue("driver"))
            validJobsInShift(temp);
        else
        {
            if (employeeID==1)
                throw new Exception("CEO is shift manager of drivers , cant remove him from shift");
        }
        //validateEmployeeRemoval(employeeID);
        if (driversConstraintsMapper.getMustsOfDriver(employeeID).size()!=0)
        {
            throw new Exception("Can't remove driver that has future delivery");
        }
        else
            shiftMapper.deleteEmployeeToShift(shiftID, employeeID);
    }

    public void removePassedDateShifts() {
        for (Shift shift: getAllShifts())
        {
            if (shift.getDate().isBefore( LocalDate.now().minusDays(185)))
                removeShift(shift.getShiftID());
        }

    }

    public List<Shift> getShiftsOfEmployee(int employeeID) throws Exception {
        List<Shift> shiftsOfEmployees = new LinkedList<>();
        for (Shift shift: shiftMapper.selectAllShifts())
        {
            if (shift.getEmployees().containsKey(employeeID))
                shiftsOfEmployees.add(shift);
        }
        return shiftsOfEmployees;
    }

    public void validateEmployeeRemoval(int employeeID) throws Exception {
        for (Shift shift : getShiftsOfEmployee(employeeID)){
            if (!shift.getDate().isAfter(LocalDate.now())){
                continue;
            }
            if(shift.getShiftManager() == employeeID){
                throw new Exception("cannot remove shift manager, remove him from future shifts and try again");
            }
            if (shift.getEmployees().containsKey(employeeID)){
                throw new Exception("cannot remove employee, remove him from future shifts and try again");
            }

        }
    }


    public void validateEmployeeJobRemoval(int employeeID, String job) throws Exception {
        for (Shift shift : getShiftsOfEmployee(employeeID)){
            if (!shift.getDate().isAfter(LocalDate.now())){
                continue;
            }
            if(shift.getEmployees().get(employeeID).equals(job)){
                throw new Exception("cannot remove employee, remove him from future shifts and try again");
            }
        }
    }

    public void validJobsInShift(HashMap<Integer, String> employeesToShift) throws Exception {
        if (!employeesToShift.containsValue("driver"))
        {
            List<String> temp = new LinkedList<>();
            temp.addAll(Arrays.asList(jobsInShift));
            for (int employeeID: employeesToShift.keySet()) {
                String job = employeesToShift.get(employeeID);
                temp.remove(job);
            }
            if (!temp.isEmpty())
                throw new Exception("cannot save shift , there are jobs missing");
        }
    }

    public List<Shift> shiftLastMonth(String morning_evening , int employeeID) throws Exception {
        List<Shift> shiftsReturn = new LinkedList<>();
        for (Shift shift: getShiftsOfEmployee(employeeID)) {
            if(shift.getDate().isAfter(LocalDate.now().minusDays(31)) && shift.getDate().isBefore(LocalDate.now()) && shift.getShiftType().equals(morning_evening)) {
                shiftsReturn.add(shift);
            }
        }
        return shiftsReturn;
    }
    private void validateShiftExist(int sID) {
        if (shiftMapper.getShift(sID)==null){
            throw new IllegalArgumentException("Shift with ID: " + sID + " does not exist");
        }
    }

    public void validateAddingEmployeeConstraint(int employeeID, String date, String shiftType) throws Exception{
        List<Shift> shiftsOnDate = getShiftsByDateAndType(date, shiftType);
        if (shiftsOnDate!=null)
        {
            for (Shift shift : shiftsOnDate){
                if(shift.isEmployeeInShift(employeeID)){
                    throw new Exception("can't add constraint because you are already assigned to a shift on this date, contact an hr manager to reassign you");
                }
            }
        }
    }

    private List<Shift> getShiftsByDateAndType(String date, String shiftType) {
        return shiftMapper.getShifts(date, shiftType);
    }

    public void removeEmployee(int employeeID) throws Exception {
        validateEmployeeRemoval(employeeID);
    }

    public void deleteAllData() {
        shiftMapper.deleteAllData();
    }

    public List<Integer> getDriversOfShift(String date, String hqAddress) throws Exception {
        List<Integer> ans = new LinkedList<>();
        Shift morning = null;
        Shift evening = null;
        try {
            morning = getShiftByDateTypeAndBranch(date, "morning", hqAddress);
        }
        catch (Exception ignored) { }
        try {
            evening = getShiftByDateTypeAndBranch(date, "evening", hqAddress);
        }
        catch (Exception ignored) { }
        if(morning != null)
            for (Map.Entry<Integer,String> jobs: morning.getEmployees().entrySet())
                if (jobs.getValue().equals("driver") && !ans.contains(jobs.getKey()))
                    ans.add(jobs.getKey());
        if(evening!=null)
            for (Map.Entry<Integer,String> jobs: evening.getEmployees().entrySet())
                if (jobs.getValue().equals("driver")&& !ans.contains(jobs.getKey())) ans.add(jobs.getKey());
        return ans;
    }

    public List<Integer> getEmployeesWorking(String date, String shiftType) {
        return shiftMapper.getEmployeesWorking(date, shiftType);
    }
}
