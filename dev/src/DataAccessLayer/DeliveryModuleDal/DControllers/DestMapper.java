package DataAccessLayer.DeliveryModuleDal.DControllers;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;

public class DestMapper extends DalController {

    private final String DeliveryIdColumnName = "DeliveryId";
    private final String DestAddressColumnName = "DestinationAddress";
    private final String NumberListColumnName = "NumberList";

    public DestMapper() {
        super("Destinations");
    }


    public void insert(int deliveryId, String address, int index) throws Exception {
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}) VALUES(?,?,?)",
                getTableName(),DeliveryIdColumnName,DestAddressColumnName,NumberListColumnName);
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, deliveryId);
            pstmt.setString(2, address);
            pstmt.setInt(3, index);
            pstmt.executeUpdate();
        }
        catch (Exception e) {
            throw new Exception("could not insert to " + getTableName());
        }
    }

    public void delete(int deliveryId, int index) throws Exception{
        if(!delete(deliveryId, index, DeliveryIdColumnName, NumberListColumnName))
            throw new Exception("no destination, delivery id:" + deliveryId + ", index:" + index);
    }

    public List<String> selectListAddress(int deliveryId) throws Exception {
        Map<Integer, String> addressPerIndex = new HashMap<>();
        String sql = "SELECT * FROM "+getTableName()+" WHERE "+DeliveryIdColumnName+" = "+ deliveryId;
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                addressPerIndex.put(ConvertReaderToInt(rs), ConvertReaderToObject(rs));
            }
            return compare(addressPerIndex);
        }
        catch (Exception e){
            throw new Exception("could not select list of addresses " + getTableName());
        }
    }

    @Override
    protected String ConvertReaderToObject(ResultSet reader) throws Exception {
        return reader.getString(2);
    }

    private Integer ConvertReaderToInt(ResultSet reader) throws Exception {
        return reader.getInt(3);
    }

    private List<String> compare(Map<Integer, String> map) {
        List<String> result = new LinkedList<>();
        while (!map.isEmpty())
            result.add(extractMinValue(map));
        return result;
    }

    private String extractMinValue(Map<Integer, String> map) {
        int min = Integer.MAX_VALUE;
        for (Integer i: map.keySet()) {
            if (i < min)
                min = i;
        }
        String result = map.get(min);
        map.remove(min);
        return result;
    }

    public void changeAddressListForDelivery(int deliveryId, List<String> siteAddresses) throws Exception {
        if(!delete(deliveryId, DeliveryIdColumnName))
            throw new Exception("could not delete address list of delivery after change");
        for (int i = 0; i<siteAddresses.size(); i++)
            insert(deliveryId, siteAddresses.get(i), i);
    }

    @Override
    public void cleanCache() {

    }

    public void deleteAllOfDelivery(int deliveryId) {
        delete(deliveryId, DeliveryIdColumnName);
    }

}
