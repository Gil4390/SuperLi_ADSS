package BusinessLayer.DeliveryModule;


import BusinessLayer.DeliveryModule.Controllers.*;
import BusinessLayer.DeliveryModule.Objects.Delivery;
import BusinessLayer.DeliveryModule.Objects.*;
import BusinessLayer.SupplierBusiness.Order;
import BusinessLayer.SupplierBusiness.OrderProduct;
import DataAccessLayer.DeliveryModuleDal.DControllers.*;

import java.util.*;

public class DeliveryFacade {


    private final SiteController siteController;
    private final SiteDocController siteDocController;
    private final TruckController truckController;
    private final DeliveryController deliveryController;
    private final StockShortnessController stockShortnessController;

    public DeliveryFacade(CacheCleaner cacheCleaner) {
        DestMapper destMapper = new DestMapper();
        LoadItemsMapper loadItemsMapper = new LoadItemsMapper();
        SiteDocLogMapper siteDocLogMapper = new SiteDocLogMapper();
        TruckAvailableMapper truckAvailableMapper = new TruckAvailableMapper();
        UnloadItemsMapper unloadItemsMapper = new UnloadItemsMapper();

        SiteMapper siteMapper = new SiteMapper();
        SiteDocMapper siteDocMapper = new SiteDocMapper(siteDocLogMapper, loadItemsMapper, unloadItemsMapper);
        TruckMapper truckMapper = new TruckMapper(truckAvailableMapper);
        DeliveryMapper deliveryMapper = new DeliveryMapper(destMapper);
        StockShortnessMapper stockShortnessMapper = new StockShortnessMapper();

        siteController = new SiteController(siteMapper);
        siteDocController = new SiteDocController(siteDocMapper);
        truckController = new TruckController(truckMapper);
        deliveryController = new DeliveryController(deliveryMapper);
        stockShortnessController = new StockShortnessController(stockShortnessMapper);

        cacheCleaner.loadMaps(Arrays.asList(siteDocMapper, truckMapper, deliveryMapper, stockShortnessMapper));
    }

    public SiteController getSiteController() {return siteController;}
    public TruckController getTruckController(){return truckController;}

    public Delivery createDelivery(String date, String time, String TruckNumber, int driverId,
                                               List<Integer> stockShortnessesId, boolean checkShippingArea) throws Exception {
        List<StockShortness> stockShortnesses = getListOdStockShortnessesFromId(stockShortnessesId);
        checkStockShortnessListIsUnbound(stockShortnesses);
        truckController.checkTruckValidityForDelivery(TruckNumber, date);
        List<String> siteAddresses = createSiteAddresses(stockShortnesses);
        if (checkShippingArea) siteController.checkShippingAreaValidity(siteAddresses);
        String sourceAddress = siteAddresses.remove(0);
        int deliveryId = deliveryController.addDelivery(date, time, TruckNumber, driverId, sourceAddress, siteAddresses);
        truckController.setUnavailable(TruckNumber, date);
        stockShortnessController.boundToDelivery(deliveryId, stockShortnessesId);
        List<String> fullsiteAddresses = new LinkedList<>();
        fullsiteAddresses.add(sourceAddress);
        for (String s: siteAddresses)
            fullsiteAddresses.add(s);
        createSiteDocs(fullsiteAddresses, stockShortnesses, deliveryId);
        return deliveryController.getDeliveryById(deliveryId);
    }

    private void checkStockShortnessListIsUnbound(List<StockShortness> stockShortnesses) throws Exception {
        for (StockShortness stockShortness: stockShortnesses) {
            if(stockShortness.getIsBounded())
                throw new Exception("this stock shortness is already bounded to a different delivery");
        }
    }

    private void checkStockShortnessListIsBound(List<StockShortness> stockShortnesses, int deliveryId) throws Exception {
        for (StockShortness stockShortness: stockShortnesses) {
            if(!stockShortness.isBoundedTo(deliveryId))
                throw new Exception("this stock shortness is not bounded to the delivery");
        }
    }

