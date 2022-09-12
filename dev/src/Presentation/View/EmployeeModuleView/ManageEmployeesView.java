package Presentation.View.EmployeeModuleView;

import Presentation.ViewModel.EmployeeModulViewModel.ManageEmployeesViewModel;
import Presentation.View.View;

public class ManageEmployeesView implements View {
    private final ManageEmployeesViewModel manageEmployeesViewModel;

    public ManageEmployeesView(int input) {
        this.manageEmployeesViewModel = new ManageEmployeesViewModel(input);
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------Manage employees Menu-----------------");
        System.out.println("1.Add employee");
        System.out.println("2.Delete employee");
        System.out.println("3.Update employee");
        System.out.println("4.Add job title for employee");
        System.out.println("5.Add job Certification for employee(e.g Licences)");
        System.out.println("6.Remove job from employee");
        System.out.println("7.Print all employees");
        System.out.println("8.Print the employee's shifts from the last month");
        System.out.println("9.Back");
        System.out.println("10.Logout");
    }

    @Override
    public View nextInput(String input) {
        try {
            if(input.equals("1"))
                return manageEmployeesViewModel.addEmployee();
            else if(input.equals("2"))
                return manageEmployeesViewModel.deleteEmployee();
            else if(input.equals("3"))
                return manageEmployeesViewModel.returnUpdateEmployeeView();
            else if(input.equals("4"))
                return manageEmployeesViewModel.addJob();
            else if(input.equals("5"))
                return manageEmployeesViewModel.addJobCertToEmployee();
            else if(input.equals("6"))
                return manageEmployeesViewModel.removeJobFromEmployee();
            else if(input.equals("7"))
                return manageEmployeesViewModel.getAllEmployees();
            else if(input.equals("8"))
                return manageEmployeesViewModel.getEmployeeShiftLastMonth();
            else if(input.equals("9"))
                return manageEmployeesViewModel.returnHRManagerMenuView();
            else if(input.equals("10"))
                return manageEmployeesViewModel.returnMainMenuView();
            else
                return manageEmployeesViewModel.returnManageEmployeesView();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return manageEmployeesViewModel.returnManageEmployeesView();
        }
    }
}
