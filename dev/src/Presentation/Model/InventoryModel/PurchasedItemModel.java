package Presentation.Model.InventoryModel;

import Presentation.Model.BackendController;
import ServiceLayer.Objects.InventoryObjects.FPurchasedItem;

import java.time.LocalDate;
import java.util.List;

public class PurchasedItemModel extends BasicItemModel{
    private final int orderID;
    private final LocalDate datePurchased;
    private final int supplierBusinessNumber;
    private final int amount;
    private final double unitPriceFromSupplier;
    private final double discountFromSupplier;

    public PurchasedItemModel(FPurchasedItem item) {
        super(item.getItemID() , item.getName(), item.getCategoryName(), item.getManufacture());
        this.orderID = item.getOrderID();
        this.datePurchased = item.getDatePurchased();
        this.supplierBusinessNumber = item.getSupplierBusinessNumber();
        this.amount = item.getAmount();
        this.unitPriceFromSupplier = item.getUnitPriceFromSupplier();
        this.discountFromSupplier = item.getDiscountFromSupplier();
    }

    public static List<PurchasedItemModel> getReport() {
        return BackendController.getInstance().getPurchaseReport();
    }

    public static List<PurchasedItemModel> getReportByDate(LocalDate sinceDate, LocalDate toDate) {
        return BackendController.getInstance().getPurchaseReportByDate(sinceDate,toDate);
    }

    public static List<PurchasedItemModel> getReportByItemID(int itemID) {
        return BackendController.getInstance().getPurchaseReportByItemID(itemID);
    }

    public static List<PurchasedItemModel> getPurchaseReportByBusinessNumber(int supplierBusinessNumber) {
        return BackendController.getInstance().getPurchaseReportByBusinessNumber(supplierBusinessNumber);
    }

    public int getOrderID() {return orderID;}
    public LocalDate getDatePurchased() {return datePurchased;}
    public int getSupplierBusinessNumber() {return supplierBusinessNumber;}
    public int getAmount() {return amount;}
    public double getUnitPriceFromSupplier() {return unitPriceFromSupplier;}
    public double getDiscountFromSupplier() {return discountFromSupplier;}

}
