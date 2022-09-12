package BusinessLayer.InventoryBusiness;

import DataAccessLayer.InventoryDAL.DALFacade;

public class Item {
    private final int itemID;
    private String name;
    private Category category;
    private final ItemPrice price;
    private final ItemLocation location;
    private final ItemQuantity quantity;
    private String manufacture;

    private final String branchAddress;

    public Item(String branchAddress, int itemID, String name, String categoryID, String manufacture){
        this.branchAddress = branchAddress;
        this.itemID = itemID;
        this.name = name;
        this.category = DALFacade.getInstance().getCategory(branchAddress, categoryID);
        this.price = DALFacade.getInstance().getItemPrice(branchAddress, getItemID());
        this.location = DALFacade.getInstance().getItemLocation(branchAddress, getItemID());
        this.quantity = DALFacade.getInstance().getItemQuantity(branchAddress, itemID);
        this.manufacture = manufacture;
    }

    public int getItemID() {return itemID;}
    public String getName() {return name;}
    public void setName(String name) {
        this.name = name;
        DALFacade.getInstance().setItemName(branchAddress, getItemID(),name);
    }
    public Category getCategory() {return category;}
    public void setCategory(Category category) {
        category.removeItemID(itemID);// remember that category remove from dal
        this.category = category;
        category.addItemID(itemID);// remember that category remove from dal
    }
    public ItemPrice getPrice() {return price;}
    public ItemLocation getLocation() {return location;}
    public ItemQuantity getQuantity() {return quantity;}
    public String getManufacture() {
        return manufacture;
    }
    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
        DALFacade.getInstance().setItemManufacture(branchAddress, getItemID(),manufacture);
    }

}
