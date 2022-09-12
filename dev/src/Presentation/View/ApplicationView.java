package Presentation.View;

import Presentation.Model.BackendController;

import java.util.Scanner;

public class ApplicationView {
    private View currentView;
    private final Scanner scanner;
    public static boolean shouldTerminate= false;


    public ApplicationView() throws Exception {
        this.currentView = new MainMenuView(true);
        this.scanner = new Scanner(System.in);

    }

    public void start() {
        System.out.println("Welcome to super-li");
        while (!shouldTerminate) {
            currentView.printMenu();
            currentView = currentView.nextInput(scanner.nextLine());
        }
    }
}
