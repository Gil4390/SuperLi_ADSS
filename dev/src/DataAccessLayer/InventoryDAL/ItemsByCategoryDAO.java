package DataAccessLayer.InventoryDAL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

public class ItemsByCategoryDAO extends AbstractDAO {
    private final String BranchAddressColumnName = "BranchAddress";
    private final String CategoryIDColumnName = "CategoryID";
    private final String ItemIDColumnName = "ItemID";

    public ItemsByCategoryDAO() {
        super("ItemsByCategory");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void cleanCache() {
    }
    // insert a new ItemsByCategory to the database
    public boolean insert(String branchAddress, String categoryID, int itemID){
        //checkItemExistsInDataBase(itemID);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}) VALUES(?,?,?)",
                getTableName(), BranchAddressColumnName, CategoryIDColumnName, ItemIDColumnName);
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             pstmt.setString(1, branchAddress);
             pstmt.setString(2, categoryID);
             pstmt.setInt(3, itemID);
             pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+", problem at inserting a new ItemsByCategory to dal");
        }
        return true;
    }

    public void updateCategoryID(String branchAddress, int categoryID, int newCategoryID) {
        update(branchAddress, categoryID, CategoryIDColumnName, newCategoryID, CategoryIDColumnName);
        //itemsIM.get(itemID).setName(newName);
    }

    public void updateItemID(String branchAddress, int categoryID, int newItemID) {
        update(branchAddress, categoryID, ItemIDColumnName, newItemID, CategoryIDColumnName);
        //itemsIM.get(itemID).setName(newName);
    }

    public List<Integer> getItemsByCategory(String branchAddress, int categoryID) {
        List<Integer> itemsIDs = (List<Integer>)(List<?>) selectListByBranch(branchAddress, categoryID, CategoryIDColumnName);
        return itemsIDs;
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

    public void deleteRecord(String branchAddress, String categoryID, int itemID){
        delete(branchAddress, itemID, categoryID, ItemIDColumnName, CategoryIDColumnName);
    }
}