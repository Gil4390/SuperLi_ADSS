package Presentation.ViewModel.EmployeeModulViewModel;

import Presentation.Model.BackendController;
import Presentation.Model.EmployeeModuleModel.EmployeeModel;
import Presentation.Model.EmployeeModuleModel.ShiftModel;
import Presentation.View.EmployeeModuleView.HRManagerMenuView;
import Presentation.View.EmployeeModuleView.ManageDriversShiftsView;
import Presentation.View.View;

import java.time.LocalDate;
import java.util.*;

public class ManageDriversShiftsViewModel {
    private final BackendController backendController;
    private final int ID;
    private String addressHQ;

    public ManageDriversShiftsViewModel(int id , String aHqQ) {
        this.backendController = BackendController.getInstance();
        ID = id;
        this.addressHQ = aHqQ;
    }

    public View invalidInput() {
        System.out.println("Invalid input!");
        return new ManageDriversShiftsView(ID , addressHQ);
    }

    public View AssignDriver() {
        try {
            System.out.println("Please enter the Date for the shift");
            System.out.println("For Example: 2025-05-27");
            Scanner in = new Scanner(System.in);
            String date = in.nextLine();
            LocalDate shiftDate = backendController.validDate(date.split(",")[0]);
            System.out.println("Please enter the driver's ID you want to assign to the shift");
            System.out.println("To finish typing ID's , enter finish \n");
            List<Integer> driversMusts =  backendController.getDriversToSpecificShift(date);
            HashMap<Integer, String> drivers = new HashMap<>();
            if (!driversMusts.isEmpty()) {
                System.out.println("Note that the following drivers have already assigned to this shift:");
                for (Integer id : backendController.getDriversToSpecificShift(date))
                {
                    System.out.println(backendController.getEmployeeByID(id).toString());
                    drivers.put(id,"driver");
                }
                System.out.println("If you want more drivers , please enter their ID");
            }
            System.out.println("To see the available employees for the shift , please enter 1");
            String ID =  in.nextLine();

            while (ID.equals("1")) {
                for (EmployeeModel employeeModel:  backendController.getAvailableDrivers(date)) {
                    System.out.println(employeeModel.toString());
                }
                System.out.println("Now start enter ID's");
                ID =  in.nextLine();
            }
            while (!ID.equals("finish")) {
                drivers.putIfAbsent(Integer.parseInt(ID), "driver");
                ID = in.nextLine();
            }
            drivers.put(backendController.getEmployeeByID(1).getID(),"CEO");
            //backendController.validEmployeesInShiftJob(drivers);
            backendController.assignNewShift(addressHQ,date,"morning",drivers);
            backendController.assignNewShift(addressHQ,date,"evening",drivers);
            return new ManageDriversShiftsView(this.ID,addressHQ);
        }
        catch (Exception e) {
            return invalidInput();
        }
    }

    public View RemoveDriver() {
        System.out.println("To delete driver from shift ,please enter: YYYY-MM-DD, driver ID");
        System.out.println("For Example: 2025-07-28,31841657");
        Scanner in = new Scanner(System.in);
        String info = in.nextLine();
        String [] Info = info.split(",");
        ShiftModel morningShift = backendController.getShiftByDateTypeAndBranch(Info[0],"morning",addressHQ);
        ShiftModel eveningShift = backendController.getShiftByDateTypeAndBranch(Info[0],"evening",addressHQ);
        if (eveningShift==null|| morningShift==null)
            invalidInput();
        else {
            backendController.removeEmployeeFromShift(morningShift.getShiftID(),Integer.parseInt(Info[1]));
            backendController.removeEmployeeFromShift(eveningShift.getShiftID(),Integer.parseInt(Info[1]));
        }
        return new ManageDriversShiftsView(this.ID,addressHQ);
    }

    public View PrintEmployeeShift() {
        System.out.println("To print driver's shifts , enter ID");
        Scanner in = new Scanner(System.in);
        String id = in.nextLine();
        for (ShiftModel sm: backendController.getEmployeeShifts(Integer.parseInt(id))) {
            System.out.println(sm.toString());
        }
        return new ManageDriversShiftsView(this.ID,addressHQ);
    }

    public View returnHRManagerMenuView() {
        return new HRManagerMenuView(ID, addressHQ);
    }
}
