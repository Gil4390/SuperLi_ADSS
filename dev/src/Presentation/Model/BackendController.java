package Presentation.Model;

import BusinessLayer.EmployeeModule.Objects.Branch;
import Presentation.Model.DeliveryModuleModel.*;
import Presentation.Model.EmployeeModuleModel.EmployeeModel;
import Presentation.Model.EmployeeModuleModel.ShiftModel;
import Presentation.Model.InventoryModel.CategoryModel;
import Presentation.Model.InventoryModel.DamagedItemModel;
import Presentation.Model.InventoryModel.InventoryItemModel;
import Presentation.Model.InventoryModel.ShortageItemModel;
import ServiceLayer.Objects.DeliveryObjects.FDelivery;
import ServiceLayer.Objects.DeliveryObjects.FSiteDoc;
import ServiceLayer.Objects.DeliveryObjects.FStockShortness;
import ServiceLayer.Objects.DeliveryObjects.FTruck;
import ServiceLayer.Objects.EmployeeObjects.FEmployee;
import ServiceLayer.Objects.EmployeeObjects.FShift;
import ServiceLayer.Objects.InventoryObjects.*;
import ServiceLayer.Objects.SupplierObjects.SBillOfQuantities;
import ServiceLayer.Objects.SupplierObjects.SSupplier;
import ServiceLayer.Responses.Response;
import ServiceLayer.Responses.ResponseT;
import BusinessLayer.SupplierBusiness.Contact;
import BusinessLayer.SupplierBusiness.DemandOrder;
import BusinessLayer.SupplierBusiness.FixedOrder;
import Presentation.Model.InventoryModel.*;
import Presentation.Model.SupplierModel.*;
import ServiceLayer.ApplicationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.sql.Date;
import java.time.DayOfWeek;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BackendController {
    private final DataLoader dataLoader;
    private final ApplicationService applicationService;

    public EmployeeModel getLoggedInUser() {
        return loggedInUser;
    }

    private EmployeeModel loggedInUser;

    private boolean loadDataForTest = false;

    private BackendController() {
        this.applicationService = new ApplicationService();
        this.dataLoader = new DataLoader(applicationService);
    }

    public void logout() {
        loggedInUser = null;
    }

    public void setBranchManagerLoggedIn() {
        this.loggedInUser.setBranchManager(true);
    }

    public String getBranchOfManager(int ManagerID) {
        ResponseT<String> response = applicationService.getBranchOfManager(ManagerID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else {
            return response.getValue();
        }
    }

    private static class BackendControllerHolder{
        private static BackendController instance = new BackendController();
    }

    public static void clearBackendControllerForTests() {
        BackendControllerHolder.instance = new BackendController();
    }

    public static BackendController getInstance() {
        return BackendController.BackendControllerHolder.instance;
    }

    /////////////////EMPLOYEE/////////////////
    public List<EmployeeModel> getAllEmployees(){
        ResponseT<List<FEmployee>> response = applicationService.getAllEmployees();
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FEmployee> fEmployees = response.getValue();
            List<EmployeeModel> employeeModels = new Vector<>();
            for(FEmployee fEmployee : fEmployees)
                employeeModels.add(new EmployeeModel(fEmployee));
            return employeeModels;
        }
    }
    public EmployeeModel getEmployeeByID(int employeeID) {
        ResponseT<FEmployee> response = applicationService.getEmployee(employeeID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else {
            FEmployee fEmployee = response.getValue();
            return new EmployeeModel(fEmployee);
        }
    }
    public List<EmployeeModel> getAvailableEmployees(String date, String shiftType) {
        ResponseT<List<FEmployee>> response = applicationService.getAvailableEmployeesForShift(date, shiftType);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FEmployee> fEmployees = response.getValue();
            List<EmployeeModel> employeeModels = new Vector<>();
            for(FEmployee fEmployee : fEmployees)
                employeeModels.add(new EmployeeModel(fEmployee));
            return employeeModels;
        }
    }
    public void addNewEmployee(String name, int ID, String bank_Account, int salary, String employment_start_date, String jobTitle, String employmentDetails, String number) {
        Response response = applicationService.addEmployee(name, ID, bank_Account, salary, employment_start_date, jobTitle, employmentDetails,number);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }

    }

    public void addNewEmployee(String name, int ID, String bank_Account, int salary, String employment_start_date, String jobTitle, String employmentDetails, Vector<String> certifications, String number) {
        Response response = applicationService.addEmployee(name, ID, bank_Account, salary, employment_start_date, jobTitle, employmentDetails, certifications,number);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
    }

    public void deleteEmployee(int employeeID) {
        Response response = applicationService.removeEmployee(employeeID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            System.out.println("deleted employee" + employeeID);
        }
    }
    public void addJobTitleToEmployee(int employeeID, String newJob) {
        Response response = applicationService.addJobToEmployee(employeeID, newJob);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            System.out.println("added new job to employee");
        }
    }

    public void addJobCertToEmployee(int empID, String jobTitle, String certName) {
        Response response = applicationService.addCertToEmployee(empID, jobTitle, certName);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else{
//            System.out.println("added new job Certification to employee");
//        }
    }
    public void removeJobTitleFromEmployee(int employeeID, String jobTitle) {
        Response response = applicationService.removeJobFromEmployee(employeeID, jobTitle);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else{
//            System.out.println("removed job from employee");
//        }
    }
    public void addJobConstraint(int id, String date, String shift_type) {
        Response response = applicationService.addJobConstraint(id, date, shift_type);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else{
//            System.out.println("added new job constraint to employee");
//        }
    }
    public void updateEmployee(int employeeID, String newInfo , String updatedInfo) {
        switch (updatedInfo) {
            case "name": updateEmployeeName(employeeID, newInfo);
                return;
            case "bank account": updateEmployeeBankAccount(employeeID, newInfo);
                return;
            case "salary": updateEmployeeSalary(employeeID, Integer.parseInt(newInfo));
                return;
            default: updateEmploymentDetails(employeeID, newInfo);
                return;
        }

    }
    public void updateEmployeeName(int employeeID, String newName) {
        Response response = applicationService.updateEmployeeName(employeeID, newName);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else{
//            System.out.println("updated employee name");
//        }
    }
    public void updateEmployeeBankAccount(int employeeID, String newBankAccount) {
        Response response = applicationService.updateEmployeeBankAccount(employeeID, newBankAccount);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else{
//            System.out.println("updated employee Bank Account");
//        }
    }
    public void updateEmployeeSalary(int employeeID, int newSalary) {
        Response response = applicationService.updateEmployeeSalary(employeeID, newSalary);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//            else{
//                System.out.println("updated employee Salary");
//            }
    }
    public void updateEmploymentDetails(int employeeID, String employmentDetails) {
        Response response = applicationService.updateEmploymentDetails(employeeID, employmentDetails);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else{
//            System.out.println("updated employee employmentDetails");
//        }
    }

    public List<Integer> getDriversToSpecificShift(String date) throws Exception
    {
        ResponseT<List<Integer>> response =applicationService.getDriversToSpecificShift(date);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else {
            return response.getValue();
        }
    }


    /////////////////SHIFT/////////////////
    public ShiftModel getShiftByID(int shiftID) {
        ResponseT<FShift> response = applicationService.getShift(shiftID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else {
            FShift fShift = response.getValue();
            return new ShiftModel(fShift);
        }
    }
    public List<ShiftModel> getAllShifts(){
        ResponseT<List<FShift>> response = applicationService.getAllShifts();
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FShift> fShifts = response.getValue();
            List<ShiftModel> shiftModels = new Vector<>();
            for(FShift fShift : fShifts)
                shiftModels.add(new ShiftModel(fShift));
            return shiftModels;
        }
    }
    //
    public List<ShiftModel> getEmployeeShifts(int employeeID) {
        ResponseT<List<FShift>> response = applicationService.getShiftsOfEmployee(employeeID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FShift> fShifts = response.getValue();
            List<ShiftModel> shiftModels = new Vector<>();
            for(FShift fShift : fShifts)
                shiftModels.add(new ShiftModel(fShift));
            return shiftModels;
        }
    }
    public void assignNewShift(String branch,String shiftDate, String shiftTime, HashMap<Integer, String> employeesToShift) {
        Response response = applicationService.addShift(branch,shiftDate,shiftTime,employeesToShift);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else{
//            System.out.println("shift added");
//        }
    }
    public void deleteShift(int shiftID) {
        Response response = applicationService.removeShift(shiftID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else {
//            System.out.println("shift deleted");
//        }
    }
    public LocalDate validDate(String date) {
        ResponseT<LocalDate> response = new ResponseT<>(LocalDate.parse(date));
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            return response.getValue();
        }
    }
    public boolean validEmployee(String jobTitle, String employeeID) {
        ResponseT<Boolean> response = applicationService.validEmployeeHasJob(jobTitle,employeeID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else return true;
    }
    public ShiftModel getShiftByDateTypeAndBranch(String date, String typeShift, String branch) {
        ResponseT<FShift> response = applicationService.getShiftByDateTypeAndBranch(date,typeShift, branch);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            return new ShiftModel(response.getValue());
        }
    }
    public boolean isEmployeeInShift(ShiftModel shift, int employeeID) {
        ResponseT<FShift> response = (applicationService.getShift(shift.getShiftID()));
        for (int e: response.getValue().getEmployees().keySet())
        {
            if (e==employeeID)
                return true;
        }
        return false;
    }
    public void replaceEmployees(ShiftModel sm, int oldID, String newID , String job) {
        Response response = applicationService.getShift(sm.getShiftID());
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            response = applicationService.removeEmployeeFromShift(sm.getShiftID(),oldID);
            if (response.errorOccurred()){
                throw new IllegalArgumentException(response.getErrorMessage());
            }
            else{
                response = applicationService.addEmployeeToShift(sm.getShiftID(),Integer.parseInt(newID),job);
                if (response.errorOccurred()){
                    throw new IllegalArgumentException(response.getErrorMessage());
                }
            }
        }
    }
    public void removeEmployeeFromShift(int shiftID, int employeeID) {
        Response response = applicationService.getShift(shiftID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            response = applicationService.removeEmployeeFromShift(shiftID,employeeID);
            if (response.errorOccurred()){
                throw new IllegalArgumentException(response.getErrorMessage());
            }
        }
    }
    public void addEmployeeToShift(int sID, int eID , String job) {
        Response response = applicationService.addEmployeeToShift(sID,eID , job);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
    }

    public void validEmployeesInShiftJob(HashMap<Integer, String> employeesToShift) {
        Response response = applicationService.validEmployeesInShiftJob(employeesToShift);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
//        else {
//            System.out.println("employees jobs validated");
//        }
    }
    public void getShiftLastMonth(String morning_evening , int employeeID) {
        ResponseT<List<FShift>> response = applicationService.getShiftLastMonth(morning_evening,employeeID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else {
            for(FShift s : response.getValue())
                System.out.println(s.toString());
        }
    }






    /////////////////DELIVERY/////////////////
    public List<TruckModel> getAllTrucksAvailable(String date) {
        ResponseT<List<FTruck>> response = applicationService.getAllTrucksAvailable(date);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<FTruck> ftrucks = response.getValue();
        List<TruckModel> truckModels = new LinkedList<>();
        for (FTruck fTruck : ftrucks)
            truckModels.add(new TruckModel(fTruck));
        return truckModels;

    }


    public List<EmployeeModel> getDriversRelevant(int maxWeight, String date) {
        ResponseT<List<FEmployee>> response = applicationService.getDriversRelevant(maxWeight, date);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FEmployee> fEmployees = response.getValue();
            List<EmployeeModel> EmployeeModels = new LinkedList<>();
            for(FEmployee fEmployee : fEmployees)
                EmployeeModels.add(new EmployeeModel(fEmployee));
            return EmployeeModels;
        }
    }

    public List<StockShortnessModel> getAllRelevantStockShortnesses() {
        ResponseT<List<FStockShortness>> response = applicationService.getAllRelevantStockShortnesses();
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FStockShortness> fStockShortnesses = response.getValue();
            List<StockShortnessModel> stockShortnessModelList = new LinkedList<>();
            for(FStockShortness fStockShortness : fStockShortnesses)
                stockShortnessModelList.add(new StockShortnessModel(fStockShortness));
            return stockShortnessModelList;
        }
    }

    public DeliveryModel CreateDelivery(String date, String time, TruckModel truckM, EmployeeModel EmployeeM, List<StockShortnessModel> stockShortnessModels, boolean checkShippingArea) throws ParseException {
        List<Integer> stockShortnesses = new LinkedList<>();
        for (StockShortnessModel stockShortnessModel: stockShortnessModels)
            stockShortnesses.add(stockShortnessModel.getId());
        ResponseT<FDelivery> response = applicationService.createDelivery(date,time, truckM.getLicenseNumber(), EmployeeM.getID(), stockShortnesses, checkShippingArea);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            FDelivery fDelivery = response.getValue();
            return new DeliveryModel(fDelivery);
        }
    }

    public List<DeliveryModel> getAllDeliveries() throws ParseException {
        ResponseT<List<FDelivery>> response = applicationService.getAllDeliveries();
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FDelivery> fDeliveries = response.getValue();
            List<DeliveryModel> deliveryModels = new LinkedList<>();
            for(FDelivery fDelivery : fDeliveries)
                deliveryModels.add(new DeliveryModel(fDelivery));
            return deliveryModels;
        }
    }

    public TruckModel getTruckByLicense(String truckLicense) {
        ResponseT<FTruck> response = applicationService.getTruckByLicense(truckLicense);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            FTruck fTruck = response.getValue();
            return new TruckModel(fTruck);
        }
    }

//    public DriverModel getDriverById(int driverId) {
//        ResponseT<FEmployee> response = applicationFacade.getDriverById(driverId);
//        if (response.errorOccurred()){
//            throw new IllegalArgumentException(response.getErrorMessage());
//        }
//        else{
//            FDriver fDriver = response.getValue();
//            return new DriverModel(fDriver, this);
//        }
//    }

    public List<SiteDocumentModel> getAllDocumentsForDelivery(int deliveryId) {
        ResponseT<List<FSiteDoc>> response = applicationService.getAllDocumentsForDelivery(deliveryId);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FSiteDoc> fSiteDocs = response.getValue();
            List<SiteDocumentModel> siteDocumentModels = new LinkedList<>();
            for(FSiteDoc fSiteDoc : fSiteDocs)
                siteDocumentModels.add(new SiteDocumentModel(fSiteDoc));
            return siteDocumentModels;
        }
    }

    public Integer getLatestAddress(int deliveryId) {
        ResponseT<Integer> response = applicationService.getLatestAddress(deliveryId);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            return response.getValue();
        }
    }

    public List<SiteDocumentModel> getDocumentForDeliveryAndSite(int deliveryId, String siteAddress) {
        ResponseT<List<FSiteDoc>> response = applicationService.getDocumentForDeliveryAndSite(deliveryId, siteAddress);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FSiteDoc> fSiteDocs = response.getValue();
            List<SiteDocumentModel> siteDocumentModelList = new LinkedList<>();
            for (FSiteDoc fSiteDoc: fSiteDocs) {
                siteDocumentModelList.add(new SiteDocumentModel(fSiteDoc));
            }
            return siteDocumentModelList;
        }
    }

    public List<String> getListOfAddressesForDelivery(int deliveryId) {
        ResponseT<List<String>> response = applicationService.getListOfAddressesForDelivery(deliveryId);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            return response.getValue();
        }
    }

    public void weightTruck(SiteDocumentModel siteDocumentModel, int weight) {
        Response response = applicationService.weightTruck(siteDocumentModel.getDocumentId(), weight);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
    }

    public void arrivedToSite(SiteDocumentModel siteDocumentModel) {
        Response response = applicationService.arrivedToSite(siteDocumentModel.getDocumentId());
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
    }

    public boolean isDeliveryFinished(DeliveryModel deliveryModel) {
        ResponseT<Boolean> response = applicationService.isDeliveryFinished(deliveryModel.getId());
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        return response.getValue();
    }

    public void changeTruck(int documentId, TruckModel truckM) {
        Response response = applicationService.changeTruck(documentId, truckM.getLicenseNumber());
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
    }

    public void changeSites(int DocumentId, List<StockShortnessModel> stockShortnessModelsAdd, List<StockShortnessModel> stockShortnessModelsDrop, boolean checkShippingArea) {
        List<Integer> addStock = new LinkedList<>();
        for(StockShortnessModel stockShortnessModel: stockShortnessModelsAdd)
            addStock.add(stockShortnessModel.getId());
        List<Integer> dropStock = new LinkedList<>();
        for(StockShortnessModel stockShortnessModel: stockShortnessModelsDrop)
            dropStock.add(stockShortnessModel.getId());
        Response response = applicationService.changeSites(DocumentId, dropStock, addStock, checkShippingArea);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
    }

    public List<StockShortnessModel> getStockShortnessOfDelivery(DeliveryModel deliveryModel) {
        ResponseT<List<FStockShortness>> response = applicationService.getStockShortnessOfDelivery(deliveryModel.getId());
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FStockShortness> fStockShortnesses = response.getValue();
            List<StockShortnessModel> stockShortnessModelList = new LinkedList<>();
            for(FStockShortness fStockShortness : fStockShortnesses)
                stockShortnessModelList.add(new StockShortnessModel(fStockShortness));
            return stockShortnessModelList;
        }
    }

    public List<TruckModel> getAllRelevantTrucks(String dateString, int siteDocId) {
        ResponseT<List<FTruck>> response = applicationService.getAllRelevantTrucks(dateString, siteDocId);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FTruck> fTrucks = response.getValue();
            List<TruckModel> truckModels = new LinkedList<>();
            for(FTruck fTruck : fTrucks)
                truckModels.add(new TruckModel(fTruck));
            return truckModels;
        }
    }

    public List<String> getDriverLicencesForDriver(int id) {
        ResponseT<List<String>> response = applicationService.getDriverLicencesForDriver(id);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else return response.getValue();
    }

    public List<String> getAllBranchesByAddress() {
        ResponseT<List<String>> response = applicationService.getAllBranchesByAddress();
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else return response.getValue();
    }
    public List<String> getAllBranchesEmployeeWorksIn(int ID) {
        ResponseT<List<String>> response = applicationService.getAllBranchesEmployeeWorksIn(ID);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else return response.getValue();
    }

    public String getAddressOfHQ() {
        ResponseT<Branch> response = applicationService.getAddressOfHQ();
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else return response.getValue().getBranchAddress();
    }

    public List<EmployeeModel> getAvailableDrivers(String date) {
        ResponseT<List<FEmployee>> response = applicationService.getAvailableDrivers(date);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            List<FEmployee> fEmployees = response.getValue();
            List<EmployeeModel> employeeModels = new Vector<>();
            for(FEmployee fEmployee : fEmployees)
                employeeModels.add(new EmployeeModel(fEmployee));
            return employeeModels;
        }
    }

    public boolean isDeliveryManager(String input) {
        try {
            Response response = applicationService.getEmployee(Integer.parseInt(input));
            if (response.errorOccurred()){
                return false;
            }
            else {
                return applicationService.validEmployeeHasJob("delivery manager",input).getValue();
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public SiteDocumentModel getDocumentForDeliveryAndlocation(int deliveryId, int LocationInAddressList) {
        ResponseT<FSiteDoc> response = applicationService.getDocumentForDeliveryAndlocation(deliveryId, LocationInAddressList);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else{
            FSiteDoc fSiteDoc = response.getValue();
            return new SiteDocumentModel(fSiteDoc);
        }
    }

    public boolean findJobEmployee(String input,String actualJob) {
        try {
            ResponseT<FEmployee> response = applicationService.getEmployee(Integer.parseInt(input));
            if (response.errorOccurred()){
                return false;
            }
            else {
                if (applicationService.validEmployeeHasJob(actualJob,input).getValue())
                {
                    loggedInUser = new EmployeeModel(response.getValue());
                    if (isInShiftEmployee(Integer.parseInt(input)))
                    {
                        loggedInUser.setInShift(true);
                    }

                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean isInShiftEmployee(int input)
    {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        int hour = Integer.parseInt(formatter.format(date).split(" ")[2].split(":")[0]);
        for (ShiftModel shift : getAllShifts())
        {
            if (Objects.equals(shift.getDate(), LocalDate.now()) && shift.getEmployees().containsKey(input) )
            {
                if ((shift.getShiftType().equals("morning") && hour > 6 && hour < 16) || (shift.getShiftType().equals("evening") && (hour < 6 || hour > 16)))
                {
                    loggedInUser.setBranch(BackendController.getInstance().getBranchOfShift(shift.getShiftID()));
                    return true;
                }
            }
        }
        return false;
    }

    public void enterBranch(String branchAddress)
    {
        loggedInUser.setBranch(branchAddress);
    }

    public List<String> pullMessages(String branchAddress , String job) {
        ResponseT<List<String>> response = applicationService.pullMessages(branchAddress,job);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else return response.getValue();
    }

    public List<String> pullMessagesForJob(String job) {
        ResponseT<List<String>> response = applicationService.pullMessagesForJob(job);
        if (response.errorOccurred()){
            throw new IllegalArgumentException(response.getErrorMessage());
        }
        else return response.getValue();
    }


    ////////////////////INVENTORY//////////////////////////
    public void removeCategory(String categoryID) {
        Response response = applicationService.removeCategory(loggedInUser.getBranch(), categoryID);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void orderShortage() {
        Response response = applicationService.orderShortage(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void buyItem(ItemModel itemModel, int amount) { //that
        Response response = applicationService.removeQuantityFromItem(loggedInUser.getBranch(), itemModel.getItemID(),amount,false);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void addDiscount(ItemModel item, String disName, double discountFare, LocalDate fromDate, LocalDate toDate) {
        Response response = applicationService.addDiscount(loggedInUser.getBranch(), item.getItemID(),disName,fromDate,toDate,discountFare);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public List<Integer> getAllItemFrontShelves(ItemModel itemModel) {
        ResponseT<List<Integer>> response = applicationService.getAllFrontShelvesByItem(loggedInUser.getBranch(), itemModel.getItemID());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        return response.getValue();
    }

    public List<Integer> getAllItemBackShelves( ItemModel itemModel) {
        ResponseT<List<Integer>> response = applicationService.getAllBackShelvesByItem(loggedInUser.getBranch(), itemModel.getItemID());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        return response.getValue();
    }

    public void moveItemLocation(int itemID, boolean fromBackRoom, int checkedAmount) {
        Response response = applicationService.moveItemsBetweenRooms(loggedInUser.getBranch(), itemID,fromBackRoom, checkedAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void removeQuantityFromItem(int itemID, boolean inBackRoom, int checkedAmount) {
        Response response = applicationService.removeQuantityFromItem(loggedInUser.getBranch(), itemID, checkedAmount, inBackRoom);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void addQuantityToItem(int itemID, boolean inBackRoom, int checkedAmount2) {
        Response response = applicationService.addQuantityToItem(loggedInUser.getBranch(), itemID, checkedAmount2, inBackRoom);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void removeDiscount(ItemModel itemModel, LocalDate fromDate) {
        try {
            applicationService.removeDiscount(loggedInUser.getBranch(), itemModel.getItemID(), fromDate);
        }
        catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void changeItemPrice(int itemID, int checkedAmount) {
        try {
            applicationService.changeItemPrice(loggedInUser.getBranch(), itemID, checkedAmount);
        }
        catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void changeMinimalQuantity(int itemID, int checkedQuantity) {
        applicationService.changeMinimalQuantity(loggedInUser.getBranch(), itemID, checkedQuantity);
    }

    public void changeFullQuantity(int itemID, int checkedQuantity) {
        applicationService.changeFullQuantity(loggedInUser.getBranch(), itemID, checkedQuantity);
    }

    public void addDamagedRecord(int desiredItemID, int amount, PDamageReason reason, int back0Front1) {
        Response response = applicationService.addDamagedRecord(loggedInUser.getBranch(), desiredItemID,amount, FDamageReason.valueOf(reason.toString()),back0Front1);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public List<Integer> getAllFrontShelves() {
        ResponseT<List<Integer>> response = applicationService.getAllFrontShelves(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        return response.getValue();
    }
    public List<Integer> getAllBackShelves() {
        ResponseT<List<Integer>> response = applicationService.getAllBackShelves(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        return response.getValue();
    }

    public CategoryModel getCategoryFather(CategoryModel categoryModel) {
        ResponseT<FCategory> response = applicationService.getCategoryFather(loggedInUser.getBranch(), categoryModel.getCatFatherID());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        return new CategoryModel(response.getValue());
    }

    public ItemModel addItem(String name, String catID, double price, int minimalQuantity, int fullQuantity, String manufacture, List<Integer> backShelves, List<Integer> frontShelves){
        ResponseT<FItem> response = applicationService.addItem(loggedInUser.getBranch(), name, catID, price, minimalQuantity, fullQuantity, manufacture, backShelves, frontShelves);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        return new ItemModel(response.getValue());
    }

    public void addShelf(boolean isInBack) {
        Response response = applicationService.addShelf(loggedInUser.getBranch(), isInBack);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void addCategory(CategoryModel cat) {
        Response response = applicationService.addCategory(loggedInUser.getBranch(), cat.getCatFatherID(),cat.getName());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void addPurchase(int desiredItemID, int orderID, int checkedSupplier, int checkedAmount, double checkedPrice, double checkedDiscount) {
        Response response = applicationService.addPurchaseRecord(loggedInUser.getBranch(), desiredItemID,orderID,checkedSupplier,checkedAmount,checkedPrice,checkedDiscount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public CategoryModel getCategory(CategoryModel categoryModel) {//todo check this func???
        ResponseT<FCategory> response= applicationService.getCategory(loggedInUser.getBranch(), categoryModel.getCatID());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            CategoryModel cat = new CategoryModel (response.getValue());
            return cat;
        }
    }
    public CategoryModel getCategory(String ID) {
        ResponseT<FCategory> response= applicationService.getCategory(loggedInUser.getBranch(), ID);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            CategoryModel cat = new CategoryModel (response.getValue());
            return cat;
        }
    }

    public ItemModel getItem(int itemID) {
        ResponseT<FItem> response= applicationService.getItem(loggedInUser.getBranch(), itemID);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            ItemModel item = new ItemModel(response.getValue());
            return item;
        }
    }

    public CategoryModel [] getSubCat(CategoryModel categoryModel) {
        ResponseT<List<FCategory>> response= applicationService.getAllCategorySubCat(loggedInUser.getBranch(), categoryModel.getCatID());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            List<FCategory> catList = response.getValue();
            CategoryModel[] catArr = new CategoryModel[catList.size()];
            int counter = 0;
            for (FCategory cat : catList) {
                catArr[counter] = new CategoryModel(cat);
                counter++;
            }
            return catArr;
        }
    }

    public void moveCategory(CategoryModel MfatherCat, CategoryModel categoryModel) {
        FCategory fatherCat = new FCategory(MfatherCat.getName(),MfatherCat.getCatID(),MfatherCat.getCatFatherID());
        FCategory category = new FCategory(categoryModel.getName(),categoryModel.getCatID(),categoryModel.getCatFatherID());
        Response response = applicationService.moveCategory(loggedInUser.getBranch(), fatherCat,category);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public List<ItemModel> getCategoryItems(CategoryModel categoryModel) {
        ResponseT<List<FItem>> response =  applicationService.getAllItemsInCategory(loggedInUser.getBranch(), categoryModel.getCatID());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<ItemModel> items = new LinkedList<>();
        for (FItem it : response.getValue()){
            items.add(new ItemModel(it));
        }
        return items;
    }

    public void setCategoryName(CategoryModel categoryModel) {
        Response response = applicationService.setCategoryName(loggedInUser.getBranch(), categoryModel.getCatID(),categoryModel.getName());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public List<ShortageItemModel> getShortageReport(){
        ResponseT<List<FInShortageItem>> response = applicationService.getShortageReport(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<ShortageItemModel> items = new LinkedList<>();
        for(FInShortageItem shortageItem : response.getValue()){
            items.add(new ShortageItemModel(shortageItem));
        }
        return items;
    }


    public List<InventoryItemModel> getInventoryReport() {
        ResponseT<List<FInventoryItem>> response = applicationService.getInventoryReport(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<InventoryItemModel> items = new LinkedList<>();
        for(FInventoryItem inventoryItem : response.getValue()){
            items.add(new InventoryItemModel(inventoryItem));
        }
        return items;
    }

    public List<InventoryItemModel> getInventoryByCategoryReport(CategoryModel categoryModel) {
        ResponseT<List<FInventoryItem>> response = applicationService.getInventoryReportByCategory(loggedInUser.getBranch(), categoryModel.getCatID());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<InventoryItemModel> items = new LinkedList<>();
        for(FInventoryItem inventoryItem : response.getValue()){
            items.add(new InventoryItemModel(inventoryItem));
        }
        return items;
    }

    public List<DamagedItemModel> getDamagedReport() {
        ResponseT<List<FDamagedItem>> response = applicationService.getDamageReport(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<DamagedItemModel> items = new LinkedList<>();
        for (FDamagedItem damagedItem : response.getValue()) {
            items.add(new DamagedItemModel(damagedItem));
        }
        return items;
    }

    public List<DamagedItemModel> getDamagedReportByDate(LocalDate sinceWhen, LocalDate untilWhen) {
        ResponseT<List<FDamagedItem>> response = applicationService.getDamageReportByDate(loggedInUser.getBranch(), sinceWhen,untilWhen);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<DamagedItemModel> items = new LinkedList<>();
        for (FDamagedItem damagedItem : response.getValue()) {
            items.add(new DamagedItemModel(damagedItem));
        }
        return items;
    }

    public List<DamagedItemModel> getDamagedReportByItemID(int itemID) {
        ResponseT<List<FDamagedItem>> response = applicationService.getDamageReportByItemID(loggedInUser.getBranch(), itemID);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<DamagedItemModel> items = new LinkedList<>();
        for (FDamagedItem damagedItem : response.getValue()) {
            items.add(new DamagedItemModel(damagedItem));
        }
        return items;
    }

    public List<DamagedItemModel> getOnlyExpiredReport() {
        ResponseT<List<FDamagedItem>> response = applicationService.getOnlyExpiredReport(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<DamagedItemModel> items = new LinkedList<>();
        for (FDamagedItem damagedItem : response.getValue()) {
            items.add(new DamagedItemModel(damagedItem));
        }
        return items;
    }

    public List<DamagedItemModel> getOnlyDamagedReport() {
        ResponseT<List<FDamagedItem>> response = applicationService.getOnlyDamageReport(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<DamagedItemModel> items = new LinkedList<>();
        for (FDamagedItem damagedItem : response.getValue()) {
            items.add(new DamagedItemModel(damagedItem));
        }
        return items;
    }

    public List<DamagedItemModel> getOnlyDamagedReportByDate(LocalDate sinceWhen, LocalDate untilWhen) {
        ResponseT<List<FDamagedItem>> response = applicationService.getOnlyDamageReportByDate(loggedInUser.getBranch(), sinceWhen,untilWhen);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<DamagedItemModel> items = new LinkedList<>();
        for (FDamagedItem damagedItem : response.getValue()) {
            items.add(new DamagedItemModel(damagedItem));
        }
        return items;
    }

    public List<DamagedItemModel> getOnlyExpiredReportByDate(LocalDate sinceWhen, LocalDate untilWhen) {
        ResponseT<List<FDamagedItem>> response = applicationService.getOnlyExpiredReportByDate(loggedInUser.getBranch(), sinceWhen,untilWhen);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<DamagedItemModel> items = new LinkedList<>();
        for (FDamagedItem damagedItem : response.getValue()) {
            items.add(new DamagedItemModel(damagedItem));
        }
        return items;
    }

    public List<DamagedItemModel> getOnlyDamagedReportByItemID(int itemID) {
        ResponseT<List<FDamagedItem>> response = applicationService.getOnlyDamageReportByItemID(loggedInUser.getBranch(), itemID);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<DamagedItemModel> items = new LinkedList<>();
        for (FDamagedItem damagedItem : response.getValue()) {
            items.add(new DamagedItemModel(damagedItem));
        }
        return items;
    }

    public List<DamagedItemModel> getOnlyExpiredReportByItemID(int itemID) {
        ResponseT<List<FDamagedItem>> response = applicationService.getOnlyExpiredReportByItemID(loggedInUser.getBranch(), itemID);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<DamagedItemModel> items = new LinkedList<>();
        for (FDamagedItem damagedItem : response.getValue()) {
            items.add(new DamagedItemModel(damagedItem));
        }
        return items;
    }

    public List<PurchasedItemModel> getPurchaseReport() {
        ResponseT<List<FPurchasedItem>> response = applicationService.getPurchaseReport(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<PurchasedItemModel> items = new LinkedList<>();
        for (FPurchasedItem purchasedItem : response.getValue()) {
            items.add(new PurchasedItemModel(purchasedItem));
        }
        return items;
    }

    public List<PurchasedItemModel>getPurchaseReportByDate(LocalDate sinceWhen, LocalDate untilWhen) {
        ResponseT<List<FPurchasedItem>> response = applicationService.getPurchaseReportByDate(loggedInUser.getBranch(), sinceWhen,untilWhen);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<PurchasedItemModel> items = new LinkedList<>();
        for (FPurchasedItem fPurchasedItem : response.getValue()) {
            items.add(new PurchasedItemModel(fPurchasedItem));
        }
        return items;
    }

    public List<PurchasedItemModel> getPurchaseReportByItemID(int itemID) {
        ResponseT<List<FPurchasedItem>> response = applicationService.getPurchaseReportByItemID(loggedInUser.getBranch(), itemID);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<PurchasedItemModel> items = new LinkedList<>();
        for (FPurchasedItem fPurchasedItem : response.getValue()) {
            items.add(new PurchasedItemModel(fPurchasedItem));
        }
        return items;
    }

    public List<PurchasedItemModel> getPurchaseReportByBusinessNumber(int supplierNumber) {
        ResponseT<List<FPurchasedItem>> response = applicationService.getPurchaseReportByBusinessNumber(loggedInUser.getBranch(), supplierNumber);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        List<PurchasedItemModel> items = new LinkedList<>();
        for (FPurchasedItem fPurchasedItem : response.getValue()) {
            items.add(new PurchasedItemModel(fPurchasedItem));
        }
        return items;
    }


    public void changeAllItemBackShelves(ItemModel itemModel, int[] idsIntArr) {
        Response response = applicationService.changeItemAppointedShelvesByRoom(loggedInUser.getBranch(), itemModel.getItemID(),true,idsIntArr);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void changeAllItemFrontShelves(ItemModel itemModel, int[] idsIntArr) {
        Response response = applicationService.changeItemAppointedShelvesByRoom(loggedInUser.getBranch(), itemModel.getItemID(),false,idsIntArr);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
    }

    public void giveDiscountToCategory(CategoryModel categoryModel, double discountFare, String discountName, LocalDate fromDate, LocalDate toDate) {
        FDiscount fDiscount = new FDiscount(discountName,fromDate,toDate,discountFare);
        applicationService.giveDiscountToCategory(loggedInUser.getBranch(), categoryModel.getCatID(),fDiscount);
    }

    //    public void orderShortage(List<Pair<Integer, Integer>> itemsToOrder) {
//        applicationService.orderShortage(itemsToOrder);
//    }
//supplier----------------------------------------------------------------------------------
    public List<SupplierModel> getAllSuppliers() {
        ResponseT<List<SSupplier>> response = applicationService.getAllSuppliers();
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            List<SSupplier> sSuppliers = response.getValue();
            List<SupplierModel> supplierModels = new ArrayList<>();
            for (SSupplier sSupplier : sSuppliers)
                supplierModels.add(new SupplierModel(sSupplier));
            return supplierModels;
        }
    }

    public List<ContactModel> getAllContacts(int businessNumber) {
        ResponseT<List<Contact>> response = applicationService.getAllContacts(businessNumber);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            List<Contact> contacts = response.getValue();
            List<ContactModel> contactModels = new ArrayList<>();
            for (Contact contact : contacts)
                contactModels.add(new ContactModel(contact));
            return contactModels;
        }
    }

    public SupplierModel getSupplier(int supplierBN) {
        ResponseT<SSupplier> response = applicationService.getSupplier(supplierBN);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            SSupplier sSupplier = response.getValue();
            return new SupplierModel(sSupplier);
        }
    }

    public void addSupplier(String name, int businessNumber, int bankAccount, boolean shouldDeliver, String paymentMethodStr, Set<DayOfWeek> daysOfSupply, String address, String area) {
        Response response = applicationService.addSupplier(name, businessNumber, bankAccount, shouldDeliver, paymentMethodStr, daysOfSupply, address, area);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supplier added successfully");
    }

    public void removeSupplier(int supplierBN) {
        Response response = applicationService.removeSupplier(supplierBN);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supplier removed successfully");
    }

    public void updateSupplierName(int supplierBN, String newName) {
        Response response = applicationService.updateSupplierName(supplierBN, newName);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supplier name updated successfully");
    }

    public void updateSupplierBankAccount(int supplierBN, int newBankAccount) {
        Response response = applicationService.updateSupplierBankAccount(supplierBN, newBankAccount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supplier bank account updated successfully");
    }

    public void updateSupplierDelivery(int supplierBN, boolean newDeliveryMethod) {
        Response response = applicationService.updateSupplierDelivery(supplierBN, newDeliveryMethod);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supplier delivery method updated successfully");
    }

    public void updateSupplierPaymentMethod(int supplierBN, String newPaymentMethod) {
        Response response = applicationService.updateSupplierPaymentMethod(supplierBN, newPaymentMethod);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supplier payment method updated successfully");
    }

    public void addContact(int supplierBN, String contactName, String phoneNumber, String email) {
        Response response = applicationService.addContact(supplierBN, contactName, phoneNumber, email);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supplier contact added successfully");
    }

    public void removeContact(int supplierBN, int contactId) {
        Response response = applicationService.removeContact(supplierBN, contactId);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supplier contact removed successfully");
    }

    public void updateContactName(int supplierBN, int contactId, String newContactName) {
        Response response = applicationService.updateContactName(supplierBN, contactId, newContactName);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Contact name updated successfully");
    }

    public void updateContactEmail(int supplierBN, int contactId, String newEmail) {
        Response response = applicationService.updateContactEmail(supplierBN, contactId, newEmail);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Contact email updated successfully");
    }

    public void updateContactPhone(int supplierBN, int contactId, String newPhoneNumber) {
        Response response = applicationService.updateContactPhone(supplierBN, contactId, newPhoneNumber);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Contact Phone updated successfully");
    }

    //-----------------------------------------------------------------------------------------------------------------------------------
    //orders-----------------------------------------------------------------------------------------------------------------------
    public void addFixedOrder(int supplierBN, Date orderDate, Set<DayOfWeek> supplyDays, HashMap<Integer, Integer> itemIdAndAmount) {
        if(loggedInUser.isBranchManager())
            throw new IllegalArgumentException("Only warehouse can add demand orders");
        Response response = applicationService.addFixedOrder(loggedInUser.getBranch(), supplierBN, orderDate, supplyDays, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Fixed Order added successfully");
    }

    public void addFixedOrders(Date orderDate, Set<DayOfWeek> supplyDays, HashMap<Integer, Integer> itemIdAndAmount) {
        if(loggedInUser.isBranchManager())
            throw new IllegalArgumentException("Only warehouse can add demand orders");
        Response response = applicationService.addFixedOrders(loggedInUser.getBranch(), orderDate, supplyDays, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Fixed Orders added successfully");
    }

    public void addDemandOrders(Date orderDate, Date supplyDay, HashMap<Integer, Integer> itemIdAndAmount) {
        if(loggedInUser.isBranchManager())
            throw new IllegalArgumentException("Only warehouse can add demand orders");
        Response response = applicationService.addDemandOrders(loggedInUser.getBranch(), orderDate, supplyDay, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Demand Orders added successfully");
    }

    public void addProductsToFixedOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) {
        if(loggedInUser.isBranchManager())
            throw new IllegalArgumentException("Only warehouse can add demand orders");
        Response response = applicationService.addProductsToFixedOrder(supplierBN, orderId, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Products added to fixed order successfully");
    }

    public void removeProductsFromFixedOrder(int supplierBN, int orderId, List<Integer> itemIdAndAmount) {
        if(loggedInUser.isBranchManager())
            throw new IllegalArgumentException("Only warehouse can add demand orders");
        Response response = applicationService.removeProductsFromFixedOrder(supplierBN, orderId, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Products removed from fixed order successfully");
    }

    public void updateProductsOfFixedOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) {
        if(loggedInUser.isBranchManager())
            throw new IllegalArgumentException("Only warehouse can add demand orders");
        Response response = applicationService.updateProductsOfFixedOrder(supplierBN, orderId, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Products of fixed order updated successfully");
    }

    public void addProductsToDemandOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) {
        if(loggedInUser.isBranchManager())
            throw new IllegalArgumentException("Only warehouse can add demand orders");
        Response response = applicationService.addProductsToDemandOrder(supplierBN, orderId, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Products added to fixed order successfully");
    }

    public void removeProductsFromDemandOrder(int supplierBN, int orderId, List<Integer> itemIdAndAmount) {
        if(loggedInUser.isBranchManager())
            throw new IllegalArgumentException("Only warehouse can add demand orders");
        Response response = applicationService.removeProductsFromDemandOrder(supplierBN, orderId, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Products removed from fixed order successfully");
    }

    public void updateProductsOfDemandOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndAmount) {
        if(loggedInUser.isBranchManager())
            throw new IllegalArgumentException("Only warehouse can add demand orders");
        Response response = applicationService.updateProductsOfDemandOrder(supplierBN, orderId, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Products of fixed order updated successfully");
    }

    public void addDemandOrder(int supplierBN, Date orderDate, Date supplyDate, HashMap<Integer, Integer> itemIdAndAmount) {
        if(loggedInUser.isBranchManager())
            throw new IllegalArgumentException("Only warehouse can add demand orders");
        Response response = applicationService.addDemandOrder(loggedInUser.getBranch(), supplierBN, orderDate, supplyDate, itemIdAndAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Demand Order added successfully");
    }

    public void removeUnSuppliedDemandOrder(int supplierBN, int orderId) {
        if(loggedInUser.isBranchManager())
            throw new IllegalArgumentException("Only warehouse can add demand orders");
        Response response = applicationService.removeUnSuppliedDemandOrder(supplierBN, orderId);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Un supplied demand order removed successfully");
    }

    public List<DemandOrderModel> getAllSupplierDemandOrders(int supplierBN) {
        ResponseT<List<DemandOrder>> response = applicationService.getAllSupplierDemandOrders(supplierBN);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            List<DemandOrder> demandOrders = response.getValue();
            List<DemandOrderModel> demandOrderModels = new ArrayList<>();
            for (DemandOrder demandOrder : demandOrders)
                demandOrderModels.add(new DemandOrderModel(demandOrder));
            return demandOrderModels;
        }
    }

    public List<DemandOrderModel> getAllDemandOrders() {
        ResponseT<List<DemandOrder>> response = applicationService.getAllDemandOrders();
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            List<DemandOrder> demandOrders = response.getValue();
            List<DemandOrderModel> demandOrderModels = new ArrayList<>();
            for (DemandOrder demandOrder : demandOrders)
                demandOrderModels.add(new DemandOrderModel(demandOrder));
            return demandOrderModels;
        }
    }

    public List<FixedOrderModel> getAllFixedOrders() {
        ResponseT<List<FixedOrder>> response = applicationService.getAllFixedOrders();
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            List<FixedOrder> fixedOrders = response.getValue();
            List<FixedOrderModel> fixedOrderModels = new ArrayList<>();
            for (FixedOrder fixedOrder : fixedOrders)
                fixedOrderModels.add(new FixedOrderModel(fixedOrder));
            return fixedOrderModels;
        }
    }

    public List<FixedOrderModel> getAllSupplierFixedOrders(int supplierBN) {
        ResponseT<List<FixedOrder>> response = applicationService.getAllSupplierFixedOrders(supplierBN);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            List<FixedOrder> fixedOrders = response.getValue();
            List<FixedOrderModel> fixedOrderModels = new ArrayList<>();
            for (FixedOrder fixedOrder : fixedOrders)
                fixedOrderModels.add(new FixedOrderModel(fixedOrder));
            return fixedOrderModels;
        }
    }

    public void supplyDemandOrder(int supplierBN, int orderId, HashMap<Integer, Integer> itemIdAndUnSuppliedAmount) {
        if(loggedInUser.isBranchManager())
            throw new IllegalArgumentException("Only warehouse can add demand orders");
        Response response = applicationService.supplyDemandOrder(supplierBN, orderId, itemIdAndUnSuppliedAmount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Demand order supplied successfully");
    }

    public void unActiveFixedOrder(int supplierBN, int orderId, Date date) {
        if(loggedInUser.isBranchManager())
            throw new IllegalArgumentException("Only warehouse can add demand orders");
        Response response = applicationService.unActiveFixedOrder(supplierBN, orderId, date);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Fixed order unactivated successfully");
    }

    public void sendPDFDOrder(int supplierBN, int orderId, String email) {
        System.out.print("Sending email");
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(notifier, 0, 1, TimeUnit.SECONDS);
        Response response = applicationService.sendPDFDOrder(supplierBN, orderId, email);
        scheduler.shutdownNow();
        System.out.println();
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Demand order sent successfully");
    }


    public List<FixedOrderModel> dailyFixedOrders() {
        ResponseT<List<FixedOrder>> response = applicationService.dailyFixedOrders();
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else {
            List<FixedOrder> fixedOrders = response.getValue();
            List<FixedOrderModel> fixedOrderModels = new ArrayList<>();
            for (FixedOrder fixedOrder : fixedOrders)
                fixedOrderModels.add(new FixedOrderModel(fixedOrder));
            return fixedOrderModels;
        }
    }

    public void addSupplyDayToFixedOrder(int supplierBN, int orderId, DayOfWeek day) {
        if(loggedInUser.isBranchManager())
            throw new IllegalArgumentException("Only warehouse can add demand orders");
        Response response = applicationService.addSupplyDayToFixedOrder(supplierBN, orderId, day);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supply day added to fixed order successfully");
    }

    public void removeSupplyDayFromFixedOrder(int supplierBN, int orderId, DayOfWeek day) {
        if(loggedInUser.isBranchManager())
            throw new IllegalArgumentException("Only warehouse can add demand orders");
        Response response = applicationService.removeSupplyDayToFixedOrder(supplierBN, orderId, day);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Supply day removed from fixed order successfully");
    }

    public void createNextWeekFixedOrders() {
        if(loggedInUser.isBranchManager())
            throw new IllegalArgumentException("Only warehouse can add demand orders");
        Response response = applicationService.createNextWeekFixedOrders(loggedInUser.getBranch());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("next week orders created successfully");
    }
    //-----------------------------------------------------------------------------------------------------------------------------
    // bill of quantity-----------------------------------------------------------------------------------------------------
    public BillOfQuantityModel getBillOfQuantity(SupplierModel supplierModel) {
        ResponseT<SBillOfQuantities> response = applicationService.getBillOfQuantity(loggedInUser.getBranch(), supplierModel.getBusinessNumber());
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            return new BillOfQuantityModel(response.getValue(), supplierModel);
    }

    public void updateItemPrice(int supplierBN, int catalogNumber, double newPrice) {
        Response response = applicationService.updateItemPrice(loggedInUser.getBranch(), supplierBN, catalogNumber, newPrice);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Item price updated successfully");
    }

    public Map<Integer, Double> getItemDiscounts(int supplierBN, int catalogNumber) {
        ResponseT<Map<Integer, Double>> response = applicationService.getItemDiscounts(loggedInUser.getBranch(), supplierBN, catalogNumber);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            return response.getValue();
    }

    public void addItemDiscountAccordingToAmount(int supplierBN, int catalogNumber, int amount, double discount) {
        Response response = applicationService.addItemDiscountAccordingToAmount(loggedInUser.getBranch(), supplierBN, catalogNumber, amount, discount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Item discount according to amount added successfully");
    }

    public void updateItemDiscountAccordingToAmount(int supplierBN, int catalogNumber, int amount, double newDiscount) {
        Response response = applicationService.updateItemDiscountAccordingToAmount(loggedInUser.getBranch(), supplierBN, catalogNumber, amount, newDiscount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Item discount according to amount updated successfully");
    }

    public void removeItemDiscountAccordingToAmount(int supplierBN, int catalogNumber, int amount) {
        Response response = applicationService.removeItemDiscountAccordingToAmount(loggedInUser.getBranch(), supplierBN, catalogNumber, amount);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Item discount according to amount removed successfully");
    }

    public void updateSupplierCatalog(int supplierBN, int catalogNumber, int newSupplierCatalog) {
        Response response = applicationService.updateSupplierCatalog(loggedInUser.getBranch(), supplierBN, catalogNumber, newSupplierCatalog);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Item supplier catalog number updated successfully");
    }

    public void updateItemName(int supplierBN, int catalogNumber, String newName) {
        Response response = applicationService.updateItemName(loggedInUser.getBranch(), supplierBN, catalogNumber, newName);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Item name updated successfully");
    }

    public void addItemToSupplier(int supplierBN, int catalogNumber, int supplierCatalog, double price, String name) {
        Response response = applicationService.addItemToSupplier(loggedInUser.getBranch(), supplierBN, catalogNumber, supplierCatalog, price, name);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Item added successfully");
    }

    public void removeItemFromSupplier(int supplierBN, int catalogNumber) {
        Response response = applicationService.removeItemFromSupplier(loggedInUser.getBranch(), supplierBN, catalogNumber);
        if (response.errorOccurred())
            throw new IllegalArgumentException(response.getErrorMessage());
        else
            System.out.println("Item removed successfully");
    }

    //---------------------------------------------------------------------------------------------------
    public void loadDataForTests() {
        dataLoader.loadDataForTests();
    }
    public void deleteDataForTests() { dataLoader.deleteDataForTests();}
    Runnable notifier = () -> System.out.print(".");

    public ApplicationService getApplicationService() {
        return applicationService;
    }
    public String getBranchOfShift(int ID)
    {
        return applicationService.getBranchOfShift(ID);
    }
}
