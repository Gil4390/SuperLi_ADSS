package BusinessLayer.DeliveryModule.Objects;

import java.util.*;


public class SiteDoc extends TimeStampChecker {
    private final int deliveryId;
    private final String siteAddress;
    private final int documentId;
    private final Map<Integer, Integer> loadItems;
    private final Map<Integer, Integer> unloadItems;
    private final List<problemSiteVisit> logList;
    private int truckWeight;
    String contactNumber;
    String contactName;
    private int locationInAddressList;

    public SiteDoc(int deliveryId, String siteAddress, int documentId, Map<Integer, Integer> loadItems, Map<Integer, Integer> unloadItems,
                   List<problemSiteVisit> logList, String contactNumber, String contactName, int locationInAddressList){
            this.deliveryId = deliveryId;
            this.siteAddress = siteAddress;
            this.documentId = documentId;
            this.loadItems = loadItems;
            this.unloadItems = unloadItems;
            this.logList = logList;
            truckWeight = 0;
            this.contactNumber = contactNumber;
            this.contactName = contactName;
            this.locationInAddressList=locationInAddressList;
    }

    public SiteDoc(int deliveryId, String siteAddress, int documentId, Map<Integer, Integer> loadItems,
                   Map<Integer, Integer> unloadItems, List<String> logList, int truckWeight, String contactNumber,
                   String contactName, int locationInList){
        this.deliveryId = deliveryId;
        this.siteAddress = siteAddress;
        this.documentId = documentId;
        this.loadItems = loadItems;
        this.unloadItems = unloadItems;
        this.logList = new LinkedList<>();
        for (String log: logList) {
            this.logList.add(convertStringToLog(log));
        }
        this.truckWeight = truckWeight;
        this.contactNumber = contactNumber;
        this.contactName = contactName;
        this.locationInAddressList= locationInList;
    }

    private problemSiteVisit convertStringToLog(String log) {
        updateTimeStamp();
        switch (log) {
            case "overweightTruck":
                return problemSiteVisit.overweightTruck;
            case "changedDriver":
                return problemSiteVisit.changedDriver;
            case "changedTruck":
                return problemSiteVisit.changedTruck;
            case "addedToDeliveryAfterAProblem":
                return problemSiteVisit.addedToDeliveryAfterAProblem;
            case "removedItemsFromLoad":
                return problemSiteVisit.removedItemsFromLoad;
            case "removedItemsFromUnload":
                return problemSiteVisit.removedItemsFromUnload;
            case "removedSiteFromDelivery":
                return problemSiteVisit.removedSiteFromDelivery;
            case "AddedItemsToLoad":
                return problemSiteVisit.AddedItemsToLoad;
            case "AddedItemsToUnload":
                return problemSiteVisit.AddedItemsToUnload;
            default:
                return null;
        }
    }

    public int getLocationInAddressList() {
        return locationInAddressList;
    }

    public int getDeliveryId() {updateTimeStamp(); return deliveryId; }

    public void addLog(String log) {
        updateTimeStamp();
        logList.add(convertStringToLog(log));
    }

    public Map<Integer, Integer> getLoadItems() {
        updateTimeStamp();
        return loadItems;
    }

    public List<problemSiteVisit> getLogList() {
        updateTimeStamp();
        return logList;
    }

    public int getWeight() {updateTimeStamp(); return truckWeight;
    }

    public int getDocumentId() {
        updateTimeStamp();
        return documentId;
    }

    public void changeWeight(int newTruckWeight) {
        updateTimeStamp();
        truckWeight = newTruckWeight;
    }

    public String getSiteAddress() {
        updateTimeStamp();
        return siteAddress;
    }

    public void removeFromLoad(int itemCatalogNumber, int amount) throws Exception {
        updateTimeStamp();
        if (!loadItems.containsKey(itemCatalogNumber))
            throw new Exception("no such item in the site document");
        if (loadItems.get(itemCatalogNumber) < amount)
            throw new Exception("the item amount in the site document dont much the amount stated");
        loadItems.replace(itemCatalogNumber, loadItems.get(itemCatalogNumber)-amount);
        if (loadItems.get(itemCatalogNumber)==0) loadItems.remove(itemCatalogNumber);
    }

    public void removeFromUnload(int itemCatalogNumber, int amount) throws Exception {
        updateTimeStamp();
        if (!unloadItems.containsKey(itemCatalogNumber))
            throw new Exception("no such item in the site document");
        if (unloadItems.get(itemCatalogNumber) < amount)
            throw new Exception("the item amount in the site document dont much the amount stated");
        unloadItems.replace(itemCatalogNumber, unloadItems.get(itemCatalogNumber)-amount);
        if (unloadItems.get(itemCatalogNumber)==0) unloadItems.remove(itemCatalogNumber);
    }

    public boolean siteHasLoadOrUnload() {updateTimeStamp(); return loadItems.size() > 0 | unloadItems.size() > 0; }

    public void addToUnload(int itemCatalogNumber, int amount) {
        updateTimeStamp();
        if (unloadItems.containsKey(itemCatalogNumber))
            unloadItems.replace(itemCatalogNumber, unloadItems.get(itemCatalogNumber)+amount);
        else unloadItems.put(itemCatalogNumber, amount);
    }

    public void addToLoad(int itemCatalogNumber, int amount) {
        updateTimeStamp();
        if (loadItems.containsKey(itemCatalogNumber))
            loadItems.replace(itemCatalogNumber, loadItems.get(itemCatalogNumber)+amount);
        else loadItems.put(itemCatalogNumber, amount);
    }

    public Map<Integer, Integer> getUnloadItems() {
        updateTimeStamp();
        return unloadItems;
    }

    public String getContactNumber(){
        updateTimeStamp();
        return contactNumber;
    }

    public String getContactName(){
        updateTimeStamp();
        return contactName;
    }

    public void changeLocation(int i) {
        locationInAddressList=i;
    }

}
