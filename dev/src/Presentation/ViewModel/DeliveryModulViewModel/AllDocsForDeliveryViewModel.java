package Presentation.ViewModel.DeliveryModulViewModel;

import Presentation.Model.BackendController;
import Presentation.Model.DeliveryModuleModel.SiteDocumentModel;
import Presentation.View.DeliveryModuleView.AllDeliveriesView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.View.DeliveryModuleView.DocumentView;

import java.util.LinkedList;
import java.util.List;

public class AllDocsForDeliveryViewModel {
    private final BackendController backendController;
    private final int deliveryId;

    public AllDocsForDeliveryViewModel(int DeliveryId) {
        this.backendController = BackendController.getInstance();
        this.deliveryId= DeliveryId;
    }

    public List<String> getAllSiteForDelivery() {
        List <SiteDocumentModel> documents = backendController.getAllDocumentsForDelivery(deliveryId);
        List<String> ans = new LinkedList<>();
        for (SiteDocumentModel documentModel: documents)
            ans.add(documentModel.getAddress());
        return ans;
    }

    public List<SiteDocumentModel> getAllDocumentsForDelivery() {
        return backendController.getAllDocumentsForDelivery(deliveryId);
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public View returnToDeliveryMenuView() {
        return new AllDeliveriesView();
    }

    public View getDocumentView(SiteDocumentModel siteDocumentModel) {
        return new DocumentView(siteDocumentModel);
    }
}
