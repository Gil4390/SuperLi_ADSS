package Presentation.View.InventoryView;

import Presentation.Model.InventoryModel.CategoryModel;
import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.InventoryViewModel.CategoryViewModel;

public class CategoryView implements View {
    private final CategoryViewModel categoryViewModel;
    public CategoryView(CategoryModel cat){
        categoryViewModel= new CategoryViewModel(cat);
    }

    @Override
    public void printMenu() {
        System.out.println("--------------Category "+categoryViewModel.getCategoryModel().getName()+"--------------");
        System.out.println("Name: "+categoryViewModel.getCategoryModel().getName());
        System.out.println("ID: "+categoryViewModel.getCategoryModel().getCatID());
        System.out.println("Father ID: "+categoryViewModel.getCategoryModel().getCatFatherID());
        System.out.println();
        System.out.println();
        System.out.println("0- Go to the parent category");
        System.out.println("1- Remove Category");
        System.out.println("2- Get all the category items");
        System.out.println("3- Change category name");
        System.out.println("4- Add a discount to all category items");
        System.out.println("5- Remove a discount to all category items");
        System.out.println("6. Go to the previous menu");
        System.out.println("7. To logout from the system");
    }


    @Override
    public View nextInput(String input) {
        switch(input){
            case "1": return categoryViewModel.removeCategory();
            case "2": return categoryViewModel.getCatItems();
            case "3": return categoryViewModel.setName();
            case "4": return categoryViewModel.addDiscount();
            case "5": return categoryViewModel.removeDiscount();
            case "0": {
                if (categoryViewModel.getCategoryModel().getCatFatherID() == null) return new CategoryMenuView();
                else return categoryViewModel.getFather();
            }
            case "6": return new InventoryMainMenuView();
            case "7": return new MainMenuView(true);
            case "close": ApplicationView.shouldTerminate = true;
                break;
            default: System.out.println("Invalid Input");
        }
        return this;
    }
}