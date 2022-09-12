package BusinessLayer.DeliveryModule.Controllers;

import BusinessLayer.DeliveryModule.Objects.SiteDoc;
import BusinessLayer.DeliveryModule.Objects.StockShortness;
import BusinessLayer.DeliveryModule.Objects.problemSiteVisit;
import DataAccessLayer.DeliveryModuleDal.DControllers.SiteDocMapper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SiteDocController {

    private final SiteDocMapper mapper;
    private int siteDocumentID;

    public SiteDocController(SiteDocMapper mapper){
        this.mapper = mapper;
        SiteDoc siteDoc = (SiteDoc) mapper.selectLastId("Id");
        if (siteDoc == null){
            siteDocumentID = 0;
        }
        else
            siteDocumentID = siteDoc.getDocumentId() + 1;
    }

    public List<Integer>[] createSiteDoc(String siteAddress, Map<StockShortness, Boolean> items, int deliveryId, String contactNumber, String contactName, int locationInAddressList) throws Exception {
        Map<Integer, Integer> loadItems = new HashMap<>();
        Map<Integer, Integer> unloadItems = new HashMap<>();

        List<Integer>[] stockBoundness = new List[3];
        stockBoundness[0] = new LinkedList<>(); //represent loadStock
        stockBoundness[1] = new LinkedList<>(); //represent unloadStock
        stockBoundness[2] = new LinkedList<>(); //represent siteDocId
        stockBoundness[2].add(siteDocumentID);

        for (Map.Entry<StockShortness, Boolean> item: items.entrySet()) {
            if(item.getKey().getSupplierAddresses().equals(siteAddress) && !item.getValue()){
                items.replace(item.getKey(), true);
                if (!loadItems.containsKey(item.getKey().getItemCatalogNumber()))
                    loadItems.put(item.getKey().getItemCatalogNumber(), item.getKey().getAmount());
                else loadItems.replace(item.getKey().getItemCatalogNumber(), loadItems.get(item.getKey().getItemCatalogNumber())+item.getKey().getAmount());
                stockBoundness[0].add(item.getKey().getId());
            }
            if(item.getKey().getBranchAddress().equals(siteAddress)  && item.getValue()){
                if (!unloadItems.containsKey(item.getKey().getItemCatalogNumber()))
                    unloadItems.put(item.getKey().getItemCatalogNumber(), item.getKey().getAmount());
                else unloadItems.replace(item.getKey().getItemCatalogNumber(), unloadItems.get(item.getKey().getItemCatalogNumber())+item.getKey().getAmount());
                stockBoundness[1].add(item.getKey().getId());
            }
        }
        mapper.insert(new SiteDoc(deliveryId, siteAddress, siteDocumentID, loadItems, unloadItems, new LinkedList<>(), contactNumber, contactName, locationInAddressList));
        siteDocumentID++;
        return stockBoundness;
    }

    public SiteDoc createNewSiteDoc(String siteAddress, Map<Integer, Integer> loadItems, Map<Integer, Integer> unloadItems, int deliveryId, String contactNumber, String contactName, int locationInAddressList) throws Exception {
        SiteDoc s = new SiteDoc(deliveryId, siteAddress, siteDocumentID, loadItems, unloadItems, new LinkedList<>(), contactNumber, contactName, locationInAddressList);
        mapper.insert(s);
        siteDocumentID++;
        return s;
    }

//    private boolean isSiteDocExist(int documentId){
//        return siteDocs.containsKey(documentId);
//    }
    private SiteDoc getSiteDoc(int documentId) throws Exception {
        mapper.checkSiteDocExist(documentId);
        return mapper.getSiteDoc(documentId);
    }

    public int getDeliveryId(int documentId) throws Exception {
        return getSiteDoc(documentId).getDeliveryId();
    }

    public void addLog(int documentId, problemSiteVisit log) throws Exception {
        mapper.addLogToSiteDoc(documentId, log.name());
    }

    public int getTruckWeight(int documentId) throws Exception {
        return getSiteDoc(documentId).getWeight();
    }

    public void changeWeight(int documentId, int newTruckWeight) throws Exception {
        mapper.changeWeightOnDocument(documentId, newTruckWeight);
    }

    public void addLogToFutureSites(int documentId, problemSiteVisit log) throws Exception {
        int deliveryId = getSiteDoc(documentId).getDeliveryId();
        List<SiteDoc> futureSiteDocs = mapper.getFutureSiteDocs(deliveryId);
        for(SiteDoc e: futureSiteDocs){
            addLog(e.getDocumentId(), log);
        }
    }


    public void removeFromUnload(int documentId, int itemCatalogNumber, int amount) throws Exception {
        mapper.removeItemFromUnLoad(documentId, itemCatalogNumber, amount);

    }

    public void removeFromLoad(int documentId, int itemCatalogNumber, int amount) throws Exception {
        mapper.removeItemFromLoad(documentId, itemCatalogNumber, amount);
    }

    public boolean siteHasLoadOrUnload(int documentId) throws Exception {
        return getSiteDoc(documentId).siteHasLoadOrUnload();
    }

    public Map<Integer, Integer> getUnloadItems(int documentId) throws Exception {
        return getSiteDoc(documentId).getUnloadItems();
    }

    public String getSiteAddress(int documentId) throws Exception {
        return getSiteDoc(documentId).getSiteAddress();
    }

    public List<SiteDoc> getAllDocumentsForDelivery(int deliveryId) throws Exception {
        return mapper.getAllDocumentsForDelivery(deliveryId);
    }

    public List<SiteDoc> getDocumentForDeliveryAndSite(int deliveryId, String address) throws Exception {
        return mapper.getDocumentForDeliveryAndSite(deliveryId,address);
    }

    public void addToUnload(int documentId, int itemCatalogNumber, int amount) throws Exception {
        mapper.addToUnload(documentId, itemCatalogNumber, amount);
    }

    public void addToLoad(int documentId, int itemCatalogNumber, int amount) throws Exception {
        mapper.addToLoad(documentId, itemCatalogNumber, amount);
    }

    public int removeSiteDocFromDelivery(int SiteDocId) throws Exception {
        return mapper.removeSiteDocFromDelivery(SiteDocId);
    }

    public void deleteAllData() {
        mapper.deleteAllDataFromDbs();
    }

    public SiteDoc getDocumentForDeliveryAndlocation(int deliveryId, int locationInAddressList) throws Exception {
        return mapper.getSiteDocByDeliveryIdAndLocation(deliveryId,locationInAddressList);
    }

    public SiteDoc getDocument(int documentId) throws Exception {
        return getSiteDoc(documentId);
    }

}
