package Presentation.Model.InventoryModel;

import Presentation.Model.BackendController;
import ServiceLayer.Objects.InventoryObjects.FItem;

import java.time.LocalDate;
import java.util.List;

public class ItemModel {
    private int itemID;
    private String name;
    private String categoryId;
    private double currPrice;
    private int minimalQuantity;
    private int fullQuantity;
    private String manufacture;
    private double finalCurrentPrice;
    private int quantityBackRoom;
    private int quantityFrontRoom;

    public ItemModel(FItem item){
        name = item.getName();
        itemID=item.getItemID();
        categoryId = item.getCategoryId();
        currPrice=item.getCurrPrice();
        minimalQuantity = item.getMinimalQuantity();
        fullQuantity = item.getFullQuantity();
        manufacture = item.getManufacture();
        finalCurrentPrice = item.getFinalCurrentPrice();
        quantityBackRoom = item.getQuantityBackRoom();
        quantityFrontRoom = item.getQuantityFrontRoom();
    }

    public int getItemID() {return itemID;}
    public String getName(){return name;}
    public String getCategoryId() {
        return categoryId;
    }
    public double getCurrPrice() {
        return currPrice;
    }
    public int getMinimalQuantity() {
        return minimalQuantity;
    }
    public int getFullQuantity() { return fullQuantity;}
    public String getManufacture() {
        return manufacture;
    }
    public double getFinalCurrentPrice() {
        return finalCurrentPrice;
    }
    //public void addItem() {backendController.addItem(this);}
    public void buyItem(int amount) {
        BackendController.getInstance().buyItem(this, amount);}
    //public void orderDelivery() {}
    public List<Integer> getAllFrontShelves() {
        return BackendController.getInstance().getAllItemFrontShelves(this);
    }
    public List<Integer> getAllBackShelves() {
        return BackendController.getInstance().getAllItemBackShelves(this);
    }
    public int getQuantityBackRoom() {return quantityBackRoom;}
    public int getQuantityFrontRoom() {return quantityFrontRoom;}

    public void addDiscount(double discountFare, String name, LocalDate fromDate, LocalDate toDate) {BackendController.getInstance().addDiscount(this, name,discountFare,fromDate,toDate);}
    public void moveItems(int itemID, boolean fromBackRoom, int checkedAmount) {BackendController.getInstance().moveItemLocation(itemID,fromBackRoom, checkedAmount);}

    public void changeItemBackShelves(int[] idsIntArr) {
        BackendController.getInstance().changeAllItemBackShelves(this,idsIntArr);
    }

    public void changeItemFrontShelves(int[] idsIntArr) {
        BackendController.getInstance().changeAllItemFrontShelves(this,idsIntArr);
    }

    public void removeQuantity(boolean inBackRoom, int checkedAmount) {
        BackendController.getInstance().removeQuantityFromItem(itemID, inBackRoom, checkedAmount);
    }

    public void addQuantity(boolean inBackRoom, int checkedAmount2) {
        BackendController.getInstance().addQuantityToItem(itemID, inBackRoom, checkedAmount2);
    }

    public void removeDiscount(LocalDate fromDate) {
        BackendController.getInstance().removeDiscount(this, fromDate);
    }

    public void changeItemPrice(int checkedAmount) {
        BackendController.getInstance().changeItemPrice(itemID, checkedAmount);
    }

    public void changeMinimalQuantity(int checkedQuantity) {
        BackendController.getInstance().changeMinimalQuantity(itemID, checkedQuantity);

    }


    public void changeFullQuantity(int checkedQuantity) {
        BackendController.getInstance().changeFullQuantity(itemID, checkedQuantity);
    }
}
