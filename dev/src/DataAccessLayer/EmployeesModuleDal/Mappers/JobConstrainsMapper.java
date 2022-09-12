package DataAccessLayer.EmployeesModuleDal.Mappers;

import DataAccessLayer.DeliveryModuleDal.DControllers.DalController;
import BusinessLayer.EmployeeModule.Objects.EmployeeJobConstraint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JobConstrainsMapper extends DalController {

    private Map<Integer, List<EmployeeJobConstraint>> dEmployeeJobConstraintMap;
    public JobConstrainsMapper() {
        super("EmployeeJobConstraint");
        dEmployeeJobConstraintMap = new ConcurrentHashMap<>();
    }

    public void load() throws Exception {
        for (EmployeeJobConstraint dJobCertifications: selectAllJobContains()) {
            if (dEmployeeJobConstraintMap.get(dJobCertifications.getEmployeeID())== null)
            {
                dEmployeeJobConstraintMap.put(dJobCertifications.getEmployeeID(), new ArrayList<>());
            }
            dEmployeeJobConstraintMap.get(dJobCertifications.getEmployeeID()).add(dJobCertifications);
        }

    }
    public List<EmployeeJobConstraint> selectAllJobContains() throws Exception {
        return (List<EmployeeJobConstraint>)(List<?>)select();
    }
    public List<EmployeeJobConstraint> selectJobContainsOfEmployee(int EmployeeID) throws Exception {
        List<EmployeeJobConstraint> cons = new LinkedList<>();
        for (EmployeeJobConstraint dejc: selectAllJobContains())
        {
            if (dejc.getEmployeeID()==EmployeeID)
                cons.add(dejc);
        }
        return cons;
    }

    public boolean insert (int id,String Date, String ShiftType)
    {
        EmployeeJobConstraint dEmployeeJobConstraint = new EmployeeJobConstraint(id , Date,ShiftType);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}) VALUES(?,?,?)",
                getTableName(), "EmployeeID","Date","ShiftType");
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, dEmployeeJobConstraint.getEmployeeID());
            pstmt.setString(2, dEmployeeJobConstraint.getDate());
            pstmt.setString(3, dEmployeeJobConstraint.getShiftType());
            pstmt.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        if(!dEmployeeJobConstraintMap.containsKey(id)){
            dEmployeeJobConstraintMap.put(id, new LinkedList<>());
        }
        dEmployeeJobConstraintMap.get(id).add(dEmployeeJobConstraint);
        return true;


    }

    public void deleteJobConstarint(int id , String Date , String shiftType) throws Exception {
        if (dEmployeeJobConstraintMap.size()==0)
            dEmployeeJobConstraintMap.put(id,selectJobContainsOfEmployee(id));
        checkDTOExists(id);
        for (EmployeeJobConstraint dEmployeeJobConstraint : dEmployeeJobConstraintMap.get(id))
        {
            if (dEmployeeJobConstraint.getDate().equals(Date) && dEmployeeJobConstraint.getShiftType().equals(shiftType)) {
                delete(dEmployeeJobConstraint.getEmployeeID(),Date,shiftType,"EmployeeID","Date","ShiftType");
                dEmployeeJobConstraintMap.get(id).remove(dEmployeeJobConstraint);
            }
        }
    }
    private void checkDTOExists(int id){
        if(!dEmployeeJobConstraintMap.containsKey(id))
            throw new IllegalArgumentException("Destination is not exists in the database!");
    }
    protected Object ConvertReaderToObject(ResultSet reader) {
        EmployeeJobConstraint result = null;
        try {
            result = new EmployeeJobConstraint(reader.getInt(1), reader.getString(2), reader.getString(3));
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public void cleanCache() {
        Iterator<Map.Entry<Integer, List<EmployeeJobConstraint>>> iter = dEmployeeJobConstraintMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer,List<EmployeeJobConstraint>> entry = iter.next();
            for (EmployeeJobConstraint e: entry.getValue()) {
                if(e.shouldCleanCache()){
                    System.out.println("Cleaning job constraint of employee "+e.getEmployeeID() +" from cache!");
                    iter.remove();
                }
            }
        }
    }
}
