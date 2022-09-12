package Presentation.View.InventoryView;


import Presentation.Model.BackendController;
import Presentation.View.ApplicationView;
import Presentation.View.EmployeeModuleView.BranchSelectView;
import Presentation.View.EmployeeModuleView.SelectionView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.MainMenuViewModel;

public class ReportsMenuView implements View {

    public ReportsMenuView() {
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------Report Menu-----------------");
        System.out.println("1. View inventory report");
        System.out.println("2. View shortage report");
        System.out.println("3. View & add Damaged report");
        System.out.println("4. View & add Purchased report");
        System.out.println("5. Back");
        System.out.println("6. Logout");
    }


    @Override
    public View nextInput(String input) {
        switch (input) {
            case "1":
                return new InventoryReportView();
            case "2":
                return new ShortageReportView();
            case "3":
                return new DamagedReportView();
            case "4":
                return new PurchasedReportView();
            case "5":
                if(!BackendController.getInstance().getLoggedInUser().isBranchManager())
                    return new BranchSelectView(BackendController.getInstance().getLoggedInUser().getID(), "Inventory");
                else
                    return new SelectionView(new MainMenuViewModel(String.valueOf(BackendController.getInstance().getLoggedInUser().getID())));
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
