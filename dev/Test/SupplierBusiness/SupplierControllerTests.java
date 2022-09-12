package SupplierBusiness;

import BusinessLayer.SupplierBusiness.SupplierController;
import DataAccessLayer.InventoryDAL.DALFacade;
import DataAccessLayer.DataBaseCreator;
import DataAccessLayer.SupplierDAL.*;
import Presentation.Model.BackendController;

import org.junit.Assert;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.HashSet;

public class SupplierControllerTests {
    private static SupplierController spController;

    @org.junit.BeforeClass
    public static void setUp() throws Exception {
        DataBaseCreator dataBaseCreator = new DataBaseCreator();
        dataBaseCreator.CreateAllTables();
        dataBaseCreator.deleteAllTables();
        dataBaseCreator.CreateAllTables();
        spController = new SupplierController();
        HashSet<DayOfWeek> dow = new HashSet<>();
        dow.add(DayOfWeek.MONDAY);
        spController.addSupplier("Oran", 123, 321321, true, "Cash", dow, "rager 1", "South");
        spController.addSupplier("Nadav", 456, 654654, false, "Cash", dow, "rager 2", "South");
    }

    @org.junit.Test
    public void testAddItem() {
        spController.addItem("BranchTest", 123, 1,1,10,"OranItem1");
        Assert.assertEquals(10.0,spController.getItemPrice("BranchTest", 123,1), 0);
        Assert.assertEquals("OranItem1",spController.getItemName("BranchTest", 123,1));

    }
    @org.junit.Test
    public void testAddSupplier() {
        HashSet<DayOfWeek> dow = new HashSet<>();
        dow.add(DayOfWeek.MONDAY);
        spController.addSupplier("Oran2", 12346, 321321, true, "Cash", dow, "rager 3", "South");
        Assert.assertEquals(12346, spController.getSupplier(12346).getBusinessNumber());
        Assert.assertNotEquals(null, spController.getBillOfQuantity(12346));
        Assert.assertEquals(321321, spController.getSupplier(12346).getBankAccount());
        Assert.assertTrue(spController.getSupplier(12346).getShouldDeliver());
        Assert.assertEquals("Cash", spController.getSupplier(12346).getPaymentMethod());
        Assert.assertEquals("Oran2", spController.getSupplier(12346).getName());
    }

    @org.junit.Test
    public void testAddContact() {
        spController.addContact(123, "OranContact2", "0542857663", "oran@gmail.com");
    }


    @org.junit.Test
    public void testGetItemDiscounts() {
        spController.addItem("BranchTest", 123, 2,1,10,"OranItem1");
        spController.addItem("BranchTest", 123, 3,2,5,"OranItem2");
        spController.addItem("BranchTest", 456, 1,1,20,"NadavItem");
        spController.addItemDiscountAccordingToAmount("BranchTest", 123, 2, 10, 50);
        spController.addItemDiscountAccordingToAmount("BranchTest", 123, 3, 30, 20);
        spController.addItemDiscountAccordingToAmount("BranchTest", 456, 1, 10, 30);
        Assert.assertEquals(50, spController.getItemDiscounts("BranchTest", 123, 2).get(10).intValue());
        Assert.assertEquals(20,spController.getItemDiscounts("BranchTest", 123, 3).get(30).intValue());
        Assert.assertEquals(30, spController.getItemDiscounts("BranchTest", 456, 1).get(10).intValue());
    }

    @org.junit.Test
    public void testRemoveSupplier() {
        HashSet<DayOfWeek> dow = new HashSet<>();
        dow.add(DayOfWeek.MONDAY);
        spController.addSupplier("OranToRemove", 333, 321321, true, "Cash", dow, "rager 4", "South");
        try {
            spController.removeSupplier(333);
            spController.getSupplier(333);
            Assert.fail();
        }
        catch (Exception e){
            Assert.assertEquals("Supplier is not found!", e.getMessage());
        }
    }
}