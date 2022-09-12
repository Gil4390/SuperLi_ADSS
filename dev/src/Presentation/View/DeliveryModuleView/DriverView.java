package Presentation.View.DeliveryModuleView;

import Presentation.View.View;
import Presentation.ViewModel.DeliveryModulViewModel.DriverViewModel;
import Presentation.View.ApplicationView;

public class DriverView implements View {
    private DriverViewModel driverViewModel;

    public DriverView(int driverId) {
        this.driverViewModel = new DriverViewModel(driverId);
    }

    @Override
    public void printMenu() {
        System.out.println("----------------- DRIVER: "+driverViewModel.getName()+"-----------------");
        System.out.println("         ID: "+ driverViewModel.getId());
        System.out.println("         DRIVER LICENSES: "+ driverViewModel.getDriverLicenses());
    }

    @Override
    public View nextInput(String input) {
        switch (input) {
            case "back":
                return driverViewModel.getDeliveryMenuView();
            case "close":
                ApplicationView.shouldTerminate = true;
                break;
            default:
                System.out.println("Invalid input!");
        }
        return this;
    }
}
