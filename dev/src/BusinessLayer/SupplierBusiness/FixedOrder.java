package BusinessLayer.SupplierBusiness;

import java.sql.Date;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class FixedOrder extends Order {
    private final Set<DayOfWeek> daysOfSupply;
    private boolean isActive;
    private Date canceledDate;

    public FixedOrder(String branchAddress, int orderId, int supplierBN, Date orderDate, Set<DayOfWeek> daysOfSupply, HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        super(branchAddress, orderId, supplierBN, orderDate, itemIdAndAmount, billOfQuantity);
        this.daysOfSupply = daysOfSupply;
        this.isActive = true;
    }

    public FixedOrder(String branchAddress, int orderId, int supplierBN, double price, boolean isActive, Date orderDate, Date canceledDate, Set<DayOfWeek> daysOfSupply) {
        super(branchAddress, orderId, supplierBN, price, orderDate);
        this.isActive = isActive;
        this.daysOfSupply = daysOfSupply;
        this.canceledDate = canceledDate;
    }

    public Set<DayOfWeek> getDaysOfSupply() {
        updateTimeStamp();
        return daysOfSupply;
    }

    public boolean isActive() {
        updateTimeStamp();
        return isActive;
    }

    public void setUnActive() {
        updateTimeStamp();
        isActive = false;
    }

    public Date getCanceledDate() {
        updateTimeStamp();
        return canceledDate;
    }

    public void setCanceledDate(Date canceledDate) {
        updateTimeStamp();
        this.canceledDate = canceledDate;
    }

    public boolean equals(FixedOrder other){
        return daysOfSupply.equals(other.getDaysOfSupply()) && canceledDate.equals(other.canceledDate) && isActive == other.isActive&& getOrderId() == other.getOrderId() &&
                getSupplierBN() == other.getSupplierBN() && getPrice() == other.getPrice() && getOrderDate() == other.getOrderDate();
    }

    public void addProducts(HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        checkIfUpdateIsPermitted();
        makeOrderItems(itemIdAndAmount, billOfQuantity);
    }

    public void updateProducts(HashMap<Integer, Integer> itemIdAndAmount, BillOfQuantity billOfQuantity) {
        checkIfUpdateIsPermitted();
        super.updateProducts(itemIdAndAmount, billOfQuantity);
    }

    public void removeProducts(List<Integer> productsId) {
        checkIfUpdateIsPermitted();
        deleteOrderProducts(productsId);
    }

    private void checkIfUpdateIsPermitted() {
        int dayOfWeek = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1) == 0 ? 7 : Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        DayOfWeek today = DayOfWeek.of(dayOfWeek);
        if(daysOfSupply.contains(today) || daysOfSupply.contains(today.plus(1)))
            throw new IllegalArgumentException("update is not allowed!");
    }

    public boolean isDaily() {
        int dayOfWeek = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1) == 0 ? 7 : Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        DayOfWeek today = DayOfWeek.of(dayOfWeek);
        updateTimeStamp();
        return daysOfSupply.contains(today);
    }

    public void addSupplyDay(DayOfWeek day) {
        daysOfSupply.add(day);
    }

    public void removeSupplyDay(DayOfWeek day) {
        daysOfSupply.remove(day);
    }
}
