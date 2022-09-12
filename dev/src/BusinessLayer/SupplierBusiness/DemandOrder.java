package BusinessLayer.SupplierBusiness;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class DemandOrder extends Order {
    private Date supplyDate;
    private boolean isSupplied;

    public DemandOrder(String branchAddress, int orderId, int supplierBN, Date orderDate, Date supplyDate, HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        super(branchAddress, orderId, supplierBN, orderDate, itemIdAndAmount, billOfQuantity);
        this.supplyDate = supplyDate;
        this.isSupplied = false;
    }

    public DemandOrder(String branchAddress, int orderId, int supplierBN, double price, boolean isSupplied, Date orderDate, Date supplyDate) {
        super(branchAddress, orderId, supplierBN, price, orderDate);
        this.supplyDate = supplyDate;
        this.isSupplied = isSupplied;
    }

    public Date getSupplyDate() {
        updateTimeStamp();
        return supplyDate;
    }

    public void setSupplyDate(Date supplyDate) {
        updateTimeStamp();
        this.supplyDate = supplyDate;
    }

    public boolean isSupplied() {
        updateTimeStamp();
        return isSupplied;
    }

    public void setSupplied() {
        updateTimeStamp();
        isSupplied = true;
    }

    public void addProducts(HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        checkIfUpdateIsPermitted();
        makeOrderItems(itemIdAndAmount, billOfQuantity);
    }

    public void updateProducts(HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        checkIfUpdateIsPermitted();
        super.updateProducts(itemIdAndAmount, billOfQuantity);
    }

    public void updateUnSuppliedAmount(HashMap<Integer, Integer> itemIdAndUnSuppliedAmount) {
        super.updateUnSuppliedAmount(itemIdAndUnSuppliedAmount);
    }

    public void removeProducts(List<Integer> productsId) {
        checkIfUpdateIsPermitted();
        deleteOrderProducts(productsId);
    }

    private void checkIfUpdateIsPermitted() {
        if(supplyDate==null)
            return;
        Calendar c = Calendar.getInstance();
        c.setTime(supplyDate);
        c.add(Calendar.DATE, -1);
        if(isSupplied || c.before(new Date(System.currentTimeMillis())))
            throw new IllegalArgumentException("update is not allowed!");
    }

    protected boolean equals(DemandOrder other){
        return supplyDate.equals(other.getSupplyDate()) && isSupplied == other.isSupplied && getOrderId() == other.getOrderId() &&
                getSupplierBN() == other.getSupplierBN() && getPrice() == other.getPrice() && getOrderDate() == other.getOrderDate();
    }
}
