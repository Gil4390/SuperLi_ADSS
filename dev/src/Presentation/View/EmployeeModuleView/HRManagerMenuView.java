package Presentation.View.EmployeeModuleView;

import Presentation.Model.BackendController;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import java.util.List;

public class HRManagerMenuView implements View {
    private final int ID;
    private final String branchAddress;

    public HRManagerMenuView(int input ,  String branchAddress) {
        this.ID = input;
        this.branchAddress = branchAddress;
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------HR Manager Menu-----------------");
        System.out.println("1.Manage employees");
        if (branchAddress.equals(BackendController.getInstance().getAddressOfHQ()))
        {
            System.out.println("2.Manage shifts of drivers");
            System.out.println("3.Logout");
        }
        else {
            System.out.println("2.Manage shifts");
            System.out.println("3.Manage shifts of drivers");
            System.out.println("4.Logout");
        }

        List<String> messages = BackendController.getInstance().pullMessages(branchAddress,"hr manager");
        if (messages.size() !=0) {
            System.out.println();
            System.out.println("Here is your latest messages:");
            for (String str :messages) {
                System.out.println(str);
            }
        }
    }

    @Override
    public View nextInput(String input) {
        try {
            if (input.equals("1"))
                return new ManageEmployeesView(ID);
            else if (branchAddress.equals(BackendController.getInstance().getAddressOfHQ()))
            {
                if (input.equals("2"))
                    return new ManageDriversShiftsView(ID, BackendController.getInstance().getAddressOfHQ());
                else if (input.equals("3"))
                    return new MainMenuView(false);
                else
                    return new HRManagerMenuView(ID,branchAddress);
            }
            else
            {
                switch (input) {
                    case "2":
                        return new ManageShiftsView(ID, branchAddress);
                    case "3":
                        return new ManageDriversShiftsView(ID, BackendController.getInstance().getAddressOfHQ());
                    case "4":
                        return new MainMenuView(false);
                    default:
                        return new HRManagerMenuView(ID, branchAddress);
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new HRManagerMenuView(ID,branchAddress);
        }
    }
}
