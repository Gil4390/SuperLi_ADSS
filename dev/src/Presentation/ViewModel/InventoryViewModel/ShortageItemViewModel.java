package Presentation.ViewModel.InventoryViewModel;

import Presentation.Model.BackendController;
import Presentation.Model.InventoryModel.ShortageItemModel;
import Presentation.View.*;
import Presentation.View.InventoryView.ShortageReportView;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShortageItemViewModel {
    Scanner scanner;
    public ShortageItemViewModel(){scanner = new Scanner(System.in);}

    public View getReport() {
        try{
            List<ShortageItemModel> items = ShortageItemModel.getReport();
            Printer.getInstance().print(makeStringReport(items));
            return new ShortageReportView();
        }
        catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to the previous screen");
            return new ShortageReportView();
        }
    }

    public View orderShortage(){
        try{
            List<ShortageItemModel> items = ShortageItemModel.getReport();
            List<Pair<Integer, Integer>> itemsToOrder = new ArrayList<>();
            for(ShortageItemModel itemInShort : items){
                itemsToOrder.add(new Pair<>(itemInShort.getItemID(), itemInShort.getFullQuantity()-itemInShort.getCurrentQuantity()));
            }
            Printer.getInstance().print(makeOrderReport(items));
            if(itemsToOrder.isEmpty()){
                Printer.getInstance().print("There aren't any items to order. \n Returning to the previous Menu.");
                return new ShortageReportView();
            }
            Printer.getInstance().print("If you wish to proceed the purchase, press Y. \n If you want to return to the previous menu press N.");
            String typed = scanner.nextLine();
            if(getAnswer(typed)){
                BackendController.getInstance().orderShortage();
                Printer.getInstance().print("Purchase Sent.");
            }
            else{
                Printer.getInstance().print("Purchase Cancelled. Returning to the previous menu.");
            }
            return new ShortageReportView();
        }
        catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to the previous screen");
            return new ShortageReportView();
        }
    }

    private String makeOrderReport(List<ShortageItemModel> items) {
        String str ="";
        for (ShortageItemModel item : items){
            str += "ID: "+item.getItemID()+", Name: "+item.getName()+", amount to buy:"+(item.getFullQuantity()-item.getCurrentQuantity())+"\n\n";
        }
        return str;
    }

    private String makeStringReport(List<ShortageItemModel> items) {
        String str ="";
        for (ShortageItemModel item : items){
            str += "Name: "+item.getName()+", ID:"+item.getItemID()+" ,Current quantity: "+item.getCurrentQuantity()+"\n"+
                    " Minimal quantity: "+item.getMinimalQuantity()+" ,Category name: "+item.getCategoryName()+
                    " ,Manufacture: "+item.getManufacture()+"\n\n";
        }
        return str;
    }

    private boolean getAnswer(String typedAnswer) {
        switch (typedAnswer) {
            case "N":
                return false;
            case "Y":
                return true;
            default:
                Printer.getInstance().print("Please Enter either Y or N");
                Printer.getInstance().print("If you wish to proceed the purchase, press Y. If you want to return to the previous menu press N.");
                typedAnswer = scanner.nextLine();
                return getAnswer(typedAnswer);
        }
    }
}
