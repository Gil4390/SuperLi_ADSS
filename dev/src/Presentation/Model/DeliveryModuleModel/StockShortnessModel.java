package Presentation.Model.DeliveryModuleModel;

import ServiceLayer.Objects.DeliveryObjects.FStockShortness;

import java.util.Scanner;

public class StockShortnessModel {
    private final String branchAddress;
    private final String ItemName;
    private final int ItemCatalogNumber;
    private final int Amount;
    private final String ProviderAddress;
    private final int id;
    private final Scanner scanner;


    public String getBranchAddress() {
        return branchAddress;
    }

    public String getItemName() {
        return ItemName;
    }

    public int getItemCatalogNumber() {
        return ItemCatalogNumber;
    }

    public int getAmount() {
        return Amount;
    }

    public String getProviderAddress() {
        return ProviderAddress;
    }

    public StockShortnessModel(FStockShortness fstockShortness)
    {
        this.branchAddress = fstockShortness.getBranchAddress();
        this.ItemName = fstockShortness.getItemName();
        this.ItemCatalogNumber = fstockShortness.getItemCatalogNumber();
        this.Amount = fstockShortness.getAmount();
        this.ProviderAddress = fstockShortness.getSupplierAddresses();
        this.id = fstockShortness.getId();
        this.scanner = new Scanner(System.in);
    }

    public String toString(){
        return "Ordered: "+branchAddress+"    Item name: "+ItemName+"    Amount: "+Amount+"    From: "+ProviderAddress;
    }

    public int getId() {
        return id;
    }

}
