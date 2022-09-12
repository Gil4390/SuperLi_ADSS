package Presentation.ViewModel.SupplierViewModel;

import Presentation.Model.BackendController;
import Presentation.Model.SupplierModel.SupplierModel;
import Presentation.View.SupplierView.SupplierMenuView;
import Presentation.View.SupplierView.SupplierView;
import Presentation.View.View;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class SupplierMenuViewModel {
    private final BackendController backendController;

    public SupplierMenuViewModel() {
        this.backendController = BackendController.getInstance();
    }

    public View enterSupplierCard() {
        try {
            List<SupplierModel> suppliers = backendController.getAllSuppliers();
            if(suppliers.size()==0) {
                System.out.println("There are no suppliers in the system yet");
                return new SupplierMenuView();
            }
            for(SupplierModel supplierModel : suppliers)
                System.out.println(supplierModel);
            System.out.println("Please enter supplier business number:");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            for(SupplierModel supplierModel : suppliers) {
                if(input.equals(String.valueOf(supplierModel.getBusinessNumber())))
                    return new SupplierView(supplierModel);
            }
            return invalidInput();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new SupplierMenuView();
        }
    }
    public View invalidInput() {
        System.out.println("Invalid input!");
        return new SupplierMenuView();
    }
    public View addSupplier() {
        System.out.println("to add new supplier please enter: name, business number, bank account, delivery, payment method, address, area, days of supply");
        System.out.println("For example: Tenuva, 26667342, 213412, true, CreditCard, rager 116, South, 1 4 6");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] inputArr = input.split(", ");
        if(inputArr.length<7 || inputArr.length > 8)
            return invalidInput();
        try {
            String name = inputArr[0];
            int businessNumber = Integer.parseInt(inputArr[1]);
            int bankAccount = Integer.parseInt(inputArr[2]);
            boolean shouldDeliver;
            if(inputArr[3].equals("true"))
                shouldDeliver = true;
            else if(inputArr[3].equals("false"))
                shouldDeliver = false;
            else
                return invalidInput();
            String paymentMethodStr;
            if(inputArr[4].equals("CreditCard") || inputArr[4].equals("Cash") || inputArr[4].equals("BankTransfer") || inputArr[4].equals("Check"))
                paymentMethodStr = inputArr[4];
            else
                return invalidInput();
            String address = inputArr[5];
            String area = inputArr[6];
            Set<DayOfWeek> daysOfSupply = new HashSet<>();
            if(inputArr.length==8) {
                String[] days = inputArr[7].split(" ");
                for(String day : days) {
                    daysOfSupply.add(DayOfWeek.of(Integer.parseInt(day) == 1 ? 7 : Integer.parseInt(day)-1));
                }
            }
            backendController.addSupplier(name, businessNumber, bankAccount, shouldDeliver, paymentMethodStr, daysOfSupply, address, area);
            return new SupplierMenuView();
        }
        catch (Exception e) {
            return invalidInput();
        }
    }

    public View removeSupplier() {
        try {
            List<SupplierModel> suppliers = backendController.getAllSuppliers();
            if(suppliers.size()==0) {
                System.out.println("There are no suppliers in the system yet");
                return new SupplierMenuView();
            }
            for(SupplierModel supplierModel : suppliers)
                System.out.println(supplierModel.getBusinessNumber() + " "+ supplierModel.getName());
            System.out.println("Please enter supplier business number that you want to remove:");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            for(SupplierModel supplierModel : suppliers) {
                if(input.equals(String.valueOf(supplierModel.getBusinessNumber()))) {
                    backendController.removeSupplier(supplierModel.getBusinessNumber());
                    return new SupplierMenuView();
                }
            }
            return invalidInput();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new SupplierMenuView();
        }
    }
}
