package Presentation.ViewModel.InventoryViewModel;

import Presentation.Model.*;
import Presentation.Model.InventoryModel.CategoryModel;
import Presentation.Model.InventoryModel.InventoryItemModel;
import Presentation.View.*;
import Presentation.View.InventoryView.CategoryMenuView;
import Presentation.View.InventoryView.InventoryReportView;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class InventoryItemViewModel {
    Scanner scanner;
    public InventoryItemViewModel(){
        scanner = new Scanner(System.in);
    }

    public View getReport() {
        try{
            List<InventoryItemModel> items = InventoryItemModel.getReport();
            items.stream().sorted(Comparator.comparing(InventoryItemModel::getName));
            Printer.getInstance().print(makeStringReport(items));
            return new InventoryReportView();
        }catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to the previous screen");
            return new InventoryReportView();
        }
    }

    public View getBasicReport() {
        try{
            List<InventoryItemModel> items = InventoryItemModel.getReport();
            items.stream().sorted(Comparator.comparing(InventoryItemModel::getName));
            Printer.getInstance().print(makeBasicStringReport(items));
            return new InventoryReportView();
        }catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to the previous screen");
            return new InventoryReportView();
        }
    }

    public View getReportByCategory() {
        try{
            CategoryModel categoryModel = BackendController.getInstance().getCategory("0");
            Printer.getInstance().print("Select a category to filter results");
            CategoryViewModel categoryViewModel = new CategoryViewModel(categoryModel);
            CategoryModel chosenCategory = categoryViewModel.selectCategory();
            List<InventoryItemModel> items;
            if(chosenCategory != null) {
                items = InventoryItemModel.getReportByCategory(chosenCategory);
                Printer.getInstance().print(makeStringReport(items));
            }
            return new InventoryReportView();
        }catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to the previous screen");
            return new InventoryReportView();
        }
    }

    public View getShortReportByCategory() {
        try{
            CategoryModel categoryModel = BackendController.getInstance().getCategory("0");
            Printer.getInstance().print("Select a category to filter results");
            CategoryViewModel categoryViewModel = new CategoryViewModel(categoryModel);
            List<InventoryItemModel> items = InventoryItemModel.getReportByCategory(categoryViewModel.selectCategory());
            Printer.getInstance().print(makeBasicStringReport(items));
            return new InventoryReportView();
        }catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to the previous screen");
            return new InventoryReportView();
        }
    }

    private String makeStringReport(List<InventoryItemModel> items) {
        String str ="";
        for (InventoryItemModel item : items){
            str += "Name: "+item.getName()+", ID:"+item.getItemID()+" ,Category name: "+item.getCategoryName()+" ,Front amount: "+item.getFrontAmount()+" ,Back amount: "+item.getBackAmount()+"\n"+
                    " ,Total amount: "+item.getTotalAmount()+" ,Price in store: "+item.getPriceInStore()+" ,Price after discount: "+item.getPriceAfterDiscounts()+ " ,Manufacture: "+item.getManufacture()+"\n\n";
        }
        return str;
    }
    private String makeBasicStringReport(List<InventoryItemModel> items) {
        String str ="";
        for (InventoryItemModel item : items)
            str += "Name: "+item.getName()+", ID:"+item.getItemID()+"\n\n";
        return str;
    }


}
