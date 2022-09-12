package ServiceLayer.Objects.SupplierObjects;

import BusinessLayer.SupplierBusiness.SupplierItem;

import java.util.Map;

public class SSupplierItem {
    private final int supplierBN;
    private final int catalogNumber;
    private final int supplierCatalog;
    private final double price;
    private final String itemName;
    private final Map<Integer, Double> discountAccordingToAmount;

    public SSupplierItem(SupplierItem supplierItem) {
        supplierBN = supplierItem.getSupplierBN();
        catalogNumber = supplierItem.getCatalogNumber();
        supplierCatalog = supplierItem.getSupplierCatalog();
        price = supplierItem.getPrice();
        itemName = supplierItem.getItemName();
        discountAccordingToAmount = supplierItem.getItemDiscounts();
    }

    public int getSupplierBN() {
        return supplierBN;
    }

    public int getCatalogNumber() {
        return catalogNumber;
    }

    public int getSupplierCatalog() {
        return supplierCatalog;
    }

    public double getPrice() {
        return price;
    }

    public String getItemName() {
        return itemName;
    }

    public Map<Integer, Double> getDiscountAccordingToAmount() {
        return discountAccordingToAmount;
    }

}
