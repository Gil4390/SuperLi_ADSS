package BusinessLayer.InventoryBusiness;

import BusinessLayer.InventoryBusiness.ReportItems.InShortageItem;
import DataAccessLayer.InventoryDAL.DALFacade;

import java.util.List;

public class ItemQuantity {
    private int amountInBackRoom;
    private int amountInFrontRoom;
    private int totalAmount;
    private int minimalQuantity;
    private int fullQuantity;

    private String branchAddress;

    public ItemQuantity (String branchAddress, int amountInBackRoom, int amountInFrontRoom, int totalAmount, int minimalQuantity, int fullQuantity){
        this.branchAddress = branchAddress;
        this.amountInBackRoom = amountInBackRoom;
        this.amountInFrontRoom = amountInFrontRoom;
        this.totalAmount = totalAmount;
        this.minimalQuantity=minimalQuantity;
        this.fullQuantity = fullQuantity;
    }

    public int getMinimalQuantity() {
        return minimalQuantity;
    }
    public void setMinimalQuantity(int minimalQuantity, int itemID) {
        if(minimalQuantity < 0)
            throw new IllegalArgumentException("can't set a negative amount as the item's minimal quantity");
        this.minimalQuantity = minimalQuantity;
        DALFacade.getInstance().setMinimalQuantity(branchAddress, minimalQuantity, itemID);
    }

    public int getFullQuantity() {
        return fullQuantity;
    }
    public void setFullQuantity(int fullQuantity, int itemID) {
        if(fullQuantity < 0)
            throw new IllegalArgumentException("can't set a negative amount as the item's full quantity");
        if(fullQuantity <= minimalQuantity)
            throw new IllegalArgumentException("can't set value less than the minimal quantity as the item's full quantity");
        this.fullQuantity = fullQuantity;
        DALFacade.getInstance().setFullQuantity(branchAddress, fullQuantity, itemID);
    }

    public int getAmountInBackRoom() {
        return amountInBackRoom;
    }
    public void setAmountInBackRoom(int amountInBackRoom, int itemID) {
        this.amountInBackRoom = amountInBackRoom;
        updateTotalAmount(itemID);
        DALFacade.getInstance().setAmountInBackRoom(branchAddress, amountInBackRoom, itemID);
        List<InShortageItem>shortageList = DALFacade.getInstance().getShortageList(branchAddress);
        if(isInShortage() && !checkItemInShortageList(itemID,shortageList)) //get into shortageList
            DALFacade.getInstance().addShortage(branchAddress, itemID);
        if(!isInShortage() && checkItemInShortageList(itemID,shortageList)) //get into shortageList
            DALFacade.getInstance().removeFromShortage(branchAddress, itemID);
    }

    public int getAmountInFrontRoom() {
        return amountInFrontRoom;
    }
    public void setAmountInFrontRoom(int amountInFrontRoom, int itemID) {
        this.amountInFrontRoom = amountInFrontRoom;
        updateTotalAmount(itemID);
        DALFacade.getInstance().setAmountInFrontRoom(branchAddress, amountInFrontRoom, itemID);
        List<InShortageItem> shortageList = DALFacade.getInstance().getShortageList(branchAddress);
        if(isInShortage() && !checkItemInShortageList(itemID,shortageList)) //get into shortageList
            DALFacade.getInstance().addShortage(branchAddress, itemID);
        if(!isInShortage() && checkItemInShortageList(itemID,shortageList)) //get into shortageList
            DALFacade.getInstance().removeFromShortage(branchAddress, itemID);
    }

    public int getTotalAmount() {
        return totalAmount;
    }
    private void updateTotalAmount(int itemID) {
        this.totalAmount = amountInBackRoom + amountInFrontRoom;
        DALFacade.getInstance().setTotalAmount(branchAddress, totalAmount, itemID);
    }

    public void addAmountToFront(int quantity, int itemID) {
        if (quantity < 0)
            throw new IllegalArgumentException("can't add a negative amount");
        setAmountInFrontRoom(getAmountInFrontRoom() + quantity, itemID);
    }

    public void addAmountToBack(int quantity, int itemID){
        if(quantity < 0)
            throw new IllegalArgumentException("can't add a negative amount");
        setAmountInBackRoom(getAmountInBackRoom() + quantity,itemID);
    }

    public void removeAmountFromFront(int quantity,int itemId){
        if(quantity > getAmountInFrontRoom())
            throw new IllegalArgumentException("There's not enough items in frontRoom");
        setAmountInFrontRoom(getAmountInFrontRoom() - quantity,itemId);
    }

    public void removeAmountFromBack(int quantity,int itemId){
        if(quantity < 0)
            throw new IllegalArgumentException("can't remove a negative amount");
        if(quantity > getAmountInBackRoom())
            throw new IllegalArgumentException("There's not enough items in backRoom");
        setAmountInBackRoom(getAmountInBackRoom() - quantity,itemId);
    }

    public void moveItemsBetweenRooms(boolean fromBackRoom, int quantity, int itemId){
        if(quantity < 0)
            throw new IllegalArgumentException("can't move a negative number");
        if(fromBackRoom){
            if(quantity > getAmountInBackRoom())
                throw new IllegalArgumentException("There's not enough items in backRoom");
            setAmountInFrontRoom(getAmountInFrontRoom() + quantity,itemId);
            setAmountInBackRoom(getAmountInBackRoom() - quantity,itemId);
        }
        else{
            if(quantity > getAmountInFrontRoom())
                throw new IllegalArgumentException("There's not enough items in frontRoom");
            setAmountInBackRoom(getAmountInBackRoom() + quantity,itemId);
            setAmountInFrontRoom(getAmountInFrontRoom() - quantity,itemId);
        }
    }

    private boolean checkItemInShortageList(int itemID, List<InShortageItem> shortageList) {
        for (InShortageItem item : shortageList){
            if(item.getItemID()==itemID)
                return true;
        }
        return false;
    }

    public boolean isInShortage(){return getMinimalQuantity() > getTotalAmount();}
}
