package DataAccessLayer.DeliveryModuleDal.DControllers;

import BusinessLayer.DeliveryModule.Objects.StockShortness;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StockShortnessMapper extends DalController {

    private final String IdColumnName = "Id";
    private final String BranchAddressColumnName = "BranchAddress";
    private final String ItemNameColumnName = "ItemName";
    private final String ItemCatalogNumberColumnName = "ItemCatalogNumber";
    private final String AmountColumnName = "Amount";
    private final String SupplierAddressColumnName = "SupplierAddress";
    private final String IsBoundedColumnName = "BoundedToDelivery";
    private final String BoundLoadDocColumnName = "BoundedToLoadDocument";
    private final String BoundUnloadDocColumnName = "BoundedToUnloadDocument";
    private final String OrderIdColumnName= "BoundToOrderId";

    private Map<Integer, StockShortness> stockShortnesses;

    public StockShortnessMapper(){
        super("'Stock Shortnesses'");
        stockShortnesses = new ConcurrentHashMap<>();
    }

    public void load() throws Exception {
        try {
            for(StockShortness stockShortness : selectAllStockShortnesses())
                stockShortnesses.put(stockShortness.getId(), stockShortness);
        }
        catch (Exception e){
            throw new Exception("could not load data " + getTableName());
        }
    }


    public StockShortness getStockShortness(int stockShortnessId) throws Exception {
        if (stockShortnesses.containsKey(stockShortnessId))
            return stockShortnesses.get(stockShortnessId);
        StockShortness res = (StockShortness) select(stockShortnessId, IdColumnName);
        if (res==null) throw new Exception("the stock shortness doesnt exist");
        stockShortnesses.put(stockShortnessId, res);
        return res;
    }

    public List<StockShortness> getAllUnboundedStockShortnesses() throws Exception {
        return  (List<StockShortness>) (List<?>)selectList(-1, IsBoundedColumnName);
    }

    public List<StockShortness>getAllStockShortnessesOfDelivery(int deliveryId) throws Exception {
        return  (List<StockShortness>) (List<?>)selectList(deliveryId, IsBoundedColumnName);
    }

    private List<StockShortness> selectAllStockShortnesses() throws Exception {
        return (List<StockShortness>)(List<?>)select();
    }

    public void insert(StockShortness stockShortness) throws Exception {
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10}) VALUES(?,?,?,?,?,?,?,?,?,?)",
                getTableName(),IdColumnName,BranchAddressColumnName, ItemNameColumnName,
                ItemCatalogNumberColumnName, AmountColumnName, SupplierAddressColumnName,
                IsBoundedColumnName, BoundLoadDocColumnName, BoundUnloadDocColumnName, OrderIdColumnName);
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, stockShortness.getId());
            pstmt.setString(2, stockShortness.getBranchAddress());
            pstmt.setString(3, stockShortness.getItemName());
            pstmt.setInt(4, stockShortness.getItemCatalogNumber());
            pstmt.setInt(5, stockShortness.getAmount());
            pstmt.setString(6, stockShortness.getSupplierAddresses());
            pstmt.setInt(7, stockShortness.getBoundNum());
            pstmt.setInt(8, stockShortness.getSiteDocLoadId());
            pstmt.setInt(9, stockShortness.getSiteDocUnloadId());
            pstmt.setInt(10, stockShortness.getOrderId());

            pstmt.executeUpdate();
            stockShortnesses.put(stockShortness.getId(), stockShortness);
        }
        catch (Exception e) {
            throw new Exception("could not insert to " + getTableName());
        }
    }

    public void delete(int id) throws Exception {
        StockShortness stockShortness = stockShortnesses.get(id);
        if (stockShortness != null)
            stockShortnesses.remove(id);
        if (!delete(id, IdColumnName)) {
            throw new Exception("could not delete stock: " + id);
        }
    }

    @Override
    protected StockShortness ConvertReaderToObject(ResultSet reader) throws Exception {
        return new StockShortness(reader.getInt(1), reader.getString(2),
                reader.getString(3), reader.getInt(4), reader.getInt(5),
                reader.getString(6), reader.getInt(7), reader.getInt(8), reader.getInt(9), reader.getInt(10));
    }

    public void boundStockShortnessToDelivery(int stockShortnessId, int deliveryId) throws Exception {
        if (stockShortnesses.containsKey(stockShortnessId))
            stockShortnesses.get(stockShortnessId).boundDelivery(deliveryId);
        if (!update(stockShortnessId,IdColumnName, deliveryId, IsBoundedColumnName))
            throw new Exception("failed to bound stock shortness to delivery");
    }

    public void unboundStockShortnessFromDelivery(int stockShortnessId) throws Exception {
        if (stockShortnesses.containsKey(stockShortnessId))
            stockShortnesses.get(stockShortnessId).unboundDelivery();
        if (!update(stockShortnessId,IdColumnName, -1, IsBoundedColumnName))
        throw new Exception("failed to bound stock shortness to delivery");
    }

    public void boundLoadSiteDoc(int stockIdLoad, int siteDocId) throws Exception {
        if (stockShortnesses.containsKey(stockIdLoad))
            stockShortnesses.get(stockIdLoad).boundLoadDoc(siteDocId);
        if (!update(stockIdLoad,IdColumnName , siteDocId,BoundLoadDocColumnName ))
        throw new Exception("failed to bound stock shortness to Load site document");
    }

    public void boundUnLoadSiteDoc(int stockIdUnload, int siteDocId) throws Exception {
        if (stockShortnesses.containsKey(stockIdUnload))
            stockShortnesses.get(stockIdUnload).boundUnloadDoc(siteDocId);
        if (!update(stockIdUnload,IdColumnName, siteDocId,BoundUnloadDocColumnName))
        throw new Exception("failed to bound stock shortness to Unload site document");
    }

    public void unboundUnloadDoc(int stockId) throws Exception {
        if (stockShortnesses.containsKey(stockId))
            stockShortnesses.get(stockId).unboundUnloadDoc();
        if (!update(stockId,IdColumnName, -1,  BoundUnloadDocColumnName))
        throw new Exception("failed to unbound stock shortness from unload site document");
    }

    public void unboundLoadDoc(int stockId) throws Exception {
        if (stockShortnesses.containsKey(stockId))
            stockShortnesses.get(stockId).unboundLoadDoc();
        if (!update(stockId,IdColumnName, -1, BoundLoadDocColumnName))
        throw new Exception("failed to unbound stock shortness from load site document");
    }

    public void deleteAllStockBoundedToUnloadDoc(int documentId) throws Exception {
        Map<Integer, StockShortness> temp = new HashMap<>(stockShortnesses);
        for (Map.Entry<Integer, StockShortness> entry: temp.entrySet())
            if(entry.getValue().getSiteDocUnloadId()==documentId)
                stockShortnesses.remove(entry.getKey());
        if(!delete(documentId, BoundUnloadDocColumnName))
            throw new Exception("could not delete the stock shortness from the system");
    }

    @Override
    public void cleanCache() {
        Iterator<Map.Entry<Integer,StockShortness>> iter = stockShortnesses.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer,StockShortness> entry = iter.next();
            if(entry.getValue().shouldCleanCache()){
                System.out.println("Cleaning stock shortness "+entry.getValue().getId() +" from cache!");
                iter.remove();
            }
        }
    }

    public void deleteAllDataFromDbs() {
        deleteAllData();
        stockShortnesses = new HashMap<>();
    }

    public List<Integer> getStockShortnessOfOrder(int oldOrderId) {
        List<StockShortness> stockShortnessList = (List<StockShortness>)(List<?>)selectList(oldOrderId, OrderIdColumnName);
        List<Integer> ans = new LinkedList<>();
        for(StockShortness ss: stockShortnessList)
            ans.add(ss.getId());
        return ans;
    }

    public List<Integer> getOrderOfDocument(int documentId) {
        List<StockShortness> stockShortnessList = (List<StockShortness>)(List<?>)selectList(documentId, BoundUnloadDocColumnName);
        List<Integer> ans = new LinkedList<>();
        for(StockShortness ss: stockShortnessList)
            if (!ans.contains(ss.getOrderId())) ans.add(ss.getOrderId());
        return ans;
    }

    public void deleteOrderStock(int orderId) {
        delete(orderId, OrderIdColumnName);
    }

    public void deletestock(int stockShortnessId) {
        delete(stockShortnessId, IdColumnName);
    }
}
