package Presentation.View.SupplierView;

import Presentation.Model.SupplierModel.SupplierModel;
import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.SupplierViewModel.BillOfQuantityViewModel;

public class BillOfQuantityView implements View {
    private final BillOfQuantityViewModel billOfQuantityViewModel;
    private final SupplierModel supplierModel;

    public BillOfQuantityView(SupplierModel supplierModel) {
        this.billOfQuantityViewModel = new BillOfQuantityViewModel(supplierModel);
        this.supplierModel = supplierModel;
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------"+supplierModel.getName()+" bill of quantity-----------------");
        System.out.println("1- View the Products list");
        System.out.println("2- Add new product");
        System.out.println("3- Delete a product");
        System.out.println("4- Edit a product");
        System.out.println("5- Add discount to a product");
        System.out.println("6- Remove a discount from product");
        System.out.println("7- Back");
        System.out.println("8- Logout");
    }

    @Override
    public View nextInput(String input) {
        switch(input) {
            case "7":
            case "back":
                return new SupplierView(supplierModel);
            case "1":
                return billOfQuantityViewModel.productsList();
            case "2":
                return billOfQuantityViewModel.addProduct();
            case "3":
                return billOfQuantityViewModel.removeProduct();
            case "4":
                return billOfQuantityViewModel.editProduct();
            case "5":
                return billOfQuantityViewModel.addProductDiscount();
            case "6":
                return billOfQuantityViewModel.removeProductDiscount();
            case "8":
                return new MainMenuView(true);
            case "close":
                ApplicationView.shouldTerminate = true;
                break;
            default:
                System.out.println("Invalid input");
        }
        return this;
    }
}
