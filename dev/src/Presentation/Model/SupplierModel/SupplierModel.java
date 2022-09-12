package Presentation.Model.SupplierModel;

import Presentation.Model.BackendController;
import ServiceLayer.Objects.SupplierObjects.SSupplier;

import java.time.DayOfWeek;
import java.util.*;
import java.sql.Date;

public class SupplierModel {
    public enum PaymentMethod {
        CreditCard,
        Cash,
        BankTransfer,
        Check
    }

    private String name;
    private final int businessNumber;
    private int bankAccount;
    private boolean shouldDeliver;
    private PaymentMethod paymentMethod;
    private String address;
    private final Set<DayOfWeek> possibleSupplyDays;
    private final BackendController backendController;
    private final Scanner scanner;

    public SupplierModel(SSupplier supplier) {
        this.name = supplier.getName();
        this.businessNumber = supplier.getBusinessNumber();
        this.bankAccount = supplier.getBankAccount();
        this.shouldDeliver = supplier.isShouldDeliver();
        this.paymentMethod = PaymentMethod.valueOf(supplier.getPaymentMethod().toString());
        this.possibleSupplyDays = supplier.getPossibleSupplyDays();
        this.address = supplier.getAddress();
        this.backendController = BackendController.getInstance();
        this.scanner = new Scanner(System.in);
    }

    public String toString() {
        return "-Name: "+name+", business number: "+businessNumber+", bank account: "+bankAccount+", address: "+address+", should Deliver: "+shouldDeliver+", payment method: "+paymentMethod+", possible supply days: "+possibleSupplyDays.toString();
    }

    public String getName() {
        return name;
    }

    public int getBusinessNumber() {
        return businessNumber;
    }

    public int getBankAccount() {
        return bankAccount;
    }

