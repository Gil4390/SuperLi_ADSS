package DataAccessLayer.InventoryDAL;

import javafx.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

public class ShelvesHandlerDAO extends AbstractDAO {
    private final String BranchAddressColumnName = "BranchAddress";
    private final String ShelfIDColumnName = "ShelfID";
    private final String Back0Front1ColumnName = "Back0Front1";

    public ShelvesHandlerDAO() {
        super("ShelvesHandler");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void cleanCache() {

    }
    // insert a new Shelf to the database
    public boolean insert(String branchAddress, int ShelfID, int Back0Front1){
        //checkItemExistsInDataBase(itemID);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}) VALUES(?,?,?)",
                getTableName(), BranchAddressColumnName, ShelfIDColumnName, Back0Front1ColumnName);
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, branchAddress);
            pstmt.setInt(2, ShelfID);
            pstmt.setInt(3, Back0Front1);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+", problem at inserting a new Shelf to dal");
        }
        return true;
    }

    public void updateShelfID(String branchAddress, int shelfID, int newShelfID) {
        update(branchAddress, shelfID, ShelfIDColumnName, newShelfID, ShelfIDColumnName);
        //itemsIM.get(itemID).setName(newName);
    }

    public void updateBack0Front1(String branchAddress, int shelfID, int newBack0Front1) {
        update(branchAddress, shelfID, Back0Front1ColumnName, newBack0Front1, ShelfIDColumnName);
        //items.get(itemBN).setBankAccount(newBankAccount);
    }

    public List<Integer> getAllShelvesInBackRoom(String branchAddress) {
        List<Pair<Integer,Integer>> itemsIDs = (List<Pair<Integer,Integer>>)(List<?>) selectListByBranch(branchAddress, 0, Back0Front1ColumnName);
        List<Integer> list = new LinkedList<>();
        for(Pair<Integer,Integer> pair : itemsIDs){
            list.add(pair.getKey());
        }
        return list;
    }

    public List<Integer> getAllShelvesInFrontRoom(String branchAddress) {
        List<Pair<Integer,Integer>> itemsIDs = (List<Pair<Integer,Integer>>)(List<?>) selectListByBranch(branchAddress, 1, Back0Front1ColumnName);
        List<Integer> list = new LinkedList<>();
        for(Pair<Integer,Integer> pair : itemsIDs){
            list.add(pair.getKey());
        }
        return list;
    }

    public List<Integer> getAllShelves(String branchAddress) {
        List<Integer> itemsIDs = (List<Integer>)(List<?>) selectAllByBranch(branchAddress);
        return itemsIDs;
    }

    public Integer getBack0Front1(String branchAddress, int shelf) {
        int answer = ((Pair<Integer,Integer>) select(branchAddress, shelf, ShelfIDColumnName)).getValue();
        return answer;
    }

    @Override
    protected Pair<Integer,Integer> ConvertReaderToObject(String branchAddress, ResultSet reader) {
        Pair <Integer,Integer> shelf;
        Integer result = null;
        try {
            shelf = new Pair<>(reader.getInt(2),reader.getInt(3));
            return shelf;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            throw new IllegalArgumentException(throwable.getMessage());
        }
    }

    public void deleteRecord(String branchAddress, int shelfID){
        delete(branchAddress, shelfID, ShelfIDColumnName);
    }

    public int getShelvesCounter(String branchAddress) {
        return selectAllByBranch(branchAddress).size();
    }
}