package BusinessLayer.InventoryBusiness.ReportItems;

import BusinessLayer.InventoryBusiness.Item;
import DataAccessLayer.InventoryDAL.DALFacade;

public abstract class BasicItem {
    private final int itemID;
    private final String name;
    private final String categoryName;
    private final String manufacture;

    public BasicItem(String branchAddress, int itemID){
        this.itemID = itemID;
        Item tmp = DALFacade.getInstance().getItem(branchAddress, itemID);
        this.name =  tmp.getName();
        this.categoryName = tmp.getCategory().getName();
        this.manufacture = tmp.getManufacture();
    }

    public int getItemID() {return itemID;}
    public String getName() {return name;}
    public String getCategoryName() {return categoryName;}
    public String getManufacture() {return manufacture;}
}
