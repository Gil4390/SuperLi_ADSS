package DataAccessLayer.InventoryDAL;

import BusinessLayer.InventoryBusiness.ReportItems.PurchasedItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PurchasedItemsDAO extends AbstractDAO {
    private final String BranchAddressColumnName = "BranchAddress";
    private final String ItemIDColumnName = "ItemID";
    private final String OrderIDColumnName = "OrderID";
    private final String TimePurchasedColumnName = "TimePurchased";
    private final String SupplierBusinessNumberColumnName = "SupplierBusinessNumber";
    private final String AmountPurchasedColumnName = "AmountPurchased";
    private final String UnitPriceFromSupplierColumnName = "UnitPriceFromSupplier";
    private final String DiscountFromSupplierColumnName = "DiscountFromSupplier";

    public PurchasedItemsDAO() {
        super("PurchasedItems");
    }
    @Override
    public void cleanCache() {}

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // insert a new PurchasedItem to the database
    public boolean insert(String branchAddress, int itemID, int orderID, String timePurchased, int supplierBusinessNumber, int amountPurchased, double unitPriceFromSupplier, double discountFromSupplier) {
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}) VALUES(?,?,?,?,?,?,?,?)",
                getTableName(), BranchAddressColumnName, ItemIDColumnName, OrderIDColumnName,
                TimePurchasedColumnName, SupplierBusinessNumberColumnName,
                AmountPurchasedColumnName, UnitPriceFromSupplierColumnName,
                DiscountFromSupplierColumnName);
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, branchAddress);
            pstmt.setInt(2, itemID);
            pstmt.setInt(3, orderID);
            pstmt.setString(4, timePurchased);
            pstmt.setInt(5, supplierBusinessNumber);
            pstmt.setInt(6, amountPurchased);
            pstmt.setDouble(7, unitPriceFromSupplier);
            pstmt.setDouble(8, discountFromSupplier);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+", problem at inserting a new PurchasedItem to dal");
        }
        return true;
    }

    public List<PurchasedItem> getAllRecords(String branchAddress) {
        return (List<PurchasedItem>)(List<?>) selectAllByBranch(branchAddress);
    }

    /////////////////////////////////

    public List<PurchasedItem> getRecordsByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) {
        List<PurchasedItem> purchasedItemList = new ArrayList<>();
        for (PurchasedItem purchasedItem : getAllRecords(branchAddress))
            if(!purchasedItem.getDatePurchased().isBefore(sinceWhen) && !purchasedItem.getDatePurchased().isAfter(untilWhen))
                purchasedItemList.add(purchasedItem);
        return purchasedItemList;
    }

    public List<PurchasedItem> getRecordsByID(String branchAddress, int itemID) {
        return (List<PurchasedItem>) (List<?>) selectListByBranch(branchAddress, itemID, ItemIDColumnName);
    }

    public List<PurchasedItem> getRecordsByBusinessNumber(String branchAddress, int supplierBusinessNumber) {
        return (List<PurchasedItem>) (List<?>) selectListByBranch(branchAddress, supplierBusinessNumber, SupplierBusinessNumberColumnName);
    }

    /////////////////////////////////

    @Override
    protected PurchasedItem ConvertReaderToObject(String branchAddress, ResultSet reader) {
        PurchasedItem result = null;
        try {
            result = new PurchasedItem(branchAddress, reader.getInt(2), reader.getInt(3),
                    reader.getString(4), reader.getInt(5), reader.getInt(6),
                    reader.getDouble(7), reader.getDouble(8));

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return result;
    }

    public void deleteRecord(String branchAddress, int itemID, String orderID){
        delete(branchAddress, itemID, orderID, ItemIDColumnName, OrderIDColumnName);
    }
}