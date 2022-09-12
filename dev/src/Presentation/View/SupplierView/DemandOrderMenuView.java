package Presentation.View.SupplierView;

import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.MainMenuViewModel;
import Presentation.ViewModel.SupplierViewModel.DemandOrderMenuViewModel;

public class DemandOrderMenuView implements View {
    private final DemandOrderMenuViewModel demandOrderMenuViewModel;

    public DemandOrderMenuView() {
        this.demandOrderMenuViewModel = new DemandOrderMenuViewModel();
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------Demand Orders Menu-----------------");
        System.out.println("1. View all demand orders");
        System.out.println("2. Add demand order");
        System.out.println("3. Back");
        System.out.println("4. Logout");
    }

    @Override
    public View nextInput(String input) {
        switch (input) {
            case "1":
                return demandOrderMenuViewModel.viewDemandOrders();
            case "2":
                return demandOrderMenuViewModel.addDemandOrder();
            case "3":
                return new OrderMenuView();
            case "4":
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
