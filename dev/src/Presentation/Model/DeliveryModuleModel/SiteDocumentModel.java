package Presentation.Model.DeliveryModuleModel;

import ServiceLayer.Objects.DeliveryObjects.FSiteDoc;
import BusinessLayer.DeliveryModule.Objects.problemSiteVisit;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SiteDocumentModel {
    private final int deliveryId;
    private final String siteAddress;
    private final int documentId;
    private final String contactName;
    private final String contactNumber;
    private Map<Integer, Integer> loadItems;
    private Map<Integer, Integer> unloadItems;
    private List<problemSiteVisit> logList;
    private final int truckWeight;
    private final Scanner scanner;

    public SiteDocumentModel(FSiteDoc fSiteDoc)
    {
        this.deliveryId=fSiteDoc.getDeliveryId();
        this.siteAddress=fSiteDoc.getSiteAddress();
        this.documentId=fSiteDoc.getDocumentId();
        this.loadItems=fSiteDoc.getLoadItems();
        this.unloadItems=fSiteDoc.getUnloadItems();
        this.logList=fSiteDoc.getLogList();
        this.truckWeight = fSiteDoc.getTruckWeight();
        this.contactNumber = fSiteDoc.getContactNumber();
        this.contactName = fSiteDoc.getContactName();
        this.scanner = new Scanner(System.in);
    }

    public String getAddress() { return siteAddress;
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

    public String getContactName() {
        return contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }
}
