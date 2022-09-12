package DataAccessLayer.EmployeesModuleDal.Mappers;

import BusinessLayer.EmployeeModule.Objects.ShiftEmployee;
import DataAccessLayer.DeliveryModuleDal.DControllers.DalController;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShiftEmployeesMapper extends DalController {
    private Map<Integer,  Map<Integer , ShiftEmployee>> shiftEmployeesMap;


    public ShiftEmployeesMapper() {
        super("ShiftsEmployees");
        shiftEmployeesMap = new ConcurrentHashMap<>();
    }

    public List<ShiftEmployee> selectAllShiftsEmployees() {
        return (List<ShiftEmployee>)(List<?>)select();
    }
    public List<ShiftEmployee> getEmployeesShifts(int shiftID)
    {
        return (List<ShiftEmployee>)(List<?>)selectList(shiftID,"shiftID");
    }


    public boolean insert (int idS, int idE , String jobTitle)
    {
        ShiftEmployee dShiftsEmployees = new ShiftEmployee(idS , idE,jobTitle);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}) VALUES(?,?,?)",
                getTableName(),"ShiftID","EmployeeID","JobTitle");
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, dShiftsEmployees.getShiftID());
            pstmt.setInt(2, dShiftsEmployees.getEmployeeID());
            pstmt.setString(3, dShiftsEmployees.getJobTitle());;
            pstmt.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        if (shiftEmployeesMap.get(dShiftsEmployees.getShiftID())==null)
            shiftEmployeesMap.put(dShiftsEmployees.getShiftID(), new ConcurrentHashMap<>());
        shiftEmployeesMap.get(dShiftsEmployees.getShiftID()).put(dShiftsEmployees.getEmployeeID(),dShiftsEmployees);
        return true;

    }
    public void deleteEmployeeFromShift(int idS , int idE ) {
        checkDTOExists(idS,idE);
        ShiftEmployee dShiftsEmployees = shiftEmployeesMap.get(idS).get(idE);
        delete(idS,idE,"ShiftID","EmployeeID");
        shiftEmployeesMap.remove(idS).remove(idE);
    }

    private void checkDTOExists(int idS , int idE){
        if ( shiftEmployeesMap.size()==0)
        {
            for(ShiftEmployee SE : getEmployeesShifts(idS))
            {
                if (shiftEmployeesMap.get(idS)==null)
                    shiftEmployeesMap.put(idS,new HashMap<>());
                shiftEmployeesMap.get(idS).put(SE.getEmployeeID(),SE);
            }
        }

        if(!shiftEmployeesMap.containsKey(idS) || !(shiftEmployeesMap.get(idS).containsKey(idE)))
            throw new IllegalArgumentException("Employee in shift is not exists in the database!");
    }

    protected Object ConvertReaderToObject(ResultSet reader) {
        ShiftEmployee result = null;
        try {
            result = new ShiftEmployee(reader.getInt(1), reader.getInt(2), reader.getString(3));
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public void cleanCache() {
        Iterator<Map.Entry<Integer, Map<Integer, ShiftEmployee>>> iter = shiftEmployeesMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer,Map<Integer, ShiftEmployee>> entry = iter.next();
            for (ShiftEmployee e: entry.getValue().values()) {
                if(e.shouldCleanCache()){
                    System.out.println("Cleaning shift of employee "+e.getEmployeeID() +" from cache!");
                    iter.remove();
                }
            }
        }
    }
}
