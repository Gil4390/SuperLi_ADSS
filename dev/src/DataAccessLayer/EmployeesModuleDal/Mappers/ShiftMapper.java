package DataAccessLayer.EmployeesModuleDal.Mappers;

import BusinessLayer.EmployeeModule.Objects.Shift;
import BusinessLayer.EmployeeModule.Objects.ShiftEmployee;
import DataAccessLayer.DeliveryModuleDal.DControllers.DalController;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ShiftMapper extends DalController {

    private Map<Integer, Shift> ShiftMapper;
    private ShiftEmployeesMapper shiftEmployeesMapper;
    public ShiftMapper() {
        super("Shifts");
        ShiftMapper = new ConcurrentHashMap<>();
        shiftEmployeesMapper = new ShiftEmployeesMapper();
    }


    public Shift getShift(int shiftID) {
        Shift shift =(Shift)select(shiftID,"ShiftID");
        if(shift == null) return null;
        for (ShiftEmployee se : shiftEmployeesMapper.getEmployeesShifts(shift.getShiftID()))
        {
            shift.addEmployee(se.getEmployeeID(),se.getJobTitle());
        }
        return shift;
    }
    public Shift getShift(String date, String shiftType, String branch) throws Exception {
        Shift shift;
        try {
             shift = ((List<Shift>) (List<?>) selectList(date, "Date", shiftType, "ShiftType", branch, "BranchAddress")).get(0);
        }
        catch (Exception e)
        {
            shift = null;
        }
        if(shift == null) return null;
        for (ShiftEmployee se : shiftEmployeesMapper.getEmployeesShifts(shift.getShiftID()))
        {
            shift.addEmployee(se.getEmployeeID(),se.getJobTitle());
        }
        return shift;
    }

    public List<Shift> selectAllShifts() {
        List<Shift> shifts = (List<Shift>)(List<?>)select();
        for (Shift s: shifts)
        {
            for (ShiftEmployee dse : shiftEmployeesMapper.selectAllShiftsEmployees())
            {
                if (dse.getShiftID()==s.getShiftID())
                    s.addEmployee(dse.getEmployeeID(),dse.getJobTitle());
            }
        }
        return shifts;
    }

    public boolean insert (int id, String branchAddress, String date, String shiftType, int managerID , HashMap<Integer,String> employees )
    {
        //Shift dShift = new Shift(id , branchAddress,date,shiftType,managerID);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4} , {5}) VALUES(?,?,?,?,?)",
                getTableName(),"ShiftID","BranchAddress","Date","ShiftType","ManagerID");
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, branchAddress);
            pstmt.setString(3, date);
            pstmt.setString(4, shiftType);
            pstmt.setInt(5, managerID);
            pstmt.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        for (int Eid: employees.keySet()) {
            shiftEmployeesMapper.insert(id, Eid, employees.get(Eid));
        }
        ShiftMapper.put(id, new Shift(id,branchAddress, LocalDate.parse(date),shiftType,managerID,employees));
        return true;

    }
    public void deleteShift(int id) {
        checkDTOExists(id);
        shiftEmployeesMapper.delete(id, "ShiftID");
        delete(id, "ShiftID");
        ShiftMapper.remove(id);
    }
    private void checkDTOExists(int id){
        if(getShift(id)==null)
            throw new IllegalArgumentException("Destination is not exists in the database!");
    }

    protected Object ConvertReaderToObject(ResultSet reader) {
        Shift result = null;
        try {

            result = new Shift(reader.getInt(1),reader.getString(2),LocalDate.parse(reader.getString(3)),reader.getString(4),reader.getInt(5),new HashMap<>());
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public void addEmployeeToShift(int idS , int idE , String jobtitle) {
        boolean check = false;
        try
        {
            for (ShiftEmployee se : shiftEmployeesMapper.getEmployeesShifts(idE))
            {
                if (getShift(idS).getDate().equals(getShift(se.getShiftID()).getDate()) && !getShift(idS).getBranchAddress().equals(getShift(se.getShiftID()).getBranchAddress()))
                {
                    check = true;
                }
            }
            if (!check)
                shiftEmployeesMapper.insert(idS, idE, jobtitle);
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
        }

    }
    public void deleteEmployeeToShift(int idS , int idE)
    {
        shiftEmployeesMapper.deleteEmployeeFromShift(idS, idE);
    }

    public List<Shift> getShifts(String date, String shiftType) {
        List<Shift> shifts = ((List<Shift>)(List<?>)selectList(date,"Date",shiftType,"ShiftType"));
        for (Shift shift : shifts) {
            for (ShiftEmployee se : shiftEmployeesMapper.getEmployeesShifts(shift.getShiftID())) {
                shift.addEmployee(se.getEmployeeID(), se.getJobTitle());
            }
        }
        return shifts;
    }

    @Override
    public void cleanCache() {
        Iterator<Map.Entry<Integer, Shift>> iter = ShiftMapper.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer,Shift> entry = iter.next();
            if(entry.getValue().shouldCleanCache()){
                System.out.println("Cleaning shift "+entry.getValue().getShiftID() +" from cache!");
                iter.remove();
            }
        }
    }

    public void deleteAllData(){
        String sql = "DELETE FROM "+"ShiftsEmployees";
        try(Connection conn = this.connect()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
        }

        sql = "DELETE FROM "+"Shifts";
        try(Connection conn = this.connect()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Integer> getEmployeesWorking(String date, String shiftType) {
        List<Shift> shifts =(List<Shift>)(List<?>) selectList(date, "Date", shiftType, "ShiftType");
        List<Integer> employeeIds = new LinkedList<>();
        for (Shift s: shifts){
            List<ShiftEmployee> se = shiftEmployeesMapper.getEmployeesShifts(s.getShiftID());
            for(ShiftEmployee shiftEmployee: se)
                if (!employeeIds.contains(shiftEmployee)) employeeIds.add(shiftEmployee.getEmployeeID());
        }
        return employeeIds;

    }
}
