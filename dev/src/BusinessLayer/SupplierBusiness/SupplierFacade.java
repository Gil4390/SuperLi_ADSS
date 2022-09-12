package BusinessLayer.SupplierBusiness;

import DataAccessLayer.SupplierDAL.*;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.Date;
import java.time.DayOfWeek;
import java.util.*;


public class SupplierFacade {
    private final OrderController orderController;
    private final SupplierController supplierController;

    public SupplierFacade() {
        CacheCleaner cacheCleaner = new CacheCleaner(Arrays.asList(SupplierMapper.getInstance(), DemandOrderMapper.getInstance(), FixedOrderMapper.getInstance(), BillOfQuantityMapper.getInstance(), ContactMapper.getInstance(), OrderProductMapper.getInstance()));
        cacheCleaner.clean();
        this.orderController = new OrderController();
        this.supplierController = new SupplierController();
    }

    public Supplier getSupplier(int supplierBN) {
        return supplierController.getSupplier(supplierBN);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierController.getAllSuppliers();
    }

    public void addSupplier(String name, int businessNumber, int bankAccount, boolean shouldDeliver, String paymentMethod, Set<DayOfWeek> daysOfSupply, String address, String area) {
        supplierController.addSupplier(name, businessNumber, bankAccount, shouldDeliver, paymentMethod, daysOfSupply, address, area);
    }

    public void removeSupplier(int supplierBN) {
        supplierController.removeSupplier(supplierBN);
    }

    public BillOfQuantity getBillOfQuantity(int supplierBN) {
        return supplierController.getBillOfQuantity(supplierBN);
    }

    public void updateSupplierName(int supplierBN, String newName) {
        supplierController.updateSupplierName(supplierBN, newName);
    }

    public void updateSupplierBankAccount(int supplierBN, int newBankAccount) {
        supplierController.updateSupplierBankAccount(supplierBN, newBankAccount);
    }

    public void updateSupplierDelivery(int supplierBN, boolean newDeliveryMethod) {
        supplierController.updateSupplierDelivery(supplierBN, newDeliveryMethod);
    }

    public void updateSupplierPaymentMethod(int supplierBN, String newPaymentMethod) {
        supplierController.updateSupplierPaymentMethod(supplierBN, newPaymentMethod);
    }

    public void addContact(int supplierBN, String contactName, String phoneNumber, String email) {
        supplierController.addContact(supplierBN, contactName, phoneNumber, email);
    }

    public void removeContact(int supplierBN, int contactId) {
        supplierController.removeContact(supplierBN, contactId);
    }

    public void updateContactName(int supplierBN, int contactId, String newContactName) {
        supplierController.updateContactName(supplierBN, contactId, newContactName);
    }

    public void updateContactEmail(int supplierBN, int contactId, String newEmail) {
        supplierController.updateContactEmail(supplierBN, contactId, newEmail);
    }

    public void updateContactPhone(int supplierBN, int contactId, String newPhoneNumber) {
        supplierController.updateContactPhone(supplierBN, contactId, newPhoneNumber);
    }

    public double getItemPrice(String branchAddress, int supplierBN, int catalogNumber) {
        return supplierController.getItemPrice(branchAddress, supplierBN, catalogNumber);
    }

    public void addItem(String branchAddress, int supplierBN, int catalogNumber, int supplierCatalogNum, double price, String name) {
        supplierController.addItem(branchAddress, supplierBN, catalogNumber, supplierCatalogNum, price, name);
    }

    public void removeItem(String branchAddress, int supplierBN, int catalogNumber) {
        supplierController.removeItem(branchAddress, supplierBN, catalogNumber);
    }

    public void updateItemPrice(String branchAddress, int supplierBN, int catalogNumber, double newPrice) {
        supplierController.updateItemPrice(branchAddress, supplierBN, catalogNumber, newPrice);
    }

    public Map<Integer, Double> getItemDiscounts(String branchAddress, int supplierBN, int catalogNumber) {
        return supplierController.getItemDiscounts(branchAddress, supplierBN, catalogNumber);
    }

    public void addItemDiscountAccordingToAmount(String branchAddress, int supplierBN, int catalogNumber, int amount, double discount) {
        supplierController.addItemDiscountAccordingToAmount(branchAddress, supplierBN, catalogNumber, amount, discount);
    }

    public void updateItemDiscountAccordingToAmount(String branchAddress, int supplierBN, int catalogNumber, int amount, double newDiscount) {
        supplierController.updateItemDiscountAccordingToAmount(branchAddress, supplierBN, catalogNumber, amount, newDiscount);
    }

