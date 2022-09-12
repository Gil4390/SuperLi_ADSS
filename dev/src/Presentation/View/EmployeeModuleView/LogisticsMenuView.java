package Presentation.View.EmployeeModuleView;

import Presentation.View.View;

public class LogisticsMenuView implements View {

    private final int ID;

    public LogisticsMenuView(int input) {
        this.ID = input;
    }

    @Override
    public void printMenu() {

    }

    @Override
    public View nextInput(String input) {
        return null;
    }
}
