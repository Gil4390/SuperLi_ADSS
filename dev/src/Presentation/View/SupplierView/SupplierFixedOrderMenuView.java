package Presentation.View.SupplierView;

import Presentation.Main;
import Presentation.Model.SupplierModel.SupplierModel;
import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.SupplierViewModel.SupplierFixedOrderViewModel;

public class SupplierFixedOrderMenuView implements View {
    private final SupplierFixedOrderViewModel supplierFixedOrderViewModel;

    public SupplierFixedOrderMenuView(SupplierModel supplierModel) {
        this.supplierFixedOrderViewModel = new SupplierFixedOrderViewModel(supplierModel);
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------Fixed Orders Menu-----------------");
        System.out.println("1- View all fixed orders");
        System.out.println("2- Add a fixed order to this supplier");
        System.out.println("3- Un active fixed order");
        System.out.println("4- Add supply day to fixed order");
        System.out.println("5- Remove supply fixed order");
        System.out.println("6- Add products to fixed order");
        System.out.println("7- Remove products from fixed order");
        System.out.println("8- Update product of fixed order");
        System.out.println("9- Back");
        System.out.println("10- Logout");
    }

    @Override
    public View nextInput(String input) {
        switch (input) {
            case "1":
                return supplierFixedOrderViewModel.viewFixedOrders();
            case "2":
                return supplierFixedOrderViewModel.addFixedOrder();
            case "3":
                return supplierFixedOrderViewModel.unActiveFixedOrder();
            case "4":
                return supplierFixedOrderViewModel.addSupplyDayToFixedOrder();
            case "5":
                return supplierFixedOrderViewModel.removeSupplyDayFromFixedOrder();
            case "6":
                return supplierFixedOrderViewModel.addProductToFixedOrder();
            case "7":
                return supplierFixedOrderViewModel.removeProductFromFixedOrder();
            case "8":
                return supplierFixedOrderViewModel.updateProductOfFixedOrder();
            case "back":
            case "9":
                return supplierFixedOrderViewModel.backToSupplierView();
            case "10":
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
