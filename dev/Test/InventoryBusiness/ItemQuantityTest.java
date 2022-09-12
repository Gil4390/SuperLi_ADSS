package InventoryBusiness;

import BusinessLayer.InventoryBusiness.Item;
import BusinessLayer.InventoryBusiness.ItemController;
import DataAccessLayer.DataBaseCreator;
import Presentation.Model.BackendController;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
public class ItemQuantityTest {
    //private static boolean setUpIsDone = false;
    private static BackendController controller;
    private static ItemController itemController;

    private static String branchAddress;
    @BeforeClass
    public static void setUp() throws Exception {
        DataBaseCreator dataBaseCreator = new DataBaseCreator();
        dataBaseCreator.CreateAllTables();
        controller = BackendController.getInstance();
        itemController = controller.getApplicationService().getInventoryService().getItemController();
        controller.loadDataForTests();
        branchAddress = "Rehov 2, Haifa";
    }

    @AfterClass
    public static void tearDown() {
        DataBaseCreator db = new DataBaseCreator();
        db.deleteAllTables();
    }

    @org.junit.Test
    public void addAmountToBack() {
        Item bigBamba = itemController.getItem(branchAddress, 1);
        bigBamba.getQuantity().setAmountInBackRoom(20, bigBamba.getItemID());
        bigBamba.getQuantity().setAmountInFrontRoom(10, bigBamba.getItemID());
        bigBamba.getQuantity().addAmountToBack(20, bigBamba.getItemID());
        Assert.assertEquals(40, bigBamba.getQuantity().getAmountInBackRoom());
        Assert.assertEquals(10, bigBamba.getQuantity().getAmountInFrontRoom());
        Assert.assertThrows(IllegalArgumentException.class, () -> bigBamba.getQuantity().addAmountToBack(-100, bigBamba.getItemID()));
    }

    @org.junit.Test
    public void removeAmountFromFront() {
        Item bigBamba = itemController.getItem(branchAddress, 1);
        bigBamba.getQuantity().setAmountInBackRoom(100, bigBamba.getItemID());
        bigBamba.getQuantity().setAmountInFrontRoom(50, bigBamba.getItemID());
        Assert.assertThrows(IllegalArgumentException.class, () -> bigBamba.getQuantity().removeAmountFromBack(-100, bigBamba.getItemID()));
        Assert.assertThrows(IllegalArgumentException.class, () -> bigBamba.getQuantity().removeAmountFromBack(101, bigBamba.getItemID()));
        bigBamba.getQuantity().removeAmountFromBack(49, bigBamba.getItemID());
        Assert.assertEquals(51, bigBamba.getQuantity().getAmountInBackRoom());
    }

    @org.junit.Test
    public void moveItemsBetweenRooms() {
        Item bigBamba = itemController.getItem(branchAddress, 1);
        bigBamba.getQuantity().setAmountInBackRoom(100, bigBamba.getItemID());
        bigBamba.getQuantity().setAmountInFrontRoom(50, bigBamba.getItemID());
        Assert.assertThrows(IllegalArgumentException.class, () -> bigBamba.getQuantity().moveItemsBetweenRooms(true, -100, bigBamba.getItemID()));
        bigBamba.getQuantity().moveItemsBetweenRooms(true, 50, bigBamba.getItemID());
        Assert.assertEquals(50, bigBamba.getQuantity().getAmountInBackRoom());
        Assert.assertEquals(100, bigBamba.getQuantity().getAmountInFrontRoom());
        Assert.assertThrows(IllegalArgumentException.class, () -> bigBamba.getQuantity().moveItemsBetweenRooms(true, 150, bigBamba.getItemID()));
    }

    @org.junit.Test
    public void isInShortage() {
        Item bigBamba = itemController.getItem(branchAddress, 1);
        bigBamba.getQuantity().setAmountInBackRoom(100, bigBamba.getItemID());
        bigBamba.getQuantity().setAmountInFrontRoom(50, bigBamba.getItemID());
        Assert.assertFalse(bigBamba.getQuantity().isInShortage());
        bigBamba.getQuantity().removeAmountFromBack(90, bigBamba.getItemID());
        bigBamba.getQuantity().removeAmountFromFront(50, bigBamba.getItemID());
        Assert.assertTrue(bigBamba.getQuantity().isInShortage());
        bigBamba.getQuantity().addAmountToBack(1000, bigBamba.getItemID());
        bigBamba.getQuantity().addAmountToFront(200, bigBamba.getItemID());
        Assert.assertFalse(bigBamba.getQuantity().isInShortage());
    }
}