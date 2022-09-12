package DataAccessLayer.SupplierDAL;

import BusinessLayer.SupplierBusiness.BillOfQuantity;
import BusinessLayer.SupplierBusiness.SupplierItem;
import javafx.util.Pair;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;

public class BillOfQuantityMapper extends DalController {
    private static class BillOfQuantityMapperHolder{
        private static BillOfQuantityMapper instance = new BillOfQuantityMapper();
    }

    public static BillOfQuantityMapper getInstance() {
        return BillOfQuantityMapperHolder.instance;
    }

    private final String BranchAddressColumnName="BranchAddress";
    private final String SupplierBNColumnName="SupplierBN";
    private final String CatalogNumberColumnName="CatalogNumber";
    private final String PriceColumnName="Price";
    private final String NameColumnName="Name";
    private final String SupplierCatalogColumnName="SupplierCatalog";
    private final String AmountColumnName="Amount";
    private final String DiscountsColumnName="Discounts";

    private Map<Integer, BillOfQuantity> billsOfQuantities;

    private BillOfQuantityMapper() { super("BillsOFQuantities"); billsOfQuantities = new HashMap<>(); }
    // get all supplier items from database
    public List<SupplierItem> selectAllSupplierItems(int supplierBN){
        return (List<SupplierItem>)(List<?>)selectList(supplierBN, SupplierBNColumnName);
    }
    // insert new supplier item to database
    public boolean insertSupplierItem(String branchAddress, int supplierBN, int catalogNumber, int supplierCatalog, double price, String itemName) {
        checkSupplierItemExistsInDataBase(branchAddress, supplierBN, catalogNumber);
        SupplierItem supplierItem = new SupplierItem(branchAddress, supplierBN, catalogNumber, price, itemName,  supplierCatalog);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}, {6}) VALUES(?,?,?,?,?,?)",
                getTableName() ,BranchAddressColumnName, SupplierBNColumnName, CatalogNumberColumnName, SupplierCatalogColumnName, PriceColumnName, NameColumnName);
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, supplierItem.getBranchAddress());
            pstmt.setInt(2, supplierItem.getSupplierBN());
            pstmt.setInt(3, supplierItem.getCatalogNumber());
            pstmt.setInt(4, supplierItem.getSupplierCatalog());
            pstmt.setDouble(5, supplierItem.getPrice());
            pstmt.setString(6, supplierItem.getItemName());
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        billsOfQuantities.putIfAbsent(supplierBN, new BillOfQuantity(supplierBN));
        billsOfQuantities.get(supplierBN).addItem(branchAddress, supplierItem);
        return true;
    }

    public boolean insertItemDiscountAccordingToAmount(String branchAddress, int supplierBN, int catalogNumber, int amount, double discount) {
        if(!checkSupplierItemExists(branchAddress, supplierBN, catalogNumber))
            loadSupplierItem(branchAddress, supplierBN, catalogNumber);
        SupplierItem supplierItem = billsOfQuantities.get(supplierBN).getItem(branchAddress, catalogNumber);
        supplierItem.checkAmountNotExists(amount);
        supplierItem.checkIfDiscountIsLegal(amount, discount);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}) VALUES(?,?,?,?)",
                "SupplierItemsDiscounts", SupplierBNColumnName, CatalogNumberColumnName,
                AmountColumnName, DiscountsColumnName);
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, supplierBN);
            pstmt.setInt(2, catalogNumber);
            pstmt.setInt(3, amount);
            pstmt.setDouble(4, discount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        supplierItem.addItemDiscountAccordingToAmount(amount, discount);
        return true;
    }
    public void loadBillOfQuantity(int supplierBN) {
        billsOfQuantities.putIfAbsent(supplierBN, new BillOfQuantity(supplierBN));
        List<SupplierItem> supplierItems = selectAllSupplierItems(supplierBN);
        if(supplierItems.size()>0) {
            for(SupplierItem supplierItem : supplierItems)
                if(!billsOfQuantities.get(supplierBN).checkItemExists(supplierItem.getCatalogNumber(), supplierItem.getBranchAddress())) {
                    billsOfQuantities.get(supplierBN).addItem(supplierItem.getBranchAddress(), supplierItem);
                    List<Pair<Integer, Double>> discounts = selectDiscountsOfItem(supplierBN, supplierItem.getCatalogNumber());
                    for(Pair<Integer, Double> discount : discounts)
                        billsOfQuantities.get(supplierBN).addItemDiscountAccordingToAmount(supplierItem.getBranchAddress(), supplierItem.getCatalogNumber(), discount.getKey(), discount.getValue());
                }
        }
    }
    public void delete(String branchAddress, int supplierBN, int catalogNumber) {
        checkSupplierItemNotExistsInDataBase(branchAddress, supplierBN, catalogNumber);
        delete(supplierBN, catalogNumber, SupplierBNColumnName, CatalogNumberColumnName);
        billsOfQuantities.get(supplierBN).removeItem(branchAddress, catalogNumber);
    }


    private boolean checkSupplierItemExists(String branchAddress, int supplierBN, int catalogNumber) {
        return billsOfQuantities.containsKey(supplierBN) && billsOfQuantities.get(supplierBN).checkItemExists(catalogNumber, branchAddress);
    }

    public void checkSupplierItemExistsInDataBase(String branchAddress, int supplierBN, int catalogNumber) {
        if(checkSupplierItemExists(branchAddress, supplierBN, catalogNumber))
            throw new IllegalArgumentException("supplier item already exists!");
        SupplierItem supplierItem = (SupplierItem) select(branchAddress, supplierBN,catalogNumber, BranchAddressColumnName, SupplierBNColumnName, CatalogNumberColumnName);
        if(supplierItem!=null)
            throw new IllegalArgumentException("supplier item already exists!");

    }
    public void checkSupplierItemNotExistsInDataBase(String branchAddress, int supplierBN, int catalogNumber) {
        if(!checkSupplierItemExists(branchAddress, supplierBN, catalogNumber)) {
            SupplierItem supplierItem = (SupplierItem) select(branchAddress, supplierBN, catalogNumber, BranchAddressColumnName, SupplierBNColumnName, CatalogNumberColumnName);
            if(supplierItem==null)
                throw new IllegalArgumentException("supplier item not exists!");
        }
    }

    public void updateItemName(String branchAddress, int supplierBN, int catalogNumber, String itemName) {
        if(!checkSupplierItemExists(branchAddress, supplierBN, catalogNumber))
            loadSupplierItem(branchAddress, supplierBN, catalogNumber);
        update(supplierBN, catalogNumber, NameColumnName, itemName, SupplierBNColumnName, CatalogNumberColumnName);
        billsOfQuantities.get(supplierBN).updateItemName(branchAddress, catalogNumber, itemName);
    }

    public void updateSupplierCatalog(String branchAddress, int supplierBN, int catalogNumber, int supplierCatalog) {
        if(!checkSupplierItemExists(branchAddress, supplierBN, catalogNumber))
            loadSupplierItem(branchAddress, supplierBN, catalogNumber);
        update(supplierBN, catalogNumber, SupplierCatalogColumnName, supplierCatalog, SupplierBNColumnName, CatalogNumberColumnName);
        billsOfQuantities.get(supplierBN).updateSupplierCatalog(branchAddress, catalogNumber, supplierCatalog);
    }

    public void updateItemPrice(String branchAddress, int supplierBN, int catalogNumber, double price) {
        if(!checkSupplierItemExists(branchAddress, supplierBN, catalogNumber))
            loadSupplierItem(branchAddress, supplierBN, catalogNumber);
        update(supplierBN, catalogNumber, PriceColumnName, price, SupplierBNColumnName, CatalogNumberColumnName);
        billsOfQuantities.get(supplierBN).updateItemPrice(branchAddress, catalogNumber, price);
    }

    public void updateItemDiscountAccordingToAmount(String branchAddress, int supplierBN, int catalogNumber, int amount, double newDiscount) {
        if(!checkSupplierItemExists(branchAddress, supplierBN, catalogNumber))
            loadSupplierItem(branchAddress, supplierBN, catalogNumber);
        update("SupplierItemsDiscounts", supplierBN, catalogNumber, amount, DiscountsColumnName, newDiscount, SupplierBNColumnName, CatalogNumberColumnName, AmountColumnName);
        billsOfQuantities.get(supplierBN).updateItemDiscountAccordingToAmount(branchAddress, catalogNumber, amount, newDiscount);
    }

    public void deleteItemDiscountAccordingToAmount(String branchAddress, int supplierBN, int catalogNumber, int amount) {
        if(!checkSupplierItemExists(branchAddress, supplierBN, catalogNumber))
            loadSupplierItem(branchAddress, supplierBN, catalogNumber);
        if(!billsOfQuantities.get(supplierBN).getItemDiscounts(branchAddress, catalogNumber).containsKey(amount))
            throw new IllegalArgumentException("Amount is not exists!");
        delete("SupplierItemsDiscounts", supplierBN, catalogNumber, amount, SupplierBNColumnName, CatalogNumberColumnName, AmountColumnName);
        billsOfQuantities.get(supplierBN).removeItemDiscountAccordingToAmount(branchAddress, catalogNumber, amount);
    }

    @Override
    protected Object ConvertReaderToObject(ResultSet reader) {
        SupplierItem result = null;
        try {
            result = new SupplierItem(reader.getString(1), reader.getInt(2), reader.getInt(3), reader.getDouble(4), reader.getString(5), reader.getInt(6));
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public void cleanCache() {
        Iterator<Map.Entry<Integer,BillOfQuantity>> iter = billsOfQuantities.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer,BillOfQuantity> entry = iter.next();
            if(entry.getValue().shouldCleanCache()){
                System.out.println("Cleaning bill of quantity "+entry.getValue().getSupplierBN() +" from cache!");
                iter.remove();
            }
        }
    }

    @Override
    protected void cleanCacheForTests() {
        billsOfQuantities = new HashMap<>();
    }

    private void loadSupplierItem(String branchAddress, int supplierBN, int catalogNumber) {
        SupplierItem supplierItem = (SupplierItem) select(supplierBN, catalogNumber, SupplierBNColumnName, CatalogNumberColumnName);
        if(supplierItem == null)
            throw new IllegalArgumentException("invalid catalog number");
        List<Pair<Integer, Double>> itemDiscounts = selectDiscountsOfItem(supplierBN, catalogNumber);
        for(Pair<Integer, Double> itemDiscount: itemDiscounts)
            supplierItem.addItemDiscountAccordingToAmount(itemDiscount.getKey(), itemDiscount.getValue());
        billsOfQuantities.putIfAbsent(supplierItem.getSupplierBN(), new BillOfQuantity(supplierBN));
        billsOfQuantities.get(supplierItem.getSupplierBN()).addItem(branchAddress, supplierItem);
    }

    public BillOfQuantity getBillOfQuantity(int supplierBN) {
        if(!billsOfQuantities.containsKey(supplierBN))
            loadBillOfQuantity(supplierBN);
        return billsOfQuantities.get(supplierBN);
    }
    public SupplierItem getSupplierItem(String branchAddress, int supplierBN, int catalogNumber) {
        if(!billsOfQuantities.containsKey(supplierBN) || !billsOfQuantities.get(supplierBN).checkItemExists(catalogNumber, branchAddress))
            loadSupplierItem(branchAddress, supplierBN, catalogNumber);
        return billsOfQuantities.get(supplierBN).getItem(branchAddress, catalogNumber);
    }

    public Map<Integer, Double> getItemDiscounts(String branchAddress, int supplierBN, int catalogNumber) {
        if(!billsOfQuantities.containsKey(supplierBN))
            loadSupplierItem(branchAddress, supplierBN, catalogNumber);
        return billsOfQuantities.get(supplierBN).getItemDiscounts(branchAddress, catalogNumber);
    }

    public String getItemName(String branchAddress, int supplierBN, int catalogNumber) {
        if(!billsOfQuantities.containsKey(supplierBN))
            loadSupplierItem(branchAddress, supplierBN, catalogNumber);
        return billsOfQuantities.get(supplierBN).getItemName(branchAddress, catalogNumber);
    }

    public List<Pair<Integer, Double>> selectDiscountsOfItem(int supplierBN, int catalogNumber) {
        List<Pair<Integer, Double>> results = new ArrayList<>();
        String sql = "SELECT * FROM SupplierItemsDiscounts WHERE "+SupplierBNColumnName+" = "+supplierBN+" AND "+CatalogNumberColumnName+" = "+catalogNumber;
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                results.add(ConvertReaderToDiscountOfItem(rs));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
        return results;
    }

    private Pair<Integer, Double> ConvertReaderToDiscountOfItem(ResultSet rs) throws SQLException {
        return new Pair<>(rs.getInt(3), rs.getDouble(4));
    }
}
