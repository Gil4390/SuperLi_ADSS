package ServiceLayer.Objects.InventoryObjects;

import BusinessLayer.InventoryBusiness.ReportItems.InventoryItem;

public class FInventoryItem extends FBasicItem{
    private final int frontAmount;
    private final int backAmount;
    private final int totalAmount;
    private final double priceInStore;
    private final double priceAfterDiscounts;

    public FInventoryItem(InventoryItem item){
        super(item.getItemID() , item.getName(), item.getCategoryName(), item.getManufacture());
        this.frontAmount = item.getFrontAmount();
        this.backAmount = item.getBackAmount();
        this.totalAmount = item.getTotalAmount();
        this.priceInStore = item.getPriceInStore();
        this.priceAfterDiscounts = item.getPriceAfterDiscounts();
    }

    public int getFrontAmount() {return frontAmount;}
    public int getBackAmount() {return backAmount;}
    public int getTotalAmount() {return totalAmount;}
    public double getPriceInStore() {return priceInStore;}
    public double getPriceAfterDiscounts() {return priceAfterDiscounts;}
}
