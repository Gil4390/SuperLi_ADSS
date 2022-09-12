package DataAccessLayer.EmployeesModuleDal.Mappers;

import BusinessLayer.EmployeeModule.Objects.DriverMustWorkDate;
import DataAccessLayer.DeliveryModuleDal.DControllers.DalController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DriversConstraintsMapper extends DalController {
    private Map<Integer,String> driversMapper;

    public DriversConstraintsMapper()
    {
        super("DriverIsAvailable");
        this.driversMapper = new ConcurrentHashMap<>();
    }

    public void load() throws Exception
    {
        for (DriverMustWorkDate dmwd: selectAllDriversDates())
        {
            driversMapper.put(dmwd.getId(),dmwd.getDate());
        }
    }
    public List<String> getMustsOfDriver(int id) throws Exception {
        List<String> listOfMusts = new LinkedList<>();
        for (DriverMustWorkDate dmwd: selectAllDriversDates())
        {
            if (dmwd.getId() == id)
                listOfMusts.add(dmwd.getDate());
        }
        return listOfMusts;
    }

    public List<DriverMustWorkDate> selectAllDriversDates() throws Exception
    {
        return (List<DriverMustWorkDate>)(List<?>)select();
    }
    public boolean insert (int id, String date)
    {
        DriverMustWorkDate mustWorkDate = new DriverMustWorkDate(id,date);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}) VALUES(?,?)",
                getTableName(),"driverId","date");
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, mustWorkDate.getId());
            pstmt.setString(2, mustWorkDate.getDate());
            pstmt.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        driversMapper.put(id, date);
        return true;
    }
    public void deleteMustWorkDate(int id , String date) {
        checkDTOExists(id , date);
        delete(id,date, "driverId","date" );
        driversMapper.remove(id,date);
    }
    private void checkDTOExists(int id , String date){
        if(!driversMapper.containsKey(id) || !driversMapper.get(id).contains(date))
            throw new IllegalArgumentException("Employee is not exists in the database!");
    }


    protected Object ConvertReaderToObject(ResultSet reader) {
        DriverMustWorkDate result = null;
        try {
            result = new DriverMustWorkDate(reader.getInt(1),reader.getString(2));
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public void cleanCache() {
    }
}
