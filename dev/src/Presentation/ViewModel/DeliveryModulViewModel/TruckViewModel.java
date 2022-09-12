package Presentation.ViewModel.DeliveryModulViewModel;

import Presentation.Model.BackendController;
import Presentation.Model.DeliveryModuleModel.TruckModel;
import Presentation.View.DeliveryModuleView.AllDeliveriesView;
import Presentation.View.View;

public class TruckViewModel {
    private final BackendController backendController;
    private TruckModel truckModel;

    public TruckViewModel(String truckNumber) {
        this.backendController = BackendController.getInstance();
        this.truckModel= backendController.getTruckByLicense(truckNumber);
    }

    public String getLicenseNumber() { return truckModel.getLicenseNumber();
    }

    public String getModel() { return truckModel.getModel();
    }

    public int getBaseWeight() { return truckModel.getBaseWeight();
    }

    public int getMaxWeight() { return truckModel.getMaxWeight();
    }

    public View getDeliveryMenuView() {
        return new AllDeliveriesView();
    }
}
