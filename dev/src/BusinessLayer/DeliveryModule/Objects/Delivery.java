package BusinessLayer.DeliveryModule.Objects;

import java.util.LinkedList;
import java.util.List;

public class Delivery extends TimeStampChecker {

    private final int id;
    private final String date;
    private final String time;
    private String truckLicense;
    private final int driverId;
    private String sourceAddress;
    private List<String> destAddresses;


    public Delivery(int id, String date, String time, String truckLicense, int driverId, String sourceAddress, List<String> destAddresses){
        this.id = id;
        this.date = date;
        this.time = time;
        this.truckLicense = truckLicense;
        this.driverId = driverId;
        this.sourceAddress = sourceAddress;
        this.destAddresses = destAddresses;
    }

    public int getId() {updateTimeStamp(); return id; }
    public String getDate() {updateTimeStamp(); return date; }
    public String getTime() {updateTimeStamp(); return time; }
    public String getTruckLicense() {updateTimeStamp(); return truckLicense; }
    public int getDriverId() {updateTimeStamp(); return driverId; }
    public String getSourceAddress() {updateTimeStamp(); return sourceAddress; }
    public List<String> getDestAddresses() {updateTimeStamp(); return destAddresses; }

    public void changeListOfAddresses(List<String> siteAddresses) {
        updateTimeStamp();
        List<String> temp = new LinkedList<>(siteAddresses);
        sourceAddress=temp.remove(0);
        destAddresses=temp;

    }

    public List<String> getListOfAddresses() {
        updateTimeStamp();
        List<String> ans= new LinkedList<>();
        ans.add(sourceAddress);
        ans.addAll(destAddresses);
        return ans;
    }

    public void removeAddress(int location) {
        updateTimeStamp();
        if (location==0) sourceAddress= destAddresses.remove(0);
        else destAddresses.remove(location-1);
    }

    public void changeTruck(String truckLicense) {
        updateTimeStamp();
        this.truckLicense= truckLicense;
    }
}
