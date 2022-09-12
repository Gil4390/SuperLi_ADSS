package Presentation;

import DataAccessLayer.DataBaseCreator;
import DataAccessLayer.SupplierDAL.CacheCleaner;
import Presentation.Model.BackendController;
import Presentation.View.ApplicationView;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DataBaseCreator dataBaseCreator = new DataBaseCreator();
        try {
            dataBaseCreator.CreateAllTables();
            BackendController.getInstance(); // create connection with backend
            ApplicationView applicationView = new ApplicationView();
            applicationView.start();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press 1 if you want to delete all data from the system");
        String input = scanner.nextLine();
        if(input.equals("1"))
            dataBaseCreator.deleteAllTables();
        CacheCleaner.executor.shutdownNow();
        DataAccessLayer.DeliveryModuleDal.DControllers.CacheCleaner.executor.shutdownNow();
    }
}
