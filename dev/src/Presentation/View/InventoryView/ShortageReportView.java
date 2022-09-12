package Presentation.View.InventoryView;

import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.InventoryViewModel.InventoryItemViewModel;
import Presentation.ViewModel.InventoryViewModel.ShortageItemViewModel;
import Presentation.ViewModel.MainMenuViewModel;

public class ShortageReportView implements View {
    private final ShortageItemViewModel shortageItemViewModel;


    public  ShortageReportView(){
        shortageItemViewModel = new ShortageItemViewModel();
    }

    @Override
    public void printMenu() {
        System.out.println("--------------Shortage report Menu--------------");
        System.out.println("1. View shortage report");
        System.out.println("2. Order the items in shortage");
        System.out.println("3. Back");
        System.out.println("4. Logout");
    }

    @Override
    public View nextInput(String input) {
        switch (input) {
            case "1":
                return shortageItemViewModel.getReport();
            case "2":
                return shortageItemViewModel.orderShortage();
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
