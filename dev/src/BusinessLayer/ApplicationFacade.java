package BusinessLayer;

import BusinessLayer.DeliveryModule.DeliveryFacade;
import BusinessLayer.DeliveryModule.Objects.Delivery;
import BusinessLayer.DeliveryModule.Objects.SiteDoc;
import BusinessLayer.DeliveryModule.Objects.StockShortness;
import BusinessLayer.DeliveryModule.Objects.Truck;
import BusinessLayer.EmployeeModule.Objects.Employee;
import BusinessLayer.EmployeeModule.Objects.Shift;
import BusinessLayer.EmployeeModule.EmployeeFacade;
import BusinessLayer.InventoryBusiness.*;
import BusinessLayer.InventoryBusiness.ReportItems.*;
import BusinessLayer.SupplierBusiness.*;

import DataAccessLayer.DeliveryModuleDal.DControllers.CacheCleaner;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class ApplicationFacade {
    private final SupplierFacade supplierFacade;
    private final DeliveryFacade deliveryFacade;
    private final EmployeeFacade employeeFacade;
    private final ItemController itemController;
    private final ReportsMaker reportsMaker;
    private final ShelfHandler shelfHandler;

    public ApplicationFacade() {
        CacheCleaner cacheCleaner = new CacheCleaner();
        this.supplierFacade = new SupplierFacade();
        this.deliveryFacade = new DeliveryFacade(cacheCleaner);
        this.employeeFacade = new EmployeeFacade(cacheCleaner);
        this.itemController = new ItemController();
        this.reportsMaker = new ReportsMaker(itemController);
        this.shelfHandler = new ShelfHandler();
        cacheCleaner.clean();
    }

    public ItemController getItemController() { return itemController; }
    public SupplierFacade getSupplierFacade() { return supplierFacade; }
    public ReportsMaker getReportsMaker() { return reportsMaker; }
    public DeliveryFacade getDeliveryFacade() {return deliveryFacade;}

    //Delivery----------------------------------------------------------------------------------

    /**
     * creates the delivery and all the site documents related to it
     * @param date - the date of the new delivery
     * @param time - the time of the new delivery
     * @param TruckNumber - the truck license of the truck the new delivery is going to leave with
     * @param driverId - the id of the driver who is supposed to deliver the delivery
     * @param Items - list of stock shortness Ids that the delivery is supposed to fix
     * @return the new delivery that was created
     * @throws Exception: 1) if truck with this license doesnt exist
     *                    2) if the truck is not available on the date
     *                    3) if there is no employee with the id exist
     *                    4) if the employee doesnt have a job driver
     *                    5)if the driver is not available on the date
     *                    6) if the driver doesnt have the right license for the truck weight
     *                    7) if one of the stock shortnesses is already boound to a different delivery
     *                    8) if shippingAreaCheck is true and in the site list there are sites from more then one shipping area
     */
    public Delivery createNewDelivery(String date, String time, String TruckNumber, int driverId,
                                      List<Integer> Items, boolean checkShippingArea) throws Exception{
        int MaxTruckWeight = deliveryFacade.getTruckByLicense(TruckNumber).getMaxWeight();
        employeeFacade.checkDriverValidity(date, driverId, MaxTruckWeight);
        Delivery ans = deliveryFacade.createDelivery(date, time, TruckNumber, driverId, Items, checkShippingArea);
        employeeFacade.setDriverUnavailable(driverId, date);
        return ans;
    }

    public Date createDelivery(List<Date> dates, Order order) throws Exception {
        //returns a date available for a delivery (checks the driver and truck)
        int supplierBn = order.getSupplierBN();
        String supplierAddress = getSupplier(supplierBn).getAddress();
        List<Integer> stockShortnessList = deliveryFacade.createStockShortnessListFromOrder(order, supplierAddress);
        for (Date d: dates){
            if (addToDelivery(d.toString(), stockShortnessList)) return d;
            if (canCreateDelivery(d, stockShortnessList))
                return d;
        }
        employeeFacade.pushMessage("HR manager", "was not able to create a delivery for order number "+ order.getOrderId());
        return null;
    }

    private Boolean canCreateDelivery(Date date, List<Integer> stockShortnessList) throws Exception {
        List <Truck> trucks = deliveryFacade.getAllTrucksAvailable(date.toString());
        for (Truck t : trucks){
            List<Employee> drivers =employeeFacade.getDriversRelevant(t.getMaxWeight(), date.toString());
            if (!drivers.isEmpty()){
                try{createNewDelivery(date.toString(), "08:00", t.getLicenseNumber(), drivers.get(0).getID(),stockShortnessList, true);
                    return true;}
                catch (Exception e) {
                    if (e.getMessage().equals("the delivery contains multiple shipping areas")) {
                        Delivery delivery = createNewDelivery(date.toString(), "08:00", t.getLicenseNumber(), drivers.get(0).getID(),stockShortnessList, false);
                        employeeFacade.pushMessage("delivery manager", "a delivery numbered "+delivery.getId()+" was created with multiple shipping areas");
                        return true;
                    }
                }

            }
        }
        deliveryFacade.deleteStockShortnesses(stockShortnessList);
        return false;
    }

    private List<Integer> createStockShortnessListFromOrder(Order order) throws Exception {
        int supplierBn = order.getSupplierBN();
        String supplierAddress = getSupplier(supplierBn).getAddress();
        String branchAddress = order.getBranchAddress();
        Map<Integer, OrderProduct> orderProducts = order.getOrderProducts();
        List<Integer> stockShortnesses = new LinkedList<>();
        for (OrderProduct orderProduct: orderProducts.values())
            stockShortnesses.add(deliveryFacade.createStockShortness(branchAddress,orderProduct.getName(), orderProduct.getProductId(), orderProduct.getAmount(), supplierAddress, order.getOrderId()));
        return stockShortnesses;
    }

    private boolean addToDelivery(String date, List<Integer> stockShortnessList) throws Exception {
        List<Integer> deliveryIds = deliveryFacade.getDeliveryIdInDate(date);
        for (Integer deliveryId : deliveryIds) {
            try {
                int documentId = getLatestAddress(deliveryId);
                changeSites(documentId, new LinkedList<>(), stockShortnessList, true);
                return true;
            } catch (Exception e) {
            }
        }
        return false;

    }

    /**
     * when leaving a site the truck is weighted and updated
     * if the truck is over wight adds a log to the site document
     * @param documentId the site document id of which to update the weight of the truck
     * @param weight the new weight
     * @throws Exception 1) if there is no site doc with that id
     *                   2) if the weight is greater than the max weight of the truck
     */
    public void weightTruck(int documentId, int weight) throws Exception {
        deliveryFacade.weightTruck(documentId, weight);
    }

    /**
     * change truck of the delivery of @documentId
     * in addition adds log to every site document of future sites in the delivery
     * @param documentId the site document id of which you need to change the truck
     * @param truckLicense the license of the new truck
     * @throws Exception 1) if a truck with this license doesnt exist
     *                   2) if a site document with this id doesnt exist
     *                   3) if the driver related to the delivery thats related to the site doc
     *                      doesnt have the right license for the new truck
     *                   4) if the new truck weight exceed the max truck weight
     *                   5) if the truck is not available for the date of the delivery
     */
    public void changeTruck(int documentId, String truckLicense) throws Exception{
        int MaxTruckWeight = deliveryFacade.getTruckByLicense(truckLicense).getMaxWeight();
        int driverId = deliveryFacade.getDriverOfSiteDoc(documentId);
        employeeFacade.checkDriverValidityForTruck(driverId, MaxTruckWeight);
        deliveryFacade.changeTruck(documentId, truckLicense);
    }

    /**
     * changes the site list of the delivery of @documentId
     * @param documentId the document of the site in which the user decided to change the
     *                   stock shortness list of the delivery
     * @param dropped the list of stock shortness ids the user decided to drop from mthe delivery
     * @param added the list of stock shortness ids the user decided to add to the delivery
     * @throws Exception 1) if a site document with this id doesnt exist
     *                   2) if in added/dropped list there is an id that doesnt fit any stock shortness
     *                   3) if in added list there is a stock shortness thats already bound
     *                   4) if in dropped list there is a stock shortness not bounded to the current delivery
     */
    public void changeSites(int documentId, List<Integer> dropped, List<Integer> added, boolean checkShippingArea) throws Exception{
        deliveryFacade.changeSites(documentId, dropped, added, checkShippingArea);
    }

    public void changeOrderInDelivery(Order newOrder) throws Exception {
        List<Integer> oldStocks = deliveryFacade.getStockShortnessOfOrder(newOrder.getOrderId());
        deliveryFacade.changeOrderInDelivery(newOrder.getOrderId() ,createStockShortnessListFromOrder(newOrder), oldStocks);
    }


    public void deleteOrderInDelivery(int OrderId) throws Exception {
        List<Integer> oldStocks = deliveryFacade.getStockShortnessOfOrder(OrderId);
        deliveryFacade.changeOrderInDelivery(OrderId,new LinkedList<>(), oldStocks);
    }

    /**
     * deletes all the stock shortnesses finished from the system-
     * meaning all the stock shortnesses bound to the unload of the site doc
     * @param documentId the document of the site the delivery has arrived to
     * @throws Exception 1) there is no site doc with this id
     *
     */
    public void arrivedToSite(int documentId) throws Exception {
        SiteDoc document = deliveryFacade.getDocument(documentId);
        if (employeeFacade.isABranch(document.getSiteAddress())) {
            employeeFacade.pushMessage("warehouse",document.getSiteAddress(), "delivery numbered "+deliveryFacade.getDocument(documentId).getDeliveryId()+" has arrived!");
            for (Map.Entry<Integer, Integer> e: document.getUnloadItems().entrySet())
                employeeFacade.pushMessage("warehouse", document.getSiteAddress(), "you received: " + e.getValue()+" of item with catalog number of: "+e.getKey());
            deliveryFacade.finisheOrder(documentId);
        }
    }

    /**
     * returns all the trucks available on this date
     * @param date - the date in question
     * @return all the trucks available on this date
     * @throws Exception data base connection exception
     */
    public List<Truck> getAllTrucksAvailable(String date) throws Exception{
        return deliveryFacade.getAllTrucksAvailable(date);
    }

    /**
     * gets all the drivers available on the date and can drive a truck with that weight
     * @param maxWeight the weight in question
     * @param date the date in question
     * @return all the drivers available on the date and can drive a truck with that weight
     * @throws Exception data base connection exception
     */
    public List<Employee> getDriversRelevant(int maxWeight, String date) throws Exception{
        return employeeFacade.getDriversRelevant(maxWeight, date);
    }

    /**
     * @return returns all the unbound stock shortnesses
     * @throws Exception data base connection exception
     */
    public List<StockShortness> getAllRelevantStockShortnesses() throws Exception{
        return deliveryFacade.getAllRelevantStockShortnesses();
    }

    /**
     * @return returns all the deliveries in the system
     * @throws Exception data base connection exception
     */
    public List<Delivery> getAllDeliveries() throws Exception{
        return deliveryFacade.getAllDeliveries();
    }

    /**
     * @param truckLicense the license in question
     * @return the truck with this truck license
     * @throws Exception if there is no truck with this license
     */
    public Truck getTruckByLicense(String truckLicense) throws Exception{
        return deliveryFacade.getTruckByLicense(truckLicense);
    }

    /**
     * @param driverId the drivery id in question
     * @return returns an Employee with this id
     * @throws Exception 1) if there is no employee with this id.
     *                   2) if the employee is not a driver
     */
    public Employee getDriverById(int driverId) throws Exception{
        return employeeFacade.getEmployee(driverId);
    }

    /**
     * gets all site documents bound the the delivery with that id
     * @param deliveryId the delivery id in question
     * @return all site documents bound the the delivery with that id
     * @throws Exception data base connection exception
     */
    public List<SiteDoc> getAllDocumentsForDelivery(int deliveryId) throws Exception{
        return deliveryFacade.getAllDocumentsForDelivery(deliveryId);
    }

    /**
     * gets the latest address we visited in the delivery or null if the delivery is finished
     * eather a site document with truck weight = 0 or a site document with overweight truck weight
     * @param deliveryId the delivery in question
     * @return the latest address we visited in the delivery or null if the delivery is finished
     * @throws Exception 1) if there is no delivery with this id
     *                   2) data base connection exception
     */
    public Integer getLatestAddress(int deliveryId) throws Exception{
        return  deliveryFacade.getLatestAddress(deliveryId);
    }

    /**
     * gets all the documents with address @siteAddress bounded to delivery @deliveryId
     * @param deliveryId the delivery id that the documents are bounded to
     * @param siteAddress the address that the document mentions
     * @return all the documents with address @siteAddress bounded to delivery @deliveryId
     * @throws Exception data base connection exception
     */
    public List<SiteDoc> getDocumentForDeliveryAndSite(int deliveryId, String siteAddress) throws Exception{
        return  deliveryFacade.getDocumentForDeliveryAndSite(deliveryId, siteAddress);
    }

    /**
     * @param deliveryId the id of the delivery in question
     * @return the list of addresses of delivery with that id
     * @throws Exception if there is no delivery with this id
     */
    public List<String> getListOfAddressesForDelivery(int deliveryId) throws Exception{
        return deliveryFacade.getListOfAddressesForDelivery(deliveryId);
    }

    /**
     * returns whether the delivery is finished- we visited all the sites in the sile list
     * @param deliveryId the id of the delivery in question
     * @return whether the delivery is finished
     * @throws Exception if there is no delivery with this id
     */
    public Boolean isDeliveryFinished(int deliveryId) throws Exception{
        return deliveryFacade.isDeliveryFinished(deliveryId);
    }

    /**
     * @param deliveryId the id of the delivery in question
     * @return all the stock shortnesses bounded to the delivery
     * @throws Exception data base connection exception
     */
    public List<StockShortness> getStockShortnessOfDelivery(int deliveryId) throws Exception {
        return deliveryFacade.getStockShortnessOfDelivery(deliveryId);
    }

    /**
     * returns all the trucks available at this date and can be driven by the driver responsible
     *for the delivery that the site document with this id is bounded to
     * @param dateString the date to check if the truck is available
     * @param siteDocId the id of the site document which bounded to the delivery in question
     * @return all the trucks available at this date and can be driven by the driver responsible
     *        for the delivery that the site document with this id is bounded to
     * @throws Exception   1)data base connection exception
     *                     2) if there is no site doc with that id
     */
    public List<Truck> getAllRelevantTrucks(String dateString, int siteDocId) throws Exception {
        List<Truck> trucks = deliveryFacade.getAllRelevantTrucks(dateString, siteDocId);
        int driverId = deliveryFacade.getDriverOfSiteDoc(siteDocId);
        return employeeFacade.getRelevanteTrucks(trucks, driverId);
    }



    //employee-----------------------------------------------------------

    public String getBranchOfManager(int managerID) throws Exception {
        return employeeFacade.getBranchOfManage(managerID);
    }
    /**
     * Get Employee from database
     * @param employeeID - int represent employee id
     * @return the Employee object that his id is employeeID
     * @throws Exception from the DB or if the employee's id doesn't exist
     */
    public Employee getEmployee(int employeeID) throws Exception {
        return employeeFacade.getEmployee(employeeID);
    }

    /**
     * Get all the employees
     * @return all the employees in the db.
     * @throws Exception from the db
     */
    public List<Employee> getAllEmployees()throws Exception {
        return employeeFacade.getAllEmployees();
    }

    /**
     * Get all the available employees for the shift held in date
     * @param date
     * @param shiftType - morning/evening
     * @return the available employees in the date and morning/evening to work in shift
     * @throws Exception from the db or if the given shift wasn't created
     */
    public List<Employee> getAvailableEmployeesForShift(String date, String shiftType)throws Exception {
        return employeeFacade.getAvailableEmployeesForShift(date, shiftType);
    }

    /**
     * Add employee to db
     * @param name - name of new employee
     * @param ID - id of new employee
     * @param bank_Account - bank account details of new employee
     * @param salary - salary of new employee
     * @param employment_start_date - the time new employee start work
     * @param jobTitle - the job title of the new
     * @param employmentDetails - extra informatin of new employee
     * @throws Exception from db , if id already exist or if jobTitle isn't invalid
     */
    public void addEmployee(String name, int ID, String bank_Account, int salary, String employment_start_date, String jobTitle, String employmentDetails, String number) throws Exception {
        employeeFacade.addEmployee(name, ID, bank_Account, salary, employment_start_date, jobTitle, employmentDetails,number);
    }

    /**
     * add employee to db
     * @param name - name of new employee
     * @param ID - id of new employee
     * @param bank_Account - bank account details of new employee
     * @param salary - salary of new employee
     * @param employment_start_date - the time new employee start work
     * @param jobTitle - the job title of the new
     * @param employmentDetails - extra informatin of new employee
     * @param certifications - list of certification of new employee
     * @throws Exception from db , if id already exist or if jobTitle isn't invalid
     */
    public void addEmployee(String name, int ID, String bank_Account, int salary, String employment_start_date, String jobTitle, String employmentDetails, Vector<String> certifications, String number) throws Exception {
        employeeFacade.addEmployee(name, ID, bank_Account, salary, employment_start_date, jobTitle, employmentDetails, certifications,number);
    }

    /**
     * remove employee from db
     * @param employeeID - id of employee
     * @throws Exception from db , if id doesn't exist , if assign to future shift.
     */
    public void removeEmployee(int employeeID) throws Exception {
        employeeFacade.removeEmployee(employeeID);
    }

    /**
     * add new job to specific employee
     * @param employeeID - id of employee
     * @param newJob - the new job added to employee
     * @throws Exception from db ,  if id doesn't exist or newJobTitle isn't valid.
     */
    public void addJobToEmployee(int employeeID, String newJob) throws Exception {
        employeeFacade.addJobToEmployee(employeeID, newJob);
    }

    /**
     * remove job from employee
     * @param employeeID - id of employee
     * @param jobTitle - the job we want to remove
     * @throws Exception  if id doesn't exist, or he doesn't have the job, or he's assign to future shift with this job
     */
    public void removeJobFromEmployee(int employeeID, String jobTitle) throws Exception {
        employeeFacade.removeJobFromEmployee(employeeID, jobTitle);
    }

    /**
     * add job constraint that employee can't work in that shift
     * @param employeeID - id of employee
     * @param date - constraint's date
     * @param shift - shift type - morning/evening
     * @throws Exception from db , id doesn't exist , or already assign to shift on this date
     */
    public void addJobConstraint(int employeeID, String date, String shift) throws Exception {
        employeeFacade.addJobConstraint(employeeID, date, shift);
    }

    /**
     * update employee's name
     * @param employeeID - employee id
     * @param newName - new name
     * @throws Exception from db if id doesn't exist
     */
    public void updateEmployeeName(int employeeID, String newName) throws Exception {
        employeeFacade.updateEmployeeName(employeeID, newName);
    }
    /**
     * update employee's Bank Account
     * @param employeeID - employee id
     * @param newBankAccount - new name
     * @throws Exception from db if id doesn't exist
     */
    public void updateEmployeeBankAccount(int employeeID, String newBankAccount) throws Exception {
        employeeFacade.updateEmployeeBankAccount(employeeID, newBankAccount);
    }
    /**
     * update employee's Salary
     * @param employeeID - employee id
     * @param newSalary - new name
     * @throws Exception from db if id doesn't exist
     */
    public void updateEmployeeSalary(int employeeID, int newSalary) throws Exception {
        employeeFacade.updateEmployeeSalary(employeeID, newSalary);
    }
    /**
     * update employee's extra details
     * @param employeeID - employee id
     * @param employmentDetails - new name
     * @throws Exception from db if id doesn't exist
     */
    public void updateEmploymentDetails(int employeeID, String employmentDetails) throws Exception {
        employeeFacade.updateEmploymentDetails(employeeID, employmentDetails);
    }

    /**
     * Add certification to employee's job
     * @param empID - employeeID
     * @param jobTitle - job of employee
     * @param certName - certification name
     * @throws Exception from db if id doesn't exist or the employee doesn't have this job or the certification isn't legal.
     */
    public void addCertToEmployee(int empID, String jobTitle, String certName) throws Exception {
        employeeFacade.addCertToEmployee(empID, jobTitle, certName);
    }

    public List<String> getAllBranchesEmployeeWorksIn(int id) throws Exception {
        return employeeFacade.getAllBranchesEmployeeWorksIn(id);
    }

    //shift-------------------------------------------------------------------

    /**
     * get shift by her id
     * @param shiftID
     * @return shift with id shiftID
     * @throws Exception from db if id doesn't exist
     */
    public Shift getShift(int shiftID) throws Exception {
        return employeeFacade.getShift(shiftID);
    }

    public String getBranchOfShift(int shiftID) throws Exception {
        return employeeFacade.getShift(shiftID).getBranchAddress();
    }

    /**
     * gel all the shifts from db
     * @return all shifts saved in db
     * @throws Exception from db
     */
    public List<Shift> getAllShifts() throws Exception {
        return employeeFacade.getAllShifts();
    }

    /**
     * get all the employee's assign shifts
     * @param employeeID
     * @return list of shifts employee assign to\
     * @throws Exception from db if id doesn't exist
     */
    public List<Shift> getShiftsOfEmployee(int employeeID) throws Exception {
        return employeeFacade.getShiftsOfEmployee(employeeID);
    }

    /**
     * add shift to db
     * @param branch - of the new shift
     * @param date - of shift
     * @param shift_type - morning/evening
     * @param employeesToShift - list of employees assign to this shift
     * @throws Exception from db if shift already exist
     */
    public void addShift(String branch, String date, String shift_type, HashMap<Integer, String> employeesToShift) throws Exception {
        employeeFacade.addShift(branch, date, shift_type, employeesToShift);
    }

    /**
     * remove shift from db
     * @param shiftID - the shift id we want to remove from db
     * @throws Exception if shift doesn't exist ,
     */
    public void removeShift(int shiftID) throws Exception {
        employeeFacade.removeShift(shiftID);
    }
    /**
     * Add 1 employee to specific shift
     * @param shiftID
     * @param employeeID
     * @param job - the position of the employee in the specific shift
     * @throws Exception from db if id of employee or shift doesn't exist , or the employee doesn't work in this shift in this job , or the employee has constraint
     */
    public void addEmployeeToShift(int shiftID, int employeeID, String job) throws Exception {
        employeeFacade.addEmployeeToShift(shiftID, employeeID,job);
    }

    /**
     * remove 1 employee from specific shift
     * @param shiftID
     * @param employeeID
     * @throws Exception from db if id of employee or shift doesn't exist , or if after removel , the shift is illegal
     */
    public void removeEmployeeFromShift(int shiftID, int employeeID) throws Exception {
        employeeFacade.removeEmployeeFromShift(shiftID, employeeID);
    }

    /**
     * find shift , using date and branch
     * @param date
     * @param typeShift
     * @param branch
     * @return the searced shift
     * @throws Exception from db if shift doesn't exist
     */
    public Shift getShiftByDateTypeAndBranch(String date, String typeShift, String branch) throws Exception {
        return employeeFacade.getShiftByDateTypeAndBranch(date, typeShift, branch);
    }

    /**
     * Validate the job of employee
     * @param jobTitle
     * @param employeeID
     * @return true if the employee can work in the job  and false else
     * @throws SQLException if id doesn't exist or job isn't valid return false , true else
     */
    public boolean validEmployeeHasJob(String jobTitle, String employeeID)throws Exception {
        return employeeFacade.validEmployeeHasJob(jobTitle,employeeID);
    }

    /**
     * Validate if employees match the specification of employees in shift
     * @param employeesToShift
     * @throws Exception if ids doesn't exist or if the employees free to work on this date , or held illegal shift .
     */
    public void validEmployeesInShiftJob(HashMap<Integer, String> employeesToShift) throws Exception{
        employeeFacade.validEmployeesInShiftJob(employeesToShift);
    }

    /**
     * Get shift from last month of employee by his id
     * @param morning_evening
     * @param employeeID
     * @return list of shifts
     * @throws Exception if id doesn't exist
     */
    public List<Shift> getShiftLastMonth(String morning_evening , int employeeID) throws Exception{
        return employeeFacade.getShiftLastMonth(morning_evening ,employeeID);
    }

    /**
     *
     * @param id - employee id
     * @return list of id's Licences  of Driver
     * @throws Exception if id doesnt exist
     */
    public List<String> getDriverLicencesForDriver(int id) throws Exception {
        return employeeFacade.getDriverLicencesForDriver(id);
    }

    /**
     * Get all branches from db
     * @return list of branches
     * @throws Exception from db
     */
    public List<String> getAllBranchesByAddress() throws Exception {
        return employeeFacade.getAllBranchesByAddress();
    }

    /**
     * get address of HQ
     * @return address of HQ
     * @throws Exception from db
     */
    public String getAddressOfHQ() throws Exception {
        return employeeFacade.getAddressOfHQ();
    }

    /**
     * Get all employees who have the job 'driver' and are available in the given day
     * @param date
     * @return list of id's of drivers
     * @throws Exception from db
     */
    public List<Integer> getDriversToSpecificShift(String date)throws Exception  {
        return employeeFacade.getDriversToSpecificShift(date);
    }

    /**
     * Get all drivers available for shift held in date
     * @param date
     * @return list of drivers
     * @throws Exception from db
     */
    public List<Employee> getAvailableDrivers(String date) throws Exception {
        return employeeFacade.getAvailableDrivers(date);
    }

    public void deleteAllData() {
        employeeFacade.deleteAllData();
        deliveryFacade.deleteAllData();
    }

    /**
     * @param deliveryId deliveryId that the site document is bounded to
     * @param locationInAddressList the location address mentioned in the site document
     * @return the site document of the i-th site where i=locationInAddressList
     */
    public SiteDoc getDocumentForDeliveryAndlocation(int deliveryId, int locationInAddressList) throws Exception {
        return deliveryFacade.getDocumentForDeliveryAndlocation(deliveryId,locationInAddressList);
    }

    public List<String> pullMessages(String branchAddress,String job) throws Exception {
        return employeeFacade.pullMessages(branchAddress,job);
    }

    public List<String> pullMessagesForJob(String job) throws Exception {
        return employeeFacade.pullMessagesForJob(job);
    }

    // supplier and inventory
    public void supplyDemandOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndUnSuppliedAmount) {
        if(getSupplier(supplierBN).getShouldDeliver() && supplierFacade.getDemandOrder(supplierBN, orderId).getSupplyDate()!=null && !deliveryFacade.isOrderFinished(orderId))
            throw new IllegalArgumentException("Cant supply order because delivery hasn't arrived yet!");
        updateUnSuppliedAmount(supplierBN, orderId, itemIdAndUnSuppliedAmount);
        List<Pair<Integer, OrderProduct>> orderProducts = supplierFacade.supplyDemandOrder(supplierBN, orderId);
        String branchAddress = supplierFacade.getDemandOrder(supplierBN, orderId).getBranchAddress();
        reportsMaker.addPurchase(branchAddress, orderProducts);
    }

    public void orderShortage(String branchAddress) {
        List<DemandOrder> demandOrders =  supplierFacade.generateOrdersForShortage(branchAddress, reportsMaker.makeShortageOrder(branchAddress));
        for(DemandOrder demandOrder : demandOrders) {
            Supplier supplier = getSupplier(demandOrder.getSupplierBN());
            if (supplier.getShouldDeliver()) {
                Set<DayOfWeek> dates = supplier.getPossibleSupplyDays();
                List<Date> possibleSupplyDays = new ArrayList<>();
                for (DayOfWeek dayOfWeek : dates) {
                    Calendar cal = Calendar.getInstance(); // Today, now
                    cal.add(Calendar.DAY_OF_MONTH, (dayOfWeek.getValue() + 7 - cal.get(Calendar.DAY_OF_WEEK)) % 7);
                    possibleSupplyDays.add(new Date(cal.getTime().getTime()));
                }
                Date supplyDate = null;
                try {
                    supplyDate = createDelivery(possibleSupplyDays, demandOrder);
                    if(supplyDate!=null)
                        supplierFacade.setSupplyDateForDemandOrder(demandOrder.getSupplierBN(), demandOrder.getOrderId(), supplyDate);
                } catch (Exception ignored) {
                }
            }
        }
    }

    //supplier
    public Supplier getSupplier(int supplierBN) {
        return supplierFacade.getSupplier(supplierBN);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierFacade.getAllSuppliers();
    }

    public void addSupplier(String name, int businessNumber, int bankAccount, boolean shouldDeliver, String paymentMethod, Set<DayOfWeek> daysOfSupply, String address, String area) {
        supplierFacade.addSupplier(name, businessNumber, bankAccount, shouldDeliver, paymentMethod, daysOfSupply, address, area);
    }

    public void removeSupplier(int supplierBN) {
        supplierFacade.removeSupplier(supplierBN);
    }

    public BillOfQuantity getBillOfQuantity(int supplierBN) {
        return supplierFacade.getBillOfQuantity(supplierBN);
    }

    public void updateSupplierName(int supplierBN, String newName) {
        supplierFacade.updateSupplierName(supplierBN, newName);
    }

    public void updateSupplierBankAccount(int supplierBN, int newBankAccount) {
        supplierFacade.updateSupplierBankAccount(supplierBN, newBankAccount);
    }

    public void updateSupplierDelivery(int supplierBN, boolean newDeliveryMethod) {
        supplierFacade.updateSupplierDelivery(supplierBN, newDeliveryMethod);
    }

    public void updateSupplierPaymentMethod(int supplierBN, String newPaymentMethod) {
        supplierFacade.updateSupplierPaymentMethod(supplierBN, newPaymentMethod);
    }

    public void addContact(int supplierBN, String contactName, String phoneNumber, String email) {
        supplierFacade.addContact(supplierBN, contactName, phoneNumber, email);
    }

    public void removeContact(int supplierBN, int contactId) {
        supplierFacade.removeContact(supplierBN, contactId);
    }

    public void updateContactName(int supplierBN, int contactId, String newContactName) {
        supplierFacade.updateContactName(supplierBN, contactId, newContactName);
    }

    public void updateContactEmail(int supplierBN, int contactId, String newEmail) {
        supplierFacade.updateContactEmail(supplierBN, contactId, newEmail);
    }

    public void updateContactPhone(int supplierBN, int contactId, String newPhoneNumber) {
        supplierFacade.updateContactPhone(supplierBN, contactId, newPhoneNumber);
    }

    public double getItemPrice(String branchAddress, int supplierBN, int catalogNumber) {
        return supplierFacade.getItemPrice(branchAddress, supplierBN, catalogNumber);
    }

    public void addStorageItem(String branchAddress, int supplierBN, int catalogNumber, int supplierCatalogNum, double price, String name) {
        supplierFacade.addItem(branchAddress, supplierBN, catalogNumber, supplierCatalogNum, price, name);
    }

    public void removeItem(String branchAddress, int supplierBN, int catalogNumber) {
        supplierFacade.removeItem(branchAddress, supplierBN, catalogNumber);
    }

    public void updateItemPrice(String branchAddress, int supplierBN, int catalogNumber, double newPrice) {
        supplierFacade.updateItemPrice(branchAddress, supplierBN, catalogNumber, newPrice);
    }

    public Map<Integer, Double> getItemDiscounts(String branchAddress, int supplierBN, int catalogNumber) {
        return supplierFacade.getItemDiscounts(branchAddress, supplierBN, catalogNumber);
    }

    public void addItemDiscountAccordingToAmount(String branchAddress, int supplierBN, int catalogNumber, int amount, double discount) {
        supplierFacade.addItemDiscountAccordingToAmount(branchAddress, supplierBN, catalogNumber, amount, discount);
    }

    public void updateItemDiscountAccordingToAmount(String branchAddress, int supplierBN, int catalogNumber, int amount, double newDiscount) {
        supplierFacade.updateItemDiscountAccordingToAmount(branchAddress, supplierBN, catalogNumber, amount, newDiscount);
    }

    public void removeItemDiscountAccordingToAmount(String branchAddress, int supplierBN, int catalogNumber, int amount) {
        supplierFacade.removeItemDiscountAccordingToAmount(branchAddress, supplierBN, catalogNumber, amount);
    }

    public void updateSupplierCatalog(String branchAddress, int supplierBN, int catalogNumber, int newSupplierCatalog) {
        supplierFacade.updateSupplierCatalog(branchAddress, supplierBN, catalogNumber, newSupplierCatalog);
    }

    public String getItemName(String branchAddress, int supplierBN, int catalogNumber) {
        return supplierFacade.getItemName(branchAddress, supplierBN, catalogNumber);
    }

    public void updateItemName(String branchAddress, int supplierBN, int catalogNumber, String newName) {
        supplierFacade.updateItemName(branchAddress, supplierBN, catalogNumber, newName);
    }

    public List<Contact> getAllContacts(int supplierBN) {
        return supplierFacade.getAllContacts(supplierBN);
    }

    public void addDemandOrder(String branchAddress, int supplierBN, Date orderDate, Date supplyDate, HashMap<Integer, Integer> itemIdAndAmount) throws Exception {
        if(getSupplier(supplierBN).getShouldDeliver()) {
            DemandOrder demandOrder = supplierFacade.addDemandOrder(branchAddress, supplierBN, orderDate, null, itemIdAndAmount);
            if (demandOrder != null) {
                if(supplyDate !=null) {
                    List<Date> dateList = new ArrayList<>();
                    dateList.add(supplyDate);
                    if (createDelivery(dateList, demandOrder) != null)
                        supplierFacade.setSupplyDateForDemandOrder(supplierBN, demandOrder.getOrderId(), supplyDate);
                }
            } else
                throw new IllegalArgumentException("Cant create demand order!");
        }
        else {
            supplierFacade.addDemandOrder(branchAddress, supplierBN, orderDate, supplyDate, itemIdAndAmount);
        }
    }

    public void addFixedOrder(String branchAddress, int supplierBN, Date orderDate, Set<DayOfWeek> supplyDays, HashMap<Integer, Integer> itemIdAndAmount) {
        supplierFacade.addFixedOrder(branchAddress, supplierBN, orderDate, supplyDays, itemIdAndAmount);
    }

    public void addFixedOrders(String branchAddress, Date orderDate, Set<DayOfWeek> supplyDays, HashMap<Integer, Integer> itemIdAndAmount) {
        supplierFacade.addFixedOrders(branchAddress, orderDate, supplyDays, itemIdAndAmount);
    }

    public void addDemandOrders(String branchAddress, Date orderDate, Date supplyDate, HashMap<Integer, Integer> itemIdAndAmount) throws Exception {
        Map<Supplier, HashMap<Integer, Integer>> supplierToItems = supplierFacade.itemsToSuppliersForDemandOrder(branchAddress, itemIdAndAmount);
        for(Supplier supplier : supplierToItems.keySet())
            addDemandOrder(branchAddress, supplier.getBusinessNumber(), orderDate, supplyDate, supplierToItems.get(supplier));    }

    public void addProductsToFixedOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) {
        supplierFacade.addProductsToFixedOrder(supplierBN, orderId, itemIdAndAmount);
    }

    public void removeProductsFromFixedOrder(int supplierBN, int orderId, List<Integer> productsId) {
        supplierFacade.removeProductsFromFixedOrder(supplierBN, orderId, productsId);
    }

    public void updateOrderProductsOfFixedOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) {
        supplierFacade.updateOrderProductsOfFixedOrder(supplierBN, orderId, itemIdAndAmount);
    }

    public void addProductsToDemandOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) throws Exception {
        supplierFacade.addProductsToDemandOrder(supplierBN, orderId, itemIdAndAmount);
        if(getSupplier(supplierBN).getShouldDeliver() && supplierFacade.getDemandOrder(supplierBN, orderId).getSupplyDate()!=null)
            changeOrderInDelivery(supplierFacade.getDemandOrder(supplierBN, orderId));
    }

    public void removeProductsFromDemandOrder(int supplierBN, int orderId, List<Integer> productsId) throws Exception {
        supplierFacade.removeProductsFromDemandOrder(supplierBN, orderId, productsId);
        if(getSupplier(supplierBN).getShouldDeliver() && supplierFacade.getDemandOrder(supplierBN, orderId).getSupplyDate()!=null)
            changeOrderInDelivery(supplierFacade.getDemandOrder(supplierBN, orderId));
    }

    public void updateOrderProductsOfDemandOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) throws Exception {
        supplierFacade.updateOrderProductsOfDemandOrder(supplierBN, orderId, itemIdAndAmount);
        if(getSupplier(supplierBN).getShouldDeliver() && supplierFacade.getDemandOrder(supplierBN, orderId).getSupplyDate()!=null)
            changeOrderInDelivery(supplierFacade.getDemandOrder(supplierBN, orderId));
    }

    public void updateUnSuppliedAmount(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndUnSuppliedAmount) {
        supplierFacade.updateUnSuppliedAmount(supplierBN, orderId, itemIdAndUnSuppliedAmount);
    }

    public void removeUnSuppliedDemandOrder(int supplierBN, int orderId) throws Exception {
        if(getSupplier(supplierBN).getShouldDeliver() && supplierFacade.getDemandOrder(supplierBN, orderId).getSupplyDate()!=null)
            deleteOrderInDelivery(orderId);
        supplierFacade.removeUnSuppliedDemandOrder(supplierBN, orderId);
    }

    public List<DemandOrder> getAllSupplierDemandOrders(int supplierBN) {
        return supplierFacade.getAllSupplierDemandOrders(supplierBN);
    }

    public List<FixedOrder> getAllSupplierFixedOrders(int supplierBN) {
        return supplierFacade.getAllSupplierFixedOrders(supplierBN);
    }

    public List<Order> getAllSupplierOrders(int supplierBN) {
        return supplierFacade.getAllSupplierOrders(supplierBN);
    }

    public void unActiveFixedOrder(int supplierBN, int orderId, Date date) { supplierFacade.unActiveFixedOrder(supplierBN, orderId, date); }

    public List<FixedOrder> getAllFixedOrders() {
        return supplierFacade.getAllFixedOrders();
    }

    public List<DemandOrder> getAllDemandOrders() {
        return supplierFacade.getAllDemandOrders();
    }

    public void sendPDFDOrder(int supplierBN, int orderId, String email) throws IOException { supplierFacade.sendPDFDOrder(supplierBN, orderId, email); }

    public void addSupplyDayToFixedOrder(int supplierBN, int orderId, DayOfWeek day) { supplierFacade.addSupplyDayToFixedOrder(supplierBN, orderId, day); }

    public void removeSupplyDayToFixedOrder(int supplierBN, int orderId, DayOfWeek day) { supplierFacade.removeSupplyDayToFixedOrder(supplierBN, orderId, day); }

    public List<FixedOrder> dailyFixedOrders() {
        return supplierFacade.dailyFixedOrders();
    }

    public void createNextWeekFixedOrders(String branchAddress) throws Exception {
        List<DemandOrder> demandOrders = supplierFacade.createNextWeekFixedOrders(branchAddress);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        List<Date> dateList = new ArrayList<>();
        dateList.add(new Date(calendar.getTime().getTime()));
        for(DemandOrder demandOrder : demandOrders)
            if(getSupplier(demandOrder.getSupplierBN()).getShouldDeliver())
                if(createDelivery(dateList, demandOrder) !=null)
                    demandOrder.setSupplyDate(dateList.get(0));
    }

    //----------------------------------------------------------------------------------------
    //Inventory
    public List<InventoryItem> makeInventoryReport(String branchAddress){
        return reportsMaker.makeInventoryReport(branchAddress);
    }

    public List<InventoryItem> makeInventoryReportByCategory(String branchAddress, String categoryID){
        return reportsMaker.makeInventoryReportByCategory(branchAddress, categoryID);
    }

    public List<InShortageItem> makeShortageReport(String branchAddress){
        return reportsMaker.makeShortageReport(branchAddress);
    }

    public List<DamagedItem> makeDamageReport(String branchAddress) {
        return reportsMaker.makeDamageReport(branchAddress);
    }

    public List<DamagedItem> makeDamageReportByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen){
        return reportsMaker.makeDamageReportByDate(branchAddress, sinceWhen, untilWhen);
    }

    public List<DamagedItem> makeDamageReportByItemId(String branchAddress, int id){
        return reportsMaker.makeDamageReportByItemId(branchAddress, id);
    }

    public List<DamagedItem> makeOnlyExpiredReport(String branchAddress) {
        return reportsMaker.makeOnlyExpiredReport(branchAddress);
    }

    public List<DamagedItem> makeOnlyDamageReport(String branchAddress) {
        return reportsMaker.makeOnlyDamageReport(branchAddress);
    }

    public List<DamagedItem> makeOnlyDamageReportByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) {
        return reportsMaker.makeOnlyDamageReportByDate(branchAddress, sinceWhen, untilWhen);
    }

    public List<DamagedItem> makeOnlyExpiredReportByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen) {
        return reportsMaker.makeOnlyExpiredReportByDate(branchAddress, sinceWhen, untilWhen);
    }

    public List<DamagedItem> makeOnlyDamageReportByItemId(String branchAddress, int itemID) {
        return reportsMaker.makeOnlyDamageReportByItemId(branchAddress, itemID);
    }

    public List<DamagedItem> makeOnlyExpiredReportByItemId(String branchAddress, int itemID) {
        return reportsMaker.makeOnlyExpiredReportByItemId(branchAddress, itemID);
    }

    public List<PurchasedItem> makePurchaseReport(String branchAddress) {
        return reportsMaker.makePurchaseReport(branchAddress);
    }

    public List<PurchasedItem> makePurchaseReportByDate(String branchAddress, LocalDate sinceWhen, LocalDate untilWhen){
        return reportsMaker.makePurchaseReportByDate(branchAddress, sinceWhen, untilWhen);
    }

    public List<PurchasedItem> makePurchaseReportByItemId(String branchAddress, int id){
        return reportsMaker.makePurchaseReportByItemId(branchAddress, id);
    }

    public List<PurchasedItem> makePurchaseReportByBusinessNumber(String branchAddress, int supplierBusiness){
        return reportsMaker.makePurchaseReportByBusinessNumber(branchAddress, supplierBusiness);
    }

    public void addPurchase(String branchAddress, int desiredItemID, int orderID, int supplierBisNumber, int amount, double checkedPrice, double checkedDiscount){
        reportsMaker.addPurchase(branchAddress, desiredItemID, orderID, supplierBisNumber, amount, checkedPrice, checkedDiscount);
    }

    public void addDamaged(String branchAddress, int desiredItemID, int amount, DamageReason reason, int back0Front1){
        reportsMaker.addDamaged(branchAddress, desiredItemID, amount, reason, back0Front1);
    }

    //----------------------------------------------------------------------------------------
    //Item Controller
    public CategoryTree getCategoryTree(String branchAddress) { return itemController.getCategoryTree(branchAddress); }

    public List<Item> getAllItemsByCategory(String branchAddress, String categoryID){
        return itemController.getAllItemsByCategory(branchAddress, categoryID);
    }

    public void giveDiscountToCategory(String branchAddress, String categoryID, Discount discount){
        itemController.giveDiscountToCategory(branchAddress, categoryID, discount);
    }

    public void giveDiscountToItem(String branchAddress, int itemId, Discount discount){
        itemController.giveDiscountToItem(branchAddress, itemId, discount);
    }

    public Item getItem(String branchAddress, int itemID){
        return itemController.getItem(branchAddress, itemID);
    }

    public Item addStorageItem(String branchAddress, String name, String categoryID, double currPrice, int minimalQuantity, int fullQuantity, String manufacture, List<Integer> backShelves, List<Integer> frontShelves){
        return itemController.addItem(branchAddress, name, categoryID, currPrice, minimalQuantity, fullQuantity,
                manufacture, backShelves, frontShelves);
    }

    public void removeQuantityFromItem(String branchAddress, int itemId, int quantity, boolean fromBackRoom){
        itemController.removeQuantityFromItem(branchAddress, itemId, quantity, fromBackRoom);
    }

    public void addQuantityToItem(String branchAddress, int itemID, int quantity, boolean fromBackRoom){
        itemController.addQuantityToItem(branchAddress, itemID, quantity, fromBackRoom);
    }

    public void setMinimalQuantity(String branchAddress, int itemID, int checkedQuantity) {
        itemController.setMinimalQuantity(branchAddress, itemID, checkedQuantity);
    }

    public void setFullQuantity(String branchAddress, int itemID, int checkedQuantity) {
        itemController.setFullQuantity(branchAddress, itemID, checkedQuantity);
    }
    //-----------------------------------------------------------------
    //Shelf
    public List<Integer> getBackRoomShelves(String branchAddress) {
        return shelfHandler.getBackRoomShelves(branchAddress);
    }

    public List<Integer> getFrontRoomShelves(String branchAddress) {
        return shelfHandler.getFrontRoomShelves(branchAddress);
    }

    public void addShelf(String branchAddress, boolean shelfInBackRoom) {
        shelfHandler.addShelf(branchAddress, shelfInBackRoom);
    }


    public void addbranch(String addressBranch, String shippingArea, int managerID) throws Exception {
        employeeFacade.addbranch(addressBranch, shippingArea,managerID);
    }

    public void removebranch(String addressBranch)throws Exception  {
        employeeFacade.removebranch(addressBranch);

    }
}
