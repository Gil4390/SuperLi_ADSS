package ServiceLayer;

import BusinessLayer.ApplicationFacade;
import BusinessLayer.EmployeeModule.Objects.Branch;
import BusinessLayer.SupplierBusiness.Contact;
import BusinessLayer.SupplierBusiness.DemandOrder;
import BusinessLayer.SupplierBusiness.FixedOrder;
import BusinessLayer.SupplierBusiness.Order;
import ServiceLayer.Objects.DeliveryObjects.FDelivery;
import ServiceLayer.Objects.DeliveryObjects.FSiteDoc;
import ServiceLayer.Objects.DeliveryObjects.FStockShortness;
import ServiceLayer.Objects.DeliveryObjects.FTruck;
import ServiceLayer.Objects.EmployeeObjects.FEmployee;
import ServiceLayer.Objects.EmployeeObjects.FShift;
import ServiceLayer.Objects.InventoryObjects.*;
import ServiceLayer.Objects.SupplierObjects.SBillOfQuantities;
import ServiceLayer.Objects.SupplierObjects.SSupplier;
import ServiceLayer.Responses.*;
import ServiceLayer.Services.DeliveryService;
import ServiceLayer.Services.EmployeeService;
import ServiceLayer.Services.InventoryService;
import ServiceLayer.Services.SupplierService;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class ApplicationService {
    private final ApplicationFacade applicationFacade;
    private final InventoryService inventoryService;
    private final SupplierService supplierService;
    private final DeliveryService deliveryService;
    private final EmployeeService employeeService;

    public ApplicationService() {
        this.applicationFacade = new ApplicationFacade();
        this.inventoryService = new InventoryService(applicationFacade);
        this.supplierService = new SupplierService(applicationFacade);
        this.deliveryService = new DeliveryService(applicationFacade);
        this.employeeService = new EmployeeService(applicationFacade);
    }

    public ApplicationFacade getApplicationFacade() { return applicationFacade; }
    public InventoryService getInventoryService() {return inventoryService;}
    //supplier----------------------------------------------------------------------------------
    public ResponseT<SSupplier> getSupplier(int supplierBN) { return supplierService.getSupplier(supplierBN); }
    public ResponseT<List<Contact>> getAllContacts(int supplierBN) { return supplierService.getAllContacts(supplierBN); }
    public ResponseT<List<SSupplier>> getAllSuppliers() { return supplierService.getAllSuppliers(); }
    public Response addSupplier(String name, int businessNumber, int bankAccount, boolean shouldDeliver ,String paymentMethodStr, Set<DayOfWeek> daysOfSupply, String address, String area) { return supplierService.addSupplier(name, businessNumber, bankAccount, shouldDeliver, paymentMethodStr, daysOfSupply, address, area); }
    public Response removeSupplier(int supplierBN)
    {
        return supplierService.removeSupplier(supplierBN);
    }
    public Response updateSupplierName(int supplierBN, String newName) { return supplierService.updateSupplierName(supplierBN, newName); }
    public Response updateSupplierBankAccount(int supplierBN, int newBankAccount) { return supplierService.updateSupplierBankAccount(supplierBN, newBankAccount); }
    public Response updateSupplierDelivery(int supplierBN, boolean newDeliveryMethod) { return supplierService.updateSupplierDelivery(supplierBN, newDeliveryMethod); }
    public Response updateSupplierPaymentMethod(int supplierBN, String newPaymentMethod) { return supplierService.updateSupplierPaymentMethod(supplierBN, newPaymentMethod); }
    public Response addContact(int supplierBN, String contactName, String phoneNumber, String email) { return supplierService.addContact(supplierBN, contactName, phoneNumber, email); }
    public Response removeContact(int supplierBN,int contactId) { return supplierService.removeContact(supplierBN, contactId); }
    public Response updateContactName(int supplierBN, int contactId, String newContactName) { return supplierService.updateContactName(supplierBN, contactId, newContactName); }
    public Response updateContactEmail(int supplierBN, int contactId, String newEmail) { return supplierService.updateContactEmail(supplierBN, contactId, newEmail); }
    public Response updateContactPhone(int supplierBN, int contactId, String newPhoneNumber) { return supplierService.updateContactPhone(supplierBN, contactId, newPhoneNumber); }
    //-----------------------------------------------------------------------------------------------------------------------------------
    //orders-----------------------------------------------------------------------------------------------------------------------
    public Response addFixedOrder(String branchAddress, int supplierBN, Date orderDate, Set<DayOfWeek> supplyDays, HashMap<Integer, Integer> itemIdAndAmount) { return supplierService.addFixedOrder(branchAddress, supplierBN, orderDate, supplyDays, itemIdAndAmount); }
    public Response addFixedOrders(String branchAddress, Date orderDate, Set<DayOfWeek> supplyDays, HashMap<Integer, Integer> itemIdAndAmount) { return supplierService.addFixedOrders(branchAddress, orderDate, supplyDays, itemIdAndAmount); }
    public Response addDemandOrders(String branchAddress, Date orderDate, Date supplyDay, HashMap<Integer, Integer> itemIdAndAmount) { return supplierService.addDemandOrders(branchAddress, orderDate, supplyDay, itemIdAndAmount); }
    public Response addProductsToFixedOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) { return supplierService.addProductsToFixedOrder(supplierBN, orderId, itemIdAndAmount); }
    public Response removeProductsFromFixedOrder(int supplierBN, int orderId, List<Integer> itemIdAndAmount) { return supplierService.removeProductsFromFixedOrder(supplierBN, orderId, itemIdAndAmount); }
    public Response updateProductsOfFixedOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) { return supplierService.updateProductsOfFixedOrder(supplierBN, orderId, itemIdAndAmount); }
    public Response addProductsToDemandOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) { return supplierService.addProductsToDemandOrder(supplierBN, orderId, itemIdAndAmount); }
    public Response removeProductsFromDemandOrder(int supplierBN, int orderId, List<Integer> itemIdAndAmount) { return supplierService.removeProductsFromDemandOrder(supplierBN, orderId, itemIdAndAmount); }
    public Response updateProductsOfDemandOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) { return supplierService.updateProductsOfDemandOrder(supplierBN, orderId, itemIdAndAmount); }
    public Response addDemandOrder(String branchAddress, int supplierBN, Date orderDate, Date supplyDate, HashMap<Integer, Integer> itemIdAndAmount) { return supplierService.addDemandOrder(branchAddress, supplierBN, orderDate, supplyDate, itemIdAndAmount); }
    public Response removeUnSuppliedDemandOrder(int supplierBN, int orderId) { return supplierService.removeUnSuppliedDemandOrder(supplierBN, orderId); }
    public ResponseT<List<Order>> getAllSupplierOrders(int supplierBN) { return supplierService.getAllSupplierOrders(supplierBN); }
    public ResponseT<List<DemandOrder>> getAllDemandOrders() { return supplierService.getAllDemandOrders();}
    public ResponseT<List<FixedOrder>> getAllFixedOrders() { return supplierService.getAllFixedOrders();}
    public ResponseT<List<DemandOrder>> getAllSupplierDemandOrders(int supplierBN) { return supplierService.getAllSupplierDemandOrders(supplierBN); }
    public ResponseT<List<FixedOrder>> getAllSupplierFixedOrders(int supplierBN) { return supplierService.getAllSupplierFixedOrders(supplierBN); }
    public Response supplyDemandOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndUnSuppliedAmount) { return supplierService.supplyDemandOrder(supplierBN, orderId, itemIdAndUnSuppliedAmount); }
    public Response unActiveFixedOrder(int supplierBN, int orderId, Date date) { return supplierService.unActiveFixedOrder(supplierBN, orderId, date); }
    public Response sendPDFDOrder(int supplierBN, int orderId, String email) { return supplierService.sendPDFDOrder(supplierBN, orderId, email); }
    public Response addSupplyDayToFixedOrder(int supplierBN, int orderId, DayOfWeek day) { return supplierService.addSupplyDayToFixedOrder(supplierBN, orderId, day); }
    public Response removeSupplyDayToFixedOrder(int supplierBN, int orderId, DayOfWeek day) { return supplierService.removeSupplyDayToFixedOrder(supplierBN, orderId, day); }
    public ResponseT<List<FixedOrder>> dailyFixedOrders() { return supplierService.dailyFixedOrders(); }
    public Response createNextWeekFixedOrders(String branchAddress) { return supplierService.createNextWeekFixedOrders(branchAddress); }
    //-----------------------------------------------------------------------------------------------------------------------------
    // bill of quantity-----------------------------------------------------------------------------------------------------
    public ResponseT<SBillOfQuantities> getBillOfQuantity(String branchAddress, int supplierBN) { return supplierService.getBillOfQuantity(branchAddress, supplierBN); }
    public Response updateItemPrice(String branchAddress, int supplierBN, int catalogNumber, double newPrice) { return supplierService.updateItemPrice(branchAddress, supplierBN,catalogNumber, newPrice);}
    public ResponseT<Map<Integer, Double>> getItemDiscounts(String branchAddress, int supplierBN, int catalogNumber) { return supplierService.getItemDiscounts(branchAddress, supplierBN,catalogNumber);}
    public Response addItemDiscountAccordingToAmount(String branchAddress, int supplierBN, int catalogNumber, int amount, double discount) { return supplierService.addItemDiscountAccordingToAmount(branchAddress, supplierBN,catalogNumber,amount,discount); }
    public Response updateItemDiscountAccordingToAmount(String branchAddress, int supplierBN, int catalogNumber, int amount, double newDiscount) { return supplierService.updateItemDiscountAccordingToAmount(branchAddress, supplierBN,catalogNumber,amount,newDiscount); }
    public Response removeItemDiscountAccordingToAmount(String branchAddress, int supplierBN, int catalogNumber, int amount) { return supplierService.removeItemDiscountAccordingToAmount(branchAddress, supplierBN,catalogNumber,amount);}
    public Response updateSupplierCatalog(String branchAddress, int supplierBN, int catalogNumber, int newSupplierCatalog) { return supplierService.updateSupplierCatalog(branchAddress, supplierBN,catalogNumber,newSupplierCatalog); }
    public Response updateItemName(String branchAddress, int supplierBN, int catalogNumber, String newName) { return supplierService.updateItemName(branchAddress, supplierBN,catalogNumber,newName); }
    public Response addItemToSupplier(String branchAddress, int supplierBN, int catalogNumber, int supplierCatalog, double price, String name) {return supplierService.addItemToSupplier(branchAddress, supplierBN,catalogNumber,supplierCatalog,price,name);}
    public Response removeItemFromSupplier(String branchAddress, int supplierBN, int catalogNumber) {return supplierService.removeItemFromSupplier(branchAddress, supplierBN,catalogNumber);}
    //---------------------------------------------------------------------------------------------------
    //ReportMaker----------------------------------------------------------------------------------
    public ResponseT<List<FInventoryItem>> getInventoryReport(String branchAddress) { return inventoryService.getInventoryReport(branchAddress);}
    public ResponseT<List<FInventoryItem>> getInventoryReportByCategory(String branchAddress, String categoryID) { return inventoryService.getInventoryReportByCategory(branchAddress, categoryID);}
    public ResponseT<List<FInShortageItem>> getShortageReport(String branchAddress) { return inventoryService.getShortageReport(branchAddress);}
    public ResponseT<List<FDamagedItem>> getDamageReport(String branchAddress) { return inventoryService.getDamageReport(branchAddress);}
    public ResponseT<List<FDamagedItem>> getDamageReportByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) {return inventoryService.getDamageReportByDate(branchAddress, sinceWhen, untilWhen);}
    public ResponseT<List<FDamagedItem>> getDamageReportByItemID(String branchAddress, int itemID) {return inventoryService.getDamageReportByItemID(branchAddress, itemID);}
    public ResponseT<List<FPurchasedItem>> getPurchaseReport(String branchAddress) { return inventoryService.getPurchaseReport(branchAddress);}
    public ResponseT<List<FPurchasedItem>> getPurchaseReportByItemID(String branchAddress, int itemID) { return inventoryService.getPurchaseReportByItemID(branchAddress, itemID);}
    public ResponseT<List<FPurchasedItem>> getPurchaseReportByBusinessNumber(String branchAddress, int supplierNumber) { return inventoryService.getPurchaseReportByBusinessNumber(branchAddress, supplierNumber);}
    public ResponseT<List<FPurchasedItem>> getPurchaseReportByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) { return inventoryService.getPurchaseReportByDate(branchAddress, sinceWhen, untilWhen);}
    public ResponseT<List<FDamagedItem>> getOnlyExpiredReport(String branchAddress) {
        return inventoryService.getOnlyExpiredReport(branchAddress);
    }
    public ResponseT<List<FDamagedItem>> getOnlyDamageReport(String branchAddress) {
        return inventoryService.getOnlyDamageReport(branchAddress);
    }
    public ResponseT<List<FDamagedItem>> getOnlyDamageReportByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) {return inventoryService.getOnlyDamageReportByDate(branchAddress, sinceWhen, untilWhen);}
    public ResponseT<List<FDamagedItem>> getOnlyExpiredReportByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) {return inventoryService.getOnlyExpiredReportByDate(branchAddress, sinceWhen, untilWhen);}
    public ResponseT<List<FDamagedItem>> getOnlyDamageReportByItemID(String branchAddress, int itemID) {return inventoryService.getOnlyDamageReportByItemID(branchAddress, itemID);}
    public ResponseT<List<FDamagedItem>> getOnlyExpiredReportByItemID(String branchAddress, int itemID) {return inventoryService.getOnlyExpiredReportByItemID(branchAddress, itemID);}
    //addRecords------------------------
    public Response addPurchaseRecord(String branchAddress, int desiredItemID, int orderID, int checkedSupplier, int checkedAmount, double checkedPrice, double checkedDiscount) {return inventoryService.addPurchaseRecord(branchAddress, desiredItemID,orderID,checkedSupplier,checkedAmount,checkedPrice,checkedDiscount);}
    public Response addDamagedRecord(String branchAddress, int desiredItemID, int amount, FDamageReason reason, int back0Front1) {return inventoryService.addDamagedRecord(branchAddress, desiredItemID,amount,reason,back0Front1);}
    //-----------------------------------------------------------------------------------------
    //Items------------------------------------------------------------------------------------
    public ResponseT<List<FItem>> getAllItemsInCategory(String branchAddress, String categoryID) {return inventoryService.getAllItemsInCategory(branchAddress, categoryID);}
    public ResponseT<FItem> getItem(String branchAddress, int itemID){ return inventoryService.getItem(branchAddress, itemID); }
    public ResponseT<FItem> addItem(String branchAddress, String name, String catID, double price, int minimalQuantity, int fullQuantity, String manufacture, List<Integer> backShelves, List<Integer> frontShelves){return inventoryService.addItem(branchAddress, name,catID,price,minimalQuantity,fullQuantity,manufacture,backShelves,frontShelves);}
    public Response removeQuantityFromItem(String branchAddress, int itemId, int quantity, boolean fromBackRoom){return inventoryService.removeQuantityFromItem(branchAddress, itemId, quantity, fromBackRoom);}
    public Response addQuantityToItem(String branchAddress, int itemId, int quantity, boolean fromBackRoom){return inventoryService.addQuantityToItem(branchAddress, itemId, quantity, fromBackRoom);}
    //----------------------------------------------------------------------------------------
    //Item Details------------------------------------------------------------------------------------
    public Response addDiscount(String branchAddress, int itemId, String disName, LocalDate fromDate, LocalDate toDate, double discountFare) {return inventoryService.addDiscount(branchAddress, itemId,disName,fromDate,toDate,discountFare);}
    public Response giveDiscountToCategory(String branchAddress, String categoryID, FDiscount fDiscount) {return inventoryService.giveDiscountToCategory(branchAddress, categoryID, fDiscount);}
    public Response removeDiscount(String branchAddress, int itemId, LocalDate fromDate) {return inventoryService.removeDiscount(branchAddress, itemId, fromDate);}
    public Response moveItemsBetweenRooms(String branchAddress, int itemId, boolean fromBackRoom, int quantity) {return inventoryService.moveItemsBetweenRooms(branchAddress, itemId, fromBackRoom, quantity);}
    public Response changeItemPrice(String branchAddress, int itemID, int checkedAmount) {return inventoryService.changeItemPrice(branchAddress, itemID, checkedAmount);}
    public Response changeMinimalQuantity(String branchAddress, int itemID, int checkedQuantity) {return inventoryService.changeMinimalQuantity(branchAddress, itemID, checkedQuantity);}
    public Response changeFullQuantity(String branchAddress, int itemID, int checkedQuantity) {return inventoryService.changeFullQuantity(branchAddress, itemID, checkedQuantity);}
    //----------------------------------------------------------------------------------------
    // Category--------------------------------------------------------------------------------
    public ResponseT<FCategory> getCategory(String branchAddress, String categoryID) { return inventoryService.getCategory(branchAddress, categoryID);}
    public Response moveCategory(String branchAddress, FCategory newFatherCat, FCategory deliveredCat) { return inventoryService.moveCategory(branchAddress, newFatherCat,deliveredCat);}
    public Response addCategory(String branchAddress, String newFatherCat, String catName) { return inventoryService.addCategory(branchAddress, newFatherCat,catName);}
    public ResponseT<List<FCategory>> getAllCategorySubCat(String branchAddress, String categoryID) { return inventoryService.getAllCategorySubCat(branchAddress, categoryID);}
    public ResponseT<FCategory> getCategoryFather(String branchAddress, String categoryID) { return inventoryService.getCategoryFather(branchAddress, categoryID);}
    public Response setCategoryName(String branchAddress, String categoryID, String newName) { return inventoryService.setCategoryName(branchAddress, categoryID,newName);}
    public Response removeCategory(String branchAddress, String categoryID) {return inventoryService.removeCategory(branchAddress, categoryID);}
    //-----------------------------------------------------------------------------------------
    // ItemLocation and Shelves---------------------------------------------------------------
    public ResponseT<List<Integer>> getAllBackShelves(String branchAddress) { return inventoryService.getAllBackShelves(branchAddress);}
    public ResponseT<List<Integer>> getAllFrontShelves(String branchAddress) { return inventoryService.getAllFrontShelves(branchAddress);}
    public Response addShelf(String branchAddress, boolean isInBackShelf) { return inventoryService.addShelf(branchAddress, isInBackShelf);}
    public ResponseT<List<Integer>> getAllFrontShelvesByItem(String branchAddress, int ID) { return inventoryService.getAllFrontShelvesByItem(branchAddress, ID);}
    public ResponseT<List<Integer>> getAllBackShelvesByItem(String branchAddress, int ID) { return inventoryService.getAllBackShelvesByItem(branchAddress, ID);}
    public Response changeItemAppointedShelvesByRoom(String branchAddress, int ID, boolean inBackRoom, int [] IDShelvesList) { return inventoryService.changeItemAppointedShelvesByRoom(branchAddress, ID,inBackRoom,IDShelvesList);}
    //-----------------------------------------------------------------------------------------
    //Delivery---------------------------------------------------------------------------------
    public ResponseT<FDelivery> createDelivery(String date, String time, String licenseNumber, int id, List<Integer> stockShortnesses, boolean checkShippingArea) { return deliveryService.createDelivery(date, time, licenseNumber, id, stockShortnesses, checkShippingArea); }
    public Response weightTruck(int documentId, int weight) { return deliveryService.weightTruck(documentId, weight); }
    public Response changeTruck(int documentId, String licenseNumber) { return deliveryService.changeTruck(documentId, licenseNumber); }
    public Response changeSites(int documentId, List<Integer> dropStock, List<Integer> addStock, boolean checkShippingArea) { return deliveryService.changeSites(documentId, dropStock, addStock, checkShippingArea); }
    public Response arrivedToSite(int documentId) { return deliveryService.arrivedToSite(documentId); }
    public ResponseT<List<FTruck>> getAllTrucksAvailable(String date) { return deliveryService.getAllTrucksAvailable(date); }
    public ResponseT<List<FStockShortness>> getAllRelevantStockShortnesses() { return deliveryService.getAllRelevantStockShortnesses(); }
    public ResponseT<List<FDelivery>> getAllDeliveries() { return deliveryService.getAllDeliveries(); }
    public ResponseT<FTruck> getTruckByLicense(String truckLicense) { return deliveryService.getTruckByLicense(truckLicense); }
    public ResponseT<List<FSiteDoc>> getAllDocumentsForDelivery(int deliveryId){ return deliveryService.getAllDocumentsForDelivery(deliveryId); }
    public ResponseT<Integer> getLatestAddress(int deliveryId) { return deliveryService.getLatestAddress(deliveryId); }
    public ResponseT<List<FSiteDoc>> getDocumentForDeliveryAndSite(int deliveryId, String siteAddress) { return deliveryService.getDocumentForDeliveryAndSite(deliveryId, siteAddress); }
    public ResponseT<List<String>> getListOfAddressesForDelivery(int deliveryId) { return deliveryService.getListOfAddressesForDelivery(deliveryId); }
    public ResponseT<Boolean> isDeliveryFinished(int id) { return deliveryService.isDeliveryFinished(id); }
    public ResponseT<List<FStockShortness>> getStockShortnessOfDelivery(int id) { return deliveryService.getStockShortnessOfDelivery(id); }
    public ResponseT<List<FTruck>> getAllRelevantTrucks(String dateString, int siteDocId) { return deliveryService.getAllRelevantTrucks(dateString, siteDocId); }
    public ResponseT<FSiteDoc> getDocumentForDeliveryAndlocation(int deliveryId, int locationInAddressList) { return deliveryService.getDocumentForDeliveryAndlocation(deliveryId, locationInAddressList); }
    //-------------------------------------------------------------------------
    //Employee---------------------------------------------------------------------------------
    public ResponseT<FEmployee> getEmployee(int employeeID) { return employeeService.getEmployee(employeeID); }
    public ResponseT<List<FEmployee>> getAllEmployees() { return employeeService.getAllEmployees(); }
    public ResponseT<List<FEmployee>> getAvailableEmployeesForShift(String date, String shiftType) { return employeeService.getAvailableEmployeesForShift(date, shiftType); }
    public Response addEmployee(String name, int ID, String bank_Account, int salary, String employment_start_date , String jobTitle, String employmentDetails,String number) { return employeeService.addEmployee(name, ID, bank_Account, salary, employment_start_date, jobTitle, employmentDetails,number); }
    public Response addEmployee(String name, int ID, String bank_Account, int salary, String employment_start_date , String jobTitle, String employmentDetails, Vector<String> certifications, String number) { return employeeService.addEmployee(name, ID, bank_Account, salary, employment_start_date, jobTitle, employmentDetails, certifications,number); }
    public Response removeEmployee(int employeeID){ return employeeService.removeEmployee(employeeID); }
    public Response addJobToEmployee(int employeeID, String newJob){ return employeeService.addJobToEmployee(employeeID, newJob); }
    public Response removeJobFromEmployee(int employeeID, String jobTitle){ return employeeService.removeJobFromEmployee(employeeID, jobTitle); }
    public Response addCertToEmployee(int empID, String jobTitle, String certName) { return employeeService.addCertToEmployee(empID, jobTitle, certName); }
    public Response addJobConstraint(int employeeID, String date, String shift){ return employeeService.addJobConstraint(employeeID, date, shift); }
    public Response updateEmployeeName(int employeeID, String newName) { return employeeService.updateEmployeeName(employeeID, newName); }
    public Response updateEmployeeBankAccount(int employeeID, String newBankAccount) { return employeeService.updateEmployeeBankAccount(employeeID, newBankAccount); }
    public Response updateEmployeeSalary(int employeeID, int newSalary) { return employeeService.updateEmployeeSalary(employeeID, newSalary); }
    public Response updateEmploymentDetails(int employeeID, String employmentDetails) { return employeeService.updateEmploymentDetails(employeeID, employmentDetails); }
    public ResponseT<List<String>> getAllBranchesEmployeeWorksIn(int ID) { return employeeService.getAllBranchesEmployeeWorksIn(ID); }
    //////////////////SHIFTS////////////////////////
    public ResponseT<FShift> getShift(int shiftID) { return employeeService.getShift(shiftID); }
    public ResponseT<List<FShift>> getAllShifts() { return employeeService.getAllShifts(); }
    public ResponseT<List<FShift>> getShiftsOfEmployee(int employeeID){ return employeeService.getShiftsOfEmployee(employeeID); }
    public Response addShift(String addressBranch, String date, String shift_type, HashMap<Integer,String> employees){ return employeeService.addShift(addressBranch, date, shift_type, employees); }
    public Response addbranch(String addressBranch , String shippingArea , int managerID){ return employeeService.addbranch(addressBranch, shippingArea, managerID); }
    public Response removebranch(String addressBranch){ return employeeService.removebranch(addressBranch); }
    public Response removeShift(int shiftID){ return employeeService.removeShift(shiftID); }
    public Response addEmployeeToShift(int shiftID, int eID, String job) { return employeeService.addEmployeeToShift(shiftID, eID, job); }
    public Response removeEmployeeFromShift(int shiftID, int oldID) { return employeeService.removeEmployeeFromShift(shiftID, oldID); }
    public ResponseT<FShift> getShiftByDateTypeAndBranch(String date, String typeShift, String branch) { return employeeService.getShiftByDateTypeAndBranch(date, typeShift, branch); }
    public ResponseT<Boolean> validEmployeeHasJob(String jobTitle, String employeeID) { return employeeService.validEmployeeHasJob(jobTitle, employeeID); }
    public Response validEmployeesInShiftJob(HashMap<Integer, String> employeesToShift) { return employeeService.validEmployeesInShiftJob(employeesToShift); }
    public ResponseT<List<FShift>> getShiftLastMonth(String morning_evening, int employeeID) { return employeeService.getShiftLastMonth(morning_evening, employeeID); }
    public ResponseT<List<FEmployee>> getDriversRelevant(int maxWeight, String date) { return employeeService.getDriversRelevant(maxWeight, date); }
    public ResponseT<List<String>> getDriverLicencesForDriver(int id) { return employeeService.getDriverLicencesForDriver(id); }
    /////////////BRANCH/////////////////
    public ResponseT<List<String>> getAllBranchesByAddress() { return employeeService.getAllBranchesByAddress(); }
    public ResponseT<Branch> getAddressOfHQ() { return employeeService.getAddressOfHQ(); }
    public ResponseT<List<Integer>>  getDriversToSpecificShift(String date)throws Exception  { return employeeService.getDriversToSpecificShift(date); }
    public ResponseT<List<FEmployee>> getAvailableDrivers(String date) { return employeeService.getAvailableDrivers(date); }
    public ResponseT<List<String>> pullMessages(String branchAddress , String  job) { return employeeService.pullMessages(branchAddress,job); }
    public ResponseT<List<String>> pullMessagesForJob(String  job) { return employeeService.pullMessagesForJob(job); }
    //---------------------------------------------------------------------------
    // Integration---------------------------------------------------------------
    public Response orderShortage(String branchAddress) {
        try {
            applicationFacade.orderShortage(branchAddress);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public ResponseT<String> getBranchOfManager(int managerID) {
        return employeeService.getBranchOfManager(managerID);
    }

    public String getBranchOfShift(int ID)
    {
        return employeeService.getBranchOfShift(ID);
    }

    public void deleteDataForTests() {
        applicationFacade.deleteAllData();
    }
}
