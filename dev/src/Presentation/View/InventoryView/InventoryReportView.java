package Presentation.View.InventoryView;

import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.InventoryViewModel.InventoryItemViewModel;
import Presentation.ViewModel.MainMenuViewModel;

public class InventoryReportView implements View {
    private final InventoryItemViewModel inventoryItemViewModel;

    public InventoryReportView(){
        inventoryItemViewModel = new InventoryItemViewModel();
    }

    @Override
    public void printMenu() {
        System.out.println("--------------Inventory Report Menu--------------");
        System.out.println("1. View Inventory Report");
        System.out.println("2. View Inventory by category Report");
        System.out.println("3. Back");
        System.out.println("4. Logout");
    }


    @Override
    public View nextInput(String input) {
        switch (input) {
            case "1":
                return inventoryItemViewModel.getReport();
            case "2":
                return inventoryItemViewModel.getReportByCategory();
            case "3":
                return new ReportsMenuView();
            case "4":
                return new MainMenuView(true);
            case "close":
                ApplicationView.shouldTerminate = true;
                break;
            default:
                System.out.println("Invalid input!");
        }
        return this;
    }
}