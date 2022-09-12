package Presentation.Model.EmployeeModuleModel;

public class PositionsShifts {
    private String[] listOfPositions = {
            "Shift manager",
            "Cashier",
            "Warehouse worker",
            "Currier",
            "HR Manager",
            "Usher",
            "Driver",
            "Acquisition",
            "Branch Manager"
    };

    public String[] getListOfPositions() {
        return listOfPositions;
    }
    public void printPositions()
    {
        for (String s: listOfPositions)
            System.out.print(s+ ",");
        System.out.println();
    }

}
