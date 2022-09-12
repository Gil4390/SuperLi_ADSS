package Presentation.View.DeliveryModuleView;

import Presentation.View.View;
import Presentation.ViewModel.DeliveryModulViewModel.TruckViewModel;
import Presentation.View.ApplicationView;

public class TruckView implements View {
    private TruckViewModel truckViewModel;

    public TruckView(String truckNumber) {
        this.truckViewModel = new TruckViewModel(truckNumber);
    }


    @Override
    public void printMenu() {
        System.out.println("----------------- TRUCK LICENSE PLAIT "+truckViewModel.getLicenseNumber()+"-----------------");
        System.out.println("         MODEL: "+ truckViewModel.getModel());
        System.out.println("         BASE WEIGHT: "+ truckViewModel.getBaseWeight());
        System.out.println("         MAXWEIGHT: "+ truckViewModel.getMaxWeight());
    }

    @Override
    public View nextInput(String input) {
        switch (input) {
            case "back":
                return truckViewModel.getDeliveryMenuView();
            case "close":
                ApplicationView.shouldTerminate = true;
                break;
            default:
                System.out.println("Invalid input!");
        }
        return this;
    }
}
