package Presentation.View.SupplierView;

import Presentation.Model.BackendController;
import Presentation.View.ApplicationView;
import Presentation.View.EmployeeModuleView.BranchSelectView;
import Presentation.View.EmployeeModuleView.MessagesView;
import Presentation.View.EmployeeModuleView.SelectionView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.MainMenuViewModel;

public class SupplierMainMenuView implements View {

    public SupplierMainMenuView() { }

    @Override
    public void printMenu() {
        System.out.println("-----------------Supplier Main Menu-----------------");
        System.out.println("1. View the suppliers menu");
        System.out.println("2. View the orders menu");
        System.out.println("3. See warehouse messages");
        System.out.println("4. Back");
        System.out.println("5. Logout");
    }

    @Override
    public View nextInput(String input) {
        switch (input) {
            case "1":
                return new SupplierMenuView();
            case "2":
                return new OrderMenuView();
            case "3":
                return new MessagesView("warehouse",BackendController.getInstance().getLoggedInUser().getBranch(), new SupplierMainMenuView());
            case "4":
            case "back":
                if(!BackendController.getInstance().getLoggedInUser().isBranchManager())
                    return new BranchSelectView(BackendController.getInstance().getLoggedInUser().getID(), "Supplier");
                else
                    return new SelectionView(new MainMenuViewModel(String.valueOf(BackendController.getInstance().getLoggedInUser().getID())));
            case "5":
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
