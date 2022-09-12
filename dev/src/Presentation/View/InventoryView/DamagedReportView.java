package Presentation.View.InventoryView;

import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.InventoryViewModel.DamagedItemViewModel;
import Presentation.ViewModel.InventoryViewModel.ShortageItemViewModel;
import Presentation.ViewModel.MainMenuViewModel;

public class DamagedReportView implements View {
    private final DamagedItemViewModel damagedItemViewModel;

    public DamagedReportView(){
        damagedItemViewModel= new DamagedItemViewModel();
    }
    @Override
    public void printMenu() {
        System.out.println("-----------------Damaged Report Menu-----------------");
        System.out.println("1. View damaged report");
        System.out.println("2. View damaged report by date");
        System.out.println("3. View damaged report by item");
        System.out.println("4. Add a damaged record");
        System.out.println("5. Back");
        System.out.println("6. Logout");
    }

    @Override
    public View nextInput(String input) {
        switch (input) {
            case "1":
                return damagedItemViewModel.getReport();
            case "2":
                return damagedItemViewModel.getReportByDate();
            case "3":
                return damagedItemViewModel.getReportByItemID();
            case "4":
                return damagedItemViewModel.addDamagedItem();
            case "5":
                return new ReportsMenuView();
            case "6":
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
