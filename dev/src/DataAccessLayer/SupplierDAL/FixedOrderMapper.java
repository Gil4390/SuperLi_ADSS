package DataAccessLayer.SupplierDAL;

import BusinessLayer.SupplierBusiness.BillOfQuantity;
import BusinessLayer.SupplierBusiness.FixedOrder;

import java.sql.*;
import java.sql.Date;
import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

public class FixedOrderMapper extends DalController {
    private static class DFixedOrderControllerHolder{
        private static FixedOrderMapper instance = new FixedOrderMapper();
    }

    public static FixedOrderMapper getInstance() {
        return DFixedOrderControllerHolder.instance;
    }

    public static final String BranchAddressColumnName="BranchAddress";
    public static final String OrderIdColumnName="OrderId";
    public static final String SupplierBNColumnName="SupplierBn";
    public static final String PriceColumnName="Price";
    public static final String IsActiveColumnName="IsActive";
    public static final String OrderDateColumnName="OrderDate";
    public static final String CancelDateColumnName="CancelDate";
    public static final String DaysColumnName="Days";

    private Map<Integer, Map<Integer, FixedOrder>> fixedOrders;

    private FixedOrderMapper()
    {
        super("FixedOrders");
        this.fixedOrders = new HashMap<>();
    }
    // get all orders from database
    public List<FixedOrder> selectAllFixedOrders(){
        return (List<FixedOrder>)(List<?>)select();
    }
    public List<FixedOrder> selectSupplierFixedOrders(int supplierBN){
        return (List<FixedOrder>)(List<?>)selectList(supplierBN, SupplierBNColumnName);
    }
    // insert new order to database
    public boolean insert(String branchAddress, int orderId, int supplierBN, Date orderDate, Set<DayOfWeek> daysOfSupply, HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        checkFixedOrderExistsInDataBase(supplierBN, orderId);
        FixedOrder order = new FixedOrder(branchAddress, orderId, supplierBN, orderDate,daysOfSupply, itemIdAndAmount, billOfQuantity);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}) VALUES(?,?,?,?,?,?,?,?)",
                getTableName(),BranchAddressColumnName, OrderIdColumnName, SupplierBNColumnName, PriceColumnName,
                IsActiveColumnName, OrderDateColumnName, CancelDateColumnName, DaysColumnName);
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, order.getBranchAddress());
            pstmt.setInt(2, order.getOrderId());
            pstmt.setInt(3, order.getSupplierBN());
            pstmt.setDouble(4, order.getPrice());
            pstmt.setBoolean(5, order.isActive());
            pstmt.setString(6, order.getOrderDate().toString());
            pstmt.setString(7, order.getCanceledDate()==null ? null : order.getCanceledDate().toString());
            pstmt.setString(8, order.getDaysOfSupply().toString());
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        fixedOrders.putIfAbsent(supplierBN, new HashMap<>());
        fixedOrders.get(supplierBN).put(orderId, order);
        return true;
    }

    public void loadAllFixedOrders() {
        List<FixedOrder> fixedOrdersList = selectAllFixedOrders();
        for(FixedOrder fixedOrder : fixedOrdersList) {
            fixedOrders.putIfAbsent(fixedOrder.getSupplierBN(), new HashMap<>());
            fixedOrders.get(fixedOrder.getSupplierBN()).putIfAbsent(fixedOrder.getOrderId(), fixedOrder);
        }
    }
    private void checkSupplierExists(int supplierBN){
        List<FixedOrder> fixedOrderList = selectSupplierFixedOrders(supplierBN);
        if(!fixedOrders.containsKey(supplierBN)) {
            if(fixedOrderList.size() == 0)
                throw new IllegalArgumentException("Supplier does not have any fixed orders!");
        }
        for(FixedOrder fixedOrder : fixedOrderList) {
            fixedOrders.putIfAbsent(fixedOrder.getSupplierBN(), new HashMap<>());
            fixedOrders.get(fixedOrder.getSupplierBN()).putIfAbsent(fixedOrder.getOrderId(), fixedOrder);
        }
    }
    private boolean checkFixedOrderExists(int supplierBN, int orderId) {
        return fixedOrders.containsKey(supplierBN) && fixedOrders.get(supplierBN).containsKey(orderId);
    }

    public void checkFixedOrderExistsInDataBase(int supplierBN, int orderId) {
        if(checkFixedOrderExists(supplierBN, orderId))
            throw new IllegalArgumentException("Fixed order already exists!");
        FixedOrder fixedOrder = (FixedOrder) select(supplierBN, orderId, SupplierBNColumnName, OrderIdColumnName);
        if(fixedOrder!=null)
            throw new IllegalArgumentException("Fixed order already exists!");
    }

    public void checkFixedOrderNotExistsInDataBase(int supplierBN, int orderId) {
        if(!checkFixedOrderExists(supplierBN, orderId)) {
            FixedOrder fixedOrder = (FixedOrder) select(supplierBN, orderId, SupplierBNColumnName, OrderIdColumnName);
            if(fixedOrder==null)
                throw new IllegalArgumentException("Fixed order not exists!");
            fixedOrders.putIfAbsent(supplierBN, new HashMap<>());
            fixedOrders.get(supplierBN).put(orderId, fixedOrder);
        }
    }

    public void setUnActive(int supplierBN, int orderId, Date canceledDate) {
        checkFixedOrderNotExistsInDataBase(supplierBN, orderId);
        update(orderId, IsActiveColumnName, false, OrderIdColumnName);
        fixedOrders.get(supplierBN).get(orderId).setUnActive();
        update(orderId, CancelDateColumnName, canceledDate.toString(), OrderIdColumnName);
        fixedOrders.get(supplierBN).get(orderId).setCanceledDate(canceledDate);
    }

    private void setPrice(int supplierBN, int orderId, double price) {
        update(supplierBN, orderId, PriceColumnName, price, SupplierBNColumnName, OrderIdColumnName);
    }

    public void addProductsToOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        checkFixedOrderNotExistsInDataBase(supplierBN, orderId);
        fixedOrders.get(supplierBN).get(orderId).addProducts(itemIdAndAmount, billOfQuantity);
        setPrice(supplierBN, orderId, fixedOrders.get(supplierBN).get(orderId).getPrice());
    }

    public void removeProducts(int supplierBN, int orderId, List<Integer> productsId) {
        checkFixedOrderNotExistsInDataBase(supplierBN, orderId);
        fixedOrders.get(supplierBN).get(orderId).removeProducts(productsId);
        setPrice(supplierBN, orderId, fixedOrders.get(supplierBN).get(orderId).getPrice());
    }

    public void updateOrderProducts(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        checkFixedOrderNotExistsInDataBase(supplierBN, orderId);
        fixedOrders.get(supplierBN).get(orderId).updateProducts(itemIdAndAmount, billOfQuantity);
        setPrice(supplierBN, orderId, fixedOrders.get(supplierBN).get(orderId).getPrice());
    }

    public void addSupplyDay(int supplierBN, int orderId, DayOfWeek day) {
        checkFixedOrderNotExistsInDataBase(supplierBN, orderId);
        Set<DayOfWeek> dayOfWeekSet = fixedOrders.get(supplierBN).get(orderId).getDaysOfSupply();
        dayOfWeekSet.add(day);
        update(supplierBN, orderId, DaysColumnName, dayOfWeekSet.toString(), SupplierBNColumnName, OrderIdColumnName);
        fixedOrders.get(supplierBN).get(orderId).addSupplyDay(day);
    }

    public void removeSupplyDay(int supplierBN, int orderId, DayOfWeek day) {
        checkFixedOrderNotExistsInDataBase(supplierBN, orderId);
        Set<DayOfWeek> dayOfWeekSet = fixedOrders.get(supplierBN).get(orderId).getDaysOfSupply();
        dayOfWeekSet.remove(day);
        update(supplierBN, orderId, DaysColumnName, dayOfWeekSet.toString(), SupplierBNColumnName, OrderIdColumnName);
        fixedOrders.get(supplierBN).get(orderId).removeSupplyDay(day);
    }

    @Override
    protected Object ConvertReaderToObject(ResultSet reader) {
        FixedOrder result = null;
        try {
            Set<DayOfWeek> daysOfSupply = new HashSet<>();
            String[] daysOfSupplyStr = reader.getString(8).substring(1, reader.getString(8).length()-1).split(", ");
            for(String day : daysOfSupplyStr)
                if(!day.equals(""))
                    daysOfSupply.add(DayOfWeek.valueOf(day));
            result = new FixedOrder(reader.getString(1), reader.getInt(2), reader.getInt(3), reader.getDouble(4),reader.getBoolean(5), Date.valueOf(reader.getString(6)), reader.getString(7) == null ? null : Date.valueOf(reader.getString(7)), daysOfSupply);
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public void cleanCache() {
        Iterator<Map.Entry<Integer, Map<Integer, FixedOrder>>> iter = fixedOrders.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, Map<Integer, FixedOrder>> entry = iter.next();
            Iterator<Map.Entry<Integer, FixedOrder>> iterFixedOrders = entry.getValue().entrySet().iterator();
            while (iterFixedOrders.hasNext()) {
                Map.Entry<Integer,FixedOrder> entryFixedOrder = iterFixedOrders.next();
                if(entryFixedOrder.getValue().shouldCleanCache()){
                    System.out.println("Cleaning fixed order "+entryFixedOrder.getValue().getOrderId() +" from cache!");
                    iterFixedOrders.remove();
                }
            }
        }
    }

    @Override
    protected void cleanCacheForTests() {
        fixedOrders = new HashMap<>();
    }

    public List<FixedOrder> getAllSupplierFixedOrders(int supplierBN) {
        checkSupplierExists(supplierBN);
        return fixedOrders.get(supplierBN).values().stream().collect(Collectors.toList());
    }

    public List<FixedOrder> getAllFixedOrders() {
        loadAllFixedOrders();
        List<FixedOrder> fixedOrdersList = new ArrayList<>();
        for(int supplierBN: fixedOrders.keySet())
            fixedOrdersList.addAll(fixedOrders.get(supplierBN).values());
        return fixedOrdersList;
    }

    public FixedOrder getFixedOrder(int businessNumber, int orderId) {
        checkFixedOrderNotExistsInDataBase(businessNumber, orderId);
        return fixedOrders.get(businessNumber).get(orderId);
    }

    public int getLastOrderId() {
        FixedOrder fixedOrder = (FixedOrder) selectLastId(OrderIdColumnName);
        if(fixedOrder == null)
            return -1;
        return fixedOrder.getOrderId();
    }
}
