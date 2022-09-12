package DataAccessLayer.InventoryDAL;

import BusinessLayer.InventoryBusiness.ItemQuantity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class ItemQuantityDAO extends AbstractDAO {
    private final String BranchAddressColumnName = "BranchAddress";
    private final String ItemIDColumnName = "ItemID";
    private final String AmountInBackRoomColumnName = "AmountInBackRoom";
    private final String AmountInFrontRoomColumnName = "AmountInFrontRoom";
    private final String TotalQuantityColumnName = "TotalAmount";
    private final String MinimalQuantityColumnName = "MinimalQuantity";
    private final String FullQuantityColumnName = "FullQuantity";

    public ItemQuantityDAO() {
        super("ItemQuantity");
    }
    @Override
    public void cleanCache() {}
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // insert a new ItemQuantity to the database
    public boolean insert(String branchAddress, int itemID, int amountInBackRoom, int amountInFrontRoom, int totalQuantity, int minimalQuantity, int fullQuantity){
        //checkItemExistsInDataBase(itemID);
        ItemQuantity itemQuantity = new ItemQuantity(branchAddress, amountInBackRoom, amountInFrontRoom, totalQuantity, minimalQuantity, fullQuantity);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}, {6}, {7}) VALUES(?,?,?,?,?,?,?)",
                getTableName(), BranchAddressColumnName, ItemIDColumnName, AmountInBackRoomColumnName, AmountInFrontRoomColumnName,
                TotalQuantityColumnName, MinimalQuantityColumnName, FullQuantityColumnName);
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, branchAddress);
            pstmt.setInt(2, itemID);
            pstmt.setInt(3, itemQuantity.getAmountInBackRoom());
            pstmt.setInt(4, itemQuantity.getAmountInFrontRoom());
            pstmt.setInt(5, itemQuantity.getTotalAmount());
            pstmt.setInt(6, itemQuantity.getMinimalQuantity());
            pstmt.setInt(7, itemQuantity.getFullQuantity());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+" ,problem at inserting a new ItemQuantity to dal");
        }
        return true;
    }

    public void updateItemID(String branchAddress, int itemID, int newItemID) {
        update(branchAddress, itemID, ItemIDColumnName, newItemID, ItemIDColumnName);
        //itemsIM.get(itemID).setName(newName);
    }

    public void updateAmountInBackRoom(String branchAddress, int itemID, int newAmountInBackRoom) {
        update(branchAddress, itemID, AmountInBackRoomColumnName, newAmountInBackRoom, ItemIDColumnName);
        //items.get(itemBN).setBankAccount(newBankAccount);
    }

    public void updateAmountInFrontRoom(String branchAddress, int itemID, int newAmountInFrontRoom) {
        update(branchAddress, itemID, AmountInFrontRoomColumnName, newAmountInFrontRoom, ItemIDColumnName);
        //items.get(itemBN).setBankAccount(newBankAccount);
    }

    public void updateTotalQuantity(String branchAddress, int itemID, int newTotalQuantity) {
        update(branchAddress, itemID, TotalQuantityColumnName, newTotalQuantity, ItemIDColumnName);
        //items.get(itemBN).setBankAccount(newBankAccount);
    }

    public void updateMinimalQuantity(String branchAddress, int itemID, int newMinimalQuantity) {
        update(branchAddress, itemID, MinimalQuantityColumnName, newMinimalQuantity, ItemIDColumnName);
        //items.get(itemBN).setBankAccount(newBankAccount);
    }

    public void updateFullQuantity(String branchAddress, int itemID, int newFullQuantity) {
        update(branchAddress, itemID, FullQuantityColumnName, newFullQuantity, ItemIDColumnName);
        //items.get(itemBN).setBankAccount(newBankAccount);
    }

    public ItemQuantity getItemQuantity(String branchAddress, int itemID) {
//        if (itemQuantityIM.containsKey(itemID))
//            return itemQuantityIM.get(itemID);
        ItemQuantity itemQuantity = (ItemQuantity) select(branchAddress, itemID, ItemIDColumnName);
//        itemQuantityIM.put(itemID, itemQuantity);
        return itemQuantity;
    }

    @Override
    protected ItemQuantity ConvertReaderToObject(String branchAddress, ResultSet reader) {
        ItemQuantity result = null;
        try {
            result = new ItemQuantity(branchAddress, reader.getInt(3), reader.getInt(4),
                    reader.getInt(5), reader.getInt(6), reader.getInt(7));

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return result;
    }

    public void deleteRecord(String branchAddress, int itemID){
        delete(branchAddress, itemID, ItemIDColumnName);
    }
}