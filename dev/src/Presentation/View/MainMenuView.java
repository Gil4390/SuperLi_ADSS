package Presentation.View;

import Presentation.Model.BackendController;
import Presentation.View.EmployeeModuleView.SelectionView;
import Presentation.ViewModel.MainMenuViewModel;

public class MainMenuView implements View {
    private boolean initial;
    public MainMenuView(boolean init) {
        BackendController.getInstance().logout();
        this.initial = init;
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------Main Menu-----------------");
        System.out.println("Enter your ID for sign in");
        if (this.initial) {
            System.out.println("Enter load for loading fake data");
            System.out.println("1. Upgraded To Premium Version");
        }
    }

    @Override
    public View nextInput(String input) {
        if(input.equals("1"))
            return new PremiumView();
        if (input.equals("close")) {
            ApplicationView.shouldTerminate = true;
            return this;
        }
        else if (input.equals("load")) {
            BackendController.getInstance().loadDataForTests();
            return this;
        }
        else {
            try {
                if (BackendController.getInstance().getEmployeeByID(Integer.parseInt(input)) != null) {
                    return new SelectionView(new MainMenuViewModel(input));
                }
            }catch (Exception ignored){}
            System.out.println("Employee doesn't exist, please enter again");
            return new MainMenuView(false);
        }
    }
}