    private List<StockShortness> getListOdStockShortnessesFromId(List<Integer> stockShortnessesId) throws Exception{
        List<StockShortness> stockShortnesses = new LinkedList<>();
        for (int stockShortnessid : stockShortnessesId)
            stockShortnesses.add(stockShortnessController.getStockShortness(stockShortnessid));
        return stockShortnesses;
    }


    public void createSiteDocs(List<String> siteAddresses, List<StockShortness> items, int deliveryId) throws Exception {
        Map<StockShortness, Boolean> mappingVisiting = new HashMap<>();
        for (StockShortness item: items)
            mappingVisiting.put(item, false);
        for (int i=0; i<siteAddresses.size(); i++) {
            String contactNumber = siteController.getContactNumber(siteAddresses.get(i));
            String contactName = siteController.getContactName(siteAddresses.get(i));
            List<Integer>[] bounding = siteDocController.createSiteDoc(siteAddresses.get(i), mappingVisiting, deliveryId, contactNumber, contactName, i);
            for (int stockIdLoad : bounding[0])
                stockShortnessController.boundLoadSiteDoc(stockIdLoad, bounding[2].get(0));
            for (int stockIdUnload : bounding[1])
                stockShortnessController.boundUnLoadSiteDoc(stockIdUnload, bounding[2].get(0));

        }
    }

    public List<String> createSiteAddresses(List<StockShortness> items) {
        //we'll create a map that sorts for each branch which sites he has to visit before unloading.
        //this in order to maximize each delivery- we want to unload items as soon as possible
        //in order to not exceed the weight limit, so after all the sites we need to load items for the branch
        //we will go straight for the branch.
        Map<String, List<String>> branchAddresses= new HashMap<>();
        for (StockShortness item: items) {
            if (branchAddresses.containsKey(item.getBranchAddress())){
                if (!branchAddresses.get(item.getBranchAddress()).contains(item.getSupplierAddresses()))
                    branchAddresses.get(item.getBranchAddress()).add(item.getSupplierAddresses());
            }
            else{
                branchAddresses.put(item.getBranchAddress(), new LinkedList<>());
                branchAddresses.get(item.getBranchAddress()).add(item.getSupplierAddresses());
            }
        }
        List<String> ans = new LinkedList<>();
        for (Map.Entry<String, List<String>> e: branchAddresses.entrySet()) {
            for (String address: e.getValue()) {
                if (!ans.contains(address))
                    ans.add(address);
            }
            ans.add(e.getKey());
        }
        return ans;
    }

    public void weightTruck(int documentId, int weight) throws Exception {
        int deliveryId = siteDocController.getDeliveryId(documentId);
        String TruckLicense = deliveryController.getTruckLicense(deliveryId);
        int maxTruckWeight = truckController.getMaxWeightOfTruck(TruckLicense);
        siteDocController.changeWeight(documentId, weight);
        if (weight > maxTruckWeight) {
            siteDocController.addLog(documentId, problemSiteVisit.overweightTruck);
            throw new Exception( "the truck is too heavy! please rePlan you're delivery!");
        }
    }

    public void changeTruck(int documentId, String truckLicense) throws Exception{
        int deliveryId = siteDocController.getDeliveryId(documentId);
        String date = deliveryController.getDate(deliveryId);
        truckController.checkTruckValidityForDelivery(truckLicense,date);
        int newTruckWeight = checkNewTruckWeight(documentId, truckLicense, date);
        deliveryController.changeTruck(deliveryId, truckLicense);
        siteDocController.changeWeight(documentId, newTruckWeight);
        addLogToFutureSites(documentId, problemSiteVisit.changedTruck);
        truckController.setUnavailable(truckLicense, deliveryController.getDate(deliveryId));

    }

