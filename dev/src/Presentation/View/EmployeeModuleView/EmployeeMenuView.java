package Presentation.View.EmployeeModuleView;

import Presentation.ViewModel.EmployeeModulViewModel.EmployeeMenuViewModel;
import Presentation.View.View;

public class EmployeeMenuView implements View {
    private final EmployeeMenuViewModel employeeMenuViewModel;


    public EmployeeMenuView(int input) {
        this.employeeMenuViewModel = new EmployeeMenuViewModel(input);
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------Employee Menu-----------------");
        System.out.println("1.Print shifts");
        System.out.println("2.Add job constraint");
        System.out.println("3.Logout");
    }

    @Override
    public View nextInput(String input) {
        try {
            if (input.equals("1"))
                return employeeMenuViewModel.printShifts();
            else if (input.equals("2"))
                return employeeMenuViewModel.addJobConstraint();
            else if (input.equals("3"))
                return employeeMenuViewModel.returnMainMenuView();
            else
                return employeeMenuViewModel.returnEmployeeMenuView();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return employeeMenuViewModel.returnEmployeeMenuView();
        }

    }
}
