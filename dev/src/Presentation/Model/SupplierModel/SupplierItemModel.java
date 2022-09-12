package Presentation.Model.SupplierModel;

import ServiceLayer.Objects.SupplierObjects.SSupplierItem;

import java.util.Map;

public class SupplierItemModel {
    private final int supplierBN;
    private final int catalogNumber;
    private int supplierCatalog;
    private double price;
    private String itemName;
    private final Map<Integer, Double> discountAccordingToAmount;

    public SupplierItemModel(SSupplierItem supplierItem) {
        this.supplierBN = supplierItem.getSupplierBN();
        this.catalogNumber = supplierItem.getCatalogNumber();
        this.supplierCatalog = supplierItem.getSupplierCatalog();
        this.price = supplierItem.getPrice();
        this.itemName = supplierItem.getItemName();
        this.discountAccordingToAmount = supplierItem.getDiscountAccordingToAmount();
    }

    @Override
    public String toString() {
        String output = "-Catalog number: "+catalogNumber+", supplier catalog: "+supplierCatalog+ ", price: "+ price+", name: "+itemName+"\n";
        if(discountAccordingToAmount.size()==1)
            output += "\tdiscounts: none\n";
        else {
            output += "\tdiscounts: \n";
            for (int amount : discountAccordingToAmount.keySet())
                if (amount != 0)
                    output += "\t\tamount: " + amount + " discount: " + discountAccordingToAmount.get(amount) + "\n";
        }
            return output;
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

    public void setSupplierCatalog(int supplierCatalog) {
        this.supplierCatalog = supplierCatalog;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
