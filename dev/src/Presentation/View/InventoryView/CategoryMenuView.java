package Presentation.View.InventoryView;

import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.InventoryViewModel.CategoryMenuViewModel;

public class CategoryMenuView implements View {
    private final CategoryMenuViewModel categoryMenuViewModel;

    public CategoryMenuView(){
        categoryMenuViewModel=new CategoryMenuViewModel();
    }
    @Override
    public void printMenu() {
        System.out.println("-----------------Category Menu-----------------");
        System.out.println("1. Add category");
        System.out.println("2. View categories");
        System.out.println("3. Back");
        System.out.println("4. Logout");
    }


    @Override
    public View nextInput(String input) {
        switch(input){
            case "1":
                return categoryMenuViewModel.addCategory();
            case "2":
                return categoryMenuViewModel.viewCategories();
            case "3":
                return new InventoryMainMenuView();
            case "4":
                return new MainMenuView(true);
            case "close":
                ApplicationView.shouldTerminate = true;
                break;
            default:
                System.out.println("Invalid Input");
        }
        return this;
    }
}
