package Presentation.View.DeliveryModuleView;

import Presentation.View.View;
import Presentation.Model.DeliveryModuleModel.SiteDocumentModel;
import Presentation.ViewModel.DeliveryModulViewModel.AllDocsForDeliveryViewModel;
import Presentation.View.ApplicationView;

import java.util.List;

public class AllDocsForDeliveryView implements View {
    private final AllDocsForDeliveryViewModel allDocsForDeliveryViewModel;

    public AllDocsForDeliveryView(int DeliveryId) {
        this.allDocsForDeliveryViewModel = new AllDocsForDeliveryViewModel(DeliveryId);
    }

    @Override
    public void printMenu() {
        System.out.println("------SITE DOCUMENTS FOR DELIVERY NUMBER: "+allDocsForDeliveryViewModel.getDeliveryId()+"---------");
        List<String> documentModels = allDocsForDeliveryViewModel.getAllSiteForDelivery();
        for (int i=0; i<documentModels.size(); i++){
            System.out.println(i+1 + ") " + documentModels.get(i));
        }
        System.out.println("enter an index to view more details about the chosen site documents");
    }

    @Override
    public View nextInput(String input) {
        List<SiteDocumentModel> siteDocumentModels = allDocsForDeliveryViewModel.getAllDocumentsForDelivery();
        try {
            switch (input) {
                case "back":
                    return allDocsForDeliveryViewModel.returnToDeliveryMenuView();
                case "close":
                    ApplicationView.shouldTerminate = true;
                    break;
                default:
                    return allDocsForDeliveryViewModel.getDocumentView(siteDocumentModels.get(Integer.parseInt(input)-1));
            }
        } catch (Exception e) {
            System.out.println("Invalid input!");
        }
        return this;
    }
}
