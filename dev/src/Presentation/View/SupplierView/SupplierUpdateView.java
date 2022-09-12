package Presentation.View.SupplierView;

import Presentation.Model.SupplierModel.SupplierModel;
import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.SupplierViewModel.SupplierViewModel;

public class SupplierUpdateView implements View {
    private final SupplierViewModel supplierViewModel;
    private final SupplierModel supplierModel;

    public SupplierUpdateView(SupplierViewModel supplierViewModel, SupplierModel supplierModel) {
        this.supplierViewModel = supplierViewModel;
        this.supplierModel = supplierModel;
    }

    @Override
    public void printMenu() {
        System.out.println(supplierModel.toString());
        System.out.println("1- Update this supplier name");
        System.out.println("2- Update this supplier bank account number");
        System.out.println("3- Update this supplier delivery method");
        System.out.println("4- Update this supplier payment method");
        System.out.println("5- Back");
        System.out.println("6- Logout");
    }

    @Override
    public View nextInput(String input) {
        switch (input) {
            case "back":
            case "5":
                return new SupplierView(supplierModel);
            case "1":
                return supplierViewModel.updateName();
            case "2":
                return supplierViewModel.updateBankAccount();
            case "3":
                return supplierViewModel.updateDeliveryMethod();
            case "4":
                return supplierViewModel.updatePaymentMethod();
            case "6":
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
