package DataAccessLayer.InventoryDAL;

import BusinessLayer.InventoryBusiness.ReportItems.InShortageItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InShortageItemsDAO extends AbstractDAO {
    private final String BranchAddressColumnName = "BranchAddress";
    private final String ItemIDColumnName = "ItemID";

    private Map<String, Map<Integer, InShortageItem>> inShortageItemsIM;

    public InShortageItemsDAO() {
        super("InShortageItems");
        inShortageItemsIM = new HashMap<>();
    }
    @Override
    public void cleanCache() {
        inShortageItemsIM= new HashMap<>();
    }
    // get all items from database
    public List<InShortageItem> selectAllItems(String branchAddress) {
        return (List<InShortageItem>) (List<?>) selectAllByBranch(branchAddress);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // insert a new item to the database
    public boolean insert(String branchAddress, int itemID) {
        //checkItemExistsInDataBase(itemID);
        InShortageItem inShortageItem = new InShortageItem(branchAddress, itemID);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}) VALUES(?,?)",
                getTableName(), BranchAddressColumnName, ItemIDColumnName);
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, branchAddress);
            pstmt.setInt(2, inShortageItem.getItemID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+", problem at inserting a new InShortageItem to dal");
        }
        inShortageItemsIM.putIfAbsent(branchAddress, new HashMap<>());
        inShortageItemsIM.get(branchAddress).put(inShortageItem.getItemID(), inShortageItem);
        return true;
    }

    public List<InShortageItem> getAllItems(String branchAddress) {
        return selectAllItems(branchAddress);
    }

    @Override
    protected InShortageItem ConvertReaderToObject(String branchAddress, ResultSet reader) {
        InShortageItem result = null;
        try {
            result = new InShortageItem(branchAddress, reader.getInt(2));
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return result;
    }

    public void deleteRecord(String branchAddress, int itemID){
        delete(branchAddress, itemID, ItemIDColumnName);
    }
}
