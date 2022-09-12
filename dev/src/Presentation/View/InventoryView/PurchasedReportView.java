package Presentation.View.InventoryView;

import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.InventoryViewModel.DamagedItemViewModel;
import Presentation.ViewModel.InventoryViewModel.PurchasedItemViewModel;
import Presentation.ViewModel.MainMenuViewModel;

public class PurchasedReportView implements View {
    private final PurchasedItemViewModel purchasedItemViewModel;

    public PurchasedReportView(){
        purchasedItemViewModel = new PurchasedItemViewModel();
    }
    @Override
    public void printMenu() {
        System.out.println("-----------------Purchases-----------------");
        System.out.println("1. View all purchase records");
        System.out.println("2. View purchase records in specific dates");
        System.out.println("3. View purchase records for a specific item");
        System.out.println("4. View purchase records from a specific supplier");
        System.out.println("5. Add a purchase record");
        System.out.println("6. Back");
        System.out.println("7. Logout");
    }


    @Override
    public View nextInput(String input) {
        switch (input) {
            case "1":
                return purchasedItemViewModel.getReport();
            case "2":
                return purchasedItemViewModel.getReportByDate();
            case "3":
                return purchasedItemViewModel.getReportByItemID();
            case "4":
                return purchasedItemViewModel.getPurchaseReportByBusinessNumber();
            case "5":
                return purchasedItemViewModel.addPurchasedItem();
            case "6":
                return new ReportsMenuView();
            case "7":
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
