package DataAccessLayer.InventoryDAL;

import BusinessLayer.InventoryBusiness.ItemPrice;

import java.sql.*;
import java.text.MessageFormat;

public class ItemPriceDAO extends AbstractDAO {
    private final String BranchAddressColumnName = "BranchAddress";
    private final String ItemIDColumnName = "ItemID";
    private final String CurrPriceColumnName = "CurrPrice";

    public ItemPriceDAO() {
        super("ItemPrice");
    }
    @Override
    public void cleanCache() {}
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // insert a new ItemPrice to the database
    public boolean insert(String branchAddress, int itemID, double currPrice){
        //checkItemExistsInDataBase(itemID);
        ItemPrice itemPrice = new ItemPrice(branchAddress, itemID, currPrice);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}) VALUES(?,?,?)",
                getTableName(),BranchAddressColumnName, ItemIDColumnName, CurrPriceColumnName);
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, branchAddress);
            pstmt.setInt(2, itemID);
            pstmt.setDouble(3, itemPrice.getCurrentPrice());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+", problem at inserting a new ItemPrice to dal");
        }
        return true;
    }

    public void updateItemID(String branchAddress, int itemID, int newItemID) {
        update(branchAddress, itemID, ItemIDColumnName, newItemID, ItemIDColumnName);
        //itemsIM.get(itemID).setName(newName);
    }

    public void updateCurrPrice(String branchAddress, int itemID, double newCurrPrice) {
        update(branchAddress, itemID, CurrPriceColumnName, newCurrPrice, ItemIDColumnName);
        //items.get(itemBN).setBankAccount(newBankAccount);
    }

    public ItemPrice getItemPrice(String branchAddress, int itemID) {
//        if (itemPricesIM.containsKey(itemID))
//            return itemPricesIM.get(itemID);
        ItemPrice itemPrice = new ItemPrice(branchAddress, itemID ,(Integer)select(branchAddress, itemID, ItemIDColumnName));
//        itemPricesIM.put(itemID, itemPrice);
        return itemPrice;
    }

    @Override
    protected Integer ConvertReaderToObject(String branchAddress, ResultSet reader) {
        Integer result = null;
        try {
            result = reader.getInt(3);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return result;
    }

    public void deleteRecord(String branchAddress, int itemID){
        delete(branchAddress, itemID, ItemIDColumnName);
    }
}