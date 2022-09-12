
import BusinessLayer.ApplicationFacade;
import BusinessLayer.EmployeeModule.Objects.Employee;
import BusinessLayer.EmployeeModule.Objects.Shift;

import DataAccessLayer.DataBaseCreator;
import Presentation.Model.BackendController;
import org.junit.*;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.HashMap;

public class EmployeeTest {

    static ApplicationFacade facade;

    private static DataBaseCreator dataBaseCreator;
    private static BackendController controller;

    @BeforeClass
    public static void setUp() throws Exception {
        dataBaseCreator = new DataBaseCreator();
        dataBaseCreator.CreateAllTables();
        dataBaseCreator.deleteAllTables();
        dataBaseCreator.CreateAllTables();
        //controller.loadDataForTests();
        facade = new ApplicationFacade();
        BackendController.getInstance().loadDataForTests();
    }
    @AfterClass
    public static void afterClass() {
        dataBaseCreator.deleteAllTables();
    }

    @Test
    public void addEmployee(){//v
        try {
            //adding normal employees
            Assert.assertNull(facade.getEmployee(12345));
            facade.addEmployee("avi", 12345, "bank1", 5000, "2022-01-01", "cashier", "employmentDetails1","0544363655");
            Assert.assertNotNull(facade.getEmployee(12345));
            facade.removeEmployee(12345);
            Assert.assertNull(facade.getEmployee(12345));

        }
        catch (Exception e)
        {
            Assert.fail();
        }
    }
    @Test
    public void removeEmployee(){//V
        try {
            //removing employee has future shifts
            facade.addEmployee("avi", 12345, "bank1", 5000, "2022-01-01", "cashier", "employmentDetails1","0544363655");
            facade.addEmployeeToShift(1, 12345, "cashier");
            facade.removeEmployee(12345); //removal should fail
            Assert.fail();
        }
        catch (Exception e)
        {

        }
        try
        {
            facade.removeEmployeeFromShift(1,12345);
            facade.removeEmployee(12345); //removal should succeed
            Assert.assertNull(facade.getEmployee(12345));
        }
        catch (Exception e)
        {
            Assert.fail();
        }

    }
    @Test
    public void addJobToEmployee(){//V
        try
        {
            Employee em = facade.getEmployee(100);
            Assert.assertNotNull(em);
            Assert.assertFalse(facade.validEmployeeHasJob("usher","100"));
            facade.addJobToEmployee(100,"usher");
            Assert.assertTrue(facade.validEmployeeHasJob("usher","100"));
            facade.removeJobFromEmployee(100,"usher");
            Assert.assertFalse(facade.validEmployeeHasJob("usher","100"));
        }
        catch (Exception e)
        {
            Assert.fail();
        }
    }
    @Test
    public void RemoveJobFromEmployee() {//V
        try
        {
            Employee em = facade.getEmployee(100);
            facade.addJobToEmployee(100,"usher");
            Assert.assertTrue(facade.validEmployeeHasJob("usher","100"));
            facade.removeJobFromEmployee(100,"usher");
            Assert.assertFalse(facade.validEmployeeHasJob("usher","100"));
        }
        catch (Exception e)
        {
            Assert.fail();
        }
    }
    @Test
    public void addShift() {
        HashMap<Integer, String> jobMap = new HashMap<>();
        jobMap.put(100, "cashier"); jobMap.put(200, "usher");
        try
        {
            facade.addShift("Ringelblum 9, Beersheba","2023-07-05", "morning", jobMap);
            Assert.fail();
        }
        catch (Exception e)
        {
            Assert.assertEquals("employee list does not contain shift manager", e.getMessage());
        }
        jobMap.put(400, "shift manager");
        try
        {
            facade.addShift("Ringelblum 9, Beersheba","2023-07-05", "morning", jobMap);
            Assert.fail();
        }
        catch (Exception e)
        {
            Assert.assertEquals("cannot save shift , there are jobs missing", e.getMessage());
        }
        jobMap.put(300, "warehouse");
        try{
            facade.addShift("Ringelblum 9, Beersheba","2023-07-05", "morning", jobMap);
        }
        catch (Exception e){
            Assert.fail();
        }
    }
    @Test
    public void removeShift() {
        try
        {
            Assert.assertNotNull(facade.getShift(1));
            facade.removeShift(1);
            facade.getShift(1);
            Assert.fail();
        }
        catch (Exception e)
        {
            Assert.assertEquals("Shift with ID: 1 does not exist", e.getMessage());
        }
    }
    @Test
    public void updateEmployeeDetails() {//V
        try {
            //update employee name
            facade.updateEmployeeName(100, "newName");
            Assert.assertEquals("newName", facade.getEmployee(100).getName());
            facade.updateEmployeeName(100, "cash");
            Assert.assertEquals("cash", facade.getEmployee(100).getName());

            //update employee Salary
            facade.updateEmployeeSalary(500, 10000);
            Assert.assertEquals(10000, facade.getEmployee(500).getSalary());
            facade.updateEmployeeSalary(500, 7000);
            Assert.assertEquals(7000, facade.getEmployee(500).getSalary());

            //update employee Bank Account
            facade.updateEmployeeBankAccount(200, "newBank");
            Assert.assertEquals("newBank", facade.getEmployee(200).getBankAccountDetails());
            facade.updateEmployeeBankAccount(200, "bank1");
            Assert.assertEquals("bank1", facade.getEmployee(200).getBankAccountDetails());
        }
        catch (Exception e)
        {
            Assert.fail();
        }

    }
    @Test
    public void addEmployeeToShift()  {
        try {
            Shift shift1 = facade.getShift(1);
            String date1 = shift1.getDate().toString();
            String shiftType1 = shift1.getShiftType();

            //adding employees with constraints on the shift date
            facade.addEmployee("aaron", 987, "bank1", 5000, "2022-01-01", "cashier", "employmentDetails1","0544363655");
            facade.addEmployeeToShift(1, 987, "cashier");
            Assert.assertTrue(facade.getShift(1).getEmployees().containsKey(987));

            facade.removeEmployeeFromShift(1,987);
            Assert.assertFalse(facade.getShift(1).getEmployees().containsKey(987));

            //now the employee has constraint on the date of shift 1
            facade.addJobConstraint(987, date1, shiftType1);

            try {
                facade.addEmployeeToShift(1, 987, "cashier");
                Assert.fail();
            }
            catch (Exception e){
                Assert.assertEquals("Employee with ID: 987 cannot be assigned to this shift", e.getMessage());
            }
            Assert.assertFalse(facade.getShift(1).getEmployees().containsKey(987));


            //adding employees with wrong jobs
            try {
                facade.addEmployeeToShift(1, 987, "usher");
                Assert.fail();
            }
            catch (Exception e){
                Assert.assertEquals("Employee hasn't qualified to work at this job", e.getMessage());
            }
            Assert.assertFalse(facade.getShift(1).getEmployees().containsKey(987));


            //adding employee who is already assigned on a shift in a different branch
            HashMap<Integer, String> jobMap1 = new HashMap<>();
            jobMap1.put(101, "cashier"); jobMap1.put(201, "usher"); jobMap1.put(301, "warehouse");jobMap1.put(401, "shift manager");
            facade.addShift("Ringelblum 9, Beersheba","2023-08-08", "morning", jobMap1);

            HashMap<Integer, String> jobMap2 = new HashMap<>();
            jobMap2.put(100, "cashier"); jobMap2.put(200, "usher"); jobMap2.put(300, "warehouse");jobMap2.put(400, "shift manager");
            facade.addShift("Yasmin 3, Dimona","2023-08-08", "morning", jobMap2);

            int newShiftID1 = facade.getShiftByDateTypeAndBranch("2023-08-08", "morning", "Ringelblum 9, Beersheba").getShiftID();
            int newShiftID2 = facade.getShiftByDateTypeAndBranch("2023-08-08", "morning", "Yasmin 3, Dimona").getShiftID();

            facade.addEmployeeToShift(newShiftID1, 987, "cashier"); //should work
            Assert.assertTrue(facade.getShift(newShiftID1).getEmployees().containsKey(987));
            try {
                facade.addEmployeeToShift(newShiftID2, 987, "cashier"); //should not work
                Assert.fail();
            }
            catch (Exception e){
                Assert.assertEquals("employee already assigned to a shift on a different branch", e.getMessage());
            }
            Assert.assertFalse(facade.getShift(newShiftID2).getEmployees().containsKey(987));
        }
        catch (Exception e)
        {
            Assert.fail();
        }
    }
    @Test
    public void removeEmployeeFromShift()  {
        try {
            facade.addEmployee("aaron", 9000, "bank1", 5000, "2023-01-01", "cashier", "employmentDetails1","0544363655");
            facade.addEmployee("aaron", 9001, "bank1", 5000, "2023-01-01", "cashier", "employmentDetails1","0544363655");

            HashMap<Integer, String> jobMap1 = new HashMap<>();
            jobMap1.put(9000, "cashier"); jobMap1.put(201, "usher"); jobMap1.put(301, "warehouse");jobMap1.put(401, "shift manager");
            facade.addShift("Rehov 2, Haifa","2023-08-08", "morning", jobMap1);
            int newShiftID1 = facade.getShiftByDateTypeAndBranch("2023-08-08", "morning", "Rehov 2, Haifa").getShiftID();

            facade.addEmployeeToShift(newShiftID1, 9001, "cashier");

            //now we remove employee 9000 making employee 9001 the only cashier in shift, so we can't remove him now.
            facade.removeEmployeeFromShift(newShiftID1, 9000); //will succeed
            Assert.assertFalse(facade.getShift(14).getEmployees().containsKey(9000));

            try {
                facade.removeEmployeeFromShift(newShiftID1, 9001); //will fail
                Assert.fail();
            }
            catch (Exception e){
                Assert.assertEquals("cannot save shift , there are jobs missing", e.getMessage());
            }
            Assert.assertTrue(facade.getShift(newShiftID1).getEmployees().containsKey(9001));
        }
        catch (Exception e)
        {
            Assert.fail();
        }
    }
    @Test
    public void getAvailableEmployees()  {
        try
        {
            int before = facade.getAvailableEmployeesForShift("2022-12-12","morning").size();
            facade.addJobConstraint(100,"2022-12-12","morning");
            int after = facade.getAvailableEmployeesForShift("2022-12-12","morning").size();
            Assert.assertEquals(before-1,after);
        }
        catch (Exception e)
        {
            Assert.fail();
        }
    }
    @Test
    public void addJobCertificationToEmployee()  {
        try
        {
            facade.addCertToEmployee(400,"shift manager","team leader");
            Assert.fail();
        }
        catch (Exception e)
        {
            Assert.assertEquals("Employee already has this job Certification", e.getMessage());
        }
        try
        {
            facade.addEmployee("tomer", 87654, "bank1", 88880, "2022-01-01", "driver", "None","0544363655");
            facade.addCertToEmployee(87654,"driver","A");
            facade.removeJobFromEmployee(87654,"driver");
            facade.removeEmployee(87654);
        }
        catch (Exception e)
        {
            Assert.fail();
        }
    }
    @Test
    public void AssignDriverToShift()  {
        HashMap<Integer, String> jobMap = new HashMap<>();
        jobMap.put(100, "cashier"); jobMap.put(200, "usher");jobMap.put(400, "shift manager"); jobMap.put(600, "driver");
        try{
            facade.addShift("Rehov 2, Haifa","2022-10-05", "morning", jobMap);
            Assert.fail();
        }
        catch (Exception e){
            Assert.assertEquals("Drivers can only be assigned to shifts in the HQ", e.getMessage());
        }
        try
        {
            jobMap = new HashMap<>();
            jobMap.put(600, "driver");
            jobMap.put(601, "driver");
            jobMap.put(602, "driver");
            facade.addShift("Rehov 2, Haifa","2023-07-05", "morning", jobMap);
            Assert.fail();
        }
        catch (Exception e)
        {
            Assert.assertEquals("Drivers can only be assigned to shifts in the HQ", e.getMessage());
        }
        try
        {
            jobMap = new HashMap<>();
            jobMap.put(600, "driver");
            jobMap.put(601, "driver");
            jobMap.put(401, "shift manager");
            facade.addShift(facade.getAddressOfHQ(),"2024-07-05", "morning", jobMap);
            facade.addShift(facade.getAddressOfHQ(),"2024-07-05", "evening", jobMap);
            Assert.fail();
        }
        catch (Exception e)
        {
            Assert.assertEquals("shift manager of drivers is the CEO", e.getMessage());
        }
        try
        {
            jobMap = new HashMap<>();
            jobMap.put(600, "driver");
            jobMap.put(601, "driver");
            jobMap.put(1, "CEO");
            facade.addShift(facade.getAddressOfHQ(),"2024-07-05", "morning", jobMap);
            facade.addShift(facade.getAddressOfHQ(),"2024-07-05", "evening", jobMap);
            facade.removeShift(9);
            facade.removeShift(10);
        }
        catch (Exception e)
        {
            Assert.fail();
        }


    }
    @Test
    public void RemoveDriverFromShift()  {
        try
        {
            HashMap<Integer, String> jobMap = new HashMap<>();
            jobMap = new HashMap<>();
            jobMap.put(600, "driver");
            jobMap.put(601, "driver");
            jobMap.put(602, "driver");
            jobMap.put(1, "CEO");
            facade.addShift(facade.getAddressOfHQ(),"2024-07-05", "morning", jobMap);
            facade.addShift(facade.getAddressOfHQ(),"2024-07-05", "evening", jobMap);

            facade.removeEmployeeFromShift(14,100);
            Assert.fail();
        }
        catch (Exception e)
        {
            Assert.assertEquals("Employee in shift is not exists in the database!",e.getMessage());
        }
        try
        {
            facade.removeEmployeeFromShift(14,1);
            Assert.fail();
        }
        catch (Exception e)
        {
            Assert.assertEquals("CEO is shift manager of drivers , cant remove him from shift",e.getMessage());
        }
        try
        {
            facade.removeEmployeeFromShift(14,600);
            facade.removeShift(14);
            facade.removeShift(15);

        }
        catch (Exception e)
        {
            Assert.fail();
        }
    }