    public int checkNewTruckWeight(int documentId, String truckLicense, String date) throws Exception {
        truckController.checkTruckValidityForDelivery(truckLicense, date);
        int TruckWeight= siteDocController.getTruckWeight(documentId);
        int deliveryId= siteDocController.getDeliveryId(documentId);
        String oldTruckLicense = deliveryController.getTruckLicense(deliveryId);
        int oldInitTruckWeight = truckController.getInitWeightOfTruck(oldTruckLicense);
        int ItemsWeight= TruckWeight-oldInitTruckWeight;
        int newInitTruckWeight = truckController.getInitWeightOfTruck(truckLicense);
        int newTruckWeight= newInitTruckWeight+ItemsWeight;
        int newMaxTruckWeight = truckController.getMaxWeightOfTruck(truckLicense);
        if(newMaxTruckWeight < newTruckWeight)
            throw new Exception("the truck you are trying to exchange is not big enough!");
        return newTruckWeight;
    }

    private void addLogToFutureSites(int documentId, problemSiteVisit log) throws Exception {
        siteDocController.addLog(documentId, log);
//        dDeliveryModulController.addLogToSiteDoc(documentId, problemSiteVisit.overweightTruck);
        siteDocController.addLogToFutureSites(documentId,log);
//        dDeliveryModulController.addLogToFutureSiteDoc(documentId, problemSiteVisit.changedTruck);
    }

    public void changeSites(int documentId, List<Integer> dropped, List<Integer> added, boolean checkShippingArea)  throws Exception{
        int deliveryId = siteDocController.getDeliveryId(documentId);
        List<StockShortness> droppedStockShortness = getListOdStockShortnessesFromId(dropped);
        List<StockShortness> addedStockShortness = getListOdStockShortnessesFromId(added);
        checkStockShortnessListIsBound(droppedStockShortness, deliveryId);
        controlRemovedStockShortness(deliveryId, droppedStockShortness, documentId);
        checkStockShortnessListIsUnbound(addedStockShortness);
        List<String> newSites = createSiteAddresses(addedStockShortness);
        List<String> addresses = creatingAddressListAfterChange(deliveryId, documentId, newSites);
        if (checkShippingArea) siteController.checkShippingAreaValidity(addresses);
        controlAddedStockShortness(newSites, deliveryId, addedStockShortness);
        if (addresses.isEmpty())
            deliveryController.removeDelivery(deliveryId);
        else deliveryController.changeListOfAddresses(deliveryId, addresses);
        stockShortnessController.deleteStockShortnesses(dropped);

    }


    public void changeOrderInDelivery(int oldOrderId, List<Integer> newStockShortnesses, List<Integer> oldStockShortnesses) throws Exception {
        if (oldStockShortnesses.isEmpty()) throw new Exception("there are no items matching this order");
        int deliveryId = stockShortnessController.getStockShortness(oldStockShortnesses.get(0)).getDeliveryBound();
        Integer documentId = getLatestAddress(deliveryId);
        if (documentId==null)
            throw new Exception("the delivery is already finished");
        changeSites(documentId, oldStockShortnesses, newStockShortnesses, false);

    }


    private List<String> creatingAddressListAfterChange(int deliveryId, int documentId, List<String> newSites) throws Exception {
        //sort for each site what need to come before him
        List<String> oldAddresses = deliveryController.getListOfAddresses(deliveryId);
        String currentAddress = siteDocController.getSiteAddress(documentId);
        int i=oldAddresses.size()-1;
        if (!newSites.isEmpty() && (currentAddress.equals(newSites.get(0)) )){
            newSites.remove(0);
        }
        while (!newSites.isEmpty() && (i>=0 && oldAddresses.get(i).equals(newSites.get(0)))){
            newSites.remove(0);
            i--;
        }
        oldAddresses.addAll(newSites);
        return oldAddresses;
    }

