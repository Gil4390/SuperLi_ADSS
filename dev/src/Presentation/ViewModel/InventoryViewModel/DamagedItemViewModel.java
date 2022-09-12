package Presentation.ViewModel.InventoryViewModel;

import Presentation.Model.*;
import Presentation.Model.InventoryModel.*;
import Presentation.View.*;
import Presentation.View.InventoryView.DamagedReportView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DamagedItemViewModel {
    Scanner scanner;

    public DamagedItemViewModel() {
        scanner = new Scanner(System.in);
    }

    public View getReport() {
        try {
            Printer.getInstance().print("to get only Defective items enter 1");
            Printer.getInstance().print("to get only Expired items enter 2");
            Printer.getInstance().print("to get all Damaged items enter 3");
            String str = scanner.nextLine();
            List<DamagedItemModel> items = new LinkedList<>();
            switch (str) {
                case "1":
                    items = DamagedItemModel.getDefectiveReport();
                    break;
                case "2":
                    items = DamagedItemModel.getExpiredReport();
                    break;
                case "3":
                    items = DamagedItemModel.getReport();
                    break;
                default: {
                    Printer.getInstance().print("Invalid input");
                    return new DamagedReportView();
                }
            }
            Printer.getInstance().print(makeStringReport(items));
            return new DamagedReportView();
        } catch (Exception e) {
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to the previous screen");
            return new DamagedReportView();
        }
    }

    public View getReportByDate() {
        try {
            Printer.getInstance().print("Enter since start date in format - dd/MM/yyyy");
            String sinceDateStr = scanner.nextLine();
            LocalDate sinceDate = makeDate(sinceDateStr);
            Printer.getInstance().print("Enter since end date in format - dd/MM/yyyy");
            String toDateStr = scanner.nextLine();
            LocalDate toDate = makeDate(toDateStr);

            Printer.getInstance().print("to get only Damaged items enter 1");
            Printer.getInstance().print("to get only Expired items enter 2");
            Printer.getInstance().print("to get all Damaged items enter 3");
            String str = scanner.nextLine();
            List<DamagedItemModel> items = new LinkedList<>();
            switch (str) {
                case "1":
                    items = DamagedItemModel.getDamagedReportByDate(sinceDate, toDate);
                    break;
                case "2":
                    items = DamagedItemModel.getExpiredReportByDate(sinceDate, toDate);
                    break;
                case "3":
                    items = DamagedItemModel.getReportByDate(sinceDate, toDate);
                    break;
                default: {
                    Printer.getInstance().print("Invalid input");
                    return new DamagedReportView();
                }
            }
            Printer.getInstance().print(makeStringReport(items));
            return new DamagedReportView();

        } catch (Exception e) {
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to the previous screen");
            return new DamagedReportView();
        }
    }

    public View getReportByItemID() {
        try {
            int itemID = selectItemID();
            Printer.getInstance().print("to get only Damaged items enter 1");
            Printer.getInstance().print("to get only Expired items enter 2");
            Printer.getInstance().print("to get all Damaged items enter 3");
            String str = scanner.nextLine();
            List<DamagedItemModel> items = new LinkedList<>();
            switch (str) {
                case "1":
                    items = DamagedItemModel.getDamagedReportByItemID(itemID);
                    break;
                case "2":
                    items = DamagedItemModel.getExpiredReportByItemID(itemID);
                    break;
                case "3":
                    items = DamagedItemModel.getReportByItemID(itemID);
                    break;
                default: {
                    Printer.getInstance().print("Invalid input");
                    return new DamagedReportView();
                }
            }
            Printer.getInstance().print(makeStringReport(items));
            return new DamagedReportView();
        } catch (Exception e) {
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to the previous screen");
            return new DamagedReportView();
        }
    }

    public View addDamagedItem() {
        try{
            Printer.getInstance().print("Enter the item id");
            String desiredItemID = scanner.nextLine();
            int itemID = Integer.parseInt(getItToBeNatural(desiredItemID));
            ItemModel item = BackendController.getInstance().getItem(itemID);
            Printer.getInstance().print("Enter the amount of the item that was damaged or expired:");
            String typedAmount = scanner.nextLine();
            int checkedAmount = Integer.parseInt(getItToBeNatural(typedAmount));
            Printer.getInstance().print("The items you found were damaged or expired?");
            Printer.getInstance().print("Damaged - press D");
            Printer.getInstance().print("Expired - press E");
            String typedReason = scanner.nextLine();
            PDamageReason checkedReason = getItToBeEnum(typedReason);
            Printer.getInstance().print("Have you found the items in the backroom or at the store?");
            Printer.getInstance().print("backroom - press Y");
            Printer.getInstance().print("store - press N");
            String typedLocation = scanner.nextLine();
            int checkedLocation = getItToBe0or1(typedLocation);
            //got all details. Now creating and sending the new record to the report.
            Printer.getInstance().print("If you wish to confirm, press Y. If you want to return to the previous menu press N.");
            String typedAnswer = scanner.nextLine();
            if(getAnswer(typedAnswer)){
                BackendController.getInstance().addDamagedRecord(itemID, checkedAmount, checkedReason, checkedLocation);
                Printer.getInstance().print("Recorded Successfully");
                return new DamagedReportView();
            }
            Printer.getInstance().print("Record Cancelled");
            return new DamagedReportView();
        }catch (Exception e){
            Printer.getInstance().print("Input is illegal\nfor going back enter: back ,for try again enter anything");
            if(scanner.nextLine().equals("back"))
                return new DamagedReportView();
            addDamagedItem();
        }
        return new DamagedReportView();
    }

    public static List<InventoryItemModel> getInventoryReport() {
        return BackendController.getInstance().getInventoryReport();
    }

    private LocalDate makeDate(String sinceDateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(sinceDateStr, formatter);
        } catch (Exception e) {
            Printer.getInstance().print("The entered date was not in the format - dd/MM/yyyy");
            sinceDateStr = scanner.nextLine();
            return makeDate(sinceDateStr);
        }
    }

    private String makeStringReport(List<DamagedItemModel> items) {
        String str = "";
        for (DamagedItemModel item : items) {
            str += "Name: " + item.getName() + " ,ID: " + item.getItemID() + " ,Category name: " + item.getCategoryName() + "\n" +
                    " ,Date found: ," + item.getDateFound() + " ,Quantity Found: " + item.getQuantityFound() + " ,Price in store: " + item.getPriceInStore() + "\n" +
                    " ,Reason: " + item.getExpiredOrFault().toString();
            if (item.isFrontRoom())
                str += " Location in store: Front room";
            else
                str += " Location in store: Back room";
            str += " ,Manufacture: " + item.getManufacture() + "\n\n";
        }
        return str;
    }

    private int getItToBe0or1(String typedLocation) {
        if (typedLocation.equals("Y"))
            return 0;
        else if (typedLocation.equals("N"))
            return 1;
        Printer.getInstance().print("!!Answer must be either Y or N!!");
        Printer.getInstance().print("Have you found the items in the backroom or at the store?");
        Printer.getInstance().print("backroom - press Y");
        Printer.getInstance().print("store - press N");
        typedLocation = scanner.nextLine();
        return getItToBe0or1(typedLocation);
    }

    private PDamageReason getItToBeEnum(String typedReason) {
        if (typedReason.equals("D"))
            return PDamageReason.Damaged;
        else if (typedReason.equals("E"))
            return PDamageReason.Expired;
        Printer.getInstance().print("!!Answer must be either D or E!!");
        Printer.getInstance().print("The items you found were damaged or expired?");
        Printer.getInstance().print("Damaged - press D");
        Printer.getInstance().print("Expired - press E");
        typedReason = scanner.nextLine();
        return getItToBeEnum(typedReason);
    }

    private int selectItemID() {
        List<String[]> itemsNameAndID = getAllBasicItems();
        for(String[] item : itemsNameAndID){
            Printer.getInstance().print("Name: "+item[0]+" ID: "+item[1]);
        }
        Printer.getInstance().print("Items are sorted by alphabetical order");
        Printer.getInstance().print("Please enter the required Item ID");
        String typedID = scanner.nextLine();
        int checkedID = Integer.parseInt(getItToBeNatural(typedID));
        return checkedID;
    }

    private static List<String[]> getAllBasicItems() {
        List<InventoryItemModel> inventoryItems = getInventoryReport();
        inventoryItems.stream().sorted(Comparator.comparing(BasicItemModel::getName));
        List<String[]> relevantItems = new ArrayList<String[]>();
        for(InventoryItemModel item : inventoryItems) {
            relevantItems.add(new String[]{item.getName(), String.valueOf(item.getItemID())});
        }
        return relevantItems;
    }

    private String getItToBeNatural(String amount) {
        while (!(amount.matches("-?\\d+") && Integer.parseInt(amount) > 0 && Math.floor(Integer.parseInt(amount)) == Integer.parseInt(amount))) {
            Printer.getInstance().print("Must be non-negative natural number. Write the number again:");
            amount = scanner.nextLine();
        }
        return amount;
    }

    private boolean getAnswer(String typedAnswer) {
        switch(typedAnswer){
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
}
