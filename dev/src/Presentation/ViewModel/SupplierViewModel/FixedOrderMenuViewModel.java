package Presentation.ViewModel.SupplierViewModel;

import Presentation.Model.*;
import Presentation.Model.InventoryModel.InventoryItemModel;
import Presentation.Model.SupplierModel.FixedOrderModel;
import Presentation.View.SupplierView.FixedOrderMenuView;
import Presentation.View.View;
import Presentation.ViewModel.MainMenuViewModel;
import javafx.util.Pair;

import java.sql.Date;
import java.time.DayOfWeek;
import java.util.*;

public class FixedOrderMenuViewModel {
    private final BackendController backendController;
    private final Scanner scanner;

    public FixedOrderMenuViewModel() {
        this.backendController = BackendController.getInstance();
        scanner = new Scanner(System.in);
    }

    public View viewFixedOrders() {
        List<FixedOrderModel> fixedOrderModels = backendController.getAllFixedOrders();
        for(FixedOrderModel fixedOrderModel : fixedOrderModels)
            System.out.println(fixedOrderModel);
        return new FixedOrderMenuView();
    }

    public View addFixedOrder() {
        try {
        printAllProducts();
            System.out.println("to add new fixed order please enter: supply days, item id-amount");
            System.out.println("For example: 1 3 5, 1444-20 1332-30");
            String[] input = getInputForOrder();
            Set<DayOfWeek> daysOfSupply = new HashSet<>();
            String[] days = input[0].split(" ");
            for(String day : days)
                daysOfSupply.add(DayOfWeek.of(Integer.parseInt(day) == 1 ? 7 : Integer.parseInt(day) - 1));
            backendController.addFixedOrders(new Date(Calendar.getInstance().getTime().getTime()), daysOfSupply, getItemIdAndAmount(input[1].split(" ")));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return new FixedOrderMenuView();
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

    public View createNextWeekFixedOrders() {
        backendController.createNextWeekFixedOrders();
        return new FixedOrderMenuView();
    }
}
