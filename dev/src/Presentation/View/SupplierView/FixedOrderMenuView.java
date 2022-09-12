package Presentation.View.SupplierView;

import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.SupplierViewModel.FixedOrderMenuViewModel;

public class FixedOrderMenuView implements View {
    private final FixedOrderMenuViewModel fixedOrderMenuViewModel;

    public FixedOrderMenuView() {
        this.fixedOrderMenuViewModel = new FixedOrderMenuViewModel();
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------Fixed Orders Menu-----------------");
        System.out.println("1. View all fixed orders");
        System.out.println("2. Add fixed order");
        System.out.println("3. Create fixed orders for next week");
        System.out.println("4. Back");
        System.out.println("5. Logout");
    }

    @Override
    public View nextInput(String input) {
        switch (input) {
            case "1":
                return fixedOrderMenuViewModel.viewFixedOrders();
            case "2":
                return fixedOrderMenuViewModel.addFixedOrder();
            case "3":
                return fixedOrderMenuViewModel.createNextWeekFixedOrders();
            case "4":
            case "back":
                return new OrderMenuView();
            case "5":
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