    public void removeItemDiscountAccordingToAmount(String branchAddress, int supplierBN, int catalogNumber, int amount) {
        supplierController.removeItemDiscountAccordingToAmount(branchAddress, supplierBN, catalogNumber, amount);
    }

    public void updateSupplierCatalog(String branchAddress, int supplierBN, int catalogNumber, int newSupplierCatalog) {
        supplierController.updateSupplierCatalog(branchAddress, supplierBN, catalogNumber, newSupplierCatalog);
    }

    public String getItemName(String branchAddress, int supplierBN, int catalogNumber) {
        return supplierController.getItemName(branchAddress, supplierBN, catalogNumber);
    }

    public void updateItemName(String branchAddress, int supplierBN, int catalogNumber, String newName) {
        supplierController.updateItemName(branchAddress, supplierBN, catalogNumber, newName);
    }

    public List<Contact> getAllContacts(int supplierBN) {
        return supplierController.getAllContacts(supplierBN);
    }

    public DemandOrder addDemandOrder(String branchAddress, int supplierBN, Date orderDate, Date supplyDate, HashMap<Integer, Integer> itemIdAndAmount) {
        BillOfQuantity billOfQuantity = getBillOfQuantity(supplierBN);
        return orderController.addDemandOrder(branchAddress, supplierBN, orderDate, supplyDate, itemIdAndAmount, billOfQuantity);
    }

    public void addFixedOrder(String branchAddress, int supplierBN, Date orderDate, Set<DayOfWeek> supplyDays, HashMap<Integer, Integer> itemIdAndAmount) {
        BillOfQuantity billOfQuantity = getBillOfQuantity(supplierBN);
        orderController.addFixedOrder(branchAddress, supplierBN, orderDate, supplyDays, itemIdAndAmount, billOfQuantity);
    }

    public void addFixedOrders(String branchAddress, Date orderDate, Set<DayOfWeek> supplyDays, HashMap<Integer, Integer> itemIdAndAmount) {
        Map<Supplier, HashMap<Integer, Integer>> supplierToItems = supplierController.itemsToSuppliersForFixedOrder(branchAddress, itemIdAndAmount, supplyDays);
        for(Supplier supplier : supplierToItems.keySet())
            orderController.addFixedOrder(branchAddress, supplier.getBusinessNumber(), orderDate, supplyDays, supplierToItems.get(supplier), getBillOfQuantity(supplier.getBusinessNumber()));
    }

    public Map<Supplier, HashMap<Integer, Integer>> itemsToSuppliersForDemandOrder(String branchAddress, HashMap<Integer, Integer> itemIdAndAmount) { return supplierController.itemsToSuppliersForDemandOrder(branchAddress, itemIdAndAmount); }

