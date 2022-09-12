package SupplierBusiness;

import BusinessLayer.SupplierBusiness.Supplier;
import BusinessLayer.SupplierBusiness.SupplierFacade;
import BusinessLayer.SupplierBusiness.SupplierItem;
import DataAccessLayer.InventoryDAL.DALFacade;
import DataAccessLayer.DataBaseCreator;
import DataAccessLayer.SupplierDAL.*;
import Presentation.Model.BackendController;
import org.junit.*;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.HashSet;

public class BillOfQuantityTest {
    static Supplier s;
    static SupplierFacade supplierFacade;
    static DataBaseCreator dataBaseCreator = new DataBaseCreator();

    @BeforeClass
    public static void setUp() throws Exception {
        dataBaseCreator.CreateAllTables();
        dataBaseCreator.deleteAllTables();
        dataBaseCreator.CreateAllTables();
        HashSet<DayOfWeek> dow = new HashSet<>();
        dow.add(DayOfWeek.MONDAY);
        supplierFacade = new SupplierFacade();
        supplierFacade.addSupplier("Oran", 1235, 123123,true, "Cash", dow, "rager 116", "South");
        s = supplierFacade.getSupplier(1235);
    }

    @Test
    public void addItemDiscountAccordingToAmount() {
        SupplierItem supplierItem1 = new SupplierItem("Yasmin 3, Dimona", 1235, 1 , 15.0, "OranItem1", 10 );
        SupplierItem supplierItem2 = new SupplierItem("Yasmin 3, Dimona", 1235, 2  , 20.0, "OranItem1", 20);
        s.getBillOfQuantity().addItem("Yasmin 3, Dimona", supplierItem1);
        s.getBillOfQuantity().addItem("Yasmin 3, Dimona", supplierItem2);
        s.getBillOfQuantity().addItemDiscountAccordingToAmount("Yasmin 3, Dimona", 1,  100, 20);
        s.getBillOfQuantity().addItemDiscountAccordingToAmount("Yasmin 3, Dimona",2, 50, 5);
        Assert.assertEquals(20, s.getBillOfQuantity().getItemDiscounts("Yasmin 3, Dimona", 1).get(100).doubleValue(), 0);
        Assert.assertEquals(5, s.getBillOfQuantity().getItemDiscounts("Yasmin 3, Dimona", 2).get(50).doubleValue(), 0);
    }

    @Test
    public void updateItemDiscountAccordingToAmount() {
        SupplierItem supplierItem3 = new SupplierItem("Yasmin 3, Dimona", 1235, 3 , 30.0, "OranItem3", 30);
        SupplierItem supplierItem4 = new SupplierItem("Yasmin 3, Dimona", 1235, 4 , 40.0, "OranItem4", 40);
        s.getBillOfQuantity().addItem("Yasmin 3, Dimona", supplierItem3);
        s.getBillOfQuantity().addItem("Yasmin 3, Dimona", supplierItem4);
        s.getBillOfQuantity().addItemDiscountAccordingToAmount("Yasmin 3, Dimona", 3, 100, 20);
        s.getBillOfQuantity().addItemDiscountAccordingToAmount("Yasmin 3, Dimona", 4, 50, 5);
        s.getBillOfQuantity().updateItemDiscountAccordingToAmount("Yasmin 3, Dimona", 3, 100, 50);
        s.getBillOfQuantity().updateItemDiscountAccordingToAmount("Yasmin 3, Dimona", 4, 50, 10);
        Assert.assertEquals(50, s.getBillOfQuantity().getItemDiscounts("Yasmin 3, Dimona", 3).get(100).doubleValue(), 0);
        Assert.assertEquals(10, s.getBillOfQuantity().getItemDiscounts("Yasmin 3, Dimona", 4).get(50).doubleValue(), 0);
    }

    @Test
    public void removeItemDiscountAccordingToAmount() {
        SupplierItem supplierItem5 = new SupplierItem("Yasmin 3, Dimona", 1235, 5 , 50.0, "OranItem5", 5);
        SupplierItem supplierItem6 = new SupplierItem("Yasmin 3, Dimona", 1235, 6 , 60.0, "OranItem6", 6);
        s.getBillOfQuantity().addItem("Yasmin 3, Dimona", supplierItem5);
        s.getBillOfQuantity().addItem("Yasmin 3, Dimona", supplierItem6);
        s.getBillOfQuantity().addItemDiscountAccordingToAmount("Yasmin 3, Dimona", 5, 100, 20);
        s.getBillOfQuantity().addItemDiscountAccordingToAmount("Yasmin 3, Dimona", 6, 50, 5);
        s.getBillOfQuantity().removeItemDiscountAccordingToAmount("Yasmin 3, Dimona", 5,100);
        Assert.assertFalse(s.getBillOfQuantity().getItemDiscounts("Yasmin 3, Dimona", 5).containsKey(100));
        s.getBillOfQuantity().removeItemDiscountAccordingToAmount("Yasmin 3, Dimona", 6,50);
        Assert.assertFalse(s.getBillOfQuantity().getItemDiscounts("Yasmin 3, Dimona", 5).containsKey(100));
    }

    @Test
    public void updateItemName() {
        SupplierItem supplierItem1 = new SupplierItem("Yasmin 3, Dimona", 1235, 1 , 15.0, "OranItem1", 10);
        s.getBillOfQuantity().addItem("Yasmin 3, Dimona", supplierItem1);
        s.getBillOfQuantity().updateItemName("Yasmin 3, Dimona", 1, "aaa");
        Assert.assertEquals("aaa", s.getBillOfQuantity().getItemName("Yasmin 3, Dimona", 1));
    }

    @Test
    public void removeItem() {
        SupplierItem supplierItem7 = new SupplierItem("Yasmin 3, Dimona", 1235, 7, 70.0, "OranItem7", 7);
        s.getBillOfQuantity().addItem("Yasmin 3, Dimona", supplierItem7);
        s.getBillOfQuantity().removeItem("Yasmin 3, Dimona", 7);
        try {
            s.getBillOfQuantity().checkItemExists("Yasmin 3, Dimona", 7);
        }
        catch (Exception e){
            Assert.assertEquals("Item with the item id: 7 not exists", e.getMessage());
        }
    }
}