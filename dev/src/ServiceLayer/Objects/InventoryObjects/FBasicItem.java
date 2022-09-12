package ServiceLayer.Objects.InventoryObjects;

public abstract class FBasicItem {
    private final int itemID;
    private final String name;
    private final String categoryName;
    private final String manufacture;

    public FBasicItem(int itemID, String name, String categoryName, String manufacture){
        this.itemID = itemID;
        this.name = name;
        this.categoryName = categoryName;
        this.manufacture = manufacture;
    }

    public int getItemID() {return itemID;}
    public String getName() {return name;}
    public String getCategoryName() {return categoryName;}
    public String getManufacture() {return manufacture;}
}
