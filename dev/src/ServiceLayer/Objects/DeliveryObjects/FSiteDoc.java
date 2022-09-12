package ServiceLayer.Objects.DeliveryObjects;

import BusinessLayer.DeliveryModule.Objects.SiteDoc;
import BusinessLayer.DeliveryModule.Objects.problemSiteVisit;

import java.util.List;
import java.util.Map;

public class FSiteDoc {
    private final int deliveryId;
    private final String siteAddress;
    private final int documentId;
    private Map<Integer, Integer> loadItems;
    private Map<Integer, Integer> unloadItems;
    private List<problemSiteVisit> logList;
    private int truckWeight;
    String contactNumber;
    String contactName;

    public FSiteDoc(SiteDoc siteDoc){
        this.deliveryId=siteDoc.getDeliveryId();
        this.siteAddress=siteDoc.getSiteAddress();
        this.documentId=siteDoc.getDocumentId();
        this.loadItems=siteDoc.getLoadItems();
        this.unloadItems=siteDoc.getUnloadItems();
        this.logList=siteDoc.getLogList();
        this.truckWeight=siteDoc.getWeight();
        this.contactNumber=siteDoc.getContactNumber();
        this.contactName=siteDoc.getContactName();
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public int getDocumentId() {
        return documentId;
    }

    public Map<Integer, Integer> getLoadItems() {
        return loadItems;
    }

    public Map<Integer, Integer> getUnloadItems() {
        return unloadItems;
    }

    public List<problemSiteVisit> getLogList() {
        return logList;
    }

    public int getTruckWeight() {
        return truckWeight;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getContactName() {
        return contactName;
    }
}
