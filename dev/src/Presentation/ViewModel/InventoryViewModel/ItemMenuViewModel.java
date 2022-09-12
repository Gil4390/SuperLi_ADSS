package Presentation.ViewModel.InventoryViewModel;

import Presentation.Model.*;
import Presentation.Model.InventoryModel.CategoryModel;
import Presentation.Model.InventoryModel.ItemModel;
import Presentation.View.*;
import Presentation.View.InventoryView.ItemMenuView;
import Presentation.View.InventoryView.ItemView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ItemMenuViewModel {
    Scanner scanner;
    public ItemMenuViewModel() {
        scanner = new Scanner(System.in);
    }

    public View viewItems() {
        try{
            System.out.println("--------------------------------------------");
            InventoryItemViewModel inventoryItemViewModel = new InventoryItemViewModel();
            inventoryItemViewModel.getBasicReport();
            Printer.getInstance().print("If you wish to make changes on a specific item, please enter its ID: ");
            Printer.getInstance().print("If you want to find a specific item through its category, press c");
            Printer.getInstance().print("If you wish to return to the previous menu, press back");
            String typedAnswer = scanner.nextLine();
            switch (typedAnswer) {
                case "back":
                    return new ItemMenuView();
                case "c":
                    return getAllItemInCategory();
                default:
                    return new ItemView(BackendController.getInstance().getItem(Integer.parseInt(typedAnswer)));
            }
        } catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to the previous screen");
            return new ItemMenuView();
        }
    }

    public View addItem() {
        try{
            Printer.getInstance().print("Choose the item's name: ");
            String itemName = scanner.nextLine();
            Printer.getInstance().print("Choose the category of the item: ");
            CategoryModel categoryRoot = BackendController.getInstance().getCategory("0");
            CategoryModel categoryModel = new CategoryViewModel(categoryRoot).selectCategory();
            String categoryID = categoryModel.getCatID();
            String categoryName = categoryModel.getName();
            Printer.getInstance().print("Enter the price for the product:");
            String typedPrice = scanner.nextLine();
            double checkedPrice = Float.parseFloat(checkIfFloatPositive(typedPrice));
            Printer.getInstance().print("Enter the minimal quantity recommended for this item:");
            String typedMinimalQuantity = scanner.nextLine();
            int checkedMinimalQuantity = Integer.parseInt(getItToBeNatural(typedMinimalQuantity));
            Printer.getInstance().print("Enter the full quantity (after purchase) recommended for this item:");
            String typedFullQuantity = scanner.nextLine();
            int checkedFullQuantity = Integer.parseInt(getItToBeNatural(typedFullQuantity));

            boolean check = false;
            while(!check) {
                check=isLargerThan(checkedFullQuantity, checkedMinimalQuantity);
                if(!check) {
                    Printer.getInstance().print("Must be larger than the minimal quantity. Write the number again:");
                    String typed = scanner.nextLine();
                    checkedFullQuantity = Integer.parseInt(getItToBeNatural(typed));
                }
            }
            //while(!isLargerThan(Integer.parseInt(getItToBeNatural(typedFullQuantity)), checkedMinimalQuantity));
            Printer.getInstance().print("Enter the product's manufacture:");
            String manufacture = scanner.nextLine();
            List<Integer> backShelves = addBackFrontShelves(true);
            List<Integer> frontShelves = addBackFrontShelves(false);
            Printer.getInstance().print("Item Details are:\n"+"ItemName: " +itemName+"\n"
                    +"categoryID: " +categoryID+"\n"
                    +"categoryName: " +categoryName+"\n"
                    +"Price: " +checkedPrice+"\n"
                    +"minimalQuantity: " +checkedMinimalQuantity+"\n"
                    +"fullQuantity: " +checkedFullQuantity+"\n"
                    +"manufacture: " +manufacture+"\n"
                    +"Front Shelves IDs: " +Arrays.toString(frontShelves.toArray())+"\n"
                    +"Back Shelves IDs: " +Arrays.toString(backShelves.toArray())+"\n");
            Printer.getInstance().print("If you wish to add the item, press Y. If you want to return to the previous menu press N.");
            String typedAnswer = scanner.nextLine();
            if(getAnswer(typedAnswer)){
                ItemModel itemModel = BackendController.getInstance().addItem(itemName, categoryID, checkedPrice, checkedMinimalQuantity, checkedFullQuantity, manufacture, backShelves, frontShelves);
                Printer.getInstance().print("Recorded Successfully");
                return new ItemMenuView();
            }
            Printer.getInstance().print("Record Cancelled");
            return new ItemMenuView();
        }catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to the previous screen");
            return new ItemMenuView();
        }

    }

    private boolean isLargerThan(int parseInt, int checkedMinimalQuantity) {
        if(parseInt <= checkedMinimalQuantity){
            return false;
        }
        return true;
    }

    public View getAllItemInCategory() {
        try{
            InventoryItemViewModel inventoryItemViewModel = new InventoryItemViewModel();
            inventoryItemViewModel.getShortReportByCategory();
            Printer.getInstance().print("If you wish to make changes on a specific item, please enter its ID: ");
            Printer.getInstance().print("If you wish to return to the previous menu, press back");
            String typedAnswer = scanner.nextLine();
            switch (typedAnswer) {
                case "back":
                    return new ItemMenuView();
                default:
                    return new ItemView(BackendController.getInstance().getItem(Integer.parseInt(typedAnswer)));
            }
        }catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to the previous screen");
            return new ItemMenuView();
        }

    }


    private String getItToBeNatural(String amount) {
        while (!(amount.matches("-?\\d+") && Integer.parseInt(amount) > 0 && Math.floor(Integer.parseInt(amount)) == Integer.parseInt(amount))) {
            Printer.getInstance().print("Must be non-negative natural number. Write the number again:");
            amount = scanner.nextLine();
        }
        return amount;
    }

    private String checkIfFloatPositive(String typedPrice) {
        while(!(typedPrice.matches("[+-]?([0-9]*[.])?[0-9]+") && (Float.parseFloat(typedPrice) > 0))){
            Printer.getInstance().print("Must be positive decimal number. Write the number again:");
            typedPrice = scanner.nextLine();
        }
        return typedPrice;
    }

    private boolean getAnswer(String typedAnswer) {
        switch (typedAnswer) {
            case "N":
                return false;
            case "Y":
                return true;
            default:
                Printer.getInstance().print("Please Enter either Y or N");
                Printer.getInstance().print("If you wish to add the item, press Y. If you want to return to the previous menu press N.");
                typedAnswer = scanner.nextLine();
                return getAnswer(typedAnswer);
        }
    }


    private List<Integer> addBackFrontShelves(boolean isBackShelves) {
        boolean check = false;
        while (!check) {
            if(isBackShelves)
                Printer.getInstance().print("Choose the required back shelves:");
            else
                Printer.getInstance().print("Choose the required front shelves:");
            List<Integer> allShelves;
            if (isBackShelves) {
                allShelves = BackendController.getInstance().getAllBackShelves().stream().sorted().collect(Collectors.toList());
            } else {
                allShelves = BackendController.getInstance().getAllFrontShelves().stream().sorted().collect(Collectors.toList());
            }
            for (int id : allShelves)
                Printer.getInstance().print(String.valueOf(id));
            if (allShelves.size() > 0) {
                Printer.getInstance().print("Enter new shelves IDs in the form 'id1,id2,id3...' (no spaces!)");
                String[] idsArr = scanner.nextLine().split(",");
                int[] idsIntArr = checkInputShelvesId(allShelves,idsArr);
                if (idsIntArr.length != 0){
                    List<Integer> list = new ArrayList<>();
                    for(int numb : idsIntArr)
                        list.add(numb);
                    return list;
                }
            } else {
                Printer.getInstance().print("There aren't any shelves");
            }
            check = true;
        }
        return null;
    }

    private int[] checkInputShelvesId(List<Integer> allShelves, String[] idsArr) {
        int[] idsIntArr = new int[idsArr.length];
        for (int i = 0; i < idsIntArr.length; i++) {
            boolean checkID = false;
            while (!checkID) {
                try {
                    if (idsArr[i].charAt(0) == '0' && idsArr[i].length() > 1)
                        throw new IllegalArgumentException("");
                    //if(Arrays.stream(idsIntArr).findAny(Integer.parseInt(idsArr[i])))
                    int id = Integer.parseInt(idsArr[i]);
                    for(Integer badInt : idsIntArr) {
                        if (id == badInt)
                            throw new IllegalArgumentException("");
                    }
                    idsIntArr[i] = Integer.parseInt(idsArr[i]);
                    if (!allShelves.contains(idsIntArr[i]))
                        throw new IllegalArgumentException("");
                    checkID = true;
                } catch (Exception e) {
                    Printer.getInstance().print("This id: " + idsArr[i] + " ,is Illegal. please enter all!! the required id shelves again");
                    idsArr = scanner.nextLine().split(",");
                    return checkInputShelvesId(allShelves,idsArr);
                }
            }
        }
        return idsIntArr;
    }

}
