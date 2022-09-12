package ServiceLayer.Objects.DeliveryObjects;

import BusinessLayer.DeliveryModule.Objects.StockShortness;

public class FStockShortness {
    private final int id;
    private final String branchAddress;
    private final String ItemName;
    private final int ItemCatalogNumber;
    private final int Amount;
    private final String ProviderAddress;

    public FStockShortness(StockShortness stockShortness){
        this.id = stockShortness.getId();
        this.branchAddress = stockShortness.getBranchAddress();
        this.ItemName = stockShortness.getItemName();
        this.ItemCatalogNumber = stockShortness.getItemCatalogNumber();
        this.Amount = stockShortness.getAmount();
        this.ProviderAddress = stockShortness.getSupplierAddresses();
    }
    public FStockShortness(int id, String branchAddress, String itemName, int itemCatalogNumber,
                          int amount, String supplierAddress, int isBounded){
        this.id = id;
        this.branchAddress = branchAddress;
        this.ItemName = itemName;
        this.ItemCatalogNumber = itemCatalogNumber;
        this.Amount = amount;
        this.ProviderAddress = supplierAddress;
    }

    public String getBranchAddress() { return branchAddress; }
    public String getItemName() { return ItemName; }
    public int getItemCatalogNumber() { return ItemCatalogNumber; }
    public int getAmount() { return Amount; }
    public String getSupplierAddresses() { return ProviderAddress; }


    public int getId() {
        return id;
    }

}
