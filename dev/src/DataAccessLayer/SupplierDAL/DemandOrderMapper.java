package DataAccessLayer.SupplierDAL;

import BusinessLayer.SupplierBusiness.*;

import java.sql.*;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class DemandOrderMapper extends DalController {
    private static class DDemandOrderControllerHolder {
        private static DemandOrderMapper instance = new DemandOrderMapper();
    }

    public static DemandOrderMapper getInstance() {
        return DDemandOrderControllerHolder.instance;
    }

    public static final String BranchAddressColumnName="BranchAddress";
    private final String OrderIdColumnName="OrderId";
    private final String SupplierBNColumnName="SupplierBN";
    private final String PriceColumnName="Price";
    private final String IsSuppliedColumnName="IsSupplied";
    private final String OrderDateColumnName="OrderDate";
    private final String SupplyDateColumnName="SupplyDate";

    private Map<Integer, Map<Integer, DemandOrder>> demandOrders;

    private DemandOrderMapper() {
        super("DemandOrders");
        this.demandOrders = new HashMap<>();
    }

    // get all orders from database
    public List<DemandOrder> selectAllOrders(){
        return (List<DemandOrder>)(List<?>)select();
    }
    private List<DemandOrder> selectSupplierOrders(int supplierBN) { return (List<DemandOrder>)(List<?>)selectList(supplierBN, SupplierBNColumnName);
    }
    // insert new order to database
    public DemandOrder insert(String branchAddress, int orderId, int supplierBN, Date orderDate, Date supplyDate, HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        checkDemandOrderExistsInDataBase(supplierBN, orderId);
        DemandOrder order = new DemandOrder(branchAddress, orderId, supplierBN, orderDate, supplyDate, itemIdAndAmount, billOfQuantity);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}, {6}, {7}) VALUES(?,?,?,?,?,?,?)",
                getTableName(), BranchAddressColumnName, OrderIdColumnName, SupplierBNColumnName,
                PriceColumnName, IsSuppliedColumnName, OrderDateColumnName, SupplyDateColumnName);
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, order.getBranchAddress());
            pstmt.setInt(2, order.getOrderId());
            pstmt.setInt(3, order.getSupplierBN());
            pstmt.setDouble(4, order.getPrice());
            pstmt.setBoolean(5, order.isSupplied());
            pstmt.setString(6, order.getOrderDate().toString());
            pstmt.setString(7, order.getSupplyDate()==null ? null : order.getSupplyDate().toString());
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        demandOrders.putIfAbsent(supplierBN, new HashMap<>());
        demandOrders.get(supplierBN).put(orderId, order);
        return order;
    }

    private void checkSupplierExists(int supplierBN){
        List<DemandOrder> demandOrderList = selectSupplierOrders(supplierBN);
        if(!demandOrders.containsKey(supplierBN))
            if(demandOrderList.size()==0)
                throw new IllegalArgumentException("Supplier does not have any demand orders!");
        demandOrders.putIfAbsent(supplierBN, new HashMap<>());
        for(DemandOrder demandOrder : demandOrderList)
            demandOrders.get(supplierBN).putIfAbsent(demandOrder.getOrderId(), demandOrder);
    }

    private boolean checkDemandOrderExists(int supplierBN, int orderId) {
        return demandOrders.containsKey(supplierBN) && demandOrders.get(supplierBN).containsKey(orderId);
    }

    public void checkDemandOrderExistsInDataBase(int supplierBN, int orderId) {
        if(checkDemandOrderExists(supplierBN, orderId))
            throw new IllegalArgumentException("Demand order already exists!");
        DemandOrder demandOrder = (DemandOrder) select(supplierBN, orderId, SupplierBNColumnName, OrderIdColumnName);
        if(demandOrder!=null)
            throw new IllegalArgumentException("Demand order already exists!");
    }

    public void checkDemandOrderNotExistsInDataBase(int supplierBN, int orderId) {
        if(!checkDemandOrderExists(supplierBN, orderId)) {
            DemandOrder demandOrder = (DemandOrder) select(supplierBN, orderId, SupplierBNColumnName, OrderIdColumnName);
            if(demandOrder==null)
                throw new IllegalArgumentException("Demand order not exists!");
            demandOrders.putIfAbsent(supplierBN, new HashMap<>());
            demandOrders.get(supplierBN).put(orderId, demandOrder);
        }
    }

    public void setSupply(int supplierBN, int orderId, Date supplyDate) {
        checkDemandOrderNotExistsInDataBase(supplierBN, orderId);
        if(!getDemandOrder(supplierBN, orderId).isSupplied()) {
            update(orderId, SupplyDateColumnName, supplyDate.toString(), OrderIdColumnName);
            update(orderId, IsSuppliedColumnName, true, OrderIdColumnName);
            demandOrders.get(supplierBN).get(orderId).setSupplyDate(supplyDate);
            demandOrders.get(supplierBN).get(orderId).setSupplied();
        }
    }

    public void removeUnSuppliedDemandOrder(int supplierBN, int orderId) {
        checkDemandOrderNotExistsInDataBase(supplierBN, orderId);
        if(demandOrders.get(supplierBN).get(orderId).isSupplied())
            throw new IllegalArgumentException("Cannot delete supplied order");
        delete(orderId, OrderIdColumnName);
        demandOrders.get(supplierBN).remove(orderId).deleteOrderProducts();
    }

    public void addProductsToOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        checkDemandOrderNotExistsInDataBase(supplierBN, orderId);
        demandOrders.get(supplierBN).get(orderId).addProducts(itemIdAndAmount, billOfQuantity);
        setPrice(supplierBN, orderId, demandOrders.get(supplierBN).get(orderId).getPrice());
    }

    public void removeProducts(int supplierBN, int orderId, List<Integer> productsId) {
        checkDemandOrderNotExistsInDataBase(supplierBN, orderId);
        demandOrders.get(supplierBN).get(orderId).removeProducts(productsId);
        setPrice(supplierBN, orderId, demandOrders.get(supplierBN).get(orderId).getPrice());
    }

    public void updateOrderProducts(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        checkDemandOrderNotExistsInDataBase(supplierBN, orderId);
        demandOrders.get(supplierBN).get(orderId).updateProducts(itemIdAndAmount, billOfQuantity);
        setPrice(supplierBN, orderId, demandOrders.get(supplierBN).get(orderId).getPrice());
    }

    public void updateUnSuppliedAmount(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndUnSuppliedAmount) {
        checkDemandOrderNotExistsInDataBase(supplierBN, orderId);
        demandOrders.get(supplierBN).get(orderId).updateUnSuppliedAmount(itemIdAndUnSuppliedAmount);
        //todo: check if need to update order price
    }

    private void setPrice(int supplierBN, int orderId, double price) {
        update(supplierBN, orderId, PriceColumnName, price, SupplierBNColumnName, OrderIdColumnName);
    }

    public void setSupplyDate(int supplierBN, int orderId, Date supplyDate) {
        checkDemandOrderNotExistsInDataBase(supplierBN, orderId);
        update(orderId, SupplyDateColumnName, supplyDate.toString(), OrderIdColumnName);
        demandOrders.get(supplierBN).get(orderId).setSupplyDate(supplyDate);
    }



    @Override
    protected Object ConvertReaderToObject(ResultSet reader) {
        DemandOrder result = null;
        try {
            result = new DemandOrder(reader.getString(1), reader.getInt(2), reader.getInt(3), reader.getDouble(4),reader.getBoolean(5), Date.valueOf(reader.getString(6)), reader.getString(7) == null ? null : Date.valueOf(reader.getString(7)));
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public void cleanCache() {
        Iterator<Map.Entry<Integer, Map<Integer, DemandOrder>>> iter = demandOrders.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, Map<Integer, DemandOrder>> entry = iter.next();
            Iterator<Map.Entry<Integer, DemandOrder>> iterDemandOrders = entry.getValue().entrySet().iterator();
            while (iterDemandOrders.hasNext()) {
                Map.Entry<Integer,DemandOrder> entryDemandOrder = iterDemandOrders.next();
                if(entryDemandOrder.getValue().shouldCleanCache()){
                    System.out.println("Cleaning demand order "+entryDemandOrder.getValue().getOrderId() +" from cache!");
                    iterDemandOrders.remove();
                }
            }
        }
    }

    @Override
    protected void cleanCacheForTests() {
        demandOrders = new HashMap<>();
    }

    public List<DemandOrder> getAllSupplierDemandOrders(int supplierBN) {
        checkSupplierExists(supplierBN);
        return demandOrders.get(supplierBN).values().stream().collect(Collectors.toList());
    }

    public List<DemandOrder> getAllDemandOrders() {
        loadAllOrders();
        List<DemandOrder> demandOrdersList = new ArrayList<>();
        for(int supplierBN: demandOrders.keySet())
            demandOrdersList.addAll(demandOrders.get(supplierBN).values());
        return demandOrdersList;
    }

    private void loadAllOrders() {
        List<DemandOrder> demandOrders = selectAllOrders();
        for(DemandOrder demandOrder : demandOrders) {
            this.demandOrders.putIfAbsent(demandOrder.getSupplierBN(), new HashMap<>());
            this.demandOrders.get(demandOrder.getSupplierBN()).putIfAbsent(demandOrder.getOrderId(), demandOrder);
        }
    }

    public DemandOrder getDemandOrder(int businessNumber, int orderId) {
        checkDemandOrderNotExistsInDataBase(businessNumber, orderId);
        return demandOrders.get(businessNumber).get(orderId);
    }
    public int getLastOrderId(){
        DemandOrder demandOrder = (DemandOrder) selectLastId(OrderIdColumnName);
        if(demandOrder == null)
            return -1;
        return demandOrder.getOrderId();
    }


}

