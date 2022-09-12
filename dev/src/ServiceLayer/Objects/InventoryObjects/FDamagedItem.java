package ServiceLayer.Objects.InventoryObjects;

import BusinessLayer.InventoryBusiness.ReportItems.DamagedItem;

import java.time.LocalDate;

public class FDamagedItem extends FBasicItem {
    private final LocalDate dateFound;
    private final int quantityFound;
    private final double priceInStore;
    private final FDamageReason ExpiredOrFault;
    private final boolean isFrontRoom;

    public FDamagedItem(DamagedItem damagedItem){
        super(damagedItem.getItemID() , damagedItem.getName(), damagedItem.getCategoryName(), damagedItem.getManufacture());
        this.dateFound = damagedItem.getDateFound();
        this.quantityFound = damagedItem.getQuantityFound();
        this.priceInStore = damagedItem.getPriceInStore();
        if(damagedItem.getExpiredOrFault().toString().equals("Expired"))
            this.ExpiredOrFault = FDamageReason.Expired;
        else
            this.ExpiredOrFault = FDamageReason.Damaged;
        this.isFrontRoom = damagedItem.getIsFrontRoom();
    }

//    public FDamagedItem(int itemID, String itemName, String categoryName, String manufacture, LocalDate dateFound, int quantityFound, double priceInStore, FDamageReason ExpiredOrFault, boolean isFrontRoom){
//        super(itemID , itemName, categoryName, manufacture);
//        this.dateFound = dateFound;
//        this.quantityFound = quantityFound;
//        this.priceInStore = priceInStore;
//        this.ExpiredOrFault = ExpiredOrFault;
//        this.isFrontRoom = isFrontRoom;
//    }

    public LocalDate getDateFound() {return dateFound;}
    public int getQuantityFound() {return quantityFound;}
    public double getPriceInStore() {return priceInStore;}
    public FDamageReason getExpiredOrFault() {return ExpiredOrFault;}
    public boolean getIsFrontRoom() {return isFrontRoom;}
}
