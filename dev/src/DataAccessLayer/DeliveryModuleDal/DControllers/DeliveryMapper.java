package DataAccessLayer.DeliveryModuleDal.DControllers;

import BusinessLayer.DeliveryModule.Objects.Delivery;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DeliveryMapper extends DalController {

    private final String DeliveryIdColumnName = "Id";
    private final String DateColumnName = "Date";
    private final String TimeColumnName = "Time";
    private final String TruckLicenseColumnName = "TruckLicense";
    private final String DriverIdColumnName = "DriverId";

    private Map<Integer, Delivery> deliveries;
    private final DestMapper destMapper;

    public DeliveryMapper(DestMapper destMapper) {
        super("Deliveries");
        deliveries = new ConcurrentHashMap<>();
        this.destMapper = destMapper;
    }

    public void load() throws Exception {
        for(Delivery delivery : selectAllDeliveries())
            deliveries.put(delivery.getId(), delivery);
    }

    public void removeAddressFromDelivery(int deliveryId, int Location) throws Exception {
        try {
            destMapper.delete(deliveryId, Location);
            if (deliveries.containsKey(deliveryId))
                deliveries.get(deliveryId).removeAddress(Location);
        }
        catch (Exception e){
            throw new Exception("could not remove address from delivery " + deliveryId);
        }
    }

    public void changeAddressListForDelivery(int deliveryId, List<String> siteAddresses) throws Exception {
        try {
            if (deliveries.containsKey(deliveryId))
                deliveries.get(deliveryId).changeListOfAddresses(siteAddresses);
            destMapper.changeAddressListForDelivery(deliveryId, siteAddresses);
            if (destMapper.selectListAddress(deliveryId).size()<=1) delete(deliveryId);
        }
        catch (Exception e){
            throw new Exception("could not change address list " + deliveryId);
        }
    }

    public void changeTruckForDelivery(int deliveryId, String truckLicense) throws Exception {
        if (deliveries.containsKey(deliveryId))
            deliveries.get(deliveryId).changeTruck(truckLicense);
        if(!update(deliveryId, TruckLicenseColumnName, "'"+truckLicense+"'", DeliveryIdColumnName))
            throw new Exception("could not update truck " + truckLicense + " for delivery " + deliveryId);
    }

    public void checkDeliveryExist(int deliveryId) throws Exception {
        try{
            getDelivery(deliveryId);
        }
        catch (Exception e){
            throw new Exception("delivery does not exist");
        }
    }

    public List<Delivery> getAllDeliveries() throws Exception {
        try {
            List<Delivery> ans = selectAllDeliveries();
            for (Delivery delivery: ans)
                if (!deliveries.containsKey(delivery.getId()))
                    deliveries.put(delivery.getId(), delivery);
            return ans;
        }
        catch (Exception e){
            throw new Exception("could not get all deliveries");
        }
    }

    public Delivery getDelivery(int deliveryId) throws Exception {
        if (deliveries.containsKey(deliveryId))
            return deliveries.get(deliveryId);
        try {
            Delivery rs = (Delivery) select(deliveryId, DeliveryIdColumnName);
            List<String> destPerId = destMapper.selectListAddress(deliveryId);
            Delivery delivery = createDeliveryObject(rs,destPerId);
            deliveries.put(deliveryId, delivery);
            return delivery;
        }
        catch (Exception e) {
            throw new Exception("could not get Delivery");
        }
    }

    private List<Delivery> selectAllDeliveries() throws Exception {
        try {
            List<Delivery> allRs = (List<Delivery>) (List<?>) select();
            List<Delivery> ans = new LinkedList<>();
            for (Delivery rs : allRs) {
                int id = rs.getId();
                List<String> destPerId = destMapper.selectListAddress(id);
                ans.add(createDeliveryObject(rs, destPerId));
            }
            return ans;
        }
        catch (Exception e){
            throw new Exception("could not retrieve all deliveries");
        }
    }

    private int getIdFromRes(ResultSet rs) throws Exception {
        int result;
        result = rs.getInt(1);
        return result;
    }

    public void insert(Delivery delivery) throws Exception {
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}) VALUES(?,?,?,?,?)",
                getTableName(),DeliveryIdColumnName,DateColumnName,TimeColumnName,TruckLicenseColumnName, DriverIdColumnName);
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, delivery.getId());
            pstmt.setString(2, delivery.getDate());
            pstmt.setString(3, delivery.getTime());
            pstmt.setString(4, delivery.getTruckLicense());
            pstmt.setInt(5, delivery.getDriverId());
            pstmt.executeUpdate();

            deliveries.put(delivery.getId(), delivery);
            for (int i = 0; i < delivery.getListOfAddresses().size(); i++) {
                destMapper.insert(delivery.getId(), delivery.getListOfAddresses().get(i), i);
            }
        }
        catch (Exception e) {
            throw new Exception("could not insert into deliveries");
        }
    }

    public void delete(int id) throws Exception {
        Delivery delivery = deliveries.get(id);
        if (delivery != null)
            deliveries.remove(id);
        try {
            delivery = getDelivery(id);
            if (delivery != null) {
                delete(delivery.getId(), DeliveryIdColumnName);
                for (int i = 0; i < delivery.getListOfAddresses().size(); i++) {
                    destMapper.delete(delivery.getId(), i);
                }
            }
        }
        catch (Exception e){
            throw new Exception("could not delete delivery");
        }
    }

    private Delivery createDeliveryObject(Delivery reader, List<String> dests) throws Exception {
        Delivery result;
        String source = dests.get(0);
        dests.remove(0);
        result = new Delivery(reader.getId(), reader.getDate(), reader.getTime(), reader.getTruckLicense(), reader.getDriverId(), source, dests);
        return result;
    }

    @Override
    protected Delivery ConvertReaderToObject(ResultSet reader) throws Exception {
        return new Delivery(reader.getInt(1), reader.getString(2), reader.getString(3), reader.getString(4), reader.getInt(5), "", new LinkedList<>());
    }

    @Override
    public void cleanCache() {
        Iterator<Map.Entry<Integer,Delivery>> iter = deliveries.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer,Delivery> entry = iter.next();
            if(entry.getValue().shouldCleanCache()){
                System.out.println("Cleaning delivery "+entry.getValue().getId() +" from cache!");
                iter.remove();
            }
        }
    }

    public void deleteAllDataFromDbs() {
        destMapper.deleteAllData();
        deleteAllData();
        deliveries = new HashMap<>();
    }

    public List<Integer> getDeliveryIdInDate(String date) throws Exception {
        try {
            List<Delivery> allRs = (List<Delivery>) (List<?>) selectList("'"+date+"'", DateColumnName);
            List<Integer> ans = new LinkedList<>();
            for (Delivery rs : allRs) {
                ans.add(rs.getId());
            }
            return ans;
        }
        catch (Exception e){
            throw new Exception("could not retrieve all deliveries on this date: "+ date);
        }
    }

    public void deleteDelivery(int deliveryId) {
        destMapper.deleteAllOfDelivery(deliveryId);
        delete(deliveryId, DeliveryIdColumnName);
    }
}
