package Presentation.Model.SupplierModel;

import Presentation.Model.BackendController;
import Presentation.Model.InventoryModel.InventoryItemModel;
import ServiceLayer.Objects.SupplierObjects.SBillOfQuantities;
import ServiceLayer.Objects.SupplierObjects.SSupplierItem;

import java.util.*;

public class BillOfQuantityModel {
    private final Map<Integer, SupplierItemModel> items;
    private final BackendController backendController;
    private final SupplierModel supplierModel;
    private final Scanner scanner;

    public BillOfQuantityModel(SBillOfQuantities sBillOfQuantities, SupplierModel supplierModel) {
        items = new HashMap<>();
        for(SSupplierItem supplierItem : sBillOfQuantities.getItems().values())
            items.put(supplierItem.getCatalogNumber(), new SupplierItemModel(supplierItem));
        this.backendController = BackendController.getInstance();
        this.supplierModel = supplierModel;
        scanner = new Scanner(System.in);
    }

    public String toString() {
        String output = "";
        for(SupplierItemModel supplierItemModel: items.values())
            output += supplierItemModel.toString()+"\n";
        return output;
    }

    public void addProduct() {
        System.out.println("Products to supply: ");
        List<InventoryItemModel> inventoryItemModelList = BackendController.getInstance().getInventoryReport();
        List<InventoryItemModel> inventoryItemsNotSupplied = new ArrayList<>();
        for(InventoryItemModel inventoryItemModel : inventoryItemModelList)
            if(!items.containsKey(inventoryItemModel.getItemID())) {
                inventoryItemsNotSupplied.add(inventoryItemModel);
                System.out.println("-ItemId: " + inventoryItemModel.getItemID() + ", name: " + inventoryItemModel.getName() + ", price in store: " + inventoryItemModel.getPriceInStore());
            }
        if(inventoryItemsNotSupplied.size()==0)
            throw new IllegalArgumentException("There are no products that in stock that supplier doesn't supply");
        System.out.println("to add new product please enter: catalog number, supplier catalog, price");
        System.out.println("For example: 55332, 11424, 7.50");
        String input = scanner.nextLine();
        String[] inputArr = input.split(", ");
        if (inputArr.length != 3)
            throw new IllegalArgumentException("Invalid input!");
        int catalogNumber = Integer.parseInt(inputArr[0]);
        int supplierCatalog = Integer.parseInt(inputArr[1]);
        double price = Double.parseDouble(inputArr[2]);
        for(InventoryItemModel inventoryItemModel : inventoryItemsNotSupplied)
            if(inventoryItemModel.getItemID() == catalogNumber) {
                backendController.addItemToSupplier(supplierModel.getBusinessNumber(), catalogNumber, supplierCatalog, price, inventoryItemModel.getName());
                return;
            }
        throw new IllegalArgumentException("Invalid input!");
    }


    public void productsList() {
        checkIfThereAreProducts();
        System.out.println(this);
    }

    public void addProductDiscount() {
        productsList();
        System.out.println("please enter the product id for the product that you want to add discount");
        String input = scanner.nextLine();
        int catalogNumber = Integer.parseInt(input);
        System.out.println("to add new product discount enter: amount, discount");
        System.out.println("For example: 1000, 12.50");
        input = scanner.nextLine();
        String[] inputArr = input.split(", ");
        if (inputArr.length != 2)
            throw new IllegalArgumentException("Invalid input!");
        int amount = Integer.parseInt(inputArr[0]);
        double discount = Double.parseDouble(inputArr[1]);
        backendController.addItemDiscountAccordingToAmount(supplierModel.getBusinessNumber(), catalogNumber, amount, discount);
    }

    public void removeProduct() {
        productsList();
        System.out.println("please enter the product id that you want remove");
        String input = scanner.nextLine();
        backendController.removeItemFromSupplier(supplierModel.getBusinessNumber(), Integer.parseInt(input));
    }

    public void checkIfThereAreProducts() {
        if(items.size()==0)
            throw new IllegalArgumentException("There are no products in the system yet");
    }

    public void updatePrice(int catalogNumber) {
        checkIfThereAreProducts();
        System.out.println("please enter new product price:");
        String input = scanner.nextLine();
        backendController.updateItemPrice(supplierModel.getBusinessNumber(), catalogNumber, Double.parseDouble(input));
        items.get(catalogNumber).setPrice(Double.parseDouble(input));
    }

    public void updateName(int catalogNumber) {
        checkIfThereAreProducts();
        System.out.println("please enter new product name:");
        String input = scanner.nextLine();
        backendController.updateItemName(supplierModel.getBusinessNumber(), catalogNumber, input);
        items.get(catalogNumber).setItemName(input);;
    }

    public int editProduct() {
        productsList();
        System.out.println("please enter product catalog number:");
        String input = scanner.nextLine();
        int catalogNumber = Integer.parseInt(input);
        if(!items.containsKey(catalogNumber))
            throw new IllegalArgumentException("Invalid input!");
        return catalogNumber;
    }

    public void updateSupplierCatalog(int catalogNumber) {
        checkIfThereAreProducts();
        System.out.println("please enter new supplier catalog:");
        String input = scanner.nextLine();
        backendController.updateSupplierCatalog(supplierModel.getBusinessNumber(), catalogNumber, Integer.parseInt(input));
        items.get(catalogNumber).setSupplierCatalog(Integer.parseInt(input));
    }

    public void updateDiscount(int catalogNumber) {
        checkIfThereAreProducts();
        if(items.get(catalogNumber).getDiscountAccordingToAmount().keySet().size()<2)
            throw new IllegalArgumentException("There are no discounts for this product");
        for(int amount : items.get(catalogNumber).getDiscountAccordingToAmount().keySet())
            if(amount!=0)
                System.out.println("amount: "+ amount+" discount: "+items.get(catalogNumber).getDiscountAccordingToAmount().get(amount));
        System.out.println("please enter amount to update: ");
        String input = scanner.nextLine();
        if(!items.get(catalogNumber).getDiscountAccordingToAmount().containsKey(Integer.parseInt(input)) || Integer.parseInt(input) == 0)
            throw new IllegalArgumentException("Invalid input!");
        int amount = Integer.parseInt(input);
        System.out.println("please enter new discount: ");
        input = scanner.nextLine();
        backendController.updateItemDiscountAccordingToAmount(supplierModel.getBusinessNumber(), catalogNumber, amount, Double.parseDouble(input));
        items.get(catalogNumber).getDiscountAccordingToAmount().replace(amount, Double.parseDouble(input));
    }

    public void removeProductDiscount() {
        productsList();
        System.out.println("please enter product catalog number:");
        String input = scanner.nextLine();
        int catalogNumber = Integer.parseInt(input);
        if(!items.containsKey(catalogNumber))
            throw new IllegalArgumentException("Invalid input!");
        System.out.println("please enter amount to remove:");
        input = scanner.nextLine();
        int amount = Integer.parseInt(input);
        if(amount==0)
            throw new IllegalArgumentException("Invalid input!");
        backendController.removeItemDiscountAccordingToAmount(supplierModel.getBusinessNumber(), catalogNumber, amount);
        items.get(catalogNumber).getDiscountAccordingToAmount().remove(amount);
    }
}
