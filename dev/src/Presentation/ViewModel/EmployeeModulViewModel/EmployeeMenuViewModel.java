package Presentation.ViewModel.EmployeeModulViewModel;

import Presentation.Model.BackendController;
import Presentation.Model.EmployeeModuleModel.ShiftModel;
import Presentation.View.EmployeeModuleView.EmployeeMenuView;
import Presentation.View.MainMenuView;
import Presentation.View.View;

import java.util.List;
import java.util.Scanner;

public class EmployeeMenuViewModel {
    private final BackendController backendController;
    private final int ID;


    public EmployeeMenuViewModel(int input) {
        this.backendController = BackendController.getInstance();
        this.ID = input;
    }
    public View invalidInput()
    {
        System.out.println("Invalid input!");
        return new EmployeeMenuView(ID);
    }

    public View printShifts()
    {
        List<ShiftModel> employeesShifts = backendController.getEmployeeShifts(ID);
        if (employeesShifts.isEmpty())
            System.out.println("The employee is not assigned to any shift");
        else
        {
            for (ShiftModel sm :employeesShifts)
                System.out.println(sm.toString());
        }
        return new EmployeeMenuView(ID);
    }
    public View addJobConstraint()
    {
        try {
            System.out.println("Please enter your new constraint (YYYY-MM-DD,Morning/Evening)");
            Scanner in = new Scanner(System.in);
            String info = in.nextLine();
            String [] Info = info.split(",");
            backendController.addJobConstraint(ID, Info[0], Info[1].toLowerCase());
            return new EmployeeMenuView(ID);
        }
        catch (Exception e)
        {
            return invalidInput();
        }

    }

    public View returnEmployeeMenuView() {
        return new EmployeeMenuView(ID);
    }

    public View returnMainMenuView() {
        return new MainMenuView(false);
    }
}
