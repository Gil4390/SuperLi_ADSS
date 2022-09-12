package Presentation.Model.DeliveryModuleModel;

import ServiceLayer.Objects.DeliveryObjects.FTruck;

import java.util.Scanner;

public class TruckModel {
    private final String licenseNumber;
    private final String model;
    private final int baseWeight;
    private final int maxWeight;
    private final Scanner scanner;

    public TruckModel(FTruck ftruck)
    {
        this.licenseNumber = ftruck.getLicenseNumber();
        this.model = ftruck.getModel();
        this.baseWeight = ftruck.getBaseWeight();
        this.maxWeight = ftruck.getMaxWeight();
        this.scanner = new Scanner(System.in);
    }

    public int getMaxWeight() { return maxWeight; }
    public String getLicenseNumber() {return licenseNumber;}
    public String getModel() { return model; }
    public int getBaseWeight() { return baseWeight; }

    public String toString(){
        return "Model: "+model+"        License: "+licenseNumber+"        Max Weight: "+maxWeight;
    }
}
