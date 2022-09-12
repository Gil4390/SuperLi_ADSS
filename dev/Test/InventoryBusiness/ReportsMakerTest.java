package InventoryBusiness;

import DataAccessLayer.DataBaseCreator;
import Presentation.Model.BackendController;
import Presentation.Model.InventoryModel.PDamageReason;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ReportsMakerTest {
    //private static boolean setUpIsDone = false;
    private static BackendController cont;

    @BeforeClass
    public static void setUp() throws Exception {
        DataBaseCreator dataBaseCreator = new DataBaseCreator();
        dataBaseCreator.CreateAllTables();
        dataBaseCreator.deleteAllTables();
        dataBaseCreator.CreateAllTables();
        cont = BackendController.getInstance();
        cont.loadDataForTests();

        BackendController.getInstance().findJobEmployee("300", "warehouse");
        BackendController.getInstance().enterBranch("Yasmin 3, Dimona");

        HashMap<Integer, Integer> map0 = new HashMap<>();
        map0.put(1, 40);
        map0.put(3,32);
        HashMap<Integer, Integer> map1 = new HashMap<>();
        map1.put(7, 25);
        map1.put(8,70);

        cont.getApplicationService().addDemandOrder(cont.getLoggedInUser().getBranch(), 4321, new Date(Calendar.getInstance().getTime().getTime()), null, map0);
        cont.getApplicationService().addDemandOrder(cont.getLoggedInUser().getBranch(), 1234, new Date(Calendar.getInstance().getTime().getTime()), null, map1);
        cont.addPurchase(1,   0,1234, 40, 430, 0.5);
        cont.addPurchase(3,   0,1234, 32, 430, 0.5);
        cont.addPurchase(7,   1,4321, 25, 430, 0.5);
        cont.addPurchase(8,   1,4321, 70, 430, 0.5);
    }

    @AfterClass
    public static void tearDown() {
        DataBaseCreator db = new DataBaseCreator();
        db.deleteAllTables();
    }

    @Test
    public void getPurchaseReport() {
        assertEquals(1, cont.getPurchaseReport().get(0).getItemID());
        assertEquals(1234, cont.getPurchaseReport().get(1).getSupplierBusinessNumber());
        assertEquals(4321, cont.getPurchaseReport().get(2).getSupplierBusinessNumber());
    }

    @Test
    public void getPurchaseReportByItemId() {
        assertEquals(0, cont.getPurchaseReportByItemID(5).size());
        assertEquals(1, cont.getPurchaseReportByItemID(3).size());
        assertEquals(1, cont.getPurchaseReportByItemID(7).size());
    }

    @Test
    public void getPurchaseReportByDate() {
        assertEquals(4, cont.getPurchaseReportByDate(makeDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))), makeDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))).size());
    }

    @Test
    public void getDamageReport() {
        cont.addDamagedRecord(1,   10, PDamageReason.Damaged, 1);
        cont.addDamagedRecord(3,   10, PDamageReason.Damaged, 1);
        cont.addDamagedRecord(7,   10, PDamageReason.Damaged, 1);
        cont.addDamagedRecord(8,   10, PDamageReason.Expired, 1);
        assertEquals(6, cont.getDamagedReport().size());
        assertEquals(2, cont.getOnlyExpiredReport().size());
        assertEquals(4, cont.getOnlyDamagedReport().size());
    }

    private LocalDate makeDate(String DateStr) {
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(DateStr, formatter);
        }catch (Exception e){
            System.out.println("The entered date was not in the format - dd/MM/yyyy");
        }
        return null;
    }
}