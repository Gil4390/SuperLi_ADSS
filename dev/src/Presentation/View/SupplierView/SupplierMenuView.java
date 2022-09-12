package Presentation.View.SupplierView;

import Presentation.View.ApplicationView;
import Presentation.View.InventoryView.InventoryMainMenuView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.MainMenuViewModel;
import Presentation.ViewModel.SupplierViewModel.SupplierMenuViewModel;

public class SupplierMenuView implements View {
    private SupplierMenuViewModel supplierMenuViewModel;

    public SupplierMenuView() {
        this.supplierMenuViewModel = new SupplierMenuViewModel();
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------Supplier Menu-----------------");
        System.out.println("1. View a supplier card");
        System.out.println("2. Add a supplier");
        System.out.println("3. Remove a supplier");
        System.out.println("4. Back");
        System.out.println("5. Logout");
    }

    @Override
    public View nextInput(String input) {
        switch (input){
            case "1":
                return supplierMenuViewModel.enterSupplierCard();
            case "2":
                return supplierMenuViewModel.addSupplier();
            case "3":
                return supplierMenuViewModel.removeSupplier();
            case "4":
            case "back":
                return new SupplierMainMenuView();
            case"5":
                return new MainMenuView(true);
            case "close":
                ApplicationView.shouldTerminate = true;
                break;
            default:
                System.out.println("Invalid Input");
        }
        return this;
    }
}
