package Presentation.View.EmployeeModuleView;

import Presentation.Model.BackendController;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.MainMenuViewModel;

public class SelectionView implements View {
    private MainMenuViewModel mainMenuViewModel;

    public SelectionView(MainMenuViewModel m)
    {
        this.mainMenuViewModel = m;
    }
    @Override
    public void printMenu() {
        System.out.println("-----------------Select Menu-----------------");
        for(String opt : mainMenuViewModel.getPrintMenuOptions())
            System.out.println(opt);
    }

    @Override
    public View nextInput(String input) {
        try
        {
            if (!mainMenuViewModel.isEmployeeExist()) {
                System.out.println("Invalid input, please try again!");
                return new SelectionView(mainMenuViewModel);
            }
            if(mainMenuViewModel.getIndex()==0) {
                return new EmployeeMenuView(Integer.parseInt(input));
            }
            if (!mainMenuViewModel.getViewOptions().containsKey(Integer.parseInt(input))) {
                System.out.println("Invalid Id, Please Try Again!");
                return new MainMenuView(false);
            }
            else {
                if (mainMenuViewModel.getViewOptions().get(Integer.parseInt(input)) == null) {
                    BackendController.getInstance().logout();
                    return new MainMenuView(false);
                }
                else return mainMenuViewModel.getViewOptions().get(Integer.parseInt(input));
            }
        } catch (Exception e) {
            System.out.println("Invalid Id, Please Try Again!");
            return new SelectionView(mainMenuViewModel);
        }
    }
}
