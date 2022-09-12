package Presentation.View.EmployeeModuleView;

import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.EmployeeModulViewModel.ManageDriversShiftsViewModel;

public class ManageDriversShiftsView implements View {
    private final int ID;
    private final ManageDriversShiftsViewModel manageDriversShiftsViewModel;

    private String aHQ;
    public ManageDriversShiftsView(int id, String addressOfHQ) {
        this.ID = id;
        this.manageDriversShiftsViewModel = new ManageDriversShiftsViewModel(id , addressOfHQ);
        this.aHQ = addressOfHQ;
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------Manage driver's shifts Menu-----------------");
        System.out.println("1.Assign driver to shift");
        System.out.println("2.Remove driver from shift");
        System.out.println("3.Print driver's shifts");
        System.out.println("4.Back");
        System.out.println("5.Logout");

    }

    @Override
    public View nextInput(String input) {
        try {
            if(input.equals("1"))
                return manageDriversShiftsViewModel.AssignDriver();
            else if(input.equals("2"))
                return manageDriversShiftsViewModel.RemoveDriver();
            else if(input.equals("3"))
                return manageDriversShiftsViewModel.PrintEmployeeShift();
            else if(input.equals("4"))
                return manageDriversShiftsViewModel.returnHRManagerMenuView();
            else if(input.equals("5"))
                return new MainMenuView(false);
            else return new ManageDriversShiftsView(ID,aHQ);

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return manageDriversShiftsViewModel.returnHRManagerMenuView();
        }
    }
}
