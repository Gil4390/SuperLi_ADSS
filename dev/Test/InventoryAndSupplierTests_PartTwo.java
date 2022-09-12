import BusinessLayer.ApplicationFacade;
import BusinessLayer.InventoryBusiness.Item;
import BusinessLayer.InventoryBusiness.ItemController;
import BusinessLayer.InventoryBusiness.ReportsMaker;
import BusinessLayer.SupplierBusiness.DemandOrder;
import BusinessLayer.SupplierBusiness.SupplierFacade;
import DataAccessLayer.InventoryDAL.DALFacade;
import DataAccessLayer.DataBaseCreator;
import DataAccessLayer.SupplierDAL.*;
import Presentation.Model.BackendController;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class InventoryAndSupplierTests_PartTwo {
    private static BackendController cont;
    private static ApplicationFacade app;
    private static SupplierFacade supFac;
    private static ItemController itemCont;
    private static ReportsMaker repMaker;

    @org.junit.BeforeClass
    public static void setUp() throws Exception {
        loadData();
        BackendController.getInstance().findJobEmployee("300", "warehouse");
        BackendController.getInstance().enterBranch("Yasmin 3, Dimona");
    }

    @org.junit.Test
    public void supplyDemandOrderTest(){
        itemCont.removeQuantityFromItem("Yasmin 3, Dimona", 1, cont.getItem(1).getQuantityBackRoom(), true);
        HashMap<Integer, Integer> hm = new HashMap<>();
        hm.put(1, 10);
        supFac.addDemandOrder("Yasmin 3, Dimona", 4321, Date.valueOf("2022-05-16"), null, hm);
        app.supplyDemandOrder(4321, DemandOrderMapper.getInstance().getLastOrderId(), new HashMap<>());
        Assert.assertEquals(10,  cont.getItem(1).getQuantityBackRoom(), 0);
    }

    @org.junit.Test
    public void supplyTwiceSameDemandOrderTest(){
        cont.removeQuantityFromItem(2, true, cont.getItem(2).getQuantityBackRoom());
        HashMap<Integer, Integer> hm = new HashMap<>();
        hm.put(2, 10);
        supFac.addDemandOrder("Yasmin 3, Dimona", 4321, Date.valueOf("2022-05-17"), null, hm);
        int lastOrder = DemandOrderMapper.getInstance().getLastOrderId();
        app.supplyDemandOrder(4321, lastOrder, new HashMap<>());
        app.supplyDemandOrder(4321, lastOrder, new HashMap<>());
        Assert.assertEquals(10,  itemCont.getItem("Yasmin 3, Dimona",2).getQuantity().getAmountInBackRoom(), 0);
    }

    @org.junit.Test
    public void updateOrderTest(){
        HashMap<Integer, Integer> hm = new HashMap<>();
        hm.put(2, 10);
        supFac.addDemandOrder("Yasmin 3, Dimona", 4321, Date.valueOf("2022-05-16"), Date.valueOf("2023-05-16"), hm);
        int lastOrder = DemandOrderMapper.getInstance().getLastOrderId();
        hm.put(2, 20);
        supFac.updateOrderProductsOfDemandOrder(4321, lastOrder, hm);
        DemandOrder order = DemandOrderMapper.getInstance().getDemandOrder(4321, DemandOrderMapper.getInstance().getLastOrderId());
        Assert.assertEquals(20,order.getOrderProducts().get(2).getAmount() , 0);
//        Assert.assertEquals(10,orderProducts.get(3).getAmount() , 0);
    }

    @org.junit.Test
    public void shortageOrderTest(){
        cont.removeQuantityFromItem(3, true, cont.getItem(3).getQuantityBackRoom());
        cont.removeQuantityFromItem(3, false, cont.getItem(3).getQuantityFrontRoom());
        app.orderShortage("Yasmin 3, Dimona");
        int lastId = DemandOrderMapper.getInstance().getLastOrderId();
        DemandOrder order = DemandOrderMapper.getInstance().getDemandOrder(4321,lastId);
        int amount = order.getOrderProducts().get(3).getAmount();
        Assert.assertEquals(50, amount, 0);
    }

    @org.junit.Test
    public void cheapestShortageOrderTest(){
        cont.removeQuantityFromItem(4, true, cont.getItem(4).getQuantityBackRoom());
        cont.removeQuantityFromItem(4, false, cont.getItem(4).getQuantityFrontRoom());
        Set<DayOfWeek> dayOfWeekSet = new HashSet<>();
        dayOfWeekSet.add(DayOfWeek.MONDAY);
        supFac.addSupplier("test", 1111, 1111, true, "CreditCard", dayOfWeekSet, "30 yaakov st.", "South");
        supFac.addItem("Yasmin 3, Dimona", 1111, 4, 1113, 1, "Bisli Small");
        app.orderShortage("Yasmin 3, Dimona");
        int lastId = DemandOrderMapper.getInstance().getLastOrderId();
        DemandOrder order = DemandOrderMapper.getInstance().getDemandOrder(1111,lastId);
        int amount = order.getOrderProducts().get(4).getAmount();
        Assert.assertEquals(50, amount, 0);
        Assert.assertEquals(1111, order.getSupplierBN(), 0);
    }


    @org.junit.Test
    public void shortageOrderTestWhenOrderExists(){
        cont.removeQuantityFromItem(5, true, cont.getItem(5).getQuantityBackRoom());
        cont.removeQuantityFromItem(5, false, cont.getItem(5).getQuantityFrontRoom());
        HashMap<Integer, Integer> hm = new HashMap<>();
        hm.put(5, 50);
        supFac.addDemandOrder("Yasmin 3, Dimona", 1234, Date.valueOf("2022-05-16"), Date.valueOf("2023-05-16"), hm);
        app.orderShortage("Yasmin 3, Dimona");
        int lastId = DemandOrderMapper.getInstance().getLastOrderId();
        DemandOrder order = DemandOrderMapper.getInstance().getDemandOrder(1234,lastId);
        Assert.assertNotNull(order.getSupplyDate());
    }
    @org.junit.Test
    public void shortageOrderTestWhenPartOrderExists(){
        cont.removeQuantityFromItem(6, true, cont.getItem(6).getQuantityBackRoom());
        cont.removeQuantityFromItem(6, false, cont.getItem(6).getQuantityFrontRoom());
        HashMap<Integer, Integer> hm = new HashMap<>();
        hm.put(6, 10);
        supFac.addDemandOrder("Yasmin 3, Dimona", 1234, Date.valueOf("2022-05-16"), Date.valueOf("2023-05-16"), hm);
        app.orderShortage("Yasmin 3, Dimona");
        int lastId = DemandOrderMapper.getInstance().getLastOrderId();
        DemandOrder order = DemandOrderMapper.getInstance().getDemandOrder(1234,lastId);
        Assert.assertEquals(10, order.getOrderProducts().get(6).getAmount(), 0);
    }

    @org.junit.Test
    public void CreateNextWeekFixedOrders(){
        HashMap<Integer, Integer> hm = new HashMap<>();
        hm.put(7, 10);
        Set<DayOfWeek> dayOfWeekSet = new HashSet<>();
        dayOfWeekSet.add(DayOfWeek.valueOf(LocalDate.now().getDayOfWeek().name()));
        supFac.addFixedOrder("Yasmin 3, Dimona", 1234, Date.valueOf("2022-05-16"),dayOfWeekSet, hm);
        supFac.createNextWeekFixedOrders("Yasmin 3, Dimona");
        int lastId = DemandOrderMapper.getInstance().getLastOrderId();
        DemandOrder order = DemandOrderMapper.getInstance().getDemandOrder(1234,lastId);
        Assert.assertEquals(10, order.getOrderProducts().get(7).getAmount(), 0);
    }

    @org.junit.Test
    public void changeFullQuantityTest(){
        Item smallBamba = itemCont.getItem("Yasmin 3, Dimona", 2);
        cont.changeMinimalQuantity(2, 20);
        cont.changeFullQuantity(2, 30);
        Assert.assertEquals(smallBamba.getQuantity().getFullQuantity(), 30);
        Assert.assertThrows(IllegalArgumentException.class, () -> cont.changeFullQuantity(2, 15));
        Assert.assertThrows(IllegalArgumentException.class, () -> cont.changeFullQuantity(2, 20));
    }

    @Test
    public void addPurchase() {
        HashMap<Integer, Integer> map0 = new HashMap<>();
        map0.put(6, 40);
        map0.put(5,32);
        HashMap<Integer, Integer> map1 = new HashMap<>();
        map1.put(7, 25);
        map1.put(8,70);
        cont.addDemandOrder(1234, new Date(Calendar.getInstance().getTime().getTime()), null, map0);
        cont.addDemandOrder(1234, new Date(Calendar.getInstance().getTime().getTime()), null, map1);
        cont.addPurchase(6,  0, 1234, 40, 430, 0.5);
        cont.addPurchase(5,   0,1234, 32, 430, 0.5);
        assertEquals(2, cont.getPurchaseReport().size());
        cont.addPurchase(7,  1, 1234, 25, 430, 0.5);
        cont.addPurchase(8,   1,1234, 70, 430, 0.5);
        assertEquals(4, cont.getPurchaseReport().size());
    }

    public static void loadData() throws Exception{
        DataBaseCreator dataBaseCreator = new DataBaseCreator();
        dataBaseCreator.CreateAllTables();
        dataBaseCreator.deleteAllTables();
        dataBaseCreator.CreateAllTables();
        DALFacade.clearSingletonForTests();
        BackendController.clearBackendControllerForTests();
        CacheCleaner cacheCleaner = new CacheCleaner(Arrays.asList(SupplierMapper.getInstance(), DemandOrderMapper.getInstance(), FixedOrderMapper.getInstance(), BillOfQuantityMapper.getInstance(), ContactMapper.getInstance(), OrderProductMapper.getInstance()));
        cacheCleaner.cleanForTests();
        cont = BackendController.getInstance();
        cont.loadDataForTests();
        app = cont.getApplicationService().getApplicationFacade();
        supFac = app.getSupplierFacade();
        itemCont = app.getItemController();
        repMaker = app.getReportsMaker();
    }
}
