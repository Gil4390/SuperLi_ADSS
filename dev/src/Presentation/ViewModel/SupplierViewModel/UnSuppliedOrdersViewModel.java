package Presentation.ViewModel.SupplierViewModel;

import BusinessLayer.SupplierBusiness.DemandOrder;
import Presentation.Model.BackendController;
import Presentation.Model.SupplierModel.DemandOrderModel;

import java.util.LinkedList;
import java.util.List;

public class UnSuppliedOrdersViewModel {
    List<DemandOrderModel> unSuppliedDemandOrders;
    public UnSuppliedOrdersViewModel() {
        unSuppliedDemandOrders = new LinkedList<>();
    }


    public String getUnSuppliedOrders() {
        List<DemandOrderModel> demandOrders = BackendController.getInstance().getAllDemandOrders();
        StringBuilder result = new StringBuilder();
        for(DemandOrderModel demandOrder : demandOrders)
            if(!demandOrder.isSupplied()) {
                unSuppliedDemandOrders.add(demandOrder);
                result.append(demandOrder).append("\n");
            }
        if(unSuppliedDemandOrders.size()==0)
            return "There are no un supplied demand orders in the system\nPress any key to go back.";
        return result+"To remove an order please enter it's id:";
    }

    public void remove(String input) {
        for(DemandOrderModel demandOrderModel : unSuppliedDemandOrders)
            if(String.valueOf(demandOrderModel.getOrderId()).equals(input)) {
                BackendController.getInstance().removeUnSuppliedDemandOrder(demandOrderModel.getSupplierBN(), demandOrderModel.getOrderId());
                return;
            }
        System.out.println("Invalid input!");
    }
}
