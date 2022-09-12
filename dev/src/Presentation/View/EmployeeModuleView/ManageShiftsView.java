package Presentation.View.EmployeeModuleView;

import Presentation.ViewModel.EmployeeModulViewModel.ManageShiftsViewModel;
import Presentation.View.View;

public class ManageShiftsView implements View {
    private final ManageShiftsViewModel manageShiftsViewModel;

    public ManageShiftsView(int input, String branchAddress) {
        this.manageShiftsViewModel = new ManageShiftsViewModel(input, branchAddress);
    }


    @Override
    public void printMenu() {
        System.out.println("-----------------Manage shifts Menu-----------------");
        System.out.println("1.Assign shift");
        System.out.println("2.Add employee to shift");
        System.out.println("3.Delete employee from shift");
        System.out.println("4.Delete shift");
        System.out.println("5.Print shifts");
        System.out.println("6.Available employees to shift");
        System.out.println("7.Back");
        System.out.println("8.Logout");

    }

    @Override
    public View nextInput(String input) {
        try {
            if (input.equals("1"))
                return manageShiftsViewModel.assignShift();
            else if (input.equals("2"))
                return manageShiftsViewModel.addEmployeeToShift();
            else if (input.equals("3"))
                return manageShiftsViewModel.removeEmployeeFromShift();
            else if (input.equals("4"))
                return manageShiftsViewModel.deleteShift();
            else if (input.equals("5"))
                return manageShiftsViewModel.printShift();
            else if (input.equals("6"))
                return manageShiftsViewModel.availableEmployees();
            else if (input.equals("7"))
                return manageShiftsViewModel.returnHRBranchSelectView();
            else if (input.equals("8"))
                return manageShiftsViewModel.returnMainMenuView();
            else
                return manageShiftsViewModel.returnManageShiftsView();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return manageShiftsViewModel.returnManageShiftsView();
        }
    }
}
