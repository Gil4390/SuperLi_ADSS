package Presentation.ViewModel.SupplierViewModel;

import Presentation.Model.BackendController;
import Presentation.Model.SupplierModel.SupplierModel;
import Presentation.View.SupplierView.SupplierView;
import Presentation.View.View;

public class SupplierDemandOrderViewModel {
    private final BackendController backendController;
    private final SupplierModel supplierModel;

    public SupplierDemandOrderViewModel(SupplierModel supplierModel) {
        this.backendController = BackendController.getInstance();
        this.supplierModel = supplierModel;
    }

    public View viewDemandOrders() {
        try {
            supplierModel.viewDemandOrders();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);
    }

    public View addDemandOrder() {
        try {
            supplierModel.addDemandOrder();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);
    }

    public View supplyDemandOrder() {
        try {
            supplierModel.supplyDemandOrder();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);
    }

    public View removeUnSuppliedDemandOrder() {
        try {
            supplierModel.removeUnSuppliedDemandOrder();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);
    }

    public View addProductToDemandOrder() {
        try {
            supplierModel.addProductToDemandOrder();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);
    }

    public View removeProductFromDemandOrder() {
        try {
            supplierModel.removeProductFromDemandOrder();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);
    }

    public View updateProductOfDemandOrder() {
        try {
            supplierModel.updateProductOfDemandOrder();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);
    }

    public View sendDemandOrderPDF() {
        try {
            supplierModel.sendDemandOrderPDF();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);
    }

    public View backToSupplierView() {
        return new SupplierView(supplierModel);
    }
}
