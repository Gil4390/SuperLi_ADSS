package Presentation.View.DeliveryModuleView;

import Presentation.View.View;
import Presentation.Model.DeliveryModuleModel.DeliveryModel;
import Presentation.ViewModel.DeliveryModulViewModel.DeliveryViewModel;
import Presentation.View.ApplicationView;

import java.util.*;

public class DeliveryView implements View {
    private DeliveryViewModel deliveryViewModel;

    public DeliveryView(DeliveryModel deliveryModel) {
        this.deliveryViewModel = new DeliveryViewModel(deliveryModel);
    }

    @Override
    public void printMenu() {
        System.out.println("----------------- DELIVERY NUMBER "+deliveryViewModel.getId()+"-----------------");
        System.out.println("         DATE: "+ deliveryViewModel.getDateString());
        System.out.println("         TIME: "+ deliveryViewModel.getTimeString());
        System.out.println("         TRUCK LICENSE: "+ deliveryViewModel.getTruckLicense());
        System.out.println("         DRIVER ID: "+ deliveryViewModel.getDriverId());
        System.out.println("         From: "+ deliveryViewModel.getSourceAddress());
        List<String> addresses= deliveryViewModel.getDestAddresses();
        String destList="";
        for(String s: addresses)
            destList=destList+" -> " + s;
        System.out.println("         TO: "+ destList);

        System.out.println("To view more info about the truck enter 1");
        System.out.println("To view more info about the driver enter 2");
        System.out.println("To view the site documents related to this delivery enter 3");
        if(deliveryViewModel.isAboutTime())
            System.out.println("To follow and update the delivery enter 4");
    }

    @Override
    public View nextInput(String input) {
        switch(input){
            case "back":
                return deliveryViewModel.getAllDeliveriesView();
            case "1":
                return deliveryViewModel.getTruckView(deliveryViewModel.getTruckLicense());
            case "2":
                return deliveryViewModel.getDriverView(deliveryViewModel.getDriverId());
            case "3":
                return deliveryViewModel.getAllDocsForDeliveryView(deliveryViewModel.getId());
            case "4":
                return deliveryViewModel.FollowAndUpdateDelivery();
            case "close":
                ApplicationView.shouldTerminate = true;
                break;
            default:
                System.out.println("Invalid Input");
        }
        return this;
    }

}
