package Presentation.ViewModel.EmployeeModulViewModel;



import Presentation.Model.BackendController;
import Presentation.Model.EmployeeModuleModel.EmployeeModel;
import Presentation.View.EmployeeModuleView.HRManagerMenuView;
import Presentation.View.EmployeeModuleView.ManageEmployeesView;
import Presentation.View.EmployeeModuleView.UpdateEmployeeView;
import Presentation.View.MainMenuView;
import Presentation.View.View;

import java.util.*;
import java.util.Scanner;

public class ManageEmployeesViewModel {
    private final BackendController backendController;

    private final int ID;

    public ManageEmployeesViewModel(int input) {
        this.backendController = BackendController.getInstance();
        this.ID  = input;
    }

    public View addEmployee() {
        System.out.println("To add new employee , please enter: Name, ID, Bank account details, Salary, Start Date, job, employmentDetails (can be empty)");
        System.out.println("For Example: Tal Galmor,318416575,Leumi xxx xxxxxx,1200, 2021-12-30,shift manager,2 year contract,0544363655");
        Scanner in = new Scanner(System.in);
        String info = in.nextLine();
        String [] Info = info.split(",");
        System.out.println("please enter the certification of the employee (can be empty)");
        String certifications = in.nextLine();
        Vector<String> Certifications = new Vector<>(Arrays.asList(certifications.split(",")));
        if (Info.length==7) {
            if (certifications.isEmpty())
                backendController.addNewEmployee(Info[0], Integer.parseInt(Info[1]), Info[2], Integer.parseInt(Info[3]), Info[4], Info[5], Info[6],Info[7]);
            else backendController.addNewEmployee(Info[0], Integer.parseInt(Info[1]), Info[2], Integer.parseInt(Info[3]), Info[4], Info[5], Info[6], Certifications,Info[7]);
        }
        else{
            if (certifications.isEmpty())
            backendController.addNewEmployee(Info[0],Integer.parseInt(Info[1]),Info[2],Integer.parseInt(Info[3]), Info[4],Info[5], "",Info[7]);
            else backendController.addNewEmployee(Info[0], Integer.parseInt(Info[1]), Info[2], Integer.parseInt(Info[3]), Info[4], Info[5], "", Certifications,Info[7]);
        }
        return new ManageEmployeesView(ID);
    }

    public View deleteEmployee() {
        System.out.println("To delete employee , please enter the employee's ID");
        Scanner in = new Scanner(System.in);
        String IdToDelete = in.nextLine();
        backendController.deleteEmployee(Integer.parseInt(IdToDelete));
        return new ManageEmployeesView(ID);
    }

    public View addJob() {
        System.out.println("To add job to employee , please enter: the employee's ID,new job title");
        System.out.println("For Example: 318416575,shift manager");
        Scanner in = new Scanner(System.in);
        String info = in.nextLine();
        String [] Info = info.split(",");
        backendController.addJobTitleToEmployee(Integer.parseInt(Info[0]),Info[1]);
        return new ManageEmployeesView(ID);
    }

    public View addJobCertToEmployee() {
        System.out.println("To add job Certification to employee , please enter: the employee's ID,the job type and the name of the certification");
        System.out.println("For Example: 12345,driver,D2");
        Scanner in = new Scanner(System.in);
        String info = in.nextLine();
        String [] Info = info.split(",");
        backendController.addJobCertToEmployee(Integer.parseInt(Info[0]),Info[1],Info[3]);
        return new ManageEmployeesView(ID);
    }

    public View updateEmployeeName() {
        System.out.println("To update employee , please enter: the employee's ID,new name");
        System.out.println("For Example: 318416575,Gal Talmor");
        return updateEmployee("name");
    }

    public View updateEmployeeBankAccount() {
        System.out.println("To update employee , please enter: the employee's ID,new bank account details");
        System.out.println("For Example: 318416575,Gal Talmor");
        return updateEmployee("bank account");
    }
    public View updateEmploymentDetails() {
        System.out.println("To update employee , please enter: the employee's ID,new employmentDetails");
        System.out.println("For Example: 318416575,Gal Talmor");
        return updateEmployee("employment details");
    }
    public View updateEmployeeSalary() {
        System.out.println("To update employee , please enter: the employee's ID,new salary");
        System.out.println("For Example: 318416575,Gal Talmor");
        return updateEmployee("salary");
    }
    public View updateEmployee(String updatedInfo)
    {
        Scanner in = new Scanner(System.in);
        String info = in.nextLine();
        String [] Info = info.split(",");
        backendController.updateEmployee(Integer.parseInt(Info[0]),Info[1],updatedInfo);
        System.out.println("Employee updated");
        return new ManageEmployeesView(ID);
    }
    public View removeJobFromEmployee() {
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter the ID of the employee and the job you want to remove");
        System.out.println("For example , 100:shift manager");
        String info = in.nextLine();
        String [] Info = info.split(":");
        backendController.removeJobTitleFromEmployee(Integer.parseInt(Info[0]),Info[1]);
        return new ManageEmployeesView(ID);
    }

    public View getAllEmployees() {
        System.out.println("Employees in DB:");
        for (EmployeeModel e: backendController.getAllEmployees())
            System.out.println(e);
        return new ManageEmployeesView(ID);
    }
    public View getEmployeeShiftLastMonth()
    {
        System.out.println("Please enter the ID of the employee who would like to see his shifts, and the type of shift ");
        System.out.println("For example: 31841657,morning");
        Scanner in = new Scanner(System.in);
        String info = in.nextLine();
        String [] Info = info.split(",");
        backendController.getShiftLastMonth(Info[1],Integer.parseInt(Info[0]));
        return new ManageEmployeesView(ID);
    }

    public View returnUpdateEmployeeView() {
        return new UpdateEmployeeView(ID);
    }

    public View returnHRManagerMenuView() {
        return new HRManagerMenuView(ID, backendController.getAddressOfHQ());
    }

    public View returnMainMenuView() {
        return new MainMenuView(false);
    }

    public View returnManageEmployeesView() {
        return new ManageEmployeesView(ID);
    }


}
