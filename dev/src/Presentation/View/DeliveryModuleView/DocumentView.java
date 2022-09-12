package Presentation.View.DeliveryModuleView;

import Presentation.View.View;
import Presentation.Model.DeliveryModuleModel.SiteDocumentModel;
import Presentation.ViewModel.DeliveryModulViewModel.SiteDocViewModel;
import BusinessLayer.DeliveryModule.Objects.problemSiteVisit;
import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;

import java.util.List;
import java.util.Map;

public class DocumentView implements View {
    private SiteDocViewModel siteDocViewModel;

    public DocumentView(SiteDocumentModel siteDocumentModel) {
        this.siteDocViewModel = new SiteDocViewModel(siteDocumentModel);
    }

    @Override
    public void printMenu() {
        System.out.println("----------------- DOCUMENT ID: "+siteDocViewModel.getDocumentId()+"-----------------");
        System.out.println("         DELIVERY ID: "+ siteDocViewModel.getDeliveryId());
        System.out.println("         SITE ADDRESS: "+ siteDocViewModel.getAddress());
        System.out.println("         CONTACT NAME: "+ siteDocViewModel.getContactName());
        System.out.println("         CONTACT PHONE: "+ siteDocViewModel.getContactNumber());
        System.out.println("         TRUCK WEIGHT: "+ siteDocViewModel.getTruckWeight());
        System.out.println("         LOAD ITEMS:     CATALOG NUMBER       AMOUNT");
        Map<Integer, Integer> loadItems = siteDocViewModel.getLoadItems();
        for (Map.Entry<Integer, Integer> entry : loadItems.entrySet())
            System.out.println("                               "+entry.getKey()+"                "+ entry.getValue());
        System.out.println("         UNLOAD ITEMS:   CATALOG NUMBER       AMOUNT");
        Map<Integer, Integer> unloadItems = siteDocViewModel.getUnloadItems();
        for (Map.Entry<Integer, Integer> entry : unloadItems.entrySet())
            System.out.println("                               "+entry.getKey()+"                "+ entry.getValue());
        List<problemSiteVisit> logs= siteDocViewModel.getLogList();
        System.out.println("         LOG LIST:");
        for(problemSiteVisit s: logs)
            System.out.println("                       "+ s);
    }

    @Override
    public View nextInput(String input) {
        switch (input) {
            case "back":
                return new AllDeliveriesView();
            case "close":
                ApplicationView.shouldTerminate = true;
                break;
            default:
                System.out.println("Invalid input!");
        }
        return this;
    }
}
