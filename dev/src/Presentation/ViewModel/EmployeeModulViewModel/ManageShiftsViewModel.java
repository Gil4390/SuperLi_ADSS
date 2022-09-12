package Presentation.ViewModel.EmployeeModulViewModel;



import Presentation.Model.BackendController;
import Presentation.Model.EmployeeModuleModel.EmployeeModel;
import Presentation.Model.EmployeeModuleModel.PositionsShifts;
import Presentation.Model.EmployeeModuleModel.ShiftModel;
import Presentation.View.EmployeeModuleView.BranchSelectView;
import Presentation.View.EmployeeModuleView.HRManagerMenuView;
import Presentation.View.EmployeeModuleView.ManageShiftsView;
import Presentation.View.MainMenuView;
import Presentation.View.View;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;

public class ManageShiftsViewModel {
    private final BackendController backendController;
    private final int ID;
    private final PositionsShifts positionsShifts = new PositionsShifts();
    private final String branchAdrress;

    public ManageShiftsViewModel(int input, String branchAdrress) {
        this.backendController = BackendController.getInstance();
        this.ID = input;
        this.branchAdrress = branchAdrress;
    }
    public View invalidInput()
    {
        System.out.println("Invalid input!");
        return new ManageShiftsView(ID, branchAdrress);
    }

    public View assignShift() {
        System.out.println("To assign new shift , please enter: YYYY-MM-DD,Morning/Evening");
        System.out.println("For Example: 2025-05-06,morning");
        Scanner in = new Scanner(System.in);
        String info = in.nextLine();
        String [] Infos = info.split(",");
        LocalDate shiftDate = backendController.validDate(Infos[0]);
        String shiftTime = Infos[1].toLowerCase();
        if (!shiftTime.equals("morning") & !shiftTime.equals("evening") )
            if (shiftDate==null)
                invalidInput();
        System.out.println("Now please enter the ID of the employees you would like to assign to each shift position");
        System.out.print("All positions available is ");
        positionsShifts.printPositions();
        System.out.println("You must assign at least 1 warehouse, 1 usher , 1 cashier and 1 shift manager(can be shift manager and also anthoer job)");
        System.out.println("For example: shift manager:318000575,cashier:318146575,cashier:13847253");
        System.out.println("To see the Available Employees for the shift , please enter 1");
        info = in.nextLine();
        while (info.equals("1"))
        {
            for (EmployeeModel e:backendController.getAvailableEmployees(Infos[0],Infos[1]))
            {
                System.out.println(e.toString());
            }
            System.out.println("Now please enter the ID of the employees you would like to assign to each shift position");
            System.out.print("All positions available is ");
            positionsShifts.printPositions();
            System.out.println("You must assign at least 1 warehouse, 1 usher , 1 cashier and 1 shift manager(can be shift manager and also anthoer job)");
            System.out.println("For example: shift manager:318416575,cashier:318146575,cashier:13847253");
            info = in.nextLine();
        }
        HashMap<Integer, String> employeesToShift = new HashMap<>();
        for(String emp:info.split(","))
        {
            String [] employee = emp.split(":");
            if(backendController.validEmployee(employee[0],employee[1]))
            {
                employeesToShift.put(Integer.parseInt(employee[1]),employee[0]);
            }
            else
            {
                invalidInput();
            }
        }
        backendController.validEmployeesInShiftJob(employeesToShift);
        backendController.assignNewShift(branchAdrress,Infos[0],Infos[1],employeesToShift);
        return new ManageShiftsView(ID,branchAdrress);
    }
    public View addEmployeeToShift() {
        System.out.println("To add employee to shift , please enter: YYYY-MM-DD,Morning/Evening");
        System.out.println("For Example: 2025-05-06,Morning");
        Scanner in = new Scanner(System.in);
        String info = in.nextLine();
        String [] Info = info.split(",");
        ShiftModel shift = backendController.getShiftByDateTypeAndBranch(Info[0],Info[1],branchAdrress);
        if(shift != null)
        {
            invalidInput();
        }
        System.out.println("The current list of employees assign to this shift is: ");
        System.out.println(shift.getEmployees());
        System.out.println("Please enter: new Employee ID:position");
        System.out.println("1243235-shift manager");
        info = in.nextLine();
        Info = info.split("-");
        backendController.addEmployeeToShift(shift.getShiftID(),Integer.parseInt(Info[0] ),Info[0]);
        return new ManageShiftsView(ID,branchAdrress);
    }
    public View removeEmployeeFromShift() {
        System.out.println("To add employee to shift , please enter:  YYYY-MM-DD,Morning/Evening");
        System.out.println("For Example: 2025-05-06,Morning");
        Scanner in = new Scanner(System.in);
        String info = in.nextLine();
        String [] Info = info.split(",");
        ShiftModel shift = backendController.getShiftByDateTypeAndBranch(Info[0],Info[1],branchAdrress);
        System.out.println("Please enter: employee ID");
        info = in.nextLine();
        backendController.removeEmployeeFromShift(shift.getShiftID(),Integer.parseInt(info));
        return new ManageShiftsView(ID,branchAdrress);
    }


    public View deleteShift() {
        System.out.println("To delete shift , please enter: YYYY-MM-DD,Morning/Evening");
        System.out.println("For Example: 2025-05-06,Morning");
        Scanner in = new Scanner(System.in);
        String info = in.nextLine();
        String [] Info = info.split(",");
        ShiftModel shift = backendController.getShiftByDateTypeAndBranch(Info[0],Info[1],branchAdrress);
        if(shift != null)
        {
            invalidInput();
        }
        backendController.deleteShift(shift.getShiftID());
        System.out.println("The shift has been deleted");
        return new ManageShiftsView(ID,branchAdrress);

    }

    public View printShift() {
        System.out.println("To print the shift , please enter: YYYY-MM-DD,Morning/Evening");
        System.out.println("For Example: 2025-05-06,Morning");
        Scanner in = new Scanner(System.in);
        String info = in.nextLine();
        String [] Info = info.split(",");
        ShiftModel shift = backendController.getShiftByDateTypeAndBranch(Info[0],Info[1].toLowerCase(),branchAdrress);
        if(shift != null)
        {
            System.out.println(shift);
        }
        else invalidInput();
        return new ManageShiftsView(ID,branchAdrress);
    }

    public View availableEmployees() {
        System.out.println("To see the employees available for specific shift , please enter: YYYY-MM-DD,Morning/Evening");
        System.out.println("For Example: 2025-05-06,morning");
        Scanner in = new Scanner(System.in);
        String info = in.nextLine();
        String [] Info = info.split(",");
        for (EmployeeModel fe : backendController.getAvailableEmployees(Info[0],Info[1]))
        {
            System.out.println(fe.toString());
        }
        return new ManageShiftsView(ID,branchAdrress);
    }


    public View returnMainMenuView() {
        return new MainMenuView(false);
    }

    public View returnManageShiftsView() {
        return new ManageShiftsView(ID, branchAdrress);
    }

    public View returnHRBranchSelectView() { return new BranchSelectView(ID,"hr"); }
}