    private void controlRemovedStockShortness(int deliveryId, List<StockShortness> dropped, int documentId) throws Exception {
        //add log "removed item from site load" to each supplier in the dropped list
        //add log "removed item from site unload" to each branch in the dropped list
        //if the site is no longer in use - no load or unload items- deletes the site from delivery info
        for (StockShortness item: dropped){
            int UnLoadSiteDocId = item.getSiteDocUnloadId();
            siteDocController.addLog(UnLoadSiteDocId, problemSiteVisit.removedItemsFromUnload);
            siteDocController.removeFromUnload(UnLoadSiteDocId, item.getItemCatalogNumber(), item.getAmount());
            if (!siteDocController.siteHasLoadOrUnload(UnLoadSiteDocId) & UnLoadSiteDocId!=documentId) {
                siteDocController.addLog(UnLoadSiteDocId, problemSiteVisit.removedSiteFromDelivery);
                int LocationInList = siteDocController.removeSiteDocFromDelivery(UnLoadSiteDocId);
                deliveryController.removeSiteFromList(deliveryId, LocationInList);
            }
            stockShortnessController.unboundUnloadDoc(item.getId());

            int LoadSiteDocId = item.getSiteDocLoadId();
            siteDocController.addLog(LoadSiteDocId, problemSiteVisit.removedItemsFromLoad);
            siteDocController.removeFromLoad(LoadSiteDocId, item.getItemCatalogNumber(), item.getAmount());
            if (!siteDocController.siteHasLoadOrUnload(LoadSiteDocId) & !Objects.equals(siteDocController.getSiteAddress(documentId), item.getSupplierAddresses())) {
                siteDocController.addLog(LoadSiteDocId, problemSiteVisit.removedSiteFromDelivery);
                int LocationInList = siteDocController.removeSiteDocFromDelivery(LoadSiteDocId);
                deliveryController.removeSiteFromList(deliveryId, LocationInList);            }
            stockShortnessController.unboundLoadDoc(item.getId());

            stockShortnessController.unbound(item.getId());
        }
    }


    private void controlAddedStockShortness(List<String> newAddresses, int deliveryId, List<StockShortness> added) throws Exception {
        //if there is a new site we now need to visit- create a new SiteDoc and add a log "added the site after Weight problem"
        //for each site not new:
        //add log "added item to site load" to each supplier in the added list
        //add log "added item to site unload" to each branch in the added list
        Map<StockShortness, Boolean> mappingVisiting = new HashMap<>();
        for (StockShortness item: added)
            mappingVisiting.put(item, false);
        for(Map.Entry<StockShortness, Boolean> shortness: mappingVisiting.entrySet()) {
            if (!shortness.getValue()) {
                String supplier = shortness.getKey().getSupplierAddresses();
                if (!newAddresses.contains(supplier)) {
                    List<SiteDoc> s = siteDocController.getDocumentForDeliveryAndSite(deliveryId, supplier);
                    siteDocController.addToLoad(s.get(s.size() - 1).getDocumentId(), shortness.getKey().getItemCatalogNumber(), shortness.getKey().getAmount());
                    stockShortnessController.boundLoadSiteDoc(shortness.getKey().getId(), s.get(s.size() - 1).getDocumentId());
                    mappingVisiting.replace(shortness.getKey(), true);
                }
            }
        }
        for(Map.Entry<StockShortness, Boolean> shortness: mappingVisiting.entrySet()){
            if (shortness.getValue()) {
                String branch = shortness.getKey().getBranchAddress();
                if (!newAddresses.contains(branch)) {
                    List<SiteDoc> s = siteDocController.getDocumentForDeliveryAndSite(deliveryId, branch);
                    siteDocController.addToUnload(s.get(s.size() - 1).getDocumentId(), shortness.getKey().getItemCatalogNumber(), shortness.getKey().getAmount());
                    stockShortnessController.boundUnLoadSiteDoc(shortness.getKey().getId(), s.get(s.size() - 1).getDocumentId());


                }
            }
        }
        int index = getListOfAddressesForDelivery(deliveryId).size();
        for (int i=0; i<newAddresses.size(); i++) {
                String contactNumber = siteController.getContactNumber(newAddresses.get(i));
                String contactName = siteController.getContactName(newAddresses.get(i));
                List<Integer>[] bounding = siteDocController.createSiteDoc(newAddresses.get(i), mappingVisiting, deliveryId, contactNumber, contactName, index+i);
                for (int stockIdLoad : bounding[0])
                    stockShortnessController.boundLoadSiteDoc(stockIdLoad, bounding[2].get(0));
                for (int stockIdUnload : bounding[1])
                    stockShortnessController.boundUnLoadSiteDoc(stockIdUnload, bounding[2].get(0));
        }
        List<Integer> stockIds = new LinkedList<>();
        for (StockShortness ss: added)
            stockIds.add(ss.getId());
        stockShortnessController.boundToDelivery(deliveryId, stockIds);
    }


