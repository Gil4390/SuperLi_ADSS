package BusinessLayer.InventoryBusiness;
import BusinessLayer.InventoryBusiness.ReportItems.*;
import DataAccessLayer.InventoryDAL.DALFacade;
import BusinessLayer.SupplierBusiness.OrderProduct;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.*;

public class ReportsMaker {
    private final ItemController itemController;

    public ReportsMaker(ItemController itemController){
        this.itemController = itemController;
    }

    public List<InventoryItem> makeInventoryReport(String branchAddress){
        List<Item>itemList = itemController.getAllItems(branchAddress);
        ArrayList<InventoryItem> inventoryReport = new ArrayList<>();
        for(Item item : itemList)
            inventoryReport.add(makeInvItem(branchAddress, item));
        return inventoryReport;
    }

    public List<InventoryItem> makeInventoryReportByCategory(String branchAddress, String categoryID){
        Category category = itemController.getCategoryTree(branchAddress).getCategory(branchAddress, categoryID);
        ArrayList<InventoryItem> inventoryReport = new ArrayList<>();
        for (int id : category.getItemIDList()){
            Item item = itemController.getItem(branchAddress, id);
            inventoryReport.add(makeInvItem(branchAddress, item));
        }
        return inventoryReport;
    }

    private InventoryItem makeInvItem(String branchAddress, Item item){
        List<Integer> frontShelfLocation = item.getLocation().getShelvesInFrontRoom();
        List<Integer> backShelfLocation = item.getLocation().getShelvesInBackRoom();
        return new InventoryItem(branchAddress, item.getItemID(),frontShelfLocation,item.getQuantity().getAmountInFrontRoom()
                ,backShelfLocation,item.getQuantity().getAmountInBackRoom(),item.getQuantity().getTotalAmount()
                ,item.getPrice().getCurrentPrice(),item.getPrice().getCalculatedPrice());
    }

    public List<InShortageItem> makeShortageReport(String branchAddress){
        return itemController.getShortageList(branchAddress);
    }

    public List<DamagedItem> makeDamageReport(String branchAddress) {
        return DALFacade.getInstance().getAllDamagedRecords(branchAddress);
    }

    public List<DamagedItem> makeDamageReportByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen){
        return  DALFacade.getInstance().getDamagedRecordsByDate(branchAddress, sinceWhen, untilWhen);
    }

    public List<DamagedItem> makeDamageReportByItemId(String branchAddress, int id){
        return  DALFacade.getInstance().getDamagedRecordsByID(branchAddress, id);
    }

    public List<DamagedItem> makeOnlyExpiredReport(String branchAddress) {
        return  DALFacade.getInstance().getOnlyExpiredRecords(branchAddress);
    }

    public List<DamagedItem> makeOnlyDamageReport(String branchAddress) {
        return  DALFacade.getInstance().getOnlyDamageRecords(branchAddress);
    }

    public List<DamagedItem> makeOnlyDamageReportByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) {
        return  DALFacade.getInstance().getOnlyDamageRecordsByDate(branchAddress, sinceWhen, untilWhen);
    }

    public List<DamagedItem> makeOnlyExpiredReportByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) {
        return  DALFacade.getInstance().getOnlyExpiredRecordsByDate(branchAddress, sinceWhen, untilWhen);
    }

    public List<DamagedItem> makeOnlyDamageReportByItemId(String branchAddress, int itemID) {
        return  DALFacade.getInstance().getOnlyDamageRecordsByID(branchAddress, itemID);
    }

    public List<DamagedItem> makeOnlyExpiredReportByItemId(String branchAddress, int itemID) {
        return  DALFacade.getInstance().getOnlyExpiredRecordsByID(branchAddress, itemID);
    }

    public List<PurchasedItem> makePurchaseReport(String branchAddress) {
        return DALFacade.getInstance().getAllPurchaseRecords(branchAddress);
    }

    public List<PurchasedItem> makePurchaseReportByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen){
        return DALFacade.getInstance().getPurchaseRecordsByDate(branchAddress, sinceWhen, untilWhen);
    }

    public List<PurchasedItem> makePurchaseReportByItemId(String branchAddress, int id){
        return DALFacade.getInstance().getPurchaseRecordsByID(branchAddress, id);
    }

    public List<PurchasedItem> makePurchaseReportByBusinessNumber(String branchAddress, int supplierBusiness){
        return DALFacade.getInstance().getPurchaseRecordsByBusinessNumber(branchAddress, supplierBusiness);
    }

    public void addPurchase(String branchAddress, int desiredItemID, int orderID, int supplierBisNumber, int amount, double checkedPrice, double checkedDiscount){
        DALFacade.getInstance().addPurchaseRecord(branchAddress, desiredItemID, orderID, LocalDate.now().toString() ,supplierBisNumber,amount,checkedPrice,checkedDiscount);
        Item item = itemController.getItem(branchAddress, desiredItemID);
        item.getQuantity().addAmountToBack(amount, desiredItemID);
        if(!item.getQuantity().isInShortage() && itemController.getShortageList(branchAddress).contains(item.getItemID())) //remove shortageList
            DALFacade.getInstance().removeFromShortage(branchAddress, desiredItemID);
    }

    public void addPurchase(String branchAddress, List<Pair<Integer, OrderProduct>> orderProducts){
        for(Pair<Integer, OrderProduct> orderProductAndSupplierId : orderProducts){
            addPurchase(branchAddress, orderProductAndSupplierId.getValue().getProductId(), orderProductAndSupplierId.getValue().getOrderId(), orderProductAndSupplierId.getKey(), orderProductAndSupplierId.getValue().getAmount() - orderProductAndSupplierId.getValue().getUnSuppliedAmount(), orderProductAndSupplierId.getValue().getSingleItemPrice(), orderProductAndSupplierId.getValue().getDiscount());
        }
    }


    public void addDamaged(String branchAddress, int desiredItemID, int amount, DamageReason reason, int back0Front1){
        DALFacade.getInstance().addDamagedRecord(branchAddress, desiredItemID, LocalDate.now().toString(),amount,reason.toString(),back0Front1);
        Item item = itemController.getItem(branchAddress, desiredItemID);
        if(back0Front1==1)
            item.getQuantity().removeAmountFromFront(amount,desiredItemID);
        else
            item.getQuantity().removeAmountFromBack(amount,desiredItemID);
        if(item.getQuantity().isInShortage() && !itemController.getShortageList(branchAddress).contains(item.getItemID())) //get into shortageList
            DALFacade.getInstance().addShortage(branchAddress, desiredItemID);
    }

    public List<Pair<Integer, Integer>> makeShortageOrder(String branchAddress) {
        List<InShortageItem> itemsInShortage = itemController.getShortageList(branchAddress);
        List<Pair<Integer, Integer>> itemsToOrder = new ArrayList<>();
        for (InShortageItem itemInShort : itemsInShortage) {
            itemsToOrder.add(new Pair<>(itemInShort.getItemID(), itemInShort.getFullQuantity() - itemInShort.getCurrentQuantity()));
        }
        return itemsToOrder;
    }
}
