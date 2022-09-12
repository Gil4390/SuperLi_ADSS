package Presentation.View.InventoryView;

import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.InventoryViewModel.ShelvesViewModel;

public class ShelvesMenuView implements View {
    ShelvesViewModel shelvesViewModel;

    public ShelvesMenuView(){
        shelvesViewModel=new ShelvesViewModel();
    }
    @Override
    public void printMenu() {
        System.out.println("--------------Shelves main menu--------------");
        System.out.println("1. Add shelf");
        System.out.println("2. Get all front shelves");
        System.out.println("3. Get all back shelves");
        System.out.println("4. Back");
        System.out.println("5. Logout");
    }


    @Override
    public View nextInput(String input) {
        switch(input){
            case "1":
                return shelvesViewModel.addShelf();
            case "2":
                return shelvesViewModel.getAllFrontShelves();
            case "3":
                return shelvesViewModel.getAllBackShelves();
            case "4":
                return new InventoryMainMenuView();
            case "5":
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
