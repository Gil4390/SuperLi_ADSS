package Presentation.ViewModel.InventoryViewModel;

import Presentation.Model.BackendController;
import Presentation.Model.InventoryModel.CategoryModel;
import Presentation.Model.InventoryModel.ItemModel;
import Presentation.View.*;
import Presentation.View.InventoryView.CategoryMenuView;
import Presentation.View.InventoryView.CategoryView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class CategoryViewModel {

    CategoryModel categoryModel;
    Scanner scanner;

    public CategoryViewModel(CategoryModel cat){
        categoryModel=cat;
        scanner = new Scanner(System.in);
    }

    public CategoryModel getCategoryModel() {return categoryModel;}

    public View getCatItems() {
        try{
            List <ItemModel> itemList = categoryModel.getCategoryItems();
            for (ItemModel item : itemList)
                Printer.getInstance().print(item.getName());
            return new CategoryView(categoryModel);
        }catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem : Returning to category main menu");
            return new CategoryMenuView();
        }
    }

    public View getFather() {
        try{
            CategoryView categoryView = new CategoryView(categoryModel.getCatFather());
            return categoryView;
        }catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem : Returning to category main menu");
            return new CategoryMenuView();
        }
    }

    public CategoryModel selectCategory() {
        try{
            CategoryModel [] catList = categoryModel.getSubCat();
            if(catList.length>0) {
                Printer.getInstance().print("Current Category: ------"+categoryModel.getName()+"------");
                Printer.getInstance().print("Enter the next category number or press 0 to select the current category");

                int counter = 1;
                for (CategoryModel cat : catList) {
                    Printer.getInstance().print(counter+". "+cat.getName());
                    counter++;
                }

                Printer.getInstance().print("-1. Back");
                Printer.getInstance().print("-2. Category menu.");
                int choice = scanner.nextInt();

                switch (choice) {
                    case -2:
                        return null;
                    case -1:
                        if (categoryModel.getCatFatherID() == null)
                            return null;
                        return new CategoryViewModel(new CategoryModel("father", categoryModel.getCatFatherID(), null).getCategory()).selectCategory();
                    case 0:
                        return categoryModel;
                    default:
                        if (choice > 0 && catList.length >= choice)
                            return new CategoryViewModel(catList[choice - 1]).selectCategory();
                        else
                            Printer.getInstance().print("Selected wrong number");
                        return selectCategory();
                }
            }
            Printer.getInstance().print("---Selected: "+categoryModel.getName()+"---");
            return categoryModel;
        }catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem : Returning to category main menu");
            return null;
        }

    }

    public View setName() {
        try{
            Printer.getInstance().print("Enter category new name");
            String newName = scanner.nextLine();
            categoryModel.setName(newName);
            return new CategoryView(categoryModel);
        }catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem : Returning to category main menu");
            return new CategoryMenuView();
        }
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
            categoryModel.addDiscountToAllItemsInCategory(discountFare,discountName,fromDate,toDate);
            Printer.getInstance().print("Success");
        }
        catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("wrong input");
        }
        return new CategoryView(categoryModel);
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

    public View removeDiscount() {
        try{
            Printer.getInstance().print("Enter the start date of the discount, format dd/mm/yyyy");
            String startDate = scanner.nextLine();
            LocalDate fromDate = makeDate(startDate);
            List<ItemModel> items = categoryModel.getCategoryItems();
            for (ItemModel item : items)
                item.removeDiscount(fromDate);
            Printer.getInstance().print("Success");
        }catch (Exception e){
            Printer.getInstance().print(e.getMessage());
        }
        return new CategoryView(categoryModel);
    }

    private double getItToBeBetween0and1(String amount) {
        while (!(amount.matches("[+-]?([0-9]*[.])?[0-9]+") && Float.parseFloat(amount) >= 0 && Float.parseFloat(amount) <= 1)){
            Printer.getInstance().print("Must be a number between 0 and 1. Write the number again:");
            amount = scanner.nextLine();
        }
        return Float.parseFloat(amount);
    }

    public View removeCategory() {
        try {
            BackendController.getInstance().removeCategory(categoryModel.getCatID());
            return new CategoryMenuView();
        } catch (Exception e) {
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem : Returning to category main menu");
            return new CategoryMenuView();
        }
    }
}
