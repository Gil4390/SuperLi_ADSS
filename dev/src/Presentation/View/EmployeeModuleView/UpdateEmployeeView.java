package Presentation.View.EmployeeModuleView;

import Presentation.ViewModel.EmployeeModulViewModel.ManageEmployeesViewModel;
import Presentation.View.View;

public class UpdateEmployeeView implements View {
    private final ManageEmployeesViewModel manageEmployeesViewModel;

    public UpdateEmployeeView(int input) {
        this.manageEmployeesViewModel = new ManageEmployeesViewModel(input);

    }

    @Override
    public void printMenu() {
        System.out.println("-----------------Update employee Menu-----------------");
        System.out.println("1.Update name");
        System.out.println("2.Update back account details");
        System.out.println("3.Update salary");
        System.out.println("4.Update employment details");
        System.out.println("5.Back");
        System.out.println("6.Logout");
    }

    @Override
    public View nextInput(String input) {
        try {
            if (input.equals("1"))
                return manageEmployeesViewModel.updateEmployeeName();
            else if (input.equals("2"))
                return manageEmployeesViewModel.updateEmployeeBankAccount();
            else if (input.equals("3"))
                return manageEmployeesViewModel.updateEmployeeSalary();
            else if (input.equals("4"))
                return manageEmployeesViewModel.updateEmploymentDetails();
            else if (input.equals("5"))
                return manageEmployeesViewModel.returnManageEmployeesView();
            else if (input.equals("6"))
                return manageEmployeesViewModel.returnMainMenuView();
            else
                return manageEmployeesViewModel.returnUpdateEmployeeView();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return manageEmployeesViewModel.returnUpdateEmployeeView();
        }
    }
}