    public boolean isShouldDeliver() {
        return shouldDeliver;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public Set<DayOfWeek> getPossibleSupplyDays() {
        return possibleSupplyDays;
    }

    public void updateName() {
        System.out.println("Please enter a new supplier name:");
        String input = scanner.nextLine();
        backendController.updateSupplierName(this.getBusinessNumber(), input);
        this.name = input;
    }
    public void updateBankAccount() {
        System.out.println("Please enter a new supplier bank account:");
        String input = scanner.nextLine();
        backendController.updateSupplierBankAccount(this.getBusinessNumber(), Integer.parseInt(input));
        this.bankAccount = Integer.parseInt(input);
    }

    public void updateDeliveryMethod() {
        System.out.println("Please enter a new supplier delivery method:");
        String input = scanner.nextLine();
        boolean newDeliveryMethod;
        if(input.equals("true"))
            newDeliveryMethod = true;
        else if(input.equals("false"))
            newDeliveryMethod = false;
        else
            throw new IllegalArgumentException("Invalid input!");
        backendController.updateSupplierDelivery(this.getBusinessNumber(), newDeliveryMethod);
        this.shouldDeliver = newDeliveryMethod;
    }

    public void updatePaymentMethod() {
        System.out.println("Please enter a new supplier payment method:");
        String input = scanner.nextLine();
        backendController.updateSupplierPaymentMethod(this.getBusinessNumber(), input);
        this.paymentMethod = PaymentMethod.valueOf(input);
    }

    public List<ContactModel> getAllContacts() {
        return backendController.getAllContacts(businessNumber);
    }

    public BillOfQuantityModel getBillOfQuantity() {
        return backendController.getBillOfQuantity(this);
    }

    public void addContact() {
        System.out.println("to add new contact please enter: name, phone number, email");
        System.out.println("For example: eli kurin, 0543748445, eli@gmail.com");
        String input = scanner.nextLine();
        String[] inputArr = input.split(", ");
        if (inputArr.length != 3)
            throw new IllegalArgumentException("Invalid input!");
        String name = inputArr[0];
        String phoneNumber = inputArr[1];
        String email = inputArr[2];
        backendController.addContact(getBusinessNumber() ,name, phoneNumber, email);
    }

    public void removeContact() {
        List<ContactModel> contacts = getAllContacts();
        printSupplierContacts(contacts);
        System.out.println("Please enter contact name that you want to remove:");
        String input = scanner.nextLine();
        for(ContactModel contactModel : contacts)
            if(input.equals(String.valueOf(contactModel.getName()))) {
                backendController.removeContact(getBusinessNumber(), contactModel.getContactId());
                return;
            }
        throw new IllegalArgumentException("Invalid input!");
    }

    public ContactModel enterContact() {
        List<ContactModel> contacts = getAllContacts();
        printSupplierContacts(contacts);
        System.out.println("Please enter contact name:");
        String input = scanner.nextLine();
        for(ContactModel contactModel : contacts)
            if(input.equals(String.valueOf(contactModel.getName())))
                return contactModel;
        throw new IllegalArgumentException("Invalid input!");
    }

    public void printSupplierContacts(List<ContactModel> contacts) {
        if(contacts.size()==0)
            throw new IllegalArgumentException("There are no contacts in the system yet");
        for(ContactModel contactModel : contacts)
            System.out.println(contactModel);
    }
    public void addFixedOrder() {
        printSupplierProducts();
        System.out.println("to add new fixed order please enter: supply days, item id-amount");
        System.out.println("For example: 1 3 5, 1444-20 1332-30");
        String[] input = getInputForOrder();
        Set<DayOfWeek> daysOfSupply = new HashSet<>();
        String[] days = input[0].split(" ");
        for(String day : days) {
            DayOfWeek dayOfWeek = DayOfWeek.of(Integer.parseInt(day) == 1 ? 7 : Integer.parseInt(day) - 1);
            if(!possibleSupplyDays.contains(dayOfWeek))
                throw new IllegalArgumentException("Invalid input!");
            daysOfSupply.add(dayOfWeek);
        }
        backendController.addFixedOrder(getBusinessNumber() , new Date(Calendar.getInstance().getTime().getTime()), daysOfSupply, getItemIdAndAmount(input[1].split(" ")));
    }

    private void printSupplierProducts() {
        getBillOfQuantity().productsList();
    }

    private HashMap<Integer, Integer> getItemIdAndAmount(String[] products) {
        HashMap<Integer, Integer> itemIdAndAmount = new HashMap<>();
        if(products.length==0)
            throw new IllegalArgumentException("Invalid input!");
        for(String product : products)
            itemIdAndAmount.put(Integer.parseInt(product.split("-")[0]), Integer.parseInt(product.split("-")[1]));
        return itemIdAndAmount;
    }

    public String[] getInputForOrder() {
        String input = scanner.nextLine();
        String[] inputArr = input.split(", ");
        if (inputArr.length != 2)
            throw new IllegalArgumentException("Invalid input!");
        return inputArr;
    }

    public void addDemandOrder() {
        printSupplierProducts();
        System.out.println("to add new demand order please enter: supply date as yyyy-mm-dd, item id-amount");
        System.out.println("For example: 2022-05-22, 1444-20 1332-30");
        String[] input = getInputForOrder();
        Date supplyDate = Date.valueOf(input[0]);
        backendController.addDemandOrder(getBusinessNumber(), new Date(Calendar.getInstance().getTime().getTime()), supplyDate, getItemIdAndAmount(input[1].split(" ")));
    }

    public void unActiveFixOrder() {
        List<FixedOrderModel> fixedOrderModels = backendController.getAllSupplierFixedOrders(businessNumber);
        if(fixedOrderModels.size()==0)
            throw new IllegalArgumentException("There are no fixed orders in the system yet");
        int count = 0;
        for(FixedOrderModel fixedOrderModel : fixedOrderModels)
            if(fixedOrderModel.isActive()) {
                System.out.println(fixedOrderModel);
                count++;
            }
        if(count == 0)
            throw new IllegalArgumentException("There are no activated fixed orders in the system yet");
        System.out.println("Please enter order id:");
        String input = scanner.nextLine();
        for(FixedOrderModel fixedOrderModel : fixedOrderModels)
            if(fixedOrderModel.isActive() && input.equals(String.valueOf(fixedOrderModel.getOrderId()))) {
                backendController.unActiveFixedOrder(businessNumber, fixedOrderModel.getOrderId(), new Date(System.currentTimeMillis()));
                return;
            }
        throw new IllegalArgumentException("Invalid input!");
    }

    public void addSupplyDayToFixedOrder() {
        List<FixedOrderModel> fixedOrderModels = backendController.getAllSupplierFixedOrders(businessNumber);
        String input = getInputForFixedOrder(fixedOrderModels);
        for(FixedOrderModel fixedOrderModel : fixedOrderModels)
            if(input.equals(String.valueOf(fixedOrderModel.getOrderId()))) {
                addSupplyDay(fixedOrderModel);
                return;
            }
        throw new IllegalArgumentException("Invalid order id!");
    }

    private void addSupplyDay(FixedOrderModel fixedOrderModel) {
        List<DayOfWeek> possibleSupplyDaysToAdd = new ArrayList<>(possibleSupplyDays);
        for(DayOfWeek dayOfWeek : fixedOrderModel.getDaysOfSupply())
            possibleSupplyDaysToAdd.remove(dayOfWeek);
        if(possibleSupplyDaysToAdd.size()==0)
            throw new IllegalArgumentException("There are no additional days that supplier can supply!");
        System.out.println("Supply days that can be added: ");
        for(DayOfWeek dayOfWeek : possibleSupplyDaysToAdd)
            System.out.print(dayOfWeek+" ");
        System.out.println();
        System.out.println("Please choose a supply day that you want to add:");
        System.out.println("For example: 1 for sunday, 4 for wednesday");
        String input = scanner.nextLine();
        DayOfWeek day = DayOfWeek.of(Integer.parseInt(input) == 1 ? 7 : Integer.parseInt(input) - 1);
        if(!possibleSupplyDaysToAdd.contains(day))
            throw new IllegalArgumentException("Invalid input!");
        backendController.addSupplyDayToFixedOrder(businessNumber, fixedOrderModel.getOrderId(), day);
    }

    public void removeSupplyDayFromFixedOrder() {
        List<FixedOrderModel> fixedOrderModels = backendController.getAllSupplierFixedOrders(businessNumber);
        String input = getInputForFixedOrder(fixedOrderModels);
        for(FixedOrderModel fixedOrderModel : fixedOrderModels)
            if(input.equals(String.valueOf(fixedOrderModel.getOrderId()))) {
                removeSupplyDay(fixedOrderModel);
                return;
            }
        throw new IllegalArgumentException("Invalid order id!");
    }

    private void removeSupplyDay(FixedOrderModel fixedOrderModel) {
        System.out.println("Supply days:");
        for(DayOfWeek dayOfWeek : fixedOrderModel.getDaysOfSupply())
            System.out.print(dayOfWeek+" ");
        System.out.println();
        System.out.println("Please choose a supply day that you want to remove:");
        System.out.println("For example: 1 for sunday, 4 for wednesday");
        String input = scanner.nextLine();
        DayOfWeek day = DayOfWeek.of(Integer.parseInt(input) == 1 ? 7 : Integer.parseInt(input) - 1);
        if(!fixedOrderModel.getDaysOfSupply().contains(day))
            throw new IllegalArgumentException("Invalid input!");
        backendController.removeSupplyDayFromFixedOrder(businessNumber, fixedOrderModel.getOrderId(), day);

    }

    public void supplyDemandOrder() {
        List<DemandOrderModel> demandOrderModels = backendController.getAllSupplierDemandOrders(businessNumber);
        String input = getInputForUnSuppliedDemandOrder(demandOrderModels);
        for(DemandOrderModel demandOrderModel : demandOrderModels)
            if(!demandOrderModel.isSupplied() && input.equals(String.valueOf(demandOrderModel.getOrderId()))) {
                HashMap<Integer, Integer> itemIdAndUnSuppliedAmount = new HashMap<>();
                System.out.println("Please enter 1 if there are un supplied products");
                String input1 = scanner.nextLine();
                if(input1.equals("1")) {
                    System.out.println("To enter un supplied products please enter: product id-un supplied amount");
                    System.out.println("for example: 1-20 2-5");
                    String unSuppliedInput = scanner.nextLine();
                    String[] itemIdAndAmount = unSuppliedInput.split(" ");
                    for(String productAndAmount : itemIdAndAmount)
                        itemIdAndUnSuppliedAmount.put(Integer.parseInt(productAndAmount.split("-")[0]), Integer.parseInt(productAndAmount.split("-")[1]));
                }
                backendController.supplyDemandOrder(businessNumber, demandOrderModel.getOrderId(), itemIdAndUnSuppliedAmount);
                return;
            }
        throw new IllegalArgumentException("Invalid input!");
    }

    public void removeUnSuppliedDemandOrder() {
        List<DemandOrderModel> demandOrderModels = backendController.getAllSupplierDemandOrders(businessNumber);
        String input = getInputForUnSuppliedDemandOrder(demandOrderModels);
        for(DemandOrderModel demandOrderModel : demandOrderModels)
            if(!demandOrderModel.isSupplied() && input.equals(String.valueOf(demandOrderModel.getOrderId()))) {
                backendController.removeUnSuppliedDemandOrder(businessNumber, demandOrderModel.getOrderId());
                return;
            }
        throw new IllegalArgumentException("Invalid input!");
    }

    private String getInputForUnSuppliedDemandOrder(List<DemandOrderModel> demandOrderModels) {
        if(demandOrderModels.size()==0)
            throw new IllegalArgumentException("There are no demand orders in the system yet");
        int count = 0;
        for(DemandOrderModel demandOrderModel : demandOrderModels)
            if(!demandOrderModel.isSupplied()) {
                System.out.println(demandOrderModel);
                count++;
            }
        if(count == 0)
            throw new IllegalArgumentException("There are no un supplied demand orders in the system yet");
        System.out.println("Please enter order id:");
        String input = scanner.nextLine();
        return input;
    }

    private String getInputForDemandOrder(List<DemandOrderModel> demandOrderModels) {
        if(demandOrderModels.size()==0)
            throw new IllegalArgumentException("There are no demand orders in the system yet");
        for(DemandOrderModel demandOrderModel : demandOrderModels)
            System.out.println(demandOrderModel);
        System.out.println("Please enter order id:");
        String input = scanner.nextLine();
        return input;
    }

    private String getInputForFixedOrder(List<FixedOrderModel> fixedOrderModels) {
        if(fixedOrderModels.size()==0)
            throw new IllegalArgumentException("There are no fixed orders in the system yet");
        for(FixedOrderModel fixedOrderModel : fixedOrderModels)
            System.out.println(fixedOrderModel);
        System.out.println("Please enter order id:");
        String input = scanner.nextLine();
        return input;
    }

    public void sendDemandOrderPDF() {
        List<DemandOrderModel> demandOrderModels = backendController.getAllSupplierDemandOrders(businessNumber);
        String input = getInputForDemandOrder(demandOrderModels);
        for(DemandOrderModel demandOrderModel : demandOrderModels)
            if(input.equals(String.valueOf(demandOrderModel.getOrderId()))) {
                List<ContactModel> contacts = getAllContacts();
                printSupplierContacts(contacts);
                System.out.println("Please enter the contact email you want to send him the order details: ");
                String email = scanner.nextLine();
                backendController.sendPDFDOrder(businessNumber,demandOrderModel.getOrderId(), email);
                return;
            }
    }

    public void viewFixedOrders() {
        List<FixedOrderModel> fixedOrderModels = backendController.getAllSupplierFixedOrders(businessNumber);
        if(fixedOrderModels.size()==0)
            throw new IllegalArgumentException("There are no fixed orders in the system yet");
        for(FixedOrderModel fixedOrderModel : fixedOrderModels)
            System.out.println(fixedOrderModel);
    }

    public void viewDemandOrders() {
        List<DemandOrderModel> demandOrderModels = backendController.getAllSupplierDemandOrders(businessNumber);
        if(demandOrderModels.size()==0)
            throw new IllegalArgumentException("There are no fixed orders in the system yet");
        for(DemandOrderModel demandOrderModel : demandOrderModels)
            System.out.println(demandOrderModel);
    }

    public void addProductToFixedOrder() {
        List<FixedOrderModel> fixedOrderModels = backendController.getAllSupplierFixedOrders(businessNumber);
        String orderId = getInputForFixedOrder(fixedOrderModels);
        FixedOrderModel fixedOrderModel = checkIfFixedOrderExist(fixedOrderModels, Integer.parseInt(orderId));
        System.out.println("Products that supplier can supply: ");
        printSupplierProducts();
        printFixedOrderProducts(fixedOrderModel);
        System.out.println("to add new products to fixed order please enter: item id-amount list");
        System.out.println("For example: 1444-20 1332-30 111-50");
        HashMap<Integer, Integer> itemIdAndAmount = getInputForAddingProductsToOrder();
        backendController.addProductsToFixedOrder(businessNumber, Integer.parseInt(orderId), itemIdAndAmount);
    }

    public void removeProductFromFixedOrder() {
        List<FixedOrderModel> fixedOrderModels = backendController.getAllSupplierFixedOrders(businessNumber);
        int orderId = Integer.parseInt(getInputForFixedOrder(fixedOrderModels));
        FixedOrderModel fixedOrderModel = checkIfFixedOrderExist(fixedOrderModels, orderId);
        printFixedOrderProducts(fixedOrderModel);
        System.out.println("to remove products from fixed order please enter: item id list");
        System.out.println("For example: 2240, 2000, 1234");
        List<Integer> itemIdList = getInputForRemovingProductsFromOrder();
        backendController.removeProductsFromFixedOrder(businessNumber, orderId, itemIdList);
    }

    public void updateProductOfFixedOrder() {
        List<FixedOrderModel> fixedOrderModels = backendController.getAllSupplierFixedOrders(businessNumber);
        int orderId = Integer.parseInt(getInputForFixedOrder(fixedOrderModels));
        FixedOrderModel fixedOrderModel = checkIfFixedOrderExist(fixedOrderModels, orderId);
        printFixedOrderProducts(fixedOrderModel);
        System.out.println("to update products of fixed order please enter: item id-new amount list");
        System.out.println("For example: 1444-20 1332-30 111-50");
        HashMap<Integer, Integer> itemIdAndAmount = getInputForAddingProductsToOrder();
        backendController.updateProductsOfFixedOrder(businessNumber, orderId, itemIdAndAmount);
    }

    public void addProductToDemandOrder() {
        List<DemandOrderModel> demandOrderModels = backendController.getAllSupplierDemandOrders(businessNumber);
        String orderId = getInputForDemandOrder(demandOrderModels);
        DemandOrderModel demandOrderModel = checkIfDemandOrderExist(demandOrderModels, Integer.parseInt(orderId));
        System.out.println("Products that supplier can supply: ");
        printSupplierProducts();
        printDemandOrderProducts(demandOrderModel);
        System.out.println("to add new products to demand order please enter: item id-amount list");
        System.out.println("For example: 1444-20 1332-30 111-50");
        HashMap<Integer, Integer> itemIdAndAmount = getInputForAddingProductsToOrder();
        backendController.addProductsToDemandOrder(businessNumber, Integer.parseInt(orderId), itemIdAndAmount);
    }

    public void updateProductOfDemandOrder() {
        List<DemandOrderModel> demandOrderModels = backendController.getAllSupplierDemandOrders(businessNumber);
        int orderId = Integer.parseInt(getInputForDemandOrder(demandOrderModels));
        DemandOrderModel demandOrderModel = checkIfDemandOrderExist(demandOrderModels, orderId);
        printDemandOrderProducts(demandOrderModel);
        System.out.println("to update products of demand order please enter: item id-new amount list");
        System.out.println("For example: 1444-20 1332-30 111-50");
        HashMap<Integer, Integer> itemIdAndAmount = getInputForAddingProductsToOrder();
        backendController.updateProductsOfDemandOrder(businessNumber, orderId, itemIdAndAmount);
    }

    public void removeProductFromDemandOrder() {
        List<DemandOrderModel> demandOrderModels = backendController.getAllSupplierDemandOrders(businessNumber);
        int orderId = Integer.parseInt(getInputForDemandOrder(demandOrderModels));
        DemandOrderModel demandOrderModel = checkIfDemandOrderExist(demandOrderModels, orderId);
        printDemandOrderProducts(demandOrderModel);
        System.out.println("to remove products from demand order please enter: item id list");
        System.out.println("For example: 2240, 2000, 1234");
        List<Integer> itemIdList = getInputForRemovingProductsFromOrder();
        backendController.removeProductsFromDemandOrder(businessNumber, orderId, itemIdList);
    }

    private List<Integer> getInputForRemovingProductsFromOrder() {
        String input = scanner.nextLine();
        String[] inputArr = input.split(", ");
        List<Integer> itemIdList = new ArrayList<>();
        for(String itemId : inputArr)
            itemIdList.add(Integer.parseInt(itemId));
        return itemIdList;
    }

    private HashMap<Integer, Integer> getInputForAddingProductsToOrder() {
        String input = scanner.nextLine();
        String[] inputArr = input.split(" ");
        HashMap<Integer, Integer> itemIdAndAmountMap = new HashMap<>();
        for(String itemIdAndAmount : inputArr) {
            if(itemIdAndAmountMap.containsKey(Integer.parseInt(itemIdAndAmount.split("-")[0])))
                throw new IllegalArgumentException("Cant choose same product twice or more!");
            itemIdAndAmountMap.put(Integer.parseInt(itemIdAndAmount.split("-")[0]), Integer.parseInt(itemIdAndAmount.split("-")[1]));
        }
        return itemIdAndAmountMap;
    }

    private void printFixedOrderProducts(FixedOrderModel fixedOrderModel) {
        System.out.println("The order products are: ");
        for(OrderProductModel orderProductModel : fixedOrderModel.getOrderProducts().values())
            System.out.println(orderProductModel);
    }

    private void printDemandOrderProducts(DemandOrderModel demandOrderModel) {
        System.out.println("The order products are: ");
        for(OrderProductModel orderProductModel : demandOrderModel.getOrderProducts().values())
            System.out.println(orderProductModel);
    }

    private FixedOrderModel checkIfFixedOrderExist(List<FixedOrderModel> fixedOrderList, int orderId) {
        for(FixedOrderModel fixedOrderModel : fixedOrderList)
            if(fixedOrderModel.getOrderId()==orderId)
                return fixedOrderModel;
        throw new IllegalArgumentException("order is not exists!");
    }

    private DemandOrderModel checkIfDemandOrderExist(List<DemandOrderModel> demandOrderList, int orderId) {
        for(DemandOrderModel demandOrderModel : demandOrderList)
            if(demandOrderModel.getOrderId()==orderId)
                return demandOrderModel;
        throw new IllegalArgumentException("order is not exists!");
    }
}
