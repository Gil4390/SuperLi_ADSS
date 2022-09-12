package DataAccessLayer.InventoryDAL;

import BusinessLayer.InventoryBusiness.ReportItems.DamagedItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;

public class DamagedItemsDAO extends AbstractDAO {
    private final String BranchAddressColumnName = "BranchAddress";
    private final String ItemIDColumnName = "ItemID";
    private final String TimeFoundColumnName = "TimeFound";
    private final String QuantityFoundColumnName = "QuantityFound";
    private final String ExpiredOrDamagedColumnName = "ExpiredOrDamaged";
    private final String Back0Front1ColumnName = "Back0Front1";

    //private Map<Integer, DamagedItem> damagedItemsIM;

    public DamagedItemsDAO() {
        super("DamagedItems");
    }

    @Override
    public void cleanCache() {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // insert a new DamagedItem to the database
    public boolean insert(String branchAddress, int itemID, String timeFound, int quantityFound, String expiredOrDamaged, int back0Front1) {
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}, {6}) VALUES(?,?,?,?,?,?)",
                getTableName(), BranchAddressColumnName, ItemIDColumnName, TimeFoundColumnName,
                QuantityFoundColumnName, ExpiredOrDamagedColumnName, Back0Front1ColumnName);
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, branchAddress);
            pstmt.setInt(2, itemID);
            pstmt.setString(3, timeFound);
            pstmt.setInt(4, quantityFound);
            pstmt.setString(5, expiredOrDamaged);
            pstmt.setInt(6, back0Front1);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+", problem at inserting a new DamagedItem to dal");
        }
        return true;
    }

    public List<DamagedItem> getAllItems(String branchAddress) {
        return (List<DamagedItem>)(List<?>) selectAllByBranch(branchAddress);
    }

    public List<DamagedItem> getAllRecordsByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) {
        List<DamagedItem> damagedItemList = new ArrayList<>();
        for (DamagedItem damagedItem : getAllItems(branchAddress))
            if((damagedItem.getDateFound().isBefore(untilWhen) && damagedItem.getDateFound().isAfter(sinceWhen)) ||
                    (damagedItem.getDateFound().isEqual(untilWhen) || damagedItem.getDateFound().isEqual(sinceWhen)))
                damagedItemList.add(damagedItem);
        return damagedItemList;
    }

    public List<DamagedItem> getAllRecordsByID(String branchAddress, int itemID) {
        return (List<DamagedItem>) (List<?>) selectListByBranch(branchAddress, itemID, ItemIDColumnName);
    }
///////////////
    public List<DamagedItem> getOnlyExpiredRecords(String branchAddress) {
        return (List<DamagedItem>) (List<?>) selectListByBranch(branchAddress, "Expired", ExpiredOrDamagedColumnName);
    }

    public List<DamagedItem> getOnlyDamageRecords(String branchAddress) {
        return (List<DamagedItem>) (List<?>) selectListByBranch(branchAddress, "Damaged", ExpiredOrDamagedColumnName);
    }

    public List<DamagedItem> getOnlyDamageRecordsByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) {
        List<DamagedItem> damagedItemList = new ArrayList<>();
        for (DamagedItem damagedItem : (List<DamagedItem>) (List<?>) selectListByBranch(branchAddress, "Damaged", ExpiredOrDamagedColumnName))
            if(damagedItem.getDateFound().isBefore(untilWhen) && damagedItem.getDateFound().isAfter(sinceWhen))
                damagedItemList.add(damagedItem);
        return damagedItemList;
    }

    public List<DamagedItem> getOnlyExpiredRecordsByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) {
        List<DamagedItem> damagedItemList = new ArrayList<>();
        for (DamagedItem damagedItem : (List<DamagedItem>) (List<?>) selectListByBranch(branchAddress,"Expired", ExpiredOrDamagedColumnName))
            if(damagedItem.getDateFound().isBefore(untilWhen) && damagedItem.getDateFound().isAfter(sinceWhen))
                damagedItemList.add(damagedItem);
        return damagedItemList;
    }

    public List<DamagedItem> getOnlyDamageRecordsByID(String branchAddress, int itemID) {
        return (List<DamagedItem>) (List<?>) selectListByBranch(branchAddress,"Damaged", ExpiredOrDamagedColumnName, itemID, ItemIDColumnName);

    }

    public List<DamagedItem> getOnlyExpiredRecordsByID(String branchAddress, int itemID) {
        return (List<DamagedItem>) (List<?>) selectListByBranch(branchAddress,"Expired", ExpiredOrDamagedColumnName, itemID, ItemIDColumnName);
    }


    @Override
    protected DamagedItem ConvertReaderToObject(String branchAddress, ResultSet reader) {
        DamagedItem result = null;
        try {
            result = new DamagedItem(branchAddress, reader.getInt(2), reader.getString(3),
                    reader.getInt(4), reader.getString(5), reader.getInt(6));

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return result;
    }

    public void deleteRecord(String branchAddress, int itemID){
        delete(branchAddress, itemID, ItemIDColumnName);
    }
}