    @Test
    public void getShiftsLastMonth() {
        try
        {
            Assert.assertEquals(facade.getShiftLastMonth("morning", 103).size(), 0);

            String date1 = LocalDate.now().minusDays(5).toString();
            String date2 = LocalDate.now().minusDays(6).toString();
            String date3 = LocalDate.now().minusDays(7).toString();
            String date4 = LocalDate.now().minusDays(50).toString();

            HashMap<Integer, String> jobMap1 = new HashMap<>();
            jobMap1.put(103, "cashier"); jobMap1.put(203, "usher"); jobMap1.put(303, "warehouse");jobMap1.put(403, "shift manager");
            facade.addShift("Ringelblum 9, Beersheba",date1, "morning", jobMap1);
            facade.addShift("Ringelblum 9, Beersheba",date2, "morning", jobMap1);
            facade.addShift("Yasmin 3, Dimona",date3, "morning", jobMap1);
            facade.addShift("Yasmin 3, Dimona",date4, "morning", jobMap1);

            Assert.assertEquals(facade.getShiftLastMonth("morning", 103).size(), 3);

            facade.removeShift(facade.getShiftByDateTypeAndBranch(date3, "morning", "Yasmin 3, Dimona").getShiftID());

            Assert.assertEquals(facade.getShiftLastMonth("morning", 203).size(), 2);
        }
        catch (Exception e)
        {
            Assert.fail();
        }
    }

    @Test
    public void getShiftByDateTypeAndBranch() {
        HashMap<Integer, String> jobMap = new HashMap<>();
        jobMap.put(100, "cashier");
        jobMap.put(200, "usher");
        jobMap.put(400, "shift manager");
        jobMap.put(300, "warehouse");
        try {
            Assert.assertNull(facade.getShiftByDateTypeAndBranch("Rehov 2, Haifa", "2025-11-08", "morning"));
        } catch (Exception e) {
            Assert.assertEquals("Shift with given date, type and branch was not found",e.getMessage());
        };
        try{
            facade.addShift("Rehov 2, Haifa", "2025-11-08", "morning", jobMap);
        }
        catch (Exception e){
            Assert.fail();
        }
    }
}