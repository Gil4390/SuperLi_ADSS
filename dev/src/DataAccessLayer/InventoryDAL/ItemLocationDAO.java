package DataAccessLayer.InventoryDAL;

import BusinessLayer.InventoryBusiness.ItemLocation;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;

public class ItemLocationDAO extends AbstractDAO {
    private final String BranchAddressColumnName = "BranchAddress";
    private final String ItemIDColumnName = "ItemID";
    private final String ShelfIDColumnName = "ShelfID";

    public ItemLocationDAO() {
        super("ItemLocation");
    }
    @Override
    public void cleanCache() {
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // insert a new ItemLocation to the database
    public boolean insert(String branchAddress, int itemID, int shelfID) {
        //checkItemExistsInDataBase(itemID);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}) VALUES(?,?,?)",
                getTableName(), BranchAddressColumnName, ItemIDColumnName, ShelfIDColumnName);
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, branchAddress);
            pstmt.setInt(2, itemID);
            pstmt.setInt(3, shelfID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+" ,problem at inserting a new ItemLocation to dal");
        }
        return true;
    }

    public void updateItemID(String branchAddress, int itemID, int newItemID) {
        update(branchAddress, itemID, ItemIDColumnName, newItemID, ItemIDColumnName);
        //itemsIM.get(itemID).setName(newName);
    }

    public void updateShelfID(String branchAddress, int itemID, int newShelfID) {
        update(branchAddress, itemID, ShelfIDColumnName, newShelfID, ItemIDColumnName);
        //items.get(itemBN).setBankAccount(newBankAccount);
    }

    public ItemLocation getItemLocation(String branchAddress, int itemID) {
//        if (itemLocationsIM.containsKey(itemID))
//            return itemLocationsIM.get(itemID);
        List<Integer> shelves = (List<Integer>)(List<?>) selectListByBranch(branchAddress, itemID, ItemIDColumnName);
        List<Integer> backShelves = new ArrayList<>();
        List<Integer> frontShelves = new ArrayList<>();
        for(int shelf : shelves){
            if(DALFacade.getInstance().getBack0Front1(branchAddress, shelf) == 0)
                backShelves.add(shelf);
            else
                frontShelves.add(shelf);
        }
        ItemLocation itemLocation = new ItemLocation(branchAddress, frontShelves, backShelves);
//        itemLocationsIM.put(itemID, itemLocation);
        return itemLocation;
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

    public void deleteRecord(String branchAddress, int itemID, int shelfID){
        delete(branchAddress, itemID, shelfID, ItemIDColumnName, ShelfIDColumnName);
    }
}