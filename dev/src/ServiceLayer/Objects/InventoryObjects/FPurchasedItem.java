package ServiceLayer.Objects.InventoryObjects;

import BusinessLayer.InventoryBusiness.ReportItems.PurchasedItem;

import java.time.LocalDate;

public class FPurchasedItem extends FBasicItem{
    private final int orderID;
    private final LocalDate datePurchased;
    private final int supplierBusinessNumber;
    private final int amount;
    private final double unitPriceFromSupplier;
    private final double discountFromSupplier;

    public FPurchasedItem(PurchasedItem item){
        super(item.getItemID() , item.getName(), item.getCategoryName(), item.getManufacture());
        this.orderID = item.getOrderID();
        this.datePurchased = item.getDatePurchased();
        this.supplierBusinessNumber = item.getSupplierBusinessNumber();
        this.amount = item.getAmount();
        this.unitPriceFromSupplier = item.getUnitPriceFromSupplier();
        this.discountFromSupplier = item.getDiscountFromSupplier();
    }

//    public FPurchasedItem(int itemID, String itemName, String categoryName, String manufacture, LocalDate datePurchased, int supplierBusinessNumber, int amount, double unitPriceFromSupplier, double discountFromSupplier){
//        super(itemID , itemName, categoryName, manufacture);
//        this.datePurchased = datePurchased;
//        this.supplierBusinessNumber = supplierBusinessNumber;
//        this.amount = amount;
//        this.unitPriceFromSupplier = unitPriceFromSupplier;
//        this.discountFromSupplier = discountFromSupplier;
//    }

    public LocalDate getDatePurchased() {return datePurchased;}
    public int getOrderID() { return  orderID;}
    public int getSupplierBusinessNumber() {return supplierBusinessNumber;}
    public int getAmount() {return amount;}
    public double getUnitPriceFromSupplier() {return unitPriceFromSupplier;}
    public double getDiscountFromSupplier() {return discountFromSupplier;}
}