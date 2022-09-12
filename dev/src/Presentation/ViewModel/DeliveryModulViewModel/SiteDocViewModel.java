package Presentation.ViewModel.DeliveryModulViewModel;

import Presentation.Model.BackendController;
import Presentation.Model.DeliveryModuleModel.SiteDocumentModel;
import BusinessLayer.DeliveryModule.Objects.problemSiteVisit;

import java.util.List;
import java.util.Map;

public class SiteDocViewModel {
    private final BackendController backendController;
    private SiteDocumentModel siteDocModel;

    public SiteDocViewModel(SiteDocumentModel siteDocumentModel) {
        this.backendController = BackendController.getInstance();
        this.siteDocModel= siteDocumentModel;
    }

    public int getDocumentId() { return siteDocModel.getDocumentId();
    }

    public int getDeliveryId() { return siteDocModel.getDeliveryId();
    }

    public String getAddress() { return siteDocModel.getAddress();
    }

    public int getTruckWeight() { return siteDocModel.getTruckWeight();
    }

    public Map<Integer, Integer> getLoadItems() { return siteDocModel.getLoadItems();
    }

    public Map<Integer, Integer> getUnloadItems() { return siteDocModel.getUnloadItems();
    }

    public List<problemSiteVisit> getLogList() { return siteDocModel.getLogList();
    }

    public String getContactName() { return siteDocModel.getContactName();
    }

    public String getContactNumber() { return siteDocModel.getContactNumber();
    }
}
