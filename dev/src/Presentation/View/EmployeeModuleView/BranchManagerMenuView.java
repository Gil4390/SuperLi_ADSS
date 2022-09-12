package Presentation.View.EmployeeModuleView;

import Presentation.Model.BackendController;
import Presentation.View.View;

import java.util.List;

public class BranchManagerMenuView implements View {
    private final int ID;

    private final String branchAddress;

    public BranchManagerMenuView(int id, String branch) {
        ID = id;
        this.branchAddress = branch;
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------Branch Manager Menu-----------------");
        System.out.println("1.");
        List<String> messages = BackendController.getInstance().pullMessages(branchAddress,"branch manager");
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
        return null;
    }
}
