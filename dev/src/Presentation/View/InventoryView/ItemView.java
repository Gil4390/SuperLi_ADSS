package Presentation.View.InventoryView;

import Presentation.Model.InventoryModel.ItemModel;
import Presentation.View.ApplicationView;
import Presentation.View.MainMenuView;
import Presentation.View.View;
import Presentation.ViewModel.InventoryViewModel.ItemViewModel;

public class ItemView implements View {
    private final ItemViewModel itemViewModel;
    public ItemView(ItemModel item){
        itemViewModel = new ItemViewModel(item);
    }

    @Override
    public void printMenu() {
        System.out.println("-----------Current Item: "+itemViewModel.getItemModel().getName()+"-----------");
        System.out.println("Item data:");
        System.out.println("Name: "+itemViewModel.getItemModel().getName());
        System.out.println("ID: "+itemViewModel.getItemModel().getItemID());
        System.out.println("Category ID: "+itemViewModel.getItemModel().getCategoryId());
        System.out.println("Current Price: "+itemViewModel.getItemModel().getCurrPrice());
        System.out.println("Current Price after discounts: "+itemViewModel.getItemModel().getFinalCurrentPrice());
        System.out.println("Current Quantity in store: "+itemViewModel.getItemModel().getQuantityFrontRoom());
        System.out.println("Current Quantity in back room: "+itemViewModel.getItemModel().getQuantityBackRoom());
        System.out.println("Minimal quantity: "+itemViewModel.getItemModel().getMinimalQuantity());
        System.out.println("Full quantity: "+itemViewModel.getItemModel().getFullQuantity());
        System.out.println("Manufacture: "+itemViewModel.getItemModel().getManufacture());
        System.out.println();
        System.out.println();
        System.out.println("1- Buy this item");
        System.out.println("2- Give discount for this item");
        System.out.println("3- Remove discount for this item");
        System.out.println("4- Get all items front shelves enter 4");
        System.out.println("5- Get all items back shelves enter 5");
        System.out.println("6- Change the shelves in one of the rooms enter 6");
        System.out.println("7- Fix amount writings of the item enter 7");
        System.out.println("8- Fix the price of the item enter 8");
        System.out.println("9- Fix the minimal quantity of the item enter 9");
        System.out.println("10- Fix the full quantity of the item enter 10");
        System.out.println("11- Go to the previous menu");
        System.out.println("12- To logout from the system");
    }


    @Override
    public View nextInput(String input) {
        switch(input){
            case "1":
                return itemViewModel.buyItem();
            case "2":
                return itemViewModel.addDiscount();
            case "3":
                return itemViewModel.removeDiscount();
            case "4":
                return itemViewModel.getAllFrontShelves();
            case "5":
                return itemViewModel.getAllBackShelves();
            case "6":
                return itemViewModel.changeShelves();
            case "7":
                return itemViewModel.changeItemQuantity();
            case "8":
                return itemViewModel.changeItemPrice();
            case "9":
                return itemViewModel.changeItemMinQuantity();
            case "10":
                return itemViewModel.changeItemFullQuantity();
            case "11":
                return new ItemMenuView();
            case "12":
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