package Presentation.ViewModel;

import Presentation.Model.BackendController;
import Presentation.View.DeliveryModuleView.AllDeliveriesView;
import Presentation.View.InventoryView.ReportsMenuView;
import Presentation.View.SupplierView.SupplierMainMenuView;
import Presentation.View.SupplierView.UnSuppliedOrdersView;
import Presentation.View.EmployeeModuleView.*;
import Presentation.View.View;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class MainMenuViewModel {
    private final BackendController backendController;

    private int index=0;

    private List<String> printMenuOptions;

    private HashMap<Integer, View> viewOptions;
    private String id;

    public MainMenuViewModel(String input){
        this.backendController= BackendController.getInstance();
        printMenuOptions = new LinkedList<>();
        viewOptions = new HashMap<>();
        this.id = input;
        signIn(input);
    }

    public void signIn(String input) {
        if (backendController.findJobEmployee(input , "hr")) {
            printMenuOptions.add(index + 1 + ". Managing shifts and defining shifts roles");
            viewOptions.put(index+1, new BranchSelectView(Integer.parseInt(input),"hr"));
            index = index+1;
            printMenuOptions.add(index+1+". View all un supplied orders");
            viewOptions.put(index+1, new UnSuppliedOrdersView(this));
            index = index+1;
            printMenuOptions.add(index+1+". See human resources messages");
            viewOptions.put(index+1, new MessagesView("HR manager", new SelectionView(this)));
            index = index+1;
        }
        if(backendController.findJobEmployee(input , "warehouse")) {
            printMenuOptions.add(index+1+". View the inventory menu");
            viewOptions.put(index+1, new BranchSelectView(Integer.parseInt(input),"Inventory"));
            index = index+1;
            printMenuOptions.add(index+1+". View the supplier menu");
            viewOptions.put(index+1, new BranchSelectView(Integer.parseInt(input),"Supplier"));
            index = index+1;
        }
        if(backendController.findJobEmployee(input , "logistics")) {
            printMenuOptions.add(index+1+". See all deliveries");
            viewOptions.put(index+1, new AllDeliveriesView());
            index = index+1;
            printMenuOptions.add(index+1+". View all un supplied orders");
            viewOptions.put(index+1, new UnSuppliedOrdersView(this));
            index = index+1;
            printMenuOptions.add(index+1+". See deliveries messages");
            viewOptions.put(index+1, new MessagesView("delivery manager", new SelectionView(this)));
            index = index+1;
        }
        if(backendController.findJobEmployee(input , "branch manager")) {
            backendController.enterBranch(backendController.getBranchOfManager(Integer.parseInt(input)));
            backendController.setBranchManagerLoggedIn();
            printMenuOptions.add(index+1+". View reports and data");
            viewOptions.put(index+1, new BranchManagerMenuView(Integer.parseInt(input), backendController.getBranchOfManager(Integer.parseInt(input))));
            index = index+1;
            printMenuOptions.add(index+1+". View inventory reports");
            viewOptions.put(index+1, new ReportsMenuView());
            index = index+1;
            printMenuOptions.add(index+1+". View supplier card");
            viewOptions.put(index+1, new SupplierMainMenuView());
            index = index+1;
        }
        if(index == 0) {
            printMenuOptions.add(index+". Employee Menu");
            viewOptions.put(index, new EmployeeMenuView(Integer.parseInt(input)));
        }
        viewOptions.put(index+1,null);
        printMenuOptions.add(index+1+". Logout");
        index = index+1;
    }

    public boolean isEmployeeExist()
    {
        try{
            return backendController.getEmployeeByID(Integer.parseInt(id))!=null;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public HashMap<Integer, View> getViewOptions() {
        return viewOptions;
    }

    public int getIndex() {
        return index;
    }

    public List<String> getPrintMenuOptions() {
        return printMenuOptions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setPrintMenuOptions(List<String> printMenuOptions) {
        this.printMenuOptions = printMenuOptions;
    }

    public void setViewOptions(HashMap<Integer, View> viewOptions) {
        this.viewOptions = viewOptions;
    }
}
