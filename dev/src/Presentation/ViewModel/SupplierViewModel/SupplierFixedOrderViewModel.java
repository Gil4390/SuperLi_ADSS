package Presentation.ViewModel.SupplierViewModel;

import Presentation.Model.BackendController;
import Presentation.Model.SupplierModel.SupplierModel;
import Presentation.View.SupplierView.SupplierView;
import Presentation.View.View;

public class SupplierFixedOrderViewModel {
    private final BackendController backendController;
    private final SupplierModel supplierModel;

    public SupplierFixedOrderViewModel(SupplierModel supplierModel) {
        this.supplierModel = supplierModel;
        this.backendController = BackendController.getInstance();
    }

    public View viewFixedOrders() {
        try {
            supplierModel.viewFixedOrders();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);
    }

    public View addFixedOrder() {
        try {
            supplierModel.addFixedOrder();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);
    }

    public View unActiveFixedOrder() {
        try {
            supplierModel.unActiveFixOrder();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);
    }

    public View addSupplyDayToFixedOrder() {
        try {
            supplierModel.addSupplyDayToFixedOrder();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);
    }

    public View removeSupplyDayFromFixedOrder() {
        try {
            supplierModel.removeSupplyDayFromFixedOrder();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);
    }

    public View addProductToFixedOrder() {
        try {
            supplierModel.addProductToFixedOrder();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);
    }

    public View removeProductFromFixedOrder() {
        try {
            supplierModel.removeProductFromFixedOrder();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new SupplierView(supplierModel);
    }

    public View updateProductOfFixedOrder() {
        try {
            supplierModel.updateProductOfFixedOrder();
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
