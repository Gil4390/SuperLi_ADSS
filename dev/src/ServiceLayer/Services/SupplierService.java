package ServiceLayer.Services;

import BusinessLayer.ApplicationFacade;
import BusinessLayer.SupplierBusiness.*;
import ServiceLayer.Objects.SupplierObjects.SBillOfQuantities;
import ServiceLayer.Objects.SupplierObjects.SSupplier;
import ServiceLayer.Responses.*;

import java.sql.Date;
import java.time.DayOfWeek;
import java.util.*;

public class SupplierService {
    private ApplicationFacade applicationFacade;

    public SupplierService(ApplicationFacade applicationFacade) {
        this.applicationFacade = applicationFacade;
    }

    //supplier----------------------------------------------------------------------------------
    public ResponseT<SSupplier> getSupplier(int supplierBN) {
        try {
            Supplier supplier = applicationFacade.getSupplier(supplierBN);
            return new ResponseT<>(new SSupplier(supplier));
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<Contact>> getAllContacts(int supplierBN) {
        try {
            List<Contact> contacts = applicationFacade.getAllContacts(supplierBN);
            return new ResponseT<>(contacts);
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<SSupplier>> getAllSuppliers() {
        try {
            List<Supplier> suppliers = applicationFacade.getAllSuppliers();
            List<SSupplier> sSuppliers = new ArrayList<>();
            for(Supplier supplier : suppliers)
                sSuppliers.add(new SSupplier(supplier));
            return new ResponseT<>(sSuppliers);
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public Response addSupplier(String name, int businessNumber, int bankAccount, boolean shouldDeliver ,String paymentMethodStr, Set<DayOfWeek> daysOfSupply, String address, String area) {
        try {
            applicationFacade.addSupplier(name, businessNumber, bankAccount, shouldDeliver, paymentMethodStr, daysOfSupply, address, area);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeSupplier(int supplierBN) {
        try {
            applicationFacade.removeSupplier(supplierBN);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response updateSupplierName(int supplierBN, String newName)
    {
        try {
            applicationFacade.updateSupplierName(supplierBN, newName);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response updateSupplierBankAccount(int supplierBN, int newBankAccount) {
        try {
            applicationFacade.updateSupplierBankAccount(supplierBN, newBankAccount);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response updateSupplierDelivery(int supplierBN, boolean newDeliveryMethod) {
        try {
            applicationFacade.updateSupplierDelivery(supplierBN, newDeliveryMethod);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response updateSupplierPaymentMethod(int supplierBN, String newPaymentMethod) {
        try {
            applicationFacade.updateSupplierPaymentMethod(supplierBN, newPaymentMethod);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response addContact(int supplierBN, String contactName, String phoneNumber, String email) {
        try {
            applicationFacade.addContact(supplierBN, contactName, phoneNumber, email);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeContact(int supplierBN, int contactId)
    {
        try {
            applicationFacade.removeContact(supplierBN, contactId);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response updateContactName(int supplierBN, int contactId, String newContactName) {
        try {
            applicationFacade.updateContactName(supplierBN, contactId, newContactName);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response updateContactEmail(int supplierBN, int contactId, String newEmail) {
        try {
            applicationFacade.updateContactEmail(supplierBN, contactId, newEmail);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response updateContactPhone(int supplierBN, int contactId, String newPhoneNumber) {
        try {
            applicationFacade.updateContactPhone(supplierBN, contactId, newPhoneNumber);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------------------
    //orders-----------------------------------------------------------------------------------------------------------------------
    public Response addFixedOrder(String branchAddress, int supplierBN, Date orderDate, Set<DayOfWeek> supplyDays, HashMap<Integer, Integer> itemIdAndAmount) {
        try {
            applicationFacade.addFixedOrder(branchAddress, supplierBN, orderDate, supplyDays, itemIdAndAmount);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response addFixedOrders(String branchAddress, Date orderDate, Set<DayOfWeek> supplyDays, HashMap<Integer, Integer> itemIdAndAmount) {
        try {
            applicationFacade.addFixedOrders(branchAddress, orderDate, supplyDays, itemIdAndAmount);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response addDemandOrders(String branchAddress, Date orderDate, Date supplyDate, HashMap<Integer, Integer> itemIdAndAmount) {
        try {
            applicationFacade.addDemandOrders(branchAddress, orderDate, supplyDate, itemIdAndAmount);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response addProductsToFixedOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) {
        try {
            applicationFacade.addProductsToFixedOrder(supplierBN, orderId, itemIdAndAmount);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeProductsFromFixedOrder(int supplierBN, int orderId, List<Integer> itemIdAndAmount) {
        try {
            applicationFacade.removeProductsFromFixedOrder(supplierBN, orderId, itemIdAndAmount);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response updateProductsOfFixedOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) {
        try {
            applicationFacade.updateOrderProductsOfFixedOrder(supplierBN, orderId, itemIdAndAmount);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response addProductsToDemandOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) {
        try {
            applicationFacade.addProductsToDemandOrder(supplierBN, orderId, itemIdAndAmount);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeProductsFromDemandOrder(int supplierBN, int orderId, List<Integer> itemIdAndAmount) {
        try {
            applicationFacade.removeProductsFromDemandOrder(supplierBN, orderId, itemIdAndAmount);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response updateProductsOfDemandOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) {
        try {
            applicationFacade.updateOrderProductsOfDemandOrder(supplierBN, orderId, itemIdAndAmount);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response addDemandOrder(String branchAddress, int supplierBN, Date orderDate, Date supplyDate, HashMap<Integer, Integer> itemIdAndAmount) {
        try {
            applicationFacade.addDemandOrder(branchAddress, supplierBN, orderDate, supplyDate, itemIdAndAmount);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeUnSuppliedDemandOrder(int supplierBN, int orderId)
    {
        try {
            applicationFacade.removeUnSuppliedDemandOrder(supplierBN, orderId);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public ResponseT<List<Order>> getAllSupplierOrders(int supplierBN) {
        try {
            return new ResponseT<>(applicationFacade.getAllSupplierOrders(supplierBN));
        }
        catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<DemandOrder>> getAllSupplierDemandOrders(int supplierBN) {
        try {
            return new ResponseT<>(applicationFacade.getAllSupplierDemandOrders(supplierBN));
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<DemandOrder>> getAllDemandOrders() {
        try {
            return new ResponseT<>(applicationFacade.getAllDemandOrders());
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<List<FixedOrder>> getAllFixedOrders() {
        try {
            return new ResponseT<>(applicationFacade.getAllFixedOrders());
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }
    public ResponseT<List<FixedOrder>> getAllSupplierFixedOrders(int supplierBN) {
        try {
            return new ResponseT<>(applicationFacade.getAllSupplierFixedOrders(supplierBN));
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public Response supplyDemandOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndUnSuppliedAmount) {
        try {
            applicationFacade.supplyDemandOrder(supplierBN, orderId, itemIdAndUnSuppliedAmount);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response unActiveFixedOrder(int supplierBN, int orderId, Date date) {
        try {
            applicationFacade.unActiveFixedOrder(supplierBN, orderId, date);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response sendPDFDOrder(int supplierBN, int orderId, String email) {
        try {
            applicationFacade.sendPDFDOrder(supplierBN, orderId, email);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response addSupplyDayToFixedOrder(int supplierBN, int orderId, DayOfWeek day) {
        try {
            applicationFacade.addSupplyDayToFixedOrder(supplierBN, orderId, day);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeSupplyDayToFixedOrder(int supplierBN, int orderId, DayOfWeek day) {
        try {
            applicationFacade.removeSupplyDayToFixedOrder(supplierBN, orderId, day);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public ResponseT<List<FixedOrder>> dailyFixedOrders() {
        try {
            return new ResponseT<>(applicationFacade.dailyFixedOrders());
        }
        catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public Response createNextWeekFixedOrders(String branchAddress) {
        try {
            applicationFacade.createNextWeekFixedOrders(branchAddress);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    // bill of quantity-----------------------------------------------------------------------------------------------------
    public ResponseT<SBillOfQuantities> getBillOfQuantity(String branchAddress, int supplierBN) {
        try {
            return new ResponseT<>(new SBillOfQuantities(branchAddress, applicationFacade.getBillOfQuantity(supplierBN)));
        }
        catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public Response updateItemPrice(String branchAddress, int supplierBN, int catalogNumber, double newPrice) {
        try {
            applicationFacade.updateItemPrice(branchAddress, supplierBN, catalogNumber, newPrice);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public ResponseT<Map<Integer, Double>> getItemDiscounts(String branchAddress, int supplierBN, int catalogNumber) {
        try {
            return new ResponseT<>(applicationFacade.getItemDiscounts(branchAddress, supplierBN,catalogNumber));
        }
        catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public Response addItemDiscountAccordingToAmount(String branchAddress, int supplierBN, int catalogNumber, int amount, double discount) {
        try {
            applicationFacade.addItemDiscountAccordingToAmount(branchAddress, supplierBN, catalogNumber, amount, discount);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response updateItemDiscountAccordingToAmount(String branchAddress, int supplierBN, int catalogNumber, int amount, double newDiscount) {
        try {
            applicationFacade.updateItemDiscountAccordingToAmount(branchAddress, supplierBN, catalogNumber, amount, newDiscount);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response removeItemDiscountAccordingToAmount(String branchAddress, int supplierBN, int catalogNumber, int amount) {
        try {
            applicationFacade.removeItemDiscountAccordingToAmount(branchAddress, supplierBN, catalogNumber, amount);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response addItemToSupplier(String branchAddress, int supplierBN, int catalogNumber, int supplierCatalog, double price, String name) {
        try {
            applicationFacade.addStorageItem(branchAddress, supplierBN, catalogNumber, supplierCatalog, price, name);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response updateSupplierCatalog(String branchAddress, int supplierBN, int catalogNumber, int newSupplierCatalog) {
        try {
            applicationFacade.updateSupplierCatalog(branchAddress, supplierBN, catalogNumber, newSupplierCatalog);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response removeItemFromSupplier(String branchAddress, int supplierBN, int catalogNumber) {
        try {
            applicationFacade.removeItem(branchAddress, supplierBN, catalogNumber);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response updateItemName(String branchAddress, int supplierBN, int catalogNumber, String newName) {
        try {
            applicationFacade.updateItemName(branchAddress, supplierBN,catalogNumber,newName);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }
    //---------------------------------------------------------------------------------------------------
}
