package BusinessLayer.EmployeeModule.Objects;

import BusinessLayer.DeliveryModule.Objects.TimeStampChecker;

public class Branch extends TimeStampChecker {
    private String  branchAddress;
    private final String shippingArea;
    private int branchManagerId;

    public Branch(String branchAddress, int branchManagerId, String shippingArea){
        this.branchManagerId=branchManagerId;
        this.branchAddress=branchAddress;
        this.shippingArea= shippingArea;
    }

    public String getBranchAddress(){updateTimeStamp(); return branchAddress;}

    public String getShippingArea() {
        updateTimeStamp();
        return shippingArea;
    }

    public int getBranchManagerId() {
        updateTimeStamp();
        return branchManagerId;
    }
}
