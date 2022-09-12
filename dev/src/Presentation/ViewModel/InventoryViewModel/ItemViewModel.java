package Presentation.ViewModel.InventoryViewModel;

import Presentation.Model.BackendController;
import Presentation.Model.InventoryModel.ItemModel;
import Presentation.View.*;
import Presentation.View.InventoryView.ItemView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ItemViewModel{

    ItemModel itemModel;
    Scanner scanner;

    public ItemViewModel(ItemModel itemModel){
        this.itemModel=itemModel;
        scanner = new Scanner(System.in);
    }

    public ItemModel getItemModel() {return itemModel;}

    public View buyItem() {
        try{
            Printer.getInstance().print("Please enter the amount you want to buy: ");
            String typedAmount = scanner.nextLine();
            int checkedAmount = Integer.parseInt(getItToBeNatural(typedAmount));
            double tmpPrice = checkedAmount*itemModel.getFinalCurrentPrice();
            Printer.getInstance().print("For "+checkedAmount+" of Item: "+itemModel.getName()+" you will pay "+tmpPrice+" shmekels");
            Printer.getInstance().print("Enter Y to finish the purchase, N to cancel");
            String typedAnswer = scanner.nextLine();
            if(getAnswer(typedAnswer)){
                itemModel.buyItem(checkedAmount);
                Printer.getInstance().print("Item purchased");
                return new ItemView(BackendController.getInstance().getItem(itemModel.getItemID()));
            }
        }
        catch (Exception e){
            Printer.getInstance().print(e.getMessage());
        }
        return new ItemView(BackendController.getInstance().getItem(itemModel.getItemID()));
    }

    public View addDiscount() {
        try{
            Printer.getInstance().print("Add discount fare between 0.00-1.00");
            double discountFare = getItToBeBetween0and1(scanner.nextLine());
            Printer.getInstance().print("Enter discount name");
            String discountName = scanner.nextLine();
            Printer.getInstance().print("Enter from date in format - dd/mm/yyyy");
            String fromDateStr = scanner.nextLine();
            LocalDate fromDate = makeDate(fromDateStr);
            Printer.getInstance().print("Enter to date in format - dd/mm/yyyy");
            String toDateStr = scanner.nextLine();
            LocalDate toDate = makeDate(toDateStr);
            itemModel.addDiscount(discountFare,discountName,fromDate,toDate);
            Printer.getInstance().print("Success");
        }
        catch (Exception e){
            Printer.getInstance().print(e.getMessage());
        }
        return new ItemView(BackendController.getInstance().getItem(itemModel.getItemID()));
    }

    public View changeItemQuantity() {
        try{
            Printer.getInstance().print("You currently have "+itemModel.getQuantityBackRoom()+" of this item in the back room");
            Printer.getInstance().print("You currently have "+itemModel.getQuantityFrontRoom()+" of this item in the front room ");
            Printer.getInstance().print("To fix quantity in store, press 0.");
            Printer.getInstance().print("To fix quantity in backRoom, press 1.");
            Printer.getInstance().print("To transfer quantity from store to backRoom or the other way around, press 2.");
            Printer.getInstance().print("If you would like to get to the previous menu press back");
            String typedChoice1 = scanner.nextLine();
            boolean inBackRoom = true;
            switch (typedChoice1) {
                case "0":
                    inBackRoom = false;
                    break;
                case "1":
                    break;
                case "2":
                    moveItemLocation();
                    return new ItemView(BackendController.getInstance().getItem(itemModel.getItemID()));
                case "back":
                    return new ItemView(itemModel);
                default:
                    Printer.getInstance().print("Invalid Input");
                    moveItemLocation();
            }
            Printer.getInstance().print("To fix quantity by removing, press 0.");
            Printer.getInstance().print("To fix quantity by adding, press 1.");
            Printer.getInstance().print("If you would like to get to the previous menu press back");
            String typedChoice2 = scanner.nextLine();
            switch (typedChoice2) {
                case "0":
                    Printer.getInstance().print("Type the amount you want to remove: ");
                    String typedAmount = scanner.nextLine();
                    int checkedAmount = Integer.parseInt(getItToBeNatural(typedAmount));
                    itemModel.removeQuantity(inBackRoom, checkedAmount);
                    Printer.getInstance().print("Amount removed successfully");
                    return new ItemView(BackendController.getInstance().getItem(itemModel.getItemID()));
                case "1":
                    Printer.getInstance().print("Type the amount you want to add: ");
                    String typedAmount2 = scanner.nextLine();
                    int checkedAmount2 = Integer.parseInt(getItToBeNatural(typedAmount2));
                    itemModel.addQuantity(inBackRoom, checkedAmount2);
                    Printer.getInstance().print("Amount added successfully");
                    return new ItemView(BackendController.getInstance().getItem(itemModel.getItemID()));
                case "back":
                    return new ItemView(itemModel);
                default:
                    Printer.getInstance().print("Invalid Input");
                    return new ItemView(itemModel);
            }
        }
        catch (Exception e){
            Printer.getInstance().print(e.getMessage());
        }
        return new ItemView(itemModel);
    }

    public View changeItemPrice() {
        try{
            Printer.getInstance().print("The current price for the Item: "+ itemModel.getName()+" is "+itemModel.getCurrPrice()+" shmekels.");
            Printer.getInstance().print("Type the new price");
            String typedAmount = scanner.nextLine();
            int checkedAmount = Integer.parseInt(checkIfFloatPositive(typedAmount));
            Printer.getInstance().print("If you wish to change '"+itemModel.getName()+"' price to: "+checkedAmount+" press Y. If you want to keep it as: "+itemModel.getCurrPrice()+" press N.");
            String typedAnswer = scanner.nextLine();
            if(getAnswer(typedAnswer)){
                itemModel.changeItemPrice(checkedAmount);
                Printer.getInstance().print("Price changed Successfully");
                return new ItemView(BackendController.getInstance().getItem(itemModel.getItemID()));
            }
        }
        catch (Exception e){
            Printer.getInstance().print(e.getMessage());
        }
        return new ItemView(itemModel);
    }

    public View changeItemFullQuantity() {
        try{
            Printer.getInstance().print("The current full quantity for the Item: "+ itemModel.getName()+" is "+itemModel.getFullQuantity());
            Printer.getInstance().print("Type the new full quantity");
            String typedFullQuantity = scanner.nextLine();
            int checkedFullQuantity = Integer.parseInt(getItToBeNatural(typedFullQuantity));
            boolean check = false;
            while(!check) {
                check=isLargerThan(checkedFullQuantity, getItemModel().getMinimalQuantity());
                if(!check) {
                    Printer.getInstance().print("Must be larger than the minimal quantity. Write the number again:");
                    String typed = scanner.nextLine();
                    checkedFullQuantity = Integer.parseInt(getItToBeNatural(typed));
                }
            }
            Printer.getInstance().print("If you wish to change '"+itemModel.getName()+"' full quantity to: "+checkedFullQuantity+" press Y. If you want to keep it as: "+itemModel.getFullQuantity()+" press N.");
            String typedAnswer = scanner.nextLine();
            if(getAnswer(typedAnswer)){
                itemModel.changeFullQuantity(checkedFullQuantity);
                Printer.getInstance().print("Full Quantity changed Successfully");
                return new ItemView(BackendController.getInstance().getItem(itemModel.getItemID()));
            }
        }
        catch (Exception e){
            Printer.getInstance().print(e.getMessage());
        }
        return new ItemView(itemModel);
    }



    public View changeItemMinQuantity() {
        try{
            Printer.getInstance().print("The current minimal quantity for the Item: "+ itemModel.getName()+" is "+itemModel.getMinimalQuantity());
            Printer.getInstance().print("Type the new minimal quantity");
            String typedQuantity = scanner.nextLine();
            int checkedQuantity = Integer.parseInt(getItToBeNatural(typedQuantity));
            boolean check = false;
            while(!check) {
                check=isLargerThan(getItemModel().getFullQuantity(),checkedQuantity);
                if(!check) {
                    Printer.getInstance().print("Must be smaller than the full quantity. Write the number again:");
                    String typed = scanner.nextLine();
                    checkedQuantity = Integer.parseInt(getItToBeNatural(typed));
                }
            }
            Printer.getInstance().print("If you wish to change '"+itemModel.getName()+"' minimal quantity to: "+checkedQuantity+" press Y. If you want to keep it as: "+itemModel.getMinimalQuantity()+" press N.");
            String typedAnswer = scanner.nextLine();
            if(getAnswer(typedAnswer)){
                itemModel.changeMinimalQuantity(checkedQuantity);
                Printer.getInstance().print("Minimal Quantity changed Successfully");
                return new ItemView(BackendController.getInstance().getItem(itemModel.getItemID()));
            }
        }
        catch (Exception e){
            Printer.getInstance().print(e.getMessage());
        }
        return new ItemView(itemModel);
    }

    public View moveItemLocation() {
        try{
            Printer.getInstance().print("If you would like to move quantity from Back room to Store press 0");
            Printer.getInstance().print("If you would like to move quantity from Store to Back room press 1");
            Printer.getInstance().print("If you would like to get to the previous menu press back");
            String typedAnswer = scanner.nextLine();
            boolean fromBackRoom = false;
            switch (typedAnswer) {
                case "0":
                    fromBackRoom = true;
                    break;
                case "1":
                    break;
                case "back":
                    return new ItemView(itemModel);
                default:
                    Printer.getInstance().print("Invalid Input");
                    moveItemLocation();
            }
            Printer.getInstance().print("Please enter the amount you want to move between rooms: ");
            String typedAmount = scanner.nextLine();
            int checkedAmount = Integer.parseInt(getItToBeNatural(typedAmount));
            itemModel.moveItems(itemModel.getItemID(), fromBackRoom, checkedAmount);
            Printer.getInstance().print("Moved Successfully");
        }
        catch (Exception e){
            Printer.getInstance().print(e.getMessage());
        }
        return new ItemView(itemModel);
    }

    public View getAllFrontShelves() {
        try{
            List<Integer> shelves = itemModel.getAllFrontShelves().stream().sorted().collect(Collectors.toList());
            for (int id : shelves)
                Printer.getInstance().print(String.valueOf(id));
        }
        catch (Exception e){
            Printer.getInstance().print(e.getMessage());
        }
        return new ItemView(itemModel);
    }

    public View getAllBackShelves() {
        try{
            List<Integer> shelves = itemModel.getAllBackShelves().stream().sorted().collect(Collectors.toList());
            for (int id : shelves)
                Printer.getInstance().print(String.valueOf(id));
        }
        catch (Exception e){
            Printer.getInstance().print(e.getMessage());
        }
        return new ItemView(itemModel);
    }

    public View changeShelves() {
        try{
            List<Integer>backShelves = itemModel.getAllBackShelves();
            List<Integer>frontShelves = itemModel.getAllFrontShelves();
            Printer.getInstance().print("Current Item's BackRoom shelves:");
            for (Integer id : backShelves)
                Printer.getInstance().print(String.valueOf(id));
            Printer.getInstance().print("Current Item's FrontRoom shelves:");
            for (Integer id : frontShelves)
                Printer.getInstance().print(String.valueOf(id));
            if(backShelves.isEmpty())
                addBackFrontShelves(true);
            if(frontShelves.isEmpty())
                addBackFrontShelves(false);
            if(!backShelves.isEmpty() && !frontShelves.isEmpty()){
                Printer.getInstance().print("To change the Item's Shelves in the BackRoom enter 'back'");
                Printer.getInstance().print("To change the Item's Shelves in the FrontRoom enter 'front'");
                String answer = scanner.nextLine();
                if (answer.equals("back"))
                    addBackFrontShelves(true);
                else if (answer.equals("front"))
                    addBackFrontShelves(false);
                else {
                    Printer.getInstance().print("Illegal input, enter front or back again");
                    return changeShelves();
                }
            }
            Printer.getInstance().print("Success");
        }
        catch (Exception e){
            Printer.getInstance().print(e.getMessage());
        }
        return new ItemView(itemModel);
    }

    public View removeDiscount() {
        try {
            Printer.getInstance().print("Enter the start date of the discount, format dd/mm/yyyy");
            String startDate = scanner.nextLine();
            LocalDate fromDate = makeDate(startDate);
            itemModel.removeDiscount(fromDate);
            Printer.getInstance().print("Success");
        }catch (Exception e){
            Printer.getInstance().print(e.getMessage());
        }
        return new ItemView(BackendController.getInstance().getItem(itemModel.getItemID()));
    }

    private void addBackFrontShelves(boolean isBackShelves) {
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
                    if(isBackShelves)
                        itemModel.changeItemBackShelves(idsIntArr);
                    else
                        itemModel.changeItemFrontShelves(idsIntArr);
                }
            } else {
                Printer.getInstance().print("There isn't back shelves");
            }
            check = true;
        }
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

    private String getItToBeNatural(String amount) {
        while (!(amount.matches("-?\\d+") && Integer.parseInt(amount) > 0 && Math.floor(Integer.parseInt(amount)) == Integer.parseInt(amount))) {
            Printer.getInstance().print("Must be non-negative natural number. Write the number again:");
            amount = scanner.nextLine();
        }
        return amount;
    }

    private LocalDate makeDate(String sinceDateStr) {
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(sinceDateStr, formatter);
        }catch (Exception e){
            Printer.getInstance().print("The entered date was not in the format - dd/MM/yyyy");
            sinceDateStr = scanner.nextLine();
            return makeDate(sinceDateStr);
        }
    }

    private double getItToBeBetween0and1(String amount) {
        while (!(amount.matches("[+-]?([0-9]*[.])?[0-9]+") && Float.parseFloat(amount) >= 0 && Float.parseFloat(amount) <= 1)){
            Printer.getInstance().print("Must be a number between 0 and 1. Write the number again:");
            amount = scanner.nextLine();
        }
        return Float.parseFloat(amount);
    }

    private boolean getAnswer(String typedAnswer) {
        switch (typedAnswer) {
            case "N":
                return false;
            case "Y":
                return true;
            default:
                Printer.getInstance().print("Please Enter either Y or N");
                Printer.getInstance().print("If you wish to confirm, press Y. If you want to return to the previous menu press N.");
                typedAnswer = scanner.nextLine();
                return getAnswer(typedAnswer);
        }
    }

    private String checkIfFloatPositive(String typedPrice) {
        while(!(typedPrice.matches("[+-]?([0-9]*[.])?[0-9]+") && (Float.parseFloat(typedPrice) > 0))){
            Printer.getInstance().print("Must be positive decimal number. Write the number again:");
            typedPrice = scanner.nextLine();
        }
        return typedPrice;
    }

    private boolean isLargerThan(int parseInt, int checkedMinimalQuantity) {
        if(parseInt <= checkedMinimalQuantity){
            return false;
        }
        return true;
    }
}
