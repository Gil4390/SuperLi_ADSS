package Presentation.ViewModel.DeliveryModulViewModel;

import Presentation.Model.BackendController;
import Presentation.Model.DeliveryModuleModel.DeliveryModel;
import Presentation.View.DeliveryModuleView.DeliveryView;
import Presentation.View.MainMenuView;
import Presentation.View.View;

import java.text.ParseException;
import java.util.List;

public class AllDeliveriesViewModel {
    private final BackendController backendController;

    public AllDeliveriesViewModel() {
        this.backendController = BackendController.getInstance();
    }

    public List<DeliveryModel> getAllDeliveries() throws ParseException {
        return backendController.getAllDeliveries();
    }

    public View returnToDeliveryMenuView() {
        return new MainMenuView(false);
    }

    public View goToDeliveryView(DeliveryModel deliveryModel) {
        return new DeliveryView(deliveryModel);
    }
}
