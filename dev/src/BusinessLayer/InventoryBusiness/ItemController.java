package BusinessLayer.InventoryBusiness;
import BusinessLayer.InventoryBusiness.ReportItems.InShortageItem;
import DataAccessLayer.InventoryDAL.DALFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ItemController {

    private HashMap <String,CategoryTree> categoryTrees;

    public ItemController(){
        categoryTrees = new HashMap<>();
    }

    public List<InShortageItem> getShortageList(String branchAddress)
    {return DALFacade.getInstance().getShortageList(branchAddress);}
    public CategoryTree getCategoryTree(String branchAddress) {
        if(!categoryTrees.containsKey(branchAddress))
            categoryTrees.put(branchAddress, new CategoryTree(branchAddress));
        return categoryTrees.get(branchAddress);
    }

    public List<Item> getAllItems(String branchAddress){
        List <Item> list = new LinkedList<>();
        HashMap<Integer,Item> map = DALFacade.getInstance().getAllItemsMap(branchAddress);
        for(Item item : map.values()){
            list.add(item);
        }
        return list;
    }

    public List<Item> getAllItemsByCategory(String branchAddress, String categoryID){
        List<Item> itemsOfCategory = new ArrayList<Item>();
        List<Integer> itemIdList = categoryTrees.get(branchAddress).getCategory(branchAddress, categoryID).getItemIDList();
        for(Integer id: itemIdList)
            itemsOfCategory.add(getItem(branchAddress, id));
        return itemsOfCategory;
    }

    public void giveDiscountToCategory(String branchAddress, String categoryID, Discount discount){
        List<Item> itemsOfCategory = getAllItemsByCategory(branchAddress, categoryID);
        for(Item item : itemsOfCategory){
            giveDiscountToItem(branchAddress, item.getItemID(), discount);
        }
    }

    public void giveDiscountToItem(String branchAddress, int itemId, Discount discount){
        getItem(branchAddress, itemId).getPrice().addDiscount(itemId, discount);
    }

    public Item getItem(String branchAddress, int itemID){
        return DALFacade.getInstance().getItem(branchAddress, itemID);
    }

    public Item addItem(String branchAddress, String name, String categoryID, double currPrice, int minimalQuantity, int fullQuantity, String manufacture, List<Integer> backShelves, List<Integer> frontShelves){
        try {
            if(!categoryTrees.containsKey(branchAddress))
                categoryTrees.put(branchAddress,new CategoryTree(branchAddress));
            int idCounter = DALFacade.getInstance().getItemCounter(branchAddress)+1;
            Category category = categoryTrees.get(branchAddress).getCategory(branchAddress, categoryID);
            category.addItemID(idCounter);// this is already add itemToCategory Dall
            DALFacade.getInstance().addItemPrice(branchAddress, idCounter, currPrice);
            DALFacade.getInstance().addItemQuantity(branchAddress, idCounter, minimalQuantity, fullQuantity);
            DALFacade.getInstance().addItemLocation(branchAddress, idCounter, backShelves, frontShelves);
            DALFacade.getInstance().addItem(branchAddress, idCounter, name, categoryID, manufacture);
            Item item = DALFacade.getInstance().getItem(branchAddress, idCounter);
            List <InShortageItem> shortageList = DALFacade.getInstance().getShortageList(branchAddress);
            if (item.getQuantity().isInShortage() && !checkItemInShortageList(item.getItemID(),shortageList)) //get into shortageList
                DALFacade.getInstance().addShortage(branchAddress, item.getItemID());
            return item;
        }
        catch (Exception e){
            throw new IllegalArgumentException(e.getMessage()+" ,failed to add item");
        }
    }

    public void removeQuantityFromItem(String branchAddress, int itemId, int quantity, boolean fromBackRoom){
        try {
            Item myItem = getItem(branchAddress, itemId);
            if (fromBackRoom)
                myItem.getQuantity().removeAmountFromBack(quantity, itemId);
            else
                myItem.getQuantity().removeAmountFromFront(quantity, itemId);
        }catch(Exception e){
            throw new IllegalArgumentException("There isn't an existing item with that id");
        }
        addOrRemoveItemToShortageIfNeeded(branchAddress,itemId);
    }

    private void addOrRemoveItemToShortageIfNeeded(String branchAddress, int itemId) {
        Item myItem = getItem(branchAddress, itemId);
        List<InShortageItem>shortageList = DALFacade.getInstance().getShortageList(branchAddress);
        if(myItem.getQuantity().isInShortage() && !checkItemInShortageList(itemId,shortageList)) //get into shortageList
            DALFacade.getInstance().addShortage(branchAddress, itemId);
        if(!myItem.getQuantity().isInShortage() && checkItemInShortageList(itemId,shortageList)) //get into shortageList
            DALFacade.getInstance().removeFromShortage(branchAddress, itemId);
    }

    public void addQuantityToItem(String branchAddress, int itemID, int quantity, boolean fromBackRoom){
//        if(!itemMap.containsKey(itemID))
//            throw new IllegalArgumentException("There isn't an existing item with that id");
        Item myItem = getItem(branchAddress, itemID);
        if(fromBackRoom)
            myItem.getQuantity().addAmountToBack(quantity,itemID);
        else
            myItem.getQuantity().addAmountToFront(quantity,itemID);
        addOrRemoveItemToShortageIfNeeded(branchAddress,itemID);
    }

    public void setMinimalQuantity(String branchAddress, int itemID, int checkedQuantity) {
        Item myItem = getItem(branchAddress, itemID);
        myItem.getQuantity().setMinimalQuantity(checkedQuantity,itemID);
        addOrRemoveItemToShortageIfNeeded(branchAddress,itemID);
    }

    private boolean checkItemInShortageList(int itemID, List<InShortageItem> shortageList) {
        for (InShortageItem item : shortageList){
            if(item.getItemID()==itemID)
                return true;
        }
        return false;
    }

    public void setFullQuantity(String branchAddress, int itemID, int checkedQuantity) {
        getItem(branchAddress, itemID).getQuantity().setFullQuantity(checkedQuantity,itemID);
    }
}