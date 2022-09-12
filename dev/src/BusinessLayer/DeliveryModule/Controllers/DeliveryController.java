package BusinessLayer.DeliveryModule.Controllers;

import BusinessLayer.DeliveryModule.Objects.Delivery;
import DataAccessLayer.DeliveryModuleDal.DControllers.DeliveryMapper;

import java.util.*;

public class DeliveryController {

    private final DeliveryMapper mapper;
    private int deliveryID;

    public DeliveryController(DeliveryMapper deliveryMapper){
        mapper = deliveryMapper;
        Delivery d = (Delivery) mapper.selectLastId("Id");
        if (d == null){
            deliveryID = 0;
        }
        else
            deliveryID = d.getId() + 1;
    }

    public int addDelivery(String date, String time, String truckLicense, int driverId, String sourceAddress, List<String> destAddresses) throws Exception {
        mapper.insert(new Delivery(deliveryID, date, time, truckLicense, driverId, sourceAddress, destAddresses));
        deliveryID++;
        return deliveryID - 1;
    }

    public String getTruckLicense(int deliveryId) throws Exception {
        return getDeliveryById(deliveryId).getTruckLicense();
    }

    public int getDriverId(int deliveryId) throws Exception {
        return getDeliveryById(deliveryId).getDriverId();
    }

    public void changeListOfAddresses(int deliveryId, List<String> siteAddresses) throws Exception {
        mapper.changeAddressListForDelivery(deliveryId, siteAddresses);
    }

    public List<String> getListOfAddresses(int deliveryId) throws Exception {
        return getDeliveryById(deliveryId).getListOfAddresses();
    }

    public void removeSiteFromList(int deliveryId, int Location) throws Exception {
        mapper.removeAddressFromDelivery(deliveryId,Location);
    }

    public String getDate(int deliveryId) throws Exception {
        return getDeliveryById(deliveryId).getDate();
    }

    public List<Delivery> getAllDeliveries() throws Exception {
        return mapper.getAllDeliveries();
    }

    public Delivery getDeliveryById(int deliveryId) throws Exception {
        mapper.checkDeliveryExist(deliveryId);
        return mapper.getDelivery(deliveryId);
    }

    public void changeTruck(int deliveryId, String truckLicense) throws Exception {
        mapper.changeTruckForDelivery(deliveryId, truckLicense);
    }

    public void deleteAllData() {
        mapper.deleteAllDataFromDbs();
    }

    public List<Integer> getDeliveryIdInDate(String date) throws Exception {
        return mapper.getDeliveryIdInDate(date);
    }

    public void removeDelivery(int deliveryId) {
        mapper.deleteDelivery(deliveryId);
    }
}
