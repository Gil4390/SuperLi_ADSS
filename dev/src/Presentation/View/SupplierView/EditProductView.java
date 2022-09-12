package Presentation.View.SupplierView;

import Presentation.Model.SupplierModel.SupplierModel;
import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.SupplierViewModel.BillOfQuantityViewModel;

public class EditProductView implements View {
    private int catalogNumber;
    private final BillOfQuantityViewModel billOfQuantityViewModel;
    private final SupplierModel supplierModel;

    public EditProductView(int catalogNumber, BillOfQuantityViewModel billOfQuantityViewModel, SupplierModel supplierModel) {
        this.catalogNumber = catalogNumber;
        this.billOfQuantityViewModel = billOfQuantityViewModel;
        this.supplierModel = supplierModel;
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------"+supplierModel.getName()+" product editing-----------------");
        System.out.println("1- Update this product price");
        System.out.println("2- Update this product name");
        System.out.println("3- Update the product supplier catalog number");
        System.out.println("4- Update a discount for this product");
        System.out.println("5- Back");
        System.out.println("6- Logout");
    }

    @Override
    public View nextInput(String input) {
        switch(input) {
            case "back":
            case "5":
                return new BillOfQuantityView(supplierModel);
            case "1":
                return billOfQuantityViewModel.updatePrice(catalogNumber);
            case "2":
                return billOfQuantityViewModel.updateName(catalogNumber);
            case "3":
                return billOfQuantityViewModel.updateSupplierCatalog(catalogNumber);
            case "4":
                return billOfQuantityViewModel.updateDiscount(catalogNumber);
            case "6":
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
