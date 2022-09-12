package BusinessLayer.InventoryBusiness.ReportItems;

import DataAccessLayer.InventoryDAL.DALFacade;

public class InShortageItem extends BasicItem {
    private final int currentQuantity;
    private final int minimalQuantity;
    private final int fullQuantity;

    public InShortageItem(String branchAddress, int itemID){
        super(branchAddress, itemID);
        this.currentQuantity = DALFacade.getInstance().getItemQuantity(branchAddress, itemID).getTotalAmount();
        this.minimalQuantity = DALFacade.getInstance().getItemQuantity(branchAddress, itemID).getMinimalQuantity();
        this.fullQuantity = DALFacade.getInstance().getItemQuantity(branchAddress, itemID).getFullQuantity();
    }

    public int getCurrentQuantity() {return currentQuantity;}
    public int getMinimalQuantity() {return minimalQuantity;}
    public int getFullQuantity() {return fullQuantity;}
}
