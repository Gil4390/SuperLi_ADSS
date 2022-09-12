package ServiceLayer.Services;

import BusinessLayer.ApplicationFacade;
import BusinessLayer.DeliveryModule.Objects.Delivery;
import BusinessLayer.DeliveryModule.Objects.SiteDoc;
import BusinessLayer.DeliveryModule.Objects.StockShortness;
import BusinessLayer.DeliveryModule.Objects.Truck;
import ServiceLayer.Objects.DeliveryObjects.FDelivery;
import ServiceLayer.Objects.DeliveryObjects.FSiteDoc;
import ServiceLayer.Objects.DeliveryObjects.FStockShortness;
import ServiceLayer.Objects.DeliveryObjects.FTruck;
import ServiceLayer.Responses.Response;
import ServiceLayer.Responses.ResponseT;

import java.util.LinkedList;
import java.util.List;

public class DeliveryService {
    private ApplicationFacade applicationFacade;

    public DeliveryService(ApplicationFacade applicationFacade) {
        this.applicationFacade = applicationFacade;
    }

    ///////////////DELIVERY////////////////
    public ResponseT<FDelivery> createDelivery(String date, String time, String licenseNumber, int id, List<Integer> stockShortnesses, boolean checkShippingArea) {
        try {
            Delivery d = applicationFacade.createNewDelivery(date, time, licenseNumber, id, stockShortnesses, checkShippingArea);
            return new ResponseT<>(new FDelivery(d));
        }
        catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public Response weightTruck(int documentId, int weight) {
        try {
            applicationFacade.weightTruck(documentId,weight);
            return new Response();
        }
        catch (Exception e) { return new Response(e.getMessage()); }
    }

    public Response changeTruck(int documentId, String licenseNumber) {
        try {
            applicationFacade.changeTruck(documentId,licenseNumber);
            return new Response();
        }
        catch (Exception e) { return new Response(e.getMessage()); }
    }

    public Response changeSites(int documentId, List<Integer> dropStock, List<Integer> addStock, boolean checkShippingArea) {
        try {
            applicationFacade.changeSites(documentId, dropStock, addStock, checkShippingArea);
            return new Response();
        }
        catch (Exception e) { return new Response(e.getMessage()); }
    }

    public Response arrivedToSite(int documentId) {
        try {
            applicationFacade.arrivedToSite(documentId);
            return new Response();
        }
        catch (Exception e) { return new Response(e.getMessage()); }
    }

    public ResponseT<List<FTruck>> getAllTrucksAvailable(String date) {
        try {
            List<Truck> trucks = applicationFacade.getAllTrucksAvailable(date);;
            List<FTruck> fTrucks = new LinkedList<>();
            for (Truck truck: trucks) {
                fTrucks.add(new FTruck(truck));
            }
            return new ResponseT<>(fTrucks);
        }
        catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }


    public ResponseT<List<FStockShortness>> getAllRelevantStockShortnesses() {
        try {
            List<StockShortness> stockShortnessList = applicationFacade.getAllRelevantStockShortnesses();;
            List<FStockShortness> fStockShortnessList = new LinkedList<>();
            for (StockShortness stockShortness: stockShortnessList) {
                fStockShortnessList.add(new FStockShortness(stockShortness));
            }
            return new ResponseT<>(fStockShortnessList);
        }
        catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }


    public ResponseT<List<FDelivery>> getAllDeliveries() {
        try {
            List<Delivery> deliveries = applicationFacade.getAllDeliveries();;
            List<FDelivery> fDeliveries = new LinkedList<>();
            for (Delivery delivery: deliveries) {
                fDeliveries.add(new FDelivery(delivery));
            }
            return new ResponseT<>(fDeliveries);
        }
        catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }


    public ResponseT<FTruck> getTruckByLicense(String truckLicense) {
        try {
            Truck t = applicationFacade.getTruckByLicense(truckLicense);
            return new ResponseT<>(new FTruck(t));
        }
        catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<FSiteDoc>> getAllDocumentsForDelivery(int deliveryId){
        try {
            List<SiteDoc> siteDocs = applicationFacade.getAllDocumentsForDelivery(deliveryId);;
            List<FSiteDoc> fSiteDocs = new LinkedList<>();
            for (SiteDoc siteDoc: siteDocs) {
                fSiteDocs.add(new FSiteDoc(siteDoc));
            }
            return new ResponseT<>(fSiteDocs);
        }
        catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<Integer> getLatestAddress(int deliveryId) {
        try {
            Integer s = applicationFacade.getLatestAddress(deliveryId);
            ResponseT<Integer> r = new ResponseT<>();
            r.setValue(s);
            return r;
        }
        catch (Exception e){ return new ResponseT<>(e.getMessage());}
    }

    public ResponseT<List<FSiteDoc>> getDocumentForDeliveryAndSite(int deliveryId, String siteAddress) {
        try {
            List<SiteDoc> siteDocs = applicationFacade.getDocumentForDeliveryAndSite(deliveryId, siteAddress);;
            List<FSiteDoc> fSiteDocs = new LinkedList<>();
            for (SiteDoc siteDoc: siteDocs) {
                fSiteDocs.add(new FSiteDoc(siteDoc));
            }
            return new ResponseT<>(fSiteDocs);
        }
        catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<String>> getListOfAddressesForDelivery(int deliveryId) {
        try {
            List<String> s = applicationFacade.getListOfAddressesForDelivery(deliveryId);
            return new ResponseT<>(s);
        } catch (Exception e) {return new ResponseT<>(e.getMessage());}
    }

    public ResponseT<Boolean> isDeliveryFinished(int id) {
        try {
            Boolean s = applicationFacade.isDeliveryFinished(id);
            return new ResponseT<>(s);
        } catch (Exception e) {return new ResponseT<>(e.getMessage());}
    }

    public ResponseT<List<FStockShortness>> getStockShortnessOfDelivery(int id) {
        try {
            List<StockShortness> stockShortnessList = applicationFacade.getStockShortnessOfDelivery(id);;
            List<FStockShortness> fStockShortnessList = new LinkedList<>();
            for (StockShortness stockShortness: stockShortnessList) {
                fStockShortnessList.add(new FStockShortness(stockShortness));
            }
            return new ResponseT<>(fStockShortnessList);
        }
        catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<FTruck>> getAllRelevantTrucks(String dateString, int siteDocId) {
        try {
            List<Truck> trucks = applicationFacade.getAllRelevantTrucks(dateString, siteDocId);;
            List<FTruck> fTrucks = new LinkedList<>();
            for (Truck truck: trucks) {
                fTrucks.add(new FTruck(truck));
            }
            return new ResponseT<>(fTrucks);
        }
        catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<FSiteDoc> getDocumentForDeliveryAndlocation(int deliveryId, int locationInAddressList) {
        try {
            SiteDoc siteDoc = applicationFacade.getDocumentForDeliveryAndlocation(deliveryId, locationInAddressList);
            return new ResponseT<>(new FSiteDoc(siteDoc));
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

}
