package BusinessLayer.InventoryBusiness.ReportItems;

import java.time.LocalDate;

public class PurchasedItem extends BasicItem{
    private final int orderID;
    private final LocalDate datePurchased;
    private final int supplierBusinessNumber;
    private final int amount;
    private final double unitPriceFromSupplier;
    private final double discountFromSupplier;

    public PurchasedItem(String branchAddress, int itemID, int orderID, String datePurchased, int supplierBusinessNumber, int amount, double unitPriceFromSupplier, double discountFromSupplier){
        super(branchAddress, itemID);
        this.orderID = orderID;
        this.datePurchased = LocalDate.parse(datePurchased);
        this.supplierBusinessNumber = supplierBusinessNumber;
        this.amount = amount;
        this.unitPriceFromSupplier = unitPriceFromSupplier;
        this.discountFromSupplier = discountFromSupplier;
    }

    public LocalDate getDatePurchased() {return datePurchased;}
    public int getOrderID() {return orderID;}
    public int getSupplierBusinessNumber() {return supplierBusinessNumber;}
    public int getAmount() {return amount;}
    public double getUnitPriceFromSupplier() {return unitPriceFromSupplier;}
    public double getDiscountFromSupplier() {return discountFromSupplier;}

}
