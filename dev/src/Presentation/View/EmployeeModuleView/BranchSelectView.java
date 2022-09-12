package Presentation.View.EmployeeModuleView;

import Presentation.Model.BackendController;
import Presentation.View.InventoryView.InventoryMainMenuView;
import Presentation.View.MainMenuView;
import Presentation.View.SupplierView.SupplierMainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.MainMenuViewModel;

import java.util.List;

public class BranchSelectView implements View {
    private final int ID;
    private List<String> branches;

    private String jobWindow;

    private String back;

    private String logout;

    public BranchSelectView(int input , String nextWindow) {
        this.ID = input;
        if(!BackendController.getInstance().findJobEmployee(input+"","hr"))
            this.branches = BackendController.getInstance().getAllBranchesEmployeeWorksIn(ID);
        else
            this.branches = BackendController.getInstance().getAllBranchesByAddress();
        this.jobWindow = nextWindow;
    }

    @Override
    public void printMenu() {
        System.out.println("-----------------Select Branch To Manage-----------------");
        int index = 1;
        if (jobWindow.equals("warehouse"))
        {
            //get current branch
            System.out.println("1. "+BackendController.getInstance().getLoggedInUser().getBranch());
            index++;
        }
        else {
            for (String branch : branches){
                if (branch.equals(BackendController.getInstance().getAddressOfHQ()))
                    System.out.println((index++)+". "+branch+" (HQ)");
                else  System.out.println((index++)+". "+branch);
            }
        }

        back = (index++)+"";
        System.out.println(back + ". Back");
        logout = index+"";;
        System.out.println(logout + ". Logout");
    }

    @Override
    public View nextInput(String input) {
        try {
            if (input.equals(logout))
                return new MainMenuView(false);
            if(input.equals(back))
                return new SelectionView(new MainMenuViewModel(String.valueOf(BackendController.getInstance().getLoggedInUser().getID())));
            else {
                if (!jobWindow.equals("warehouse"))
                    BackendController.getInstance().enterBranch(branches.get(Integer.parseInt(input) - 1));
                if(jobWindow.equals("hr"))
                    return new HRManagerMenuView(ID, branches.get(Integer.parseInt(input) - 1));
                else if(jobWindow.equals("Inventory"))
                    return new InventoryMainMenuView();
                else if(jobWindow.equals("Supplier"))
                    return new SupplierMainMenuView();
            }
            return new MainMenuView(false);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new BranchSelectView(ID , jobWindow);
        }
    }
}

