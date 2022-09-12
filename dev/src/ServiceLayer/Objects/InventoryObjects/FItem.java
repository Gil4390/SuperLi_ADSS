package ServiceLayer.Objects.InventoryObjects;

import BusinessLayer.InventoryBusiness.Item;

public class FItem {
    private final int itemID;
    private final String name;
    private final String categoryId;
    private final double currPrice;
    private final int minimalQuantity;
    private final int fullQuantity;
    private final String manufacture;
    private final double finalCurrentPrice;
    private final int quantityBackRoom;
    private final int quantityFrontRoom;

    public FItem(Item item){
        this.itemID = item.getItemID();
        this.name = item.getName();
        this.categoryId = item.getCategory().getCatID();
        this.currPrice = item.getPrice().getCurrentPrice();
        this.minimalQuantity = item.getQuantity().getMinimalQuantity();
        this.fullQuantity = item.getQuantity().getFullQuantity();
        this.manufacture = item.getManufacture();
        this.finalCurrentPrice = item.getPrice().getCalculatedPrice();
        this.quantityBackRoom = item.getQuantity().getAmountInBackRoom();
        this.quantityFrontRoom = item.getQuantity().getAmountInFrontRoom();
    }
//    public FItem(int id, String name, String catID, double currentPrice, int minimalQuantity, String manufacture, double calculatePrice){
//        this.itemID = id;
//        this.name = name;
//        this.categoryId = catID;
//        this.currPrice = currentPrice;
//        this.minimalQuantity = minimalQuantity;
//        this.manufacture = manufacture;
//        this.finalCurrentPrice = calculatePrice;
//    }

    public int getItemID() {return itemID;}
    public String getName() {return name;}
    public String getCategoryId() {return categoryId;}
    public double getCurrPrice() {return currPrice;}
    public int getMinimalQuantity() {return minimalQuantity;}
    public int getFullQuantity() {return fullQuantity;}
    public String getManufacture() {return manufacture;}
    public double getFinalCurrentPrice() {return finalCurrentPrice;}
    public int getQuantityBackRoom() { return quantityBackRoom;}
    public int getQuantityFrontRoom() { return quantityFrontRoom;}
}