    public void addProductsToFixedOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) {
        BillOfQuantity billOfQuantity = getBillOfQuantity(supplierBN);
        orderController.addProductsToFixedOrder(supplierBN, orderId, itemIdAndAmount, billOfQuantity);
    }

    public void removeProductsFromFixedOrder(int supplierBN, int orderId, List<Integer> productsId) {
        orderController.removeProductsFromFixedOrder(supplierBN, orderId, productsId);
    }

    public void updateOrderProductsOfFixedOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) {
        BillOfQuantity billOfQuantity = getBillOfQuantity(supplierBN);
        orderController.updateOrderProductsOfFixedOrder(supplierBN, orderId, itemIdAndAmount, billOfQuantity);
    }

    public void addProductsToDemandOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) {
        BillOfQuantity billOfQuantity = getBillOfQuantity(supplierBN);
        orderController.addProductsToDemandOrder(supplierBN, orderId, itemIdAndAmount, billOfQuantity);
    }

    public void removeProductsFromDemandOrder(int supplierBN, int orderId, List<Integer> productsId) {
        orderController.removeProductsFromDemandOrder(supplierBN, orderId, productsId);
    }

    public void updateOrderProductsOfDemandOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) {
        BillOfQuantity billOfQuantity = getBillOfQuantity(supplierBN);
        orderController.updateOrderProductsOfDemandOrder(supplierBN, orderId, itemIdAndAmount, billOfQuantity);
    }

    public void updateUnSuppliedAmount(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndUnSuppliedAmount) {
        orderController.updateUnSuppliedAmount(supplierBN, orderId, itemIdAndUnSuppliedAmount);
    }

    public void removeUnSuppliedDemandOrder(int supplierBN, int orderId) {
        orderController.removeUnSuppliedDemandOrder(supplierBN, orderId);
    }

    public List<DemandOrder> getAllSupplierDemandOrders(int supplierBN) {
        return orderController.getAllSupplierDemandOrders(supplierBN);
    }

    public List<FixedOrder> getAllSupplierFixedOrders(int supplierBN) {
        return orderController.getAllSupplierFixedOrders(supplierBN);
    }

    public List<Order> getAllSupplierOrders(int supplierBN) {
        return orderController.getAllSupplierOrders(supplierBN);
    }

    public List<Pair<Integer, OrderProduct>> supplyDemandOrder(int supplierBN, int orderId) {
        return orderController.supplyDemandOrder(supplierBN, orderId);
    }

    public void unActiveFixedOrder(int supplierBN, int orderId, Date date) {
        orderController.unActiveFixedOrder(supplierBN, orderId, date);
    }

    public List<FixedOrder> getAllFixedOrders() {
        return orderController.getAllFixedOrders();
    }

    public List<DemandOrder> getAllDemandOrders() {
        return orderController.getAllDemandOrders();
    }

    public void sendPDFDOrder(int supplierBN, int orderId, String email) throws IOException {
        orderController.sendPDFDOrder(supplierController.getSupplier(supplierBN), orderId, email);
    }

    public void addSupplyDayToFixedOrder(int supplierBN, int orderId, DayOfWeek day) {
        supplierController.isSupplierCanSupply(supplierBN, day);
        orderController.addSupplyDayToFixedOrder(supplierBN, orderId, day);
    }

    public void removeSupplyDayToFixedOrder(int supplierBN, int orderId, DayOfWeek day) {
        orderController.removeSupplyDayToFixedOrder(supplierBN, orderId, day);
    }

    public List<FixedOrder> dailyFixedOrders() {
        return orderController.dailyFixedOrders();
    }

    public List<DemandOrder> generateOrdersForShortage(String branchAddress, List<Pair<Integer, Integer>> itemIdAndAmountList) {
        List<DemandOrder> demandOrders = new ArrayList<>();
        Map<Integer, Integer> itemIdAndAmountMap = new HashMap<>();
        List<OrderProduct> arrivingOrderProducts = orderController.getOrderProductsOfUnSuppliedDemandOrders();
        for(Pair<Integer, Integer> itemIdAndAmount : itemIdAndAmountList)
            itemIdAndAmountMap.put(itemIdAndAmount.getKey(), itemIdAndAmount.getValue());
        for(OrderProduct orderProduct : arrivingOrderProducts)
            if(itemIdAndAmountMap.containsKey(orderProduct.getProductId()))
                if(itemIdAndAmountMap.get(orderProduct.getProductId()) <= orderProduct.getAmount())
                    itemIdAndAmountMap.remove(orderProduct.getProductId());
                else
                    itemIdAndAmountMap.replace(orderProduct.getProductId(), itemIdAndAmountMap.get(orderProduct.getProductId()) - orderProduct.getAmount());
        Map<Supplier, HashMap<Integer, Integer>> supplierToItems = supplierController.itemsToSuppliersForDemandOrder(branchAddress, itemIdAndAmountMap);
        for(Supplier supplier : supplierToItems.keySet()) {
            DemandOrder demandOrder = orderController.addDemandOrder(branchAddress, supplier.getBusinessNumber(), new Date(Calendar.getInstance().getTime().getTime()), null, supplierToItems.get(supplier), getBillOfQuantity(supplier.getBusinessNumber()));
        if(demandOrder!=null)
            demandOrders.add(demandOrder);
        }
        return demandOrders;
    }

    public List<DemandOrder> createNextWeekFixedOrders(String branchAddress) {
        List<DemandOrder> demandOrders = new ArrayList<>();
        List<FixedOrder> fixedOrders = orderController.dailyFixedOrders();
        if(fixedOrders.size() == 0)
            throw new IllegalArgumentException("There are no fixed orders for today");
        for(FixedOrder fixedOrder: fixedOrders) {
            BillOfQuantity billOfQuantity = getBillOfQuantity(fixedOrder.getSupplierBN());
            demandOrders.add(orderController.createNextWeekFixedOrder(branchAddress, fixedOrder, billOfQuantity, getSupplier(fixedOrder.getSupplierBN()).getShouldDeliver()));
        }
        return demandOrders;
    }

    public void setSupplyDateForDemandOrder(int supplierBN, int orderId, Date supplyDate) {
        orderController.setSupplyDateForDemandOrder(supplierBN, orderId, supplyDate);
    }

    public DemandOrder getDemandOrder(int supplierBN, int orderId) {
        return orderController.getDemandOrder(supplierBN, orderId);
    }

}
