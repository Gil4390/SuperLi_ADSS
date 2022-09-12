package BusinessLayer.InventoryBusiness.ReportItems;

import DataAccessLayer.InventoryDAL.DALFacade;

import java.time.LocalDate;

public class DamagedItem extends BasicItem{

    private final LocalDate dateFound;
    private final int quantityFound;
    private final double priceInStore;
    private final DamageReason ExpiredOrFault;
    private final boolean isFrontRoom;

    public DamagedItem(String branchAddress, int itemID, String timeFound, int quantityFound, String expiredOrDamaged, int back0Front1){
        super(branchAddress, itemID);
        this.dateFound = LocalDate.parse(timeFound);
        this.quantityFound = quantityFound;
        this.priceInStore = DALFacade.getInstance().getItemPrice(branchAddress, itemID).getCurrentPrice();
        if(expiredOrDamaged.equals("Expired"))
            this.ExpiredOrFault = DamageReason.Expired;
        else if(expiredOrDamaged.equals("Damaged"))
            this.ExpiredOrFault = DamageReason.Damaged;
        else
            throw new IllegalArgumentException("Illegal DamageReason received in DamagedItem Constructor");
        if(back0Front1 == 0)
            this.isFrontRoom = false;
        else if(back0Front1 == 1)
            this.isFrontRoom = true;
        else
            throw new IllegalArgumentException("Illegal back0Front1 received in DamagedItem Constructor");
    }

    public LocalDate getDateFound() {return dateFound;}
    public int getQuantityFound() {return quantityFound;}
    public double getPriceInStore() {return priceInStore;}
    public DamageReason getExpiredOrFault() {return ExpiredOrFault;}
    public boolean getIsFrontRoom() {return isFrontRoom;}
}
