package BusinessLayer.DeliveryModule.Objects;

public class StockShortness extends TimeStampChecker {

    private final int id;
    private final String branchAddress;
    private final String itemName;
    private final int itemCatalogNumber;
    private final int amount;
    private final String supplierAddress;
    private int isBounded;
    private int[] siteDocBoundness;
    private int orderBound;

    public StockShortness(int id, String branchAddress, String itemName, int itemCatalogNumber,
                            int amount, String supplierAddress, int orderId){
        this.id = id;
        this.branchAddress = branchAddress;
        this.itemName = itemName;
        this.itemCatalogNumber = itemCatalogNumber;
        this.amount = amount;
        this.supplierAddress = supplierAddress;
        isBounded = -1;
        siteDocBoundness= new int[]{-1, -1};
        this.orderBound= orderId;
    }

    public StockShortness(int id, String branchAddress, String itemName, int itemCatalogNumber,
                          int amount, String supplierAddress, int deliveryBound, int loadBound, int unloadBound, int orderId){
        this.id = id;
        this.branchAddress = branchAddress;
        this.itemName = itemName;
        this.itemCatalogNumber = itemCatalogNumber;
        this.amount = amount;
        this.supplierAddress = supplierAddress;
        this.isBounded = deliveryBound;
        siteDocBoundness= new int[]{loadBound, unloadBound};
        this.orderBound= orderId;

    }

    public String getBranchAddress() {updateTimeStamp(); return branchAddress; }
    public String getItemName() {updateTimeStamp(); return itemName; }
    public int getItemCatalogNumber() {updateTimeStamp(); return itemCatalogNumber; }
    public int getAmount() {updateTimeStamp(); return amount; }
    public String getSupplierAddresses() {updateTimeStamp(); return supplierAddress; }
    public int getDeliveryBound() {updateTimeStamp(); return isBounded; }
    public int getSiteDocLoadId(){updateTimeStamp(); return siteDocBoundness[0];}
    public int getSiteDocUnloadId(){updateTimeStamp(); return siteDocBoundness[1];}
    public int getId() {updateTimeStamp(); return id; }

    public boolean isBoundedTo(int DeliveryID){
        updateTimeStamp();
        return isBounded==DeliveryID;
    }

    public boolean getIsBounded() {
        updateTimeStamp();
        return isBounded!=-1;
    }

    public int getBoundNum(){
        updateTimeStamp();
        return isBounded;
    }

    public void boundDelivery(int deliveryId) throws Exception {
        updateTimeStamp();
        if(isBounded != -1)
            throw new Exception("stock shortness already bounded");
        isBounded = deliveryId;
    }

    public void boundLoadDoc(int siteDocId) throws Exception {
        updateTimeStamp();
        if(siteDocBoundness[0] != -1)
            throw new Exception("stock shortness already bounded to load SiteDoc");
        siteDocBoundness[0] = siteDocId;
    }

    public void boundUnloadDoc(int siteDocId) throws Exception {
        updateTimeStamp();
        if(siteDocBoundness[1] != -1)
            throw new Exception("stock shortness already bounded to unload SiteDoc");
        siteDocBoundness[1] = siteDocId;
    }

    public void unboundDelivery() throws Exception {
        updateTimeStamp();
        if(isBounded==-1)
            throw new Exception("stock shortness is not bounded");
        isBounded = -1;
    }

    public void unboundLoadDoc() throws Exception {
        updateTimeStamp();
        if(siteDocBoundness[0] == -1)
            throw new Exception("stock shortness is not bounded to load SiteDoc");
        siteDocBoundness[0] = -1;
    }

    public void unboundUnloadDoc() throws Exception {
        updateTimeStamp();
        if(siteDocBoundness[1] == -1)
            throw new Exception("stock shortness is not bounded to unload SiteDoc");
        siteDocBoundness[1] = -1;
    }


    public int getOrderId() { return orderBound;
    }
}