    public void arrivedToSite(int documentId) throws Exception{
        stockShortnessController.deleteAllStockBoundedToUnloadDoc(documentId);

    }

    public void finisheOrder(int documentId) {
        stockShortnessController.finisheOrder(documentId);
    }

    public List<Truck> getAllTrucksAvailable(String date) throws Exception {
        return truckController.getAllTrucksAvailable(date);

    }


    public List<StockShortness> getAllRelevantStockShortnesses() throws Exception{
        return stockShortnessController.getAllRelevantStockShortnesses();
    }

    public List<Delivery> getAllDeliveries() throws Exception{
        return deliveryController.getAllDeliveries();
    }

    public Truck getTruckByLicense(String truckLicense) throws Exception{
         return truckController.getTruckByLicense(truckLicense);
    }

    public List<SiteDoc> getAllDocumentsForDelivery(int deliveryId) throws Exception {
        return siteDocController.getAllDocumentsForDelivery(deliveryId);
    }

    public Integer getLatestAddress(int deliveryId) throws Exception {
        List<SiteDoc> allDocs = siteDocController.getAllDocumentsForDelivery(deliveryId);
        String truckLicense = deliveryController.getTruckLicense(deliveryId);
//        if (!allDocs.isEmpty()) {
//            SiteDoc source = allDocs.get(0);
//            if (!source.getLogList().isEmpty() && source.getLogList().contains(problemSiteVisit.overweightTruck) && source.getWeight() > truckController.getMaxWeightOfTruck(truckLicense))
//                return source.getSiteAddress();
//            if (source.getWeight() == 0)
//                return source.getSiteAddress();
//        }
        for (SiteDoc siteDoc : allDocs) {
            if (!siteDoc.getLogList().isEmpty() &&
                    siteDoc.getLogList().contains(problemSiteVisit.overweightTruck) &&
                    siteDoc.getWeight() > truckController.getMaxWeightOfTruck(truckLicense) &&
                    !siteDoc.getLogList().contains(problemSiteVisit.removedSiteFromDelivery))
                return siteDoc.getLocationInAddressList();
            if (siteDoc.getWeight() == 0 &&
                    (siteDoc.getLogList().isEmpty() || !siteDoc.getLogList().contains(problemSiteVisit.removedSiteFromDelivery)))
                if (!siteDoc.getLogList().contains(problemSiteVisit.removedSiteFromDelivery)) return siteDoc.getLocationInAddressList();
        }
        return null;
    }

    public List<SiteDoc> getDocumentForDeliveryAndSite(int deliveryId, String siteAddress) throws Exception{
        return siteDocController.getDocumentForDeliveryAndSite(deliveryId, siteAddress);
    }

    public List<String> getListOfAddressesForDelivery(int deliveryId) throws Exception{
        return deliveryController.getListOfAddresses(deliveryId);
    }

    public Boolean isDeliveryFinished(int deliveryId) throws Exception{
        Integer latestAddress = getLatestAddress(deliveryId);
        return latestAddress == null;
    }

    public List<StockShortness> getStockShortnessOfDelivery(int deliveryId) throws Exception {
        return stockShortnessController.getStockShortnessOfDelivery(deliveryId);
    }

