package Presentation.View.DeliveryModuleView;

import Presentation.View.View;
import Presentation.Model.DeliveryModuleModel.DeliveryModel;
import Presentation.ViewModel.DeliveryModulViewModel.AllDeliveriesViewModel;
import Presentation.View.ApplicationView;

import java.text.ParseException;
import java.util.*;

public class AllDeliveriesView implements View {
    private final AllDeliveriesViewModel allDeliveriesViewModel;

    public AllDeliveriesView() {
        allDeliveriesViewModel= new AllDeliveriesViewModel();
    }

    @Override
    public void printMenu() {
            List<DeliveryModel> deliveries =new LinkedList<>();
        try {
            deliveries = allDeliveriesViewModel.getAllDeliveries();
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("----- DELIVERIES:  -----");
        for (int i=0; i<deliveries.size(); i++)
            System.out.println(i+1+") "+ deliveries.get(i).toString());
        System.out.println("enter an index to view more details about the chosen delivery");
    }

    @Override
    public View nextInput(String input) {
        List<DeliveryModel> deliveries =new LinkedList<>();
        try {
            deliveries = allDeliveriesViewModel.getAllDeliveries();
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        try {
            switch (input) {
                case "back":
                    return allDeliveriesViewModel.returnToDeliveryMenuView();

                case "close":
                    ApplicationView.shouldTerminate = true;
                    break;
                default:
                    return allDeliveriesViewModel.goToDeliveryView(deliveries.get(Integer.parseInt(input)-1));
            }
        } catch (Exception e) {
            System.out.println("Invalid input!");
        }
        return this;
    }
}
