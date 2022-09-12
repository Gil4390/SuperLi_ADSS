package Presentation.ViewModel.DeliveryModulViewModel;

import BusinessLayer.DeliveryModule.Objects.problemSiteVisit;
import Presentation.Model.BackendController;
import Presentation.View.DeliveryModuleView.*;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.Model.DeliveryModuleModel.DeliveryModel;
import Presentation.Model.DeliveryModuleModel.SiteDocumentModel;
import Presentation.Model.DeliveryModuleModel.StockShortnessModel;
import Presentation.Model.DeliveryModuleModel.TruckModel;

import java.util.*;

public class DeliveryViewModel {
    private final BackendController backendController;
    private final DeliveryModel deliveryModel;

    public DeliveryViewModel(DeliveryModel deliveryM) {
        this.backendController = BackendController.getInstance();
        this.deliveryModel=deliveryM;
    }

    public View FollowAndUpdateDelivery() {
        try {
            System.out.println("---------Following Delivery Number: " + deliveryModel.getId() + "---------");
            int numberOfSitesInDelivery = backendController.getListOfAddressesForDelivery(deliveryModel.getId()).size();
            int latestLocation = backendController.getLatestAddress(deliveryModel.getId());
            while (latestLocation<numberOfSitesInDelivery){
                SiteDocumentModel siteDocumentModel = backendController.getDocumentForDeliveryAndlocation(deliveryModel.getId(), latestLocation);
                System.out.println("the delivery is currently at:" + siteDocumentModel.getAddress());
//            String address = backendController.getLatestAddress(deliveryModel.getId());
//            List<String> addresses = backendController.getListOfAddressesForDelivery(deliveryModel.getId());
//            Map<String, List<SiteDocumentModel>> siteDocsForAddress = new HashMap<>();
//            for (int i = addresses.indexOf(address); i < addresses.size(); i++) {
//                if (!siteDocsForAddress.containsKey(addresses.get(i))){
//                    siteDocsForAddress.put(addresses.get(i), backendController.getDocumentForDeliveryAndSite(deliveryModel.getId(), addresses.get(i)));
//                }
//            }
//            for (int i = addresses.indexOf(address); i < addresses.size(); i++) {
//                System.out.println("the delivery is currently at:" + addresses.get(i));
//                if (siteDocsForAddress.get(addresses.get(i)).size() > 0) {
//                    int maxTruckWeight = backendController.getTruckByLicense(getTruckLicense()).getMaxWeight();
//                    while (siteDocsForAddress.get(addresses.get(i)).get(0).getLogList().contains(problemSiteVisit.removedSiteFromDelivery) ||
//                            (siteDocsForAddress.get(addresses.get(i)).get(0).getTruckWeight() > 0 && !(siteDocsForAddress.get(addresses.get(i)).get(0).getLogList().contains(problemSiteVisit.overweightTruck))) ||
//                            ((siteDocsForAddress.get(addresses.get(i)).get(0).getLogList().contains(problemSiteVisit.overweightTruck)) &&  (siteDocsForAddress.get(addresses.get(i)).get(0).getTruckWeight() < maxTruckWeight)))
//                        siteDocsForAddress.get(addresses.get(i)).remove(0);
//                }
//                SiteDocumentModel siteDocumentModel = siteDocsForAddress.get(addresses.get(i)).get(0);
//                siteDocsForAddress.get(addresses.get(i)).remove(0);
                System.out.println("please unload the following items: ");
                Map<Integer, Integer> unloadItems= siteDocumentModel.getUnloadItems();
                for (Map.Entry<Integer, Integer> entry : unloadItems.entrySet())
                    System.out.println("catalog number: "+entry.getKey()+"      amount: "+ entry.getValue());
                backendController.arrivedToSite(siteDocumentModel);
                System.out.println("please load the following items: ");
                Map<Integer, Integer> loadItems= siteDocumentModel.getLoadItems();
                for (Map.Entry<Integer, Integer> entry : loadItems.entrySet())
                    System.out.println("catalog number: "+entry.getKey()+"      amount: "+ entry.getValue());
                System.out.println("");
                System.out.println("please weight the truck and enter the new weight:");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                try {
                    backendController.weightTruck(siteDocumentModel, Integer.parseInt(input));
                } catch (Exception e) {
                    if (e.getMessage().equals("the truck is too heavy! please rePlan you're delivery!"))
                        return overWeightTruck(siteDocumentModel.getDocumentId());
                    else throw new Exception(e.getMessage());
                }
                System.out.println("Great! to the next place!");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                latestLocation++;
            }
            System.out.println("finished the delivery!!!!");
            return new MainMenuView(false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new MainMenuView(false);
        }
    }

    private View overWeightTruck(int siteDocId) throws Exception {
        System.out.println("the truck is too heavy! please rePlan you're delivery!");
        System.out.println("to change the truck- please enter 1");
        System.out.println("to edit the stock shortness list addressed in this delivery- please enter 2");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if(input.equals("1"))
            return changeTruck(siteDocId);
        if (input.equals("2"))
            return changeStockShortnesses(siteDocId);
        else return invalidInput();
    }

    private View changeStockShortnesses(int siteDocId) throws Exception {
        System.out.println("Please choose which stock shortness you won't fix today (in format 'n,n,...'):");
        System.out.println("If you don't want to remove any stock shortness from the delivery, enter ','");
        List<StockShortnessModel> stockShortnessModel = backendController.getStockShortnessOfDelivery(deliveryModel);
        for (int i=0; i<stockShortnessModel.size(); i++)
            System.out.println(i+1 + ") "+ stockShortnessModel.get(i).toString());
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] str = input.split(",");
        List<StockShortnessModel> stockShortnessModelsDrop = new LinkedList<>();
        for (String s: str) {
            stockShortnessModelsDrop.add(stockShortnessModel.get(Integer.parseInt(s)-1));
        }
        System.out.println("Please choose which stock shortness you want to add to the delivery (in format 'n,n,...'):");
        System.out.println("If you don't want to add any stock shortness to the delivery, enter ','");
        stockShortnessModel = backendController.getAllRelevantStockShortnesses();
        for (int i=0; i<stockShortnessModel.size(); i++)
            System.out.println(i+1 + ") "+ stockShortnessModel.get(i).toString());
        input = scanner.nextLine();
        str = input.split(",");
        List<StockShortnessModel> stockShortnessModelsAdd = new LinkedList<>();
        for (String s: str) {
            stockShortnessModelsAdd.add(stockShortnessModel.get(Integer.parseInt(s)-1));
        }
        try {
            backendController.changeSites(siteDocId, stockShortnessModelsAdd, stockShortnessModelsDrop, true);
            return FollowAndUpdateDelivery();
        }
        catch (Exception e){
            if (e.getMessage().equals("the new delivery will contains multiple shipping areas"))
            {
                System.out.println("the delivery contains multiple shipping area, do you wish to continue?");
                input = scanner.nextLine();
                if (input.equals("yes")) {
                    backendController.changeSites(siteDocId, stockShortnessModelsAdd, stockShortnessModelsDrop, false);
                    return FollowAndUpdateDelivery();
                }
                else return new MainMenuView(false);
            }
            else throw new Exception(e.getMessage());
        }


    }

