package Presentation.ViewModel.InventoryViewModel;

import Presentation.Model.*;
import Presentation.Model.InventoryModel.BasicItemModel;
import Presentation.Model.InventoryModel.InventoryItemModel;
import Presentation.Model.InventoryModel.PurchasedItemModel;
import Presentation.View.*;
import Presentation.View.InventoryView.PurchasedReportView;
import Presentation.View.InventoryView.ReportsMenuView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class PurchasedItemViewModel {
    Scanner scanner;
    public PurchasedItemViewModel() {
        scanner = new Scanner(System.in);
    }

    public View getReport() {
        try{
            List<PurchasedItemModel> items = PurchasedItemModel.getReport();
            Printer.getInstance().print(makeStringReport(items));
            return new PurchasedReportView();
        }
        catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to the previous screen");
            return new PurchasedReportView();
        }
    }

    public View getReportByDate() {
        try{
            Printer.getInstance().print("Enter since start date in format - dd/MM/yyyy");
            String sinceDateStr = scanner.nextLine();
            LocalDate sinceDate = makeDate(sinceDateStr);
            Printer.getInstance().print("Enter since end date in format - dd/MM/yyyy");
            String toDateStr = scanner.nextLine();
            LocalDate toDate = makeDate(toDateStr);
            List<PurchasedItemModel> items = PurchasedItemModel.getReportByDate(sinceDate,toDate);
            Printer.getInstance().print(makeStringReport(items));
            return new PurchasedReportView();
        }
        catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to the previous screen");
            return new PurchasedReportView();
        }
    }

    public View getReportByItemID() {
        try{
            int itemID = selectItemID();
            List<PurchasedItemModel> items = PurchasedItemModel.getReportByItemID(itemID);
            Printer.getInstance().print(makeStringReport(items));
            return new PurchasedReportView();
        }
        catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to the previous screen");
            return new PurchasedReportView();
        }
    }

    public View getPurchaseReportByBusinessNumber() {
        try{
            Printer.getInstance().print("Enter the required supplier business number");
            String supplierNumber = scanner.nextLine();
            int checkedSupplier = Integer.parseInt(getItToBeNatural(supplierNumber));
            List<PurchasedItemModel> items = PurchasedItemModel.getPurchaseReportByBusinessNumber(checkedSupplier);
            Printer.getInstance().print(makeStringReport(items));
            return new PurchasedReportView();
        }
        catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to the previous screen");
            return new PurchasedReportView();
        }
    }

    public View addPurchasedItem() {
        try {
            Printer.getInstance().print("Enter the item iD");
            String desiredItemIDString = scanner.nextLine();
            int desiredItemID = Integer.parseInt(getItToBeNatural(desiredItemIDString));
            BackendController.getInstance().getItem(desiredItemID);
            Printer.getInstance().print("Enter the Order ID:");
            String typedOrderID = scanner.nextLine();
            int checkedOrderID = Integer.parseInt(getItToBeNatural(typedOrderID));
            Printer.getInstance().print("Enter the Supplier Business Number:");
            String typedSupplier = scanner.nextLine();
            int checkedSupplier = Integer.parseInt(getItToBeNatural(typedSupplier));
            Printer.getInstance().print("Enter the amount that was bought:");
            String typedAmount = scanner.nextLine();
            int checkedAmount = Integer.parseInt(getItToBeNatural(typedAmount));
            Printer.getInstance().print("Enter the unit price:");
            String typedPrice = scanner.nextLine();
            int checkedPrice = Integer.parseInt(getItToBeNatural(typedPrice));
            Printer.getInstance().print("Enter the discount the supplier gave for the purchase (0 if none):");
            String typedDiscount = scanner.nextLine();
            double checkedDiscount = getItToBeBetween0and1(typedDiscount);
            //got all details. Now creating and sending the new record to the report.
            Printer.getInstance().print("If you wish to confirm, press Y. If you want to return to the previous menu press N.");
            String typedAnswer = scanner.nextLine();
            if(getAnswer(typedAnswer)){
                BackendController.getInstance().addPurchase(desiredItemID, checkedOrderID, checkedSupplier, checkedAmount, checkedPrice, checkedDiscount);
                Printer.getInstance().print("Recorded Successfully");
                return new ReportsMenuView();
            }
            Printer.getInstance().print("Record Cancelled");
            return new PurchasedReportView();
        } catch (Exception e) {
            Printer.getInstance().print("Input is illegal\nfor going back enter: back ,for try again enter anything");
            if(scanner.nextLine().equals("back"))
                return new PurchasedReportView();
            addPurchasedItem();
        }
        return new PurchasedReportView();
    }

    public static List<InventoryItemModel> getInventoryReport() {
        return BackendController.getInstance().getInventoryReport();
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

    private String makeStringReport(List<PurchasedItemModel> items) {
        String str ="";
        for (PurchasedItemModel item : items){
            str += "Name: "+item.getName()+", ID:"+item.getItemID()+" ,Category name: "+item.getCategoryName()+"\n"+
                    " ,Date purchased: "+item.getDatePurchased()+" ,Quantity purchased: "+item.getAmount()+" ,Order ID: "+item.getOrderID()+" ,Supplier number: "+item.getSupplierBusinessNumber()+"\n"+
                    " ,Unit price from supplier: "+item.getUnitPriceFromSupplier()+" ,Discount received from supplier (between 0.00 to 1.00 [0.3 = 30% off]): "+item.getDiscountFromSupplier();
            str += " ,Manufacture: "+item.getManufacture()+"\n\n";
        }
        return str;
    }

    private int selectItemID() {
        List<String[]> itemsNameAndID = getAllBasicItems();
        for(String[] item : itemsNameAndID){
            Printer.getInstance().print("Name: "+item[0]+"ID: "+item[1]);
        }
        Printer.getInstance().print("Items are sorted by alphabetical order");
        Printer.getInstance().print("Please enter the required Item ID");
        String typedID = scanner.nextLine();
        int checkedID = Integer.parseInt(getItToBeNatural(typedID));
        return checkedID;
    }

    private static List<String[]> getAllBasicItems() {
        List<InventoryItemModel> inventoryItems = getInventoryReport();
        List<String[]> relevantItems = new ArrayList<String[]>();
        inventoryItems.stream().sorted(Comparator.comparing(BasicItemModel::getName));
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
}
