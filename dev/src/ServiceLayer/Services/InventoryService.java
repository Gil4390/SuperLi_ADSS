package ServiceLayer.Services;

import BusinessLayer.ApplicationFacade;
import BusinessLayer.InventoryBusiness.*;
import BusinessLayer.InventoryBusiness.ReportItems.*;
import ServiceLayer.Objects.InventoryObjects.*;
import ServiceLayer.Responses.*;

import java.time.LocalDate;
import java.util.*;

public class InventoryService {
    private final ApplicationFacade applicationFacade;

    public InventoryService(ApplicationFacade applicationFacade) {
        this.applicationFacade = applicationFacade;
    }

    public ResponseT<List<FInventoryItem>> getInventoryReport(String branchAddress) {
        try {
            List<InventoryItem> list = applicationFacade.makeInventoryReport(branchAddress);
            List<FInventoryItem> fList = new ArrayList<>();
            for(InventoryItem item : list)
                fList.add(new FInventoryItem(item));
            return new ResponseT<>(fList);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<FInventoryItem>> getInventoryReportByCategory(String branchAddress, String categoryID) {
        try {
            List<InventoryItem> list = applicationFacade.makeInventoryReportByCategory(branchAddress, categoryID);
            List<FInventoryItem> fList = new ArrayList<>();
            for(InventoryItem item : list)
                fList.add(new FInventoryItem(item));
            return new ResponseT<>(fList);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<FInShortageItem>> getShortageReport(String branchAddress) {
        try {
            List<InShortageItem> list = applicationFacade.makeShortageReport(branchAddress);
            List<FInShortageItem> fList = new ArrayList<>();
            for(InShortageItem item : list)
                fList.add(new FInShortageItem(item));
            return new ResponseT<>(fList);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<FDamagedItem>> getDamageReport(String branchAddress) {
        try {
            List<DamagedItem> list = applicationFacade.makeDamageReport(branchAddress);
            List<FDamagedItem> fList = new ArrayList<>();
            for(DamagedItem item : list)
                fList.add(new FDamagedItem(item));
            return new ResponseT<>(fList);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<FDamagedItem>> getDamageReportByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) {
        try {
            List<DamagedItem> list = applicationFacade.makeDamageReportByDate(branchAddress, sinceWhen, untilWhen);
            List<FDamagedItem> fList = new ArrayList<>();
            for(DamagedItem item : list)
                fList.add(new FDamagedItem(item));
            return new ResponseT<>(fList);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<FDamagedItem>> getDamageReportByItemID(String branchAddress, int itemID) {
        try {
            List<DamagedItem> list = applicationFacade.makeDamageReportByItemId(branchAddress, itemID);
            List<FDamagedItem> fList = new ArrayList<>();
            for(DamagedItem item : list)
                fList.add(new FDamagedItem(item));
            return new ResponseT<>(fList);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<FDamagedItem>> getOnlyDamageReport(String branchAddress) {
        try {
            List<DamagedItem> list = applicationFacade.makeOnlyDamageReport(branchAddress);
            List<FDamagedItem> fList = new ArrayList<>();
            for(DamagedItem item : list)
                fList.add(new FDamagedItem(item));
            return new ResponseT<>(fList);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<FDamagedItem>> getOnlyExpiredReport(String branchAddress) {
        try {
            List<DamagedItem> list = applicationFacade.makeOnlyExpiredReport(branchAddress);
            List<FDamagedItem> fList = new ArrayList<>();
            for(DamagedItem item : list)
                fList.add(new FDamagedItem(item));
            return new ResponseT<>(fList);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<FDamagedItem>> getOnlyDamageReportByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) {
        try {
            List<DamagedItem> list = applicationFacade.makeOnlyDamageReportByDate(branchAddress, sinceWhen, untilWhen);
            List<FDamagedItem> fList = new ArrayList<>();
            for(DamagedItem item : list)
                fList.add(new FDamagedItem(item));
            return new ResponseT<>(fList);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<FDamagedItem>> getOnlyExpiredReportByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) {
        try {
            List<DamagedItem> list = applicationFacade.makeOnlyExpiredReportByDate(branchAddress, sinceWhen, untilWhen);
            List<FDamagedItem> fList = new ArrayList<>();
            for(DamagedItem item : list)
                fList.add(new FDamagedItem(item));
            return new ResponseT<>(fList);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<FDamagedItem>> getOnlyDamageReportByItemID(String branchAddress, int itemID) {
        try {
            List<DamagedItem> list = applicationFacade.makeOnlyDamageReportByItemId(branchAddress, itemID);
            List<FDamagedItem> fList = new ArrayList<>();
            for(DamagedItem item : list)
                fList.add(new FDamagedItem(item));
            return new ResponseT<>(fList);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<FDamagedItem>> getOnlyExpiredReportByItemID(String branchAddress, int itemID) {
        try {
            List<DamagedItem> list = applicationFacade.makeOnlyExpiredReportByItemId(branchAddress, itemID);
            List<FDamagedItem> fList = new ArrayList<>();
            for(DamagedItem item : list)
                fList.add(new FDamagedItem(item));
            return new ResponseT<>(fList);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<FPurchasedItem>> getPurchaseReport(String branchAddress) {
        try {
            List<PurchasedItem> list = applicationFacade.makePurchaseReport(branchAddress);
            List<FPurchasedItem> fList = new ArrayList<>();
            for(PurchasedItem item : list)
                fList.add(new FPurchasedItem(item));
            return new ResponseT<>(fList);
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<FPurchasedItem>> getPurchaseReportByItemID(String branchAddress, int itemID) {
        try {
            List<PurchasedItem> list = applicationFacade.makePurchaseReportByItemId(branchAddress, itemID);
            List<FPurchasedItem> fList = new ArrayList<>();
            for(PurchasedItem item : list)
                fList.add(new FPurchasedItem(item));
            return new ResponseT<>(fList);
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<FPurchasedItem>> getPurchaseReportByBusinessNumber(String branchAddress, int supplierNumber) {
        try {
            List<PurchasedItem> list = applicationFacade.makePurchaseReportByBusinessNumber(branchAddress, supplierNumber);
            List<FPurchasedItem> fList = new ArrayList<>();
            for(PurchasedItem item : list)
                fList.add(new FPurchasedItem(item));
            return new ResponseT<>(fList);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<FPurchasedItem>> getPurchaseReportByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) {
        try {
            List<PurchasedItem> list = applicationFacade.makePurchaseReportByDate(branchAddress, sinceWhen, untilWhen);
            List<FPurchasedItem> fList = new ArrayList<>();
            for(PurchasedItem item : list)
                fList.add(new FPurchasedItem(item));
            return new ResponseT<>(fList);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    //AddRecords-------------------------------
    public Response addPurchaseRecord(String branchAddress, int desiredItemID, int orderID, int checkedSupplier, int checkedAmount, double checkedPrice, double checkedDiscount) {
        try {
            applicationFacade.addPurchase(branchAddress, desiredItemID,orderID,checkedSupplier,checkedAmount,checkedPrice,checkedDiscount);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response addDamagedRecord(String branchAddress, int desiredItemID, int amount, FDamageReason reason, int back0Front1) {
        try {
            applicationFacade.addDamaged(branchAddress, desiredItemID,amount,DamageReason.valueOf(reason.toString()),back0Front1);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    //Items----------------------------------------------------------------------------------
    public ResponseT<List<FItem>> getAllItemsInCategory(String branchAddress, String categoryId) {
        try {
            List<Item> list = applicationFacade.getAllItemsByCategory(branchAddress, categoryId);
            List<FItem> fList = new ArrayList<>();
            for(Item item : list)
                fList.add(new FItem(item));
            return new ResponseT<>(fList);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public Response giveDiscountToCategory(String branchAddress, String categoryID, FDiscount fDiscount) {
        try {
            applicationFacade.giveDiscountToCategory(branchAddress, categoryID, new Discount(fDiscount.getName(), fDiscount.getFromDate(), fDiscount.getToDate(), fDiscount.getDiscountFare()));
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public ResponseT<FItem> getItem(String branchAddress, int itemID) {
        try {
            return new ResponseT<>(new FItem(applicationFacade.getItem(branchAddress, itemID)));
        } catch (Exception e) {
            return new ResponseT<>("didn't find the item");
        }
    }

    public ResponseT<FItem> addItem(String branchAddress, String name, String catID, double price, int minimalQuantity, int fullQuantity, String manufacture, List<Integer> backShelves, List<Integer> frontShelves) {
        try {
            Item item = applicationFacade.addStorageItem(branchAddress, name, catID, price,
                    minimalQuantity, fullQuantity, manufacture, backShelves, frontShelves);
            FItem fItem = new FItem(item);
            return new ResponseT(fItem);
        } catch (Exception e) {
            return new ResponseT(e.getMessage());
        }
    }

    public Response removeQuantityFromItem(String branchAddress, int itemId, int quantity, boolean fromBackRoom) {
        try {
            applicationFacade.removeQuantityFromItem(branchAddress, itemId, quantity, fromBackRoom);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response addQuantityToItem(String branchAddress, int itemId, int quantity, boolean fromBackRoom) {
        try {
            applicationFacade.addQuantityToItem(branchAddress, itemId, quantity, fromBackRoom);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    //Item Discounts and Quantities------------------------------------------------------------------------------------
    public Response addDiscount(String branchAddress, int itemId, String name, LocalDate fromDate, LocalDate toDate, double discountFare) {
        try {
            applicationFacade.giveDiscountToItem(branchAddress, itemId, new Discount(name, fromDate, toDate, discountFare));
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeDiscount(String branchAddress, int itemId, LocalDate fromDate) {
        try {
            applicationFacade.getItem(branchAddress, itemId).getPrice().removeDiscount(fromDate,itemId);
            return new Response();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public Response moveItemsBetweenRooms(String branchAddress, int itemId, boolean fromBackRoom, int quantity) {
        try{
            applicationFacade.getItem(branchAddress, itemId).getQuantity().moveItemsBetweenRooms(fromBackRoom, quantity, itemId);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    //Category----------------------------------------------------------------------------------
    public ResponseT<FCategory> getCategory(String branchAddress, String categoryID) {
        try {
            Category category = applicationFacade.getCategoryTree(branchAddress).getCategory(branchAddress, categoryID);
            FCategory fCategory = new FCategory(category);
            return new ResponseT<> (fCategory);
        }
        catch (Exception e){
            return new ResponseT<>(""+e.getMessage());
        }
    }

    public Response moveCategory(String branchAddress, FCategory newFatherCat, FCategory deliveredCat) {
        try {
            Category newFather = applicationFacade.getCategoryTree(branchAddress).getCategory(branchAddress, newFatherCat.getCatID());
            Category deliveredCategory = applicationFacade.getCategoryTree(branchAddress).getCategory(branchAddress, deliveredCat.getCatID());
            applicationFacade.getCategoryTree(branchAddress).moveCategory(newFather, deliveredCategory);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response addCategory(String branchAddress, String newFatherCat, String catName) {
        try {
            Category newFather = applicationFacade.getCategoryTree(branchAddress).getCategory(branchAddress, newFatherCat);
            applicationFacade.getCategoryTree(branchAddress).addCategory(newFather, catName);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public ResponseT<List<FCategory>> getAllCategorySubCat(String branchAddress, String categoryID) {
        try {
            List<FCategory> fCategories = new LinkedList<>();
            Category category = applicationFacade.getCategoryTree(branchAddress).getCategory(branchAddress, categoryID);
            for(Category cat : category.getSubCatList())
                fCategories.add(new FCategory(cat));
            return new ResponseT<>(fCategories);
        }
        catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<FCategory> getCategoryFather(String branchAddress, String categoryID) {
        try {
            return new ResponseT<>(new FCategory(applicationFacade.getCategoryTree(branchAddress).getCategory(branchAddress, categoryID)));
        }
        catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public Response setCategoryName(String branchAddress, String categoryID, String newName) {
        try{
            applicationFacade.getCategoryTree(branchAddress).getCategory(branchAddress, categoryID).setName(newName);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
    // ItemLocation and Shelves---------------------------------------------------------------
    public ResponseT<List<Integer>> getAllBackShelves(String branchAddress) {
        try {
            return new ResponseT<>(applicationFacade.getBackRoomShelves(branchAddress));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }
    public ResponseT<List<Integer>> getAllFrontShelves(String branchAddress) {
        try {
            return new ResponseT<>(applicationFacade.getFrontRoomShelves(branchAddress));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }
    public Response addShelf(String branchAddress, boolean isInBackShelf) {
        try {
            applicationFacade.addShelf(branchAddress, isInBackShelf);
            return new Response();
        } catch (Exception e) {
            return new Response (e.getMessage());
        }
    }
    public ResponseT<List<Integer>> getAllFrontShelvesByItem(String branchAddress, int id) {
        try{
            return new ResponseT<>(applicationFacade.getItem(branchAddress, id).getLocation().getShelvesInFrontRoom());
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }
    public ResponseT<List<Integer>> getAllBackShelvesByItem(String branchAddress, int id) {
        try{
            return new ResponseT<>(applicationFacade.getItem(branchAddress, id).getLocation().getShelvesInBackRoom());
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }
    public Response changeItemAppointedShelvesByRoom(String branchAddress, int id, boolean inBackRoom, int [] idShelvesList) {
        try{
            applicationFacade.getItem(branchAddress, id).getLocation().changeItemAppointedShelvesByRoom(inBackRoom,idShelvesList, id);
            return new Response();
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public Response changeItemPrice(String branchAddress, int itemID, int checkedAmount) {
        try{
            applicationFacade.getItem(branchAddress, itemID).getPrice().setCurrentPrice(checkedAmount, itemID);
            return new Response();
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public Response changeMinimalQuantity(String branchAddress, int itemID, int checkedQuantity) {
        try{
            //applicationFacade.getItem(itemID).getQuantity().setMinimalQuantity(checkedQuantity);
            applicationFacade.setMinimalQuantity(branchAddress, itemID, checkedQuantity);
            return new Response();
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public Response changeFullQuantity(String branchAddress, int itemID, int checkedQuantity) {
        try{
            //applicationFacade.getItem(itemID).getQuantity().setMinimalQuantity(checkedQuantity);
            applicationFacade.setFullQuantity(branchAddress, itemID, checkedQuantity);
            return new Response();
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public Response removeCategory(String branchAddress, String categoryID) {
        applicationFacade.getCategoryTree(branchAddress).removeCategory(branchAddress, categoryID);
        return new Response();
    }

    public ItemController getItemController() {
        return applicationFacade.getItemController();
    }
    //-----------------------------------------------------------------------------------------
}
