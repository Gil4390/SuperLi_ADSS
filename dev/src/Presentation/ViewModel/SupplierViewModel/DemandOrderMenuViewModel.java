package Presentation.ViewModel.SupplierViewModel;

import Presentation.Model.*;
import Presentation.Model.InventoryModel.InventoryItemModel;
import Presentation.Model.SupplierModel.DemandOrderModel;
import Presentation.View.SupplierView.DemandOrderMenuView;
import Presentation.View.View;

import java.sql.Date;
import java.util.*;

public class DemandOrderMenuViewModel {
    private final BackendController backendController;
    private final Scanner scanner;

    public DemandOrderMenuViewModel() {
        this.backendController = BackendController.getInstance();
        scanner = new Scanner(System.in);
    }

    public View viewDemandOrders() {
        List<DemandOrderModel> demandOrderModels = backendController.getAllDemandOrders();
        for(DemandOrderModel demandOrderModel : demandOrderModels)
            System.out.println(demandOrderModel);
        return new DemandOrderMenuView();
    }

    public View addDemandOrder() {
        try {
            printAllProducts();
            System.out.println("to add new demand orders please enter: supply date as yyyy-mm-dd, item id-amount");
            System.out.println("For example: 2022-05-22, 1444-20 1332-30");
            String[] input = getInputForOrder();
            Date supplyDate = Date.valueOf(input[0]);
            backendController.addDemandOrders(new Date(Calendar.getInstance().getTime().getTime()), supplyDate, getItemIdAndAmount(input[1].split(" ")));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return new DemandOrderMenuView();
    }

    private HashMap<Integer, Integer> getItemIdAndAmount(String[] products) {
        HashMap<Integer, Integer> itemIdAndAmount = new HashMap<>();
        if(products.length==0)
            throw new IllegalArgumentException("Invalid input!");
        for(String product : products)
            itemIdAndAmount.put(Integer.parseInt(product.split("-")[0]), Integer.parseInt(product.split("-")[1]));
        return itemIdAndAmount;
    }

    public String[] getInputForOrder() {
        String input = scanner.nextLine();
        String[] inputArr = input.split(", ");
        if (inputArr.length != 2)
            throw new IllegalArgumentException("Invalid input!");
        return inputArr;
    }

    private void printAllProducts() {
        List<InventoryItemModel> inventoryItemModelList = BackendController.getInstance().getInventoryReport();
        for(InventoryItemModel inventoryItemModel : inventoryItemModelList)
            System.out.println("-ItemId: "+inventoryItemModel.getItemID()+", name: "+inventoryItemModel.getName()+", price in store: "+inventoryItemModel.getPriceInStore());
    }
}
