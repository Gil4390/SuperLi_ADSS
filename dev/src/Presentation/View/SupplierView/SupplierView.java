package Presentation.View.SupplierView;

import Presentation.Model.SupplierModel.SupplierModel;
import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.SupplierViewModel.SupplierViewModel;

public class SupplierView implements View {
    private final SupplierViewModel supplierViewModel;

    public SupplierView(SupplierModel supplierModel) {
        supplierViewModel = new SupplierViewModel(supplierModel);
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------"+supplierViewModel.getName()+" supplier-----------------");
        System.out.println("1- Update this supplier details");
        System.out.println("2- View Bill of quantity");
        System.out.println("3- View demand orders");
        System.out.println("4- View fixed orders");
        System.out.println("5- View a contact of this supplier");
        System.out.println("6- Add a contact to this supplier");
        System.out.println("7- Remove a contact from this supplier");
        System.out.println("8- Back");
        System.out.println("9- Logout");
    }

    @Override
    public View nextInput(String input) {
        switch(input){
            case "back":
            case "8":
                return new SupplierMenuView();
            case "1":
                return new SupplierUpdateView(supplierViewModel, supplierViewModel.getSupplierModel());
            case "2":
                return new BillOfQuantityView(supplierViewModel.getSupplierModel());
            case "3":
                return new SupplierDemandOrderMenuView(supplierViewModel.getSupplierModel());
            case "4":
                return new SupplierFixedOrderMenuView(supplierViewModel.getSupplierModel());
            case "5":
                return supplierViewModel.enterContact();
            case "6":
                return supplierViewModel.addContact();
            case "7":
                return supplierViewModel.removeContact();
            case "9":
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
