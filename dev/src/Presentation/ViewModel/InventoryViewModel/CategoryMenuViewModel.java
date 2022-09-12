package Presentation.ViewModel.InventoryViewModel;

import Presentation.Model.BackendController;
import Presentation.Model.InventoryModel.CategoryModel;
import Presentation.View.*;
import Presentation.View.InventoryView.CategoryMenuView;
import Presentation.View.InventoryView.CategoryView;

import java.util.Scanner;

public class CategoryMenuViewModel {
    Scanner scanner;
    public CategoryMenuViewModel(){
        scanner = new Scanner(System.in);
    }

    public View addCategory() {
        try {
            Printer.getInstance().print("Select new father category");
            CategoryModel newFatherCat = selectCategory();
            if (newFatherCat == null)
                return new CategoryMenuView();
            Printer.getInstance().print("Enter new category name");
            String name = scanner.nextLine();
            CategoryModel categoryModel = new CategoryModel(name, "-1", newFatherCat.getCatID());
            categoryModel.addCategory();
            Printer.getInstance().print("Success");
            return new CategoryMenuView();
        }
        catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to the previous screen");
            return new CategoryMenuView();
        }
    }

    public View viewCategories() {
        try{
            CategoryModel cat = selectCategory();
            if(cat==null)
                return new CategoryMenuView();
            return new CategoryView(cat);
        }
        catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to the previous screen");
            return new CategoryMenuView();
        }

    }

    private CategoryModel selectCategory(){
        try {
            CategoryModel categoryModel = BackendController.getInstance().getCategory("0");
            CategoryViewModel categoryViewModel = new CategoryViewModel(categoryModel);
            return categoryViewModel.selectCategory();
        }catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            return null;
        }
    }
}
