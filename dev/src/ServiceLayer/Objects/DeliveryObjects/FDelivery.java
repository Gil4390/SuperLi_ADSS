package ServiceLayer.Objects.DeliveryObjects;

import BusinessLayer.DeliveryModule.Objects.Delivery;

import java.util.List;

public class FDelivery {
    private int id;
    private String date;
    private String time;
    private String truckLicense;
    private int driverId;
    private String sourceAddress;
    private List<String> destAddresses;

    public FDelivery(Delivery delivery)
    {
        this.id = delivery.getId();
        this.date = delivery.getDate();
        this.time = delivery.getTime();
        this.driverId = delivery.getDriverId();
        this.truckLicense = delivery.getTruckLicense();
        this.sourceAddress = delivery.getSourceAddress();
        this.destAddresses = delivery.getDestAddresses();
    }

    public int getId() { return id; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getTruckLicense() { return truckLicense; }
    public int getDriverId() { return driverId; }
    public String getSourceAddress() { return sourceAddress; }
    public List<String> getDestAddresses() { return destAddresses; }
}
