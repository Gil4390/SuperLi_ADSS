package BusinessLayer.DeliveryModule.Objects;

import java.util.LinkedList;
import java.util.List;

public class Truck extends TimeStampChecker {

    private final String licenseNumber;
    private final String model;
    private final int baseWeight;
    private final int maxWeight;
    private final List<String> isAvailable;

    public Truck(String licenseNumber, String model, int baseWeight, int maxWeight){
        this.licenseNumber = licenseNumber;
        this.model = model;
        this.baseWeight = baseWeight;
        this.maxWeight = maxWeight;
        isAvailable = new LinkedList<>();
    }

    public Truck(String licenseNumber, String model, int baseWeight, int maxWeight, List<String> isAvailable){
        this.licenseNumber = licenseNumber;
        this.model = model;
        this.baseWeight = baseWeight;
        this.maxWeight = maxWeight;
        this.isAvailable = isAvailable;
    }

    public String getLicenseNumber() {updateTimeStamp(); return licenseNumber; }
    public String getModel() {updateTimeStamp(); return model; }
    public int getBaseWeight() {updateTimeStamp(); return baseWeight; }
    public int getMaxWeight() {updateTimeStamp(); return maxWeight; }
    public List<String> getIsAvailable() {updateTimeStamp(); return isAvailable; }

    public boolean isAvailable(String date) {
        updateTimeStamp();
        return !isAvailable.contains(date);
    }

    public void setUnavailable(String date) {
        updateTimeStamp();
        isAvailable.add(date);
    }

}
