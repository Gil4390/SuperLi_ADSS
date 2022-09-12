package ServiceLayer.Objects.DeliveryObjects;

import BusinessLayer.DeliveryModule.Objects.Truck;

public class FTruck {
    private final String licenseNumber;
    private final String model;
    private final int baseWeight;
    private final int maxWeight;

    public FTruck(Truck truck){
        this.licenseNumber = truck.getLicenseNumber();
        this.model = truck.getModel();
        this.baseWeight = truck.getBaseWeight();
        this.maxWeight = truck.getMaxWeight();
    }

    public String getLicenseNumber() { return licenseNumber; }
    public String getModel() { return model; }
    public int getBaseWeight() { return baseWeight; }
    public int getMaxWeight() { return maxWeight; }
}
