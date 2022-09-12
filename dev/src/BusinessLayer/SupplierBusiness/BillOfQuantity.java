package BusinessLayer.SupplierBusiness;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BillOfQuantity {
    private final int supplierBN;
    private final Map<String, Map<Integer, SupplierItem>> items;
    private Instant timeStamp;

    public BillOfQuantity(int supplierBN) {
        this.supplierBN = supplierBN;
        this.items = new ConcurrentHashMap<>();
        timeStamp = Instant.now();
    }

    public Map<Integer, SupplierItem> getItems(String branchAddress) {
        updateTimeStamp();
        items.putIfAbsent(branchAddress, new HashMap<>());
        return items.get(branchAddress);
    }

    public double getItemPrice(String branchAddress, int itemId) {
        updateTimeStamp();
        checkItemExists(branchAddress, itemId);
        return items.get(branchAddress).get(itemId).getPrice();
    }
    public double getItemPriceAccordingToAmount(String branchAddress, int itemId, int amount) {
        updateTimeStamp();
        double price = getItemPrice(branchAddress, itemId);
        price = price - getDiscountForOrder(branchAddress, itemId, amount)*price/100.0;
        return price;
    }

    public void updateItemPrice(String branchAddress, int itemId, double price){
        updateTimeStamp();
        checkItemExists(branchAddress, itemId);
        if(price<=0)
            throw new IllegalArgumentException("Invalid price!");
        items.get(branchAddress).get(itemId).setPrice(price);
    }

    public Double getDiscountForOrder(String branchAddress, int itemId, int amount) {
        updateTimeStamp();
        checkItemExists(branchAddress, itemId);
        return items.get(branchAddress).get(itemId).getDiscountForOrder(amount);
    }

    public void addItemDiscountAccordingToAmount(String branchAddress, int itemId, int amount, double discount) {
        updateTimeStamp();
        checkItemExists(branchAddress, itemId);
        items.get(branchAddress).get(itemId).addItemDiscountAccordingToAmount(amount, discount);
    }

    public void updateItemDiscountAccordingToAmount(String branchAddress, int itemId, int amount, double newDiscount) {
        updateTimeStamp();
        checkItemExists(branchAddress, itemId);
        items.get(branchAddress).get(itemId).updateItemDiscountAccordingToAmount(amount, newDiscount);
    }

    public void removeItemDiscountAccordingToAmount(String branchAddress, int itemId, int amount) {
        updateTimeStamp();
        checkItemExists(branchAddress, itemId);
        items.get(branchAddress).get(itemId).removeItemDiscountAccordingToAmount(amount);
    }

    public void updateSupplierCatalog(String branchAddress, int storeCatalog, int newSupplierCatalog) {
        updateTimeStamp();
        checkItemExists(branchAddress, storeCatalog);
        if(newSupplierCatalog<=0)
            throw new IllegalArgumentException("Invalid supplier catalog!");
        items.get(branchAddress).get(storeCatalog).setSupplierCatalog(newSupplierCatalog);
    }

    public String getItemName(String branchAddress, int storeCatalog) {
        updateTimeStamp();
        checkItemExists(branchAddress, storeCatalog);
        return items.get(branchAddress).get(storeCatalog).getItemName();
    }

    public void updateItemName(String branchAddress, int storeCatalog, String newName) {
        updateTimeStamp();
        checkItemExists(branchAddress, storeCatalog);
        items.get(branchAddress).get(storeCatalog).setItemName(newName);
    }

    public void addItem(String branchAddress, SupplierItem supplierItem) {
        updateTimeStamp();
        items.putIfAbsent(branchAddress, new HashMap<>());
        items.get(branchAddress).put(supplierItem.getCatalogNumber(), supplierItem);
    }

    public void removeItem(String branchAddress, int storeCatalog) {
        updateTimeStamp();
        checkItemExists(branchAddress, storeCatalog);
        items.get(branchAddress).remove(storeCatalog);
    }

    public Map<Integer, Double> getItemDiscounts(String branchAddress, int catalogNumber) {
        updateTimeStamp();
        checkItemExists(branchAddress, catalogNumber);
        return items.get(branchAddress).get(catalogNumber).getItemDiscounts();
    }

    public void checkItemExists(String branchAddress, int itemId){
        updateTimeStamp();
        if(!items.containsKey(branchAddress) || !items.get(branchAddress).containsKey(itemId))
            throw new IllegalArgumentException("Item with the item id: "+ itemId+" not exists");
    }

    public boolean checkItemExists(int itemId, String branchAddress){
        updateTimeStamp();
        return items.containsKey(branchAddress) && items.get(branchAddress).containsKey(itemId);
    }

    public boolean checkItemDiscountAccordingToAmount(String branchAddress, int catalogNumber, int amount) {
        updateTimeStamp();
        checkItemExists(branchAddress, catalogNumber);
        return  items.get(branchAddress).get(catalogNumber).isAmountExists(amount);
    }

    public boolean equals(String branchAddress, BillOfQuantity other){
        updateTimeStamp();
        return other.getItems(branchAddress).equals(items.get(branchAddress));
    }

    public SupplierItem getItem(String branchAddress, int catalogNumber) {
        updateTimeStamp();
        checkItemExists(branchAddress, catalogNumber);
        return items.get(branchAddress).get(catalogNumber);
    }

    public int getSupplierBN() {
        updateTimeStamp();
        return supplierBN;
    }

    private void updateTimeStamp() { timeStamp = Instant.now();}

    public boolean shouldCleanCache() {
        Duration duration = Duration.between( timeStamp , Instant.now());
        Duration limit = Duration.ofMinutes(5);
        return (duration.compareTo( limit ) > 0);
    }
}