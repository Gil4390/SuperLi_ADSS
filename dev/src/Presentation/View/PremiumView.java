package Presentation.View;

import Presentation.View.MainMenuView;
import Presentation.View.View;

public class PremiumView implements View {
    @Override
    public void printMenu() {
        System.out.println("************** You Have Been Upgraded To Premium Version!!! **************");
        System.out.println("                - it cost only 0 $$$$");
        System.out.println("                - now you can add unlimited shelves");
        System.out.println("                - create unlimited items");
        System.out.println("                - create unlimited branches");
        System.out.println("**** WE HOPE YOUR STORE WILL BENEFIT FROM WORKING WITH US ****");
        System.out.println("Enter anything to go back");
    }


    @Override
    public View nextInput(String input) {
        return new MainMenuView(false);
    }
}
