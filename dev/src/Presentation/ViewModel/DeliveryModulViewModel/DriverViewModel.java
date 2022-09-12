package Presentation.ViewModel.DeliveryModulViewModel;

import Presentation.Model.BackendController;
import Presentation.Model.EmployeeModuleModel.EmployeeModel;
import Presentation.View.DeliveryModuleView.AllDeliveriesView;
import Presentation.View.MainMenuView;
import Presentation.View.View;

import java.util.*;

public class DriverViewModel {
    private final BackendController backendController;
    private EmployeeModel employeeModel;

    public DriverViewModel(int employeeId) {
        this.backendController = BackendController.getInstance();
        this.employeeModel= backendController.getEmployeeByID(employeeId);
    }

    public String getName() {
        return employeeModel.getName();
    }

    public int getId() { return employeeModel.getID();
    }

    public String getDriverLicenses() {
        List<String> licenses = backendController.getDriverLicencesForDriver(getId());
        String ans = "";
        for (String license: licenses)
            ans= ans+", "+license;
        return ans.substring(2);
    }

    public View getDeliveryMenuView() {
        return new AllDeliveriesView();
    }
}
