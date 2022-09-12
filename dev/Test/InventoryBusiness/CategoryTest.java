package InventoryBusiness;

import DataAccessLayer.DataBaseCreator;
import Presentation.Model.BackendController;
import ServiceLayer.Services.InventoryService;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

public class CategoryTest {
    //private static boolean setUpIsDone = false;
    private static DataBaseCreator dataBaseCreator;
    private static BackendController controller;
    private static InventoryService inventoryService;

    private static String branchAddress;

    @BeforeClass
    public static void setUp() throws Exception {
        DataBaseCreator dataBaseCreator = new DataBaseCreator();
        dataBaseCreator.CreateAllTables();
        dataBaseCreator.deleteAllTables();
        dataBaseCreator.CreateAllTables();
        controller = BackendController.getInstance();
        inventoryService = controller.getApplicationService().getInventoryService();
        controller.loadDataForTests();
        branchAddress = "Rehov 2, Haifa";
    }

    @AfterClass
    public static void tearDown() {
        DataBaseCreator db = new DataBaseCreator();
        db.deleteAllTables();
    }

    @org.junit.Test
    public void getItemIDList() {
        Assert.assertEquals(2, inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1#1").getItemIDList().size());
        Assert.assertEquals(2, inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1#2").getItemIDList().size());
        Assert.assertEquals(6, (long)inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1#2").getItemIDList().get(0));
    }

    @org.junit.Test
    public void addSubCat() {
        Assert.assertEquals(3, inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1").getSubCatList().size());
        Assert.assertEquals(3, inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#2").getSubCatList().size());
        Assert.assertEquals(0, inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1#3").getSubCatList().size());
    }

    @org.junit.Test
    public void setFatherCategory() {
        Assert.assertEquals(3, inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1").getSubCatList().size());
        Assert.assertEquals(inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1"), inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1#2").getFatherCategory());
        Assert.assertEquals(inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1"), inventoryService.getItemController().getCategoryTree(branchAddress).getCategory(branchAddress, "0#1#1#3").getFatherCategory());
    }
}

