package Presentation.View.SupplierView;

import Presentation.Model.SupplierModel.SupplierModel;
import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.SupplierViewModel.SupplierDemandOrderViewModel;

public class SupplierDemandOrderMenuView implements View {
    private final SupplierDemandOrderViewModel supplierDemandOrderViewModel;

    public SupplierDemandOrderMenuView(SupplierModel supplierModel) {
        this.supplierDemandOrderViewModel = new SupplierDemandOrderViewModel(supplierModel);
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------Demand Orders Menu-----------------");
        System.out.println("1- View all demand orders");
        System.out.println("2- Add an order by demand");
        System.out.println("3- Mark an order by demand as supplied");
        System.out.println("4- Delete an un supplied order by demand");
        System.out.println("5- Add products to demand order");
        System.out.println("6- Remove products from demand order");
        System.out.println("7- Update product of demand order");
        System.out.println("8- Send demand order to supplier");
        System.out.println("9- Back");
        System.out.println("10- Logout");
    }

    @Override
    public View nextInput(String input) {
        switch (input) {
            case "1":
                return supplierDemandOrderViewModel.viewDemandOrders();
            case "2":
                return supplierDemandOrderViewModel.addDemandOrder();
            case "3":
                return supplierDemandOrderViewModel.supplyDemandOrder();
            case "4":
                return supplierDemandOrderViewModel.removeUnSuppliedDemandOrder();
            case "5":
                return supplierDemandOrderViewModel.addProductToDemandOrder();
            case "6":
                return supplierDemandOrderViewModel.removeProductFromDemandOrder();
            case "7":
                return supplierDemandOrderViewModel.updateProductOfDemandOrder();
            case "8":
                return supplierDemandOrderViewModel.sendDemandOrderPDF();
            case "back":
            case "9":
                return supplierDemandOrderViewModel.backToSupplierView();
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
