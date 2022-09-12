package Presentation.View.SupplierView;

import Presentation.View.EmployeeModuleView.SelectionView;
import Presentation.View.View;
import Presentation.ViewModel.SupplierViewModel.UnSuppliedOrdersViewModel;
import Presentation.ViewModel.MainMenuViewModel;

public class UnSuppliedOrdersView implements View {
    private final UnSuppliedOrdersViewModel unSuppliedOrdersViewModel;
    private final MainMenuViewModel mainMenuViewModel;

    public UnSuppliedOrdersView(MainMenuViewModel mainMenuViewModel) {
        this.mainMenuViewModel = mainMenuViewModel;
        this.unSuppliedOrdersViewModel = new UnSuppliedOrdersViewModel();
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------Un Supplied Orders-----------------");
        System.out.print(unSuppliedOrdersViewModel.getUnSuppliedOrders());

    }

    @Override
    public View nextInput(String input) {
        unSuppliedOrdersViewModel.remove(input);
        return new SelectionView(mainMenuViewModel);
    }
}
