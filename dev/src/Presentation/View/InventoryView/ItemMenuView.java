package Presentation.View.InventoryView;

import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.InventoryViewModel.ItemMenuViewModel;

public class ItemMenuView implements View {
    private ItemMenuViewModel itemMenuViewModel;

    public ItemMenuView(){
        itemMenuViewModel = new ItemMenuViewModel();
    }
    @Override
    public void printMenu() {
        System.out.println("-----------------Items Menu-----------------");
        System.out.println("1. View and make changes on an item");
        System.out.println("2. Add an item");
        System.out.println("3. View items in a specific category");
        System.out.println("4. Back");
        System.out.println("5. Logout");
    }

    @Override
    public View nextInput(String input) {
        switch (input) {
            case "1":
                return itemMenuViewModel.viewItems();
            case "2":
                return itemMenuViewModel.addItem();
            case "3":
                return itemMenuViewModel.getAllItemInCategory();
            case "4":
                return new InventoryMainMenuView();
            case "5":
                return new MainMenuView(true);
            case "close":
                ApplicationView.shouldTerminate = true;
                break;
            default:
                System.out.println("Invalid input!");
        }
        return this;
    }
}
