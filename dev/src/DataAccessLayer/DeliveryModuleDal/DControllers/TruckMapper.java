package DataAccessLayer.DeliveryModuleDal.DControllers;

import BusinessLayer.DeliveryModule.Objects.Truck;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TruckMapper extends DalController {

    private final String LicenseNumberColumnName = "LicenseNumber";
    private final String ModelColumnName = "Model";
    private final String BaseWeightColumnName = "BaseWeight";
    private final String MaxWeightColumnName = "MaxWeight";

    private final Map<String, Truck> trucks;
    private final TruckAvailableMapper truckAvailableMapper;

    public TruckMapper(TruckAvailableMapper truckAvailableMapper) {
        super("Trucks");
        trucks = new ConcurrentHashMap<>();
        this.truckAvailableMapper = truckAvailableMapper;
    }

    public void load() throws Exception {
        for(Truck truck : selectAllTrucks())
            trucks.put(truck.getLicenseNumber(), truck);
    }


    public void setTruckUnavailable(String licenseNumber, String date) throws Exception {
        if (trucks.containsKey(licenseNumber))
            trucks.get(licenseNumber).setUnavailable(date);
        truckAvailableMapper.insert(licenseNumber,date);
    }

    public void checkIfTruckExists(String licenseNumber) throws Exception {
        try{
            getTruck(licenseNumber);
        }
        catch (Exception e){
            throw new Exception("truck does not exist");
        }
    }

    public Truck getTruck(String licenseNumber) throws Exception{
        if (trucks.containsKey(licenseNumber))
            return trucks.get(licenseNumber);
        try {
            Truck rs = (Truck) select(licenseNumber, LicenseNumberColumnName);
            List<String> datesPerId = truckAvailableMapper.getAllDatesUnAvailable(licenseNumber);
            Truck truck = createTruckObject(rs,datesPerId);
            trucks.put(licenseNumber, truck);
            return truck;
        }
        catch (Exception e) {
            throw new Exception("could not get truck");
        }
    }

    public List<Truck> getAllTrucksAvailable(String date) throws Exception {
        List<String> licensesNotAvailable = truckAvailableMapper.getAllTrucksAvailableByDate(date);
        List<Truck> allTrucks = selectAllTrucks();
        List<Truck> result = new LinkedList<>();
        for (Truck truck: allTrucks) {
            if (!licensesNotAvailable.contains(truck.getLicenseNumber()))
                result.add(truck);
        }
        return result;
    }

    private List<Truck> selectAllTrucks() throws Exception {
        try{
            List<Truck> allRs = (List<Truck>) (List<?>) select();
            List<Truck> results = new ArrayList<>();
            for (Truck rs : allRs) {
                String license = rs.getLicenseNumber();
                List<String> dates = truckAvailableMapper.getAllDatesUnAvailable(license);
                results.add(createTruckObject(rs, dates));
            }
            return results;
        }
        catch (Exception e) {
            throw new Exception("could not retrieve all trucks");
        }
    }

    public void insert(Truck truck) throws Exception {
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}) VALUES(?,?,?,?)",
                getTableName(),LicenseNumberColumnName,ModelColumnName,BaseWeightColumnName,MaxWeightColumnName);
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, truck.getLicenseNumber());
            pstmt.setString(2, truck.getModel());
            pstmt.setInt(3, truck.getBaseWeight());
            pstmt.setInt(4, truck.getMaxWeight());
            pstmt.executeUpdate();

            trucks.put(truck.getLicenseNumber(), truck);
            for (String date: truck.getIsAvailable()) {
                truckAvailableMapper.insert(truck.getLicenseNumber(), date);
            }
        }
        catch (Exception e) {
            throw new Exception("could not insert into trucks");
        }
    }

    private void delete(String license) throws Exception {
        Truck truck = trucks.get(license);
        if (truck != null)
            trucks.remove(license);
        try {
            truck = getTruck(license);
            if (truck != null) {
                delete("'"+truck.getLicenseNumber()+"'", LicenseNumberColumnName);
                for (int i = 0; i < truck.getIsAvailable().size(); i++) {
                    truckAvailableMapper.remove(truck.getLicenseNumber(), truck.getIsAvailable().get(i));
                }
            }
        }
        catch (Exception e){
            throw new Exception("could not delete delete");
        }
    }

    protected Truck createTruckObject(Truck reader, List<String> dates) throws Exception {
        return new Truck(reader.getLicenseNumber(), reader.getModel(), reader.getBaseWeight(), reader.getMaxWeight(), dates);
    }

    @Override
    protected Truck ConvertReaderToObject(ResultSet reader) throws Exception {
        return new Truck(reader.getString(1), reader.getString(2), reader.getInt(3), reader.getInt(4), new LinkedList<>());
    }

    @Override
    public void cleanCache() {
        Iterator<Map.Entry<String, Truck>> iter = trucks.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String,Truck> entry = iter.next();
            if(entry.getValue().shouldCleanCache()){
                System.out.println("Cleaning truck "+entry.getValue().getLicenseNumber() +" from cache!");
                iter.remove();
            }
        }
    }

    public void deleteAllDataFromDbs() {
        truckAvailableMapper.deleteAllData();
        deleteAllData();
    }
}
