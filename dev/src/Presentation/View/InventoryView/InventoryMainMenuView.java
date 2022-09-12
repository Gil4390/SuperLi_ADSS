package Presentation.View.InventoryView;

import Presentation.Model.BackendController;
import Presentation.View.ApplicationView;
import Presentation.View.EmployeeModuleView.BranchSelectView;
import Presentation.View.EmployeeModuleView.MessagesView;
import Presentation.View.MainMenuView;
import Presentation.View.SupplierView.SupplierMainMenuView;
import Presentation.View.View;

public class InventoryMainMenuView implements View {
    public InventoryMainMenuView() { }

    @Override
    public void printMenu() {
        System.out.println("-----------------Inventory Main Menu-----------------");
        System.out.println("1. View the Item menu");
        System.out.println("2. View the Category menu");
        System.out.println("3. View the Shelves menu");
        System.out.println("4. View the Reports maker menu(Inventory, Shortages, Purchases, Damages) and add purchase/damaged records");
        System.out.println("5. See warehouse messages");
        System.out.println("6. Back");
        System.out.println("7. Logout");
    }


    @Override
    public View nextInput(String input) {
        switch (input) {
            case "1":
                return new ItemMenuView();
            case "2":
                return new CategoryMenuView();
            case "3":
                return new ShelvesMenuView();
            case "4":
                return new ReportsMenuView();
            case "5":
                return new MessagesView("warehouse",BackendController.getInstance().getLoggedInUser().getBranch(), new InventoryMainMenuView());

            case "6":
                return new BranchSelectView(BackendController.getInstance().getLoggedInUser().getID(), "Supplier");
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
