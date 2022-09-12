package Presentation.ViewModel.InventoryViewModel;

import Presentation.Model.BackendController;
import Presentation.View.*;
import Presentation.View.InventoryView.ShelvesMenuView;

import java.util.List;
import java.util.Scanner;

public class ShelvesViewModel {
    Scanner scanner;

    public ShelvesViewModel(){
        scanner= new Scanner(System.in);
    }
    public View addShelf() {
        try{
            boolean valid = false;
            boolean isInBack = true;
            while(!valid) {
                Printer.getInstance().print("Enter front to create front shelf, or back for back shelf");
                String str = scanner.nextLine();
                if(str.equals("front") || str.equals("back")) {
                    valid = true;
                    if(str.equals("front"))
                        isInBack=false;
                }
                else
                    Printer.getInstance().print("Illegal input, please enter front or back");
            }
            BackendController.getInstance().addShelf(isInBack);
            Printer.getInstance().print("Success");
            return new ShelvesMenuView();
        }catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to category main menu");
            return new ShelvesMenuView();
        }
    }

    public View getAllFrontShelves() {
        try{
            List<Integer> shelvesList = BackendController.getInstance().getAllFrontShelves();
            for (int id : shelvesList)
                Printer.getInstance().print(String.valueOf(id));
            return new ShelvesMenuView();
        }catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to category main menu");
            return new ShelvesMenuView();
        }
    }

    public View getAllBackShelves() {
        try{
            List<Integer> shelvesList = BackendController.getInstance().getAllBackShelves();
            for (int id : shelvesList)
                Printer.getInstance().print(String.valueOf(id));
            return new ShelvesMenuView();
        }catch (Exception e){
            Printer.getInstance().print(e.getMessage());
            Printer.getInstance().print("Problem: Returning to category main menu");
            return new ShelvesMenuView();
        }
    }
}
