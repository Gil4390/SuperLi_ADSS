package BusinessLayer.DeliveryModule.Controllers;

import BusinessLayer.DeliveryModule.Objects.Truck;
import DataAccessLayer.DeliveryModuleDal.DControllers.TruckMapper;

import java.util.*;

public class TruckController {

    private final TruckMapper mapper;

    public TruckController(TruckMapper truckMapper){
        mapper = truckMapper;
        //loadData();
    }

    public void addTruck(String licenseNumber, String model, int baseWeight, int maxWeight) throws Exception {
        mapper.insert(new Truck(licenseNumber, model, baseWeight, maxWeight));
    }

    private Truck getTruck(String licenseNumber)throws Exception{
        mapper.checkIfTruckExists(licenseNumber);
        return mapper.getTruck(licenseNumber);
    }

    private boolean isTruckAvailable(String licenseNumber, String date) throws Exception {
        return getTruck(licenseNumber).isAvailable(date);
    }

    public int getMaxWeightOfTruck(String licenseNumber) throws Exception {
        return getTruck(licenseNumber).getMaxWeight();
    }

    public void setUnavailable(String licenseNumber, String date) throws Exception {
        mapper.setTruckUnavailable(licenseNumber, date);
    }

    public int getInitWeightOfTruck(String licenseNumber) throws Exception {
        return getTruck(licenseNumber).getBaseWeight();
    }

    public void checkTruckValidityForDelivery(String truckLicense, String date) throws Exception {
        if (!isTruckAvailable(truckLicense, date))
            throw new Exception("the truck is not available for the drive");
    }

    public List<Truck> getAllTrucksAvailable(String date)throws Exception {
        return mapper.getAllTrucksAvailable(date);
    }

    public Truck getTruckByLicense(String truckLicense) throws Exception {
        return getTruck(truckLicense);
    }

    private void loadData() {
        try {
            addTruck("0372", "Kia", 5000, 10000);
            addTruck("7766", "Ford", 4500, 9000);
            addTruck("3434", "Mercedes", 6000, 12000);
            addTruck("1834", "Volvo", 4800, 10000);
            addTruck("7790", "Toyota", 7500, 15000);
            addTruck("0175", "Mazda", 7000, 14000);
            addTruck("0578", "Ford", 4500, 9000);
            addTruck("1043", "Mercedes", 6000, 12000);
            addTruck("9966", "Ford", 4500, 9000);
            addTruck("0420", "Honda", 2000, 3000);
        }
        catch (Exception ignored){

        }
    }

    public void deleteAllData() {
        mapper.deleteAllDataFromDbs();
    }
}