    private View changeTruck(int siteDocId) {
        System.out.println("Please choose a different Truck for the delivery:");
        List<TruckModel> trucks = backendController.getAllRelevantTrucks(deliveryModel.getDateString(), siteDocId);
        if(trucks.size()==0) {
            System.out.println("There are no trucks available");
            return new MainMenuView(false);
        }
        for (int i=0; i<trucks.size(); i++)
            System.out.println(i+1 + ") "+ trucks.get(i).toString());
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        TruckModel truckM = trucks.get(Integer.parseInt(input)-1);
        backendController.changeTruck(siteDocId, truckM);
        return FollowAndUpdateDelivery();
    }


    public View invalidInput() {
        System.out.println("invalid input!");
        return new MainMenuView(false);
    }

    public int getId() { return deliveryModel.getId();
    }

    public String getDateString() { return deliveryModel.getDateString();
    }

    public String getTimeString() { return deliveryModel.getTimeString();
    }

    public String getTruckLicense() { return deliveryModel.getTruckLicense();
    }

    public int getDriverId() { return deliveryModel.getDriverId();
    }

    public String getSourceAddress() { return deliveryModel.getSourceAddress();
    }

    public List<String> getDestAddresses() { return deliveryModel.getDestAddresses();
    }

    public Date getDate() { return deliveryModel.getDate();
    }

    public Date getTime() { return deliveryModel.getTime();
    }

    public boolean isAboutTime() {
        Date d= deliveryModel.getDate();
        return (d.before(new Date()) && !backendController.isDeliveryFinished(deliveryModel));
    }

    public View getAllDeliveriesView() {
        return new AllDeliveriesView();
    }

    public View getTruckView(String truckLicense) {
        return new TruckView(truckLicense);
    }

    public View getDriverView(int driverId) {
        return new DriverView(driverId);
    }

    public View getAllDocsForDeliveryView(int id) {
        return new AllDocsForDeliveryView(id);
    }
}
