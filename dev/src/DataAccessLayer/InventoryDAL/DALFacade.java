package DataAccessLayer.InventoryDAL;

import BusinessLayer.InventoryBusiness.*;
import BusinessLayer.InventoryBusiness.ReportItems.DamagedItem;
import BusinessLayer.InventoryBusiness.ReportItems.InShortageItem;
import BusinessLayer.InventoryBusiness.ReportItems.PurchasedItem;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class DALFacade {
    private static DALFacade instance = null;
    private static CategoriesDAO categoriesDAO;
    private static DamagedItemsDAO damagedItemsDAO;
    private static DiscountsDAO discountsDAO;
    private static InShortageItemsDAO inShortageItemsDAO;
    private static ItemLocationDAO itemLocationDAO;
    private static ItemPriceDAO itemPriceDAO;
    private static ItemQuantityDAO itemQuantityDAO;
    private static ItemsByCategoryDAO itemsByCategoryDAO;
    private static ItemsDAO itemsDAO;
    private static PurchasedItemsDAO purchasedItemsDAO;
    private static ShelvesHandlerDAO shelvesHandlerDAO;

    private DALFacade(){
        categoriesDAO = new CategoriesDAO();
        damagedItemsDAO = new DamagedItemsDAO();
        discountsDAO = new DiscountsDAO();
        inShortageItemsDAO = new InShortageItemsDAO();
        itemLocationDAO = new ItemLocationDAO();
        itemPriceDAO = new ItemPriceDAO();
        itemQuantityDAO = new ItemQuantityDAO();
        itemsByCategoryDAO = new ItemsByCategoryDAO();
        itemsDAO = new ItemsDAO();
        purchasedItemsDAO = new PurchasedItemsDAO();
        shelvesHandlerDAO = new ShelvesHandlerDAO();
    }

    public void cleanCache(){
        categoriesDAO.cleanCache();
        damagedItemsDAO.cleanCache();
        discountsDAO.cleanCache();
        inShortageItemsDAO.cleanCache();
        itemLocationDAO.cleanCache();
        itemPriceDAO.cleanCache();
        itemQuantityDAO.cleanCache();
        itemsByCategoryDAO.cleanCache();
        itemsDAO.cleanCache();
        purchasedItemsDAO.cleanCache();
        shelvesHandlerDAO.cleanCache();
    }
    public int getBack0Front1(String branchAddress, int shelf) {
        return shelvesHandlerDAO.getBack0Front1(branchAddress, shelf);
    }

    public void addShelf(String branchAddress, int shelfCounter, boolean shelfInBackRoom) {
        int back0front1 = shelfInBackRoom ? 0 : 1;
        shelvesHandlerDAO.insert(branchAddress, shelfCounter, back0front1);
    }

    public List<Integer> getAllBackShelves(String branchAddress) {
        return shelvesHandlerDAO.getAllShelvesInBackRoom(branchAddress);
    }

    public List<Integer> getAllFrontShelves(String branchAddress) {
        return shelvesHandlerDAO.getAllShelvesInFrontRoom(branchAddress);
    }

    public int getShelvesCounter(String branchAddress) {
        return shelvesHandlerDAO.getShelvesCounter(branchAddress);
    }

    public int getItemCounter(String branchAddress) {
        return itemsDAO.selectItemsCounter(branchAddress);
    }

    public void addItemToCategory(String branchAddress, String categoryID, int idCounter) {
        itemsByCategoryDAO.insert(branchAddress, categoryID,idCounter);
    }

    public void removeCategory(String branchAddress, String catID) {
        categoriesDAO.deleteRecord(branchAddress, catID);
    }


    public void removeFromMap(String branchAddress, String catID) {
        categoriesDAO.removeFromMap(branchAddress, catID);
    }

    public void removeDiscount(String branchAddress, int itemID, LocalDate fromDate) {
        discountsDAO.removeDis(branchAddress, itemID, fromDate.toString());
    }

    private static class DALFacadeHolder{
        private static DALFacade instance = new DALFacade();
    }

    public static void clearSingletonForTests() {
        DALFacadeHolder.instance = new DALFacade();
    }

    public static DALFacade getInstance() {
        return DALFacadeHolder.instance;
    }

    public ItemPrice getItemPrice(String branchAddress, int itemID) {
        return itemPriceDAO.getItemPrice(branchAddress, itemID);
    }

    public Category getCategory(String branchAddress, String categoryID) {
        return categoriesDAO.getCategory(branchAddress, categoryID);
    }

    public ItemLocation getItemLocation(String branchAddress, int itemID) {
        return itemLocationDAO.getItemLocation(branchAddress, itemID);
    }

    public ItemQuantity getItemQuantity(String branchAddress, int itemID) {
        return itemQuantityDAO.getItemQuantity(branchAddress, itemID);
    }

    public List<Category> getSubCatList(String branchAddress, String categoryId) {
       return categoriesDAO.getSubCat(branchAddress, categoryId);
    }
    public List<String> getIDSubCat(String branchAddress, String categoryID) {
        return categoriesDAO.getIDSubCat(branchAddress, categoryID);
    }

    public List<Integer> getItemIDList(String branchAddress, String categoryID) {
        return categoriesDAO.getItemIDList(branchAddress, categoryID);
    }


    public void addCategory(String branchAddress, String catID, String catName, String catFatherID) {
        categoriesDAO.insert(branchAddress, catID, catName, catFatherID);
    }

    public Integer getCategoryCounter(String branchAddress, String categoryID) {
        return categoriesDAO.getCounter(branchAddress, categoryID);
    }

    public void setCategoryCounter(String branchAddress, String catID, int counter) {
        categoriesDAO.updateCounter(branchAddress, catID, counter);
    }


    public void setCategoryName(String branchAddress, String catID, String newName) {
        categoriesDAO.updateName(branchAddress, catID,newName);
    }

    public void setCategoryFather(String branchAddress, String catID, String fatherID) {
        categoriesDAO.updateFather(branchAddress, catID,fatherID);
    }

    public void removeSubCategory(String branchAddress, String subCatID) {
        categoriesDAO.removeSubCategory(branchAddress, subCatID);
    }

    public void removeItemFromCategory(String branchAddress, String catID, int itemID) {
        itemsByCategoryDAO.deleteRecord(branchAddress, catID,itemID);
    }

    public Item getItem(String branchAddress, int itemID) {return itemsDAO.getItem(branchAddress, itemID);}

    public Category getFatherCategory(String branchAddress, String catID) {
        String fatherID = categoriesDAO.getFatherID(branchAddress, catID);
        return categoriesDAO.getCategory(branchAddress, fatherID);
    }

    public List<Discount> getAllItemsDiscounts(String branchAddress,  int itemID) {
        return discountsDAO.getAllItemsDiscounts(branchAddress, itemID);
    }

    public void changeItemAppointedShelvesByRoom(String branchAddress, boolean fromBackRoom, List<Integer> shelves, int itemID) {
        try {
            ItemLocation location = itemLocationDAO.getItemLocation(branchAddress, itemID);
            if (fromBackRoom) {
                for (Integer shelfID : location.getShelvesInBackRoom())
                    itemLocationDAO.deleteRecord(branchAddress, itemID, shelfID);
            } else {
                for (Integer shelfID : location.getShelvesInFrontRoom())
                    itemLocationDAO.deleteRecord(branchAddress, itemID, shelfID);
            }
            for (Integer shelfID : shelves) {
                if(fromBackRoom)
                    itemLocationDAO.insert(branchAddress, itemID, shelfID);
                else
                    itemLocationDAO.insert(branchAddress, itemID, shelfID);
            }
        }
        catch (Exception e){
            throw new IllegalArgumentException("cant insert items shelves");
        }
    }

    public void addItemQuantity(String branchAddress, int itemID, int minimalQuantity, int fullQuantity) {
        itemQuantityDAO.insert(branchAddress, itemID,0,0,0,minimalQuantity,fullQuantity);
    }

    public void setMinimalQuantity(String branchAddress, int minimalQuantity, int itemID) {
        itemQuantityDAO.updateMinimalQuantity(branchAddress, itemID,minimalQuantity);
    }

    public void setFullQuantity(String branchAddress, int fullQuantity, int itemID) {
        itemQuantityDAO.updateFullQuantity(branchAddress, itemID, fullQuantity);
    }

    public void setAmountInBackRoom(String branchAddress, int amountInBackRoom, int itemID) {
        itemQuantityDAO.updateAmountInBackRoom(branchAddress, itemID,amountInBackRoom);
    }

    public void setAmountInFrontRoom(String branchAddress, int amountInBackRoom, int itemID) {
        itemQuantityDAO.updateAmountInFrontRoom(branchAddress, itemID,amountInBackRoom);
    }

    public void setTotalAmount(String branchAddress, int totalAmount, int itemID) {
        itemQuantityDAO.updateTotalQuantity(branchAddress, itemID,totalAmount);
    }

    public void addItemPrice(String branchAddress, int itemID, double currPrice) {
        itemPriceDAO.insert(branchAddress, itemID, currPrice);
    }

    public void addItem(String branchAddress, int idCounter, String name, String categoryID, String manufacture) {
        itemsDAO.insert(branchAddress, idCounter, name, categoryID, manufacture);
    }

    public Discount addDiscount(String branchAddress, int itemID, String disName, String disFromDate, String disToDate, double discountFare){
        return discountsDAO.insert(branchAddress, itemID, disName, disFromDate, disToDate, discountFare);
    }

    public void setCurrentPrice(String branchAddress, double currentPrice, int itemID) {
        itemPriceDAO.updateCurrPrice(branchAddress, itemID,currentPrice);
    }

    public void setDiscountName(String branchAddress, int itemID, String newName, String fromDate) {
        discountsDAO.updateDiscountName(branchAddress, itemID,fromDate,newName);
    }

    public void setItemName(String branchAddress, int itemID, String name) {
        itemsDAO.updateItemName(branchAddress, itemID,name);
    }

    public void setItemManufacture(String branchAddress, int itemID, String manufacture) {
        itemsDAO.updateManufacture(branchAddress, itemID,manufacture);
    }

    public void addItemLocation(String branchAddress, int itemID, List<Integer> backShelves, List<Integer> frontShelves) {
        for(Integer backShelf : backShelves){
            itemLocationDAO.insert(branchAddress, itemID, backShelf);
        }
        for(Integer frontShelf : frontShelves){
            itemLocationDAO.insert(branchAddress, itemID, frontShelf);
        }
    }
//////////////////////////////////////////////////////////////////////////////////report
    public List<DamagedItem> getAllDamagedRecords(String branchAddress) {
        return damagedItemsDAO.getAllItems(branchAddress);
    }

    public List<DamagedItem> getDamagedRecordsByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) {
        return damagedItemsDAO.getAllRecordsByDate(branchAddress, sinceWhen,untilWhen);
    }

    public List<DamagedItem> getDamagedRecordsByID(String branchAddress, int id) {
        return damagedItemsDAO.getAllRecordsByID(branchAddress, id);
    }

    public List<DamagedItem> getOnlyExpiredRecords(String branchAddress) {
        return damagedItemsDAO.getOnlyExpiredRecords(branchAddress);
    }

    public List<DamagedItem> getOnlyDamageRecords(String branchAddress) {
        return damagedItemsDAO.getOnlyDamageRecords(branchAddress);
    }

    public List<DamagedItem> getOnlyDamageRecordsByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) {
        return damagedItemsDAO.getOnlyDamageRecordsByDate(branchAddress, sinceWhen,untilWhen);
    }

    public List<DamagedItem> getOnlyExpiredRecordsByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) {
        return damagedItemsDAO.getOnlyExpiredRecordsByDate(branchAddress, sinceWhen,untilWhen);
    }

    public List<DamagedItem> getOnlyDamageRecordsByID(String branchAddress, int itemID) {
        return damagedItemsDAO.getOnlyDamageRecordsByID(branchAddress, itemID);
    }

    public List<DamagedItem> getOnlyExpiredRecordsByID(String branchAddress, int itemID) {
        return damagedItemsDAO.getOnlyExpiredRecordsByID(branchAddress, itemID);
    }

    public void addDamagedRecord(String branchAddress, int itemID, String timeFound, int quantityFound, String expiredOrDamaged, int back0Front1) {
        damagedItemsDAO.insert(branchAddress, itemID, timeFound, quantityFound, expiredOrDamaged, back0Front1);
    }

    public List<PurchasedItem> getAllPurchaseRecords(String branchAddress) {
        return purchasedItemsDAO.getAllRecords(branchAddress);
    }

    public List<PurchasedItem> getPurchaseRecordsByID(String branchAddress, int id) {
        return purchasedItemsDAO.getRecordsByID(branchAddress, id);
    }

    public List<PurchasedItem> getPurchaseRecordsByBusinessNumber(String branchAddress, int supplierBusiness) {
        return purchasedItemsDAO.getRecordsByBusinessNumber(branchAddress, supplierBusiness);
    }

    public void addPurchaseRecord(String branchAddress, int desiredItemID, int orderID, String timePurchased, int supplierBisNumber, int amount, double checkedPrice, double checkedDiscount) {
        purchasedItemsDAO.insert(branchAddress, desiredItemID, orderID, timePurchased, supplierBisNumber, amount, checkedPrice, checkedDiscount);
    }

    public List<PurchasedItem> getPurchaseRecordsByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) {
        return purchasedItemsDAO.getRecordsByDate(branchAddress, sinceWhen,untilWhen);
    }

    public List <InShortageItem> getShortageList(String branchAddress) {
        return inShortageItemsDAO.getAllItems(branchAddress);
    }

    public void addShortage(String branchAddress, int itemID) {
        inShortageItemsDAO.insert(branchAddress, itemID);
    }

    public void removeFromShortage(String branchAddress, int itemId) {
        inShortageItemsDAO.deleteRecord(branchAddress, itemId);
    }

    public HashMap<Integer, Item> getAllItemsMap(String branchAddress){
        HashMap<Integer, Item> result = new HashMap<>();
        List<Item> itemList = itemsDAO.getAllItems(branchAddress);
        for( Item item : itemList)
            result.put(item.getItemID(), item);
        return result;
    }
    //////////////////////////////////////////////////////////////////////////////////End report
}
