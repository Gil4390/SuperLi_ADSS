package DataAccessLayer.InventoryDAL;

import BusinessLayer.InventoryBusiness.Discount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

public class DiscountsDAO extends AbstractDAO {
    private final String BranchAddressColumnName = "BranchAddress";
    private final String ItemIDColumnName = "ItemID";
    private final String DiscountNameColumnName = "DiscountName";
    private final String FromDateColumnName = "FromDate";
    private final String EndDateColumnName = "EndDate";
    private final String DiscountFareColumnName = "DiscountFare";

    public DiscountsDAO() {
        super("Discounts");
    }

    @Override
    public void cleanCache() {

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // insert a new Discount to the database
    public Discount insert(String branchAddress, int itemID, String name, String fromDate, String endDate, double discountFare) {
        Discount discount = new Discount(name, LocalDate.parse(fromDate), LocalDate.parse(endDate), discountFare);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}, {6}) VALUES(?,?,?,?,?,?)",
                getTableName(), BranchAddressColumnName, ItemIDColumnName, DiscountNameColumnName,
                FromDateColumnName, EndDateColumnName, DiscountFareColumnName);
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, branchAddress);
            pstmt.setInt(2, itemID);
            pstmt.setString(3, discount.getName());
            pstmt.setString(4, discount.getFromDate().toString());
            pstmt.setString(5, discount.getToDate().toString());
            pstmt.setDouble(6, discount.getDiscountFare());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+", problem at inserting a new discount to dal");
        }
        return discount;
    }

    public void updateDiscountName(String branchAddress, int itemID, String fromDate, String discountName) {
        update(branchAddress, itemID, fromDate, DiscountNameColumnName, discountName, FromDateColumnName,  ItemIDColumnName);
    }

    public void updateEndDate(String branchAddress, int itemID, String fromDate, String endDate) {
        update(branchAddress, itemID, fromDate, EndDateColumnName, endDate, FromDateColumnName, ItemIDColumnName);
    }

    public void updateDiscountFare(String branchAddress, int itemID, String fromDate, double discountFare) {
        update(branchAddress, itemID, fromDate, DiscountFareColumnName, discountFare, FromDateColumnName, ItemIDColumnName);
    }

    public Discount getDiscount(String branchAddress, int itemID, String fromDate) {
        Discount discount = (Discount) select(branchAddress, itemID, ItemIDColumnName);
        return discount;
    }

    public List<Discount> getAllItemsDiscounts(String branchAddress, int itemID) {
        List<Discount> itemsDiscounts = (List<Discount>)(List<?>) selectListByBranch(branchAddress, itemID, ItemIDColumnName);
        return itemsDiscounts;
    }

    @Override
    protected Discount ConvertReaderToObject(String branchAddress, ResultSet reader) {
        Discount result = null;
        try {
            result = new Discount(reader.getString(3), reader.getString(4),
                    reader.getString(5), reader.getDouble(6));
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return result;
    }

    public void deleteRecord(String branchAddress, int itemID, String fromDate){
        delete(branchAddress, itemID, fromDate, ItemIDColumnName, FromDateColumnName);
    }

    public void removeDis(String branchAddress, int itemID, String toString) {
        deleteRecord(branchAddress, itemID,toString);
    }
}
