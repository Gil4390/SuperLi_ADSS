package BusinessLayer.InventoryBusiness.ReportItems;

import java.util.List;

public class InventoryItem extends BasicItem{
    private final List<Integer> frontShelfLocation;
    private final int frontAmount;
    private final List<Integer> backShelfLocation;
    private final int backAmount;
    private final int totalAmount;
    private final double priceInStore;
    private final double priceAfterDiscounts;

    public InventoryItem(String branchAddress, int itemID, List<Integer> frontShelfLocation, int frontAmount, List<Integer> backShelfLocation, int backAmount, int totalAmount, double priceInStore, double priceAfterDiscounts){
        super(branchAddress, itemID);
        this.frontShelfLocation = frontShelfLocation;
        this.frontAmount = frontAmount;
        this.backShelfLocation = backShelfLocation;
        this.backAmount = backAmount;
        this.totalAmount = totalAmount;
        this.priceInStore = priceInStore;
        this.priceAfterDiscounts = priceAfterDiscounts;
    }

    public List<Integer> getFrontShelfLocation() {return frontShelfLocation;}
    public int getFrontAmount() {return frontAmount;}
    public List<Integer> getBackShelfLocation() {return backShelfLocation;}
    public int getBackAmount() {return backAmount;}
    public int getTotalAmount() {return totalAmount;}
    public double getPriceInStore() {return priceInStore;}
    public double getPriceAfterDiscounts() {return priceAfterDiscounts;}
}
