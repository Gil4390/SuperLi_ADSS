package BusinessLayer.DeliveryModule.Objects;

public class Site extends TimeStampChecker {

    private final String address;
    private final String contactNumber;
    private final String contactName;
    private final String shippingArea;

    public Site(String address, String contactNumber, String contactName, String shippingArea){
        this.address = address;
        this.contactNumber = contactNumber;
        this.contactName = contactName;
        this.shippingArea = shippingArea;
    }

    public String getShippingArea(){
        updateTimeStamp();
        return shippingArea;
    }


    public String getContactNumber() {
        updateTimeStamp();
        return contactNumber;
    }

    public String getContactName() {
        updateTimeStamp();
        return contactName;
    }

    public String getAddress() {updateTimeStamp(); return address;
    }
}