    public List<Truck> getAllRelevantTrucks(String dateString, int siteDocId) throws Exception{
        List<Truck> trucks = truckController.getAllTrucksAvailable(dateString);
        List<Truck> ans = new LinkedList<>();
        int deliveryId = siteDocController.getDeliveryId(siteDocId);
        int weight = siteDocController.getTruckWeight(siteDocId);
        String truckLicense = deliveryController.getTruckLicense(deliveryId);
        int baseWeight = truckController.getInitWeightOfTruck(truckLicense);
        for (Truck truck : trucks) {
            int actualWeight = weight - baseWeight + truck.getBaseWeight();
            if (truck.getMaxWeight() >= actualWeight)
                ans.add(truck);
        }
        return ans;
    }


    public int getDriverOfSiteDoc(int siteDocId) throws Exception {
        int deliveryId = siteDocController.getDeliveryId(siteDocId);
        return deliveryController.getDriverId(deliveryId);
    }

    public void deleteAllData() {
        siteDocController.deleteAllData();
        deliveryController.deleteAllData();
        truckController.deleteAllData();
        stockShortnessController.resetData();
    }

    public SiteDoc getDocumentForDeliveryAndlocation(int deliveryId, int locationInAddressList) throws Exception {
        return siteDocController.getDocumentForDeliveryAndlocation(deliveryId,locationInAddressList);
    }

    public List<Integer> getDeliveryIdInDate(String date) throws Exception {
        return deliveryController.getDeliveryIdInDate(date);
    }

    public boolean isDeliveryExistOnDate(String date) throws Exception {
        return (!getDeliveryIdInDate(date).isEmpty());
    }

    public int createStockShortness(String branchAddress, String name, int productId, int amount, String supplierAddress, int orderId) throws Exception {
        return stockShortnessController.createStockShortness( branchAddress,  name,  productId,  amount,  supplierAddress, orderId);
    }

    public SiteDoc getDocument(int documentId) throws Exception {
        return siteDocController.getDocument(documentId);
    }

    public List<Integer> createStockShortnessListFromOrder(Order order, String supplierAddress) throws Exception {
            Map<Integer, OrderProduct> orderProducts = order.getOrderProducts();
            List<Integer> stockShortnesses = new LinkedList<>();
            for (OrderProduct orderProduct: orderProducts.values())
                stockShortnesses.add(createStockShortness(order.getBranchAddress(),orderProduct.getName(), orderProduct.getProductId(), orderProduct.getAmount(), supplierAddress, order.getOrderId()));
            return stockShortnesses;
        }

    public void deleteStockShortnesses(List<Integer> stockShortnessList) {
        stockShortnessController.deleteStockShortnesses(stockShortnessList);
    }

    public List<Integer> getStockShortnessOfOrder(int orderId) {
        return stockShortnessController.getStockShortnessOfOrder(orderId);
    }

    public boolean isOrderFinished(int orderId) {
        return stockShortnessController.getStockShortnessOfOrder(orderId).isEmpty();
    }

    public StockShortnessController getStockShortnessController(){return stockShortnessController; }

    public void loadTrucks() throws Exception {
        {
            truckController.addTruck("0372", "Kia", 5000, 10000);
            truckController.addTruck("7766", "Ford", 4500, 9000);
            truckController.addTruck("3434", "Mercedes", 6000, 12000);
            truckController.addTruck("1834", "Volvo", 4800, 10000);
            truckController.addTruck("7790", "Toyota", 7500, 15000);
            truckController.addTruck("0175", "Mazda", 7000, 14000);
            truckController.addTruck("0578", "Ford", 4500, 9000);
            truckController.addTruck("1043", "Mercedes", 6000, 12000);
            truckController.addTruck("9966", "Ford", 4500, 9000);
            truckController.addTruck("0420", "Honda", 2000, 3000);
        }
    }

}
